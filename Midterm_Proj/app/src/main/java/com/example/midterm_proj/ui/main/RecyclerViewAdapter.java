package com.example.midterm_proj.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    PhotosFragment mContext;
    List<PhotoDate> photosDate ;
    GridViewAdapter gridViewAdapter;
    OpenPopupHandler mOpenPopupHandler;

    @SuppressLint("SimpleDateFormat")
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
    public RecyclerViewAdapter(PhotosFragment mContext, List<PhotoDate> mData) {
        this.mContext = mContext;
        this.photosDate = mData;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout;
        layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_by_date, parent, false);
        return new RecyclerViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        gridViewAdapter = new GridViewAdapter(mContext.getContext(), photosDate.get(position).photos);
        gridViewAdapter.setOpenPopupHandler(mOpenPopupHandler);
        holder.title.setText(getParseStringDate(photosDate.get(position).getDate()));
        holder.photos.getLayoutParams().height = (photosDate.get(position).photos.size() / 5 + 1 ) * 220;
        holder.photos.setAdapter(gridViewAdapter);
    }

    @Override
    public int getItemCount() {
        return photosDate.size();
    }

    public void setOpenPopupHandler(OpenPopupHandler openPopupHandler) {
        mOpenPopupHandler = openPopupHandler;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        GridView photos;

        public RecyclerViewHolder(@NonNull View itemView)
        {
            super(itemView);
              title = (TextView)itemView.findViewById(R.id.date_title); // title
              photos = (GridView)itemView.findViewById(R.id.gridview); // description of that person
              itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    private String getParseStringDate(Date date){
        @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String dayInWeek = "Mon";
        String currentDateString = dateFormat.format(currentDate);
        switch (date.getDay()){
            case 1:
                dayInWeek = "Mon";
                break;
            case 2:
                dayInWeek = "Tue";
                break;
            case 3:
                dayInWeek = "Wed";
                break;
            case 4:
                dayInWeek = "Thus";
                break;
            case 5:
                dayInWeek = "Fri";
                break;
            case 6:
                dayInWeek = "Sat";
                break;
            default:
                dayInWeek = "Sun";
                break;
        }
        String strDate = dayInWeek + " " + dateFormat.format(date);

        if (currentDateString.equals(dateFormat.format(date))) return "Today";
        else if (dateFormat.format(yesterday()).equals(dateFormat.format(date))) return "Yesterday";
        return strDate;
    }
    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }
}
