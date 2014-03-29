package com.droibit.appsmanager.model.refresh;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.AbsListView;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by kumagai on 2014/03/29.
 */
public final class PullToRefresh {

    /**
     * {@link AbsListView}を取得するためのイベントリスナー
     */
    public interface OnAbsListViewListener {

        /**
         * {@link AbsListView}を取得する際に呼ばれる処理
         *
         * @return {@link AbsListView}オブジェクト
         */
        public AbsListView getAbsListView();
    }

    /**
     * Pull-to-refreshコンポーネントを{@link AbsListView}に設定する
     *
     * @param targetFragment {@link AbsListView}を包含するフラグメント
     * @param parentGroup {@link AbsListView}を包含するレイアウト
     * @return {@link PullToRefreshLayout}オブジェクト
     */
    public static PullToRefreshLayout setup(Fragment targetFragment, ViewGroup parentGroup) {
        final OnAbsListViewListener listener = (OnAbsListViewListener) targetFragment;
        final AbsListView listView = listener.getAbsListView();
        final PullToRefreshLayout refreshLayout = new PullToRefreshLayout(parentGroup.getContext());

        ActionBarPullToRefresh.from(targetFragment.getActivity())
                .insertLayoutInto(parentGroup)
                .theseChildrenArePullable(listener.getAbsListView(), listener.getAbsListView().getEmptyView())
                .listener((OnRefreshListener)targetFragment)
                .setup(refreshLayout);

        return refreshLayout;
    }
}
