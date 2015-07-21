package com.droibit.appsmanager2;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

import com.droibit.content.IntentHepler;
import com.droibit.content.IntentMailObject;

/**
 * アプリケーションの設定を表示するアクティビティ
 *
 * @author kumagai
 * @since 2014/03/27.
 */
public class SettingsActivity extends ActionBarActivity {

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

    /** {@inheritDoc} */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * アプリケーションの設定を行うためのフラグメント
     */
    public static class SettingsFragment extends PreferenceFragment {

        /** {@inheritDoc} */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);

            final Preference pref = findPreference(getText(R.string.pref_contact_title));
            pref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    sendContactMail();
                    return true;
                }
            });
        }

        private void sendContactMail() {
            final IntentMailObject mail = new IntentMailObject();
            mail.title = getString(R.string.mail_subject);
            mail.address = getString(R.string.mail_address);
            IntentHepler.sendMail(getActivity(), mail);
        }
    }
}