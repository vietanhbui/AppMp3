package com.anhbv.appmp3.ui.layouts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ItemTabFrag extends LinearLayout {
    private int position;

    private ImageView imvTab;
    private TextView tvTab;

    private LayoutParams paramsImv;
    private LayoutParams paramsTv;

    private IOnClickItemTab iOnClickItemTab;
    public ItemTabFrag(Context context, int res, String text, int width, int position) {
        super(context);
        this.position = position;

        initView(res, text,width);
    }

    private void initView(final int res, String text, int width) {
        LayoutParams params = new LayoutParams(width,ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);

        imvTab = new ImageView(getContext());
        tvTab = new TextView(getContext());

        setGravity(Gravity.CENTER);
        setOrientation(VERTICAL);

        tvTab.setTextColor(Color.parseColor("#999999"));
        imvTab.setColorFilter(Color.parseColor("#999999"));
        tvTab.setTextSize(11);
        imvTab.setScaleType(ImageView.ScaleType.FIT_XY);

        tvTab.setTypeface(null,Typeface.BOLD);

        paramsImv = new LayoutParams(75,75);
        paramsTv = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        setContainer(res,text);
        addView(imvTab);
        addView(tvTab);

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                iOnClickItemTab.OnClickItemTab(position);
            }
        });
    }

    private void setContainer(int res, String text){
        imvTab.setImageResource(res);
        tvTab.setText(text);
        imvTab.setLayoutParams(paramsImv);
        tvTab.setLayoutParams(paramsTv);
    }

    public void selected(boolean isSelect){
        if(isSelect){
            tvTab.setTextColor(Color.parseColor("#660066"));
            imvTab.setColorFilter(Color.parseColor("#660066"));
        }
        else {
            tvTab.setTextColor(Color.parseColor("#999999"));
            imvTab.setColorFilter(Color.parseColor("#999999"));
        }
    }

    public interface IOnClickItemTab{
        void OnClickItemTab(int position);
    }

    public void setiOnClickItemTab(IOnClickItemTab iOnClickItemTab){
        this.iOnClickItemTab = iOnClickItemTab;
    }

}
