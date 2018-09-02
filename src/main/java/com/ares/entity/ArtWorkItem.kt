package com.ares.entity

class ArtWorkItem :BaseSearchItem() {


    var title:String?=null
    var content:String?=null
    var code:String?=null
    var photoUrl:String?=null
    var date:String?=null
    var movieUrl:String?=null
    override fun toString(): String {
        return "ArtWorkItem(title=$title, content=$content, code=$code, photoUrl=$photoUrl, date=$date, movieUrl=$movieUrl)"
    }


}