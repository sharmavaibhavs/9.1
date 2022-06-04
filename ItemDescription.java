package com.example.sit305_7_1p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Arrays;
import java.util.List;

public class ItemDescription extends AppCompatActivity {

    //these are the variables for the ui elements
    TextView name, phone ,date, location, desc ;
    Button saveButton;
    RadioButton lost, found;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_description);

        DbModel db = new DbModel(this);

        name = findViewById(R.id.Name);
        phone = findViewById(R.id.phone);
        desc = findViewById(R.id.desc);
        date = findViewById(R.id.date);
        location = findViewById(R.id.location);
        lost = findViewById(R.id.LostRadio);
        found = findViewById(R.id.FoundRadio);
        saveButton = findViewById(R.id.button4);

        Intent intent = new Intent(this, MainActivity.class);


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Type = "";
                String Name = name.getText().toString();
                int Phone = 0;
                String Description = desc.getText().toString();
                String Date = date.getText().toString();
                String Location = location.getText().toString();
                DataModel dataCollected;
                boolean dataFound = true;

                if (lost.isChecked() == true){
                    Type = "Lost";
                }else if (found.isChecked() == true){
                    Type = "Found";
                }else{
                    dataFound = false;
                }
                if (!phone.getText().toString().equals("")){
                    Phone = Integer.parseInt(phone.getText().toString());
                }else{
                    dataFound = false;
                }
                if (name.getText().toString().equals("")||desc.getText().toString().equals("")
                        ||date.getText().toString().equals("")||location.getText().toString().equals("")){
                    dataFound = false;
                }

                if (dataFound == true){

                    dataCollected = new DataModel(Type,Name,Phone,Description,Date,Location);
                    db.InsertAlert(dataCollected);

                    startActivity(intent);
                    Toast.makeText(ItemDescription.this, "Item information stored successful !", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getApplicationContext(), "Please fill all fields.", Toast.LENGTH_SHORT).show();
                }



            }
        });

        private void Getlocation() {
            ProgressDialog progressDialog = ProgressDialog.show(this," get location",true);
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);
            @SuppressWarnings("MissingPermission") final Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener(new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();
                        if (!likelyPlaces.getPlaceLikelihoods().isEmpty()) {
                            Place place = null;
                            for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                                place = placeLikelihood.getPlace();
                            }
                            if (null == place) {
                                Toast.makeText(getApplicationContext(), "Please enter data!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            mLatLng = place.getLatLng();
                            mlocation.setText(place.getAddress());
                            if(null != place.getLatLng()) {
                                l1 = place.getLatLng().latitude;
                                l2 = place.getLatLng().longitude;
                            }

                            Toast.makeText(getApplicationContext(), "获取位置信息成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "获取不到可用位置", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "获取不到可用位置", Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }

    }
}