package com.interview.weather;

import java.io.IOException;

import io.reactivex.Single;

/** Makes network requests to fetch weather data. */
public interface WeatherNetworkClient {

    // ========== Network calls for the city list screen ==========

    /**
     * @return The list of cities to display.
     * @throws IOException If an error was encountered during the network call.
     */
    GetCitiesResponse waitForCities() throws IOException;

    /** @return An async operation for the list of cities to display. */
    Single<GetCitiesResponse> getCities();

    /** @param callback The listener that will be called after the async operation for the list of cities finishes. */
    void getCities(Callback<GetCitiesResponse> callback);


    // ========== Network calls for the city forecast screen ==========

    /**
     * @param cityName The city to fetch the weather forecast for.
     * @return The weather forecast to display.
     * @throws IOException If an error was encountered during the network call.
     */
    GetWeatherForCityResponse waitForWeatherForCity(String cityName) throws IOException;

    /**
     * @param cityName The city to fetch the weather forecast for.
     * @return An async operation for the weather forecast to display.
     */
    Single<GetWeatherForCityResponse> getWeatherForCity(String cityName);

    /**
     * @param cityName The city to fetch the weather forecast for.
     * @param callback The listener that will be called after the async operation for the weather forecast finishes.
     */
    void getWeatherForCity(String cityName, Callback<GetWeatherForCityResponse> callback);


    // ========== Shared interfaces ==========

    /** Callbacks on the main thread when execution has completed. */
    interface Callback<T> {
        void onResult(T result);
        void onError(Throwable error);
    }
}
