package jp.ac.ecc.wisperclient

import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

/**
 * ログインおよびユーザ作成画面以外のヘッダ部に表示するオーバーフローメニュー
 * 各画面で処理が必要なため、このクラスで共通処理できるようにする
 * 各メニューから対象の画面に遷移することができる
 * ただし、Logoutに関してはログアウト処理を行いログイン画面に遷移とする。
 */

class OverflowMenu {
    // １．オプションメニュー生成メソッド
    fun onCreateOptionsMenu(menu: Menu?, activity: AppCompatActivity): Boolean {
        // １－１．引数で受けとったAppCompatActivityのインフレータを取得する
        val inflater: MenuInflater = activity.menuInflater
        // １－２．インフレータにオーバーフローメニューのデザインを設定する
        inflater.inflate(R.menu.overflow_menu, menu)
        // １－３．戻り値にtrueをセットする
        return true
    }

    // ２．オプションメニューアイテム選択メソッド
    fun onOptionsItemSelected(item: MenuItem, activity: AppCompatActivity){
         when (item.itemId){
            // ２－１．受け取ったMenuItemがtimelineの時、タイムライン画面に遷移する
            R.id.timeline -> {
//                val intent = Intent(activity, TimelineActivity::class.java)
                Log.e("Transiton Successed","画面遷移成功")
//                activity.startActivity(intent)

            }

            // ２－２．受け取ったMenuItemがsearchの時、検索画面に遷移する
            R.id.search ->{
//                val intent = Intent(activity, SearchActivity::class.java)
                Log.e("Transiton Successed","画面遷移成功")
//                activity.startActivity(intent)

            }

            // ２－３．受け取ったMenuItemがwhisperの時、ささやき登録画面に遷移する
            R.id.whisper -> {
                val intent = Intent(activity, WhisperActivity::class.java)
                Log.e("Transiton Successed","画面遷移成功")
                activity.startActivity(intent)


            }

            // ２－４．受け取ったMenuItemがmyprofileの時
            R.id.myprofile -> {
                // ２－４－１．インテントにログインユーザIDをセットする
//                val intent = Intent(activity, UserInfoActivity::class.java)
//                intent.putExtra("userId", LoginActivity.loginUserId)
                // ２－４－２．ユーザ情報画面に遷移する
                Log.e("Transiton Successed","画面遷移成功")
//                activity.startActivity(intent)

            }

            // ２－５．受け取ったMenuItemがprofileeditの時、プロフィール編集画面に遷移する
            R.id.profileedit -> {
//                val intent = Intent(activity, UserEditAcritiby::class.java)
                Log.e("Transiton Successed","画面遷移成功")
//                activity.startActivity(intent)

            }

            // ２－６．受け取ったMenuItemがlogoutの時
            R.id.logout -> {
                // ２－６－１．グローバル変数loginUserIdに空文字を格納する
                LoginActivity.loginUserId = ""
                // ２－６－２．ログイン画面に前画面情報をクリアして遷移する
                val intent = Intent(activity, LoginActivity::class.java)
                Log.e("Transiton Successed","画面遷移成功")
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                activity.startActivity(intent)
                activity.finishAffinity()

            }

        }
    }

}