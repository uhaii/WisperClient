package jp.ac.ecc.wisperclient

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.ecc.wisperclient.databinding.UserRowBinding

/**
 * Fan
 * recyclerViewで表示するユーザのフォロー・フォロワー数を表示する行情報
 * ユーザアイコンをタップするとユーザ情報画面に遷移を行う
*/
class UserAdapter(private val userdataset : MutableList<UserRowData>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    // １．ビューホルダー（内部クラス）
    inner class UserViewHolder(
        private val binding: UserRowBinding
    ) : RecyclerView.ViewHolder(binding.root){
        // １－１．画面デザインで定義したオブジェクトを変数として宣言する。
        val userImage = binding.userImage
        val userNameText = binding.userNameText
        val followText = binding.followText
        val followCntText = binding.followCntText
        val followerText = binding.followerText
        val followerCntText = binding.followerCntText
    }

    // ２．ビューホルダー生成時
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        // ２－１．ユーザ行情報の画面デザインを設定する
        val binding = UserRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // ２－２．設定した画面デザインを戻り値にセットする
        return UserViewHolder(binding)
    }

    // ３．ビューホルダーバインド時
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        // ３－１．ビューホルダーのオブジェクトに対象行のデータをセットする
        holder.userNameText.text = userdataset[position].userName
        holder.followCntText.text = userdataset[position].followCount.toString()
        holder.followerCntText.text = userdataset[position].followerCount.toString()

        // ３－２．userImageのクリックイベントリスナーを生成する
        holder.userImage.setOnClickListener{
            // ３－２－１．インテントに対象行のユーザIDをセットする
//            val intent = Intent(holder.itemView.context, UserInfoActivity::class.java)
//            intent.putExtra("userId", userdataset[position].userId)
            // ３－２－２．ユーザ情報画面に遷移する
            Log.e("Transiton Successed","画面遷移成功")
//            holder.itemView.context.startActivity(intent)
        }

    }

    // ４．行数取得時
    override fun getItemCount(): Int {
        // ４－１．行リストの件数を戻り値にセットする
        return userdataset.size
    }

}