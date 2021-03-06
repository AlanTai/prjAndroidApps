package fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.exshipper.generalhandlers.DataExchangeHandler;
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
import android.widget.ScrollView;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TWCustomEntryPackagesFragment extends FragmentTemplate {
	private JSONObject jsonObjTWCustomEntryPackagesSets = null;
	private JSONObject jsonObjTWCustomEntryPackageSizeWeight = null;

	private String strScanedBarcode = null;

	private DataExchangeHandler getTWCustomEntryNumberHandler = null;
	private DataExchangeHandler submitPackagesSetsHandler = null;
	private ProgressDialog mProgressbar = null;

	private Button currentBtnForScanBarcode = null;

	private View viewDeleteBtn = null;
	private AlertDialog.Builder alertDialogBuilder = null;
	private View viewForScrollTo = null;

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

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent p_intent) {
		// TODO Auto-generated method stub
		if (requestCode == 0) {
			getActivity();
			if (resultCode == FragmentActivity.RESULT_OK) {
				strScanedBarcode = p_intent.getStringExtra("SCAN_RESULT");
				addNewSUDATrackingNumber();
			}
		}
	}
	
	//functions block
	private void addNewSUDATrackingNumber(){
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
										
										
										//auto-scroll to specific view
										viewForScrollTo = layoutSUDATrackingNumberInfoRow;
										//auto-scroll to specific view
										scrollViewTWCustomEntryList.post(new Runnable() {
											
											@Override
											public void run() {
												// TODO Auto-generated method stub
												scrollViewTWCustomEntryList.scrollTo(0, viewForScrollTo.getTop());
											}
										});
										
										
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
	//end of functions block
	
	
	// OnClickListeners
	//get tw custom entry number & add new package set
	OnClickListener getTWCustomEntryNumberAndAddNewSet = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(getActivity());
			}
			DialogInterface.OnClickListener currentDialogListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					// TODO Auto-generated method stub

					if (!"".equals(txtSubmitResult.getText())) {
						txtSubmitResult.setText("");
					}

					try {
						if (getTWCustomEntryNumberHandler != null
								&& getTWCustomEntryNumberHandler.getStatus() != AsyncTask.Status.FINISHED) {
							getTWCustomEntryNumberHandler.cancel(true);
						}

						//
						getTWCustomEntryNumberHandler = new DataExchangeHandler(
								progressBarForGetTWCustomEntryNumber);
						getTWCustomEntryNumberHandler
								.execute(new String[] { "https://winever-test.appspot.com/exshipper_tw_custom_entry_handler" });
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getActivity(),
								"Fail to get response from server!", Toast.LENGTH_SHORT)
								.show();
						Log.e("error", e.getMessage());
					}
				}
			};
			
			alertDialogBuilder.setTitle("Download TW Custom Entry Tracking Number");
			alertDialogBuilder
					.setMessage(
							"Do you want to download TW Custom Entry tracking number?")
					.setCancelable(false)
					.setPositiveButton("Yes", currentDialogListener
							).setNegativeButton("No", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});
			
			AlertDialog currentAlertDialog = alertDialogBuilder.create();
			currentAlertDialog.show();
		}
	};

	//delete package set
	OnClickListener deletePackagesSet = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewDeleteBtn = v;
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(getActivity());
			}
			;
			DialogInterface.OnClickListener currentDialogListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					if (viewDeleteBtn instanceof Button) {
						JSONObject jsonObjFromBtnScanBarcode = (JSONObject) viewDeleteBtn
								.getTag();
						try {
							String twCustomEntryNumber = (String) jsonObjFromBtnScanBarcode
									.get("p_result");
							LinearLayout layoutCurrentPackageSet = (LinearLayout) jsonObjFromBtnScanBarcode
									.get("layoutPackagesSets");

							txtTWCustomEntryHandlingResult
									.setText("Current Deleted TW Custom Entry NO."
											+ twCustomEntryNumber);

							jsonObjTWCustomEntryPackagesSets
									.remove(twCustomEntryNumber);
							jsonObjTWCustomEntryPackageSizeWeight
									.remove(twCustomEntryNumber);
							layoutCurrentPackageSet.removeAllViews();
							layoutPakcagesSetsList
									.removeView(layoutCurrentPackageSet);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							Log.e("error", e.getMessage());
						}

					}

				}
			};
			alertDialogBuilder
					.setTitle("Delete Package Set")
					.setMessage("Do you want to delete the package set?")
					.setCancelable(false)
					.setPositiveButton("Yes", currentDialogListener)
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});

			AlertDialog currentAlertDialog = alertDialogBuilder.create();
			currentAlertDialog.show();
		}
	};

	//start barcode reader/scanner
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

	//delete suda tracking number
	OnClickListener deleteSUDATrackingNumberInfo = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewDeleteBtn = v;
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(getActivity());
			}
			;
			DialogInterface.OnClickListener currentDialogListener = new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					if (viewDeleteBtn instanceof Button) {
						Button btn = (Button) viewDeleteBtn;
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

								jsonObjCurrentPackagesSet
										.remove(strScanedBarcode);
								layoutCurrentPackagesSet
										.removeView(layoutSUDATrackingNumberInfoRow);
							} catch (Exception e) {
								Log.e("error", e.getMessage());
							}

						}
					}

				}
			};
			alertDialogBuilder
					.setTitle("Delete SUDA Tracking Number")
					.setMessage(
							"Do you want to delete the SUDA tracking number?")
					.setCancelable(false)
					.setPositiveButton("Yes", currentDialogListener)
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});

			AlertDialog currentAlertDialog = alertDialogBuilder.create();
			currentAlertDialog.show();

		}
	};

	//submit packages information
	OnClickListener submitPackagesSets = new OnClickListener() {

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(getActivity());
			}
			DialogInterface.OnClickListener currentDialogListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub

					try {
						if (submitPackagesSetsHandler != null
								&& submitPackagesSetsHandler.getStatus() != AsyncTask.Status.FINISHED) {
							submitPackagesSetsHandler.cancel(true);
						}

						// set asynctask
						if (jsonObjTWCustomEntryPackagesSets != null
								&& jsonObjTWCustomEntryPackagesSets.length() > 0) {
							// check if jsonObj empty
							boolean isJSONObjEmpty = false;
							JSONArray jsonAryTWCustomEntryPackagesSetsNames = jsonObjTWCustomEntryPackagesSets
									.names();
							String strTWCustomEntryNumber = null;

							for (int ith = 0; ith < jsonAryTWCustomEntryPackagesSetsNames
									.length(); ith++) {
								JSONObject jsonObj = (JSONObject) jsonObjTWCustomEntryPackagesSets
										.get(jsonAryTWCustomEntryPackagesSetsNames
												.getString(ith));
								if (jsonObj.length() == 0) {
									isJSONObjEmpty = true;
									strTWCustomEntryNumber = jsonAryTWCustomEntryPackagesSetsNames
											.getString(ith);
									break;
								}
							}
							// end

							// check if information of size & weight are empty
							boolean isSizeWeightInfoValid = false;
							JSONArray jsonArySizeWeightInfoNames = jsonObjTWCustomEntryPackageSizeWeight
									.names();
							for (int ith = 0; ith < jsonArySizeWeightInfoNames
									.length(); ith++) {
								JSONObject jsonObj = (JSONObject) jsonObjTWCustomEntryPackageSizeWeight
										.get(jsonArySizeWeightInfoNames
												.getString(ith));
								LinearLayout layoutSizeWeight = (LinearLayout) jsonObj
										.get("layoutObj");
								JSONObject jsonObjSizeWeight = (JSONObject) jsonObj
										.get("strObj");

								LinearLayout layoutLength = (LinearLayout) layoutSizeWeight
										.getChildAt(0);
								EditText editLength = (EditText) layoutLength
										.getChildAt(1);
								LinearLayout layoutWidth = (LinearLayout) layoutSizeWeight
										.getChildAt(1);
								EditText editWidth = (EditText) layoutWidth
										.getChildAt(1);
								LinearLayout layoutHeight = (LinearLayout) layoutSizeWeight
										.getChildAt(2);
								EditText editHeight = (EditText) layoutHeight
										.getChildAt(1);
								LinearLayout layoutWeight = (LinearLayout) layoutSizeWeight
										.getChildAt(3);
								EditText editWeight = (EditText) layoutWeight
										.getChildAt(1);
								try {
									float length = Float.parseFloat(editLength
											.getText().toString());
									jsonObjSizeWeight.put("length", length);
									isSizeWeightInfoValid = true;
								} catch (Exception e) {
									isSizeWeightInfoValid = false;
									editLength.requestFocus();
									Toast.makeText(getActivity(),
											"Invalid Length", Toast.LENGTH_LONG)
											.show();
									break;
								}
								try {
									float width = Float.parseFloat(editWidth
											.getText().toString());
									jsonObjSizeWeight.put("width", width);
									isSizeWeightInfoValid = true;
								} catch (Exception e) {
									isSizeWeightInfoValid = false;
									editWidth.requestFocus();
									Toast.makeText(getActivity(),
											"Invalid Width", Toast.LENGTH_LONG)
											.show();
									break;
								}
								try {
									float height = Float.parseFloat(editHeight
											.getText().toString());
									jsonObjSizeWeight.put("height", height);
									isSizeWeightInfoValid = true;
								} catch (Exception e) {
									isSizeWeightInfoValid = false;
									editHeight.requestFocus();
									Toast.makeText(getActivity(),
											"Invalid Height", Toast.LENGTH_LONG)
											.show();
									break;
								}
								try {
									float weight = Float.parseFloat(editWeight
											.getText().toString());
									jsonObjSizeWeight.put("weight", weight);
									isSizeWeightInfoValid = true;
								} catch (Exception e) {
									isSizeWeightInfoValid = false;
									editWeight.requestFocus();
									Toast.makeText(getActivity(),
											"Invalid Weight", Toast.LENGTH_LONG)
											.show();
									break;
								}
							}

							// execute the submission
							if (isJSONObjEmpty || !isSizeWeightInfoValid) {
								Toast.makeText(
										getActivity(),
										"The Package Set, "
												+ strTWCustomEntryNumber
												+ ", doesn't has any package! Or, there are invalid information of package size or weight!",
										Toast.LENGTH_LONG).show();
							} else {
								//disable button of submitting packages info
								btnSubmitPakcagesSets.setEnabled(false);
								
								//execute submit event
								submitPackagesSetsHandler = new DataExchangeHandler(
										progressBarForSubmitPackagesSets);
								submitPackagesSetsHandler
										.execute(new String[] { "https://winever-test.appspot.com/exshipper_tw_custom_entry_handler" });
							}
							
						} else {
							Toast.makeText(getActivity(),
									"No data for upload!", Toast.LENGTH_LONG)
									.show();
						}

					} catch (Exception e) {
						// TODO: handle exception
						Log.e("error", e.getMessage());
						btnSubmitPakcagesSets.setEnabled(true);
					}

				}
			};

			alertDialogBuilder.setTitle("Submit Pakcages\' Information");
			alertDialogBuilder
					.setMessage("Do you want to submit the information?")
					.setCancelable(false)
					.setPositiveButton("Yes", currentDialogListener)
					.setNegativeButton("No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub
									dialog.cancel();
								}
							});

			AlertDialog currentAlertDialog = alertDialogBuilder.create();
			currentAlertDialog.show();

		}
	};
	//end of OnClickListeners
	
	
	// progress bars block
	// get tw custom entry number & add new package set
	ProgressBarUpdateListener progressBarForGetTWCustomEntryNumber = new ProgressBarUpdateListener() {

		@Override
		public void updateResult(String p_result) {
			// TODO Auto-generated method stub
			mProgressbar.dismiss();
			mProgressbar = null;

			// get result from server
			if (p_result != null) {
				txtTWCustomEntryHandlingResult
						.setText("Current Obtained TW Custom Entry NO."
								+ p_result);
				// parse jsonObj to get TW Custom Entry Number...

				//
				if (!"NA".equals(p_result)) {
					JSONObject jsonObjPackagesSet = new JSONObject();

					JSONObject jsonObjSetSizeWeight = new JSONObject();
					JSONObject jsonObjStringSizeWeight = new JSONObject(); // save
																			// string
																			// data
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
						if (jsonObjTWCustomEntryPackageSizeWeight == null) {
							jsonObjTWCustomEntryPackageSizeWeight = new JSONObject();
						}

						if (jsonObjTWCustomEntryPackagesSets.has(p_result)) {
							Toast.makeText(
									getActivity(),
									"Duplicated TW Custom Entry NO. "
											+ p_result, Toast.LENGTH_LONG)
									.show();
						} else {
							// add new package info jsonobj
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

							// length
							TextView txtLength = new TextView(getActivity());
							txtLength.setText("Length(inch): ");
							txtLength
									.setLayoutParams(new LinearLayout.LayoutParams(
											150, 50));
							txtLength.setTextSize(TypedValue.COMPLEX_UNIT_SP,
									14);
							txtLength.setGravity(Gravity.CENTER);
							EditText editTxtLength = new EditText(getActivity());
							editTxtLength
									.setLayoutParams(new LinearLayout.LayoutParams(
											250, 50));
							editTxtLength
									.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							editTxtLength.setGravity(Gravity.CENTER);
							LinearLayout layoutLength = new LinearLayout(
									getActivity());
							layoutLength
									.setOrientation(LinearLayout.HORIZONTAL);
							layoutLength.setPadding(1, 2, 1, 2);
							layoutLength.addView(txtLength);
							layoutLength.addView(editTxtLength);

							// width
							TextView txtWidth = new TextView(getActivity());
							txtWidth.setText("Width(inch): ");
							txtWidth.setLayoutParams(new LinearLayout.LayoutParams(
									150, 50));
							txtWidth.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
							txtWidth.setGravity(Gravity.CENTER);
							EditText editTxtWidth = new EditText(getActivity());
							editTxtWidth
									.setLayoutParams(new LinearLayout.LayoutParams(
											250, 50));
							editTxtWidth
									.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							editTxtWidth.setGravity(Gravity.CENTER);
							LinearLayout layoutWidth = new LinearLayout(
									getActivity());
							layoutWidth.setOrientation(LinearLayout.HORIZONTAL);
							layoutWidth.setPadding(1, 2, 1, 2);
							layoutWidth.addView(txtWidth);
							layoutWidth.addView(editTxtWidth);

							// height
							TextView txtHeight = new TextView(getActivity());
							txtHeight.setText("Height(inch): ");
							txtHeight
									.setLayoutParams(new LinearLayout.LayoutParams(
											150, 50));
							txtHeight.setTextSize(TypedValue.COMPLEX_UNIT_SP,
									14);
							txtHeight.setGravity(Gravity.CENTER);
							EditText editTxtHeight = new EditText(getActivity());
							editTxtHeight
									.setLayoutParams(new LinearLayout.LayoutParams(
											250, 50));
							editTxtHeight
									.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							editTxtHeight.setGravity(Gravity.CENTER);
							LinearLayout layoutHeight = new LinearLayout(
									getActivity());
							layoutHeight
									.setOrientation(LinearLayout.HORIZONTAL);
							layoutHeight.setPadding(1, 2, 1, 2);
							layoutHeight.addView(txtHeight);
							layoutHeight.addView(editTxtHeight);

							// weight
							TextView txtWeight = new TextView(getActivity());
							txtWeight.setText("Weight(kg): ");
							txtWeight
									.setLayoutParams(new LinearLayout.LayoutParams(
											150, 50));
							txtWeight.setTextSize(TypedValue.COMPLEX_UNIT_SP,
									14);
							txtWeight.setGravity(Gravity.CENTER);
							EditText editTxtWeight = new EditText(getActivity());
							editTxtWeight
									.setLayoutParams(new LinearLayout.LayoutParams(
											250, 50));
							editTxtWeight
									.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
							editTxtWeight.setGravity(Gravity.CENTER);
							LinearLayout layoutWeight = new LinearLayout(
									getActivity());
							layoutWeight
									.setOrientation(LinearLayout.HORIZONTAL);
							layoutWeight.setPadding(1, 2, 1, 2);
							layoutWeight.addView(txtWeight);
							layoutWeight.addView(editTxtWeight);

							// size & weight layout
							LinearLayout layoutSizeWeight = new LinearLayout(
									getActivity());
							layoutSizeWeight
									.setOrientation(LinearLayout.VERTICAL);
							layoutSizeWeight.setPadding(1, 2, 1, 2);
							layoutSizeWeight.addView(layoutLength);
							layoutSizeWeight.addView(layoutWidth);
							layoutSizeWeight.addView(layoutHeight);
							layoutSizeWeight.addView(layoutWeight);

							// preparation for validation of size & weight
							jsonObjSetSizeWeight.put("layoutObj",
									layoutSizeWeight);
							jsonObjSetSizeWeight.put("strObj",
									jsonObjStringSizeWeight);
							jsonObjTWCustomEntryPackageSizeWeight.put(p_result,
									jsonObjSetSizeWeight);

							// end

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
									.setBackgroundResource(R.drawable.clicked_item);
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
									.setBackgroundResource(R.drawable.clicked_item);
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
							
							viewForScrollTo = layoutPackagesSet;
							//auto-scroll to specific view
							scrollViewTWCustomEntryList.post(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									scrollViewTWCustomEntryList.scrollTo(0, viewForScrollTo.getTop());
								}
							});
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

	// submit packages information
	ProgressBarUpdateListener progressBarForSubmitPackagesSets = new ProgressBarUpdateListener() {
		@Override
		public void updateResult(String p_result) {
			//dismiss progress dialog
			mProgressbar.dismiss();
			mProgressbar = null;
			
			//obtain response from server and show message on the screen
			if (p_result != null) {
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(p_result);
					String key = jsonObj.getString("key");
					String result = jsonObj.getString("result");
					if ("success".equals(key)) {
						layoutPakcagesSetsList.removeAllViews();
						jsonObjTWCustomEntryPackagesSets = null;
						jsonObjTWCustomEntryPackageSizeWeight = null;
						txtTWCustomEntryHandlingResult
								.setText("Successfully Submitted the Data");
						txtSubmitResult.setText(result); // get response from
															// server
					} else {
						txtSubmitResult.setText(result); // get response from
															// server
					}
				} catch (Exception e) {
					Log.e("error", e.getMessage());
				}
			}
			
			//enable button of submitting packages info
			btnSubmitPakcagesSets.setEnabled(true);
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

			if (jsonObjTWCustomEntryPackagesSets != null
					&& jsonObjTWCustomEntryPackageSizeWeight != null) {
				nameValuePairs.add(new BasicNameValuePair(
						"tw_custom_entry_packages_sets",
						jsonObjTWCustomEntryPackagesSets.toString()));
				nameValuePairs.add(new BasicNameValuePair(
						"tw_custom_entry_packages_size_weight",
						jsonObjTWCustomEntryPackageSizeWeight.toString()));
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
	// end of progress bars block
	
	/* XML components */
	TextView txtIntroduction = null;
	Button btnAddNewPackagesSet = null;
	TextView txtTWCustomEntryHandlingResult = null;
	TextView txtTotalSets = null;

	LinearLayout layoutPakcagesSetsList = null;

	TextView txtSubmitResult = null;
	Button btnSubmitPakcagesSets = null;
	ScrollView scrollViewTWCustomEntryList = null;

	private void initXMLViewComponents(View mView) {
		txtIntroduction = (TextView) mView
				.findViewById(R.id.txt_introduction_tw_custom_entry);
		txtIntroduction
				.setText("Demo Version");

		btnAddNewPackagesSet = (Button) mView
				.findViewById(R.id.btn_add_new_packages_set_tw_custom_entry);
		btnAddNewPackagesSet
				.setOnClickListener(getTWCustomEntryNumberAndAddNewSet);

		txtTWCustomEntryHandlingResult = (TextView) mView
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
		
		scrollViewTWCustomEntryList = (ScrollView) mView.findViewById(R.id.scroll_view_tw_custom_entry_list);
	}

}