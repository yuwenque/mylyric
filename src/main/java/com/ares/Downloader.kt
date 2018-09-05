package com.ares

import com.ares.entity.Actress
import com.ares.http.AppConstants
import com.ares.http.ArtworkApi
import com.ares.http.RetrofitServiceManager
import io.reactivex.Scheduler
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers

import java.io.*
import java.net.MalformedURLException
import java.net.URL
import java.util.concurrent.*

object Downloader {


    @JvmStatic
    fun main(args: Array<String>) {

        val artworkApi = RetrofitServiceManager.getManager().create(AppConstants.URL.ARTWORK_URL, ArtworkApi::class.java)

        artworkApi.getActressList(1).subscribeOn(Schedulers.io())
                .subscribe({ actresses ->
                    println("actresses.size = " + actresses.size)

                    val service = Executors.newCachedThreadPool()

                    val path = "/Users/yuwenque/Downloads/actresses"
                    val rootFile = File(path)
                    println("rootFile .exist = " + rootFile.exists())
                    for (actress in actresses) {
                        service.submit { Downloader.downloadPicture(actress.avatar, path + "/" + actress.name + ".jpg") }
                    }
                }) { throwable -> throwable.printStackTrace() }

        try {
            Thread.sleep(200000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

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
