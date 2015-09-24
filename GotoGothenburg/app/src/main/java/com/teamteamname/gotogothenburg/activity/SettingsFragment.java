package com.teamteamname.gotogothenburg.activity;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.teamteamname.gotogothenburg.R;

import java.util.prefs.Preferences;

/**
 * The settings tab fragment.
 * Displays settings through the use of android supplied functionality.
 * TODO If time, consider creating a solution which is not dependant on the android framework for portability
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);
        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);
        initSummaries(getPreferenceScreen());
    }

    // PreferenceScreen is a tree structure
    private void initSummaries(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummaries(pGrp.getPreference(i));
            }
        } else {
            updateListSummary(p);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onResume(){
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateListSummary(findPreference(key));
    }

    private void updateListSummary(Preference p) {
        if ( p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            listPref.setSummary(listPref.getEntry());
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
