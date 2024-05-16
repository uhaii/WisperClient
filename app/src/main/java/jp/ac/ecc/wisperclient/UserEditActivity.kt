package jp.ac.ecc.wisperclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import jp.ac.ecc.wisperclient.databinding.ActivityUserEditBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import org.json.JSONObject

/**
 * ログインユーザのプロフィール情報を登録・修正する画面
 * ユーザIDは変更不可とする。
 * ここで登録された内容はユーザ情報画面に表示される。
 */

class UserEditActivity : AppCompatActivity() {

    lateinit var binding: ActivityUserEditBinding
    var overflowMenu = OverflowMenu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val userEditText = binding.userEditText
        val userImage = binding.userImage
        val userIdText = binding.userIdText
        val userNameEdit = binding.userNameEdit
        val profileEdit = binding.profileEdit
        val changeButton = binding.changeButton
        val cancelButton = binding.cancelButton

        //　TODO:チェック待ち　PHP　APIの連携
        // １－２．ユーザ情報取得APIをリクエストしてログインユーザのユーザ情報取得処理を行う
        // HTTP接続用インスタンス生成
        val client = OkHttpClient()
        // JSON形式でパラメータを送るようデータ形式を設定
        val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
        // TODO:確認　Bodyのデータ(APIに渡したいパラメータを設定)
        val requestBody =  "{\"userId\":\"${userIdText.text}\"}"
        // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
        val request = Request.Builder().url("http://userInfo.php").post(requestBody.toRequestBody(mediaType)).build()

        client.newCall(request!!).enqueue(object : Callback
        {
            // １－４．リクエストが失敗した時(コールバック処理)
            override fun onFailure(call: Call, e: IOException) {
                // １－４－１．エラーメッセージをトースト表示する
                // エラー発生のため runOnUiThread で囲む会
                runOnUiThread {
                    Toast.makeText(this@UserEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            // １－３．正常にレスポンスを受け取った時(コールバック処理)
            override fun onResponse(call: Call, response: Response) {
                try {
                    // １－３－２．取得したデータを各オブジェクトにセットする
                    // APIから受け取ったデータを文字列で取得
                    val responseBody = response.body?.string()
                    // APIから取得してきたJSON文字列をJSONオブジェクトに変換
                    val json = JSONObject(responseBody)
                    //TODO:名前チェック必要 JSONオブジェクトの中からKey値別で情報取得
                    val userName = json.getString("userName")
                    val userProfile = json.getString("profile")
                    //TODO:書き方チェック必要 取得した情報をセットする
                    userNameEdit.text = Editable.Factory.getInstance().newEditable(userName)
                    profileEdit.text = Editable.Factory.getInstance().newEditable(userProfile)

                } catch (e : Exception){
                    // １－３－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                    Toast.makeText(this@UserEditActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

        })

        // １－５．changeButtonのクリックイベントリスナーを作成する
        changeButton.setOnClickListener {
            // １－５－１．ユーザ変更処理APIをリクエストして入力したユーザ情報の更新処理を行う
            //　TODO:チェック待ち　PHP　APIの連携
            // １－２．ユーザ情報取得APIをリクエストしてログインユーザのユーザ情報取得処理を行う
            // HTTP接続用インスタンス生成
            val client = OkHttpClient()
            // JSON形式でパラメータを送るようデータ形式を設定
            val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
            // TODO:確認　Bodyのデータ(APIに渡したいパラメータを設定)
            val requestBody = "{" +
                    "\"userId\":\"${userIdText.text}\"" +
                    "\"userName\":\"${userNameEdit.text}\"" +
                    "\"profile\":\"${profileEdit.text}\""
                    "}"
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            val request = Request.Builder().url("http://userUpd.php").post(requestBody.toRequestBody(mediaType)).build()

            client.newCall(request!!).enqueue(object :Callback
            {
                // １－５－４．リクエストが失敗した時(コールバック処理)
                override fun onFailure(call: Call, e: IOException) {
                    // １－５－４－１．エラーメッセージをトースト表示する
                    // エラー発生のため runOnUiThread で囲む会
                    runOnUiThread {
                        Toast.makeText(this@UserEditActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }

                // １－５－２．正常にレスポンスを受け取った時(コールバック処理)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        //TODO: １－５－２－２．インテントにログインユーザIDをセットする
//                        val intent = Intent(this@UserEditActivity, UserInfoActivity::class.java)
//                        intent.putExtra("userId", LoginActivity.loginUserId)
                        // １－５－２－３．ユーザ情報画面に遷移する
                        Log.e("Transiton Successed","画面遷移成功")
//                        startActivity(intent)
                        // １－５－２－４．自分の画面を閉じる
                        finish()

                    } catch (e : Exception){
                        // １－５－２－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                        Toast.makeText(this@UserEditActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        // １－６．cancelButtonのクリックイベントリスナーを作成する
        cancelButton.setOnClickListener {
            // １－６－１．自分の画面を閉じる
            finish()
        }
    }

    // ２．オプションメニュー生成時
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // ２－１．オーバーフローメニューのオプションメニュー生成メソッドにmenuとactivityを渡して呼び出す
        // ２－２．戻り値に上記メソッドの戻り値をセットする
        return overflowMenu.onCreateOptionsMenu(menu, this)
    }

    // ３．オプションメニューアイテム選択時
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // ３－１．オーバーフローメニューのオプションメニューアイテム選択メソッドにitemとactivityを渡して呼び出す
        overflowMenu.onOptionsItemSelected(item, this)
        // ３－２．戻り値に親クラスのオプションメニューアイテム選択をセットする
        return super.onOptionsItemSelected(item)
    }
}