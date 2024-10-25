package com.sanika.beachapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.sanika.beachapplication.R;
import com.sanika.beachapplication.adapter.HotelAdapter;
import com.sanika.beachapplication.api.ApiClient;
import com.sanika.beachapplication.api.ApiInterface;
import com.sanika.beachapplication.api.Function;
import com.sanika.beachapplication.model.Hotel;
import com.sanika.beachapplication.model.HotelDetail;
import com.sanika.beachapplication.model.HotelDetailResponse;
import com.sanika.beachapplication.model.HotelResponse;
import com.sanika.beachapplication.model.HotelSampleData;
import com.sanika.beachapplication.model.Main;
import com.sanika.beachapplication.model.Place;
import com.sanika.beachapplication.model.TwnoOtherDetailsModel;
import com.sanika.beachapplication.model.Weather;
import com.sanika.beachapplication.model.WeatherResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BeachDetailsActivity extends AppCompatActivity {
    private TextView textViewBeachName, textViewTemperature, textViewWeatherDescription, textViewHotelDetails;
    private RecyclerView recyclerViewHotels;

    private List<HotelDetail> hotelDetails;
    ImageView imgtemp;
    ImageView imageView4;
    ImageView imageLogo;
    private HotelAdapter hotelAdapter; // Assuming you have a HotelAdapter to display hotels
    //private List<Hotel> hotelList; // Assuming you have a Hotel class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_beach_details);
        textViewBeachName = findViewById(R.id.textViewBeachName);
        textViewTemperature = findViewById(R.id.textViewTemperature);
        textViewWeatherDescription = findViewById(R.id.textViewWeatherDescription);
        recyclerViewHotels = findViewById(R.id.recyclerViewHotels);
        imgtemp = findViewById(R.id.imgtemp);
        imageView4 = findViewById(R.id.imageView4);
        imageLogo = findViewById(R.id.imageLogo);

        hotelDetails = new ArrayList<>();
        hotelDetails = HotelSampleData.getSampleHotelDetails();
      //  hotelAdapter.notifyDataSetChanged();
        hotelAdapter = new HotelAdapter(this, hotelDetails);

        recyclerViewHotels.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerViewHotels.setAdapter(hotelAdapter);

        // Get beach details from intent
        Intent intent = getIntent();
        String beachName = intent.getStringExtra("BEACH_NAME");
        String BEACH_Url = intent.getStringExtra("BEACH_Url");
        double beachLatitude = intent.getDoubleExtra("BEACH_LATITUDE", 0);
        double beachLongitude = intent.getDoubleExtra("BEACH_LONGITUDE", 0);

        textViewBeachName.setText(beachName);
        // Load beach image using Glide
        Glide.with(BeachDetailsActivity.this)
                .load(BEACH_Url)  // Ensure this is the correct URL for the image
                .placeholder(R.drawable.img3) // Optional: Placeholder image while loading
                .into(imageLogo);

        // Fetch hotels and weather
      //  fetchNearbyHotels(beachLatitude, beachLongitude);
        fetchWeatherData(beachLatitude, beachLongitude);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }






    private void fetchHotelDetails(String placeId) {
        String apiKey = "AIzaSyBa2ns1JyOmwVpz2r2KF_BOGGDmJZ8Op2Q"; // Replace with your actual API key

        ApiInterface apiInterface = ApiClient.getApi();
        Call<HotelDetailResponse> call = apiInterface.getHotelDetails(placeId, apiKey);

        call.enqueue(new Callback<HotelDetailResponse>() {
            @Override
            public void onResponse(Call<HotelDetailResponse> call, Response<HotelDetailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HotelDetail hotelDetail = response.body().getResult();
                    // Handle the hotel detail (e.g., add to a list, update UI, etc.)
                    updateHotelDetailsUI(hotelDetail);
                } else {
                    Log.e("Error", "Failed to fetch hotel details.");
                }
            }

            @Override
            public void onFailure(Call<HotelDetailResponse> call, Throwable t) {
                Log.e("Error", "Failed to fetch hotel details: " + t.getMessage());
            }
        });
    }
    private void updateHotelDetailsUI(HotelDetail hotelDetail) {

    }
    private void fetchWeatherData(double latitude, double longitude) {
        String apiKey = "95df43604483425602859a34530c9fef"; // Replace with your OpenWeatherMap API key
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?lat=%s&lon=%s&appid=%s&units=metric", latitude, longitude, apiKey);

        ApiInterface apiInterface = ApiClient.getApi();
        Call<WeatherResponse> call = apiInterface.getWeatherData(url);

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    updateWeatherUI(weatherResponse);
                } else {
                    Log.e("Error", "Failed to fetch weather data.");
                    Toast.makeText(BeachDetailsActivity.this, "Failed to fetch weather", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Log.e("Error", "Failed to fetch weather: " + t.getMessage());
                Toast.makeText(BeachDetailsActivity.this, "Error fetching weather: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void updateWeatherUI(WeatherResponse weatherResponse) {
        String cityName = weatherResponse.getCityName();
        double temperature = weatherResponse.getMain().getTemp();
        int humidity = weatherResponse.getMain().getHumidity();
        String weatherDescription = weatherResponse.getWeather().get(0).getDescription();
        String weatherIconCode = weatherResponse.getWeather().get(0).getIcon();
        double windSpeed = weatherResponse.getWind().getSpeed();
        double waterTemp = weatherResponse.getMain().getWaterTemp(); // Adjust based on API response


        List<Weather> weather = weatherResponse.getWeather();
        TwnoOtherDetailsModel main = weatherResponse.getMain();
        String weatherDescription1 = weather.get(0).getDescription().toLowerCase();
        double temperature1 = main.getTemp();

        // Update UI
        textViewTemperature.setText(String.format("%.1f °C", temperature));
        textViewWeatherDescription.setText(weatherDescription);

        // Set appropriate icon and alert
        setWeatherIconAndAlert(weatherDescription, temperature);
    }
    private void setWeatherIconAndAlert(String weatherDescription, double temperature) {
        int weatherIconResourceId = R.drawable.sun; // Default icon for sunny weather
        boolean showAlert = false;
        String alertMessage = "";

        // Check various weather conditions
        if (weatherDescription.contains(getString(R.string.weather_heavy_rain))) {
            weatherIconResourceId = R.drawable.heavy_rain; // Icon for heavy rain
            showAlert = temperature > 30; // Alert if it's hot and raining heavily
            if (showAlert) {
                alertMessage = getString(R.string.alert_heavy_rain_hot);
            }
        } else if (weatherDescription.contains(getString(R.string.weather_thunderstorm))) {
            weatherIconResourceId = R.drawable.thunderstorm; // Icon for thunderstorms
            showAlert = true; // Always alert for thunderstorms
            alertMessage = getString(R.string.alert_thunderstorm);
        } else if (weatherDescription.contains(getString(R.string.weather_clear)) ||
                weatherDescription.contains(getString(R.string.weather_sunny))) {
            weatherIconResourceId = R.drawable.sun; // Icon for clear or sunny weather
            showAlert = temperature > 40; // Alert for extremely high temperatures
            if (showAlert) {
                alertMessage = getString(R.string.alert_extreme_heat);
            }
        } else if (weatherDescription.contains(getString(R.string.weather_cloud))) {
            weatherIconResourceId = R.drawable.cloudy; // Icon for cloudy weather
        } else if (weatherDescription.contains(getString(R.string.weather_hot))) {
            weatherIconResourceId = R.drawable.hot; // Icon for hot weather
            showAlert = temperature > 35; // Alert for extreme heat
            if (showAlert) {
                alertMessage = getString(R.string.alert_extreme_heat);
            }
        } else if (weatherDescription.contains(getString(R.string.weather_snow))) {
            weatherIconResourceId = R.drawable.snow; // Icon for snowy weather
            showAlert = true; // Always alert for snow
            alertMessage = getString(R.string.alert_snow);
        } else if (weatherDescription.contains(getString(R.string.weather_fog))) {
            weatherIconResourceId = R.drawable.fog; // Icon for foggy weather
            showAlert = true; // Always alert for fog
            alertMessage = getString(R.string.alert_fog);
        } else if (weatherDescription.contains(getString(R.string.weather_mist))) {
            weatherIconResourceId = R.drawable.mist; // Icon for misty weather
        } else if (weatherDescription.contains(getString(R.string.weather_hail))) {
            weatherIconResourceId = R.drawable.hail; // Icon for hail
            showAlert = true; // Always alert for hail
            alertMessage = getString(R.string.alert_hail);
        } else if (weatherDescription.contains(getString(R.string.weather_windy)) ||
                weatherDescription.contains(getString(R.string.weather_storm))) {
            weatherIconResourceId = R.drawable.wind; // Icon for windy or stormy weather
            showAlert = true; // Always alert for storms and strong winds
            alertMessage = getString(R.string.alert_strong_winds);
        } else if (weatherDescription.contains(getString(R.string.weather_drizzle))) {
            weatherIconResourceId = R.drawable.drizzle; // Icon for light rain or drizzle
        } else if (weatherDescription.contains(getString(R.string.weather_sleet))) {
            weatherIconResourceId = R.drawable.sleet; // Icon for sleet
            showAlert = true; // Always alert for sleet
            alertMessage = getString(R.string.alert_strong_winds);
        } else if (weatherDescription.contains(getString(R.string.weather_cold))) {
            weatherIconResourceId = R.drawable.cold; // Icon for cold weather
            showAlert = temperature < 0; // Alert if it's freezing
            if (showAlert) {
                alertMessage = getString(R.string.alert_extreme_cold);
            }
        } else if (weatherDescription.contains(getString(R.string.weather_haze))) {
            weatherIconResourceId = R.drawable.haze; // Icon for haze
            showAlert = true; // Always alert for haze due to poor visibility and air quality
            alertMessage = getString(R.string.alert_haze);
        }

        // Update the weather icon in the UI
        imgtemp.setImageResource(weatherIconResourceId);

        // Show weather alert if conditions are met
        if (showAlert) {
            showWeatherAlert(alertMessage); // Pass alert message to the alert method
        }
    }


    private void showWeatherAlert(String alertMessage) {
        new AlertDialog.Builder(this)
                .setTitle(getResources().getString(R.string.weather_alert))
                .setMessage(alertMessage)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
    public  void test()
    {
        Function.placeIdTask asyncTask =new Function.placeIdTask(new Function.AsyncResponse() {
            public void processFinish(String weather_city, String weather_description, String weather_temperature, String weather_humidity, String weather_pressure, String weather_updatedOn, String weather_iconText, String sun_rise) {
                Toast.makeText(BeachDetailsActivity.this, weather_city, Toast.LENGTH_SHORT).show();
                Toast.makeText(BeachDetailsActivity.this, weather_city, Toast.LENGTH_SHORT).show();
                Toast.makeText(BeachDetailsActivity.this, weather_city, Toast.LENGTH_SHORT).show();

            }
        });
        // you can paste your city "latitude" and "longitude" here
        asyncTask.execute("25.180000", "89.530000"); //  asyncTask.execute("Latitude", "Longitude")



    }
}