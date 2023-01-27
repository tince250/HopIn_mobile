package com.example.uberapp_tim13.customs;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.ListView;

public class ExpandedListView extends ListView {

    private android.view.ViewGroup.LayoutParams params;
    private int old_count = 0;

    public ExpandedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getCount() != old_count) {
            old_count = getCount();
            params = getLayoutParams();
            if (getCount() < 3) {
                if (getCount() == 2)
                    params.height = getCount() * (old_count > 0 ? getChildAt(0).getHeight() + 20: 0);
                else
                    params.height = getCount() * (old_count > 0 ? getChildAt(0).getHeight() : 0);
            }

            else
                params.height = 2 * (old_count > 0 ? getChildAt(0).getHeight() + 50 : 0);
            setLayoutParams(params);
        }

        super.onDraw(canvas);
    }

}