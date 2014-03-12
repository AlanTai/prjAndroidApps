package fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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

	JSONObject jsonObjTWCustomEntryTrackingNumberPairs = null;
	String strScanedBarcode =null;

	WebContentDownloadHandler getTWCustomEntryNumberHandler = null;
	WebContentDownloadHandler submitPackagesSetsHandler = null;
	ProgressDialog mProgressbar = null;

	View triggerBtn = null;
	JSONObject currentPackageSet = null;

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
	public void onActivityResult(int requestCode, int resultCode, Intent p_intent) {
		// TODO Auto-generated method stub
		try {
			if(requestCode == currentPackageSet.getInt("current_btn_hashcode")){
				strScanedBarcode = p_intent.getStringExtra("SCAN_RESULT");
				
				//
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
										if (!jsonObjTWCustomEntryTrackingNumberPairs.has(strScanedBarcode)) {
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
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("error", e.getMessage());
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

					// set connection between view and textview
					if (triggerBtn != null) {
						triggerBtn = null;
						triggerBtn = v;
						triggerBtn.setTag(txtDownlaodCustomEntryNumberResult);
					}

					//
					getTWCustomEntryNumberHandler = new WebContentDownloadHandler(
							updateProgressBar);
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
			Integer btn_hashcode = (Integer) v.hashCode();
			LinearLayout current_layout = (LinearLayout) v.getTag();

			if (currentPackageSet == null) {
				currentPackageSet = new JSONObject();
			}
			startScanAction(btn_hashcode, current_layout);
		}
	};

	private void startScanAction(Integer p_btnHashcode,
			LinearLayout p_currentLayout) {
		try {
			currentPackageSet.put("current_btn_hashcode", p_btnHashcode);
			currentPackageSet.put("current_package_set", p_currentLayout);

			// start scan action
			Intent myIntent = new Intent("com.google.zxing.client.android.SCAN");
			myIntent.putExtra("SCAN_MODE", "ONE_D_MODE");
			startActivityForResult(myIntent,
					currentPackageSet.getInt("current_btn_hashcode"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Log.e("error", e.getMessage());
		}
	}

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

					// set connection between view and textview
					if (triggerBtn != null) {
						triggerBtn = null;
						triggerBtn = v;
						triggerBtn.setTag(txtSubmitResult);
					}

					//
					submitPackagesSetsHandler = new WebContentDownloadHandler(
							updateProgressBar);
					submitPackagesSetsHandler
							.execute(new String[] { "https:exwine-tw.appspot.com/exshipper_tw_custom_entry_handler" });
				}
			} catch (Exception e) {
				// TODO: handle exception
				Log.e("error", e.getMessage());
			}

		}
	};

	//
	ProgressBarUpdateListener updateProgressBar = new ProgressBarUpdateListener() {

		@Override
		public void updateResult(String p_result) {
			// TODO Auto-generated method stub
			mProgressbar.dismiss();

			// get result from server
			if (p_result != null && triggerBtn != null) {
				Object obj = triggerBtn.getTag();
				if (obj != null && obj instanceof TextView) {
					TextView currentView = (TextView) obj;
					currentView.setText(p_result);

					// create a new layout
					if (triggerBtn == btnAddNewPackagesSet) {
						//
						TextView txtBarcodeNumber = new TextView(getActivity());
						txtBarcodeNumber.setTextColor(Color
								.parseColor("#04550E"));
						txtBarcodeNumber.setTextSize(
								TypedValue.COMPLEX_UNIT_SP, 16);
						txtBarcodeNumber.setGravity(Gravity.CENTER);
						txtBarcodeNumber.setPadding(3, 2, 1, 2);
						txtBarcodeNumber.setText(p_result);
						
						Button btnDeletePackageSet = new Button(getActivity());
						btnDeletePackageSet.setTextColor(Color.parseColor("#f00"));
						btnDeletePackageSet.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
						btnDeletePackageSet.setGravity(Gravity.CENTER);
						btnDeletePackageSet.setPadding(3, 2, 1, 2);
						btnDeletePackageSet.setText("Delete Package");
						
						LinearLayout layoutPackageSetTitle = new LinearLayout(getActivity());
						layoutPackageSetTitle.setOrientation(LinearLayout.HORIZONTAL);
						layoutPackageSetTitle.setPadding(1, 2, 1, 2);
						layoutPackageSetTitle.addView(txtBarcodeNumber);
						layoutPackageSetTitle.addView(btnDeletePackageSet);
						//end
						
						//scan button & tracking numbers list
						Button btnScanBarcode = new Button(getActivity());
						btnScanBarcode.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
						btnScanBarcode.setTextColor(Color.parseColor("#00f"));
						btnScanBarcode.setGravity(Gravity.CENTER);
						btnScanBarcode.setText("Scan");
						btnScanBarcode.setOnClickListener(startBarcodeReader);
						
						LinearLayout layoutTrackingNumberList = new LinearLayout(getActivity());
						layoutTrackingNumberList.setOrientation(LinearLayout.VERTICAL);
						layoutTrackingNumberList.setPadding(1, 2, 1, 2);
						
						btnScanBarcode.setTag(layoutTrackingNumberList);
						//end
						
						//package layout
						LinearLayout layoutPackageSet = new LinearLayout(getActivity());
						layoutPackageSet.setOrientation(LinearLayout.VERTICAL);
						layoutPackageSet.setPadding(1, 2, 1, 2);
						layoutPackageSet.addView(layoutPackageSetTitle);
						layoutPackageSet.addView(btnScanBarcode);
						layoutPackageSet.addView(layoutTrackingNumberList);
						
						btnDeletePackageSet.setTag(layoutPackageSet);
						//end
						
					}
				}
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
				mProgressbar.setMessage("Obtaining Custom Entry Number...");
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

			if (jsonObjTWCustomEntryTrackingNumberPairs != null) {
				nameValuePairs.add(new BasicNameValuePair(
						"tw_custom_entry_packages_sets",
						jsonObjTWCustomEntryTrackingNumberPairs.toString()));
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
