package com.droibit.apps;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.graphics.drawable.Drawable;

/**
 * アプリケーション名とアイコンを格納するクラス
 *
 * @author kumagai
 * 
 */
public class AppSnippet {
    /** アプリケーション名 */
	public String name;

    /** アプリケーションアイコン */
	public Drawable icon;

	/**
	 * 新しいインスタンスを作成する
	 */
	public AppSnippet() {
		this("", null);
	}

	/**
	 * 新しいインスタンスを作成する
     *
	 * @param name アプリケーション名
	 * @param icon アプリケーションアイコン
	 */
	public AppSnippet(String name, Drawable icon) {
		this.name = name;
		this.icon = icon;
	}

	/**
	 * 新しいインスタンスを作成する
     *
	 * @param context コンテキスト
	 * @param appInfo アプリケーション情報
	 */
	public AppSnippet(Context context, ApplicationInfo appInfo) {
		this.name = context.getString(appInfo.labelRes);
		try {
			this.icon = context.getPackageManager()
					.getResourcesForApplication(appInfo)
                    .getDrawable(appInfo.icon);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public String toString() {
		return name;
	}
}