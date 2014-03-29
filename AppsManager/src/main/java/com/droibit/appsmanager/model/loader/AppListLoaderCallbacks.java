package com.droibit.appsmanager.model.loader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.AbsListView;

import com.droibit.apps.AppEntry;
import com.droibit.apps.loader.AppliationLoader;
import com.droibit.apps.utils.SortTypes;
import com.droibit.apps.utils.SorterFactory;
import com.droibit.appsmanager.view.adapter.AppArrayAdapter;
import com.droibit.utils.NullCheck;
import com.droibit.widget.ScrolledListHolder;

import java.util.List;

import static com.droibit.appsmanager.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager.MainActivity.ARGS_UPDATED;

/**
 *
 * @author kumagai
 * @since 2014/03/28.
 */
public class AppListLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<AppEntry>> {

    /** ロード中を表す{@link Bundle}のキー */
    public static final String ARG_PROGRESS = "progress";

    /** {@link LoaderManager.LoaderCallbacks}のID */
    public static final int ID = 1;

    private final Fragment targetFragmet;
    private final OnLoadListener listener;
    private AbsListView listViiew;
    private AppArrayAdapter listAdapter;
    private ScrolledListHolder scrollHolder;

    /**
     * 新しいインスタンスを作成する
     *
     * @param fragment 保持するフラグメント
     */
    public AppListLoaderCallbacks(Fragment fragment) {
        targetFragmet = fragment;
        listener = (OnLoadListener) fragment;
        scrollHolder = new ScrolledListHolder();
    }

    /** {@inheritDoc} */
    @Override
    public Loader<List<AppEntry>> onCreateLoader(int id, Bundle args) {
        listener.onPreLoad();

        if (args.getBoolean(ARGS_UPDATED)) {
            scrollHolder.save(listViiew);
            args.putBoolean(ARGS_UPDATED, false);
        }
        args.putBoolean(ARG_PROGRESS, true);
        return new AppliationLoader(targetFragmet.getActivity());
    }

    /** {@inheritDoc} */
    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
        final Bundle args = targetFragmet.getArguments();
        args.putBoolean(ARG_PROGRESS, false);

        final SortTypes sortType = (SortTypes) args.getSerializable(ARGS_SORT_TYPE);
        listAdapter.setNewItems(data);
        listAdapter.sort(SorterFactory.get(sortType));
        if (NullCheck.isNotNull(scrollHolder)) {
            scrollHolder.restore(listViiew);
        }

        listener.onPostLoad();
    }

    /** {@inheritDoc} */
    @Override
    public void onLoaderReset(Loader<List<AppEntry>> loader) {
        final Bundle args = targetFragmet.getArguments();
        args.putBoolean(ARG_PROGRESS, false);
        listAdapter.clear();
    }

    /**
     * アプリケーションのロード中かどうか
     *
     * @return trueの場合ロード中、falseの場合ロードしていない
     */
    public boolean isProgress() {
        return targetFragmet.getArguments().getBoolean(ARG_PROGRESS, false);
    }

    public void setListView(AbsListView listView) {
        this.listViiew = listView;
        this.listAdapter = (AppArrayAdapter)listView.getAdapter();
    }

    public ScrolledListHolder getScrollHolder() {
        return scrollHolder;
    }

    public void setScrollHolder(ScrolledListHolder scrollHolder) {
        this.scrollHolder = scrollHolder;
    }
}
