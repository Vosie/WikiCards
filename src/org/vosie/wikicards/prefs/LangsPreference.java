package org.vosie.wikicards.prefs;

import org.vosie.wikicards.Constants;
import org.vosie.wikicards.utils.LanguageUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class LangsPreference extends ListPreference {

  public LangsPreference(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.initOptions();
    this.initValue();
  }

  private void initOptions() {
    String[] langNames = LanguageUtils.getLocalizedLanguageNames(
            Constants.SUPPORTED_LANGUAGES);
    this.setEntries(langNames);
    this.setEntryValues(Constants.SUPPORTED_LANGUAGES);
  }

  private void initValue() {
    if (null == getValue()) {
      setValue(Constants.SUPPORTED_LANGUAGES[0]);
    }
    this.setDefaultValue(getValue());
    this.setSummary(getValue());
  }

  @Override
  protected void onDialogClosed(boolean positiveResult) {
    super.onDialogClosed(positiveResult);
    if (!positiveResult) {
      return;
    }
    this.setSummary(LanguageUtils.getLocalizedLanguageName(this.getValue()));
    this.persistString(getValue());
    SharedPreferences.Editor editor = this.getEditor();
    editor.putString(getKey(), getValue());
    editor.commit();
  }

  @Override
  protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {

    String oldValue;
    if (restoreValue) {
      oldValue = this.getSharedPreferences().getString(getKey(),
              Constants.SUPPORTED_LANGUAGES[0]);
    } else {
      oldValue = defaultValue.toString();
      persistString(oldValue);
    }

    setSummary(LanguageUtils.getLocalizedLanguageName(oldValue));
  }
}
