package com.droibit.apps.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.AsyncTaskLoader;

/**
 * Helper class to look for interesting changes to the installed apps so
 * that the loader can be updated.
 */
public class PackageIntentReceiver extends BroadcastReceiver {
	private final AsyncTaskLoader<?> loader;

	/**
	 * 
	 * @param loader
	 */
	public PackageIntentReceiver(AsyncTaskLoader<?> loader) {
		this.loader = loader;

		final IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		filter.addAction(Intent.ACTION_PACKAGE_CHANGED);
		filter.addDataScheme("pm");
		loader.getContext().registerReceiver(this, filter);

		// Register for events related to sdcard installation.
		final IntentFilter extAppFilter = new IntentFilter();
		extAppFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_AVAILABLE);
		extAppFilter.addAction(Intent.ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE);
		loader.getContext().registerReceiver(this, extAppFilter);
	}

	/** {@inheritDoc} */
	@Override
	public void onReceive(Context context, Intent intent) {
		// Tell the loader about the change.
		loader.onContentChanged();
	}
}
