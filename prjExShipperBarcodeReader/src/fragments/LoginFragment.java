package fragments;

import com.exshipper.prjexshipperbarcodereader.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class LoginFragment extends FragmentTemplate{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		setHasOptionsMenu(false);
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View mView = inflater.inflate(R.layout.login_fragment, container, false);
		initXMLcomponents(mView);
		return mView;
	}
	
	OnClickListener submitClick = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if("alan".equals(editTxtAccount.getText().toString()) && "1014".equals(editTxtPassword.getText().toString())){
				gotoSpearnetPackagesPickupHandler();
			}
			else {
				popupDialog();
			}
		}
	};
	
	private void popupDialog(){
		//
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());
		alertDialogBuilder.setTitle("SUDA Tracking Number");
		alertDialogBuilder
				.setMessage(
						"Wrong Account or Password!")
				.setCancelable(false)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								editTxtAccount.setText("");
								editTxtPassword.setText("");
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if(!"".equals(editTxtAccount.getText().toString()) || !"".equals(editTxtPassword.getText().toString())){
			editTxtAccount.setText("");
			editTxtPassword.setText("");
		}
		super.onPause();
	}

	//XML components
	EditText editTxtAccount= null;
	EditText editTxtPassword = null;
	Button btnSubmit = null;
	
	private void initXMLcomponents(View mView){
		editTxtAccount = (EditText) mView.findViewById(R.id.editTxtAccount);
		editTxtPassword = (EditText) mView.findViewById(R.id.editTxtPassword);
		btnSubmit = (Button) mView.findViewById(R.id.btnSubmitLoginInfo);
		btnSubmit.setOnClickListener(submitClick);
	}

}
