package fragments;

import java.util.List;

import org.apache.http.NameValuePair;

import com.exshipper.generalhandlers.DataExchangeHandler;
import com.exshipper.listeners.ProgressBarUpdateListener;
import com.exshipper.prjexshipperbarcodereader.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class GeneralClientsPackagesPickupHandler extends FragmentTemplate{
	//inner variables
	private DataExchangeHandler uploadSUDATrackingNumbersHandler = null;
	private ProgressDialog mProgressBarDialog = null;
	private AlertDialog.Builder alertDialogBuilder = null;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

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
		View mView = inflater.inflate(R.layout.general_clients_fragment, container, false);
		initXMLViewComponents(mView);
		return mView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	//progress bar dialog listener
	ProgressBarUpdateListener updateProgresBar = new ProgressBarUpdateListener() {
		
		@Override
		public void updateResult(String p_result) {
			// TODO Auto-generated method stub
			if(mProgressBarDialog == null){
				mProgressBarDialog = new ProgressDialog(getActivity());
				mProgressBarDialog.setCancelable(false);
				mProgressBarDialog.setMessage("Uploading data...");
				mProgressBarDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressBarDialog.show();
			}
		}
		
		@Override
		public void updateProgressBar(int p_progress) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void setupProgressBar() {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public List<NameValuePair> setAuthorizationInfo() {
			// TODO Auto-generated method stub
			return null;
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

	
	/* XML components*/
	TextView txtAppIntroduction = null;
	Button btnSubmitSUDATrackingNumbers = null;
	
	Button btnAddSUDATrackingNumbers = null;
	EditText editTxtSUDATrackingNumber = null;
	
	Button btnScan = null;
	
	TextView txtScanResult = null;
	TextView txtTotalamount = null;
	TextView txtSubmitResult = null;
	
	ScrollView scrollViewGeneralclientsPickupList = null;
	LinearLayout layoutSUDATrackingNumbersList = null;
	
	//initiate XML components
	private void initXMLViewComponents(View argView){
		txtAppIntroduction = (TextView) argView.findViewById(R.id.txt_general_clients_packages_pickup_handler_introduction);
		btnSubmitSUDATrackingNumbers = (Button) argView.findViewById(R.id.btn_general_clients_packages_pickup_handler_submit_suda_tracking_numbers);
		
		btnAddSUDATrackingNumbers = (Button) argView.findViewById(R.id.btn_general_clients_packages_pickup_handler_add_suda_tracking_number);
		editTxtSUDATrackingNumber = (EditText) argView.findViewById(R.id.edit_txt_general_clients_suda_tracking_number);
		
		btnScan = (Button) argView.findViewById(R.id.btn_general_clients_packages_pickup_handler_scan);
		
		txtAppIntroduction = (TextView) argView.findViewById(R.id.txt_general_clients_packages_pickup_handler_introduction);
		txtTotalamount = (TextView) argView.findViewById(R.id.txt_general_clients_packages_pickup_handler_total_amount);
		txtSubmitResult = (TextView) argView.findViewById(R.id.txt_general_clients_packages_pickup_handler_submit_result);
		
		scrollViewGeneralclientsPickupList = (ScrollView) argView.findViewById(R.id.scroll_view_general_clients_packages_pickup_handler_content_list);
		layoutSUDATrackingNumbersList = (LinearLayout) argView.findViewById(R.id.layout_general_clients_packages_pickup_handler_suda_tracking_numbers_list);
	}
}
