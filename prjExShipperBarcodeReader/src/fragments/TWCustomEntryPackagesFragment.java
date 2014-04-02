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
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TWCustomEntryPackagesFragment extends FragmentTemplate {
	JSONObject jsonObjTWCustomEntryPackagesSets = null;
	JSONObject jsonObjTWCustomEntryPackageSizeWeight = null;

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
										if (!isTrackingNumberExist) {
											// add new barcode into
											// corresponding package set
											try {

												JSONObject jsonObjFromBtnScanBarcode = (JSONObject) currentBtnForScanBarcode
														.getTag();

												// String twCustomEntryNumber =
												// (String)
												// jsonObjFromBtnScanBarcode.get("p_result");
												// //get tw custom entry package
												// number
												JSONObject jsonObjCurrentPackagesSet = (JSONObject) jsonObjFromBtnScanBarcode
														.getJSONObject("jsonObjPackageSet");
												LinearLayout layoutCurrentPackagesSet = (LinearLayout) jsonObjFromBtnScanBarcode
														.get("layoutTrackingNumberList");

												// add to obj
												jsonObjCurrentPackagesSet.put(
														strScanedBarcode,
														"exshipper");

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
														.setText("SUDA NO."
																+ strScanedBarcode);
												txtAddedSUDATrackingNumber
														.setGravity(Gravity.CENTER);
												txtAddedSUDATrackingNumber
														.setPadding(3, 2, 1, 2);

												Button btnDeleteInfoRow = new Button(
														getActivity());
												btnDeleteInfoRow
														.setTextSize(
																TypedValue.COMPLEX_UNIT_SP,
																16);
												btnDeleteInfoRow.setTextColor(Color
														.parseColor("#ff0000"));
												btnDeleteInfoRow
														.setText("Delete NO.");
												btnDeleteInfoRow
														.setBackgroundColor(Color.TRANSPARENT);
												btnDeleteInfoRow
														.setGravity(Gravity.CENTER);
												btnDeleteInfoRow
														.setBackgroundResource(R.drawable.clicked_item);
												btnDeleteInfoRow.setPadding(3,
														2, 1, 2);
												btnDeleteInfoRow
														.setOnClickListener(deleteSUDATrackingNumberInfo);

												LinearLayout layoutSUDATrackingNumberInfoRow = new LinearLayout(
														getActivity());
												layoutSUDATrackingNumberInfoRow
														.setOrientation(LinearLayout.HORIZONTAL);
												layoutSUDATrackingNumberInfoRow
														.setPadding(1, 2, 1, 2);
												layoutSUDATrackingNumberInfoRow
														.addView(btnDeleteInfoRow);
												layoutSUDATrackingNumberInfoRow
														.addView(txtAddedSUDATrackingNumber);
												layoutSUDATrackingNumberInfoRow
														.setTag(strScanedBarcode
																.hashCode());

												//
												JSONObject jsonObjCurrentInfoRow = new JSONObject();
												jsonObjCurrentInfoRow
														.put("layoutSUDATrackingNumberInfoRow",
																layoutSUDATrackingNumberInfoRow);
												jsonObjCurrentInfoRow
														.put("layoutCurrentPackagesSet",
																layoutCurrentPackagesSet);
												jsonObjCurrentInfoRow
														.put("jsonObjCurrentPackagesSet",
																jsonObjCurrentPackagesSet);
												jsonObjCurrentInfoRow.put(
														"strScanedBarcode",
														strScanedBarcode);
												btnDeleteInfoRow
														.setTag(jsonObjCurrentInfoRow);

												layoutCurrentPackagesSet
														.addView(layoutSUDATrackingNumberInfoRow);
											} catch (Exception e) {
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
				Toast.makeText(getActivity(), "Fail to get response from server!", Toast.LENGTH_SHORT).show();
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

					txtDownlaodCustomEntryNumberResult
							.setText("Current Deleted TW Custom Entry NO."
									+ twCustomEntryNumber);

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
					try {
						JSONObject jsonObjCurrentPackagesSet = jsonObj
								.getJSONObject("jsonObjCurrentPackagesSet");
						LinearLayout layoutCurrentPackagesSet = (LinearLayout) jsonObj
								.get("layoutCurrentPackagesSet");
						LinearLayout layoutSUDATrackingNumberInfoRow = (LinearLayout) jsonObj
								.get("layoutSUDATrackingNumberInfoRow");
						String strScanedBarcode = jsonObj
								.getString("strScanedBarcode");

						jsonObjCurrentPackagesSet.remove(strScanedBarcode);
						layoutCurrentPackagesSet
								.removeView(layoutSUDATrackingNumberInfoRow);
					} catch (Exception e) {
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
				if (jsonObjTWCustomEntryPackagesSets != null
						&& jsonObjTWCustomEntryPackagesSets.length() > 0) {
					//check if jsonObj empty
					boolean isJSONObjEmpty = false;
					JSONArray jsonAryTWCustomEntryPackagesSetsNames = jsonObjTWCustomEntryPackagesSets
							.names();
					String strTWCustomEntryNumber = null;
					
					for (int ith = 0; ith < jsonAryTWCustomEntryPackagesSetsNames.length(); ith++) {
						JSONObject jsonObj = (JSONObject) jsonObjTWCustomEntryPackagesSets
								.get(jsonAryTWCustomEntryPackagesSetsNames.getString(ith));
						if (jsonObj.length() == 0) {
							isJSONObjEmpty = true;
							strTWCustomEntryNumber = jsonAryTWCustomEntryPackagesSetsNames
									.getString(ith);
							break;
						}
					}
					//end
					
					//check if information of size & weight are empty
					boolean isSizeWeightInfoValided = false;
					JSONArray jsonArySizeWeightInfoNames = jsonObjTWCustomEntryPackageSizeWeight.names();
					for (int ith = 0 ; ith < jsonArySizeWeightInfoNames.length() ; ith++){
						JSONObject jsonObj= (JSONObject) jsonObjTWCustomEntryPackageSizeWeight.get(jsonArySizeWeightInfoNames.getString(ith));
						LinearLayout layoutSizeWeight = (LinearLayout) jsonObj.get("layoutObj");
						JSONObject jsonObjSizeWeight = (JSONObject) jsonObj.get("strObj");

						LinearLayout layoutLength = (LinearLayout) layoutSizeWeight.getChildAt(0);
						EditText editLength = (EditText) layoutLength.getChildAt(1);
						LinearLayout layoutWidth = (LinearLayout) layoutSizeWeight.getChildAt(1);
						EditText editWidth = (EditText) layoutWidth.getChildAt(1);
						LinearLayout layoutHeight = (LinearLayout) layoutSizeWeight.getChildAt(2);
						EditText editHeight = (EditText) layoutHeight.getChildAt(1);
						LinearLayout layoutWeight = (LinearLayout) layoutSizeWeight.getChildAt(3);
						EditText editWeight = (EditText) layoutWeight.getChildAt(1);
						try{
							float length = Float.parseFloat(editLength
									.getText().toString());
							jsonObjSizeWeight.put("length", String.valueOf(length));
							isSizeWeightInfoValided = true;
						}
						catch(Exception e){
							editLength.requestFocus();
							Toast.makeText(getActivity(), "Invalid Length", Toast.LENGTH_LONG).show();
							break;
						}
						try{
							float width = Float.parseFloat(editWidth
									.getText().toString());
							jsonObjSizeWeight.put("width", String.valueOf(width));
							isSizeWeightInfoValided = true;
						}
						catch(Exception e){
							editWidth.requestFocus();
							Toast.makeText(getActivity(), "Invalid Width", Toast.LENGTH_LONG).show();
							break;
						}
						try{
							float height = Float.parseFloat(editHeight
									.getText().toString());
							jsonObjSizeWeight.put("height", String.valueOf(height));
							isSizeWeightInfoValided = true;
						}
						catch(Exception e){
							editHeight.requestFocus();
							Toast.makeText(getActivity(), "Invalid Height", Toast.LENGTH_LONG).show();
							break;
						}
						try{
							float weight = Float.parseFloat(editWeight
									.getText().toString());
							jsonObjSizeWeight.put("weight", String.valueOf(weight));
							isSizeWeightInfoValided = true;
						}
						catch(Exception e){
							editWeight.requestFocus();
							Toast.makeText(getActivity(), "Invalid Weight", Toast.LENGTH_LONG).show();
							break;
						}
					}
					
					
					//execute the submission
					if (isJSONObjEmpty) {
						Toast.makeText(
								getActivity(),
								"The Package Set, " + strTWCustomEntryNumber
										+ ", doesn't has any package!",
								Toast.LENGTH_LONG).show();
					} else {
						submitPackagesSetsHandler = new WebContentDownloadHandler(
								progressBarForSubmitPackagesSets);
						submitPackagesSetsHandler
								.execute(new String[] { "https://exwine-tw.appspot.com/exshipper_tw_custom_entry_handler" });
					}
				} else {
					Toast.makeText(getActivity(), "No data for upload!",
							Toast.LENGTH_LONG).show();
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
			mProgressbar.dismiss();
			mProgressbar = null;

			// get result from server
			if (p_result != null) {
				txtDownlaodCustomEntryNumberResult
						.setText("Current Obtained TW Custom Entry NO."
								+ p_result);
				// parse jsonObj to get TW Custom Entry Number...

				//
				if (!"NA".equals(p_result)) {
					JSONObject jsonObjPackagesSet = new JSONObject();
					
					JSONObject jsonObjSetSizeWeight = new JSONObject();
					JSONObject jsonObjStringSizeWeight = new JSONObject(); //save string data
					try {
						jsonObjStringSizeWeight.put("length", "NA");
						jsonObjStringSizeWeight.put("width", "NA");
						jsonObjStringSizeWeight.put("height", "NA");
						jsonObjStringSizeWeight.put("weight", "NA");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.e("error", e.getMessage());
					}
					
					
					try {
						//
						if (jsonObjTWCustomEntryPackagesSets == null) {
							jsonObjTWCustomEntryPackagesSets = new JSONObject();
						}
						
						//
						if(jsonObjTWCustomEntryPackageSizeWeight == null){
							jsonObjTWCustomEntryPackageSizeWeight = new JSONObject();
						}

						if (jsonObjTWCustomEntryPackagesSets.has(p_result)) {
							Toast.makeText(
									getActivity(),
									"Duplicated TW Custom Entry NO. "
											+ p_result, Toast.LENGTH_LONG)
									.show();
						} else {
							//add new package info jsonobj
							jsonObjTWCustomEntryPackagesSets.put(p_result,
									jsonObjPackagesSet);

							// build View
							TextView txtTWCustomEntryNumber = new TextView(
									getActivity());
							txtTWCustomEntryNumber.setTextColor(Color
									.parseColor("#000000"));
							txtTWCustomEntryNumber.setTextSize(
									TypedValue.COMPLEX_UNIT_SP, 16);
							txtTWCustomEntryNumber.setGravity(Gravity.CENTER);
							txtTWCustomEntryNumber.setPadding(3, 2, 1, 2);
							txtTWCustomEntryNumber
									.setText("TW Custom Entry NO. " + p_result);

							//length
							TextView txtLength = new TextView(getActivity());
							txtLength.setText("Length(inch): ");
							txtLength.setLayoutParams(new LinearLayout.LayoutParams(150, 50));
							txtLength.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
							EditText editTxtLength = new EditText(getActivity());
							editTxtLength.setLayoutParams(new LinearLayout.LayoutParams(250, 50));
							editTxtLength.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							LinearLayout layoutLength = new LinearLayout(
									getActivity());
							layoutLength
									.setOrientation(LinearLayout.HORIZONTAL);
							layoutLength.setPadding(1, 2, 1, 2);
							layoutLength.addView(txtLength);
							layoutLength.addView(editTxtLength);

							//width
							TextView txtWidth = new TextView(getActivity());
							txtWidth.setText("Width(inch): ");
							txtWidth.setLayoutParams(new LinearLayout.LayoutParams(150, 50));
							txtWidth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
							EditText editTxtWidth = new EditText(getActivity());
							editTxtWidth.setLayoutParams(new LinearLayout.LayoutParams(250, 50));
							editTxtWidth.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							LinearLayout layoutWidth = new LinearLayout(
									getActivity());
							layoutWidth
							.setOrientation(LinearLayout.HORIZONTAL);
							layoutWidth.setPadding(1, 2, 1, 2);
							layoutWidth.addView(txtWidth);
							layoutWidth.addView(editTxtWidth);
					
							//height
							TextView txtHeight = new TextView(getActivity());
							txtHeight.setText("Height(inch): ");
							txtHeight.setLayoutParams(new LinearLayout.LayoutParams(150, 50));
							txtHeight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
							EditText editTxtHeight = new EditText(getActivity());
							editTxtHeight.setLayoutParams(new LinearLayout.LayoutParams(250, 50));
							editTxtHeight.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							LinearLayout layoutHeight = new LinearLayout(
									getActivity());
							layoutHeight
							.setOrientation(LinearLayout.HORIZONTAL);
							layoutHeight.setPadding(1, 2, 1, 2);
							layoutHeight.addView(txtHeight);
							layoutHeight.addView(editTxtHeight);
							
							//weight
							TextView txtWeight = new TextView(getActivity());
							txtWeight.setText("Weight(kg): ");
							txtWeight.setLayoutParams(new LinearLayout.LayoutParams(150, 50));
							txtWeight.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
							EditText editTxtWeight = new EditText(getActivity());
							editTxtWeight.setLayoutParams(new LinearLayout.LayoutParams(250, 50));
							editTxtWeight.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							LinearLayout layoutWeight = new LinearLayout(
									getActivity());
							layoutWeight
							.setOrientation(LinearLayout.HORIZONTAL);
							layoutWeight.setPadding(1, 2, 1, 2);
							layoutWeight.addView(txtWeight);
							layoutWeight.addView(editTxtWeight);

							//size & weight layout
							LinearLayout layoutSizeWeight = new LinearLayout(getActivity());
							layoutSizeWeight
							.setOrientation(LinearLayout.VERTICAL);
							layoutSizeWeight.setPadding(1, 2, 1, 2);
							layoutSizeWeight.addView(layoutLength);
							layoutSizeWeight.addView(layoutWidth);
							layoutSizeWeight.addView(layoutHeight);
							layoutSizeWeight.addView(layoutWeight);
							
							//preparation for validation of size & weight 
							jsonObjSetSizeWeight.put("layoutObj", layoutSizeWeight);
							jsonObjSetSizeWeight.put("strObj", jsonObjStringSizeWeight);
							jsonObjTWCustomEntryPackageSizeWeight.put(p_result, jsonObjSetSizeWeight);

							String working_on = "";
							//end
							
							// button
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
									.parseColor("#744a0f"));
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
									.addView(btnDeletePackageSet);
							layoutPackageSetBtnsField
									.addView(btnStartBarcodeReader);
							// end

							LinearLayout layoutTrackingNumberList = new LinearLayout(
									getActivity());
							layoutTrackingNumberList
									.setOrientation(LinearLayout.VERTICAL);
							layoutTrackingNumberList.setPadding(1, 2, 1, 2);

							// psrting line
							View viewPartingLine = new View(getActivity());
							viewPartingLine
									.setLayoutParams(new FrameLayout.LayoutParams(
											LayoutParams.MATCH_PARENT, 3));
							viewPartingLine.setBackgroundColor(Color
									.parseColor("#ffffff"));

							// package layout
							LinearLayout layoutPackagesSet = new LinearLayout(
									getActivity());
							layoutPackagesSet
									.setOrientation(LinearLayout.VERTICAL);
							layoutPackagesSet.setBackgroundColor(Color
									.parseColor("#e1e1e1"));
							layoutPackagesSet.setPadding(1, 2, 1, 2);
							layoutPackagesSet.addView(txtTWCustomEntryNumber);
							layoutPackagesSet.addView(layoutSizeWeight);
							layoutPackagesSet
									.addView(layoutPackageSetBtnsField);
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
			nameValuePairs.add(new BasicNameValuePair("token",
					"tw_custom_entry_handler_get_number"));

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
			nameValuePairs.add(new BasicNameValuePair("token",
					"tw_custom_entry_handler_submit_packages_sets"));

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
