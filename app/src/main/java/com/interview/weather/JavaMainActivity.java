package com.interview.weather;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class JavaMainActivity extends AppCompatActivity {

    private WeatherNetworkClient weatherNetworkClient;

    private RecyclerView citiesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        weatherNetworkClient = new WeatherNetworkClientImpl();
        setContentView(R.layout.activity_main);

        citiesList = findViewById(R.id.cities_list);
        citiesList.setLayoutManager(new LinearLayoutManager(this));
        try {
            citiesList.setAdapter(new CitiesAdapter(weatherNetworkClient.waitForCities().cities));
        } catch (final IOException e) {
            Log.e(JavaMainActivity.class.getSimpleName(), "Error on waitForCities", e);
        }
    }

    private static class CitiesAdapter extends RecyclerView.Adapter<CitiesAdapter.ViewHolder> {

        private final List<City> cities;

        private CitiesAdapter(final List<City> cities) {
            this.cities = cities;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            if (viewType == 2)
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.country_item, parent, false));
            else
                return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.city_item, parent, false));


        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            //doing this just because we are not using delegates
            City city = cities.get(position);
            if(city.viewType ==  1) {
                TextView cityNameView = holder.itemView.findViewById(R.id.city_name);
                cityNameView.setText(cities.get(position).name);
            } else {
                TextView countryNameView = holder.itemView.findViewById(R.id.country_name);
                countryNameView.setText(cities.get(position).countryName);
            }
        }

        @Override
        public int getItemCount() {
            return cities.size();
        }

        @Override
        public int getItemViewType(int position) {
            return cities.get(position).viewType;
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            ViewHolder(final View itemView) {
                super(itemView);
            }
        }
    }

    private class CityHolder extends RecyclerView.ViewHolder{

        TextView cityTV;

        public CityHolder(@NonNull View itemView) {
            super(itemView);

        }
    }
}
