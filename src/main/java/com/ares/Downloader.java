package com.ares;

import com.ares.entity.Actress;
import com.ares.http.AppConstants;
import com.ares.http.ArtworkApi;
import com.ares.http.RetrofitServiceManager;
import io.reactivex.Scheduler;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

public class Downloader {


    public static void main(String args[]){

        ArtworkApi artworkApi= RetrofitServiceManager.getManager().create(AppConstants.URL.ARTWORK_URL,ArtworkApi.class);

        artworkApi.getActressList(1).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<List<Actress>>() {
                    @Override
                    public void accept(List<Actress> actresses) throws Exception {


                        System.out.println("actresses.size = "+actresses.size());

                       ExecutorService service =  Executors.newFixedThreadPool(actresses.size());

                       final String path = "/Users/yuwenque/Downloads/actresses";
                       File rootFile = new File(path);
                        System.out.println("rootFile .exist = "+ rootFile.exists());
                        for (Actress actress : actresses) {
                            service.submit(new Runnable() {
                                @Override
                                public void run() {


                                    Downloader.downloadPicture(actress.getAvatar(),path+"/"+actress.getName()+".jpg");
                                }
                            });
                        }

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

        try {
            Thread.sleep(200000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //链接url下载图片
    private static void downloadPicture(String imageUrl,String filePath) {
        URL url = null;

        try {
            url = new URL(imageUrl);
            DataInputStream dataInputStream = new DataInputStream(url.openStream());

            FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath));
            ByteArrayOutputStream output = new ByteArrayOutputStream();

            byte[] buffer = new byte[1024];
            int length;

            while ((length = dataInputStream.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            fileOutputStream.write(output.toByteArray());
            dataInputStream.close();
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
