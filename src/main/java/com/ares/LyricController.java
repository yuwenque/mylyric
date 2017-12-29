package com.ares;

import com.ares.entity.LyricEntity;
import com.ares.entity.SingerEntity;
import com.ares.entity.SongEmotionEntity;
import com.ares.entity.SongEntity;
import com.ares.http.LexicalAnalysisResult;
import com.ares.http.RetrofitServiceManager;
import com.ares.http.TextSentimentResult;
import com.ares.service.LyricService;
import com.ares.service.SingerService;
import com.ares.service.SongEmotionService;
import com.ares.service.SongService;
import com.google.gson.Gson;
import com.gzkit.jaguar.core.beans.DefaultResultBean;
import com.qcloud.Module.Base;
import com.qcloud.QcloudApiModuleCenter;
import io.reactivex.Flowable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import javafx.util.Pair;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Created by ares on 2017/12/21.
 */
@RestController
@RequestMapping("/lyric")
public class LyricController {


    @Autowired
    SongService songService;

    @Autowired
    SongEmotionService emotionService;

    @Autowired
    LyricService lyricService;

    @Autowired
    SingerService singerService;

    @Value(value = "${qcloud.api.secretId}")
    private String secretId;


    @Value(value = "${qcloud.api.secretKey}")
    private String secretKey;


    private int songCount = 0;
    private List<Song> songs = new ArrayList<>();


    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private String currentSingerName;

    @RequestMapping("/getSingerSongEmotionList")
    public List<LyricEmotion> searchSinger(@RequestParam(value = "singer") String singer) {

        songs.clear();

        currentSingerName = singer;
        List<LyricEmotion> resultList = new ArrayList<>();

        getSong(singer).flatMap(song -> {


            File rootFile = new File("/Users/ares/Documents/MyLyricCollection/singer/" + singer);
            if (!rootFile.exists()) {
                rootFile.mkdirs();
            }
            File lyricFile = new File("/Users/ares/Documents/MyLyricCollection/singer/" + singer + "/" + song.getName() + ".txt");

            if (lyricFile.exists()) {
                lyricFile.delete();
            }
            OutputStream outputStream = new FileOutputStream(lyricFile);

            outputStream.write(song.getLyricContent().getBytes());
            outputStream.close();

            Gson gson = new Gson();


            SongEmotionEntity songEmotionEntity = emotionService.findSongEmotionBySongId(song.getSongId());
            if (songEmotionEntity != null) {


                return Flowable.just(songEmotionEntity)
                        .map(new Function<SongEmotionEntity, LyricEmotion>() {
                            @Override
                            public LyricEmotion apply(@NonNull SongEmotionEntity songEmotionEntity) throws Exception {
                                LyricEmotion emotion
                                        = new LyricEmotion();
                                emotion.setSongId(song.getSongId());
                                emotion.setSongName(songEmotionEntity.getSongName());
                                TextSentimentResult result = new TextSentimentResult();
                                result.setPositive(songEmotionEntity.getPositive());
                                result.setNegative(songEmotionEntity.getNegative());
                                emotion.setEmotion(result);
                                return emotion;
                            }
                        });

            }

            String result = doAnalysisLyricEmotion(song.lyricContent);

            TextSentimentResult lexicalAnalysisResult = gson.fromJson(result, TextSentimentResult.class);

            LyricEmotion emotion
                    = new LyricEmotion();
            emotion.setSongName(song.getName());
            emotion.setEmotion(lexicalAnalysisResult);
            if ((lexicalAnalysisResult.getNegative() != 0 && lexicalAnalysisResult.getNegative() != 1) && (lexicalAnalysisResult.getPositive() != 0 && lexicalAnalysisResult.getPositive() != 1)) {
                emotionService.insertSongEmotion(song.getSongId(), song.getName(), lexicalAnalysisResult.getPositive(), lexicalAnalysisResult.getNegative());
            }


            return Flowable.just(emotion);

        }).subscribe((emotion -> {


            if ((emotion.getEmotion().getNegative() != 0 && emotion.getEmotion().getNegative() != 1) && (emotion.getEmotion().getPositive() != 0 && emotion.getEmotion().getPositive() != 1)) {

                resultList.add(emotion);
            }
            if (emotion.getSongName() != null && emotion.getSongName().length() > 0) {

                HSSFRow row = sheet.createRow(songCount + 1);
                HSSFCell cell = row.createCell(0);
                HSSFCell cell2 = row.createCell(1);
                HSSFCell cell3 = row.createCell(2);

                cell.setCellValue(emotion.getSongName());

                BigDecimal bd = new BigDecimal(emotion.getEmotion().getPositive() * 100);
                BigDecimal bd2 = new BigDecimal(emotion.getEmotion().getNegative() * 100);
                double positive = bd.setScale(2, 4).doubleValue();
                double negative = bd2.setScale(2, 4).doubleValue();
                cell2.setCellValue(positive + "%");
                cell3.setCellValue(negative + "%");

            }


            songCount++;


        }), (throwable -> throwable.printStackTrace()
        ), () -> {
            System.out.println("循环完毕");

            File file = new File("/Users/ares/Documents/MyLyricCollection/" + currentSingerName + "网易云音乐top50歌词情感分析.xls");
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream fileOut = new FileOutputStream("/Users/ares/Documents/MyLyricCollection/" + currentSingerName + "网易云音乐top50歌词情感分析.xls");

            wb.write(fileOut);
            fileOut.close();

        });


        return resultList;

    }


