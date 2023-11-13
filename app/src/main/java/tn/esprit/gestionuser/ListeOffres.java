package tn.esprit.gestionuser;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ListeOffres extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offre);
        displayOffers();

        final Button buttonOpenForm = findViewById(R.id.button_open_form);
       // buttonOpenForm.setVisibility(View.GONE);

        // In your DetailsHotel activity's onCreate method:

        buttonOpenForm.setOnClickListener(v -> {
            // Inflate the dialog with custom view
            final View dialogView = LayoutInflater.from(ListeOffres.this)
                    .inflate(R.layout.add_hotel_form, null);
            // AlertDialog for the form
            final AlertDialog dialog = new AlertDialog.Builder(ListeOffres.this)
                    .setView(dialogView)
                    .create();

            dialog.show();

            Button buttonSubmit = dialogView.findViewById(R.id.button_submit);
            buttonSubmit.setOnClickListener(view -> {
                // Retrieve the EditText fields and CheckBoxes
                EditText editTextName = dialogView.findViewById(R.id.edittext_name);
                EditText editTextDetails = dialogView.findViewById(R.id.edittext_details);
                EditText editTextLocation = dialogView.findViewById(R.id.edittext_location);
                EditText editTextPrice = dialogView.findViewById(R.id.edittext_price);

                String name = editTextName.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String details = editTextDetails.getText().toString().trim();
                Float price = null;
                try {
                    price = Float.parseFloat(editTextPrice.getText().toString().trim());
                } catch (NumberFormatException e) {
                    Toast.makeText(ListeOffres.this, "Please enter a valid price", Toast.LENGTH_SHORT).show();
                    return; // Exit the method early if price is not valid
                }

                // Assuming you have an 'options' field in your offer table to store the selected options
                StringBuilder selectedOptions = new StringBuilder();
                if (((CheckBox) dialogView.findViewById(R.id.checkbox_option1)).isChecked()) selectedOptions.append("Pool,");
                if (((CheckBox) dialogView.findViewById(R.id.checkbox_option2)).isChecked()) selectedOptions.append("GYM,");
                if (((CheckBox) dialogView.findViewById(R.id.checkbox_option3)).isChecked()) selectedOptions.append("SPA,");
                if (((CheckBox) dialogView.findViewById(R.id.checkbox_option4)).isChecked()) selectedOptions.append("Free WIFI,");

                // Remove the last comma if there are selected options
                if (selectedOptions.length() > 0) {
                    selectedOptions.setLength(selectedOptions.length() - 1);
                }

                // Create an instance of UserDatabaseHelper and add the offer
                UserDatabaseHelper dbHelper = new UserDatabaseHelper(ListeOffres.this);
                dbHelper.addOffer(name, details, location, price);

                // Dismiss the dialog after submission
                displayOffers();

                dialog.dismiss();
            });
            displayOffers();

        });

    }
    private void displayOffers() {
        UserDatabaseHelper dbHelper = new UserDatabaseHelper(ListeOffres.this);
        ArrayList<Offer> offers = dbHelper.getAllOffers2();

        LinearLayout offerContainer = findViewById(R.id.offerContainer);
        offerContainer.removeAllViews(); // Clear existing views

        // Inflate the layout for the static elements (rectangle and button)
        View headerView = getLayoutInflater().inflate(R.layout.activity_offre, null);
        offerContainer.addView(headerView);

        for (Offer offer : offers) {
            // Inflate the offer card layout for each offer
            View offerCardView = getLayoutInflater().inflate(R.layout.offer_layout, null);

            // Set offer details
            //ImageView offerImageView = offerCardView.findViewById(R.id.imageView);
            TextView offerName = offerCardView.findViewById(R.id.offerName);
            TextView offerLocation = offerCardView.findViewById(R.id.offerLocation);
            TextView offerPrice = offerCardView.findViewById(R.id.offerPrice);

            // Assuming you have an 'image' field in your Offer class
            // Replace 'setImageResource' with the appropriate method based on your data
            // offerImageView.setImageResource(offer.getImage());

            // Set the text data
            offerName.setText(offer.getName());
            offerLocation.setText(offer.getLocation());
            offerPrice.setText(String.format("$%s/night", String.valueOf(offer.getPrice())));
            offerPrice.setTextColor(Color.BLACK);

            // Add the inflated offer card view to the container
            offerContainer.addView(offerCardView);

            // Set a click listener for each offer card
            offerCardView.setOnClickListener(v -> {
                // Handle offer card click, if needed
                // You can access offer details using the offer object
                Toast.makeText(ListeOffres.this, "Clicked on: " + offer.getName(), Toast.LENGTH_SHORT).show();
            });
        }
    }













}
