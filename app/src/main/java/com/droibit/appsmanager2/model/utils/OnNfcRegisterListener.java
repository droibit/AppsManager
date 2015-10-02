package com.droibit.appsmanager2.model.utils;

/**
 * {@link com.droibit.nfc.NfcHandler}の登録・解除の切り替えを行うためのイベントリスナ
 *
 * @author kumagai
 */
public interface OnNfcRegisterListener {

    /**
     * {@link com.droibit.nfc.NfcHandler}の登録・解除の切り替える際に呼ばれる処理
     *
     * @param toggle 切り替えフラグ
     */
    void onToggle(boolean toggle);
}
