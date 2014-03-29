package com.droibit.appsmanager2.model.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;

import com.droibit.app.fragment.dialog.ChoosableListDialogFragment;
import com.droibit.app.fragment.dialog.DialogFramgentInfo;
import com.droibit.apps.utils.SortTypes;
import com.droibit.appsmanager2.R;
import com.droibit.appsmanager2.model.loader.AppListLoaderCallbacks;

import static com.droibit.appsmanager2.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager2.MainActivity.ARGS_UPDATED;

/**
 * フラグメントのメニューイベントをハンドルするための抽象クラス
 *
 * @author kumagai
 * @since 2014/03/28.
 */
public abstract class FragmentMenu implements AbsListView.MultiChoiceModeListener {

    protected Fragment targetFragemnt;
    protected AbsListView listView;
    protected AppListLoaderCallbacks loaderCallbacks;

    /**
     * {@link Menu}を作成した後に呼ばれる処理
     *
     * @param menu 作成した{@link Menu}
     */
    public abstract void onCreatedOptionsMenu(Menu menu);

    /**
     * メニューの項目をクリックした時に呼ばれる処理
     *
     * @param item クリックされたメニュー項目
     * @return 処理を続けるかどうか
     */
    public abstract boolean onOptionsItemSelected(MenuItem item);

    /**
     * アプリケーションのソート種類が選択された時に呼ばれる処理
     *
     * @param index {@link SortTypes}に対応するインでクックス
     */
    public abstract void onChooseSortType(int index);

    /**
     * {@link android.view.ActionMode}を表示しているかどうか
     *
     * @return trueの場合有効、falseの場合無効
     */
    public abstract boolean showActionMode();

    /**
     * アプリケーション情報を表示する{@link AbsListView}を保持する
     *
     * @param listView アプリケーション情報を表示する{@link AbsListView}
     */
    public void setListView(AbsListView listView) {
        this.listView = listView;
        this.listView.setTextFilterEnabled(true);
        this.listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        this.listView.setMultiChoiceModeListener(this);
    }

    /**
     * アプリケーション一覧を更新する際に呼ばれる処理
     */
    public void onRefreshApplications() {
        if (loaderCallbacks.isProgress()) {
            return;
        }

        final Bundle args = targetFragemnt.getArguments();
        args.putBoolean(ARGS_UPDATED, true);

        targetFragemnt.getLoaderManager().restartLoader(
                AppListLoaderCallbacks.ID, args, loaderCallbacks);
    }

    /**
     * アプリケーションをソートする際に呼ばれる処理
     */
    public void onSortApplications() {
        final SortTypes sortType = (SortTypes) targetFragemnt.getArguments().getSerializable(ARGS_SORT_TYPE);
        final DialogFramgentInfo info = new DialogFramgentInfo(-1, R.array.sort_types);
        final ChoosableListDialogFragment df = ChoosableListDialogFragment.newInstanceChoise(info, sortType.getIndex());
        df.show(targetFragemnt);
    }

    /**
     * 表示するフラグメントを切り替える際に呼ばれる処理
     *
     * @param fragment 切り替える対象のフラグメント
     */
    public void onToggleFragment(Fragment fragment) {
        targetFragemnt.getFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
