package org.vosie.wikicards;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

import com.google.analytics.tracking.android.EasyTracker;

public class SettingsActivity extends PreferenceActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FragmentTransaction ft = this.getFragmentManager().beginTransaction();
    ft.replace(android.R.id.content, new SettingsFragment());
    ft.commit();
  }

  @Override
  protected void onStart() {
    super.onStart();
    EasyTracker.getInstance(this).activityStart(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EasyTracker.getInstance(this).activityStop(this);
  }

  public static class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      this.addPreferencesFromResource(R.xml.settings);
    }
  }

}
