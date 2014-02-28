package com.exshipper.prjexshipperbarcodereader;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.exshipper.handlers.WebContentDownloadHandler;
import com.exshipper.listeners.ProgressBarUpdateListener;

public class ExShipperBarcodeReaderActivity extends Activity {
	//inner variables
	String suda_tracking_number = null;
	String scanResult = "";
	Map<Integer, String> sudaTrackingNumberMap = new HashMap<Integer, String>();
	JSONObject suda_tracking_numbers_json_obj = null;
	JSONArray suda_tracking_number_list = null;
	
	WebContentDownloadHandler uploadPickedPackagesTrackingNumbers =null;
	private void initInnerVariables(){
		
	}
	//end of inner variables & init function
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exshipper_barcode_reader);
		
		initXMLViewComponents(); // initialize XML view
		
		initInnerVariables(); //initialize some inner variables
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ex_shipper_barcode_reader, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		if(uploadPickedPackagesTrackingNumbers!=null && uploadPickedPackagesTrackingNumbers.getStatus()!=AsyncTask.Status.FINISHED){
			uploadPickedPackagesTrackingNumbers.cancel(true);
			uploadPickedPackagesTrackingNumbers=null;
		}
		txtScanResult.setText("");
		txtScanResult = null;
		txtTotalAmount.setText("");
		txtTotalAmount = null;
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent p_intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				suda_tracking_number = p_intent.getStringExtra("SCAN_RESULT");
				
				//
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExShipperBarcodeReaderActivity.this);
				alertDialogBuilder.setTitle("SUDA Tracking Number");
				alertDialogBuilder.setMessage("Do you want to save the SUDA Tracking Number-"+suda_tracking_number+"?")
				.setCancelable(false)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if(!sudaTrackingNumberMap.containsValue(suda_tracking_number)){
							//add new suda tracking number into MapList
							sudaTrackingNumberMap.put(suda_tracking_number.hashCode(), suda_tracking_number);
							scanResult = "The SUDA tracking number is: "+suda_tracking_number;
							
							txtScanResult.setText(scanResult);
							txtTotalAmount.setText("Total Amount of SUDA Tracking Number: "+sudaTrackingNumberMap.size());
							
							//append suda tracking number info onto layout-list
							TextView txtAddedSUDATrackingNumber = new TextView(ExShipperBarcodeReaderActivity.this);
							txtAddedSUDATrackingNumber.setTextColor(Color.parseColor("#E39D53"));
							txtAddedSUDATrackingNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
							txtAddedSUDATrackingNumber.setText(suda_tracking_number);
							txtAddedSUDATrackingNumber.setGravity(Gravity.CENTER);
							txtAddedSUDATrackingNumber.setPadding(3, 2, 1, 2);
							
							Button deleteBtn = new Button(ExShipperBarcodeReaderActivity.this);
							deleteBtn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
							deleteBtn.setTextColor(Color.parseColor("#ff0000"));
							deleteBtn.setText("Delete");
							deleteBtn.setBackgroundColor(Color.TRANSPARENT);
							deleteBtn.setGravity(Gravity.CENTER);
							deleteBtn.setPadding(3, 2, 1, 2);
							deleteBtn.setOnClickListener(deleteSUDATrackingNumberInfo);
							
							LinearLayout layoutSUDATrackingNumberInfoRow = new LinearLayout(ExShipperBarcodeReaderActivity.this);
							layoutSUDATrackingNumberInfoRow.setOrientation(LinearLayout.HORIZONTAL);
							layoutSUDATrackingNumberInfoRow.setPadding(1, 2, 1, 2);
							layoutSUDATrackingNumberInfoRow.addView(txtAddedSUDATrackingNumber);
							layoutSUDATrackingNumberInfoRow.addView(deleteBtn);
							layoutSUDATrackingNumberInfoRow.setTag(suda_tracking_number.hashCode());
							
							deleteBtn.setTag(layoutSUDATrackingNumberInfoRow);
							layoutSUDATrackingNumbersList.addView(layoutSUDATrackingNumberInfoRow);
						}
						else{
							Toast.makeText(ExShipperBarcodeReaderActivity.this, "SUDA Tracking Number Duplicated!", Toast.LENGTH_LONG).show();
						}
					}
				})
				.setNegativeButton("No", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});
				
				//create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();
				
			} else if (resultCode == RESULT_CANCELED) {
				Log.i("APP", "Scan Unsuccessful");
			}
		}
	}

	// OnclickListeners
	//start barcode scan action
	OnClickListener startBarcodeReader = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent("com.google.zxing.client.android.SCAN");
			myIntent.putExtra("SCAN_MODE", "ONE_D_MODE");
			startActivityForResult(myIntent, 0);
		}
	};
	
	OnClickListener deleteSUDATrackingNumberInfo = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(v instanceof Button){
				Button btn = (Button) v;
				if(btn.getTag() instanceof LinearLayout){
					LinearLayout tagLayout = (LinearLayout) btn.getTag();
					if(tagLayout.getTag() instanceof Integer){
						Integer tagKey = (Integer) tagLayout.getTag();
						String value = sudaTrackingNumberMap.get(tagKey);
						String msg = "The deleted SUDA tracking number is: "+value;
						txtScanResult.setText(msg);
						sudaTrackingNumberMap.remove(tagKey);
					}
					layoutSUDATrackingNumbersList.removeView(tagLayout);
					txtTotalAmount.setText("Total Amount of SUDA Tracking Number: "+sudaTrackingNumberMap.size());
				}
			}
		}
	};
	
	OnClickListener submitSUDATrackingNumbers = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			if(sudaTrackingNumberMap.size()!=0){
				suda_tracking_numbers_json_obj = new JSONObject();
				suda_tracking_number_list = new JSONArray();
				
				//take out all SUDA tracking numbers
				for (Object suda_obj : sudaTrackingNumberMap.values()){
					suda_tracking_number_list.put(suda_obj.toString());
				}
				try {
					suda_tracking_numbers_json_obj.put("suda_tracking_numbers", suda_tracking_number_list.toString());
					
					//upload data
					if(uploadPickedPackagesTrackingNumbers != null && (uploadPickedPackagesTrackingNumbers.getStatus() != AsyncTask.Status.FINISHED)){
						uploadPickedPackagesTrackingNumbers.cancel(true);
					}
					uploadPickedPackagesTrackingNumbers = new WebContentDownloadHandler(progress_bar_update);
					uploadPickedPackagesTrackingNumbers.execute(new String[] {"https://exwine-tw.appspot.com/exshipper_spearnet_packages_pickup_handler"});
				} catch (JSONException e) {
					Log.e("error", "fail to build a JSONObject");
				}
			}
			else{
				Toast.makeText(ExShipperBarcodeReaderActivity.this, "No data for upload!", Toast.LENGTH_LONG).show();
			}
		}
	};
	//end of OnClickListener
	
	//self-defined listeners
	ProgressBarUpdateListener progress_bar_update = new ProgressBarUpdateListener() {
		
		@Override
		public void setupProgressBar() {
			// TODO Auto-generated method stub
			progressBar = (ProgressBar) findViewById(R.id.progress_bar_get_customer_entry_number);
			progressBar.setVisibility(View.VISIBLE);
		}
		
		@Override
		public boolean isProgressCountable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void updateProgressBar(int p_progress) {
			// TODO Auto-generated method stub
			progressBar.setProgress(p_progress);
		}

		@Override
		public void updateResult(String p_result) {
			// TODO Auto-generated method stub
			progressBar.setVisibility(View.INVISIBLE);
			if(p_result != null){
			txtAppIntroduction.setText(p_result);
			}
		}

		@Override
		public boolean isAuthorizationNecessary() {
			// user should set authorization information after return true
			return true;
		}

		@Override
		public List<NameValuePair> setAuthorizationInfo() {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("account", "alantai"));
			nameValuePairs.add(new BasicNameValuePair("password", "1014"));
			nameValuePairs.add(new BasicNameValuePair("spearnet_picked_packages", suda_tracking_numbers_json_obj.toString()));
			return nameValuePairs;
		}


	};
	//end of self-defined listeners

	/* XML view components */
	TextView txtAppIntroduction = null;
	Button btnScan = null;
	TextView txtScanResult = null;
	TextView txtTotalAmount = null;
	
	LinearLayout layoutSUDATrackingNumbersList = null;
	
	Button btnSubmitSUDATrackingNumbers = null;
	ProgressBar progressBar = null;
	
	//init function
	private void initXMLViewComponents() {
		txtAppIntroduction = (TextView) findViewById(R.id.txt_introduction);
		txtAppIntroduction.setText("The application is for packages pickup only!\n" +
				"1. Scan all packages' barcodes (If you want to delete the barcode, please click the \'Delete\' key word)\n" +
				"2. Suibmit them to server\n" +
				"3. Get response from server and you're done");
		btnScan = (Button) findViewById(R.id.btn_scan);
		btnScan.setOnClickListener(startBarcodeReader);
		txtScanResult = (TextView) findViewById(R.id.txt_result);
		
		txtTotalAmount = (TextView) findViewById(R.id.txt_total_amount);
		
		btnSubmitSUDATrackingNumbers = (Button) findViewById(R.id.btn_submit_suda_tracking_number);
		btnSubmitSUDATrackingNumbers.setOnClickListener(submitSUDATrackingNumbers);
		
		layoutSUDATrackingNumbersList = (LinearLayout) findViewById(R.id.layout_suda_tracking_numbers_list);
	}
	/* end of XML view components init function*/

}
