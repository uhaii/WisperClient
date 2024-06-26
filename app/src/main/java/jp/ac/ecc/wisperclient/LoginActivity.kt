package jp.ac.ecc.wisperclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Telephony.Mms.Intents
import android.text.TextUtils
import android.widget.Toast
import jp.ac.ecc.wisperclient.databinding.ActivityLoginBinding
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
import java.util.Objects

/**
 * Whisperを利用するためのログイン画面
 * メールアドレス(userId)とパスワードを入力してログインを行う。
 * ユーザが存在しない場合は、ユーザ作成画面に遷移してユーザ作成を行ってもらう
 */


class LoginActivity : AppCompatActivity() {

    //グローバル変数定義
    companion object{
        var loginUserId : String? = ""
    }

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_login)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        // bindingを使うためしなくても良いと思いますが設計書通りにする。
        val loginText = binding.loginText
        val userIdEdit =binding.userIdEdit
        val passwordEdit = binding.passwordEdit
        val loginButton = binding.loginButton
        val createButton = binding.createButton

        //１－２．loginButtonのクリックイベントリスナーを作成する
        loginButton.setOnClickListener {
            //１－２－１．入力項目が空白の時、エラーメッセージをトースト表示して処理を終了させる
            if (userIdEdit.text.isEmpty() || passwordEdit.text.isEmpty()){
                Toast.makeText(this, "ユーザー名またはパスワードを入力してください", Toast.LENGTH_SHORT).show()
            }

            //　TODO:チェック待ち　PHP　APIの連携
            // １－２－２．ログイン認証APIをリクエストして入力ユーザのログイン認証を行う
            // HTTP接続用インスタンス生成
            val client = OkHttpClient()
            // JSON形式でパラメータを送るようデータ形式を設定
            val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
            // Bodyのデータ(APIに渡したいパラメータを設定)
            val requestBody = "{" +
                    "\"userId\":\"${userIdEdit.text}\"" +
                    "\"password\":\"${passwordEdit.text}\""
                    "}"
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            //TODO:url変更する必要がある。
            val request = Request.Builder().url("http://10.0.2.2/SampleProject/sample.php").post(requestBody.toRequestBody(mediaType)).build()


            client.newCall(request!!).enqueue(object : Callback
                {
                    // １－２－４．リクエストが失敗した時(コールバック処理)
                    override fun onFailure(call: Call, e: IOException) {
                        // １－２－４－１．エラーメッセージをトースト表示する
                        runOnUiThread{
                            Toast.makeText(this@LoginActivity, e.message , Toast.LENGTH_SHORT).show()
                        }
                    }

                    // １－２－３．正常にレスポンスを受け取った時(コールバック処理)
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            // APIから受け取ったデータを文字列で取得
                            val responseBody = response.body?.string()
                            // APIから取得してきたJSON文字列をJSONオブジェクトに変換
                            val json = JSONObject(responseBody)
                            // １－２－３－２．グローバル変数loginUserIdに作成したユーザIDを格納する
                            loginUserId = json.getString("userId")

                            // １－２－３－３．タイムライン画面に遷移する
                            //val intent = Intent(this@LoginActivity, TimelineActivity::class.java)
                            //startActivity(intent)

                            // １－２－３－４．自分の画面を閉じる
                            finish()

                        } catch (e : Exception){
                            runOnUiThread {
                                // １－２－３－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                                Toast.makeText(this@LoginActivity, e.message, Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                })
        }

        // １－３．createButtonのクリックイベントリスナーを作成する
        createButton.setOnClickListener {
            // １－３－１．ユーザ作成画面に遷移する
            val intent = Intent(this, CreateUserActivity::class.java)
            startActivity(intent)
        }

    }

}