package com.example.yass.country.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yass.country.R;
import com.example.yass.country.data.CountryContract;

/**
 * Created by yass on 6/20/17.
 */

public class CountryListAdapter extends RecyclerView.Adapter<CountryListAdapter.CountryViewHolder> {

    private Context context;
    private Cursor cursor;
    final private CountryListAdapterOnClickHandler mClickHandler;

    public CountryListAdapter(Context context, Cursor cursor, CountryListAdapterOnClickHandler clickHandler) {
        this.context = context;
        this.cursor = cursor;
        mClickHandler = clickHandler;
    }

    public interface CountryListAdapterOnClickHandler {
        void onClick(String date);
    }

    @Override
    public CountryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.country_list_item, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CountryViewHolder holder, int position) {
        if (!cursor.moveToPosition(position)) return;
        int idIndex = cursor.getColumnIndex(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME);

        String nameCountry = cursor.getString(idIndex);
        if (nameCountry != null && nameCountry.length() !=0){
            holder.countyTextView.setText(nameCountry);
            holder.countyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int adapterPosition = holder.getAdapterPosition();
                    cursor.moveToPosition(adapterPosition);
                    String country = cursor.getString(cursor.getColumnIndex(CountryContract.CountryEntry.COLUMN_COUNTRY_NAME));
                    mClickHandler.onClick(country);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return cursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (cursor != null) cursor.close();

        cursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder {

        TextView countyTextView;

        public CountryViewHolder(View itemView) {
            super(itemView);
            countyTextView = (TextView) itemView.findViewById(R.id.country_textView);
        }
    }
}