    @RequestMapping("/api/search")
    public List<LyricEmotion> apiSearchSinger(@RequestParam(value = "singer") String singer) {


        List<LyricEmotion> resultList = new ArrayList<>();

        getSong(singer).flatMap(song -> {


            Gson gson = new Gson();

            String result = doAnalysisLyricEmotion(song.lyricContent);

            TextSentimentResult lexicalAnalysisResult = gson.fromJson(result, TextSentimentResult.class);

            LyricEmotion emotion
                    = new LyricEmotion();
            emotion.setSongId(song.getSongId());
            emotion.setSongName(song.getName());
            emotion.setEmotion(lexicalAnalysisResult);

            return Flowable.just(emotion);

        }).subscribe((emotion -> {

            resultList.add(emotion);


            songCount++;


        }), (throwable -> throwable.printStackTrace()
        ), () -> {


        });


        return resultList;

    }


    @RequestMapping("/getLyricKeyword")
    public void getLyricKeyword(@RequestParam(value = "singer") String singer) {
        currentSingerName = singer;
        final StringBuffer stringBuffer = new StringBuffer();
        Map<String, Integer> mapOfKeywordCount = new HashMap<>();
        File rootFile = new File("/Users/ares/Documents/MyLyricCollection/singer/" + singer + "/keyword");
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        File keywordFile = new File("/Users/ares/Documents/MyLyricCollection/singer/" + singer + "/keyword/" + singer + ".txt");

        if (keywordFile.exists()) {
            keywordFile.delete();
        }
        getSong(singer).flatMap(song -> {

            Gson gson = new Gson();

            String result = lexicalAnalysisLyric(song.getLyricContent());
            LexicalAnalysisResult keywordToken = gson.fromJson(result, LexicalAnalysisResult.class);

            if (keywordToken == null) {
                keywordToken = new LexicalAnalysisResult();
                keywordToken.setCombtokens(new ArrayList<>());
            }

            return Flowable.just(keywordToken);

        }).subscribe(lexicalAnalysisResult -> {

            Map<String, Integer> map = new HashMap<>();
            List<LexicalAnalysisResult.CombtokensBean> list = lexicalAnalysisResult.getCombtokens();


            for (LexicalAnalysisResult.CombtokensBean combtokensBean : list) {

                if (combtokensBean != null && !"人名".equals(combtokensBean.getCls())) {

                    // stringBuffer.append(combtokensBean.getWord()).append(",");

                    if (!map.containsKey(combtokensBean.getWord())) {
                        map.put(combtokensBean.getWord(), 1);
                        if (mapOfKeywordCount.containsKey(combtokensBean.getWord())) {
                            mapOfKeywordCount.put(combtokensBean.getWord(), mapOfKeywordCount.get(combtokensBean.getWord()) + 1);
                        } else {
                            mapOfKeywordCount.put(combtokensBean.getWord(), 1);
                        }

                    }
                }

            }


        }, throwable -> throwable.printStackTrace(), () -> {

            System.out.println("................the end .................");
            FileOutputStream fileOutputStream = new FileOutputStream(keywordFile);

            List<Temp> tempList = new ArrayList<>();
            mapOfKeywordCount.forEach(new BiConsumer<String, Integer>() {
                @Override
                public void accept(String keyword, Integer integer) {

                    // stringBuffer.append(keyword+",出现次数"+integer+"\n");


                    tempList.add(new Temp(keyword, integer));
                }
            });

            for (Temp temp1 : sortList(tempList)) {

                stringBuffer.append(temp1.getKeyword() + ",出现次数" + temp1.getCount() + "\n");

            }


            fileOutputStream.write(stringBuffer.toString().getBytes());
            fileOutputStream.close();

        });

    }

