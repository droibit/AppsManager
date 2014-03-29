package com.droibit.appsmanager.fragment;

import android.app.Activity;
import android.content.Context;
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
import com.droibit.appsmanager.MainActivity;
import com.droibit.appsmanager.R;
import com.droibit.appsmanager.model.loader.AppListLoaderCallbacks;
import com.droibit.appsmanager.model.loader.OnLoadListener;
import com.droibit.appsmanager.model.menu.FragmentMenu;
import com.droibit.appsmanager.model.menu.UninstallMenu;
import com.droibit.appsmanager.model.refresh.PullToRefresh;
import com.droibit.appsmanager.model.refresh.PullToRefresh.OnAbsListViewListener;
import com.droibit.appsmanager.view.adapter.AppArrayAdapter;
import com.droibit.appsmanager.view.adapter.CommonGridAdapter;
import com.droibit.content.IntentHepler;
import com.droibit.utils.NullCheck;
import com.droibit.view.OnClickItemListener;
import com.droibit.widget.ScrolledListHolder;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import static com.droibit.appsmanager.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager.MainActivity.ARG_SECTION_NUMBER;
import static com.droibit.widget.ScrolledListHolder.KEY_SCROLL_HOLDER;


/**
 * 
 * @author kumagai
 * 
 */
public class UninstallGridFragment extends FlexibleGridFragment
        implements OnLoadListener, OnClickItemListener, OnAbsListViewListener, OnRefreshListener {

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
	public static final UninstallGridFragment newInstance(Applications appType, int sectionNumber) {
		final UninstallGridFragment fragment = new UninstallGridFragment();
		final Bundle args = new Bundle();

        args.putSerializable(ARGS_SORT_TYPE, SortTypes.NAME_ASC);
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);

		fragment.setArguments(args);
		return fragment;
	}

    /** {@inheritDoc} */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        loaderCallbacks = new AppListLoaderCallbacks(this);
        actionMenu = new UninstallMenu(this, loaderCallbacks);
    }

    /** {@inheritDoc} */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
    }

	/** {@inheritDoc} */
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        final MainActivity activity = (MainActivity) getActivity();
        if (!activity.isDrawerOpen()) {
            inflater.inflate(R.menu.menu_grid_uninstall, menu);
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
		final AppEntry entry = ((AppArrayAdapter)getListAdapter()).getItem(position);
		IntentHepler.uninstallApp(getActivity(), entry.getPackageName());
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
}
