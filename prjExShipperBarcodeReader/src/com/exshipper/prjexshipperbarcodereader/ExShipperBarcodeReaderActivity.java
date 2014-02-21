package com.exshipper.prjexshipperbarcodereader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exshipper.listeners.ProgressBarUpdateListener;
import com.exshipper.listeners.ViewsUpdateListener;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ExShipperBarcodeReaderActivity extends Activity {
	//inner variables
	String suda_tracking_number = null;
	String scanResult = "";
	Map<Integer, String> sudaTrackingNumberMap = new HashMap<Integer, String>();
	JSONObject suda_tracking_number_obj = null;
	JSONArray suda_tracking_number_list = null;
	
	LongRunningIO getCustomEnrtyNumberTask =null;
	
	private void initInnerVariables(){
		
	}
	//end of inner variables & init function
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_exshipper_barcode_reader);
		
		initXMLViewComponents(); // initialize XML view
		
		initInnerVariables(); //initialize some of inner variables
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.ex_shipper_barcode_reader, menu);
		return true;
	}
	
	@Override
	protected void onDestroy() {
		if(getCustomEnrtyNumberTask!=null && getCustomEnrtyNumberTask.getStatus()!=AsyncTask.Status.FINISHED){
			getCustomEnrtyNumberTask.cancel(true);
			getCustomEnrtyNumberTask=null;
		}
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
							sudaTrackingNumberMap.put(suda_tracking_number.hashCode(), suda_tracking_number);
							scanResult += suda_tracking_number+"\n";
							
							txtScanResult.setText(scanResult);
							txtTotalAmount.setText("Total Amount of SUDA Tracking Number: "+sudaTrackingNumberMap.size());
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
	OnClickListener startBarcodeReader = new OnClickListener() {

		@Override
		public void onClick(View v) {
			Intent myIntent = new Intent("com.google.zxing.client.android.SCAN");
			myIntent.putExtra("SCAN_MODE", "ONE_D_MODE");
			startActivityForResult(myIntent, 0);
		}
	};

	OnClickListener getCustomEntryNumber = new OnClickListener() {
		@Override
		public void onClick(View v) {
			getCustomEnrtyNumberTask = new LongRunningIO();
			getCustomEnrtyNumberTask.execute();
		}
	};
	
	OnClickListener submitSUDATrackingNumbers = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			suda_tracking_number_obj = new JSONObject();
			suda_tracking_number_list = new JSONArray();
			
			//take out all SUDA tracking numbers
			for (Object suda_obj : sudaTrackingNumberMap.values()){
				suda_tracking_number_list.put(suda_obj.toString());
			}
			try {
				suda_tracking_number_obj.put("suda_tracking_numbers", suda_tracking_number_list.toString());
			} catch (JSONException e) {
				Log.e("error", "fail to build a JSONObject");
			}
		}
	};
	//end of OnClickListener
	
	//self-defined listeners
	ViewsUpdateListener view_update = new ViewsUpdateListener() {

	};
	
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
			txtCustomNumber.setText(p_result);
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
			return nameValuePairs;
		}


	};
	//end of self-defined listeners

	/* XML view components */
	Button btnGetCustomNumber = null;
	TextView txtCustomNumber = null;
	Button btnScan = null;
	TextView txtScanResult = null;
	TextView txtTotalAmount = null;
	
	Button btnSubmitSUDATrackingNumbers = null;
	
	ProgressBar progressBar = null;
	
	//init function
	private void initXMLViewComponents() {
		btnGetCustomNumber = (Button) findViewById(R.id.btn_get_custom_number);
		btnGetCustomNumber.setOnClickListener(getCustomEntryNumber);
		txtCustomNumber = (TextView) findViewById(R.id.txt_custom_number);
		
		btnScan = (Button) findViewById(R.id.btn_scan);
		btnScan.setOnClickListener(startBarcodeReader);
		txtScanResult = (TextView) findViewById(R.id.txt_result);
		
		txtTotalAmount = (TextView) findViewById(R.id.txt_total_amount);
		
		btnSubmitSUDATrackingNumbers = (Button) findViewById(R.id.btn_submit_suda_tracking_number);
		btnSubmitSUDATrackingNumbers.setOnClickListener(submitSUDATrackingNumbers);
	}
	/* end of XML view components init function*/

	//RESTful service
	private class LongRunningIO extends AsyncTask<Void, Integer, String> {		
		@Override
		protected void onPreExecute() {
			progressBar = (ProgressBar) findViewById(R.id.progress_bar_get_customer_entry_number);
			progressBar.setVisibility(View.VISIBLE);
			super.onPreExecute();
		}

		
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected String doInBackground(Void... params) {
			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpPost httpPost = new HttpPost("https://exwine-tw.appspot.com/exshipper_custom_entry_handler");
			
			JSONObject responseJSON = null;
			String txt_custom_number = null;
			String utf8_response = null;
			
			try{
				List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("account", "alantai"));
				nameValuePairs.add(new BasicNameValuePair("password", "1014"));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				
				HttpResponse serverResponse = httpClient.execute(httpPost,localContext);
				HttpEntity entityFromServer = serverResponse.getEntity();
				utf8_response = EntityUtils.toString(entityFromServer, HTTP.UTF_8); //get data encoded in utf-8
				
				responseJSON = new JSONObject(utf8_response);
				txt_custom_number = responseJSON.getString("custom_number");
				
			}
			catch(Exception e){
				Log.e("Http Error", "Fail to get response from server! "+e.getMessage());
			}
			return txt_custom_number;
		}
		
		@Override
		protected void onPostExecute(String result) {
			progressBar.setVisibility(View.INVISIBLE);
			if(result != null){
			txtCustomNumber.setText(result);
			}
			super.onPostExecute(result);
		}

		
		//RESTful download testing
		protected String getContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException{
			/*working on...*/
			InputStream inputFromServer = entity.getContent();
			StringBuffer outResult = new StringBuffer();
			
			int n = 1;
			while(n>0){
				byte[] b = new byte[4096];
				n = inputFromServer.read(b);
				if(n>0) outResult.append(new String(b,0,n));
			}
			return outResult.toString();
		}

	}

}
