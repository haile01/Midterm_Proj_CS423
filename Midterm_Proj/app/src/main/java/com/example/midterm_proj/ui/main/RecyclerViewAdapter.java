package com.example.midterm_proj.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RecyclerViewHolder> {

    private List<ImageDate> mImageDateList;
    public Context mContext;
//    List<PhotoDate> photosDate ;
//    GridRecyclerAdapter gridViewAdapter;
    OpenPopupHandler mOpenPopupHandler;

    public RecyclerViewAdapter(Context mContext, List<ImageDate> imageDateList) {
        this.mContext = mContext;
        this.mImageDateList = imageDateList;
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
        
        SizeConfig size = new SizeConfig();
        //GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext ,3, LinearLayoutManager.VERTICAL,false);
        //holder.photos.setLayoutManager(layoutManager);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);

        GridRecyclerAdapter gridViewAdapter = new GridRecyclerAdapter(holder.photos.getContext(), mImageDateList.get(position).getImageList());

        gridViewAdapter.setOpenPopupHandler(mOpenPopupHandler);

        holder.title.setText(getParseStringDate(mImageDateList.get(position).getDate()));

        holder.photos.getLayoutParams().height = (mImageDateList.get(position).getImageList().size() / size.getNumOfImagesRow() + 1 ) * (size.getWidth() / size.getNumOfImagesRow());

        holder.photos.setAdapter(gridViewAdapter);
        Toast.makeText(mContext, gridViewAdapter.getName(), Toast.LENGTH_LONG).show();
    }

    @Override
    public int getItemCount() {
        return mImageDateList.size();
    }

    public void setOpenPopupHandler(OpenPopupHandler openPopupHandler) {
        mOpenPopupHandler = openPopupHandler;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        RecyclerView photos;

        public RecyclerViewHolder(@NonNull View itemView)
        {
            super(itemView);
              title = (TextView)itemView.findViewById(R.id.date_title); // title
              photos = (RecyclerView)itemView.findViewById(R.id.grid_recycle); // description of that person
              photos.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return event.getAction() == MotionEvent.ACTION_MOVE;
                }
            });

        }
    }

    private String getParseStringDate(Date date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, dd MMM yyyy");
        Date currentDate = new Date();
        String currentDateString = dateFormat.format(currentDate);

        String strDate = dateFormat.format(date);

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
