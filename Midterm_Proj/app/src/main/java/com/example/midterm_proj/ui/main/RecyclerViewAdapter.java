package com.example.midterm_proj.ui.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.midterm_proj.OpenPopupHandler;
import com.example.midterm_proj.R;

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

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(mContext, 3);

        GridRecyclerAdapter gridViewAdapter = new GridRecyclerAdapter(holder.photos.getContext(), mImageDateList.get(position).getImageList());

        gridViewAdapter.setOpenPopupHandler(mOpenPopupHandler);

        holder.title.setText(getParseStringDate(mImageDateList.get(position).getDate()));

//        holder.photos.getLayoutParams().height =
//                ( (mImageDateList.get(position).getImageList().size() % size.getNumOfImagesRow() == 0)
//                        ? mImageDateList.get(position).getImageList().size() / size.getNumOfImagesRow()
//                        : mImageDateList.get(position).getImageList().size() / size.getNumOfImagesRow() + 1 ) * ( (size.getWidth() ) / size.getNumOfImagesRow());


        holder.photos.setAdapter(gridViewAdapter);

        holder.photos.setLayoutManager(layoutManager);
    }

    @Override
    public int getItemCount() {
        return mImageDateList != null ? mImageDateList.size() : 0;
    }

    public void setOpenPopupHandler(OpenPopupHandler openPopupHandler) {
        mOpenPopupHandler = openPopupHandler;
    }

    public void setImageDateList(List<ImageDate> imageDateList) {
        this.mImageDateList = imageDateList;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        RecyclerView photos;

        public RecyclerViewHolder(@NonNull View itemView)
        {
            super(itemView);
              title = (TextView)itemView.findViewById(R.id.date_title); // date
              photos = (RecyclerView)itemView.findViewById(R.id.gridview); // image
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
