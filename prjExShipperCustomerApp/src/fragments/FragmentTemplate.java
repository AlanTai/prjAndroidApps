package fragments;

import android.support.v4.app.Fragment;

public class FragmentTemplate extends Fragment {
	//
	private static int currentTabHandler = 0x0;
	private static final int EXSHIPPER_CUSTOMER_DEFAULT_PAGE = 0x0;
	public void gotoDefault(){
		if(currentTabHandler != EXSHIPPER_CUSTOMER_DEFAULT_PAGE){
			currentTabHandler = EXSHIPPER_CUSTOMER_DEFAULT_PAGE;
		}
	}

}
