package jp.ac.ecc.wisperclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import jp.ac.ecc.wisperclient.databinding.ActivityUserInfoBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONArray
import org.json.JSONObject

/**
 * Fan
 * 対象ユーザのプロフィール情報およびWhisper一覧表示画面
 * 表示ユーザのフォローの登録・解除ができる
 * また、イイね一覧を選択すると表示ユーザがイイねしたWhisper一覧が参照できる
 */

class UserInfoActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserInfoBinding
    var overflowMenu = OverflowMenu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val userInfoText = binding.userInfoText
        val userImage = binding.userImage
        val userNameText = binding.userNameText
        val profileText = binding.profileText
        val followText = binding.followText
        val followerText = binding.followerText
        val followCntText = binding.followCntText
        val followerCntText = binding.followerCntText
        val followButton = binding.followButton
        val radioGroup = binding.radiogroup2
        val whisperRadio = binding.whisperRadio
        val goodInfoRadio = binding.goodInfoRadio
        val userRecycle = binding.userRecycle

        // TODO:確認必要　１－２．インテント(前画面)から対象ユーザIDを取得する
        val userId = intent.getStringExtra("userId")

        // １－３．ユーザささやき情報取得API　共通実行メソッドを呼び出す
        getWhisperInfo(
            MyApplication(),
            userId,
            MyApplication.loginUserId.toString(),
            userNameText,
            profileText,
            followCntText,
            followerCntText,
            followButton,
            userRecycle,
            radioGroup
        )

        // １－４．radioGroupのチェック変更イベントリスナーを作成する
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId){
                R.id.whisperRadio -> {
                    //TODO: １－４－１．ユーザささやき情報取得API　共通実行メソッドを呼び出して最新の状態にする
                    // myapp : MyApplication,
                    //        userId : String?,
                    //        loginUserId : String,
                    //        userNameTx : TextView,
                    //        userProfileTx : TextView,
                    //        followCountTx : TextView,
                    //        followerCountTx : TextView,
                    //        followBtn : Button,
                    //        userRecycle : RecyclerView,
                    //        radioGroup : RadioGroup,


                    getWhisperInfo(
                        MyApplication(),
                        userId,
                        MyApplication.loginUserId.toString(),
                        userNameText,
                        profileText,
                        followCntText,
                        followerCntText,
                        followButton,
                        userRecycle,
                        radioGroup
                    )
                }
            }
        }

        // １－５．followCntTextのクリックイベントリスナーを作成する
        followCntText.setOnClickListener {
//            val intent = Intent(this, FollowListActivity::class.java)
            // 試し
            val intent = Intent(this, TimelineActivity::class.java)

            // １－５－１．インテントに対象ユーザIDをセットする
            intent.putExtra("userId", userId)
            // １－５－２．インテントに文字列followをセットする
            intent.putExtra("follow",followText.toString())
            // １－５－３．フォロー一覧画面に遷移する
            startActivity(intent)
            Log.e("Transiton Successed","画面遷移成功")
        }

        // １－６．followerTextのクリックイベントリスナーを作成する
        followerText.setOnClickListener {
//            val intent = Intent(this, FollowListActivity::class.java)
            // 試し
            val intent = Intent(this, TimelineActivity::class.java)
            // １－６－１．インテントに対象ユーザIDをセットする
            intent.putExtra("userId", userId)
            // １－６－２．インテントに文字列followerをセットする
            intent.putExtra("follower", followerText.toString())
            // １－６－３．フォロー一覧画面に遷移する
            startActivity(intent)
            Log.e("Transiton Successed","画面遷移成功")
        }

        // １－７．followButtonのクリックイベントリスナーを作成する
        followButton.setOnClickListener {
            // １－７－１．フォロー管理処理APIをリクエストして対象ユーザのフォロー登録または解除を行う
            // HTTP接続用インスタンス生成
            val client = OkHttpClient()
            // JSON形式でパラメータを送るようデータ形式を設定
            val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
            // TODO:確認必要　Bodyのデータ(APIに渡したいパラメータを設定)
            val requestBody = "{" +
                    "\"userId\":\"${MyApplication.loginUserId}\"," +
                    "\"followUserId\":\"${userId}\"," +
                    "\"followFlg\":\"true\"" +
                    "}"
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            val request = Request.Builder().url("${MyApplication.apiUrl}followCtl.php").post(requestBody.toRequestBody(mediaType)).build()

            Log.e("successed send followctl", "転送成功")

            client.newCall(request!!).enqueue(object : Callback
            {
                // １－７－３．リクエストが失敗した時(コールバック処理)
                override fun onFailure(call: Call, e: IOException) {
                    // １－７－３－１．エラーメッセージをトースト表示する
                    runOnUiThread {
                        Toast.makeText(this@UserInfoActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                // １－７－２．正常にレスポンスを受け取った時(コールバック処理)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        val intent = Intent(this@UserInfoActivity, UserInfoActivity::class.java)
                        // １－７－２－２．インテントに対象ユーザIDをセットする
                        intent.putExtra("userId", userId)
                        // １－７－２－３．ユーザ情報画面に遷移する
                        startActivity(intent)
                        // １－７－２－４．自分の画面を閉じる
                        finish()

                    } catch (e : Exception){
                        // １－７－２－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                        runOnUiThread {
                            Toast.makeText(this@UserInfoActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            })

        }
    }

    // ２．ユーザささやき情報取得API　共通実行メソッド
    fun getWhisperInfo(
        myapp : MyApplication,
        userId : String?,
        loginUserId : String,
        userNameTx : TextView,
        userProfileTx : TextView,
        followCountTx : TextView,
        followerCountTx : TextView,
        followBtn : Button,
        userRecycle : RecyclerView,
        radioGroup : RadioGroup,
    ) {
        // ２－１．ユーザささやき情報取得APIをリクエストして対象ユーザのささやき情報とそのユーザがイイねしている情報取得を行う
        // HTTP接続用インスタンス生成
        val client = OkHttpClient()
        // JSON形式でパラメータを送るようデータ形式を設定
        val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
        // TODO:確認必要　Bodyのデータ(APIに渡したいパラメータを設定)
        val requestBody = "{" +
                "\"userId\":\"${userId}\"," +
                "\"loginUserId\":\"${MyApplication.loginUserId}\"" +
                "}"
        // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
        val request = Request.Builder().url("${MyApplication.apiUrl}userWhisperInfo.php").post(requestBody.toRequestBody(mediaType)).build()

        Log.e("successed send userwhisperinfo", "転送成功")

        client.newCall(request!!).enqueue(object : Callback
        {
            // ２－３．リクエストが失敗した時(コールバック処理)
            override fun onFailure(call: Call, e: IOException) {
                // ２－３－１．エラーメッセージをトースト表示する
                runOnUiThread {
                    Toast.makeText(this@UserInfoActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            // ２－２．正常にレスポンスを受け取った時(コールバック処理)
            override fun onResponse(call: Call, response: Response) {
                try {
                    // ２－２－２．取得したデータを各オブジェクトにセットする
                    // APIから受け取ったデータを文字列で取得
                    val responseBody = response.body?.string()
                    // APIから取得してきたJSON文字列をJSONオブジェクトに変換
                    val json = JSONObject(responseBody)
                    // ささやき情報に使っているリスト生成
                    val whisperlist = mutableListOf<WhisperRowData>()
                    val goodlist = mutableListOf<WhisperRowData>()
                    // JSONオブジェクトの中から取得して、セットする
                    userNameTx.text = json.getString("userName")
                    userProfileTx.text = json.getString("profile")
                    followCountTx.text = json.getString("followCount")
                    followerCountTx.text = json.getString("followerCount")

                    // ２－２－３．フォローボタン
                    // ２－２－３－１．フォローユーザならフォロー中と表示、それ以外ならフォローすると表示する
                    if (json.getBoolean("userFollowFlg")){
                        followBtn.text = "フォロー中"
                    } else {
                        followBtn.text = "フォローする"
                    }
                    // TODO:確認 ２－２－３－２．対象ユーザがログインユーザの時、ボタンを非表示にする
                    if (loginUserId == json.getString("userId")){
                        followBtn.visibility = View.GONE
                    }

                    // ２－２－４．ささやき情報一覧が存在する間、以下の処理を繰り返す
                    if (json.getString("whisperList") != null){
                        // ２－２－４－１．ささやき情報をリストに格納する
                        // JSONオブジェクトの中から取得
                        val whisperList = json.getString("whisperList")
                        // 取得した文字列は配列の構成になっているので、JSON配列に変換
                        val jsonArray = JSONArray(whisperList)
                        for (i in 0 until jsonArray.length()){
                            val userId = jsonArray.getJSONObject(i).getString("userId")
                            val userName = jsonArray.getJSONObject(i).getString("userName")
                            val whisperNo = jsonArray.getJSONObject(i).getString("whisperNo").toInt()
                            val content = jsonArray.getJSONObject(i).getString("content")
                            val goodFlg = jsonArray.getJSONObject(i).getString("goodFlg").toBoolean()
                            whisperlist.add(WhisperRowData(userId, userName, whisperNo, content, goodFlg))
                        }
                    }

                    // ２－２－５．イイね情報一覧が存在する間、以下の処理を繰り返す
                    if (json.getString("goodList") != null){
                        // ２－２－５－１．イイね情報をリストに格納する
                        // JSONオブジェクトの中から取得
                        val goodList = json.getString("goodList")
                        // 取得した文字列は配列の構成になっているので、JSON配列に変換
                        val jsonArray = JSONArray(goodList)
                        for (i in 0 until jsonArray.length()){
                            val userId = jsonArray.getJSONObject(i).getString("userId")
                            val userName = jsonArray.getJSONObject(i).getString("userName")
                            val whisperNo = jsonArray.getJSONObject(i).getString("whisperNo").toInt()
                            val content = jsonArray.getJSONObject(i).getString("content")
                            val goodFlg = jsonArray.getJSONObject(i).getString("goodFlg").toBoolean()
                            goodlist.add(WhisperRowData(userId, content, whisperNo, userName, goodFlg))
                        }
                    }

                    // ２－２－６．userRecycle
                    // RecyclerViewを初期化する
                    userRecycle.layoutManager = LinearLayoutManager(applicationContext)
                    radioGroup.setOnCheckedChangeListener { group, checkedId ->
                        when(checkedId){
                            // ２－２－６－１．ラジオボタンがwhisperRadioを選択している時ささやき行情報のアダプターにささやき情報リストをセットする
                            R.id.whisperRadio -> {
                                userRecycle.adapter = WhisperAdapter(whisperlist)
                                Log.e("Whisper Successed","ささやき情報リスト表示成功")
                            }
                            // ２－２－６－２．ラジオボタンがgoodInfoRadioを選択している時ささやき行情報のアダプターにイイね情報リストをセットする
                            R.id.goodInfoRadio -> {
                                userRecycle.adapter = WhisperAdapter(goodlist)
                                Log.e("GoodInfo Successed","イイね情報リスト表示成功")
                            }
                        }
                    }

                } catch (e : Exception) {
                    // ２－２－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                    runOnUiThread {
                        Toast.makeText(this@UserInfoActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        })
    }

    // ３．オプションメニュー生成時
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // ３－１．オーバーフローメニューのオプションメニュー生成メソッドにmenuとactivityを渡して呼び出す
        // ３－２．戻り値に上記メソッドの戻り値をセットする
        return overflowMenu.onCreateOptionsMenu(menu, this)
    }

    // ４．オプションメニューアイテム選択時
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // ４－１．オーバーフローメニューのオプションメニューアイテム選択メソッドにitemとactivityを渡して呼び出す
        overflowMenu.onOptionsItemSelected(item, this)
        // ４－２．戻り値に親クラスのオプションメニューアイテム選択をセットする
        return super.onOptionsItemSelected(item)
    }

}