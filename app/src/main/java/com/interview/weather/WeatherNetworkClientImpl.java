package com.interview.weather;

import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WeatherNetworkClientImpl implements WeatherNetworkClient {

    // <editor-fold defaultstate="collapsed" desc="Only need to look at the interface">
    private static final Random RANDOM = new Random();

    private static final Map<String, List<String>> COUNTRIES_WITH_CITIES = new HashMap<>();
    static {
        COUNTRIES_WITH_CITIES.put("Australia", Arrays.asList("Canberra", "Melbourne", "Perth", "Sydney"));
        COUNTRIES_WITH_CITIES.put("Canada", Arrays.asList("Toronto", "Vancouver", "Montreal", "Calgary"));
        COUNTRIES_WITH_CITIES.put("Ireland", Arrays.asList("Dublin", "Galway", "Kilkenny"));
        COUNTRIES_WITH_CITIES.put("Mexico", Arrays.asList("Guadalajara", "Mexico City", "Monterrey"));
        COUNTRIES_WITH_CITIES.put("Philippines", Arrays.asList("Manila", "Cebu City"));
        COUNTRIES_WITH_CITIES.put("United Kingdom", Arrays.asList("London", "Conwy", "Brighton", "Bath", "York", "Edinburgh"));
        COUNTRIES_WITH_CITIES.put("United States", Arrays.asList("San Francisco", "New York", "Boston", "Los Angeles"));
        COUNTRIES_WITH_CITIES.put("Uruguay", Arrays.asList("Montevideo", "Punta del Este", "Colonia del Sacramento"));
    }

    private static final List<String> WEATHER_DESCRIPTIONS = Arrays.asList(
            "Cloudy with a chance of meatballs",
            "Mostly sunny with afternoon tornadoes",
            "Plague of locusts",
            "Hot as an oven",
            "Sideways rain"
    );

    private static final List<String> WIND_DIRECTIONS = Arrays.asList(
            "North",
            "Northeast",
            "East",
            "Southeast",
            "South",
            "Southwest",
            "West",
            "Northwest"
    );

    @Override
    public GetCitiesResponse waitForCities() throws IOException {
        randomDelay();
        maybeThrowError();
        final List<City> cities = new ArrayList<>();
        for (Map.Entry<String, List<String>> countryWithCities : COUNTRIES_WITH_CITIES.entrySet()) {
            cities.add(new City("", countryWithCities.getKey(), 0.0d, 0.0d, City.VIEW_TYPE_COUNTRY));
            for (String cityName: countryWithCities.getValue()) {
                cities.add(new City(
                        cityName,
                        countryWithCities.getKey(),
                        90 * RANDOM.nextGaussian(),
                        180 * RANDOM.nextGaussian(), City.VIEW_TYPE_CITY));
            }
        }
        //Collections.shuffle(cities);
        return new GetCitiesResponse(cities);
    }

    @Override
    public Single<GetCitiesResponse> getCities() {
      return Single.fromCallable(this::waitForCities).subscribeOn(Schedulers.io());
    }

    @Override
    public void getCities(final Callback<GetCitiesResponse> callback) {
        getCities()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<GetCitiesResponse>() {
                @Override
                public void onSubscribe(final Disposable d) {}

                @Override
                public void onSuccess(final GetCitiesResponse response) {
                    callback.onResult(response);
                }

                @Override
                public void onError(final Throwable e) {
                    callback.onError(e);
                }
            });
    }

    @Override
    public GetWeatherForCityResponse waitForWeatherForCity(final String cityName) throws IOException {
        randomDelay();
        maybeThrowError();
        return new GetWeatherForCityResponse(
                new CurrentWeather(
                        WEATHER_DESCRIPTIONS.get(RANDOM.nextInt(WEATHER_DESCRIPTIONS.size())),
                        randomTemperatureCelsius(),
                        (short) (100 * RANDOM.nextFloat()),
                        (short) (100 * RANDOM.nextFloat()),
                        (1000 + 20 * RANDOM.nextGaussian()) + " mbar",
                        WIND_DIRECTIONS.get(RANDOM.nextInt(WIND_DIRECTIONS.size())),
                        100 * RANDOM.nextFloat()),
                new HashMap<String, PredictedWeather>() {{
                    put("Sunday", randomPredictedWeather());
                    put("Monday", randomPredictedWeather());
                    put("Tuesday", randomPredictedWeather());
                    put("Wednesday", randomPredictedWeather());
                    put("Thursday", randomPredictedWeather());
                    put("Friday", randomPredictedWeather());
                    put("Saturday", randomPredictedWeather());
                }}
        );
    }

    @Override
    public Single<GetWeatherForCityResponse> getWeatherForCity(final String cityName) {
      return Single.fromCallable(() -> waitForWeatherForCity(cityName)).subscribeOn(Schedulers.io());
    }

    @Override
    public void getWeatherForCity(final String cityName, final Callback<GetWeatherForCityResponse> callback) {
      getWeatherForCity(cityName)
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new SingleObserver<GetWeatherForCityResponse>() {
            @Override
            public void onSubscribe(final Disposable d) {}

            @Override
            public void onSuccess(final GetWeatherForCityResponse response) {
              callback.onResult(response);
            }

            @Override
            public void onError(final Throwable e) {
              callback.onError(e);
            }
          });
    }

    private static PredictedWeather randomPredictedWeather() {
        final float minTemp = randomTemperatureCelsius();
        final float maxTemp = minTemp + 15 * RANDOM.nextFloat();
        return new PredictedWeather(
                minTemp,
                maxTemp,
                WEATHER_DESCRIPTIONS.get(RANDOM.nextInt(WEATHER_DESCRIPTIONS.size())));
    }

    private static float randomTemperatureCelsius() {
        return (float) (10 + 20 * RANDOM.nextGaussian());
    }

    private static void randomDelay() {
        try {
            Thread.sleep(RANDOM.nextInt(5000));
        } catch (InterruptedException e) {
            Log.e(WeatherNetworkClientImpl.class.getSimpleName(), "Interrupted sleep");
            Thread.currentThread().interrupt();
        }
    }

    private static void maybeThrowError() throws IOException {
        if (true) {
            return;
        }
        if (RANDOM.nextInt(5) == 0) {
            throw new IOException("fake network error");
        }
    }
    // </editor-fold>
}
