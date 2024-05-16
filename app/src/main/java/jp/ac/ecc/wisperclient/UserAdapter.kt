package jp.ac.ecc.wisperclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import jp.ac.ecc.wisperclient.databinding.UserRowBinding

/**
 * recyclerViewで表示するユーザのフォロー・フォロワー数を表示する行情報
 * ユーザアイコンをタップするとユーザ情報画面に遷移を行う
*/
class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
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

    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

}