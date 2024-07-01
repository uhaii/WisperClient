package jp.ac.ecc.wisperclient

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import jp.ac.ecc.wisperclient.databinding.WhisperRowBinding
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import okio.IOException

/**
 * Fan
 * recyclerViewで表示するささやき内容を表示する行情報
 * ユーザアイコンをタップするとユーザ情報画面に遷移して、
 * イイねボタン(☆)をタップするとイイねの登録・解除を行う
 */
class WhisperAdapter(private val whisperdataset: MutableList<WhisperRowData>) : RecyclerView.Adapter<WhisperAdapter.WhisperViewHolder>(){
    // １．ビューホルダー（内部クラス）
    inner class WhisperViewHolder(
        private val binding: WhisperRowBinding
    ) : RecyclerView.ViewHolder(binding.root){
        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val userImage = binding.userImage
        val userNameText = binding.userNameText
        val whisperText = binding.whisperText
        val goodImage = binding.goodImage
    }

    // ２．ビューホルダー生成時
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WhisperViewHolder {
        // ２－１．ささやき行情報の画面デザインを設定する
        val binding = WhisperRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // ２－２．設定した画面デザインを戻り値にセットする
        return WhisperViewHolder(binding)
    }

    // ３．ビューホルダーバインド時
     override fun onBindViewHolder(holder: WhisperViewHolder, position: Int) {
        // ３－１．ビューホルダーのオブジェクトに対象行のデータをセットする
        holder.userNameText.text = whisperdataset[position].userName
        holder.whisperText.text = whisperdataset[position].content
        // ３－２．イイね対象のデータは、イイねボタンの画像をイイね状態にする
        if (whisperdataset[position].goodFlg){
            holder.goodImage.setImageResource(R.drawable.star_marked)
        }else {
            holder.goodImage.setImageResource(R.drawable.star)
        }
        val context = holder.itemView.context

        //TODO: ３－３．userImageのクリックイベントリスナーを生成する
        holder.userImage.setOnClickListener {
            val intent = Intent(context, UserInfoActivity::class.java)
            // ３－３－１．呼び出された画面がユーザ情報画面の時
            // TODO:確認必要あり　３－３－１－１．ユーザ情報画面から対象ユーザIDを取得する
            val userInfoId = intent.getIntExtra("userId", -1).toString()
            // ３－３－１－２．対象ユーザIDと対象行のユーザIDが違う時
            if (!whisperdataset[position].userId.equals(userInfoId) ){
                // ３－３－１－２－１．インテントに対象行のユーザIDをセットする
                intent.putExtra("userId",whisperdataset[position].userId)
                // ３－３－１－２－２．ユーザ情報画面に遷移する
                context.startActivity(intent)
            }
            // ３－３－２．呼び出された画面がタイムライン画面の時
            // ３－３－２－１．インテントに対象行のユーザIDをセットする
            intent.putExtra("userId", whisperdataset[position].userId)
            // ３－３－２－２．ユーザ情報画面に遷移する
            context.startActivity(intent)
            Log.e("Transiton Successed","画面遷移成功")
        }
        // ３－４．goodImageのクリックイベントリスナーを生成する
        holder.goodImage.setOnClickListener {
            // ３－４－１．イイね管理処理APIをリクエストして入力した対象行のささやきのイイねの登録・解除を行う
            // HTTP接続用インスタンス生成
            val client = OkHttpClient()
            // JSON形式でパラメータを送るようデータ形式を設定
            val mediaType : MediaType = "application/json; charset=utf-8".toMediaType()
            // Bodyのデータ(APIに渡したいパラメータを設定)
            val requestBody = "{" +
                    "\"userId\":\"${whisperdataset[position].userId}\"," +
                    "\"whisperNo\":\"${whisperdataset[position].whisperNo}\"," +
                    "\"goodFlg\":\"${whisperdataset[position].goodFlg}\""
            "}"
            // Requestを作成(先ほど設定したデータ形式とパラメータ情報をもとにリクエストデータを作成)
            val request = Request.Builder().url("${MyApplication.apiUrl}goodCtl.php").post(requestBody.toRequestBody(mediaType)).build()

            Log.e("successed send", "転送成功")

            client.newCall(request!!).enqueue(object : Callback{
                // ３－４－３．リクエストが失敗した時(コールバック処理)
                override fun onFailure(call: Call, e: IOException) {
                    // ３－４－３－１．エラーメッセージをトースト表示する
                    Toast.makeText(context, e.message ,Toast.LENGTH_SHORT).show()
                }

                // ３－４－２．正常にレスポンスを受け取った時(コールバック処理)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        //TODO: ３－４－２－２．呼び出された画面がユーザ情報画面の時
                        // ユーザ情報画面のユーザささやき情報取得API共通実行メソッドを呼び出す
                        if (context is UserInfoActivity){
                            context.getWhisperInfo(
                                context.application as MyApplication,
                                whisperdataset[position].userId,
                                MyApplication.loginUserId.toString(),
                                context.binding.userNameText,
                                context.binding.profileText,
                                context.binding.followCntText,
                                context.binding.followerCntText,
                                context.binding.followButton,
                                context.binding.userRecycle,
                                context.binding.radiogroup2
                            )
                        }
                        //TODO: ３－４－２－３．呼び出された画面がタイムライン画面の時
                        // タイムライン画面のタイムライン情報取得API共通実行メソッドを呼び出す
                        if (context is TimelineActivity){
                            context.getTimelineInfo(context.application as MyApplication, whisperdataset[position].userId, context.binding.timelineRecycle)
                        }

                    } catch (e : Exception){
                        // ３－４－２－１．JSONデータがエラーの場合、受け取ったエラーメッセージをトースト表示して処理を終了させる
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }

                }

            })
        }
        
     }

    // ４．行数取得時
    override fun getItemCount(): Int {
        return whisperdataset.size
    }


}