package com.droibit.apps.utils;

import android.content.pm.ApplicationInfo;

/**
 * 端末のアプリケーションを取得するさいに適用するフィルタ。<br>
 * <br>
 * 実装先ではフィルタリング（アプリケーションを取得）する条件を記述する。<br>
 * フィルタできる種類は{@link com.droibit.apps.Applications}を参照。
 * 
 * @author kumagai
 */
public interface IFilterable {

	/**
	 * アプリケーションをフィルタリングする
	 * 
	 * @param appInfo アプリケーションの情報
	 * @return trueの場合はアプリケーションをフィルタリングする、falseの場合はしない
	 */
	boolean filter(ApplicationInfo appInfo);
}
