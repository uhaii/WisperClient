package jp.ac.ecc.wisperclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class FollowListActivity : AppCompatActivity() {

    var overflowMenu = OverflowMenu()

    private lateinit var followListText: TextView
    private lateinit var followRecycle: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_list)

        // 画面デザインで定義したオブジェクトを変数として宣言する。
        followListText = findViewById(R.id.followListText)
        followRecycle = findViewById(R.id.followRecycle)
        followRecycle.layoutManager = LinearLayoutManager(this)

        // インテント(前画面)から対象ユーザIDと区分（フォロー・フォロワー）を取得する
        val userId = intent.getLongExtra("userId", -1)
        val type = intent.getStringExtra("type") ?: ""

        // 区分がフォローの時
        if (type == "follow") {
            followListText.text = "フォローリスト" // followListTextをフォローリストと表示する

        } else if (type == "follower") { // 区分がフォロワーの時
            followListText.text = "フォロワーリスト" // followListTextをフォロワーリストと表示する
        }

        // フォロワー情報取得APIをリクエストして対象ユーザのフォロー・フォロワー情報取得処理を行う
        val client = OkHttpClient() // HTTP接続用インスタンス生成

        // JSON形式でパラメータを送るようデータ形式を設定
        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        // Bodyのデータ(APIに渡したいパラメータを設定)
        val requestBody = """
            {
                "userId": "$userId",
                "type": "$type"
            }
        """.trimIndent().toRequestBody(mediaType)

        // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
        val request = Request.Builder()
            .url("http://10.0.2.2/SampleProject/sample.php")
            .post(requestBody)
            .build()

        // リクエスト送信（非同期処理）
        client.newCall(request).enqueue(object : Callback {

            // リクエストが失敗した場合の処理を実装
            override fun onFailure(call: Call, e: IOException) {
                // runOnUiThreadメソッドを使うことでUIを操作することができる。(postメソッドでも可)
                runOnUiThread {
                    Toast.makeText(this@FollowListActivity, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            // 正常にレスポンスを受け取った時(コールバック処理)
            // JSONデータがエラーの場合、受け取ったエラーメッセージを　トースト表示して処理を終了させる
            override fun onResponse(call: Call, response: Response) {
                try {
                    // APIから受け取ったデータを文字列で取得
                    val responseBody = response.body?.string()
                    // RecyclerViewに設定するリストを作成
                    val list = mutableListOf<RowData>()
                    // APIから取得してきたJSON文字列をJSONオブジェクトに変換
                    val json = JSONObject(responseBody)
                    // JSONオブジェクトの中からKey値がlistのValue値を文字列として取得(Value値のイメージ：{"list" : [{"???" : "xxx"}, {"???" : "yyy"} ...]})
                    val productList = json.getString("list")
                    // 取得したValue値(文字列)は配列の構成になっているので、JSON配列に変換
                    val jsonArray = JSONArray(productList)

                    if (response.isSuccessful) {
                        // フォロー情報またはフォロワー情報一覧が存在する間、以下の処理を繰り返す
                        for (i in 0 until jsonArray.length()) {
                            val userId = jsonArray.getJSONObject(i).getString("userId")
                            val userImage = jsonArray.getJSONObject(i).getString("userImage")
                            val userName = jsonArray.getJSONObject(i).getString("userName")
                            val followText = jsonArray.getJSONObject(i).getString("followText")
                            val followerText = jsonArray.getJSONObject(i).getString("followerText")
                            val followCount = jsonArray.getJSONObject(i).getString("followCount")
                            val followerCount = jsonArray.getJSONObject(i).getString("followerCount")

                            // フォロー情報またはフォロワー情報をリストに格納する
                            list.add(
                                RowData(
                                    userId,
                                    userImage,
                                    userName,
                                    followText,
                                    followerText,
                                    followCount,
                                    followerCount
                                )
                            )
                        }
                    }
                    // followRecycleにフォロー情報リストまたはフォロワー情報リストをセットする
                    // UIスレッドでRecyclerViewにリストを設定
                    this@FollowListActivity.runOnUiThread {
                        // LinearLayoutManagerを設定し、RecyclerViewを初期化する
                        followRecycle.layoutManager = LinearLayoutManager(applicationContext)
                        // 作成したlistをアダプターに渡し、RecyclerViewにアダプターを設定する
                        val adapter = ProductRecycleAdapter(list)
                        followRecycle.adapter = adapter
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        // エラーメッセージをトースト表示する
                        Toast.makeText(this@FollowListActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    // オプションメニュー生成時
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // オーバーフローメニューのオプションメニュー生成メソッドにmenuとactivityを渡して呼び出す
        return overflowMenu.onCreateOptionsMenu(menu, this)
    }

    // オプションメニューアイテム選択時
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // オーバーフローメニューのオプションメニューアイテム選択メソッドにitemとactivityを渡して呼び出す
        overflowMenu.onOptionsItemSelected(item, this)
        // 親クラスのオプションメニューアイテム選択をセットする
        return super.onOptionsItemSelected(item)
    }
}
