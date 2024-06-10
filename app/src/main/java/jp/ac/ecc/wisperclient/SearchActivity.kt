package jp.ac.ecc.wisperclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import jp.ac.ecc.wisperclient.databinding.ActivitySearchBinding
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
 * 検索キーワードからユーザおよびWhisperの検索を行う画面
 * ラジオボタンの選択によりユーザ検索なのかWhisper検索なのか選択できる。
 */

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    var overflowMenu = OverflowMenu()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val searchText = binding.searchText
        val radioGroup = binding.radioGroup
        val userRadio = binding.userRadio
        val whisperRadio = binding.whisperRadio
        val searchEdit = binding.searchEdit
        val searchButton = binding.searchButton
        val searchRecycle = binding.searchRecycle

        // １－２．searchButtonのクリックイベントリスナーを作成する
        searchButton.setOnClickListener {
            // １－２－１．入力項目が空白の時、エラーメッセージをトースト表示して処理を終了させる
            if (searchEdit.text.isEmpty()){
                Toast.makeText(this, R.string.search_blank_error_message, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // １－２－２．検索結果取得APIをリクエストして検索キーワードに該当する情報取得を行う
            // HTTP接続用インスタンス生成
            val client = OkHttpClient()
            // JSON形式でパラメータを送るようデータ形式を設定
            val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
            // TODO:確認　Bodyのデータ(APIに渡したいパラメータを設定)
            val requestBody =  "{\"content\":\"${searchEdit.text}\"}"
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            val request = Request.Builder().url("${MyApplication.apiUrl}search.php").post(requestBody.toRequestBody(mediaType)).build()

            client.newCall(request).enqueue(object : Callback{
                // １－２－４．リクエストが失敗した時(コールバック処理)
                override fun onFailure(call: Call, e: IOException) {
                    // １－２－４－１．エラーメッセージをトースト表示する
                    Toast.makeText(this@SearchActivity, e.message, Toast.LENGTH_SHORT).show()
                }

                // １－２－３．正常にレスポンスを受け取った時(コールバック処理)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        // APIから受け取ったデータを文字列で取得
                        val responseBody = response.body?.string()
                        // APIから取得してきたJSON文字列をJSONオブジェクトに変換
                        val json = JSONObject(responseBody)
                        // RecyclerViewに設定するリストを作成
                        val userlist = mutableListOf<UserRowData>()
                        val goodlist = mutableListOf<GoodRowData>()


                        // １－２－３－２．ユーザ情報一覧(API側のこと)が存在する間、以下の処理を繰り返す
                        if (json.has("userList")){
                            // １－２－３－２－１．ユーザ情報をリストに格納する
                            // JSONオブジェクトの中から取得
                            val userList = json.getString("userList")
                            // 取得した文字列は配列の構成になっているので、JSON配列に変換
                            val jsonArray = JSONArray(userList)
                            // 配列をfor文で回して中身を取得
                            for (i in 0 until jsonArray.length()){
                                val userId = jsonArray.getJSONObject(i).getString("userId")
                                val userName = jsonArray.getJSONObject(i).getString("userName")
                                val followCount= jsonArray.getJSONObject(i).getString("followCount").toInt()
                                val followerCount = jsonArray.getJSONObject(i).getString("followerCount").toInt()
                                userlist.add(UserRowData(userId,userName,followCount,followerCount))
                            }
                        }

                        // １－２－３－３．イイね情報一覧が存在する間、以下の処理を繰り返す
                        if (json.has("whisperList")){
                            // １－２－３－３－１．イイね情報をリストに格納する
                            // JSONオブジェクトの中から取得
                            val whisperList = json.getString("whisperList")
                            // 取得した文字列は配列の構成になっているので、JSON配列に変換
                            val jsonArray = JSONArray(whisperList)
                            // 配列をfor文で回して中身を取得
                            for (i in 0 until jsonArray.length()){
                                val whisperNo= jsonArray.getJSONObject(i).getString("whisperNo").toInt()
                                val content = jsonArray.getJSONObject(i).getString("content")
                                val userId = jsonArray.getJSONObject(i).getString("userId")
                                val userName = jsonArray.getJSONObject(i).getString("userName")
                                val goodCount = jsonArray.getJSONObject(i).getString("goodCount").toInt()
                                goodlist.add(GoodRowData(whisperNo,content,userId,userName,goodCount))
                            }
                        }

                        // １－２－３．searchRecycle
                        this@SearchActivity.runOnUiThread {
                            // RecyclerViewを初期化する
                            searchRecycle.layoutManager = LinearLayoutManager(applicationContext)

                            // １－２－３－１．ラジオボタンがuserRadioを選択している時ユーザ行情報のアダプターにユーザ情報リストをセットする
                            if (userRadio.isChecked){
                                searchRecycle.adapter = UserAdapter(userlist)
                                Log.e("Search Successed","ユーザー検索成功")
                            }
                            // １－２－３－２．ラジオボタンがwhisperRadioを選択している時イイね行情報のアダプターにイイね情報リストをセットする
                            else if (whisperRadio.isChecked){
                                searchRecycle.adapter = GoodAdapter(goodlist)
                                Log.e("Search Successed","ささやき検索成功")
                            }
                        }

                    } catch (e : Exception){
                        // １－２－３－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                        Toast.makeText(this@SearchActivity, e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            })
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

