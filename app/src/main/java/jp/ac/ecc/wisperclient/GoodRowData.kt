package jp.ac.ecc.wisperclient

import android.net.Uri

data class GoodRowData(
    val whisperNo : Int,
    val content : String,
    val userId : String,
    val userName : String,
    val goodCount : Int,
    val icon : Uri,
)
