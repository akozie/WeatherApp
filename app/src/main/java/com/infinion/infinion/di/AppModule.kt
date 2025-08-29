package com.infinion.infinion.di

import android.content.Context
import androidx.room.Room
import com.infinion.infinion.data.network.WeatherApi
import com.infinion.infinion.data.repository.IWeatherRepository
import com.infinion.infinion.data.repository.WeatherRepositoryImpl
import com.infinion.infinion.db.CityDatabase
import com.infinion.infinion.db.dao.CityDao
import com.infinion.infinion.utils.UtilityParam.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOkHttp(): OkHttpClient {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder().addInterceptor(logger).build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit =
        Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .client(client).build()

    @Provides
    @Singleton
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Provides
    @Singleton
    fun provideRepository(api: WeatherApi): IWeatherRepository = WeatherRepositoryImpl(api)


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): CityDatabase =
        Room.databaseBuilder(context, CityDatabase::class.java, "city_db")
            .fallbackToDestructiveMigration().build()

    @Provides
    @Singleton
    fun provideCityDao(db: CityDatabase): CityDao = db.cityDao()
}
