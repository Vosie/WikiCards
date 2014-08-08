package org.vosie.wikicards;

import java.util.Arrays;
import java.util.Locale;

import org.vosie.wikicards.utils.LanguageUtils;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class MainActivity extends Activity implements Constants {

  private Spinner languageSpinner;
  private Button cardModeButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
  }

  @Override
  protected void onResume() {
    super.onResume();
    // We need to read preference here because user may change the settings in
    // SettingsActivity which goes back to this activity.
    this.initPreferences();
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

  private void initViews() {
    initLangugeSpinner();
    initCardModeButton();
    initDownloadDB();
    initTypeFace();
  }

  private void initTypeFace() {
    Settings.iconFont = Typeface.createFromAsset(getAssets(), "icomoon.ttf");
  }

  private void initDownloadDB() {
    this.findViewById(R.id.button_downloaddb).setOnClickListener(
            new OnClickListener() {

              @Override
              public void onClick(View v) {
                openActivity(MainActivity.this, DownloadDBActivity.class);
              }
            });
  }

  private void initLangugeSpinner() {
    String[] langNames = LanguageUtils.getLocalizedLanguageNames(
            SUPPORTED_LANGUAGES);

    languageSpinner = (Spinner) this.findViewById(R.id.spinner_language);
    languageSpinner.setAdapter(new ArrayAdapter<String>(this,
            R.layout.large_spinner_item, langNames));

    int selectedIdx = Arrays.asList(SUPPORTED_LANGUAGES).indexOf(
            readSelectedLanguageCode());
    // select spinner with the saved language code.
    languageSpinner.setSelection(selectedIdx);
    languageSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

      @Override
      public void onItemSelected(AdapterView<?> arg0, View arg1, int idx,
              long arg3) {
        switchSelectedLanguage(SUPPORTED_LANGUAGES[idx]);
      }

      @Override
      public void onNothingSelected(AdapterView<?> arg0) {
        // Do nothing in this case.
      }
    });
  }

  private void initPreferences() {
    String defLang = LanguageUtils.getDefaultLangCode();

    if (Arrays.asList(SUPPORTED_LANGUAGES).indexOf(defLang) < 0) {
      defLang = SUPPORTED_LANGUAGES[0];
    }
    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    Settings.nativeTongue = sp.getString(PREF_KEY_NATIVE_TONGUE, defLang);
    Settings.contributeRecordings = sp.getBoolean(PREF_KEY_CONTRIBUTE_SND,
            true);
  }

  private void switchSelectedLanguage(String langCode) {
    // save the selected language to preference for future usage.
    SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putString(PREF_KEY_SELECTED_LANGUAGE, langCode);
    editor.commit();
    // update the language code property.
    Settings.selectedLanguageCode = langCode;
  }

  private String readSelectedLanguageCode() {
    String defLang = SUPPORTED_LANGUAGES[0];
    // We choose the second language when the current language is English.
    if (Locale.getDefault().getLanguage().equals(defLang)) {
      defLang = SUPPORTED_LANGUAGES[1];
    }
    SharedPreferences sp = this.getPreferences(Context.MODE_PRIVATE);
    // update the language code property to sync the storage with memory
    // variable.
    Settings.selectedLanguageCode = sp.getString(
            PREF_KEY_SELECTED_LANGUAGE, defLang);
    return Settings.selectedLanguageCode;
  }

  private void initCardModeButton() {
    cardModeButton = (Button) findViewById(R.id.button_cardmode);
    cardModeButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        openActivity(MainActivity.this, CardActivity.class);
      }
    });
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {

    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();
    if (id == R.id.action_settings) {
      openActivity(this, SettingsActivity.class);
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  // If other activities also use this method, we should move it to
  // an utill class.
  private static void openActivity(Context ctx, Class<? extends Activity> cls) {
    ctx.startActivity(new Intent(ctx, cls));
  }
}
