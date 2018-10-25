package com.ares

import com.ares.entity.Actress
import com.ares.entity.VideoSearchItem
import com.ares.http.AppConstants
import com.ares.http.ArtworkApi
import com.ares.http.RetrofitServiceManager
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup
import org.jsoup.nodes.Node
import org.jsoup.select.NodeFilter

import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.*

object Downloader {


    @JvmStatic
    fun main(args: Array<String>) {

        val document = Jsoup.connect("https://www.torrentkitty.tv/search/SCOP-321").get()


      val trs =   document.getElementsByTag("tr")

        val searchList  = ArrayList<VideoSearchItem>()
        trs.forEach {


           val tds =  it.getElementsByTag("td")
            val videoItem = VideoSearchItem()
            tds.forEach {

                when(it.className()){
                    "name"-> videoItem.name = it.text()
                    "size" -> videoItem.size = it.text()
                    "date" -> videoItem.date = it.text()
                    "action"-> {

                      val a =  it.getElementsByAttributeValueContaining("href","magnet")
                        if(a.size >0){

                          val href =   a[0].attr("href")
                            videoItem.magnet = href

                            println("----")
                            println(videoItem)
                            searchList.add(videoItem)
                        }


                    }
                }


            }

        }


        val filterList =  trs.filter {

            it.hasClass("name") && it.hasAttr("href")
        }
        println(filterList)

        Thread.sleep(10000000)

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
}
