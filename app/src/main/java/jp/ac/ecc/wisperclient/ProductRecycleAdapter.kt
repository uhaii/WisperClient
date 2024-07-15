package jp.ac.ecc.wisperclient

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductRecycleAdapter (private val dataset: MutableList<RowData>) : RecyclerView.Adapter<ProductRecycleAdapter.ViewHolder>() {
    // 画面デザインで定義したオブジェクトを変数として宣言する。
    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val userImage : ImageView
        val userNameText : TextView
        val followText : TextView
        val followerText : TextView
        val followCntText : TextView
        val followerCntText : TextView

        init {
            userImage = item.findViewById(R.id.userImage)
            userNameText = item.findViewById(R.id.userNameText)
            followText = item.findViewById(R.id. followText)
            followerText = item.findViewById(R.id.followerText)
            followCntText = item.findViewById(R.id.followCntText)
            followerCntText = item.findViewById(R.id.followerCntText)
        }
    }

    // ユーザ行情報の画面デザインを設定する
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //1行分のデータのイメージ(user_row)を読み込み、ViewHolderに設定
        val View = LayoutInflater.from(parent.context).inflate(R.layout.user_row, parent, false)
        // 設定した画面デザインを戻り値にセットする
        return ViewHolder(View)
    }

    // ビューホルダーのオブジェクトに対象行のデータをセットする
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      // GlideでURLから取得した画像をuserImageに格納する
        Glide.with(holder.itemView)
            .load(dataset[position].userImage)
            .into(holder.userImage)

        holder.userNameText.text = dataset[position].userName
        holder.followText.text = dataset[position].followText
        holder.followerText.text = dataset[position].followerText
        holder.followCntText.text = dataset[position].followCount
        holder.followerCntText.text = dataset[position].followerCount

        // クリックイベントリスナーを設定する
        holder.userImage.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UserInfoActivity::class.java)
            intent.putExtra("userId",dataset[position].userId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        //データ件数を返す
        return dataset.size
    }
}