    private List<Temp> sortList(List<Temp> list) {


        for (int i = 0; i < list.size(); i++) {

            for (int j = 0; j < list.size() - i - 1; j++) {

                if (list.get(j).getCount() < list.get(j + 1).getCount()) {

                    Collections.swap(list, j, j + 1);
                }

            }
        }

        return list;

    }

    private Temp temp;

    private class Temp {

        public Temp() {
        }

        public Temp(String keyword, Integer count) {
            this.keyword = keyword;
            this.count = count;
        }

        private String keyword;
        private Integer count = 0;

        public String getKeyword() {
            return keyword;
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }
    }


    @RequestMapping("/lexicalAnalysis")
    public String lexicalAnalysisLyric(@RequestParam(value = "text") String text) {

        //TreeMap<String,Object> params  = QcloudUtil.createQCloudMap();
        TreeMap<String, Object> params = new TreeMap<>();
        //  params.put("Action","TextKeywords");
        params.put("text", text);
        params.put("code", 0x00200000);
        params.put("type", 1);


        TreeMap<String, Object> config = getCommonParams("GET");


        QcloudApiModuleCenter module = new QcloudApiModuleCenter(new HostCenter(), config);

        System.out.println(module.generateUrl("LexicalAnalysis", params));
        try {
            return module.call("LexicalAnalysis", params);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";

    }


    @RequestMapping("/api/songs")
    public List<Song> findTheSongOfThisSinger(String singer) {
        List<Song> list = new ArrayList<>();

        getSong(singer).subscribe(new Consumer<Song>() {
            @Override
            public void accept(Song song) throws Exception {
                list.add(song);
            }
        });

        return list;
    }


    private Flowable<Song> getSong(String singer) {


        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("s", singer);
        //搜索歌手
        objectMap.put("type", 100);


        SingerEntity singerEntity1 = singerService.getSingerByName(singer);
        Flowable<String> flowable;
        if (singerEntity1 != null && singerEntity1.getId() != null) {

            flowable = Flowable.just(singerEntity1.getId());

        } else {
            flowable = RetrofitServiceManager.getManager().create(LyricApi.class)
                    .search("http://music.163.com/api/search/pc", objectMap).flatMap(searchResult -> {


                        List<SearchResult.ResultBean.ArtistsBean> artistsBeanList = searchResult.getResult().getArtists();
                        int currentSingerId = 19020;//默认值
                        if (artistsBeanList != null && artistsBeanList.size() > 0) {
                            SearchResult.ResultBean.ArtistsBean artistsBean = artistsBeanList.get(0);
                            currentSingerId = artistsBeanList.get(0).getId();
                            SingerEntity singerEntity = singerService.getSingerById(currentSingerId + "");
                            if (singerEntity == null) {
                                singerEntity = new SingerEntity();
                                singerEntity.setId(artistsBean.getId() + "");
                                singerEntity.setAlbumSize(artistsBean.getAlbumSize());
                                singerEntity.setFollowed(artistsBean.isFollowed());
                                singerEntity.setImg1v1Url(artistsBean.getImg1v1Url());
                                singerEntity.setMvSize(artistsBean.getMvSize());
                                singerEntity.setName(artistsBean.getName());
                                singerEntity.setPicId(artistsBean.getPicId());
                                singerEntity.setPicUrl(artistsBean.getPicUrl());

                                singerService.insertSinger(singerEntity);


                            }

                        }

                        return Flowable.just(currentSingerId + "");
                    });
        }

        return flowable.flatMap(new Function<String, Publisher<List<Song>>>() {
            @Override
            public Publisher<List<Song>> apply(@NonNull String singerId) throws Exception {

                wb = new HSSFWorkbook();
                sheet = wb.createSheet("表1");

                HSSFRow row = sheet.createRow(0);
                HSSFCell cell = row.createCell(0);
                HSSFCell cell2 = row.createCell(1);
                HSSFCell cell3 = row.createCell(2);

                cell.setCellValue("歌名");
                cell2.setCellValue("积极占比%");
                cell3.setCellValue("消极占比%");

                List<SongEntity> songListFromDb = songService.findSongList(singerId);

                if (songListFromDb != null && songListFromDb.size() > 0) {

                    List<Song> songList = new ArrayList<>();
                    for (SongEntity songEntity : songListFromDb) {

                        songList.add(new Song(songEntity.getSongName(), songEntity.getSongId()));
                    }
                    return Flowable.just(songList);

                }

                final List<Song> streamSongs = new ArrayList<>();
                Jsoup.connect("http://music.163.com/artist?id=" + singerId).timeout(10000)
                        .header("Referer", "http://music.163.com/")
                        .header("Host", "music.163.com").get().select("ul[class=f-hide] a")
                        .stream()
                        .map(element -> {


                            String href = element.attr("href");
                            String songId = href.substring(href.indexOf("id=") + 3, href.length());
                            //插入数据库
                            songService.insertSong(songId, element.text(), singerId);
                            Song song = new Song(element.text(), songId);
                            return song;
                        }).forEach(new java.util.function.Consumer<Song>() {
                    @Override
                    public void accept(Song song) {
                        streamSongs.add(song);

                    }
                });
                return Flowable.just(streamSongs);
            }
        })
                .flatMap(songStream -> {
//                            List<Song> listSong = new ArrayList<>();
//
//                            songStream.forEach(song -> listSong.add(song));


                            songs.addAll(songStream);

                            return Flowable.fromIterable(songs);
                        }
                ).flatMap(song -> {


                    return getSongLyric(song.getSongId()).flatMap(new Function<LyricEntity, Publisher<Pair<Song, Lyric>>>() {
                        @Override
                        public Publisher<Pair<Song, Lyric>> apply(@NonNull LyricEntity lyricEntity) throws Exception {
                            Lyric lyric = new Lyric();
                            Lyric.LrcBean bean = new Lyric.LrcBean();
                            bean.setLyric(lyricEntity.getLyricContent());
                            lyric.setLrc(bean);
                            Pair<Song, Lyric> pair = new Pair<>(song, lyric);
                            return Flowable.just(pair);
                        }
                    });

                    //从数据库查询
//                    LyricEntity lyricEntity = lyricService.findLyricBySongId(song.getSongId());
//                    if (lyricEntity != null) {
//                        Lyric lyric = new Lyric();
//                        Lyric.LrcBean bean = new Lyric.LrcBean();
//                        bean.setLyric(lyricEntity.getLyricContent());
//                        lyric.setLrc(bean);
//
//                        return Flowable.just(new Pair<>(song, lyric));
//                    }
//                    Map<String, Object> map = new HashMap<>();
//                    map.put("id", song.getSongId());
//                    map.put("os", "pc");
//                    map.put("lv", 0);
//
//
//                    return RetrofitServiceManager.getManager().create(LyricApi.class).getSongLyric("http://music.163.com/api/song/lyric", map)
//                            .flatMap(new Function<Lyric, Publisher<Pair<Song, Lyric>>>() {
//                                @Override
//                                public Publisher<Pair<Song, Lyric>> apply(@NonNull Lyric lyric) throws Exception {
//                                    Pair<Song, Lyric> pair = new Pair<>(song, lyric);
//
//                                    return Flowable.just(pair);
//                                }
//                            });

                }).map(songLyricPair -> {

                    try {
                        Song songOri = songLyricPair.getKey();
                        Song song = new Song(songOri.getName(), songOri.getSongId());
                        song.setLyricContent(handleTheLyricContent(songLyricPair.getValue().getLrc().getLyric()));
                        //插入数据库
                        if (song.getLyricContent() != null && song.getLyricContent().length() > 0) {

                            lyricService.insertLyric(song.getSongId(), song.getName(), song.getLyricContent());
                        }
                        return song;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return new Song();

                });

    }


    @RequestMapping("/search")
    public DefaultResultBean<LyricEntity> getSongLyricWithId(@RequestParam("songId") String songId) {


        DefaultResultBean<LyricEntity> bean = new DefaultResultBean<>();

        getSongLyric(songId).subscribe(lyricEntity -> bean.buildSuccessResult(lyricEntity));


        return bean;

    }


    private Flowable<LyricEntity> getSongLyric(String songId) {

        LyricEntity lyricEntity = lyricService.findLyricBySongId(songId);

        if (lyricEntity != null) {

            return Flowable.just(lyricEntity);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", songId);
        map.put("os", "pc");
        map.put("lv", 0);

        return RetrofitServiceManager.getManager().create(LyricApi.class).getSongLyric("http://music.163.com/api/song/lyric", map)

                .flatMap(new Function<Lyric, Publisher<LyricEntity>>() {
                    @Override
                    public Publisher<LyricEntity> apply(@NonNull Lyric lyric) throws Exception {
                        LyricEntity lyricEntity = new LyricEntity();
                        lyricEntity.setSongName("");
                        lyricEntity.setSongId(songId);
                        lyricEntity.setLyricContent(lyric.getLrc().getLyric());

                        return Flowable.just(lyricEntity);
                    }
                })
                ;

    }


    /**
     * 去除歌词里面的一些无用信息
     *
     * @param lyricContent
     * @return
     */
    static String handleTheLyricContent(String lyricContent) {


        if (lyricContent != null) {

            String[] c = lyricContent.split("\n");
            StringBuffer sb = new StringBuffer();

            for (String s : c) {

                if (s.startsWith("歌词") || s.startsWith("作曲") || s.startsWith("作词") || s.startsWith("歌词")) {
                    sb.append("");
                } else {
                    int end = s.indexOf("]") + 1;
                    sb.append(s.substring(end, s.length())).append("\n");
                }

            }
            lyricContent = sb.toString();

        }
        return lyricContent;
    }

    public class Song {

        public Song() {
        }

        public Song(String name, String songId) {
            this.name = name;
            this.songId = songId;
        }

        private String name = "";
        private String songId = "";

        private String lyricContent = "";

        public String getLyricContent() {
            return lyricContent;
        }

        public void setLyricContent(String lyricContent) {
            this.lyricContent = lyricContent;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSongId() {
            return songId;
        }

        public void setSongId(String songId) {
            this.songId = songId;
        }
    }


    @RequestMapping("/analysis/emotion")
    public String doAnalysisLyricEmotion(@RequestParam(value = "content") String content) {

        //TreeMap<String,Object> params  = QcloudUtil.createQCloudMap();
        TreeMap<String, Object> params = new TreeMap<>();
        params.put("Action", "TextSentiment");
        params.put("content", content);


        TreeMap<String, Object> config = getCommonParams("GET");

        QcloudApiModuleCenter module = new QcloudApiModuleCenter(new HostCenter(), config);

        System.out.println(module.generateUrl("TextSentiment", params));
        try {
            return module.call("TextSentiment", params);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    private TextSentimentResult mTextSentimentResult;

    @RequestMapping("/analysis/song")
    public TextSentimentResult getTheAnalysisResult(@RequestParam(value = "songId") String songId, @RequestParam(value = "songName") String songName) {


        //如果数据库有
        SongEmotionEntity emotionEntity = emotionService.findSongEmotionBySongId(songId);
        if (emotionEntity != null) {
            mTextSentimentResult = new TextSentimentResult();
            mTextSentimentResult.setPositive(emotionEntity.getPositive());
            mTextSentimentResult.setNegative(emotionEntity.getNegative());
            return mTextSentimentResult;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", songId);
        map.put("os", "pc");
        map.put("lv", 0);

        RetrofitServiceManager.getManager().create(LyricApi.class).getSongLyric("http://music.163.com/api/song/lyric", map)

                .flatMap(new Function<Lyric, Publisher<TextSentimentResult>>() {
                    @Override
                    public Publisher<TextSentimentResult> apply(@NonNull Lyric lyric) throws Exception {


                        String content = doAnalysisLyricEmotion(handleTheLyricContent(lyric.getLrc().getLyric()));
                        Gson gson = new Gson();

                        TextSentimentResult textSentimentResult
                                = gson.fromJson(content, TextSentimentResult.class);

                        return Flowable.just(textSentimentResult);
                    }
                }).subscribe(new Consumer<TextSentimentResult>() {
            @Override
            public void accept(TextSentimentResult textSentimentResult) throws Exception {

                mTextSentimentResult = textSentimentResult;
                if (textSentimentResult.getPositive() != 0 && textSentimentResult.getNegative() != 0) {

                    emotionService.insertSongEmotion(songId, songName, textSentimentResult.getPositive(), textSentimentResult.getNegative());
                }
            }
        });


        return mTextSentimentResult;
    }

    @RequestMapping("/api/getSongListWithId")
    public List<Song> getSongs(@RequestParam("id") String id) {

        List<Song> list = new ArrayList<>();

        List<SongEntity> songListFromDb = songService.findSongList(id);

        if (songListFromDb != null && songListFromDb.size() > 0) {

            List<Song> songList = new ArrayList<>();
            for (SongEntity songEntity : songListFromDb) {

                songList.add(new Song(songEntity.getSongName(), songEntity.getSongId()));
            }
            return songList;

        }

        try {


            Stream<Song> stream = Jsoup.connect("http://music.163.com/artist?id=" + id).timeout(10000)
                    .header("Referer", "http://music.163.com/")
                    .header("Host", "music.163.com")
                    .get().select("ul[class=f-hide] a").stream()
                    .map(new java.util.function.Function<Element, Song>() {

                        @Override
                        public Song apply(Element element) {

                            String href = element.attr("href");
                            String songId = href.substring(href.indexOf("id=") + 3, href.length());
                            return new Song(element.text(), songId);
                        }
                    });

            stream.forEach(new java.util.function.Consumer<Song>() {
                @Override
                public void accept(Song song) {

                    list.add(song);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }


    @RequestMapping("/analysis/keyword")
    public String doAnalysisLyricKeyword(@RequestParam(value = "content") String content, @RequestParam(value = "title") String title) {

        //TreeMap<String,Object> params  = QcloudUtil.createQCloudMap();
        TreeMap<String, Object> params = new TreeMap<>();
        //  params.put("Action","TextKeywords");
        params.put("content", content);
        params.put("title", title);


        TreeMap<String, Object> config = getCommonParams("GET");


        QcloudApiModuleCenter module = new QcloudApiModuleCenter(new HostCenter(), config);

        System.out.println(module.generateUrl("TextKeywords", params));
        try {
            return module.call("TextKeywords", params);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return "";
    }


    private TreeMap<String, Object> getCommonParams(String requestMethos) {

        TreeMap<String, Object> config = new TreeMap<String, Object>();
        config.put("SecretId", secretId);
        config.put("SecretKey", secretKey);
        config.put("RequestMethod", requestMethos);

        return config;

    }


    private class HostCenter extends Base {


        public HostCenter() {
            serverHost = "wenzhi.api.qcloud.com";

        }
    }


}
