package tn.esprit.gestionuser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button resetPasswordButton;
    private UserDatabaseHelper userDatabaseHelper;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        resetPasswordButton = findViewById(R.id.resetPasswordButton);
        userDatabaseHelper = new UserDatabaseHelper(this);

        username = getIntent().getStringExtra("username");
        resetPasswordButton.setOnClickListener(v -> resetPassword());
    }

    private void resetPassword() {
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_LONG).show();
            return;
        }


        boolean isPasswordReset = userDatabaseHelper.updateUserPassword(username, newPassword);
        if (isPasswordReset) {
            Toast.makeText(this, "Password reset successfully.", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, SignIn.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this, "Failed to reset password.", Toast.LENGTH_LONG).show();
        }
    }




}
