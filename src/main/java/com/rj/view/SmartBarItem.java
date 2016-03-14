package com.rj.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rj.widgetlib.R;


/**
 * Created by jxwq on 2016/1/21.
 */
public class SmartBarItem extends LinearLayout {

    public static final int EMPTY_WIDTH_IN_DP = 34; //textView内容为空时默认所占宽度
    private Button button;

    public SmartBarItem(Context context) {
        this(context, null);
    }

    public SmartBarItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmartBarItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER_VERTICAL);
        int verticalPadding = (int) (getResources().getDisplayMetrics().density * 15 + 0.5f);
        setPadding(0, 0, 0, 0);
        LayoutInflater.from(getContext()).inflate(R.layout.tablet_smartbar_item, this, true);
        button = (Button) findViewById(R.id.smarbar_item_bt);
    }

    public Button getButton() {
        return this.button;
    }

}
