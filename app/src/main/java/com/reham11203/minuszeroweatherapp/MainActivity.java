package com.reham11203.minuszeroweatherapp;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.reham11203.minuszeroweatherapp.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    LocationManager locationManager;
    LocationListener locationListener;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnAddLocation.setOnClickListener(view -> {

            Intent intent = new Intent(MainActivity.this, WeatherActivity.class);
            startActivity(intent);

        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {

                lat = location.getLatitude();
                lon = location.getLongitude();

                Log.e("lat : ",String.valueOf(lat));
                Log.e("lon : ",String.valueOf(lon));

                getWeatherData(lat,lon);

            }
        };

        if(ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{ACCESS_FINE_LOCATION},1);
        }
        else{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1 && permissions.length > 0 && ContextCompat.checkSelfPermission(this,
                ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,500,50,locationListener);
        }
    }

    public void getWeatherData(double lat, double lon){
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithLocation(lat,lon);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {
                binding.textCity.setText(response.body().getName() + " , " + response.body().getSys().getCountry()+" ");
                binding.textTemperature.setText(response.body().getMain().getTemp().intValue() + "°");
                binding.weatherCondition.setText(response.body().getWeather().get(0).getDescription());
                binding.textHumidityValue.setText(response.body().getMain().getHumidity() + "%");
                binding.textMaxTempValue.setText(response.body().getMain().getTempMax().intValue() + "/");
                binding.textMinTempValue.setText(response.body().getMain().getTempMin().intValue() + "°");
                binding.textPressureValue.setText(response.body().getMain().getPressure() + " mb");

                String icon = response.body().getWeather().get(0).getIcon();
                Picasso.get().load("https://openweathermap.org/img/wn/" + icon + "@2x.png")
                        .placeholder(R.drawable.cloudy_sunny)
                        .into(binding.imageView);

            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable throwable) {

            }
        });
    }
}