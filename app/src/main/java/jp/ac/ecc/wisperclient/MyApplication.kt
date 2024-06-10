package jp.ac.ecc.wisperclient

import android.app.Application

/**
 * Fan
 * アプリケーションのログインユーザ情報や
 * API接続先情報などアプリケーション全体で利用される
 * グローバル変数の宣言などを行う
 */

// １．Applicationクラスを継承する
class MyApplication : Application() {

    companion object {
        // ２．グローバル変数loginUserIdを文字列型で宣言して、空文字で初期化する
        var loginUserId: String? = ""

        // ３．グローバル変数apiUrlを文字列型で宣言して、各チームのAPIのホームディレクトリで初期化する
        var apiUrl: String = "https://click.ecc.ac.jp/ecc/whisper24_c/"
    }
}