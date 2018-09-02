package com.ares.entity

class ActressSearchItem:BaseSearchItem() {

    var name:String?=null
    var avatar:String?=null
    var workListUrl:String?=null
    override fun toString(): String {
        return "ActressSearchItem(name=$name, avatar=$avatar, workListUrl=$workListUrl)"
    }


}