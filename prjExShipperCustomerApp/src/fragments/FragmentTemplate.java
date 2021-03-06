package fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentTemplate extends Fragment {
	//
	private static int currentTabHandler = 0x0;
	private static final int EXSHIPPER_CUSTOMER_DEFAULT_HANDLER = 0x1;
	private static final int EXSHIPPER_CUSTOMER_LOGIN_HANDLER = 0x2;
	private static final int EXSHIPPER_CUSTOMER_LOGISTICS_SERVICE_HANDLER = 0x3;
	private static final int EXSHIPPER_CUSTOMER_SIGNATURE_HANDLER = 0x4;
	
	
	//
	public void gotoDefault(){
		if(currentTabHandler != EXSHIPPER_CUSTOMER_DEFAULT_HANDLER){
			currentTabHandler = EXSHIPPER_CUSTOMER_DEFAULT_HANDLER;
		}
	}
	
	public void gotoLoginHandler(){
		if(currentTabHandler != EXSHIPPER_CUSTOMER_LOGIN_HANDLER){
			currentTabHandler = EXSHIPPER_CUSTOMER_LOGIN_HANDLER;
			
			FragmentManager fm = getFragmentManager();
			if(fm != null){
				FragmentTransaction ft = fm.beginTransaction();
			}
		}
	}
	
	public void gotoLogisticsServiceHandler(){
		if(currentTabHandler != EXSHIPPER_CUSTOMER_LOGISTICS_SERVICE_HANDLER){
			currentTabHandler = EXSHIPPER_CUSTOMER_LOGISTICS_SERVICE_HANDLER;
			
			FragmentManager fm = getFragmentManager();
			if(fm != null){
				FragmentTransaction ft = fm.beginTransaction();
			}
		}
	}

	public void gotoSignatureHandler(){
		if(currentTabHandler != EXSHIPPER_CUSTOMER_SIGNATURE_HANDLER){
			currentTabHandler = EXSHIPPER_CUSTOMER_SIGNATURE_HANDLER;
			
			FragmentManager fm = getFragmentManager();
			if(fm != null){
				FragmentTransaction ft = fm.beginTransaction();
			}
		}
	}

}
