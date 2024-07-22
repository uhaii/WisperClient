package jp.ac.ecc.wisperclient

import android.net.Uri

data class UserRowData(
    val userId : String,
    val userName : String,
    val followCount: Int,
    val followerCount : Int,
    val icon : Uri,
)
