package fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.exshipper.handlers.WebContentDownloadHandler;
import com.exshipper.listeners.ProgressBarUpdateListener;
import com.exshipper.prjexshipperbarcodereader.R;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TWCustomEntryPackagesFragment extends FragmentTemplate {

	JSONObject jsonObjTWCustomEntryPackagesSets = null;

	WebContentDownloadHandler getTWCustomEntryNumberHandler = null;
	WebContentDownloadHandler submitPackagesSetsHandler = null;
	ProgressDialog mProgressbar = null;

	View triggerBtn = null;

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
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
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
				Object obj= triggerBtn.getTag();
				if( obj != null && obj instanceof TextView){
					TextView currentView = (TextView) obj;
					currentView.setText(p_result);
					
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
		
		txtDownlaodCustomEntryNumberResult = (TextView) mView.findViewById(R.id.txt_download_tw_custom_entry_number_result);

		txtTotalSets = (TextView) mView
				.findViewById(R.id.txt_total_sets_tw_custom_entry);

		txtSubmitResult = (TextView) mView
				.findViewById(R.id.txt_submit_result_tw_custom_entry);
		btnSubmitPakcagesSets = (Button) mView
				.findViewById(R.id.btn_submit_packages_sets_tw_custom_entry);
		btnSubmitPakcagesSets.setOnClickListener(submitPackagesSets);
	}

}
