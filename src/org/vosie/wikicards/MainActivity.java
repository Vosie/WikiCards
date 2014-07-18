package org.vosie.wikicards;

import java.util.Arrays;
import java.util.Locale;

import org.vosie.wikicards.utils.LanguageUtils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class MainActivity extends Activity implements Constants {

  private Spinner languageSpinner;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    initViews();
  }

  private void initViews() {
    initLangugeSpinner();
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
      return true;
    }
    return super.onOptionsItemSelected(item);
  }
}
