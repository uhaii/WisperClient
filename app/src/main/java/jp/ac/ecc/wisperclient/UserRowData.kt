package jp.ac.ecc.wisperclient

data class UserRowData(
    val userId : String,
    val userName : String,
    val followCount: Int,
    val followerCount : Int
)
