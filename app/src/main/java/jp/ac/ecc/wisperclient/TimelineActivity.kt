package jp.ac.ecc.wisperclient

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import jp.ac.ecc.wisperclient.MyApplication
import jp.ac.ecc.wisperclient.MyApplication.Companion.loginUserId
import jp.ac.ecc.wisperclient.OverflowMenu
import jp.ac.ecc.wisperclient.R
import jp.ac.ecc.wisperclient.WhisperAdapter
import jp.ac.ecc.wisperclient.WhisperRowData
import jp.ac.ecc.wisperclient.databinding.ActivityTimelineBinding
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
 * フォローしているユーザのWhisper一覧を表示する画面
 * 各Whisperのアイコンをタップしたら、ユーザ情報画面に遷移をして、
 * イイねボタンをタップしたらイイねの選択および解除を行う
 */

class TimelineActivity : AppCompatActivity() {

    lateinit var binding: ActivityTimelineBinding
    var overflowMenu = OverflowMenu()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTimelineBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val timelineText = binding.timelineText
        val timelineRecycle = binding.timelineRecycle
        val myapp = application as MyApplication

        // １－２．タイムライン情報取得API　共通実行メソッドを呼び出す
        getTimelineInfo(myapp, loginUserId.toString(), timelineRecycle)
    }

    // ２．タイムライン情報取得API　共通実行メソッド
    fun getTimelineInfo(myapp: MyApplication, loginUserId: String, timelineRecycle: RecyclerView){
        //TODO: ２－１．タイムライン情報取得APIをリクエストしてログインユーザがフォローしているささやき情報取得を行う
        // HTTP接続用インスタンス生成
        val client = OkHttpClient()
        // JSON形式でパラメータを送るようデータ形式を設定
        val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
        // TODO:確認　Bodyのデータ(APIに渡したいパラメータを設定)
        val requestBody =  "{\"content\":\"${loginUserId}\"}"
        // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
        val request = Request.Builder().url("http://timelineInfo.php").post(requestBody.toRequestBody(mediaType)).build()

        client.newCall(request!!).enqueue(object : Callback{
            // ２－３．リクエストが失敗した時(コールバック処理)
            override fun onFailure(call: Call, e: IOException) {
                // ２－３－１．エラーメッセージをトースト表示する
                Toast.makeText(myapp, e.message, Toast.LENGTH_SHORT).show()
            }

            // ２－２．正常にレスポンスを受け取った時(コールバック処理)
            override fun onResponse(call: Call, response: Response) {
                try {
                    // APIから受け取ったデータを文字列で取得
                    val responseBody = response.body?.string()
                    // APIから取得してきたJSON文字列をJSONオブジェクトに変換
                    val json = JSONObject(responseBody)
                    val whisperlist = mutableListOf<WhisperRowData>()
                    // ２－２－３．ささやき情報一覧が存在する間、以下の処理を繰り返す
                    if (json.getString("whisperList") != null){
                        // ２－２－３－１．ささやき情報をリストに格納する
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
                    // ２－２－４．timelineRecycleにささやき情報リストをセットする
                    timelineRecycle.adapter = WhisperAdapter(whisperlist)


                } catch (e:Exception){
                    // ２－２－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                    Toast.makeText(myapp, e.message, Toast.LENGTH_SHORT).show()
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