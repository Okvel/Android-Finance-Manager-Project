package net.lion.okvel.financemanager.fragment;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.lion.okvel.financemanager.R;

public class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.pref_general);
        final SharedPreferences preferences =
                PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        ListPreference dateTypeList = (ListPreference) findPreference(getString(R.string.settings_start_type_key));
        dateTypeList.setSummary(preferences.getString(getString(R.string.settings_start_type_key),
                getString(R.string.start_type_default)));
        dateTypeList.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString());
                Editor editor = preferences.edit();
                editor.putString(preference.getKey(), preference.getSummary().toString()).apply();
                return true;
            }
        });
    }
}
