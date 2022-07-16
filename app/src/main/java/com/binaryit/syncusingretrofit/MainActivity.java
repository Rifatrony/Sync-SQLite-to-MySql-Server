package com.binaryit.syncusingretrofit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;

    FloatingActionButton fab;
    EditText nameEditText, numberEditText, ageEditText;

    Button saveButton;

    String name, number, age;
    MyDatabaseHelper databaseHelper;
    List<Model> modelList;
    MyAdapter adapter;

    BroadcastReceiver broadcastReceiver = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        assert wifiManager != null;
        String ip = Formatter.formatIpAddress(wifiManager.getConnectionInfo().getIpAddress());
        System.out.println(String.format("\n\nIP Address: %s", ip));


        broadcastReceiver = new NetworkMonitor();
        InternetStatus();

        recyclerView = findViewById(R.id.recyclerView);

        modelList = new ArrayList<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MyAdapter(this, modelList);
        recyclerView.setAdapter(adapter);

        databaseHelper = new MyDatabaseHelper(MainActivity.this);

        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.add_data_layout);

                nameEditText = dialog.findViewById(R.id.nameEditText);
                numberEditText = dialog.findViewById(R.id.numberEditText);
                ageEditText = dialog.findViewById(R.id.ageEditText);

                saveButton = dialog.findViewById(R.id.saveButton);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        name = nameEditText.getText().toString().trim();
                        number = numberEditText.getText().toString().trim();
                        age = ageEditText.getText().toString().trim();

                        if (name.isEmpty()){
                            Toast.makeText(MainActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (number.isEmpty()){
                            Toast.makeText(MainActivity.this, "Enter Number", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (age.isEmpty()){
                            Toast.makeText(MainActivity.this, "Enter Age", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        else {
                            SaveDataToServer();
                            dialog.dismiss();
                        }
                    }
                });

                dialog.show();
            }
        });

        ReadDataFromLocalDatabase();

    }

    public void InternetStatus(){
        registerReceiver(broadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    @SuppressLint("NotifyDataSetChanged")
    private void ReadDataFromLocalDatabase() {

        modelList.clear();

        Cursor cursor = databaseHelper.ReadDataFromLocalStorage();

        if (cursor.getCount() == 0 ){
            Toast.makeText(this, "No Data Found", Toast.LENGTH_SHORT).show();
        }
        else {
            while (cursor.moveToNext()){

                Model obj = new Model(cursor.getString(0), cursor.getString(1),cursor.getString(2),cursor.getString(3));
                modelList.add(obj);

            }
            adapter.notifyDataSetChanged();
            cursor.close();
            databaseHelper.close();
        }


    }

    private void SaveDataToServer() {
        if (checkNetworkConnection()){

            SaveDataToTheServerDatabase();
            Toast.makeText(this, "Connected to the Network", Toast.LENGTH_SHORT).show();
        }
        else {
            SaveDataToLocalStorage();
            Toast.makeText(this, "No Connected to the Network", Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveDataToTheServerDatabase() {

        RetrofitClient.getRetrofitClient().SendToServer(name, number, age).enqueue(new Callback<AddResponse>() {
            @Override
            public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {
                if (response.isSuccessful()){
                    Toast.makeText(MainActivity.this, "Data Send to server", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(MainActivity.this, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AddResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, t.getMessage().toString(), Toast.LENGTH_SHORT).show();
                System.out.println("\n\n\n\n " + t.getMessage().toString());
            }
        });
    }

    private void SaveDataToLocalStorage() {
        databaseHelper = new MyDatabaseHelper(this);
        databaseHelper.saveToLocalDatabase(name, number, age);
        databaseHelper.close();
    }

    public boolean checkNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }
}