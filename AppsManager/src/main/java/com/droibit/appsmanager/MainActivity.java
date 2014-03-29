package com.droibit.appsmanager;

import android.app.ActionBar;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.droibit.apps.Applications;
import com.droibit.appsmanager.fragment.NavigationDrawerFragment;
import com.droibit.appsmanager.fragment.ShareListFragment;
import com.droibit.appsmanager.fragment.UninstallListFragment;

/**
 *
 *
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final String ARGS_SORT_TYPE = "sort_type";
    public static final String ARGS_UPDATED = "updated";
    public static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        // スクリーンをポートレイトに固定する
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public void onNavigationDrawerItemSelected(int position) {
        final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (position) {
            case 0:
                ft.replace(R.id.container, UninstallListFragment
                                            .newInstance(Applications.DOWNLOADED, position));
                break;
            case 1:
                ft.replace(R.id.container, ShareListFragment
                                            .newInstance(Applications.DOWNLOADED, position));
                break;
        }
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 0:
                mTitle = getString(R.string.actionbar_uninstall);
                break;
            case 1:
                mTitle = getString(R.string.actionbar_share);
                break;
        }
    }

    public void restoreActionBar() {
        final ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    public boolean isDrawerOpen() {
        return mNavigationDrawerFragment.isDrawerOpen();
    }
 }
