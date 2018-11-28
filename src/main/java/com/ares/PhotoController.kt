package com.ares

import com.ares.entity.*
import io.reactivex.Observable
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import retrofit2.http.Path
import retrofit2.http.Query
import java.io.*

import java.net.URL
import java.util.ArrayList
import java.util.concurrent.Executors

@RestController
@RequestMapping("/artwork")
class PhotoController {



    @RequestMapping("/video/{code}")
    fun searchArtWorkVideo(@PathVariable(required = true) code: String): ArrayList<VideoSearchItem>{

        val searchList = ArrayList<VideoSearchItem>()
        val document = Jsoup.connect("https://www.torrentkitty.tv/search/$code").get()
        val trs =   document.getElementsByTag("tr")
         trs.forEach {
             val tds = it.getElementsByTag("td")
             val videoItem = VideoSearchItem()
             tds.forEach {

                 when (it.className()) {
                     "name" -> videoItem.name = it.text()
                     "size" -> videoItem.size = it.text()
                     "date" -> videoItem.date = it.text()
                     "action" -> {

                         val a = it.getElementsByAttributeValueContaining("href", "magnet")
                         if (a.size > 0) {

                             val href = a[0].attr("href")
                             videoItem.magnet = href
                             println("----")
                             println(videoItem)
                             searchList.add(videoItem)
                         }


                     }
                 }


             }


    }
        return searchList

    }



    @RequestMapping("/test")
    fun test():String{

        val document =  Jsoup.connect("https://www.javbus.com/SCOP-321").get()

        document.getElementsByTag("script")
        println(document)
        return document.text()
    }

    @RequestMapping("/test3")
    fun test3():String{

        val document =  Jsoup.connect("https://www.javbus.com/SCOP-321").get()

       val sriEle= document.getElementsByTag("script")
       var tar = sriEle.filter {

            it.getElementsContainingText("gid").size >0

        }
        println(tar)
        return document.text()



    }
    @RequestMapping("/test2")
    fun tes2t():String{

        val document =  Jsoup.connect("https://www.javbus.com/ajax/uncledatoolsbyajax.php?gid=28421388172&lang=zh&img=https://pics.javbus.com/cover/4yov_b.jpg&uc=0&floor=845").get()

        println(document)
        return document.text()



    }

