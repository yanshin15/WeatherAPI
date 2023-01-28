package com.yanshin.weatherapi


import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import kotlin.math.roundToInt

class MainActivity : AppCompatActivity() {
    private lateinit var temperatureTextView: TextView
    private lateinit var weatherStatusTextView: TextView

    // Define the API endpoint
    private interface WeatherApi {
        @GET("weather?q=Singapore&APPID=b4f32e7240f584122431aaf3887cf166")
        suspend fun getTemperature(): WeatherResponse
    }

    // Define the response object
    data class WeatherResponse(val main: Main,val weather:List<Weather>)
    data class Weather(val main:String)
    data class Main(val temp: Double)

    @SuppressLint("SetTextI18n", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        temperatureTextView = findViewById(R.id.temperature_text_view)
        weatherStatusTextView = findViewById(R.id.weather_status_text_view)

        // Initialize Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Create an instance of the API interface
        val weatherApi = retrofit.create(WeatherApi::class.java)

        CoroutineScope(Dispatchers.Main).launch {
            val weatherResponse = weatherApi.getTemperature()
            val temperature = weatherResponse.main.temp.roundToInt()
            temperatureTextView.text = "Singapore " +
                    "${temperature - 273.00}C"
            weatherStatusTextView.text = "Weather: "+ weatherResponse.weather[0].main
        }
    }
}
