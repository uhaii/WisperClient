package jp.ac.ecc.wisperclient

data class WhisperRowData(
    val userId : String,
    val userName : String,
    val whisperNo : Int,
    val content : String,
    val goodFlg : Boolean)