    @RequestMapping("/download/{page}")
    fun download(@PathVariable page:Int):String{

        var list = getActressList(page)

        val pool  =  Executors.newCachedThreadPool()
        File("/opt/actresses/").mkdir()
        list.forEach {

            pool.submit {



                println("download url = ${it.avatar}")
                downloadPicture(it.avatar,"/opt/actresses"+it.name+".jpg")
            }

        }
        return  "done"
    }
    //链接url下载图片
    private fun downloadPicture(imageUrl: String, filePath: String) {
        var url: URL? = null

        try {
            url = URL(imageUrl)
            val dataInputStream = DataInputStream(url.openStream())

            val fileOutputStream = FileOutputStream(File(filePath))
            val output = ByteArrayOutputStream()

            val buffer = ByteArray(1024)
            var length=  dataInputStream.read(buffer)


            while (length>0){
                length = dataInputStream.read(buffer)
                output.write(buffer, 0, length)

            }
            fileOutputStream.write(output.toByteArray())
            dataInputStream.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }


    @RequestMapping("/{code}")
    fun test(@PathVariable(required = true) code:String):MovieSearchItem{

        // code = "TEM-070"

        val movieSearchItem= MovieSearchItem()
        val url = SEARCH_URL.plus(code)

        try {
            val document = Jsoup.connect(url).get()

            val movieElement = document.getElementsByClass("bigImage")[0]


            val urlList = ArrayList<String>()
            val waterFall =  document.getElementsByClass("sample-box")
            println("-----")
            println(waterFall)
            println("-----")
            if(waterFall.isNotEmpty()){

                waterFall.forEach {


                    urlList.add( it.attr("href"))
                }

                movieSearchItem.samplePhotos = urlList
            }


//            println(document)
            movieSearchItem.coverPhotoUrl=  movieElement.attr("href")

            val srcE = movieElement.getElementsByAttribute("src")[0]

            movieSearchItem.title =srcE.attr("title")
            movieSearchItem.code = code

//            val starRootElement = document.getElementsByClass("star-div")[0]
//            val startElements = starRootElement.getElementsByClass("avatar-waterfall")
            val startElements = document.getElementsByClass("avatar-box")


            val starList = ArrayList<ActressSearchItem>()

            startElements.forEach {

                val item = ActressSearchItem()

                item.workListUrl=  it.attr("href")

               val photoEle =  it.getElementsByAttribute("src")[0]
                item.avatar = photoEle.attr("src")
                item.name = photoEle.attr("title")
                item.id = item.avatar?.split("/")?.last()
                starList.add(item)

            }

            val infoEles = document.getElementsByClass("col-md-3 info")[0]

            println("-------infoEles--------")
            println(infoEles)
            println("---------------")
            infoEles.getElementsByTag("p").forEach {

              val spanElements =  it.allElements.filter {

                   it.tagName() == "span"
                }

                println("-------spanElements--------")
                println(spanElements)
                println("---------------")
                if(spanElements.isNotEmpty()){
                    val header = spanElements[0].text().split(":")[0]

                    when(header){

                        "發行日期" -> movieSearchItem.date =it.text().split(":")[1]
                        "長度"-> movieSearchItem.duration = it.text().split(":")[1]

                    }
                }



            }


            movieSearchItem.actresses = starList


            val relateEle = document.getElementById("related-waterfall")
            val relateList = ArrayList<SimpleMovieItem>()
            if(relateEle!=null){

                relateEle.getElementsByClass("movie-box").forEach {


                    val url =   it.attr("href")
                    val title =   it.attr("title")

                    val imgEle=it.allElements.filter {
                        it.tagName() =="img"
                    }
                    if(imgEle.isNotEmpty()){
                        println("-------imgEle--------")


                        val src = imgEle[0].attr("src")
                        println("relate url=$url,title=$title,src=$src")
                        println("---------------")
                        val item = SimpleMovieItem()
                        item.title = title
                        item.url =url
                        item.coverPhotoUrl=src
                        item.code = item.url?.split("/")?.last()

                        relateList.add(item)
                    }

                }
                movieSearchItem.relateArtWorkList = relateList

            }




        }catch (e:IOException){

            e.printStackTrace()
        }


        return movieSearchItem
    }

    @RequestMapping("/main/{page}")
    fun getMainPage(@PathVariable(required = false) page:Int=0): List<BaseSearchItem>{
        val list = ArrayList<BaseSearchItem>()
        val url = if(page==0){
            SEARCH_URL
        }else{
            SEARCH_URL.plus("page/").plus(page)
        }
        println("搜索路径 = $url")
        try {
            val document = Jsoup.connect(url).get()
            println(document)
            list.addAll(getActWorkItems(document))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return list

    }
    @RequestMapping("/search/{keyword}/{page}")
    fun search(@PathVariable keyword: String, @PathVariable page: Int = 1, @RequestParam(name = "type", required = false) type: Int = SEARCH_ARTWORK): List<BaseSearchItem> {

        val list = ArrayList<BaseSearchItem>()
        val url = when (type) {

            SEARCH_ARTWORK -> SEARCH_URL.plus("search/$keyword/$page")
            SEARCH_ARTWORK_UNCENSORED -> SEARCH_URL.plus("uncensored/search/$keyword/$page&type=$type")
            SEARCH_ACTRESS -> SEARCH_URL.plus("searchstar/$keyword/$page")
            else -> SEARCH_URL

        }
        println("搜索路径 = $url")
        try {
            val document = Jsoup.connect(url).get()
            println(document)

            when (type) {

                SEARCH_ARTWORK_UNCENSORED, SEARCH_ARTWORK -> list.addAll(getActWorkItems(document))
                SEARCH_ACTRESS -> list.addAll(getActressItems(document))

                else -> BaseSearchItem()

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return list

    }

    /**
     * 从爬取的数据中获取有效数据，转化为艺术品列表
     * @param document
     */
    private fun getActWorkItems(document: Document): List<ArtWorkItem> {


        val list = ArrayList<ArtWorkItem>()

        val items = document.getElementsByClass("item")

        items.forEach {
            val workItem = ArtWorkItem()
            val box = it.getElementsByClass("movie-box")[0]

            workItem.movieUrl = box.attr("href")

            val photo = box.allElements.find {

                it.className() == "photo-frame"
            }?.allElements?.find {
                it.hasAttr("src")
            }
            workItem.photoUrl = photo?.attr("src")
            workItem.title = photo?.attr("title")

            val content = box.allElements.find {
                it.className() == "photo-info"
            }

            val contentList = content?.getElementsByTag("date")

            workItem.content = content?.getElementsByTag("span")?.first()?.text()
            workItem.code = contentList?.first()?.text()
            workItem.date = contentList?.last()?.text()


            list.add(workItem)


        }

        return list

    }

    private fun getActressItems(document: Document): List<ActressSearchItem> {


        val list = ArrayList<ActressSearchItem>()
        val items = document.getElementsByClass("item")

        items.forEach {
            val item = ActressSearchItem()
            val box = it.getElementsByClass("avatar-box text-center")[0]
            item.workListUrl = box.attr("href")

            val l = item.workListUrl?.split("/")
            item.id =l!![l.size-1]

            val photo = box.allElements.find {
                it.className()=="photo-frame"
            }?.allElements?.find {
                it.hasAttr("src")
            }

            item.avatar = photo?.attr("src")
            item.name = photo?.attr("title")

            list.add(item)
        }


        return list

    }

    companion object {

        const val SEARCH_URL = "https://www.javbus.com/"

        //模糊
        const val SEARCH_ARTWORK = 0
        //高清
        const val SEARCH_ARTWORK_UNCENSORED = 1
        //艺术家
        const val SEARCH_ACTRESS = 2
    }

    @RequestMapping("/actressInfo")
    fun getActressInfo(@RequestParam(name = "id") id: String): ActressDetail {


        return getActWorkList(SEARCH_URL.plus("star/$id"))
    }



    @RequestMapping("/actress")
    fun getArtworkListOfActress(@RequestParam(name = "id") id: String,@RequestParam("page")page:Int):List<ArtWorkItem>{
        val list = ArrayList<ArtWorkItem>()

        val redirectUrl = SEARCH_URL.plus("star/$id/$page")
        lateinit var document:Document

        try{
             document = Jsoup.connect(redirectUrl).get()

        }catch (e:Exception){
            e.printStackTrace()

            return list
        }


        val boxes =  document.getElementsByClass("movie-box")
        println(boxes)
        boxes.forEach {


            val workItem =ArtWorkItem()
            workItem.movieUrl = it.attr("href")

            val photo = it.allElements.find {

                it.className() == "photo-frame"
            }?.allElements?.find {
                it.hasAttr("src")
            }
            workItem.photoUrl = photo?.attr("src")
            workItem.title = photo?.attr("title")

            val content = it.allElements.find {
                it.className() == "photo-info"
            }

            val contentList = content?.getElementsByTag("date")

            workItem.content = content?.getElementsByTag("span")?.first()?.text()
            workItem.code = contentList?.first()?.text()
            workItem.date = contentList?.last()?.text()

            println(workItem)

            list.add(workItem)




        }



        return list


    }


    fun getActWorkList(@RequestParam(name = "redirectUrl") redirectUrl: String): ActressDetail {

        if (redirectUrl.isEmpty()) {

            throw RuntimeException("redirectUrl参数不能为空！！！")
        }
        println("url = $redirectUrl")
        val actressDetail = ActressDetail()
        try {
            val document = Jsoup.connect(redirectUrl).get()

            val photoElements = document.getElementsByClass("avatar-box")
            println("---avatar-box---")
            println(photoElements)
            println("------")

            val nameAndUrl = photoElements[0].allElements.filter {

                it.className() == "photo-frame"
            }[0].allElements[0].getElementsByAttribute("src")[0]

            println("---photoElements---")
            println(nameAndUrl)
            println("------")
            actressDetail.name = nameAndUrl.attr("title")
            actressDetail.avatar = nameAndUrl.attr("src")
            val topElement = document.getElementsByClass("photo-info")

            val infoElement = topElement[0]


//            println(topElement)
            infoElement.allElements.filter {

                it.className() != "pb10"
            }.map {
                it.text().split(": ")
            }.forEach {
                when (it[0]) {

                    "生日" -> actressDetail.birthday = it[1]
                    "年齡" -> {


                        actressDetail.age = it[1].toInt()

                    }
                    "罩杯"->actressDetail.cup=it[1]
                    "胸圍" -> actressDetail.chestWidth = it[1]
                    "身高" -> actressDetail.stature = it[1]
                    "腰圍" -> actressDetail.waistline = it[1]
                    "臀圍" -> actressDetail.hipline = it[1]
                    "出生地" -> actressDetail.home = it[1]
                    "愛好" -> actressDetail.hobby = it[1]
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }


        println(actressDetail)
        return actressDetail
    }

    @RequestMapping("/actresses/{page}")
    fun getActressList(@PathVariable page: Int): List<Actress> {


        val url = "https://www.javbus.com/actresses/$page"
        val list = ArrayList<Actress>()

        try {
            val document = Jsoup.connect(url).get()

            val elements = document.getElementsByClass("item")


            for (element in elements) {
                val actress = Actress()

                val href1 = element.getElementsByAttribute("href")

                val actUrl = href1.attr("href")
                actress.artworkListUrl = actUrl
                val srcE = element.getElementsByAttribute("src")

                val src = srcE.attr("src")
                val name = srcE.attr("title")
                actress.avatar = src
                actress.name = name
                list.add(actress)

            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

        println(list)
        return list
    }


}
