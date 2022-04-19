package com.example.rssreaderjava.authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.rssreaderjava.R;
import com.example.rssreaderjava.RSSMainActivity;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private Button btnRegister;
    private EditText edtEmail, edtPassword;
    private TextView tvLogin;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regsiter);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        btnRegister = findViewById(R.id.btnRegister);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(v -> {
               emailRegister();
        });

        tvLogin = findViewById(R.id.loginText);
        tvLogin.setOnClickListener(v -> {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                setResult(Activity.RESULT_OK, intent);
                finish();
        });

    }

    public boolean validateData( String emailString, String passwordString) {
        if (TextUtils.isEmpty(emailString) || TextUtils.isEmpty(passwordString)) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (edtEmail.getError() != null || edtPassword.getError() != null )
            return false;
        return true;
    }


    private void emailRegister() {
        String email, password;
        email = edtEmail.getText().toString();
        password = edtPassword.getText().toString();

        validateData(email,password);

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Tạo tài khoản thành công!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, RSSMainActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Email đã tồn tại!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
