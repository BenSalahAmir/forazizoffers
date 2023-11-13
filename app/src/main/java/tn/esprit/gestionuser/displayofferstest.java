package tn.esprit.gestionuser;

        import android.database.Cursor;
        import android.os.Bundle;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;
        import androidx.appcompat.app.AppCompatActivity;
        import java.util.ArrayList;

public class displayofferstest extends AppCompatActivity {

    private ListView listView;
    private UserDatabaseHelper userDatabaseHelper;
    private ArrayList<String> offreList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_offer_test);

        listView = findViewById(R.id.listViewOffers);
        userDatabaseHelper = new UserDatabaseHelper(this);
        offreList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, offreList);
        listView.setAdapter(adapter);

        loadOfferss();
    }

    private void loadOfferss() {
        Cursor cursor = userDatabaseHelper.getAllOffers();
        if (cursor.moveToFirst()) {
            int userNameIndex = cursor.getColumnIndex(UserDatabaseHelper.COLUMN_OFFER_NAME);
            int userEmailIndex = cursor.getColumnIndex(UserDatabaseHelper.COLUMN_OFFER_DETAILS);
            int userPasswordIndex = cursor.getColumnIndex(UserDatabaseHelper.COLUMN_OFFER_LOCATION);

            if (userNameIndex != -1 && userEmailIndex != -1 && userPasswordIndex != -1) {
                do {
                    String userName = cursor.getString(userNameIndex);
                    String userEmail = cursor.getString(userEmailIndex);
                    String userPassword = cursor.getString(userPasswordIndex);
                    String userInfo = "Name: " + userName + ", Email: " + userEmail + ", Password: " + userPassword;
                    offreList.add(userInfo);
                } while (cursor.moveToNext());
            }
        }
        cursor.close();

        adapter.notifyDataSetChanged();
    }


}
