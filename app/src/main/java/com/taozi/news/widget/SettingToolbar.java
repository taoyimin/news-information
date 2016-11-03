package com.taozi.news.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.taozi.news.R;

/**
 * Created by Tao Yimin on 2016/10/9.
 * 自定义设置界面toolbar
 */
public class SettingToolbar extends RelativeLayout{
    private ImageView mImageView;
    private TextView mTextView;

    public SettingToolbar(Context context) {
        this(context,null);
    }

    public SettingToolbar(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SettingToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        View view=View.inflate(context, R.layout.layout_setting_toolbar,this);
        mTextView= (TextView) view.findViewById(R.id.setting_toolbar_tv);
        mImageView= (ImageView) view.findViewById(R.id.setting_toolbar_iv);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingToolbar);
        String title=a.getString(R.styleable.SettingToolbar_setting_title);
        int resouceId=a.getResourceId(R.styleable.SettingToolbar_back_image,R.mipmap.star_arrow_previous);
        mTextView.setText(title);
        mImageView.setImageResource(resouceId);
        mImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)context).onBackPressed();
            }
        });
    }

    public void setTitleText(String title){
        mTextView.setText(title);
    }
}
