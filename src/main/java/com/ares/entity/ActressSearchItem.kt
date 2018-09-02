package com.ares.entity

class ActressSearchItem:BaseSearchItem() {

    var id:String?=null
    var name:String?=null
    var avatar:String?=null
    var workListUrl:String?=null
    override fun toString(): String {
        return "ActressSearchItem(id=$id,name=$name, avatar=$avatar, workListUrl=$workListUrl)"
    }


}