package developer.citypalestine8936ps.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import developer.citypalestine8936ps.new_city_feature.CityProfileActivity;
import developer.citypalestine8936ps.databinding.CityItemBinding;
import developer.citypalestine8936ps.models.City;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    CityItemBinding binding;
    private List<City> cities;
    private Context context ;
    class CityViewHolder extends  RecyclerView.ViewHolder{
        CityViewHolder(CityItemBinding cityItemBinding){
            super(cityItemBinding.getRoot());
            binding = cityItemBinding;

        }

        void setUserData(City city ){
            binding.txtCityName.setText(city.cityName);

        }


}

    public CityAdapter(List<City> cities, Context context) {
        this.cities = cities;
        this.context=context;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CityItemBinding cityItemBinding = CityItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),parent,false
        );
        return new CityViewHolder(cityItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        holder.setUserData(cities.get(position));

        binding.cardCity.setOnClickListener(view -> {
            Intent intent = new Intent(context , CityProfileActivity.class);
            intent.putExtra("city" , cities.get(position));
            context.startActivity(intent);

        });

    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

}
