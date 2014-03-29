package com.droibit.appsmanager2.model.loader;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.widget.AbsListView;

import com.droibit.apps.AppEntry;
import com.droibit.apps.loader.AppliationLoader;
import com.droibit.apps.utils.SortTypes;
import com.droibit.apps.utils.SorterFactory;
import com.droibit.appsmanager2.view.adapter.AppArrayAdapter;
import com.droibit.utils.NullCheck;
import com.droibit.widget.ScrolledListHolder;

import java.util.List;

import static com.droibit.appsmanager2.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager2.MainActivity.ARGS_UPDATED;

/**
 *アプリケーションのロード時のイベントをハンドルするためのコールバッククラス
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

        // 一覧更新後もスクロール位置を復元できるようにする
        if (args.getBoolean(ARGS_UPDATED)) {
            scrollHolder.save(listViiew);
            args.putBoolean(ARGS_UPDATED, false);
        }
        // アプリケーションのロード中かどうか↓のフラグで判定するために使用
        args.putBoolean(ARG_PROGRESS, true);
        return new AppliationLoader(targetFragmet.getActivity());
    }

    /** {@inheritDoc} */
    @Override
    public void onLoadFinished(Loader<List<AppEntry>> loader, List<AppEntry> data) {
        getArguments().putBoolean(ARG_PROGRESS, false);

        final SortTypes sortType = (SortTypes) getArguments().getSerializable(ARGS_SORT_TYPE);
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
        getArguments().putBoolean(ARG_PROGRESS, false);
        listAdapter.clear();
    }

    /**
     * アプリケーションのロード中かどうか
     *
     * @return trueの場合ロード中、falseの場合ロードしていない
     */
    public boolean isProgress() {
        return getArguments().getBoolean(ARG_PROGRESS, false);
    }

    /**
     * アプリケーション一覧を表示する{@link AbsListView}を保持する
     *
     * @param listView {@link AbsListView}オブジェクト
     */
    public void setListView(AbsListView listView) {
        this.listViiew = listView;
        this.listAdapter = (AppArrayAdapter)listView.getAdapter();
    }

    /**
     * {@link AbsListView}のスクロール位置を保持するオブジェクトを取得する
     *
     * @return {@link ScrolledListHolder}オブジェクト
     */
    public ScrolledListHolder getScrollHolder() {
        return scrollHolder;
    }

    /**
     * {@link AbsListView}のスクロール位置を保持するオブジェクトを保持する
     *
     * @param scrollHolder {@link ScrolledListHolder}オブジェクト
     */
    public void setScrollHolder(ScrolledListHolder scrollHolder) {
        this.scrollHolder = scrollHolder;
    }

    private Bundle getArguments() {
        return targetFragmet.getArguments();
    }
}
