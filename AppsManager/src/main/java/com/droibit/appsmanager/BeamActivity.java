package com.droibit.appsmanager;

import static android.nfc.NfcAdapter.EXTRA_NDEF_MESSAGES;
import android.app.ListActivity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.droibit.content.IntentHepler;
import com.droibit.nfc.NfcHandler;
import com.droibit.utils.Debug;

/**
 * AndroidBeam(NFC)でアプリケーション情報を受け取った際に表示するアクティビティ
 *
 * @author kumagai
 * 
 */
public class BeamActivity extends ListActivity {

	/** {@inheritDoc} */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		final ArrayAdapter<ListItem> adapter = new ArrayAdapter<ListItem>(this,
				android.R.layout.simple_list_item_1);
		setListAdapter(adapter);
	}

	/** {@inheritDoc} */
	@Override
	public void onResume() {
		super.onResume();

		final Intent intent = getIntent();
		if (NfcHandler.isDiscoveredNdef(intent)) {
			processIntent(intent);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void onNewIntent(Intent intent) {
		// onResume gets called after this to handle the intent
		setIntent(intent);
	}

	@SuppressWarnings("unchecked")
	private void processIntent(Intent intent) {
		final Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(EXTRA_NDEF_MESSAGES);

		final NdefMessage ndfMsg = (NdefMessage) rawMsgs[0];
		final NdefRecord[] ndfRecords = ndfMsg.getRecords();

		Debug.log("launch beam");
		// 共有するアプリが1つの場合はGooglePlayでアプリのページを開きます
		if (ndfRecords.length == 1) {
			IntentHepler.sendGooglePlay(this,
                    new String(ndfRecords[0].getPayload()).substring(3));
			finish();
		}
		
		final ArrayAdapter<ListItem> adapter = (ArrayAdapter<ListItem>)getListAdapter();
		for (int i = 0, len = ndfRecords.length; i < len; i++) {
			// 1レコードが「アプリ名,パッケージ名」になっているので分割して使用する
			final String line = new String(ndfRecords[i].getPayload()).substring(3);
			adapter.add(new ListItem(line.split(",")));
		}
		adapter.notifyDataSetChanged();
	}
	
	/** {@inheritDoc} */
    protected void onListItemClick(ListView l, View v, int position, long id) {
    		final ListItem item = (ListItem) getListAdapter().getItem(position);
    		IntentHepler.sendGooglePlay(this, item.packageName);
    }

    /**
     * NFCで受け取ったアプリ情報を格納するクラス
     */
    private class ListItem {
    		String appName;
    		String packageName;
    		
    		public ListItem(String[] appInfo) {
    			this.appName = appInfo[0];
    			this.packageName = appInfo[1];
    		}
    		
    		@Override
    		public String toString() {
    			return appName;
    		}
    }
}
