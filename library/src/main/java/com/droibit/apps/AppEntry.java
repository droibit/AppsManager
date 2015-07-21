package com.droibit.apps;

import java.io.File;
import java.text.Format;
import java.util.Date;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.text.format.Formatter;

/**
 * インストールされているアプリケーションアプリケーションのデータ。
 * 
 * @author kumagai
 * 
 */
public class AppEntry {

	/** コンテキスト */
	private final Context context;

	/** アプリケーションの情報 */
	private final ApplicationInfo appInfo;

	/** アプリケーションのスニペット */
	private final AppSnippet appSnippet;

	/** apkファイル */
	private final File apkFile;

	/** アプリケーションの最終更新日 */
	private final Date lastUpdateTime;

	/** アプリケーションのサイズ */
	private final long appSizeInBytes;

    /** アプリケーションのバージョン */
	private String version;

	/**  */
	private boolean mounted;

	/**
	 * コンストラクタ
	 * 
	 * @param context コンテキスト
	 * @param appInfo アプリケーションの情報
	 */
	public AppEntry(final Context context, ApplicationInfo appInfo) {
		this.context = context;
		this.appInfo = appInfo;
		this.apkFile = new File(appInfo.sourceDir);
		this.appSnippet = new AppSnippet();
		this.lastUpdateTime = PackageHelper.getLastUpdateTime(context,
				appInfo.packageName);
		this.appSizeInBytes = apkFile.length();
	}


    /** {@inheritDoc} */
    @Override
    public String toString() {
        return appSnippet.name;
    }


    /**
	 * アプリケーション名を取得する
	 * 
	 * @return アプリケーション名
	 */
	public String getLabel() {
		return appSnippet.name;
	}

	/**
	 * アプリケーションのサイズを取得する
	 * 
	 * @return アプリケーションのサイズ
	 */
	public long getAppSize() {
		return appSizeInBytes;
	}

	/**
	 * アプリケーションのサイズを取得する。<br>
	 * サイズは単位付きでフォーマットされている。
	 * 
	 * @return アプリケーションのサイズ
	 */
	public String getFormattedAppSize() {
		return Formatter.formatFileSize(context, appSizeInBytes);
	}

	/**
	 * アプリケーションが更新された日付を取得する
	 * 
	 * @return アプリケーションが更新された日付
	 */
	public Date getLastUpdatelTime() {
		return lastUpdateTime;
	}

    /**
     * アプリケーションが更新された日付を取得する。<br>
     * 日付は表示用にフォーマットされている。
     *
     * @return アプリケーションの更新日
     */
    public String getFormattedUpdateTime() {
        return DateFormat.getDateFormat(context).format(lastUpdateTime);
    }

	/**
	 * アプリケーションのパッケージ名を取得する
	 * 
	 * @return アプリケーションのパッケージ名
	 */
	public String getPackageName() {
		return appInfo.packageName;
	}

	/**
	 * アプリケーションのアイコンを取得する
	 * 
	 * @return アプリケーションのアイコン
	 */
	public Drawable getIcon() {
		if (appSnippet.icon == null) {
			if (apkFile.exists()) {
				return loadIcon(context.getPackageManager());
			}
			mounted = false;
			return getDefaultAppIcon();
		} else if (!mounted) {
			// If the app wasn't mounted but is now mounted, reload its icon.
			if (apkFile.exists()) {
				mounted = true;
				return loadIcon(context.getPackageManager());
			}
			return getDefaultAppIcon();
		}
		return appSnippet.icon;
	}
	
	/**
	 * アプリケーションのバージョンを取得する
	 * 
	 * @return アプリケーションのバージョン
	 */
	public String getVersion() {
		if (TextUtils.isEmpty(version)) {
			final PackageInfo pi = PackageHelper.getPackageInfo(context, appInfo.packageName);
			version = pi.versionName;
		}
		return version;
	}

	/**
	 * 
	 * @return
	 */
	public boolean existPackageSize() {
		return appSizeInBytes != 0L;
	}

	/**
	 * アプリケーション名をアプリケーション情報から読み込む。
	 */
	public void loadName() {
		if (!TextUtils.isEmpty(appSnippet.name) && mounted) {
			return;
		}

		if (!apkFile.exists()) {
			mounted = false;
			appSnippet.name = appInfo.packageName;
			return;
		}
		final CharSequence label = appInfo.loadLabel(context
				.getPackageManager());
		appSnippet.name = (label != null) ? label.toString()
				: appInfo.packageName;
		mounted = true;
	}

	private Drawable loadIcon(PackageManager pm) {
		appSnippet.icon = appInfo.loadIcon(pm);
		return appSnippet.icon;
	}

    private Drawable getDefaultAppIcon() {
        return context.getResources().getDrawable(
                android.R.drawable.sym_def_app_icon);
    }
}
