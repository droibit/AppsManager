package com.droibit.apps.utils;

import android.content.pm.ApplicationInfo;

import com.droibit.apps.Applications;

import static android.content.pm.ApplicationInfo.FLAG_SYSTEM;

/**
 * インストールされているアプリケーションのフィルタを作成するためのファクトリ
 *
 * @author kumagai
 */
public class FilterFactory {

	/**
	 * {@link IFilterable}を作成する
     *
	 * @param appType フィルタリングするアプリ
	 * @return {@link IFilterable}オブジェクト
	 */
	public static final IFilterable get(Applications appType) {
		switch (appType) {
		case ALL:
			return new AllApplicationFilter();
		case SYSTEM:
			return new SystemApplicationFilter();
		case DOWNLOADED:
			return new DownloadedApplicationFilter();
		case ON_SDCARD:
			return new OnSdcardApplicationFilter();
		default:
			throw new IllegalArgumentException();
		}
	}

	/**
	 * 全てのアプリケーションを取得するためのフィルタ
     *
	 * @author kumagai
	 *
	 */
	public static class AllApplicationFilter implements IFilterable {
		/** {@inheritDoc} */
		@Override
		public boolean filter(ApplicationInfo appInfo) {
			return true;
		}
	}

	/**
	 * システムにインストールされているアプリケーションのみ取得するためのフィルタ
     *
     *
	 * @author kumagai
	 *
	 */
	public static class SystemApplicationFilter implements IFilterable {
		/** {@inheritDoc} */
		@Override
		public boolean filter(ApplicationInfo appInfo) {
			return (appInfo.flags & FLAG_SYSTEM) == FLAG_SYSTEM;
		}
	}
	
	/**
	 * ダウンロードされたアプリケーションのみ取得するためのフィルタ
     *
	 * @author kumagai
	 *
	 */
	public static class DownloadedApplicationFilter implements IFilterable {
		/** {@inheritDoc} */
		@Override
		public boolean filter(ApplicationInfo appInfo) {
			// TODO SDカードアプリを除外
			return (appInfo.flags & FLAG_SYSTEM) != FLAG_SYSTEM;
		}
	}
	
	/**
	 * SDカードにインストールされているアプリケーションのみ取得するためのフィルタ
     *
	 * @author kumagai
	 *
	 */
	public static class OnSdcardApplicationFilter implements IFilterable {
		/** {@inheritDoc} */
		@Override
		public boolean filter(ApplicationInfo appInfo) {
			// TODO
			return false;
		}
	}
}
