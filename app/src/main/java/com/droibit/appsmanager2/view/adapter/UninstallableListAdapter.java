package com.droibit.appsmanager2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droibit.appsmanager2.R;
import com.droibit.appsmanager2.view.holder.UninstallableListItemViewHolder;
import com.droibit.utils.NullCheck;

/**
 * アプリケーションをリスト表示するためのアダプタ。<br>
 * このアダプタはアプリをアンインストール際に使用する。
 *
 * @author kumagai
 */
public class UninstallableListAdapter extends AppArrayAdapter {

    /**
     * 新しいインスタンスを作成する
     *
     * @param context コンテキスト
     */
    public UninstallableListAdapter(Context context) {
        super(context, R.layout.listview_item_uinstall_app);
    }

    /** {@inheritDoc} */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final UninstallableListItemViewHolder viewHolder;
        if (NullCheck.isNull(convertView)) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_uinstall_app, parent, false);
            viewHolder = new UninstallableListItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UninstallableListItemViewHolder)convertView.getTag();
        }
        viewHolder.update(getItem(position));

        return convertView;
    }
}