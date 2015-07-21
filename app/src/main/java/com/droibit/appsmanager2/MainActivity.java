package com.droibit.appsmanager2;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NdefMessage;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.droibit.apps.Applications;
import com.droibit.appsmanager2.fragment.NavigationDrawerFragment;
import com.droibit.appsmanager2.fragment.ShareListFragment;
import com.droibit.appsmanager2.fragment.UninstallListFragment;
import com.droibit.appsmanager2.model.utils.NfcManager;
import com.droibit.appsmanager2.model.utils.OnNfcRegisterListener;
import com.droibit.nfc.NfcHandler;

/**
 * アプリケーション一覧を表示するためのメインアクティビティ
 *
 * @author kumagai
 * @since 2014/03/27
 */
public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
            NfcHandler.OnNfcEventListener, NfcManager.NdefMessageDelegateCallback {

    public static final String ARGS_SORT_TYPE = "sort_type";
    public static final String ARGS_UPDATED = "updated";
    public static final String ARG_SECTION_NUMBER = "section_number";

    private OnNfcRegisterListener nfcRegisterListener;
    private NfcManager.INdefMessageDelegate ndefMessageDelegate;

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

        // #setContentView より先にインスタンスを作成しないと#onNavigationDrawerItemSelected でヌルポ発生
        if (NfcHandler.isEnableNfc(this)) {
            nfcRegisterListener = new NfcManager(this);
        } else {
            nfcRegisterListener = new NfcManager.NullObject();
        }

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
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
        // 全フラグメント共通の設定メニュークリックのみハンドルする
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

        // NFCイベントハンドルの有無を切り替える（共有時のみ有効）
        nfcRegisterListener.onToggle(position == 1);
    }

    /** {@inheritDoc} */
    @Override
    public void reciveNdefMessageDelegate(NfcManager.INdefMessageDelegate delegate) {
        ndefMessageDelegate = delegate;
    }

    /** {@inheritDoc} */
    @Override
    public NdefMessage onCreateNdefMessage(NfcEvent nfcEvent) {
        if (ndefMessageDelegate == null) {
            return null;
        }
        return ndefMessageDelegate.createNdefMessage();
    }

    /** {@inheritDoc} */
    @Override
    public void onNdefPushComplete() {

    }

    /**
     * 選択されたナビゲーションに対応する{@link ActionBar}のタイトル文字列を切り替える
     *
     * @param number ナビゲーションのインデックス
     */
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

    /**
     * アクションバーにタイトルをセットする
     */
    public void restoreActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    /**
     * ナビゲーションを表示しているかどうか
     *
     * @return trueの場合表示、falseの場合非表示
     */
    public boolean isDrawerOpen() {
        return mNavigationDrawerFragment.isDrawerOpen();
    }
}
