package com.droibit.apps.utils;

import com.droibit.apps.AppEntry;

import java.text.Collator;
import java.util.Comparator;

/**
 * アプリケーションをソートするための{@link Comparator}を作成するファクトリ
 *
 * @author kumagai
 *
 */
public class SorterFactory {

	private static final Collator collator = Collator.getInstance();
	
	
	/**
	 * アプリケーションをソートするための{@link Comparator}を作成する
     *
	 * @param sortType ソートの種類
	 * @return {@link Comparator}オブジェクト
	 */
	public static final Comparator<AppEntry> get(SortTypes sortType) {
		switch (sortType) {
		case NAME_ASC:
			return new ApplicationNameAscSorter();
		case NAME_DESC:
			return new ApplicationNameDescSorter();
		case DATE_ASC:
			return new ApplicationDateAscSorter();
		case DATE_DESC:
			return new ApplicationDateDescSorter();
		case SIZE_ASC:
			return new ApplicationSizeAscSorter();
		case SIZE_DESC:
			return new ApplicationSizeDescSorter();
		default:
			throw new IllegalArgumentException();
		}
	}
	
	/**
	 * 
	 * @author kumagai
	 *
	 */
	public static class ApplicationNameAscSorter implements Comparator<AppEntry> {
		@Override
		public int compare(AppEntry lhs, AppEntry rhs) {
			return collator.compare(lhs.getLabel(), rhs.getLabel());
		}
	}
	
	/**
	 * 
	 * @author kumagai
	 *
	 */
	public static class ApplicationNameDescSorter implements Comparator<AppEntry> {
		@Override
		public int compare(AppEntry lhs, AppEntry rhs) {
			return collator.compare(rhs.getLabel(), lhs.getLabel());
		}
	}
	
	/**
	 * 
	 * @author kumagai
	 *
	 */
	public static class ApplicationDateAscSorter implements Comparator<AppEntry> {
		@Override
		public int compare(AppEntry lhs, AppEntry rhs) {
			return lhs.getLastUpdatelTime().compareTo(rhs.getLastUpdatelTime());
		}
	}
	
	/**
	 * 
	 * @author kumagai
	 *
	 */
	public static class ApplicationDateDescSorter implements Comparator<AppEntry> {
		@Override
		public int compare(AppEntry lhs, AppEntry rhs) {
			return rhs.getLastUpdatelTime().compareTo(lhs.getLastUpdatelTime());
		}
	}
	
	/**
	 * 
	 * @author kumagai
	 *
	 */
	public static class ApplicationSizeAscSorter implements Comparator<AppEntry> {
		@Override
		public int compare(AppEntry lhs, AppEntry rhs) {
			if (lhs.getAppSize() < rhs.getAppSize()) {
				return -1;
			} else if (lhs.getAppSize() > rhs.getAppSize()) {
				return 1;
			}
			return collator.compare(lhs.getLabel(), rhs.getLabel());
		}
	}
	
	/**
	 * 
	 * @author kumagai
	 *
	 */
	public static class ApplicationSizeDescSorter implements Comparator<AppEntry> {
		@Override
		public int compare(AppEntry lhs, AppEntry rhs) {
			if (lhs.getAppSize() < rhs.getAppSize()) {
				return 1;
			} else if (lhs.getAppSize() > rhs.getAppSize()) {
				return -1;
			}
			return collator.compare(lhs.getLabel(), rhs.getLabel());
		}
	}
}
