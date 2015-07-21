package com.droibit.appsmanager2.view.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.droibit.apps.AppEntry;
import com.droibit.appsmanager2.R;

/**
 * アプリケーションをリスト表示するためのビューを格納するクラス。<br>
 * このビューホルダーはアプリをアンインストール際に使用する。
 *
 * @author kumagai
 * @since 2014/03/27.
 */
public class UninstallableListItemViewHolder {

    private final ImageView mIconView;
    private final TextView mLabelView;
    private final TextView mUpdateTimeView;
    private final TextView mSizeView;
    private String mPackageName;

    /**
     * 新しいインスタンスを作成する
     *
     * @param containerView コンテナビュー
     */
    public UninstallableListItemViewHolder(View containerView) {

        mIconView = (ImageView) containerView.findViewById(R.id.app_icon);
        mLabelView = (TextView) containerView.findViewById(R.id.app_label);
        mUpdateTimeView = (TextView) containerView.findViewById(R.id.app_last_updatetime);
        mSizeView = (TextView) containerView.findViewById(R.id.app_size);
    }

    /**
     * ビューのコンテンツを更新する
     *
     * @param entry アプリケーション情報
     */
    public void update(AppEntry entry) {
        mIconView.setImageDrawable(entry.getIcon());

        mLabelView.setText(entry.getLabel());
        mUpdateTimeView.setText(entry.getFormattedUpdateTime());
        // TODO サイズが表示されない原因は同期がとれていないのでここでサイズ取得、反映する必要がある
        mSizeView.setText(entry.getFormattedAppSize());

        mPackageName = entry.getPackageName();
    }

}
