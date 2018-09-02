package com.ares

import com.ares.entity.Actress
import com.ares.entity.ActressDetail
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.io.File

import javax.lang.model.util.Elements
import java.io.IOException
import java.util.ArrayList

@RestController
@RequestMapping("/actresses")
class PhotoController {


    @RequestMapping("/search")
    fun getActWorkList(@RequestParam(name = "redirectUrl", defaultValue = "https://www.javbus.com/star/2di") redirectUrl: String): ActressDetail {

        val actressDetail = ActressDetail()
        try {
            val document = Jsoup.connect(redirectUrl).get()
//            val document = Jsoup.parse(File("/Users/yuwenque/Downloads/test.html "),"UTF-8")



            val photoElements = document.getElementsByClass("photo-frame")
            val nameAndUrl = photoElements[0]
            actressDetail.name = nameAndUrl.attr("title")
            actressDetail.avatar = nameAndUrl.attr("src")
            val infoElements = document.getElementsByClass("photo-info")


            println(infoElements)
            (1 until infoElements.size).map {

                infoElements[it]
            }.map {

                it.text()
            }  .forEach {

                when{

                    it.indexOf("生日") !=-1 -> actressDetail.birthday = it
                    it.indexOf("年齡") !=-1 ->{

                        val str = it.split(":")
                        actressDetail.age = str[1].toInt()
                    }
                    it.indexOf("胸圍") !=-1 -> actressDetail.chestWidth = it
                    it.indexOf("身高") !=-1 -> actressDetail.stature = it
                    it.indexOf("腰圍") !=-1 -> actressDetail.waistline = it
                    it.indexOf("臀圍") !=-1 -> actressDetail.hipline = it
                    it.indexOf("出生地") !=-1 -> actressDetail.home = it
                    it.indexOf("愛好") !=-1 -> actressDetail.hobby = it
                }

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }


        println(actressDetail)
        return actressDetail
    }

    @RequestMapping("/{page}")
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
