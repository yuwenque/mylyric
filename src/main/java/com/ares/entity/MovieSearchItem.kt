package com.ares.entity

class MovieSearchItem {

    var title:String?=null
    var coverPhotoUrl:String?=null
    var code:String?=null
    var date:String?=null
    var duration:String?=null

    var manufacturer:String?=null
    var actresses:ArrayList<ActressSearchItem>  = ArrayList()
}