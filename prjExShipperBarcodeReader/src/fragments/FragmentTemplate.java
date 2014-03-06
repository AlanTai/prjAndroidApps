package fragments;

import com.exshipper.prjexshipperbarcodereader.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class FragmentTemplate extends Fragment {
	//
	private static final int EXSHIPPER_LOGIN = 0x1;
	private static final int EXSHIPPER_SPEARNET_PACKAGES_PICKUP_HANDLER = 0x2;
	private static final int EXSHIPPER_TW_CUSTOM_ENTRY_PACKAGES_HANDLER = 0x3;

	private int currentTabHandler = 0x0;
	
	//---functions of toggling fragments---
	public void gotoLogin() {
		// go to login view
		if (currentTabHandler != EXSHIPPER_LOGIN) {
			currentTabHandler = EXSHIPPER_LOGIN;

			// toggle fragment content
			FragmentManager fm = getFragmentManager();
			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content, new LoginFragment());
				ft.commit();
			}
		}
	}
	
	public void gotoSpearnetPackagesPickupHandler() {
		// go to spearnet fragment
		if (currentTabHandler != EXSHIPPER_SPEARNET_PACKAGES_PICKUP_HANDLER) {
			currentTabHandler = EXSHIPPER_SPEARNET_PACKAGES_PICKUP_HANDLER;

			// toggle fragment content
			FragmentManager fm = getFragmentManager();
			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content,
						new SpearnetPakcagesPickupFragment());
				ft.commit();
			}
		}
	}

	public void gotoTWCustomEntryPackagesHandler() {
		// go to spearnet fragment
		if (currentTabHandler != EXSHIPPER_TW_CUSTOM_ENTRY_PACKAGES_HANDLER) {
			currentTabHandler = EXSHIPPER_TW_CUSTOM_ENTRY_PACKAGES_HANDLER;

			// toggle fragment content
			FragmentManager fm = getFragmentManager();
			if (fm != null) {
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.fragment_content,
						new TWCustomEntryPackagesFragment());
				ft.commit();
			}
		}

	}

}
