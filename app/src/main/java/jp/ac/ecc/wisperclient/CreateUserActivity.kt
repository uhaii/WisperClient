package jp.ac.ecc.wisperclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONObject
import java.io.IOException
import java.net.PasswordAuthentication

class CreateUserActivity : AppCompatActivity() {

    //画面デザインで定義したオブジェクトを変数として宣言する
    private lateinit var userNameEdit: EditText
    private lateinit var userIdEdit: EditText
    private lateinit var passwordEdit: EditText
    private lateinit var rePasswordEdit: EditText
    private lateinit var createButton: Button
    private lateinit var cancelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)

        userNameEdit = findViewById(R.id.userNameEdit)
        userIdEdit = findViewById(R.id.userIdEdit)
        passwordEdit = findViewById(R.id.passwordEdit)
        rePasswordEdit = findViewById(R.id.rePasswordEdit)
        createButton = findViewById(R.id.createButton)
        cancelButton = findViewById(R.id.cancelButton)

        //createButtonのクリックイベントリスナーを作成する
        createButton.setOnClickListener {
            val userName = userNameEdit.text.toString()
            val userId = userIdEdit.text.toString()
            val password = passwordEdit.text.toString()
            val rePassword = rePasswordEdit.text.toString()

            //入力項目が空白の時、エラーメッセージをトースト表示して処理を終了させる
            if (userName.isEmpty()) {
                Toast.makeText(this, "ユーザー名を入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (userId.isEmpty()) {
                Toast.makeText(this, "メールアドレスを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                Toast.makeText(this, "パスワードを入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (rePassword.isEmpty()) {
                Toast.makeText(this, "パスワードを再入力してください", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(userId).matches()) {
                Toast.makeText(this, "存在しないメールアドレスです", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (password != rePassword) {
                Toast.makeText(this, "パスワードが一致していません", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // HTTP接続用インスタンス生成
            val client = OkHttpClient()
            // JSON形式でパラメータを送るようデータ形式を設定
            val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
            // Bodyのデータ(APIに渡したいパラメータを設定)
            val requestBody = "{" +
                    "\"userName\":\"${userNameEdit.text}\"" +
                    "\"userId\":\"${userIdEdit.text}\"" +
                    "\"password\":\"${passwordEdit.text}\""
            "}"
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            val request = Request.Builder().url("http://10.0.2.2/SampleProject/sample.php") 
                .post(requestBody.toRequestBody(mediaType)).build()

            // リクエスト送信（非同期処理）
            client.newCall(request!!).enqueue(object : Callback {
                // リクエストが失敗した場合の処理を実装
                override fun onFailure(call: Call, e: IOException) {
                    // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)
                    runOnUiThread {
                        Toast.makeText(this@CreateUserActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                // リクエストが成功した場合の処理を実装
                override fun onResponse(call: Call, response: Response) {
                    try {
                        // APIから受け取ったデータを文字列で取得
                        val responseBody = response.body?.string()
                        // APIから取得してきたJSON文字列をJSONオブジェクトに変換
                        val json = JSONObject(responseBody)
                        // １－２－３－２．グローバル変数loginUserIdに作成したユーザIDを格納する
                        LoginActivity.loginUserId = json.getString("userId")

                        // １－２－３－３．タイムライン画面に遷移する
                        //val intent = Intent(this@CreateUserActivity, TimelineActivity::class.java)
                        //startActivity(intent)

                        // １－２－３－４．自分の画面を閉じる
                        finish()

                    } catch (e: Exception) {
                        runOnUiThread {
                            // １－２－３－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                            Toast.makeText(this@CreateUserActivity, e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            })
        }
        cancelButton.setOnClickListener {
            finish() // 現在のアクティビティを終了して前の画面に戻る
        }
    }
}
