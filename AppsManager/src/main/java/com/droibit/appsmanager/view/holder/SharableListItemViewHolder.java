package com.droibit.appsmanager.view.holder;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droibit.apps.AppEntry;
import com.droibit.appsmanager.R;

/**
 * アプリケーションをリスト表示するためのビューを格納するクラス。<br>
 * このビューホルダーはアプリを共有する際に使用する。
 *
 * @author kumagai
 * @since 2014/03/27.
 */
public class SharableListItemViewHolder {

    private final ImageView mIconView;
    private final TextView mLabelView;
    private final TextView mVersionView;

    /**
     * 新しいインスタンスを作成する
     *
     * @param containerView コンテナビュー
     */
    public SharableListItemViewHolder(View containerView) {
        mIconView = (ImageView) containerView.findViewById(R.id.app_icon);
        mLabelView = (TextView) containerView.findViewById(R.id.app_label);
        mVersionView = (TextView) containerView.findViewById(R.id.app_option);
    }

    /**
     * ビューのコンテンツを更新する
     *
     * @param context コンテキスト
     * @param entry アプリケーション情報
     */
    public void update(Context context, AppEntry entry) {
        mIconView.setImageDrawable(entry.getIcon());
        mLabelView.setText(entry.getLabel());
        mVersionView.setText(context.getString(R.string.display_version_format, entry.getVersion()));
    }
}
