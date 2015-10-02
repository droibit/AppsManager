package com.droibit.appsmanager2.view.adapter;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.droibit.apps.AppEntry;

import java.util.List;

/**
 * {@link AppEntry}をビューで表示するための基底アダプタ
 *
 * @author kumagai
 */
public class AppArrayAdapter extends ArrayAdapter<AppEntry> {

    /**
     * 新しいインスタンスを作成する
     *
     * @param context コンテキスト
     * @param resource ビューのリソースID
     */
    public AppArrayAdapter(Context context, int resource) {
        super (context, resource);
    }

    /**
     * 新しい{@link AppEntry}のリストを保持する
     *
     * @param items 新しい{@link AppEntry}のリスト
     */
    public void setNewItems(List<AppEntry> items) {
        clear();
        addAll(items);
    }
}
