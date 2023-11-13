package tn.esprit.gestionuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText emailEditText;
    private Button submitButton;
    private UserDatabaseHelper userDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        submitButton = findViewById(R.id.submitButton);
        userDatabaseHelper = new UserDatabaseHelper(this);

        submitButton.setOnClickListener(v -> verifyAccount());
    }

    private void verifyAccount() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();

        boolean isValidUser = userDatabaseHelper.checkUserExists(username, email);
        if (isValidUser) {
            Intent resetIntent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
            resetIntent.putExtra("username", username);
            startActivity(resetIntent);
        } else {
            Toast.makeText(this, "Invalid username or email.", Toast.LENGTH_LONG).show();
        }
    }
}
