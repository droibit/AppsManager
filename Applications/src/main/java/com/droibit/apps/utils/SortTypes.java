package com.droibit.apps.utils;

/**
 * アプリケーションをソートの種類の列挙体。<br>
 * <br>
 * 名前、更新日、サイズ、キャッシュでソートが可能。
 * 
 * @author kumagai
 *
 */
public enum SortTypes {
	
	/** アプリケーション名で昇順 */
	NAME_ASC(0),
	
	/** アプリケーション名で降順 */
	NAME_DESC(1),
	
	/**アプリケーションの更新日で昇順  */
	DATE_ASC(2),
	
	/** アプリケーションの更新日で降順 */
	DATE_DESC(3),
	
	/** アプリケーションのサイズで昇順 */
	SIZE_ASC(4),
	
	/** アプリケーションのサイズで降順 */
	SIZE_DESC(5),
	
	/** アプリケーションのキャッシュサイズで昇順 */
	CACHE_ASC(6),
	
	/** アプリケーションのキャッシュサイズで降順*/
	CAHCHE_DESC(7);

	/**
	 * コンストラクタ
	 * 
	 * @param index 列挙子のインデックス
	 */
	private SortTypes(int index) {
		this.index = index;
	}
	private int index;

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
	public static SortTypes from(int index) {
		switch (index) {
		case 0:
			return NAME_ASC;
		case 1:
			return NAME_DESC;
		case 2:
			return DATE_ASC;
		case 3:
			return DATE_DESC;
		case 4:
			return SIZE_ASC;
		case 5:
			return SIZE_DESC;
		case 6:
			return CACHE_ASC;
		case 7:
			return CAHCHE_DESC;
		default:
			throw new IllegalArgumentException();
		}
	}
}
