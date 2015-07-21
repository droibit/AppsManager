package com.droibit.appsmanager2.fragment;

import android.app.Activity;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.droibit.app.fragment.FlexibleGridFragment;
import com.droibit.apps.AppEntry;
import com.droibit.apps.Applications;
import com.droibit.apps.utils.SortTypes;
import com.droibit.appsmanager2.MainActivity;
import com.droibit.appsmanager2.R;
import com.droibit.appsmanager2.model.loader.AppListLoaderCallbacks;
import com.droibit.appsmanager2.model.loader.OnLoadListener;
import com.droibit.appsmanager2.model.menu.FragmentMenu;
import com.droibit.appsmanager2.model.menu.ShareMenu;
import com.droibit.appsmanager2.model.utils.NfcManager;
import com.droibit.appsmanager2.model.utils.PullToRefresh;
import com.droibit.appsmanager2.model.utils.PullToRefresh.OnAbsListViewListener;
import com.droibit.appsmanager2.view.adapter.AppArrayAdapter;
import com.droibit.appsmanager2.view.adapter.CommonGridAdapter;
import com.droibit.utils.NullCheck;
import com.droibit.view.OnClickItemListener;
import com.droibit.widget.ListViews;
import com.droibit.widget.ScrolledListHolder;

import java.io.UnsupportedEncodingException;
import java.util.List;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import static com.droibit.appsmanager2.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager2.MainActivity.ARG_SECTION_NUMBER;
import static com.droibit.widget.ScrolledListHolder.KEY_SCROLL_HOLDER;

/**
 * アプリケーションを共有するためのフラグメント。<br>
 * アプリケーションをグリッド表示するので、複数選択しやすくなる。
 *
 * @author kumagai
 * @since 2014/03/28.
 */
public class ShareGridFragment extends FlexibleGridFragment
        implements OnLoadListener, OnClickItemListener,
            OnAbsListViewListener, OnRefreshListener, NfcManager.INdefMessageDelegate {

    private FragmentMenu actionMenu;
    private AppListLoaderCallbacks loaderCallbacks;
    private PullToRefreshLayout mPullToRefreshLayout;

    /**
     * 新しいインスタンスを作成する
     *
     * @param appType 表示するアプリケーションの種類
     * @param sectionNumber ナビゲーションのセクション番号
     * @return 新しいインスタンス
     */
    public static final ShareGridFragment newInstance(Applications appType, int sectionNumber) {
        final ShareGridFragment fragment = new ShareGridFragment();
        final Bundle args = new Bundle();

        args.putSerializable(ARGS_SORT_TYPE, SortTypes.NAME_ASC);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

        fragment.setArguments(args);
        return fragment;
    }

    /** {@inheritDoc} */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

        ((NfcManager.NdefMessageDelegateCallback) activity)
                .reciveNdefMessageDelegate(this);
    }

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        loaderCallbacks = new AppListLoaderCallbacks(this);
        actionMenu = new ShareMenu(this, loaderCallbacks);
    }

    /** {@inheritDoc} */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final MainActivity activity = (MainActivity) getActivity();
        if (!activity.isDrawerOpen()) {
            inflater.inflate(R.menu.menu_grid_share, menu);
            activity.restoreActionBar();
            actionMenu.onCreatedOptionsMenu(menu);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionMenu.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AppArrayAdapter listAdapter = new CommonGridAdapter(getActivity());
        setListAdapter(listAdapter);
        loaderCallbacks.setListView(getGridView());
        actionMenu.setListView(getGridView());

        setEmptyText(getText(R.string.empty_text_apps));

        mPullToRefreshLayout = PullToRefresh.setup(this, (ViewGroup) view);
    }

    /** {@inheritDoc} */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (NullCheck.isNotNull(savedInstanceState)) {
            final ScrolledListHolder scrollHolder = savedInstanceState.getParcelable(KEY_SCROLL_HOLDER);
            loaderCallbacks.setScrollHolder(scrollHolder);
        }

        setListShown(false);

        // 画面が回転した場合は以前の状態を復元する
        final Bundle args = getArguments();
        args.putParcelable(KEY_SCROLL_HOLDER, loaderCallbacks.getScrollHolder());
        getLoaderManager().initLoader(AppListLoaderCallbacks.ID, args, loaderCallbacks);
    }

    /** {@inheritDoc} */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(ScrolledListHolder.KEY_SCROLL_HOLDER,
                new ScrolledListHolder(getGridView()));
    }

    /** {@inheritDoc} */
    @Override
    public void onGridItemClick(GridView g, View v, int position, long id) {
        if (!actionMenu.showActionMode()) {
            getGridView().startActionMode(actionMenu);
            getGridView().setItemChecked(position, true);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected GridView createGridView(Context context) {
        return (GridView) LayoutInflater.from(context)
                .inflate(R.layout.fragment_grid, null);
    }

    /** {@inheritDoc} */
    @Override
    public void onPreLoad() {
        setListShown(false);
    }

    /** {@inheritDoc} */
    @Override
    public void onPostLoad() {
        if (mPullToRefreshLayout.isRefreshing()) {
            mPullToRefreshLayout.setRefreshComplete();
        }

        if (isResumed()) {
            setListShown(true);
        } else {
            setListShownNoAnimation(false);
        }
    }

    /** {@inheritDoc} */
    @Override
    public boolean onClickItem(int position) {
        actionMenu.onChooseSortType(position);
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public AbsListView getAbsListView() {
        return getGridView();
    }

    /** {@inheritDoc} */
    @Override
    public void onRefreshStarted(View view) {
        actionMenu.onRefreshApplications();
    }

    /** {@inheritDoc} */
    @Override
    public NdefMessage createNdefMessage() {
        final List<AppEntry> apps = ListViews.getCheckedItems(getGridView());
        if (apps.isEmpty()) {
            return null;
        }

        final NdefRecord records[] = new NdefRecord[apps.size()];
        for (int i = 0, size = apps.size(); i < size; i++) {
            final AppEntry app = apps.get(i);
            try {
                records[i] = NfcManager.createPackageRecord(app.getLabel(), app.getPackageName());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return new NdefMessage(records);
    }
}
