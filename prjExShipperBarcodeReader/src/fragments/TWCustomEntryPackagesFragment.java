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
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
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
		if (requestCode == 0) {
			getActivity();
			if (resultCode == FragmentActivity.RESULT_OK) {
				strScanedBarcode = p_intent.getStringExtra("SCAN_RESULT");

				// alert dialog
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
										JSONArray jsonAryNames = jsonObjTWCustomEntryPackagesSets
												.names(); // get all names from
															// jsonObj
										
										boolean isTrackingNumberExist = false;
										for (int ith = 0; ith < jsonAryNames
												.length(); ith++) {
											JSONObject jsonObj;
											try {
												jsonObj = jsonObjTWCustomEntryPackagesSets
														.getJSONObject(jsonAryNames
																.getString(ith));
		
												// check if scanned barcode
												// exists
												if (jsonObj
														.has(strScanedBarcode)) {
													Toast.makeText(
															getActivity(),
															"SUDA Tracking Number Duplicated!",
															Toast.LENGTH_LONG)
															.show();
													isTrackingNumberExist = true;
													break;
												}
											} catch (JSONException e) {
												// TODO Auto-generated catch
												// block
												Log.e("error", e.getMessage());
											}
										}
										
										//
										if(!isTrackingNumberExist){
											// add new barcode into
											// corresponding package set
											try{

												JSONObject jsonObjFromBtnScanBarcode = (JSONObject) currentBtnForScanBarcode
														.getTag();

												String twCustomEntryNumber = (String) jsonObjFromBtnScanBarcode
														.get("p_result");
												JSONObject jsonObjCurrentPackagesSet = (JSONObject) jsonObjFromBtnScanBarcode
														.getJSONObject("jsonObjPackageSet");
												LinearLayout layoutCurrentPackagesSet = (LinearLayout) jsonObjFromBtnScanBarcode
														.get("layoutTrackingNumberList");

												// add to obj
												jsonObjCurrentPackagesSet
														.put(strScanedBarcode,
																twCustomEntryNumber);

												// add layout-list
												TextView txtAddedSUDATrackingNumber = new TextView(
														getActivity());
												txtAddedSUDATrackingNumber.setTextColor(Color
														.parseColor("#04550E"));
												txtAddedSUDATrackingNumber
														.setTextSize(
																TypedValue.COMPLEX_UNIT_SP,
																16);
												txtAddedSUDATrackingNumber
														.setText("SUDA NO."+strScanedBarcode);
												txtAddedSUDATrackingNumber
														.setGravity(Gravity.CENTER);
												txtAddedSUDATrackingNumber
														.setPadding(3, 2,
																1, 2);

												Button btnDeleteInfoRow = new Button(
														getActivity());
												btnDeleteInfoRow
														.setTextSize(
																TypedValue.COMPLEX_UNIT_SP,
																16);
												btnDeleteInfoRow.setTextColor(Color
														.parseColor("#ff0000"));
												btnDeleteInfoRow.setText("Delete");
												btnDeleteInfoRow
														.setBackgroundColor(Color.TRANSPARENT);
												btnDeleteInfoRow
														.setGravity(Gravity.CENTER);
												btnDeleteInfoRow.setPadding(3, 2,
														1, 2);
												btnDeleteInfoRow
														.setOnClickListener(deleteSUDATrackingNumberInfo);

												LinearLayout layoutSUDATrackingNumberInfoRow = new LinearLayout(
														getActivity());
												layoutSUDATrackingNumberInfoRow
														.setOrientation(LinearLayout.HORIZONTAL);
												layoutSUDATrackingNumberInfoRow
														.setPadding(1, 2,
																1, 2);
												layoutSUDATrackingNumberInfoRow
														.addView(txtAddedSUDATrackingNumber);
												layoutSUDATrackingNumberInfoRow
														.addView(btnDeleteInfoRow);
												layoutSUDATrackingNumberInfoRow
														.setTag(strScanedBarcode
																.hashCode());
												
												//
												JSONObject jsonObjCurrentInfoRow = new JSONObject();
												jsonObjCurrentInfoRow.put("layoutSUDATrackingNumberInfoRow", layoutSUDATrackingNumberInfoRow);
												jsonObjCurrentInfoRow.put("layoutCurrentPackagesSet", layoutCurrentPackagesSet);
												jsonObjCurrentInfoRow.put("jsonObjCurrentPackagesSet", jsonObjCurrentPackagesSet);
												jsonObjCurrentInfoRow.put("strScanedBarcode", strScanedBarcode);
												btnDeleteInfoRow.setTag(jsonObjCurrentInfoRow);

												layoutCurrentPackagesSet
														.addView(layoutSUDATrackingNumberInfoRow);
											}
											catch (Exception e){
												Log.e("error", e.getMessage());
											}

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
					getTWCustomEntryNumberHandler.cancel(true);
				}

				//
				getTWCustomEntryNumberHandler = new WebContentDownloadHandler(
						progressBarForGetTWCustomEntryNumber);
				getTWCustomEntryNumberHandler
						.execute(new String[] { "https://exwine-tw.appspot.com/exshipper_tw_custom_entry_handler" });
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
			if (v instanceof Button) {
				JSONObject jsonObjFromBtnScanBarcode = (JSONObject) v.getTag();
				try {
					String twCustomEntryNumber = (String) jsonObjFromBtnScanBarcode
							.get("p_result");
					LinearLayout layoutCurrentPackageSet = (LinearLayout) jsonObjFromBtnScanBarcode
							.get("layoutPackagesSets");
					
					txtDownlaodCustomEntryNumberResult.setText("Current Deleted SUDA NO."+twCustomEntryNumber);
					

					jsonObjTWCustomEntryPackagesSets
							.remove(twCustomEntryNumber);
					layoutCurrentPackageSet.removeAllViews();
					layoutPakcagesSetsList.removeView(layoutCurrentPackageSet);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Log.e("error", e.getMessage());
				}

			}

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
			if (v instanceof Button) {
				Button btn = (Button) v;
				if (btn.getTag() instanceof JSONObject) {
					JSONObject jsonObj = (JSONObject) btn.getTag();
					try{
						JSONObject jsonObjCurrentPackagesSet = jsonObj.getJSONObject("jsonObjCurrentPackagesSet");
						LinearLayout layoutCurrentPackagesSet = (LinearLayout) jsonObj.get("layoutCurrentPackagesSet");
						LinearLayout layoutSUDATrackingNumberInfoRow = (LinearLayout) jsonObj.get("layoutSUDATrackingNumberInfoRow");
						String strScanedBarcode = jsonObj.getString("strScanedBarcode");
						
						jsonObjCurrentPackagesSet.remove(strScanedBarcode);
						layoutCurrentPackagesSet.removeView(layoutSUDATrackingNumberInfoRow);
					}
					catch (Exception e){
						Log.e("error", e.getMessage());
					}

				}
			}

		}
	};

	OnClickListener submitPackagesSets = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			try {
				if (submitPackagesSetsHandler != null
						&& submitPackagesSetsHandler.getStatus() != AsyncTask.Status.FINISHED) {
					submitPackagesSetsHandler.cancel(true);
				}

				// set asynctask
				submitPackagesSetsHandler = new WebContentDownloadHandler(
						progressBarForSubmitPackagesSets);
				submitPackagesSetsHandler
						.execute(new String[] { "https://exwine-tw.appspot.com/exshipper_tw_custom_entry_handler" });
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
			mProgressbar.dismiss();
			mProgressbar = null;

			// get result from server
			if (p_result != null) {
				txtDownlaodCustomEntryNumberResult.setText("Current Obtained TW Custom Entry NO."+p_result);
				// parse jsonObj to get TW Custom Entry Number...

				//
				if (!"NA".equals(p_result)) {

					JSONObject jsonObjPackagesSet = new JSONObject();
					try {
						if (jsonObjTWCustomEntryPackagesSets == null) {
							jsonObjTWCustomEntryPackagesSets = new JSONObject();
						}

						if (jsonObjTWCustomEntryPackagesSets
								.has(p_result)) {
							Toast.makeText(
									getActivity(),
									"Duplicated TW Custom Entry Number- "
											+ p_result, Toast.LENGTH_LONG)
									.show();
						} else {
							jsonObjTWCustomEntryPackagesSets.put(p_result,
									jsonObjPackagesSet);
							
							//build View
							TextView txtTWCustomEntryNumber = new TextView(
									getActivity());
							txtTWCustomEntryNumber.setTextColor(Color
									.parseColor("#000000"));
							txtTWCustomEntryNumber.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 16);
							txtTWCustomEntryNumber.setGravity(Gravity.CENTER);
							txtTWCustomEntryNumber.setPadding(3, 2, 1, 2);
							txtTWCustomEntryNumber.setText("TW Custom Entry NO. "+p_result);

							Button btnDeletePackageSet = new Button(
									getActivity());
							btnDeletePackageSet.setTextColor(Color
									.parseColor("#ff0000"));
							btnDeletePackageSet.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 16);
							btnDeletePackageSet
									.setBackgroundColor(Color.TRANSPARENT);
							btnDeletePackageSet.setGravity(Gravity.CENTER);
							btnDeletePackageSet.setPadding(3, 2, 1, 2);
							btnDeletePackageSet.setText("Delete Package");
							btnDeletePackageSet
									.setOnClickListener(deletePackagesSet);

							// scan button & tracking numbers list
							Button btnStartBarcodeReader = new Button(
									getActivity());
							btnStartBarcodeReader.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 16);
							btnStartBarcodeReader.setTextColor(Color
									.parseColor("#0000ff"));
							btnStartBarcodeReader
									.setBackgroundColor(Color.TRANSPARENT);
							btnStartBarcodeReader.setGravity(Gravity.CENTER);
							btnStartBarcodeReader.setText("Scan");
							btnStartBarcodeReader
									.setOnClickListener(startBarcodeReader);

							//
							LinearLayout layoutPackageSetBtnsField = new LinearLayout(
									getActivity());
							layoutPackageSetBtnsField
									.setOrientation(LinearLayout.HORIZONTAL);
							layoutPackageSetBtnsField.setPadding(1, 2, 1, 2);
							layoutPackageSetBtnsField
									.addView(btnStartBarcodeReader);
							layoutPackageSetBtnsField.addView(btnDeletePackageSet);
							// end

							LinearLayout layoutTrackingNumberList = new LinearLayout(
									getActivity());
							layoutTrackingNumberList
									.setOrientation(LinearLayout.VERTICAL);
							layoutTrackingNumberList.setPadding(1, 2, 1, 2);
							
							//psrting line
							View viewPartingLine = new View(getActivity());
							viewPartingLine.setLayoutParams(new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, 3));
							viewPartingLine.setBackgroundColor(Color.parseColor("#ffffff"));

							// package layout
							LinearLayout layoutPackagesSet = new LinearLayout(
									getActivity());
							layoutPackagesSet
									.setOrientation(LinearLayout.VERTICAL);
							layoutPackagesSet.setBackgroundColor(Color.parseColor("#e1e1e1"));
							layoutPackagesSet.setPadding(1, 2, 1, 2);
							layoutPackagesSet.addView(txtTWCustomEntryNumber);
							layoutPackagesSet.addView(layoutPackageSetBtnsField);
							layoutPackagesSet.addView(layoutTrackingNumberList);
							layoutPackagesSet.addView(viewPartingLine);
							layoutPakcagesSetsList.addView(layoutPackagesSet);

							//
							JSONObject jsonObj = new JSONObject();
							jsonObj.put("p_result", p_result);
							jsonObj.put("jsonObjPackageSet", jsonObjPackagesSet);
							jsonObj.put("layoutPackagesSets", layoutPackagesSet);
							jsonObj.put("layoutTrackingNumberList",
									layoutTrackingNumberList);

							btnStartBarcodeReader.setTag(jsonObj);
							btnDeletePackageSet.setTag(jsonObj);
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.e("error", e.getMessage());
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
				mProgressbar.setCancelable(false);
				mProgressbar.setMessage("Get TW Custom Entry Number...");
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
				mProgressbar.setCancelable(false);
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

		layoutPakcagesSetsList = (LinearLayout) mView
				.findViewById(R.id.layout_tw_custom_entry_tracking_numbers_list);

		txtSubmitResult = (TextView) mView
				.findViewById(R.id.txt_submit_result_tw_custom_entry);
		btnSubmitPakcagesSets = (Button) mView
				.findViewById(R.id.btn_submit_packages_sets_tw_custom_entry);
		btnSubmitPakcagesSets.setOnClickListener(submitPackagesSets);
	}

}
