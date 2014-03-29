package com.droibit.apps;

/**
 * アプリケーションのインストール先の種類の列挙体<br>
 * <i>DOWNLOAD</i>は端末とSDカードにもインストールされているアプリ
 * 
 * @author kumagaishinya
 *
 */
public enum Applications {
	/** 端末内にインストールされた全てのアプリケーション */
	ALL(0),

	/** 端末のシステム領域に保存されたアプリケーション */
	SYSTEM(1),

	/** ダウンロードした全てのアプリケーション */
	DOWNLOADED(2),

	/** SDカードに移動したアプリケーション */
	ON_SDCARD(3);

	/**
	 * コンストラクタ
	 * 
	 * @param index 列挙子のインデックス
	 */
	private Applications(int index) {
		this.index = index;
	}

	private final int index;

	/**
	 * 列挙子のインデックスを取得する
	 * 
	 * @return 列挙子のインデックス
	 */
	public int getIndex() {
		return index;
	}

	/**
	 * インデックスから列挙体を作成する
	 * 
	 * @param index 列挙子のインデックス
	 * @return インデックスに対応する列挙体
	 */
	public static Applications from(int index) {
		switch (index) {
		case 0:
			return ALL;
		case 1:
			return SYSTEM;
		case 2:
			return DOWNLOADED;
		case 3:
			return ON_SDCARD;
		default:
			throw new IllegalArgumentException();
		}
	}
}
