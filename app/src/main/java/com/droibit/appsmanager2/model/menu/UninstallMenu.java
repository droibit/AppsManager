package com.droibit.appsmanager2.model.menu;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import com.droibit.apps.AppEntry;
import com.droibit.apps.Applications;
import com.droibit.apps.utils.SortTypes;
import com.droibit.apps.utils.SorterFactory;
import com.droibit.appsmanager2.R;
import com.droibit.appsmanager2.fragment.UninstallGridFragment;
import com.droibit.appsmanager2.fragment.UninstallListFragment;
import com.droibit.appsmanager2.model.loader.AppListLoaderCallbacks;
import com.droibit.appsmanager2.view.adapter.AppArrayAdapter;
import com.droibit.content.IntentHepler;
import com.droibit.text.Strings;
import com.droibit.widget.ListViews;

import java.util.List;

import static com.droibit.appsmanager2.MainActivity.ARGS_SORT_TYPE;
import static com.droibit.appsmanager2.MainActivity.ARG_SECTION_NUMBER;

/**
 * アプリケーションのアンインストールフラグメントのメニューをハンドルするためのクラス
 *
 * @author kumagai
 * @since 2014/03/28.
 */
public class UninstallMenu extends FragmentMenu implements SearchView.OnQueryTextListener {

    private class UninstallTask extends AsyncTask<List<AppEntry>, Void, Void> {
        @Override
        protected Void doInBackground(List<AppEntry>... params) {
            final List<AppEntry> apps = params[0];
            for (AppEntry app : apps) {
                IntentHepler.uninstallApp(targetFragemnt.getActivity(), app.getPackageName());
            }
            return null;
        }
    }

    /**
     * 新しいインスタンスを作成する
     *
     * @param targetFragemt メニューを保持するフラグメント
     * @param loaderCallbacks {@link Loader}のコールバック
     */
    public UninstallMenu(Fragment targetFragemt, AppListLoaderCallbacks loaderCallbacks) {
        this.targetFragemnt = targetFragemt;
        this.loaderCallbacks = loaderCallbacks;
    }

    /** {@inheritDoc} */
    @Override
    public void onCreatedOptionsMenu(Menu menu) {
        final MenuItem item = menu.getItem(0);
        final SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(this);

        MenuItemCompat.setOnActionExpandListener(item, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                // ビューを閉じる際にアダプタのフィルタをクリアしておく
                searchView.setQuery(Strings.EMPTY, false);
                return true;
            }
        });
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
                onToggleFragment(UninstallGridFragment.newInstance(Applications.DOWNLOADED, section));
                return true;
            }
            case R.id.action_view_as_list: {
                final int section = targetFragemnt.getArguments().getInt(ARG_SECTION_NUMBER);
                onToggleFragment(UninstallListFragment.newInstance(Applications.DOWNLOADED, section));
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
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.actionmode_uninstall, menu);
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
        if (item.getItemId() == R.id.action_uninstall) {
            final List<AppEntry> apps = ListViews.getCheckedItems(listView);
            ListViews.clearCheckedItems(listView);
            mode.finish();

            // 選択されたアプリケーションをアンインストールする
            new UninstallTask().execute(apps);
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
    }

    /** {@inheritDoc} */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    public boolean onQueryTextChange(String newText) {
        if (TextUtils.isEmpty(newText)) {
            listView.clearTextFilter();
        } else {
            listView.setFilterText(newText);
        }
        return true;
    }
  }
