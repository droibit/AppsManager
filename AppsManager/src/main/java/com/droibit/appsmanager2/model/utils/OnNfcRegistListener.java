package com.droibit.appsmanager2.model.utils;

/**
 * {@link com.droibit.nfc.NfcHandler}の登録・解除の切り替えを行うためのイベントリスナ
 *
 * @author kumagai
 * @since 2014/03/29.
 */
public interface OnNfcRegistListener {

    /**
     * {@link com.droibit.nfc.NfcHandler}の登録・解除の切り替える際に呼ばれる処理
     *
     * @param toggle 切り替えフラグ
     */
    public void onToggle(boolean toggle);
}
