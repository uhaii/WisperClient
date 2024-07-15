package jp.ac.ecc.wisperclient

data class RowData(
    val userId : String,
    val userImage : String,
    val userName : String,
    val followText : String,
    val followerText : String,
    val followCount : String,
    val followerCount : String
)
