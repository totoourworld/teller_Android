package com.custom;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * Created by devin on 2018-01-31.
 */

public class MyEditTextEllipsize extends BEditText{

    private String dotsString;

    private String storeString;

    public MyEditTextEllipsize(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

        if(focused)

        {
            setText(storeString);
        }else {
            String NOW = getText().toString();
            storeString = NOW;
            if (NOW != null && getWidth() <= getTextSize() * NOW.length()) {

                dotsString = NOW.substring(0, (int) (getWidth() / getTextSize())) + "...";

                setText(dotsString);

            }
        }

    }
}
