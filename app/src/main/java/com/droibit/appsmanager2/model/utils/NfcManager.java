package com.droibit.appsmanager2.model.utils;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;

import com.droibit.nfc.NfcHandler;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * NFCイベントを管理するためのクラス
 *
 * @author kumagai
 * @since 2014/03/29.
 */
public class NfcManager implements OnNfcRegisterListener {

    /**
     * {@link android.nfc.NdefMessage}の作成を以上するためのインターフェース
     */
    public interface INdefMessageDelegate {

        /**
         * NFCで送信するための{@link NdefMessage}を作成する
         *
         * @return {@link NdefMessage}オブジェクト
         */
        NdefMessage createNdefMessage();
    }

    /**
     * {@link INdefMessageDelegate}を受け取るためのコールバックインターフェース
     */
    public interface NdefMessageDelegateCallback {

        /**
         * {@link INdefMessageDelegate}を保持する
         *
         * @param delegate {@link INdefMessageDelegate}オブジェクト
         */
        void receiveNdefMessageDelegate(INdefMessageDelegate delegate);
    }

    private static final String LOCAL_EN = "en";
    private static final String MIME_TYPE = "application/com.droibit.appsmanager";

    /** アクティビティ */
    private final Activity activity;

    /** NCFを操作するためのクラス */
    private final NfcHandler nfcHandler;

    /**
     * 新しいインスタンスを作成する
     *
     * @param activity
     */
    public NfcManager(Activity activity) {
        this.activity = activity;
        this.nfcHandler = new NfcHandler((NfcHandler.OnNfcEventListener) activity);
    }

    /** {@inheritDoc} */
    @Override
    public void onToggle(boolean toggle) {
        if (toggle) {
            NfcHandler.register(activity, nfcHandler);
        } else {
            NfcHandler.unregister(activity);
        }
    }

    /**
     * アプリ名とパッケージ名から{@link NdefRecord}を作成する
     *
     * @param appName アプリ名
     * @param packageName アプリのパッケージ名
     * @return {@link NdefRecord}オブジェクト
     * @throws UnsupportedEncodingException
     */
    public static final NdefRecord createPackageRecord(String appName, String packageName)
            throws UnsupportedEncodingException {

        final String text = String.format("%s,%s", appName, packageName);
        final byte[] textBytes = text.getBytes();
        final byte[] mimeBytes = MIME_TYPE.getBytes(Charset.forName("US-ASCII"));
        final byte[] langBytes = LOCAL_EN.getBytes("US-ASCII");
        final byte[] payload = new byte[1 + langBytes.length + textBytes.length];

        // set status byte (see NDEF spec for actual bits)
        payload[0] = (byte) langBytes.length;

        // copy langbytes and textbytes into payload
        System.arraycopy(langBytes, 0, payload, 1, langBytes.length);
        System.arraycopy(textBytes, 0, payload, 1 + langBytes.length, textBytes.length);

        return new NdefRecord(NdefRecord.TNF_MIME_MEDIA, mimeBytes, null, payload);
    }

    /**
     * NULLオブジェクト。NFCに対応していない場合はこのクラスを使用する
     */
    public static final class NullObject implements OnNfcRegisterListener {
        @Override
        public void onToggle(boolean toggle) {

        }
    }
}
