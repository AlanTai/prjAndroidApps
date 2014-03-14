package fragments;

import java.util.ArrayList;
import java.util.List;

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
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TWCustomEntryPackagesFragment extends FragmentTemplate {
	JSONObject jsonObjTWCustomEntryPackagesSets = null;

	String strScanedBarcode = null;

	WebContentDownloadHandler getTWCustomEntryNumberHandler = null;
	WebContentDownloadHandler submitPackagesSetsHandler = null;
	ProgressDialog mProgressbar = null;

	Button currentBtnForScanBarcode = null;

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
		View mView = inflater.inflate(R.layout.tw_custom_entry_fragment,
				container, false);
		initXMLViewComponents(mView);
		return mView;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent p_intent) {
		// TODO Auto-generated method stub
		if(requestCode == 0){
			getActivity();
			if(resultCode == FragmentActivity.RESULT_OK){
				strScanedBarcode = p_intent.getStringExtra("SCAN_RESULT");
				

				//alert dialog
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						getActivity());
				alertDialogBuilder.setTitle("SUDA Tracking Number");
				alertDialogBuilder
						.setMessage(
								"Do you want to save the SUDA Tracking Number-"
										+ strScanedBarcode + "?")
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
													strScanedBarcode
															.hashCode(),
													suda_tracking_number);
											String scanResult = "The SUDA tracking number is: "
													+ strScanedBarcode;
											jsonObjTWCustomEntryPackagesSets.
											
											//
											String twCustomEntryNumber = (String) currentBtnForScanBarcode.getTag(0x1);
											JSONObject jsonObjCurrentPackageSet = (JSONObject) currentBtnForScanBarcode.getTag(0x2);
											LinearLayout layoutTrackingNumberList = (LinearLayout) currentBtnForScanBarcode.getTag(0x3);
											

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
													.setText(strScanedBarcode);
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
													.setTag(strScanedBarcode
															.hashCode());

											deleteBtn
													.setTag(layoutSUDATrackingNumberInfoRow);
											layoutTrackingNumberList
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
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (getTWCustomEntryNumberHandler != null
				&& getTWCustomEntryNumberHandler.getStatus() != AsyncTask.Status.FINISHED) {
			getTWCustomEntryNumberHandler.cancel(true);
			getTWCustomEntryNumberHandler = null;
		}
		txtTotalSets = null;
		txtTotalSets = null;
		layoutPakcagesSetsList = null;
		super.onDestroy();
	}

	// OnClickListeners
	OnClickListener getTWCustomEntryNumberAndAddNewSet = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				if (getTWCustomEntryNumberHandler != null
						&& getTWCustomEntryNumberHandler.getStatus() != AsyncTask.Status.FINISHED) {

					//
					getTWCustomEntryNumberHandler = new WebContentDownloadHandler(
							progressBarForGetTWCustomEntryNumber);
					getTWCustomEntryNumberHandler
							.execute(new String[] { "https:exwine-tw.appspot.com/exshipper_tw_custom_entry_handler" });
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("error", e.getMessage());
			}
		}
	};

	OnClickListener deletePackagesSet = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	OnClickListener startBarcodeReader = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent myIntent = new Intent("com.google.zxing.client.android.SCAN");
			myIntent.putExtra("SCAN_MODE", "ONE_D_MODE");
			startActivityForResult(myIntent, 0);
			if (v instanceof Button) {
				currentBtnForScanBarcode = (Button) v;
			}
		}
	};

	OnClickListener deleteSUDATrackingNumberInfo = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

		}
	};

	OnClickListener submitPackagesSets = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				if (submitPackagesSetsHandler != null
						&& submitPackagesSetsHandler.getStatus() != AsyncTask.Status.FINISHED) {

					// set asynctask
					submitPackagesSetsHandler = new WebContentDownloadHandler(
							progressBarForSubmitPackagesSets);
					submitPackagesSetsHandler
							.execute(new String[] { "https:exwine-tw.appspot.com/exshipper_tw_custom_entry_handler" });
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("error", e.getMessage());
			}

		}
	};

	// progress bar update listeners
	ProgressBarUpdateListener progressBarForGetTWCustomEntryNumber = new ProgressBarUpdateListener() {

		@Override
		public void updateResult(String p_result) {
			// TODO Auto-generated method stub

			// get result from server
			if (p_result != null) {
				txtDownlaodCustomEntryNumberResult.setText(p_result);
				// parse jsonObj to get TW Custom Entry Number...

				// end...

				//
				JSONObject jsonObjPackageSet = new JSONObject();
				try {
					jsonObjTWCustomEntryPackagesSets.put(p_result,
							jsonObjPackageSet);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("error", e.getMessage());
				}

				//
				TextView txtBarcodeNumber = new TextView(getActivity());
				txtBarcodeNumber.setTextColor(Color.parseColor("#04550E"));
				txtBarcodeNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				txtBarcodeNumber.setGravity(Gravity.CENTER);
				txtBarcodeNumber.setPadding(3, 2, 1, 2);
				txtBarcodeNumber.setText(p_result);

				Button btnDeletePackageSet = new Button(getActivity());
				btnDeletePackageSet.setTextColor(Color.parseColor("#f00"));
				btnDeletePackageSet.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				btnDeletePackageSet.setGravity(Gravity.CENTER);
				btnDeletePackageSet.setPadding(3, 2, 1, 2);
				btnDeletePackageSet.setText("Delete Package");
				btnDeletePackageSet.setOnClickListener(deletePackagesSet);

				LinearLayout layoutPackageSetTitle = new LinearLayout(
						getActivity());
				layoutPackageSetTitle.setOrientation(LinearLayout.HORIZONTAL);
				layoutPackageSetTitle.setPadding(1, 2, 1, 2);
				layoutPackageSetTitle.addView(txtBarcodeNumber);
				layoutPackageSetTitle.addView(btnDeletePackageSet);
				// end

				// scan button & tracking numbers list
				Button btnStartBarcodeReader = new Button(getActivity());
				btnStartBarcodeReader.setTextSize(TypedValue.COMPLEX_UNIT_SP,
						16);
				btnStartBarcodeReader.setTextColor(Color.parseColor("#00f"));
				btnStartBarcodeReader.setGravity(Gravity.CENTER);
				btnStartBarcodeReader.setText("Scan");
				btnStartBarcodeReader.setOnClickListener(startBarcodeReader);

				LinearLayout layoutTrackingNumberList = new LinearLayout(
						getActivity());
				layoutTrackingNumberList.setOrientation(LinearLayout.VERTICAL);
				layoutTrackingNumberList.setPadding(1, 2, 1, 2);

				btnStartBarcodeReader.setTag(0x1, p_result); // set barcode number
				btnStartBarcodeReader.setTag(0x2, jsonObjPackageSet); //set jsonObj packages set
				btnStartBarcodeReader.setTag(0x3, layoutTrackingNumberList); // set list
				// end

				// package layout
				LinearLayout layoutPackageSet = new LinearLayout(getActivity());
				layoutPackageSet.setOrientation(LinearLayout.VERTICAL);
				layoutPackageSet.setPadding(1, 2, 1, 2);
				layoutPackageSet.addView(layoutPackageSetTitle);
				layoutPackageSet.addView(btnStartBarcodeReader);
				layoutPackageSet.addView(layoutTrackingNumberList);

				btnDeletePackageSet.setTag(0x1, p_result); // set tw custom
															// entry number
				btnDeletePackageSet.setTag(0x2, jsonObjPackageSet); // set
																	// jsonObj
																	// packages
																	// set
				btnDeletePackageSet.setTag(0x3, layoutPackageSet);
				// end

			}

		}

		@Override
		public void updateProgressBar(int p_progress) {
			// TODO Auto-generated method stub
			mProgressbar.setProgress(p_progress);

		}

		@Override
		public void setupProgressBar() {
			// TODO Auto-generated method stub
			if (mProgressbar == null) {
				mProgressbar = new ProgressDialog(getActivity());
				mProgressbar.setCancelable(true);
				mProgressbar
						.setMessage("Submit TW Custom Entry Packages Information...");
				mProgressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressbar.show();
			}
		}

		@Override
		public List<NameValuePair> setAuthorizationInfo() {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("account", "alantai"));
			nameValuePairs.add(new BasicNameValuePair("password", "1014"));

			return nameValuePairs;
		}

		@Override
		public boolean isProgressCountable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAuthorizationNecessary() {
			// TODO Auto-generated method stub
			return true;
		}
	};

	//
	ProgressBarUpdateListener progressBarForSubmitPackagesSets = new ProgressBarUpdateListener() {
		@Override
		public void updateResult(String p_result) {
			// TODO Auto-generated method stub
			mProgressbar.dismiss();
			mProgressbar = null;
			if (p_result != null) {
				txtSubmitResult.setText(p_result);
			}
		}

		@Override
		public void updateProgressBar(int p_progress) {
			// TODO Auto-generated method stub
			mProgressbar.setProgress(p_progress);

		}

		@Override
		public void setupProgressBar() {
			// TODO Auto-generated method stub
			if (mProgressbar == null) {
				mProgressbar = new ProgressDialog(getActivity());
				mProgressbar.setCancelable(true);
				mProgressbar
						.setMessage("Submit TW Custom Entry Packages Information...");
				mProgressbar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressbar.show();
			}

		}

		@Override
		public List<NameValuePair> setAuthorizationInfo() {
			// TODO Auto-generated method stub
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("account", "alantai"));
			nameValuePairs.add(new BasicNameValuePair("password", "1014"));

			if (jsonObjTWCustomEntryPackagesSets != null) {
				nameValuePairs.add(new BasicNameValuePair(
						"tw_custom_entry_packages_sets",
						jsonObjTWCustomEntryPackagesSets.toString()));
			}
			return nameValuePairs;
		}

		@Override
		public boolean isProgressCountable() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean isAuthorizationNecessary() {
			// TODO Auto-generated method stub
			return true;
		}
	};

	/* XML components */
	TextView txtIntroduction = null;
	Button btnAddNewPackagesSet = null;
	TextView txtDownlaodCustomEntryNumberResult = null;
	TextView txtTotalSets = null;

	LinearLayout layoutPakcagesSetsList = null;

	TextView txtSubmitResult = null;
	Button btnSubmitPakcagesSets = null;

	private void initXMLViewComponents(View mView) {
		txtIntroduction = (TextView) mView
				.findViewById(R.id.txt_introduction_tw_custom_entry);
		txtIntroduction.setText("TW Custom Entry Handler");

		btnAddNewPackagesSet = (Button) mView
				.findViewById(R.id.btn_add_new_packages_set_tw_custom_entry);
		btnAddNewPackagesSet
				.setOnClickListener(getTWCustomEntryNumberAndAddNewSet);

		txtDownlaodCustomEntryNumberResult = (TextView) mView
				.findViewById(R.id.txt_download_tw_custom_entry_number_result);

		txtTotalSets = (TextView) mView
				.findViewById(R.id.txt_total_sets_tw_custom_entry);

		txtSubmitResult = (TextView) mView
				.findViewById(R.id.txt_submit_result_tw_custom_entry);
		btnSubmitPakcagesSets = (Button) mView
				.findViewById(R.id.btn_submit_packages_sets_tw_custom_entry);
		btnSubmitPakcagesSets.setOnClickListener(submitPackagesSets);
	}

}
