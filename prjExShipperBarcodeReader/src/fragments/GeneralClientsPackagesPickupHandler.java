package fragments;

import com.exshipper.generalhandlers.DataExchangeHandler;
import com.exshipper.prjexshipperbarcodereader.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GeneralClientsPackagesPickupHandler extends FragmentTemplate{
	//inner variables
	private DataExchangeHandler uploadSUDATrackingNumbersHandler = null;
	private ProgressDialog mProgressBar = null;
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
		return mView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
