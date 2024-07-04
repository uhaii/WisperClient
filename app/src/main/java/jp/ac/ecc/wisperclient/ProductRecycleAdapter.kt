package jp.ac.ecc.wisperclient

import android.service.autofill.Dataset
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

class ProductRecycleAdapter (private val dataset: MutableList<RowData>) : RecyclerView.Adapter<ProductRecycleAdapter.ViewHolder>() {
    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val userId : TextView
        val userName : TextView
        //val whisperCount : TextView
        //val followCount : TextView
        //val followerCount : TextView

        init {
            userId = item.findViewById(R.id.userIdText)
            userName = item.findViewById(R.id.userNameText)
            //whisperCount = item.findViewById(R.id.)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //1行分のデータのイメージ(user_row)を読み込み、ViewHolderに設定
        val View = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        return ViewHolder(View)
    }

    override fun getItemCount(): Int {
        //データ件数を返す
        return dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //取得したデータを画面に表示している項目に設定する
        holder.userId.text = dataset[position].userId
        holder.userName.text = dataset[position].userName
    }
}