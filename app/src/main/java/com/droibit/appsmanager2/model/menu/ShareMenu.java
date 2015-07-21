package com.droibit.appsmanager2.model.menu;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.droibit.apps.AppEntry;
import com.droibit.apps.Applications;
import com.droibit.apps.utils.SortTypes;
import com.droibit.apps.utils.SorterFactory;
import com.droibit.appsmanager2.R;
import com.droibit.appsmanager2.fragment.ShareGridFragment;
import com.droibit.appsmanager2.fragment.ShareListFragment;
import com.droibit.appsmanager2.model.loader.AppListLoaderCallbacks;
import com.droibit.appsmanager2.view.adapter.AppArrayAdapter;
import com.droibit.content.IntentHepler;
import com.droibit.text.Strings;
import com.droibit.widget.ListViews;

import java.util.List;

import static com.droibit.appsmanager2.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager2.MainActivity.ARG_SECTION_NUMBER;

/**
 * アプリケーションの共有フラグメントのメニューをハンドルするためのクラス
 *
 * @author kumagai
 * @since 2014/03/28.
 */
public class ShareMenu extends FragmentMenu {

    private ActionMode actionMode;

    /**
     * 新しいインスタンスを作成する
     *
     * @param targetFragemt メニューを保持するフラグメント
     * @param loaderCallbacks {@link android.support.v4.content.Loader}のコールバック
     */
    public ShareMenu(Fragment targetFragemt, AppListLoaderCallbacks loaderCallbacks) {
        this.targetFragemnt = targetFragemt;
        this.loaderCallbacks = loaderCallbacks;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreatedOptionsMenu(Menu menu) {
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                onRefreshApplications();
                return true;
            case R.id.action_sort:
                onSortApplications();
                return true;
            case R.id.action_view_as_grid: {
                final int section = targetFragemnt.getArguments().getInt(ARG_SECTION_NUMBER);
                onToggleFragment(ShareGridFragment.newInstance(Applications.DOWNLOADED, section));
                return true;
            }
            case R.id.action_view_as_list: {
                final int section = targetFragemnt.getArguments().getInt(ARG_SECTION_NUMBER);
                onToggleFragment(ShareListFragment.newInstance(Applications.DOWNLOADED, section));
                return true;
            }
        }
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public void onChooseSortType(int index) {
        final Bundle args = targetFragemnt.getArguments();
        final SortTypes sortType = SortTypes.from(index);
        final SortTypes currentSortType = (SortTypes) args.getSerializable(ARGS_SORT_TYPE);

        if (currentSortType == sortType) {
            return;
        }

        ((AppArrayAdapter)listView.getAdapter()).sort(SorterFactory.get(sortType));
        // 変更がわかるようリストの先頭に移動する
        listView.setSelection(0);

        args.putSerializable(ARGS_SORT_TYPE, sortType);
    }

    /** {@inheritDoc} */
    @Override
    public boolean showActionMode() {
        return actionMode != null;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        actionMode = mode;
        mode.getMenuInflater().inflate(R.menu.actionmode_share, menu);
        mode.setTitle(R.string.title_actionmode);
        mode.setSubtitle(targetFragemnt.getString(R.string.actionmode_selected_count, 0));
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_share) {
            shareApps();
            mode.finish();
        }
        return true;
    }

    /** {@inheritDoc} */
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        final int selectedCount = listView.getCheckedItemCount();
        mode.setSubtitle(targetFragemnt.getString(
                R.string.actionmode_selected_count, selectedCount));
    }

    /** {@inheritDoc} */
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        actionMode = null;
    }

    private void shareApps() {
        final List<AppEntry> apps = ListViews.getCheckedItems(listView);
        final StringBuilder sb = new StringBuilder();
        for (int i = 0, size = apps.size(); i < size; i++) {
            final AppEntry app = apps.get(i);
            sb.append(app.getLabel()).append(Strings.LINE_BREAK)
              .append(IntentHepler.makeGooglePlayUri(app.getPackageName()));

            if (i < size - 1) {
                sb.append(Strings.DOUBLE_LINE_BREAK);
            }
        }
        IntentHepler.shareText(targetFragemnt.getActivity(), sb.toString());

        // アンインストール対象のアプリが取得できたらアクションモードを終了させておく
        ListViews.clearCheckedItems(listView);
    }
}
