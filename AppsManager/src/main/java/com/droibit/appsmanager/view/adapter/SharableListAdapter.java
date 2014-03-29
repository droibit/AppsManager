package com.droibit.appsmanager.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droibit.appsmanager.R;
import com.droibit.appsmanager.view.holder.SharableListItemViewHolder;
import com.droibit.utils.NullCheck;

/**
 * アプリケーションをリスト表示するためのアダプタ。<br>
 * このアダプタはアプリを共有する際に使用する。
 *
 * @author kumagai
 * @since 2014/03/27.
 */
public class SharableListAdapter extends AppArrayAdapter {

    /**
     * 新しいインスタンスを作成する
     *
     * @param context コンテキスト
     */
    public SharableListAdapter(Context context) {
        super(context, R.layout.listview_item_share_app);
    }

    /** {@inheritDoc} */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final SharableListItemViewHolder viewHolder;
        if (NullCheck.isNull(convertView)) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item_share_app, parent, false);
            viewHolder = new SharableListItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SharableListItemViewHolder)convertView.getTag();
        }
        viewHolder.update(getContext(), getItem(position));

        return convertView;
    }
}
