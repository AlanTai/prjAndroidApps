package com.exshipper.handlers;

import com.exshipper.listeners.ViewsUpdateListener;

public class ViewsUpdateHandler {
	private ViewsUpdateListener viewUpdateListener=null;
	void addViewUpdateListener(ViewsUpdateListener p_view) {
		viewUpdateListener = p_view;
	}
	
	public boolean execute(){
		updateView();
		return false;
	}
	
	private boolean updateView(){
		String result = "NA";
		viewUpdateListener.updateView(result);
		return false;
	}
	
}
