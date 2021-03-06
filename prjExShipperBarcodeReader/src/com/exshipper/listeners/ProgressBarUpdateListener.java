package com.exshipper.listeners;

import java.util.List;

import org.apache.http.NameValuePair;

public interface ProgressBarUpdateListener extends ViewsUpdateListener{
	void setupProgressBar();
	void updateProgressBar(int p_progress);
	boolean isProgressCountable();
	boolean isAuthorizationNecessary(); //working on
	List<NameValuePair> setAuthorizationInfo();
	void updateResult(String p_result);
}
