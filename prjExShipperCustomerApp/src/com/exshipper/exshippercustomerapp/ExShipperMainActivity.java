package com.exshipper.exshippercustomerapp;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ExShipperMainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ex_shipper_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ex_shipper_main, menu);
		return true;
	}

}
