package com.droibit.appsmanager2.model.utils;

import android.support.v4.app.Fragment;
import android.view.ViewGroup;
import android.widget.AbsListView;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * フラグメントにPull to refreshを設定するためのユーティリティクラス
 *
 * @author kumagai
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
        AbsListView getAbsListView();
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
        final PullToRefreshLayout refreshLayout = new PullToRefreshLayout(parentGroup.getContext());

        ActionBarPullToRefresh.from(targetFragment.getActivity())
                .insertLayoutInto(parentGroup)
                .theseChildrenArePullable(listener.getAbsListView(), listener.getAbsListView().getEmptyView())
                .listener((OnRefreshListener)targetFragment)
                .setup(refreshLayout);

        return refreshLayout;
    }
}
