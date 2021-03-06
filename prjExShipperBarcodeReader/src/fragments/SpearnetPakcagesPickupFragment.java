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
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class SpearnetPakcagesPickupFragment extends FragmentTemplate {
	// inner variables
	private String strSUDATrackingNumber = null;
	private String scanResult = "";
	private Map<Integer, String> sudaTrackingNumberMap = new HashMap<Integer, String>();

	private JSONObject jsonObjSUDATrackingNumbers = null;
	private JSONArray jsonArySUDATrackingNumberList = null;

	private DataExchangeHandler uploadPickedPackagesTrackingNumbersHandler = null;
	private ProgressDialog mProgressBarDialog = null;

	private View viewDeleteBtn = null;
	private AlertDialog.Builder alertDialogBuilder = null;
	private View viewForScrollTo = null;

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
		mProgressBarDialog = null;
		layoutSUDATrackingNumbersList = null;
		super.onDestroy();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent p_intent) {
		if (requestCode == 0) {
			getActivity();
			if (resultCode == FragmentActivity.RESULT_OK) {
				strSUDATrackingNumber = p_intent.getStringExtra("SCAN_RESULT");
				addNewSUDATrackingNumber();
			} else {
				getActivity();
				if (resultCode == FragmentActivity.RESULT_CANCELED) {
					Log.e("APP", "Scan Unsuccessfully");
				}
			}
		}
	}
	
	//functions block
	private void addNewSUDATrackingNumber(){
		//add new suda tracking number
		// alert dialog
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setTitle("SUDA Tracking Number");
		alertDialogBuilder
				.setMessage(
						"Do you want to save the SUDA NO. "
								+ strSUDATrackingNumber + "?")
				.setCancelable(false)
				.setPositiveButton("Yes",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if (!sudaTrackingNumberMap
										.containsValue(strSUDATrackingNumber)) {
									// add new suda tracking number into
									// MapList
									sudaTrackingNumberMap.put(
											strSUDATrackingNumber
													.hashCode(),
											strSUDATrackingNumber);
									scanResult = "*Current Obtained SUDA NO. "
											+ strSUDATrackingNumber;

									txtScanResult.setText(scanResult);
									txtTotalAmount
											.setText("*Total Amount of SUDA NO. = "
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
											.setText("SUDA NO."
													+ strSUDATrackingNumber);
									txtAddedSUDATrackingNumber
											.setGravity(Gravity.CENTER);
									txtAddedSUDATrackingNumber
											.setPadding(3, 2, 1, 2);

									Button btnDeleteSUDATrackingNumber = new Button(
											getActivity());
									btnDeleteSUDATrackingNumber
											.setTextSize(
													TypedValue.COMPLEX_UNIT_SP,
													16);
									btnDeleteSUDATrackingNumber.setTextColor(Color
											.parseColor("#ff0000"));
									btnDeleteSUDATrackingNumber
											.setText("Delete NO.");
									btnDeleteSUDATrackingNumber
											.setBackgroundColor(Color.TRANSPARENT);
									btnDeleteSUDATrackingNumber
											.setGravity(Gravity.CENTER);
									btnDeleteSUDATrackingNumber
											.setBackgroundResource(R.drawable.clicked_item);
									btnDeleteSUDATrackingNumber
											.setPadding(3, 2, 1, 2);
									btnDeleteSUDATrackingNumber
											.setOnClickListener(deleteSUDATrackingNumberInfo);

									LinearLayout layoutSUDATrackingNumberInfoRow = new LinearLayout(
											getActivity());
									layoutSUDATrackingNumberInfoRow
											.setOrientation(LinearLayout.HORIZONTAL);
									layoutSUDATrackingNumberInfoRow
											.setPadding(1, 2, 1, 2);
									layoutSUDATrackingNumberInfoRow
											.addView(btnDeleteSUDATrackingNumber);
									layoutSUDATrackingNumberInfoRow
											.addView(txtAddedSUDATrackingNumber);
									layoutSUDATrackingNumberInfoRow
											.setTag(strSUDATrackingNumber
													.hashCode());

									btnDeleteSUDATrackingNumber
											.setTag(layoutSUDATrackingNumberInfoRow);
									layoutSUDATrackingNumbersList
											.addView(layoutSUDATrackingNumberInfoRow);
									
									
									//auto-scroll to specific view
									viewForScrollTo = layoutSUDATrackingNumberInfoRow;
									//auto-scroll to specific view
									scrollViewSpearnetPickupList.post(new Runnable() {
										
										@Override
										public void run() {
											// TODO Auto-generated method stub
											scrollViewSpearnetPickupList.scrollTo(0, viewForScrollTo.getTop());
										}
									});
								} else {
									Toast.makeText(getActivity(),
											"SUDA NO. Duplicated!",
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
	//end of functions block
	
	// OnClickListeners
	// start barcode scan action
	OnClickListener startBarcodeReader = new OnClickListener() {

		@Override
		public void onClick(View v) {
			if(!"".equals(txtSubmitResult.getText())){
				txtSubmitResult.setText("");
			}
			
			Intent myIntent = new Intent("com.google.zxing.client.android.SCAN");
			myIntent.putExtra("SCAN_MODE", "ONE_D_MODE");
			startActivityForResult(myIntent, 0);

		}
	};

	// delete suda tracking number
	OnClickListener deleteSUDATrackingNumberInfo = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			viewDeleteBtn = v;
			if (alertDialogBuilder == null) {
				alertDialogBuilder = new AlertDialog.Builder(getActivity());
			}
			DialogInterface.OnClickListener currentDialogListener = new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog,
						int which) {
					// TODO Auto-generated method stub
					if (viewDeleteBtn instanceof Button) {
						Button btn = (Button) viewDeleteBtn;
						if (btn.getTag() instanceof LinearLayout) {
							LinearLayout tagLayout = (LinearLayout) btn.getTag();
							if (tagLayout.getTag() instanceof Integer) {
								Integer tagKey = (Integer) tagLayout.getTag();
								String value = sudaTrackingNumberMap.get(tagKey);
								String msg = "*Current Deleted SUDA NO." + value;
								txtScanResult.setText(msg);
								sudaTrackingNumberMap.remove(tagKey);
								layoutSUDATrackingNumbersList.removeView(tagLayout);
								txtTotalAmount
										.setText("*Total Amount of SUDA NO. ="
												+ sudaTrackingNumberMap.size());
							}
						}
					}
				}
			};
			
			alertDialogBuilder.setTitle("Delete SUDA Tracking Number");
			alertDialogBuilder
					.setMessage(
							"Do you want to delete the SUDA tracking number?")
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
	
	//manually add suda tracking number
	OnClickListener addSUDATrackingNumber = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			strSUDATrackingNumber = editTxtSUDATrackingNumber.getText().toString();
			if(!"".equals(strSUDATrackingNumber)){
				addNewSUDATrackingNumber();
			}
			else{
				Toast.makeText(getActivity(), "Please key in a SUDA tracking number!", Toast.LENGTH_LONG).show();
			}
		}
	};
	
	//submit package information
	OnClickListener submitSUDATrackingNumbers = new OnClickListener() {
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

					if (sudaTrackingNumberMap.size() != 0) {
						//disable button
						btnSubmitSUDATrackingNumbers.setEnabled(false);
						
						//build data object & array
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
									&& (uploadPickedPackagesTrackingNumbersHandler
											.getStatus() != AsyncTask.Status.FINISHED)) {
								uploadPickedPackagesTrackingNumbersHandler.cancel(true);
							}
							uploadPickedPackagesTrackingNumbersHandler = new DataExchangeHandler(
									updateProgressBar);
							uploadPickedPackagesTrackingNumbersHandler
									.execute(new String[] { "https://winever-test.appspot.com/exshipper_spearnet_packages_pickup_handler" });
						} catch (JSONException e) {
							Log.e("error", e.getMessage());
							btnSubmitSUDATrackingNumbers.setEnabled(true);
						}
					} else {
						Toast.makeText(getActivity(), "No data for upload!",
								Toast.LENGTH_LONG).show();
					}
				}
			};
			
			alertDialogBuilder.setTitle("Submit Pickup Pakcages\' Information");
			alertDialogBuilder
					.setMessage(
							"Do you want to submit the information?")
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
	// end of OnClickListeners

	// self-defined listeners
	ProgressBarUpdateListener updateProgressBar = new ProgressBarUpdateListener() {

		@Override
		public void setupProgressBar() {
			// progress bar dialog
			if (mProgressBarDialog == null) {
				mProgressBarDialog = new ProgressDialog(getActivity());
				mProgressBarDialog.setCancelable(false);
				mProgressBarDialog.setMessage("Uploading Data...");
				mProgressBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressBarDialog.show();
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
			mProgressBarDialog.setProgress(p_progress);
		}

		@Override
		public void updateResult(String p_result) {
			// progress dialog...
			mProgressBarDialog.dismiss();
			mProgressBarDialog = null;

			// get result from server
			if (p_result != null) {
				JSONObject jsonObj = null;
				try {
					jsonObj = new JSONObject(p_result);
					String key = jsonObj.getString("key");
					String result = jsonObj.getString("result");
					if ("success".equals(key)) {
						sudaTrackingNumberMap.clear();
						jsonArySUDATrackingNumberList = null;
						jsonObjSUDATrackingNumbers = null;
						layoutSUDATrackingNumbersList.removeAllViews();
						txtSubmitResult.setText(result);
						txtScanResult.setText("");
						txtTotalAmount.setText("");
					} else {
						txtSubmitResult.setText(result);
					}
				} catch (Exception e) {
					Log.e("UpdateResult error", e.getMessage());
				}

			}
			
			//enable submit button
			btnSubmitSUDATrackingNumbers.setEnabled(true);
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

			if (jsonObjSUDATrackingNumbers != null) {
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
	Button btnSubmitSUDATrackingNumbers = null;
	Button btnAddSUDATrackingNumber = null;
	EditText editTxtSUDATrackingNumber = null;
	Button btnScan = null;

	TextView txtScanResult = null;
	TextView txtTotalAmount = null;
	TextView txtSubmitResult = null;
	
	ScrollView scrollViewSpearnetPickupList = null;
	LinearLayout layoutSUDATrackingNumbersList = null;

	// init function
	private void initXMLViewComponents(View argView) {
		txtAppIntroduction = (TextView) argView
				.findViewById(R.id.txt_introduction);
		txtAppIntroduction
				.setText("Demo Version");
		btnScan = (Button) argView.findViewById(R.id.btn_scan);
		btnScan.setOnClickListener(startBarcodeReader);
		txtScanResult = (TextView) argView.findViewById(R.id.txt_result);

		txtTotalAmount = (TextView) argView.findViewById(R.id.txt_total_amount);

		btnSubmitSUDATrackingNumbers = (Button) argView
				.findViewById(R.id.btn_submit_suda_tracking_number);
		btnSubmitSUDATrackingNumbers
				.setOnClickListener(submitSUDATrackingNumbers);

		layoutSUDATrackingNumbersList = (LinearLayout) argView
				.findViewById(R.id.layout_suda_tracking_numbers_list);
		txtSubmitResult = (TextView) argView.findViewById(R.id.txt_submit_result);
		
		scrollViewSpearnetPickupList = (ScrollView) argView.findViewById(R.id.scroll_view_spearnet_pickup_list);
		btnAddSUDATrackingNumber = (Button) argView.findViewById(R.id.btn_add_suda_tracking_number);
		btnAddSUDATrackingNumber.setOnClickListener(addSUDATrackingNumber);
		editTxtSUDATrackingNumber = (EditText) argView.findViewById(R.id.edit_txt_suda_tracking_number);
	}
	/* end of XML view components init function */

}