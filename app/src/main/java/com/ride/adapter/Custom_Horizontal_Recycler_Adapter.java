package com.ride.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.custom.BTextView;
import com.rider.xenia.R;
import com.utils.Catagories;
import com.utils.NotifyCarTypeChangeCallback;

import java.util.ArrayList;

/**
 * Created by shubh on 1/22/2018.
 */

public class Custom_Horizontal_Recycler_Adapter extends RecyclerView.Adapter<Custom_Horizontal_Recycler_Adapter.MyViewHolder> {

    ArrayList<Catagories> catList = new ArrayList<>();
    Context context;
    NotifyCarTypeChangeCallback notifyCarTypeChangeCallback;

    public Custom_Horizontal_Recycler_Adapter(ArrayList<Catagories> catList, Context context, NotifyCarTypeChangeCallback notifyCarTypeChangeCallback)
    {
        this.catList = catList;
        this.context = context;
        this.notifyCarTypeChangeCallback = notifyCarTypeChangeCallback;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_car_catagery_layout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.car_category_name.setText(catList.get(position).getCat_name());
        if(position==catList.size()-1){
            holder.view.setVisibility(View.GONE);
        }
        if(position==0){
            holder.view1.setVisibility(View.INVISIBLE);
        }

        setImageOnClick(holder.car_category_imageView,holder.car_category_name.getText().toString(),catList.get(position).isSelected());
        holder.car_category_imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int i=0;i<catList.size();i++)
                {
                    if(i==position){
                        catList.get(i).setSelected(true);

                    }else{
                        catList.get(i).setSelected(false);
                         }

                    setImageOnClick(holder.car_category_imageView,holder.car_category_name.getText().toString(),catList.get(position).isSelected());
                }
                notifyCarTypeChangeCallback.notifyCarTypeChange(position);

            }
        });
    }

    public void setImageOnClick(ImageView car_category_imageView,String s,boolean isSelected) {
               if(s.equalsIgnoreCase("Motor")){
                   car_category_imageView.setImageResource(isSelected? R.drawable.bike_image: R.drawable.thumb_icon);
        }else if(s.equalsIgnoreCase("SUV")){
                   car_category_imageView.setImageResource(isSelected? R.drawable.suv_new: R.drawable.thumb_icon);
        }else if(s.equalsIgnoreCase("Sedan")){
                   car_category_imageView.setImageResource(isSelected? R.drawable.sedan_new: R.drawable.thumb_icon);
        }else if(s.equalsIgnoreCase("Hatchback")){
                   car_category_imageView.setImageResource(isSelected? R.drawable.hatchback_new: R.drawable.thumb_icon);
        }
    }

    @Override
    public int getItemCount() {
        return catList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView car_category_imageView;
        BTextView car_category_name;
        View view,view1;
        public MyViewHolder(View itemView) {
            super(itemView);
            car_category_name = (BTextView) itemView.findViewById(R.id.tv_car_category_name);
            car_category_imageView = (ImageView) itemView.findViewById(R.id.iv_car_category_icon);
            view = itemView.findViewById(R.id.view);
            view1 = itemView.findViewById(R.id.view1);


        }
    }
}
