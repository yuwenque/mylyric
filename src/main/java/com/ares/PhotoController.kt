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


    @RequestMapping("/search/{keyword}/{page}")
    fun search(@PathVariable keyword:  String,@PathVariable page:Int=1 ,@RequestParam(name = "type") type:Int = SEARCH_ACTWORK):String {

        val url =   when(type){

            SEARCH_ACTWORK -> SEARCH_URL.plus("search/$keyword/$page")
            SEARCH_ACTWORK_UNCENSORED -> SEARCH_URL.plus("uncensored/search/$keyword/$page&type=$type")
            SEARCH_ACTRESS -> SEARCH_URL.plus("searchstar/$keyword/$page")
            else -> SEARCH_URL

        }
        println("搜索路径 = $url")
        try {
            val document = Jsoup.connect(url).get()
             println(document)
        }catch (e:IOException){
            e.printStackTrace()
        }


        return ""

    }


    companion object {

       const val SEARCH_URL ="https://www.javbus.com/"

        //模糊
        const val SEARCH_ACTWORK = 0
        //高清
        const val SEARCH_ACTWORK_UNCENSORED = 1
        //艺术家
        const val SEARCH_ACTRESS = 2
    }

    @RequestMapping("/getDetail")
    fun getActWorkList(@RequestParam(name = "redirectUrl") redirectUrl: String): ActressDetail {

        if(redirectUrl.isEmpty()){

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

           val nameAndUrl= photoElements[0].allElements.filter {

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
