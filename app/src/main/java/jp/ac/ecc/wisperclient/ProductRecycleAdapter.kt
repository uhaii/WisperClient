package jp.ac.ecc.wisperclient

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView

//class ProductRecycleAdapter : RecyclerView.Adapter<ProductRecycleAdapter.ViewHolder>() {
    class ViewHolder(item : View) : RecyclerView.ViewHolder(item){
        val userId : TextView
        val userName : TextView
        //val whisperCount : TextView
        //val followCount : TextView
        //val followerCount : TextView

        init {
            userId = item.findViewById(R.id.userIdEdit)
            userName = item.findViewById(R.id.userNameEdit)
            //whisperCount = item.findViewById(R.id.)
        }

    }
//}