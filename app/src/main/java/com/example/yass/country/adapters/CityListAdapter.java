package com.example.yass.country.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yass.country.R;

import java.util.List;

/**
 * Created by yass on 6/21/17.
 */

public class CityListAdapter extends RecyclerView.Adapter<CityListAdapter.CityViewHolder> {

    private List<String> citiesList;
    Context context;

    private CityListAdapterOnClickHandler onClickHandler;

    public CityListAdapter(Context context, List<String> citiesList,
                           CityListAdapterOnClickHandler onClickHandler) {
        this.citiesList = citiesList;
        this.context = context;
        this.onClickHandler = onClickHandler;
    }

    public interface CityListAdapterOnClickHandler{

        void onClick(String city);
    }

    @Override
    public CityViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.city_list_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CityListAdapter.CityViewHolder holder, int position) {
        if (citiesList.size() == 0) return;

        holder.cityTextView.setText(citiesList.get(position));
        holder.cityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickHandler.onClick(citiesList.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return citiesList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        private TextView cityTextView;

        public CityViewHolder(View itemView) {
            super(itemView);
            cityTextView = (TextView) itemView.findViewById(R.id.city_textView);
        }
    }
}
