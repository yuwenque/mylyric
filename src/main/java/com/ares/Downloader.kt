package com.ares

import com.ares.entity.Actress
import com.ares.http.AppConstants
import com.ares.http.ArtworkApi
import com.ares.http.RetrofitServiceManager
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers
import org.jsoup.Jsoup

import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.*

object Downloader {


    @JvmStatic
    fun main(args: Array<String>) {


        val document = Jsoup.connect("https://www.javbus.com/SCOP-321").get()


        println(document)

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
