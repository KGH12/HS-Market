package com.example.firebaseproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignupActivity : AppCompatActivity() {

    private val db: FirebaseFirestore = Firebase.firestore
    private val usersCollectionRef = db.collection("users")

    private val editUserEmail by lazy { findViewById<EditText>(R.id.editTextSignupEmail) }
    private val editPassword by lazy { findViewById<EditText>(R.id.editTextSignupPW) }
    private val editUserName by lazy { findViewById<EditText>(R.id.editTextSignupUserName) }
    private val editDateOfBirth by lazy { findViewById<EditText>(R.id.editTextSignupDateOfBirth) }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        // 회원가입 확인 버튼
        findViewById<Button>(R.id.buttonSignup)?.setOnClickListener {
            signUp()
        }

        // 취소 버튼
        findViewById<Button>(R.id.buttonCancel)?.setOnClickListener {
            finish()
        }
    }

    private fun signUp() {

        val email = editUserEmail.text.toString()
        if (email.isEmpty()) {
            Snackbar.make(editUserEmail, "이메일을 입력하세요!", Snackbar.LENGTH_SHORT).show()
            return
        }
        val password = editPassword.text.toString()
        if (password.isEmpty()) {
            Snackbar.make(editPassword, "비밀번호를 입력하세요!", Snackbar.LENGTH_SHORT).show()
            return
        }
        val name = editUserName.text.toString()
        if (name.isEmpty()) {
            Snackbar.make(editUserName, "이름을 입력하세요!", Snackbar.LENGTH_SHORT).show()
            return
        }
        val dateOfBirth = editDateOfBirth.text.toString()
        if (dateOfBirth.isEmpty()) {
            Snackbar.make(editDateOfBirth, "생년월일을 입력하세요!", Snackbar.LENGTH_SHORT).show()
            return
        }

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { // it: Task<AuthResult!>
                if (it.isSuccessful) {
                    val userMap = hashMapOf(
                        "uid" to Firebase.auth.currentUser?.uid,
                        "userName" to name,
                        "userDateOfBirth" to dateOfBirth
                    )
                    usersCollectionRef.add(userMap).addOnSuccessListener { }
                        .addOnFailureListener { }
                    startActivity(
                        Intent(this, MainActivity::class.java)
                    )
                    finish()
                } else {
                    Log.w("SignUpActivity", "signUpWithEmail", it.exception)
                    Toast.makeText(this, "아이디를 이메일 형식으로 작성해주세요!", Toast.LENGTH_SHORT).show()
                }
            }
    }
}