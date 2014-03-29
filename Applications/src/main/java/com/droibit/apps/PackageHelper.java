package com.droibit.apps;

import java.io.File;
import java.util.Date;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

/**
 * パッケージに関するユーティリティクラス。
 * 
 * @author kumagaishinya
 * 
 */
public final class PackageHelper {

	@SuppressWarnings("unused")
	private static final String TAG = PackageManager.class.getSimpleName();

	/**
	 * コンストラクタ
	 */
	private PackageHelper() {
	}

	/**
	 * アプリケーションの{@link AppSnippet}のを取得する
     *
	 * @param context コンテキスト
	 * @return アプリケーションの{@link AppSnippet}
	 */
	public static AppSnippet getApplicationSnippet(Context context) {
		final ApplicationInfo appInfo = context.getApplicationInfo();
		final AppSnippet snippet = new AppSnippet();

		snippet.name = context.getString(appInfo.labelRes);
		if (TextUtils.isEmpty(snippet.name)) {
			snippet.name = appInfo.packageName;
		}
		snippet.icon = context.getResources().getDrawable(appInfo.icon);

		return snippet;
	}

	/**
	 * 指定したパッケージから{@link AppSnippet}を取得する
     *
	 * @param context コンテキスト
	 * @param packageName パッケージ名
	 * @return 指定したパッケージの{@link AppSnippet}
	 */
	public static final AppSnippet getApplicationSnippet(Context context, String packageName) {
        final ApplicationInfo appInfo = getApplicationInfo(context, packageName);
        if (appInfo != null) {
            return new AppSnippet(context, appInfo);
        }
        return null;
	}

	/**
	 * コンテキストのパッケージ情報を取得する
	 * 
	 * @param context コンテキスト
	 * @return コンテキストのパッケージ情報
	 */
	public static final PackageInfo getPackageInfo(Context context) {
		return getPackageInfo(context, context.getPackageName(), 0);
	}

	/**
	 * 指定されたパッケージのパッケージ情報を取得する
	 * 
	 * @param context コンテキスト
	 * @param packageName パッケージ名
	 * @return 指定されたパッケージのパッケージ情報
	 */
	public static final PackageInfo getPackageInfo(Context context,
			String packageName) {
		return getPackageInfo(context, packageName, 0);
	}

	/**
	 * コンテキストのパッケージ情報を取得する<br>
	 * <br>
	 * オプションフラグについては{@link android.content.pm.PackageManager#getPackageInfo(String, int)}
	 * のflags引数を参照。
	 * 
	 * @param context コンテキスト
	 * @param flags オプションフラグ
	 * @return コンテキストのパッケージ情報
	 */
	public static final PackageInfo getPackageInfo(Context context, int flags) {
		return getPackageInfo(context, context.getPackageName(), flags);
	}

	/**
	 * 指定されたパッケージのパッケージ情報を取得する。<br>
	 * <br>
	 * オプションフラグについては{@link android.content.pm.PackageManager#getPackageInfo(String, int)}
	 * のflags引数を参照。
	 * 
	 * @param context コンテキスト
	 * @param packageName パッケージ名
	 * @param flags オプションフラグ
	 * @return 指定されたパッケージのパッケージ情報
	 */
	public static final PackageInfo getPackageInfo(Context context,
			String packageName, int flags) {
		PackageInfo info = null;
		try {
			info = context.getPackageManager().getPackageInfo(packageName,
					flags);
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		return info;
	}

	/**
	 * パッケージのURIからアプリケーションの情報を取得する。<br>
	 * アプリケーションの情報はマニフェストのアプリケーションタグ内の要素になる。
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションの情報
	 */
	public static ApplicationInfo getApplicationInfo(Context context) {
		final PackageInfo pkgInfo = getPackageInfo(context,
				context.getPackageName());
		return (pkgInfo != null) ? pkgInfo.applicationInfo : null;
	}

	/**
	 * パッケージのURIからアプリケーションの情報を取得する。<br>
	 * アプリケーションの情報はマニフェストのアプリケーションタグ内の要素になる。
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションの情報
	 */
	public static ApplicationInfo getApplicationInfo(Context context,
			String packageName) {
		final PackageInfo pkgInfo = getPackageInfo(context, packageName);
		return (pkgInfo != null) ? pkgInfo.applicationInfo : null;
	}

	/**
	 * アプリケーション名を取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーション名
	 */
	public static final String getApplicationName(Context context) {
		return context.getString(context.getApplicationInfo().labelRes);
	}

	/**
	 * アプリケーションのアイコンを取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションのアイコン
	 */
	public static final Drawable getApplicationIcon(Context context) {
		return context.getResources().getDrawable(
				context.getApplicationInfo().icon);
	}

	/**
	 * アプリケーションアイコンのリソースIDを取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションアイコンのリソースID
	 */
	public static final int getApplicationIconId(Context context) {
		return context.getApplicationInfo().icon;
	}

	/**
	 * アプリケーションのバージョンコードを取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションのバージョンコード
	 */
	public static final int getVersionCode(Context context) {
		return getPackageInfo(context).versionCode;
	}

	/**
	 * アプリケ-ションのバージョン名を取得する
	 * 
	 * @param context コンテキスト
	 * @return アプリケーションのバージョン名
	 */
	public static final String getVersionName(Context context) {
		return getPackageInfo(context).versionName;
	}

	/**
	 * アプリケーションが最初にインストールされた日付を取得する
	 * 
	 * @param context コンテキスト
	 * @param packageName アプリケーションのパッケージ名
	 * @return 最初にインストールされた日付
	 */
	public static final Date getFirstInstallTime(Context context,
			String packageName) {
		return new Date(getPackageInfo(context, packageName).firstInstallTime);
	}

	/**
	 * アプリケーションが更新された日付を取得する
	 * 
	 * @param context コンテキスト
	 * @param packageName アプリケーションのパッケージ名
	 * @return 更新された日付を取得する
	 */
	public static final Date getLastUpdateTime(Context context, String packageName) {
		return new Date(getPackageInfo(context, packageName).lastUpdateTime);
	}

    /**
     * アプリケーションパッケージのサイズ（Byte）を取得する
     *
     * @param context コンテキスト
     * @param packageName パッケージ名
     * @return パッケージサイズ（Byte)
     */
    public static final long getPackageSizeInBytes(Context context, String packageName) {
        final ApplicationInfo appInfo = getApplicationInfo(context, packageName);
        if (appInfo == null) {
            return -1L;
        }

        final File file = new File(appInfo.sourceDir);
        return file.length();
    }
}
