package com.exshipper.prjexshipperbarcodereader;

import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import fragments.MainFragment;
import fragments.SpearnetPakcagesPickupFragment;

public class ExShipperBarcodeReaderActivity extends FragmentActivity {
	// fragments management
	FragmentManager fm =null;
	MainFragment mainFragment = null;
	SpearnetPakcagesPickupFragment spearnetFragment = null;
	// end of fragment management
	private void initInnerVariables() {
		fm = getSupportFragmentManager();
		mainFragment = (MainFragment) fm.findFragmentById(R.id.fragment_title);
		mainFragment.gotoLogin();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main_fragment);

		//initXMLViewComponents(); // initialize XML view
		initInnerVariables(); // initialize some inner variables
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ex_shipper_barcode_reader, menu);
		return false;
	}
	

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case R.id.spearnet_packages_pickup_handler:
			mainFragment.gotoSpearnetPackagesPickupHandler();
			break;
		case R.id.tw_custom_entry_packages_handler:
			mainFragment.gotoTWCustomEntryPackagesHandler();
		case R.id.app_exit:
			Process.killProcess(Process.myPid()); //force to kill the app
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		setNullForAllVariables();
		super.onDestroy();
	}
	
	private void setNullForAllVariables(){
		// fragments management
		fm =null;
		mainFragment = null;
		spearnetFragment = null;
	}

}
