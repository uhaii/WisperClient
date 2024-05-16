package jp.ac.ecc.wisperclient

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Email
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class UserActivity : AppCompatActivity() {

    private lateinit var usernameEditText: EditText
    private lateinit var EmailAddress: EditText
    private lateinit var PasswordEditText: EditText
    private lateinit var confirmPassword: EditText
    private lateinit var createbutton: Button
    private lateinit var cancelbutton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // Viewの初期化
        usernameEditText = findViewById(R.id.usernameEditText)
        EmailAddress = findViewById(R.id.EmailAddress)
        PasswordEditText = findViewById(R.id.PasswordEditText)
        confirmPassword = findViewById(R.id.confirmPassword)
        createbutton = findViewById(R.id.createbutton)
        cancelbutton = findViewById(R.id.cancelbutton)

        // ユーザー作成ボタンのクリックリスナーを設定
        createbutton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = EmailAddress.text.toString()
            val password = PasswordEditText.text.toString()
            val repassword = confirmPassword.text.toString()

            // 入力チェック
            when {
                username.isEmpty() || email.isEmpty() || password.isEmpty() || repassword.isEmpty() -> {
                    Toast.makeText(this, "すべて必須項目です", Toast.LENGTH_SHORT).show()
                }

                !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                    Toast.makeText(this, "無効なメールアドレス", Toast.LENGTH_SHORT).show()
                }

                password != repassword -> {
                    Toast.makeText(this, "パスワードが一致していません", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    // ユーザーを作成するメソッドを呼び出す
                    createUser(username, email, password)
                }
            }
        }

        // キャンセルボタンのクリックリスナーを設定
        cancelbutton.setOnClickListener {
            finish() // 現在のアクティビティを終了して前の画面に戻る
        }
    }

    private fun createUser(username: String, email: String, password: String) {
        // ここにユーザーを作成する処理を記述します
        // この例では、単に成功メッセージを表示します
        Toast.makeText(this, "User created successfully: $username, $email", Toast.LENGTH_SHORT).show()
    }

}


