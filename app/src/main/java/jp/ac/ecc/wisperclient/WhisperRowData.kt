package jp.ac.ecc.wisperclient

import android.net.Uri

data class WhisperRowData(
    val userId : String,
    val userName : String,
    val whisperNo : Int,
    val content : String,
    var goodFlg : Boolean,
    val icon : Uri
)
