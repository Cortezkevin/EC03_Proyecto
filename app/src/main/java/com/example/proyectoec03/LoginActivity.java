package com.example.proyectoec03;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {

    MaterialButton login;
    TextInputLayout text_layout_username, text_layout_password;
    TextInputEditText text_edit_username, text_edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.login);
        text_layout_username = findViewById(R.id.text_layout_username);
        text_layout_password = findViewById(R.id.text_layout_password);
        text_edit_username = findViewById(R.id.text_edit_username);
        text_edit_password = findViewById(R.id.text_edit_password);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (text_edit_username.getText().length() == 0 && text_edit_password.getText().length() == 0) {
                    text_layout_username.setError("Complete este campo");
                    text_layout_password.setError("Complete este campo");
                } else if (text_edit_username.getText().length() == 0) {
                    text_layout_password.setError(null);
                    text_layout_username.setError("Complete este campo");
                } else if (text_edit_password.getText().length() == 0) {
                    text_layout_username.setError(null);
                    text_layout_password.setError("Complete este campo");
                } else {
                    text_layout_username.setError(null);
                    text_layout_password.setError(null);
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(LoginActivity.this, "Sesion iniciada", Toast.LENGTH_SHORT).show();
                }
            }
        });

        text_edit_username.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int valid_username = isValidData(text_edit_username.getText());
                int valid_password = isValidData(text_edit_password.getText());
                if (valid_username == 1) {
                    text_layout_username.setError("Ingrese al menos 6 caracteres");
                } else if (valid_password == 1) {
                    text_layout_password.setError("Ingrese al menos 6 caracteres");
                } else if (valid_username == 0) {
                    text_layout_username.setError(null);
                } else if (valid_password == 0) {
                    text_layout_password.setError(null);
                }
                return false;
            }
        });
    }

    public int isValidData(Editable text) {
        if (text.length() > 0 && text.length() < 6) {
            return 1;
        }
        return 0;
    }

}