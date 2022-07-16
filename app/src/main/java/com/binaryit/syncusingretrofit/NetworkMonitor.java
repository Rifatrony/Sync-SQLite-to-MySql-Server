package com.binaryit.syncusingretrofit;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkMonitor extends BroadcastReceiver {

    @SuppressLint("Range")
    @Override
    public void onReceive(Context context, Intent intent) {
        if (checkNetworkConnection(context)){
            Toast.makeText(context, "Internet Come Back", Toast.LENGTH_LONG).show();

            MyDatabaseHelper dbHelper = new MyDatabaseHelper(context);
            SQLiteDatabase database = dbHelper.getWritableDatabase();

            Cursor cursor = dbHelper.ReadDataFromLocalStorage();

            if (cursor.getCount() == 0 ){
                Toast.makeText(context, "No Data Found in Local Storage", Toast.LENGTH_SHORT).show();
            }
            else {

                Toast.makeText(context, "Has some Data in local", Toast.LENGTH_SHORT).show();

                while (cursor.moveToNext()){

                    @SuppressLint("Range") String Name = cursor.getString(cursor.getColumnIndex(DbContains.NAME));
                    @SuppressLint("Range") String Number = cursor.getString(cursor.getColumnIndex(DbContains.NUMBER));
                    @SuppressLint("Range") String Age = cursor.getString(cursor.getColumnIndex(DbContains.AGE));

                    RetrofitClient.getRetrofitClient().SendToServer(Name, Number, Age).enqueue(new Callback<AddResponse>() {
                        @Override
                        public void onResponse(Call<AddResponse> call, Response<AddResponse> response) {

                            if (response.isSuccessful()){

                                Toast.makeText(context, "Send To server", Toast.LENGTH_SHORT).show();

                                dbHelper.deleteFromLocal();
                            }
                            else {
                                Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<AddResponse> call, Throwable t) {

                        }
                    });
                }

            }

        }
        else {
            Toast.makeText(context, "No Internet Connection Available", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean checkNetworkConnection(Context context){

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());

    }
}