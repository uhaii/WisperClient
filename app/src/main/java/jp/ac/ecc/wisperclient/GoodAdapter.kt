package jp.ac.ecc.wisperclient

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.ecc.wisperclient.databinding.GoodRowBinding
import jp.ac.ecc.wisperclient.databinding.UserRowBinding

/**
 * Fan
 * recyclerViewで表示するWhisperのイイね数を表示する行情報
 * ユーザアイコンをタップするとユーザ情報画面に遷移を行う
 */

class GoodAdapter(private val gooddataset : MutableList<GoodRowData>) : RecyclerView.Adapter<GoodAdapter.GoodViewHolder>(){
    // １．ビューホルダー（内部クラス）
    inner class GoodViewHolder(
        private val binding: GoodRowBinding
    ) : RecyclerView.ViewHolder(binding.root){
        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val userImage = binding.userImage
        val userNameText = binding.userNameText
        val whisperText = binding.whisperText
        val goodText = binding.goodText
        val goodCntText = binding.goodCntText
    }

    // ２．ビューホルダー生成時
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GoodViewHolder {
        // ２－１．イイね行情報の画面デザインを設定する
        val binding = GoodRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // ２－２．設定した画面デザインを戻り値にセットする
        return GoodViewHolder(binding)
    }

    // ３．ビューホルダーバインド時
    override fun onBindViewHolder(holder: GoodViewHolder, position: Int) {
        // ３－１．ビューホルダーのオブジェクトに対象行のデータをセットする
        holder.whisperText.text = gooddataset[position].content
        holder.userNameText.text = gooddataset[position].userName
        holder.goodCntText.text = gooddataset[position].goodCount.toString()

        // ３－２．userImageのクリックイベントリスナーを生成する
        holder.userImage.setOnClickListener {
            // ３－２－１．インテントに対象行のユーザIDをセットする
//            val intent = Intent(holder.itemView.context, UserInfoActivity::class.java)
//            intent.putExtra("userId", gooddataset[position].userId)
            // ３－２－２．ユーザ情報画面に遷移する
            Log.e("Transiton Successed","画面遷移成功")
//            holder.itemView.context.startActivity(intent)
        }
    }

    // ４．行数取得時
    override fun getItemCount(): Int {
        // ４－１．行リストの件数を戻り値にセットする
        return gooddataset.size
    }

}