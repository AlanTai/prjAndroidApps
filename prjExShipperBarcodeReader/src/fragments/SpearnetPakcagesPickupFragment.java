package fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exshipper.handlers.WebContentDownloadHandler;
import com.exshipper.listeners.ProgressBarUpdateListener;
import com.exshipper.prjexshipperbarcodereader.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SpearnetPakcagesPickupFragment extends FragmentTemplate {
	// inner variables
	String suda_tracking_number = null;
	String scanResult = "";
	Map<Integer, String> sudaTrackingNumberMap = new HashMap<Integer, String>();
	JSONObject jsonObjSUDATrackingNumbers = null;
	JSONArray jsonArySUDATrackingNumberList = null;

	WebContentDownloadHandler uploadPickedPackagesTrackingNumbersHandler = null;
	ProgressDialog mProgressBar = null;

	// end of inner variables & init function

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(true);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.spearnet_fragment, container,
				false);
		initXMLViewComponents(mView);
		return mView;
	}

	@Override
	public void onDestroy() {
		if (uploadPickedPackagesTrackingNumbersHandler != null
				&& uploadPickedPackagesTrackingNumbersHandler.getStatus() != AsyncTask.Status.FINISHED) {
			uploadPickedPackagesTrackingNumbersHandler.cancel(true);
			uploadPickedPackagesTrackingNumbersHandler = null;
		}
		txtSubmitResult = null;
		txtScanResult = null;
		txtTotalAmount = null;
		mProgressBar = null;
		layoutSUDATrackingNumbersList = null;
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent p_intent) {

		if (requestCode == 0) {
			getActivity();
			if (resultCode == FragmentActivity.RESULT_OK) {
				suda_tracking_number = p_intent.getStringExtra("SCAN_RESULT");

				//alert dialog
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("SUDA Tracking Number");
				alertDialogBuilder
						.setMessage(
								"Do you want to save the SUDA Tracking Number-"
										+ suda_tracking_number + "?")
						.setCancelable(false)
						.setPositiveButton("Yes",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										if (!sudaTrackingNumberMap
												.containsValue(suda_tracking_number)) {
											// add new suda tracking number into
											// MapList
											sudaTrackingNumberMap.put(
													suda_tracking_number
															.hashCode(),
													suda_tracking_number);
											scanResult = "The SUDA tracking number is: "
													+ suda_tracking_number;

											txtScanResult.setText(scanResult);
											txtTotalAmount
													.setText("Total Amount of SUDA Tracking Number: "
															+ sudaTrackingNumberMap
																	.size());

											// append suda tracking number info
											// onto layout-list
											TextView txtAddedSUDATrackingNumber = new TextView(
													getActivity());
											txtAddedSUDATrackingNumber.setTextColor(Color
													.parseColor("#04550E"));
											txtAddedSUDATrackingNumber
													.setTextSize(
															TypedValue.COMPLEX_UNIT_SP,
															16);
											txtAddedSUDATrackingNumber
													.setText(suda_tracking_number);
											txtAddedSUDATrackingNumber
													.setGravity(Gravity.CENTER);
											txtAddedSUDATrackingNumber
													.setPadding(3, 2, 1, 2);

											Button deleteBtn = new Button(
													getActivity());
											deleteBtn.setTextSize(
													TypedValue.COMPLEX_UNIT_SP,
													16);
											deleteBtn.setTextColor(Color
													.parseColor("#ff0000"));
											deleteBtn.setText("Delete");
											deleteBtn
													.setBackgroundColor(Color.TRANSPARENT);
											deleteBtn
													.setGravity(Gravity.CENTER);
											deleteBtn.setPadding(3, 2, 1, 2);
											deleteBtn
													.setOnClickListener(deleteSUDATrackingNumberInfo);

											LinearLayout layoutSUDATrackingNumberInfoRow = new LinearLayout(
													getActivity());
											layoutSUDATrackingNumberInfoRow
													.setOrientation(LinearLayout.HORIZONTAL);
											layoutSUDATrackingNumberInfoRow
													.setPadding(1, 2, 1, 2);
											layoutSUDATrackingNumberInfoRow
													.addView(txtAddedSUDATrackingNumber);
											layoutSUDATrackingNumberInfoRow
													.addView(deleteBtn);
											layoutSUDATrackingNumberInfoRow
													.setTag(suda_tracking_number
															.hashCode());

											deleteBtn
													.setTag(layoutSUDATrackingNumberInfoRow);
											layoutSUDATrackingNumbersList
													.addView(layoutSUDATrackingNumberInfoRow);
										} else {
											Toast.makeText(
													getActivity(),
													"SUDA Tracking Number Duplicated!",
													Toast.LENGTH_LONG).show();
										}
									}
								})
						.setNegativeButton("No",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.cancel();
									}
								});

				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

			} else {
				getActivity();
				if (resultCode == FragmentActivity.RESULT_CANCELED) {
					Log.i("APP", "Scan Unsuccessful");
				}
			}
		}
	}

	// OnClickListeners
	// start barcode scan action
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
			if (v instanceof Button) {
				Button btn = (Button) v;
				if (btn.getTag() instanceof LinearLayout) {
					LinearLayout tagLayout = (LinearLayout) btn.getTag();
					if (tagLayout.getTag() instanceof Integer) {
						Integer tagKey = (Integer) tagLayout.getTag();
						String value = sudaTrackingNumberMap.get(tagKey);
						String msg = "The deleted SUDA tracking number is: "
								+ value;
						txtScanResult.setText(msg);
						sudaTrackingNumberMap.remove(tagKey);
					}
					layoutSUDATrackingNumbersList.removeView(tagLayout);
					txtTotalAmount
							.setText("Total Amount of SUDA Tracking Number: "
									+ sudaTrackingNumberMap.size());
				}
			}
		}
	};

	OnClickListener submitSUDATrackingNumbers = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			if (sudaTrackingNumberMap.size() != 0) {
				jsonObjSUDATrackingNumbers = new JSONObject();
				jsonArySUDATrackingNumberList = new JSONArray();

				// take out all SUDA tracking numbers
				for (Object suda_obj : sudaTrackingNumberMap.values()) {
					jsonArySUDATrackingNumberList.put(suda_obj.toString());
				}
				try {
					jsonObjSUDATrackingNumbers.put("suda_tracking_numbers",
							jsonArySUDATrackingNumberList.toString());

					// upload data
					if (uploadPickedPackagesTrackingNumbersHandler != null
							&& (uploadPickedPackagesTrackingNumbersHandler.getStatus() != AsyncTask.Status.FINISHED)) {
						uploadPickedPackagesTrackingNumbersHandler.cancel(true);
					}
					uploadPickedPackagesTrackingNumbersHandler = new WebContentDownloadHandler(
							updateProgressBar);
					uploadPickedPackagesTrackingNumbersHandler
							.execute(new String[] { "https://exwine-tw.appspot.com/exshipper_spearnet_packages_pickup_handler" });
				} catch (JSONException e) {
					Log.e("error", e.getMessage());
				}
			} else {
				Toast.makeText(getActivity(), "No data for upload!",
						Toast.LENGTH_LONG).show();
			}
		}
	};
	// end of OnClickListener

	// self-defined listeners
	ProgressBarUpdateListener updateProgressBar = new ProgressBarUpdateListener() {

		@Override
		public void setupProgressBar() {
			// progress bar dialog
			if (mProgressBar == null) {
				mProgressBar = new ProgressDialog(getActivity());
				mProgressBar.setCancelable(false);
				mProgressBar.setMessage("Uploading Data...");
				mProgressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressBar.show();
			}
		}

		@Override
		public boolean isProgressCountable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void updateProgressBar(int p_progress) {
			// progress dialog
			mProgressBar.setProgress(p_progress);
		}

		@Override
		public void updateResult(String p_result) {
			// progress dialog...
			mProgressBar.dismiss();
			
			//get result from server
			if (p_result != null) {
				txtSubmitResult.setText(p_result);
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
			
			if(jsonObjSUDATrackingNumbers != null){
				nameValuePairs.add(new BasicNameValuePair(
						"spearnet_picked_packages", jsonObjSUDATrackingNumbers
								.toString()));
			}
			return nameValuePairs;
		}

	};
	// end of self-defined listeners

	/* XML view components */
	TextView txtAppIntroduction = null;
	Button btnScan = null;
	TextView txtScanResult = null;
	TextView txtTotalAmount = null;

	LinearLayout layoutSUDATrackingNumbersList = null;

	TextView txtSubmitResult = null;
	Button btnSubmitSUDATrackingNumbers = null;

	// init function
	private void initXMLViewComponents(View mView) {
		txtAppIntroduction = (TextView) mView
				.findViewById(R.id.txt_introduction);
		txtAppIntroduction
				.setText("The application is for packages pickup only!\n"
						+ "1. Scan all packages' barcodes (If you want to delete the barcode, please click the \'Delete\' key word)\n"
						+ "2. Suibmit them to server\n"
						+ "3. Get response from server and you're done");
		btnScan = (Button) mView.findViewById(R.id.btn_scan);
		btnScan.setOnClickListener(startBarcodeReader);
		txtScanResult = (TextView) mView.findViewById(R.id.txt_result);

		txtTotalAmount = (TextView) mView.findViewById(R.id.txt_total_amount);

		btnSubmitSUDATrackingNumbers = (Button) mView
				.findViewById(R.id.btn_submit_suda_tracking_number);
		btnSubmitSUDATrackingNumbers
				.setOnClickListener(submitSUDATrackingNumbers);

		layoutSUDATrackingNumbersList = (LinearLayout) mView
				.findViewById(R.id.layout_suda_tracking_numbers_list);
		txtSubmitResult = (TextView) mView.findViewById(R.id.txt_submit_result);
	}
	/* end of XML view components init function */

}