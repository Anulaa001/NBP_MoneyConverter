package com.example.nbpapiconvert;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Button konwertuj;
    EditText suma;
    String wybranaWaluta;
    float srednia;
    TextView rezultat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        konwertuj = findViewById(R.id.buttonKonweruj);
        rezultat = findViewById(R.id.textViewResutl);
        suma = findViewById(R.id.editTextNumberPLN);

        Spinner Waluta = (Spinner) findViewById(R.id.spinnerWaluta);
        Waluta.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<String>();
        list.add("THB");
        list.add("USD");
        list.add("AUD");
        list.add("HKD");
        list.add("CAD");
        list.add("NZD");
        list.add("SGD");
        list.add("EUR");
        list.add("HUF");
        list.add("CHF");
        list.add("GBP");
        list.add("UAH");
        list.add("JPY");
        list.add("CZK");
        list.add("DKK");
        list.add("ISK");
        list.add("NOK");
        list.add("SEK");
        list.add("HRK");
        list.add("RON");
        list.add("BGN");
        list.add("TRY");
        list.add("ILS");
        list.add("CLP");
        list.add("PHP");
        list.add("MXN");
        list.add("ZAR");
        list.add("BRL");
        list.add("MYR");
        list.add("RUB");
        list.add("IDR");
        list.add("INR");
        list.add("KRW");
        list.add("CNY");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Waluta.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        wybranaWaluta = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        wybranaWaluta = parent.getItemAtPosition(0).toString();

    }
    public class DownloadTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... parms) {
            String json = "";
            URL url;
            HttpURLConnection urlConnection = null;
            String wybranaWaluta = parms[0];
            try {
                url = new URL("http://api.nbp.pl/api/exchangerates/rates/a/"+wybranaWaluta+"/?format=json");
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while (data != -1) {
                    char letter = (char) data;
                    json += letter;
                    data = reader.read();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return json;
        }

        @Override
        protected void onPostExecute(String json) {
            super.onPostExecute(json);
            try {
                JSONObject jsonObject = new JSONObject(json);
                String rates = jsonObject.getString("rates");
                JSONArray array = new JSONArray(rates);
                JSONObject mid = array.getJSONObject(0);
                srednia = (float) mid.getDouble("mid");
                if(suma.getText().toString().isEmpty()){
                    suma.setText("1");
                }
                float f = (float) (Float.parseFloat(suma.getText().toString()) / srednia);
                rezultat.setText(String.valueOf("Result: " +String.format("%.2f",f)+ " " + wybranaWaluta));
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }


    }
    public void konwertuj(View view){
        DownloadTask task = new DownloadTask();
        task.execute(wybranaWaluta);

    }
}