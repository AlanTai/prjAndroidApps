package com.exshipper.listeners;

public interface ProgressBarUpdateListener extends ViewsUpdateListener{
	void setupProgressBar();
	void updateProgressBar(int p_progress);
	boolean isProgressCountable();
}
