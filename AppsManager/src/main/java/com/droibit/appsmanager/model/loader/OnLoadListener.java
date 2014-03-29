package com.droibit.appsmanager.model.loader;

/**
 * Created by kumagai on 2014/03/28.
 */
public interface OnLoadListener {

    /**
     * ロードを開始する前に呼ばれる処理
     */
    void onPreLoad();

    /**
     * ロードを終了後に呼ばれる処理
     */
    void onPostLoad();
}
