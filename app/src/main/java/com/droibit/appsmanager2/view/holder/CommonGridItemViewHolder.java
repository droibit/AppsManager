package com.droibit.appsmanager2.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droibit.apps.AppEntry;
import com.droibit.appsmanager2.R;

/**
 * アプリケーションをグリッド表示するためのビューを格納するクラス
 *
 * @author kumagai
 * @since 2014/03/27.
 */
public class CommonGridItemViewHolder {

    private ImageView mIconView;
    private TextView mLabelView;

    /**
     * 新しいインスタンスを作成する
     *
     * @param containerView コンテナビュー
     */
    public CommonGridItemViewHolder(View containerView) {
        //final View containerView = LayoutInflater.from(context).inflate(R.layout.gridview_item_app, parent, false);
        mIconView = (ImageView) containerView.findViewById(R.id.app_icon);
        mLabelView = (TextView) containerView.findViewById(R.id.app_label);
    }

    /**
     * ビューのコンテンツを更新する
     *
     * @param entry アプリケーション情報
     */
    public void update(AppEntry entry) {
        mIconView.setImageDrawable(entry.getIcon());
        mLabelView.setText(entry.getLabel());
    }
}
