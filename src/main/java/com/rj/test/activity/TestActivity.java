package com.rj.test.activity;

import android.app.Activity;
import android.os.Bundle;

import com.rj.view.MainTabletLayout;
import com.rj.widgetlib.R;

public class TestActivity extends Activity {
	MainTabletLayout mainTabletLayout;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mainTabletLayout=(MainTabletLayout)findViewById(R.id.wanan_tablet);

	}

}
