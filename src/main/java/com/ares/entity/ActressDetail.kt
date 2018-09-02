package com.ares.entity

class ActressDetail{

    var name :String?=null
    var avatar :String?=null
    var birthday:String?=null
    var age:Int?=null
    var stature:String?=null
    var chestWidth:String?=null
    var waistline:String?=null
    var hipline :String?=null
    var home:String?=null
    var hobby:String?=null
    override fun toString(): String {
        return "ActressDetail(name=$name, avatar=$avatar, birthday=$birthday, age=$age, stature=$stature, chestWidth=$chestWidth, waistline=$waistline, hipline=$hipline, home=$home, hobby=$hobby)"
    }


}