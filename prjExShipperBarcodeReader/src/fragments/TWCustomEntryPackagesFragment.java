package fragments;

import com.exshipper.handlers.WebContentDownloadHandler;
import com.exshipper.prjexshipperbarcodereader.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TWCustomEntryPackagesFragment extends FragmentTemplate{

	WebContentDownloadHandler getTWCustomEntryNumber = null;
	
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
		View mView = inflater.inflate(R.id.tw_custom_entry_packages_handler, container,false);
		initXMLViewComponents(mView);
		return mView;
	}
	
	/*XML components*/
	
	private void initXMLViewComponents(View mView){
		
	}

}
