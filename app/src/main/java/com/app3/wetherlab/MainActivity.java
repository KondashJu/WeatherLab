package com.app3.wetherlab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button find_city_res;
    EditText city_name_input;
    ListView result_wrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        find_city_res = findViewById(R.id.find_button);
        city_name_input = findViewById(R.id.city_name_field);
        result_wrap = findViewById(R.id.api_results);

        find_city_res.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
                if (!city_name_input.getText().toString().equals("")) {
                    String url = "https://api.openweathermap.org/data/2.5/weather?appid=af9926862c2571691a6deec90e753bc6&lang=ua&units=metric&q=" + city_name_input.getText().toString();

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            String wether = "";
                            String temp = "";
                            String feels_like = "";
                            String temp_min = "";
                            String temp_max = "";
                            String pressure = "";
                            String humidity = "";
                            String clouds = "";
                            String wind_speed = "";
                            try {
                                JSONArray wether_array = response.getJSONArray("weather");
                                JSONObject i = wether_array.getJSONObject(0);
                                wether = "Теперішня погода: " + i.getString("description");

                                JSONObject main_inf = response.getJSONObject("main");
                                temp = "Температура:" + main_inf.getString("temp") + "°C";
                                feels_like = "Відчуваєтся як: " + main_inf.getString("feels_like") + "°C";
                                temp_min = "Мінімальна температура: " + main_inf.getString("temp_min") + "°C";
                                temp_max = "Максимальна температура: " + main_inf.getString("temp_max") + "°C";
                                pressure = "Тиск: " + main_inf.getString("pressure") + "hPa";
                                humidity = "Вологість: " + main_inf.getString("humidity") + "%";

                                JSONObject wind = response.getJSONObject("wind");
                                wind_speed = "Швидкість вітру: " + wind.getString("speed") + "м/с";

                                JSONObject cloud_inf = response.getJSONObject("clouds");
                                clouds = "Хмарність: " + cloud_inf.getString("all") + "%";
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_expandable_list_item_1,
                                    new String[]{wether, temp, feels_like, temp_min, temp_max, clouds, wind_speed, humidity, pressure});
                            result_wrap.setAdapter(arrayAdapter);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(MainActivity.this, "Перевірте назву міста.", Toast.LENGTH_SHORT).show();
                        }
                    });

                    queue.add(request);
                } else
                    Toast.makeText(MainActivity.this, "Введіть назву міста.", Toast.LENGTH_SHORT).show();
            }

        });
    }
}