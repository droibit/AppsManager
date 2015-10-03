package com.droibit.appsmanager2.fragment;

import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
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
import com.droibit.appsmanager2.view.ListFragmentSwipeRefreshLayout;
import com.droibit.appsmanager2.view.adapter.AppArrayAdapter;
import com.droibit.appsmanager2.view.adapter.CommonGridAdapter;
import com.droibit.utils.NullCheck;
import com.droibit.view.OnClickItemListener;
import com.droibit.widget.ListViews;
import com.droibit.widget.ScrolledListHolder;

import java.io.UnsupportedEncodingException;
import java.util.List;

import static com.droibit.appsmanager2.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager2.MainActivity.ARG_SECTION_NUMBER;
import static com.droibit.widget.ScrolledListHolder.KEY_SCROLL_HOLDER;

/**
 * アプリケーションを共有するためのフラグメント。<br>
 * アプリケーションをグリッド表示するので、複数選択しやすくなる。
 *
 * @author kumagai
 */
public class ShareGridFragment extends FlexibleGridFragment
        implements OnLoadListener, OnClickItemListener,
        SwipeRefreshLayout.OnRefreshListener,
        ListFragmentSwipeRefreshLayout.Adapter,
        NfcManager.INdefMessageDelegate {

    private FragmentMenu actionMenu;
    private AppListLoaderCallbacks loaderCallbacks;
    private SwipeRefreshLayout mSwipeRefreshLayout;

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
    public void onAttach(Context context) {
        super.onAttach(context);

        ((MainActivity) context).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));

        ((NfcManager.NdefMessageDelegateCallback) context)
                .receiveNdefMessageDelegate(null);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create the list fragment's content view by calling the super method
        final View listFragmentView = super.onCreateView(inflater, container, savedInstanceState);

        // Now create a SwipeRefreshLayout to wrap the fragment's content view
        mSwipeRefreshLayout = new ListFragmentSwipeRefreshLayout(container.getContext(), this);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        // Add the list fragment's content view to the SwipeRefreshLayout, making sure that it fills
        // the SwipeRefreshLayout
        mSwipeRefreshLayout.addView(listFragmentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        // Make sure that the SwipeRefreshLayout will fill the fragment
        mSwipeRefreshLayout.setLayoutParams(
                new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT));

        // Now return the SwipeRefreshLayout as this fragment's content view
        return mSwipeRefreshLayout;
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
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
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
    public void onRefresh() {
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
