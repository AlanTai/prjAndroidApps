package com.exshipper.prjexshipperbarcodereader;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import fragments.MainFragment;
import fragments.SpearnetPakcagesPickupFragment;

public class ExShipperBarcodeReaderActivity extends FragmentActivity {
	// fragments management
	FragmentManager fm = null;
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

		// initXMLViewComponents(); // initialize XML view
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
		case R.id.general_clients_packages_pickup_handler:
			mainFragment.gotoGeneralClientsPickupHandler();
			break;
		case R.id.tw_custom_entry_packages_handler:
			mainFragment.gotoTWCustomEntryPackagesHandler();
			break;
		case R.id.app_exit:
			Process.killProcess(Process.myPid()); // force to kill the app
			break;
		default:
			break;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Builder alertDialogBuilder = new AlertDialog.Builder(this)
					.setMessage("Do you want to leave the App?")
					.setCancelable(false)
					.setPositiveButton("Yes",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									finish();
								}
							})
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});

			AlertDialog alertDialog = alertDialogBuilder.create(); 
			alertDialog.show();
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		setNullForAllVariables();
		super.onDestroy();
	}

	private void setNullForAllVariables() {
		// fragments management
		fm = null;
		mainFragment = null;
		spearnetFragment = null;
	}

}
