package jp.ac.ecc.wisperclient

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import jp.ac.ecc.wisperclient.MyApplication.Companion.loginUserId
import jp.ac.ecc.wisperclient.databinding.ActivityWhisperBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException
import java.lang.Exception

/**
 * Fan
 * 新しいWhisper（ささやき）を行う画面。
 * ささやき処理をするとユーザごとに各ささやきが管理される
 */

class WhisperActivity : AppCompatActivity() {

    lateinit var binding: ActivityWhisperBinding
    var overflowMenu = OverflowMenu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWhisperBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val wisperText = binding.wisperText
        val wisperEdit = binding.wisperEdit
        val wisperButton = binding.wisperButton
        val cancelButton = binding.cancelButton

        // １－２．wisperButtonのクリックイベントリスナーを作成する
        wisperButton.setOnClickListener {
            // １－２－１．入力項目が空白の時、エラーメッセージをトースト表示して処理を終了させる
            if (wisperEdit.text.isEmpty()){
                Toast.makeText(this, R.string.whisper_blank_error_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // TODO:チェック待ち　PHP　APIの連携
            // １－２－２．ささやき登録処理APIをリクエストして入力したささやきの登録処理を行う
            // HTTP接続用インスタンス生成
            val client = OkHttpClient()
            // JSON形式でパラメータを送るようデータ形式を設定
            val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
            // TODO:確認　Bodyのデータ(APIに渡したいパラメータを設定)
            val requestBody =  "{\"content\":\"${wisperEdit.text}\"}"
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            val request = Request.Builder().url("${MyApplication.apiUrl}whisperAdd.php").post(requestBody.toRequestBody(mediaType)).build()

            client.newCall(request!!).enqueue(object : Callback
                {
                    // １－２－４．リクエストが失敗した時(コールバック処理)
                    override fun onFailure(call: Call, e: IOException) {
                        // １－２－４－１．エラーメッセージをトースト表示する
                        Toast.makeText(this@WhisperActivity, e.message, Toast.LENGTH_SHORT).show()
                    }

                    // １－２－３．正常にレスポンスを受け取った時(コールバック処理)
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            // １－２－３－２．インテントにログインユーザIDをセットする
//                            val intent = Intent(this@WhisperActivity, UserInfoActivity::class.java)
                            intent.putExtra("userId", loginUserId)
                            // １－２－３－３．ユーザ情報画面に遷移する
                            Log.e("Transiton Successed","画面遷移成功")
                            startActivity(intent)
                            // １－２－３－４．自分の画面を閉じる
                            finish()

                        } catch (e : Exception){
                            // １－２－３－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                            Toast.makeText(this@WhisperActivity, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                })
        }
        // １－３．cancelButtonのクリックイベントリスナーを作成する
        cancelButton.setOnClickListener {
            // １－３－１．ささやき登録画面に遷移する
            val intent = Intent(this, WhisperActivity::class.java)
            startActivity(intent)
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