package com.reham11203.minuszeroweatherapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.reham11203.minuszeroweatherapp.databinding.ActivityWeatherBinding;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherActivity extends AppCompatActivity {

    ActivityWeatherBinding binding;
    double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWeatherBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.search.setOnClickListener(view -> {
            String cityName = binding.editTextLocation.getText().toString();
            getWeatherData(cityName);
            binding.editTextLocation.setText("");
        });
    }

    public void getWeatherData(String name){
        WeatherAPI weatherAPI = RetrofitWeather.getClient().create(WeatherAPI.class);
        Call<OpenWeatherMap> call = weatherAPI.getWeatherWithCityName(name);

        call.enqueue(new Callback<OpenWeatherMap>() {
            @Override
            public void onResponse(Call<OpenWeatherMap> call, Response<OpenWeatherMap> response) {

                if(response.isSuccessful()){
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
                else {
                    Toast.makeText(WeatherActivity.this, "City Not Found", Toast.LENGTH_LONG).show();
                }


            }

            @Override
            public void onFailure(Call<OpenWeatherMap> call, Throwable throwable) {

            }
        });
    }
}