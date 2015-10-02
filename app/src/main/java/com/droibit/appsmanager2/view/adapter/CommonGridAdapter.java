package com.droibit.appsmanager2.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.droibit.appsmanager2.R;
import com.droibit.appsmanager2.view.holder.CommonGridItemViewHolder;
import com.droibit.utils.NullCheck;

/**
 * アプリケーションをグリッド表示するためのアダプタ。<br>
 * このアダプタはアプリをアンインストール際に使用する。
 *
 * @author kumagai
 */
public class CommonGridAdapter extends AppArrayAdapter {

    /**
     * 新しいインスタンスを作成する
     *
     * @param context コンテキスト
     */
    public CommonGridAdapter(Context context) {
        super(context, R.layout.gridview_item_app);
    }

    /** {@inheritDoc} */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final CommonGridItemViewHolder viewHolder;
        if (NullCheck.isNull(convertView)) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.gridview_item_app, parent, false);
            viewHolder = new CommonGridItemViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CommonGridItemViewHolder)convertView.getTag();
        }
        viewHolder.update(getItem(position));

        return convertView;
    }
}
