package jp.ac.ecc.wisperclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class FollowListActivity : AppCompatActivity() {

    private lateinit var followListText: TextView
    private lateinit var followRecycle: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_follow_list)

        //１－１．画面デザインで定義したオブジェクトを変数として宣言する。
        followListText = findViewById(R.id.followListText)
        followRecycle = findViewById(R.id.followRecycle)
        followRecycle.layoutManager = LinearLayoutManager(this)

        //１－２．インテント(前画面)から対象ユーザIDと区分（フォロー・フォロワー）を取得する
        val userId = intent.getLongExtra("userId", -1)
        val type = intent.getStringExtra("type") ?: ""
        //１－３．区分がフォローの時
        if (type == "follow") {
            //１－３－１．followListTextをフォローリストと表示する
            followListText.text = "フォローリスト"
        } else if (type == "follower") {//１－４．区分がフォロワーの時
            //１－４－１．followListTextをフォロワーリストと表示する
            followListText.text = "フォロワーリスト"
        }

        //１－５．フォロワー情報取得APIをリクエストして対象ユーザのフォロー・フォロワー情報取得処理を行う
        // HTTP接続用インスタンス生成
        val client = OkHttpClient()
        // JSON形式でパラメータを送るようデータ形式を設定
        val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
        // Bodyのデータ(APIに渡したいパラメータを設定)
        val requestBody = "{" +
                "\"userId\":\"${userId}\"" +
                "\"type\":\"${type}\""
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
                    Toast.makeText(this@FollowListActivity, e.message, Toast.LENGTH_SHORT)
                        .show()
                }
            }

            //１－６．正常にレスポンスを受け取った時(コールバック処理)
            // １－６－１．JSONデータがエラーの場合、受け取ったエラーメッセージを　トースト表示して処理を終了させる
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
                        //１－６ー２．区分がフォローの時
                        if (type == "follow") {
                            //１－６－２－１．フォロー情報一覧が存在する間、以下の処理を繰り返す
                            // 配列をfor文で回して中身を取得
                            for (i in 0 until jsonArray.length()) {
                                val userId = jsonArray.getJSONObject(i).getString("userId")
                                val userName = jsonArray.getJSONObject(i).getString("userName")
                                val whisperCount =
                                    jsonArray.getJSONObject(i).getString("whisperCount")
                                val followCount =
                                    jsonArray.getJSONObject(i).getString("followCount")
                                val followerCount =
                                    jsonArray.getJSONObject(i).getString("followerCount")

                                //１－６－２－１－１．フォロー情報をリストに格納する
                                list.add(
                                    RowData(
                                        userId,
                                        userName,
                                        whisperCount,
                                        followCount,
                                        followerCount
                                    )
                                ) // リストに追加
                            }

                            //１－６ー３．区分がフォロワーの時
                        } else if (type == "follower") {
                            //１－６－３－１．フォロワー情報一覧が存在する間、以下の処理を繰り返す
                            for (i in 0 until jsonArray.length()) {
                                val userId = jsonArray.getJSONObject(i).getString("userId")
                                val userName = jsonArray.getJSONObject(i).getString("userName")
                                val whisperCount =
                                    jsonArray.getJSONObject(i).getString("whisperCount")
                                val followCount =
                                    jsonArray.getJSONObject(i).getString("followCount")
                                val followerCount =
                                    jsonArray.getJSONObject(i).getString("followerCount")

                                //１－６－２－１－１．フォロー情報をリストに格納する
                                list.add(
                                    RowData(
                                        userId,
                                        userName,
                                        whisperCount,
                                        followCount,
                                        followerCount
                                    )
                                ) //１－６－３－１－１．フォロワー情報をリストに格納する
                            }
                        }
                    }
                    //１－６－４．followRecycleにフォロー情報リストまたはフォロワー情報リストをセットする
                    // UIスレッドでRecyclerViewにリストを設定
                    this@FollowListActivity.runOnUiThread {
                        // LinearLayoutManagerを設定し、RecyclerViewを初期化する（productRecyclerViewはonCreateメソッドでfindViewByIdを使い、取得しておきましょう。）
                        followRecycle.layoutManager = LinearLayoutManager(applicationContext)
                        // 作成したlistをアダプターに渡し、RecyclerViewにアダプターを設定する
                        //val adapter = ProductRecycleAdapter(list)
                        //followRecycle.adapter = adapter
                    }

                //１－７．リクエストが失敗した時(コールバック処理)
                } catch (e: Exception) {
                    runOnUiThread {
                        // １－７－１．エラーメッセージをトースト表示する
                        Toast.makeText(this@FollowListActivity, e.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                //２．オプションメニュー生成時
                // ２－１．オーバーフローメニューのオプションメニュー生成メソッドに
                //２－２．戻り値に上記メソッドの戻り値をセットする
                // ３．オプションメニューアイテム選択時
                //３－１．オーバーフローメニューのオプションメニューアイテム選択メソッドに　itemとactivityを渡して呼び出す
                // ３－２．戻り値に親クラスのオプションメニューアイテム選択をセットする
            }
        })

    }
}