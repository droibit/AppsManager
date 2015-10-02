package com.droibit.apps.loader;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.support.v4.content.AsyncTaskLoader;

import com.droibit.apps.Applications;
import com.droibit.apps.AppEntry;
import com.droibit.apps.utils.FilterFactory;
import com.droibit.apps.utils.IFilterable;
import com.droibit.apps.utils.SortTypes;
import com.droibit.apps.receiver.PackageIntentReceiver;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.CONFIG_LOCALE;
import static android.content.pm.ActivityInfo.CONFIG_SCREEN_LAYOUT;
import static android.content.pm.ActivityInfo.CONFIG_UI_MODE;
import static android.content.pm.PackageManager.GET_DISABLED_COMPONENTS;
import static android.content.pm.PackageManager.GET_UNINSTALLED_PACKAGES;

/**
 * A custom Loader that loads all of the installed applications.
 */
public class ApplicationLoader extends AsyncTaskLoader<List<AppEntry>> {

	private final InterestingConfigChanges lastConfig;
	private final PackageManager packageManager;
	private final IFilterable appFilter;

	private List<AppEntry> apps;
	private PackageIntentReceiver packageObserver;


	public ApplicationLoader(Context context) {
		this(context, Applications.DOWNLOADED);
	}

	public ApplicationLoader(Context context, Applications appType) {
		this(context, appType, SortTypes.NAME_ASC);
	}

	public ApplicationLoader(Context context, Applications appType, SortTypes sortType) {
		super(context);

		// Retrieve the pm manager for later use; note we don't
		// use 'context' directly but instead the save global application
		// context returned by getContext().
		this.packageManager = getContext().getPackageManager();
		this.lastConfig = new InterestingConfigChanges();
		this.appFilter = FilterFactory.get(appType);
	}

	/**
	 * This is where the bulk of our work is done. This function is called in a
	 * background thread and should generate a new set of data to be published
	 * by the loader.
	 */
	@Override
	public List<AppEntry> loadInBackground() {
		final int flags = GET_UNINSTALLED_PACKAGES | GET_DISABLED_COMPONENTS;
		// Retrieve all known applications.
		List<ApplicationInfo> apps = packageManager.getInstalledApplications(flags);
		if (apps == null) {
			apps = new ArrayList<>();
		}

		final Context context = getContext();
		final String packageName = context.getPackageName();
		// Create corresponding array of entries and load their labels.
		final List<AppEntry> entries = new ArrayList<AppEntry>(apps.size());
		for (int i = 0, size = apps.size(); i < size; i++) {
			final ApplicationInfo appInfo = apps.get(i);			
			if (appFilter.filter(appInfo) && !packageName.equals(appInfo.packageName)) {
				final AppEntry entry = new AppEntry(context, appInfo);
				entry.loadName();
				entries.add(entry);
			}
		}
		return entries;
	}

	/**
	 * Called when there is new data to deliver to the client. The super class
	 * will take care of delivering it; the implementation here just adds a
	 * little more logic.
	 */
	@Override
	public void deliverResult(List<AppEntry> apps) {
		if (isReset()) {
			// An async query came in while the loader is stopped. We1don't need the result.
			if (apps != null) {
				onReleaseResources(apps);
			}
		}
		final List<AppEntry> oldApps = apps;
		this.apps = apps;

		if (isStarted()) {
			// If the Loader is currently started, we can immediately
			// deliver its results.
			super.deliverResult(apps);
		}

		// At this point we can release the resources associated with
		// 'oldApps' if needed; now that the new result is delivered we
		// know that it is no longer in use.
		if (oldApps != null) {
			onReleaseResources(oldApps);
		}
	}

	/**
	 * Handles a request to start the Loader.
	 */
	@Override
	protected void onStartLoading() {
		if (apps != null) {
			// If we currently have a result available, deliver it
			// immediately.
			deliverResult(apps);
		}

		// Start watching for changes in the app data.
		if (packageObserver == null) {
			packageObserver = new PackageIntentReceiver(this);
		}

		// Has something interesting in the configuration changed since we
		// last built the app list?
		final boolean configChange = lastConfig.applyNewConfig(getContext()
				.getResources());

		if (takeContentChanged() || apps == null || configChange) {
			// If the data has changed since the last time it was loaded
			// or is not currently available, start a load.
			forceLoad();
		}
	}

	/**
	 * Handles a request to stop the Loader.
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}

	/**
	 * Handles a request to cancel a load.
	 */
	@Override
	public void onCanceled(List<AppEntry> apps) {
		super.onCanceled(apps);

		// At this point we can release the resources associated with 'apps'
		// if needed.
		onReleaseResources(apps);
	}

	/**
	 * Handles a request to completely reset the Loader.
	 */
	@Override
	protected void onReset() {
		super.onReset();

		// Ensure the loader is stopped
		onStopLoading();

		// At this point we can release the resources associated with 'apps'
		// if needed.
		if (apps != null) {
			onReleaseResources(apps);
			apps = null;
		}

		// Stop monitoring for changes.
		if (packageObserver != null) {
			getContext().unregisterReceiver(packageObserver);
			packageObserver = null;
		}
	}

	/**
	 * Helper function to take care of releasing resources associated with an
	 * actively loaded data set.
	 */
	protected void onReleaseResources(List<AppEntry> apps) {
		// For a simple List<> there is nothing to do. For something
		// like a Cursor, we would close it here.
	}

	/**
	 * Helper for determining if the configuration has changed in an interesting
	 * way so we need to rebuild the app list.
	 */
	public static class InterestingConfigChanges {
		final Configuration lastConfiguration = new Configuration();
		int lastDensity;

		boolean applyNewConfig(Resources res) {
			final int configFrags = CONFIG_LOCALE | CONFIG_UI_MODE
					| CONFIG_SCREEN_LAYOUT;

			final int configChanges = lastConfiguration.updateFrom(res
					.getConfiguration());
			final boolean densityChanged = lastDensity != res.getDisplayMetrics().densityDpi;
			if (densityChanged || (configChanges & configFrags) != 0) {
				lastDensity = res.getDisplayMetrics().densityDpi;
				return true;
			}
			return false;
		}
	}
}
