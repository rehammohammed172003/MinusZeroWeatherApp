package com.reham11203.minuszeroweatherapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherAPI {
    @GET("weather?appid=4cb6846f9de6d731b533eeaf6aa389a8&units=metric")
    Call<OpenWeatherMap> getWeatherWithLocation(@Query("lat") double lat, @Query("lon") double lon);

    @GET("weather?appid=4cb6846f9de6d731b533eeaf6aa389a8&units=metric")
    Call<OpenWeatherMap> getWeatherWithCityName(@Query("q") String name);
}
