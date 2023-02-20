package com.dvtopenweather.dvtopenweatherapp.di

import android.content.Context
import com.dvtopenweather.dvtopenweatherapp.R
import com.dvtopenweather.dvtopenweatherapp.data.remote.GooglePlacesLocationData
import com.dvtopenweather.dvtopenweatherapp.data.remote.GooglePlacesLocationDataImpl
import com.dvtopenweather.dvtopenweatherapp.data.remote.OpenWeatherForecastData
import com.dvtopenweather.dvtopenweatherapp.data.remote.OpenWeatherForecastDataImpl
import com.dvtopenweather.dvtopenweatherapp.data.remote.api.GooglePlacesRetrofitAPI
import com.dvtopenweather.dvtopenweatherapp.data.remote.api.OpenWeatherRetrofitAPI
import com.dvtopenweather.dvtopenweatherapp.util.CONNECTION_TIME_OUT_READ_TIME_OUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun providesRetrofit(@ApplicationContext context: Context, okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(context.getString(R.string.open_weather_base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @GooglePlaceRetrofitBuilder
    @Singleton
    @Provides
    fun providesGooglePlacesRetrofit(@ApplicationContext context: Context, okHttpClient: OkHttpClient) : Retrofit = Retrofit.Builder()
        .baseUrl(context.getString(R.string.google_places_base_url))
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun providesRetrofitClient(okHttpLoggingInterceptor: HttpLoggingInterceptor) : OkHttpClient = OkHttpClient.Builder().addInterceptor(okHttpLoggingInterceptor)
        .connectTimeout(CONNECTION_TIME_OUT_READ_TIME_OUT, TimeUnit.SECONDS)
        .readTimeout(CONNECTION_TIME_OUT_READ_TIME_OUT, TimeUnit.SECONDS)
        .build()

    @Singleton
    @Provides
    fun providesOkHTTPInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

    @Singleton
    @Provides
    fun providesOpenWeatherAPI(retrofit: Retrofit): OpenWeatherRetrofitAPI = retrofit.create(OpenWeatherRetrofitAPI::class.java)

    @Singleton
    @Provides
    fun providesGooglePlacesAPI(@GooglePlaceRetrofitBuilder retrofit: Retrofit): GooglePlacesRetrofitAPI = retrofit.create(GooglePlacesRetrofitAPI::class.java)

    @Singleton
    @Provides
    fun providesOpenWeatherForecastData(openWeatherRetrofitAPI: OpenWeatherRetrofitAPI): OpenWeatherForecastData = OpenWeatherForecastDataImpl(openWeatherRetrofitAPI)

    @Singleton
    @Provides
    fun providesGooglePlacesData(googlePlacesRetrofitAPI: GooglePlacesRetrofitAPI): GooglePlacesLocationData = GooglePlacesLocationDataImpl(googlePlacesRetrofitAPI)

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class GooglePlaceRetrofitBuilder

}