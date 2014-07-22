package org.vosie.wikicards;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class SettingsActivity extends PreferenceActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    FragmentTransaction ft = this.getFragmentManager().beginTransaction();
    ft.replace(android.R.id.content, new SettingsFragment());
    ft.commit();
  }

  public static class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      this.addPreferencesFromResource(R.xml.settings);
    }
  }

}
