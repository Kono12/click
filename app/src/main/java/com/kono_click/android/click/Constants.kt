package com.kono_click.android.click

object Constants {
    var HighScore : Int = 0
    var scoree : Int =0
    var time : Int = 60
    var UserMoney : Long =0
    // Todo : make variables for shop
    var wallPaper ="default"
    var SlowMotionLevel = 5
    var MagmetLevel =5
    var GoldLevel =5
    var MoreMoneyLevel=5
    var BigHitLevel=5

    //todo: money collected by   (done)
    var normalMoey=0
    var GoldenMoney=0
    var MagnetMoney=0
    var SlowMoney = 0
    var BigHitMoney =0
    var moreMoneyMoney=0

    // todo : number of Abilities (done)
    var MagnetAmount=0
    var GoldenAmount =0
    var SlowAmount=0
    var BigHitAmount=0
    var MoreMoneyAmount=0

    fun resetData(){
         normalMoey=0
         GoldenMoney=0
         MagnetMoney=0
         SlowMoney = 0
         BigHitMoney =0
         moreMoneyMoney=0
         MagnetAmount=0
         GoldenAmount =0
         SlowAmount=0
         BigHitAmount=0
         MoreMoneyAmount=0

    }
}