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

        println("url = $redirectUrl")
        val actressDetail = ActressDetail()
        try {
            val document = Jsoup.connect(redirectUrl).get()

            val photoElements = document.getElementsByClass("avatar-box")
            println("---avatar-box---")
            println(photoElements)
            println("------")

           val nameAndUrl= photoElements.filter {

                it.className() == "photo-frame"
            }

            println("---photoElements---")
            println(nameAndUrl)
            println("------")
//            actressDetail.name = nameAndUrl.attr("title")
//            actressDetail.avatar = nameAndUrl.attr("src")
            val topElement = document.getElementsByClass("photo-info")

            val infoElement = topElement[0]


            println(topElement)
            infoElement.allElements.filter {

                it.className() != "pb10"
            }.map {
                it.text().split(": ")
            }.forEach {
                when(it[0]){

                    "生日"-> actressDetail.birthday =  it[1]
                    "年齡"->{


                        actressDetail.age = it[1].toInt()

                    }
                    "胸圍" -> actressDetail.chestWidth =  it[1]
                    "身高" -> actressDetail.stature =  it[1]
                    "腰圍" -> actressDetail.waistline =  it[1]
                    "臀圍"-> actressDetail.hipline =  it[1]
                    "出生地"-> actressDetail.home = it[1]
                    "愛好" -> actressDetail.hobby = it[1]
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
