package com.haivo.editablerecyclerview.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.haivo.editablerecyclerview.bean.AppBean;
import com.haivo.editablerecyclerview.CommonAppsAdapter;
import com.haivo.editablerecyclerview.R;

import java.util.List;


/**
 * desc
 *
 * @author wumaoqiang
 * @date 2021/12/23 11:39
 */
public class HorizontalItemsLayout extends LinearLayout {
    private OnLeftSlideListener onLeftSlideListener;
    private CommonAppsAdapter commonAppsAdapter;
    private float lastX = 0;

    public HorizontalItemsLayout(Context context) {
        super(context);
        init(context);
    }

    public HorizontalItemsLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_horizontal_items, this);
        RecyclerView recyclerView = view.findViewById(R.id.rv_ho);
        commonAppsAdapter = new CommonAppsAdapter();
        GridLayoutManager layoutManager =
                new GridLayoutManager(context, 4, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commonAppsAdapter);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                float offX = event.getX() - lastX;
                if (offX < 0) {
                    onLeftSlideListener.onLeftSlide();
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }


    public interface OnLeftSlideListener {
        void onLeftSlide();
    }

    public void setOnLeftSlideListener(OnLeftSlideListener onLeftSlideListener) {
        this.onLeftSlideListener = onLeftSlideListener;
    }


    public void setList(List<AppBean> apps) {
        commonAppsAdapter.setData(apps);
    }
}