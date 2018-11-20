package com.example.peter.exercise2.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.peter.exercise2.HomeActivity;
import com.example.peter.exercise2.R;

public class ViewPagerAdapter extends PagerAdapter {

    int[] img;
    LayoutInflater inflater;
    Context context;

    public ViewPagerAdapter(HomeActivity homeActivity, int[] img) {
        this.context = homeActivity;
        this.img = img;
    }

    @Override
    public int getCount() {
        return img.length;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        ImageView trailimg;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item, container,false);
        trailimg = itemView.findViewById(R.id.trailimg);
        trailimg.setImageResource(img[position]);
        ((ViewPager)container).addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        ((ViewPager)container).removeView((RelativeLayout) object);
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== ((RelativeLayout) object);
    }
}
