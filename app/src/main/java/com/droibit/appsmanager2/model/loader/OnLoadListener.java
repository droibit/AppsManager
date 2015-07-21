package com.droibit.appsmanager2.model.loader;

/**
 * {@link com.droibit.appsmanager2.model.loader.AppListLoaderCallbacks}内でロード開始と終了時に呼ばれるイベントリスナ
 *
 * @author kumagai
 * @since 2014/03/28.
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
