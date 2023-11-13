package tn.esprit.gestionuser;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class DisplayUsersActivity extends AppCompatActivity {

    private ListView listView;
    private UserDatabaseHelper userDatabaseHelper;
    private ArrayList<String> userList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_users);

        listView = findViewById(R.id.listViewUsers);
        userDatabaseHelper = new UserDatabaseHelper(this);
        userList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, userList);
        listView.setAdapter(adapter);

        loadUsers();
    }

    private void loadUsers() {
        Cursor cursor = userDatabaseHelper.getAllUsers();
        if (cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_NAME);
            int userEmailIndex = cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_EMAIL);
            int userPasswordIndex = cursor.getColumnIndex(UserDatabaseHelper.COLUMN_USER_PASSWORD);

            if (userNameIndex != -1 && userEmailIndex != -1 && userPasswordIndex != -1) {
                do {
                    String userName = cursor.getString(userNameIndex);
                    String userEmail = cursor.getString(userEmailIndex);
                    String userPassword = cursor.getString(userPasswordIndex);
                    String userInfo = "Name: " + userName + ", Email: " + userEmail + ", Password: " + userPassword;
                    userList.add(userInfo);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }


}
