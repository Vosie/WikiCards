package org.vosie.wikicards.utils;

import java.util.Locale;

import junit.framework.TestCase;

import org.vosie.wikicards.utils.LanguageUtils;

public class LangageUtilsTest extends TestCase {

  public void testGetLocaleByLangCode() {
    assertEquals(Locale.CHINESE.getLanguage(),
            LanguageUtils.getLocaleByLangCode("zh").getLanguage());
  }

  public void testGetLocalesByLangCodes() {
    Locale[] results = LanguageUtils.getLocalesByLangCodes(
            new String[] { "zh", "en" });
    assertEquals(Locale.CHINESE.getLanguage(), results[0].getLanguage());
    assertEquals(Locale.ENGLISH.getLanguage(), results[1].getLanguage());
  }

  public void testGetLocalizedLanguageName() {
    assertEquals(Locale.CHINESE.getDisplayLanguage(),
            LanguageUtils.getLocalizedLanguageName("zh"));
  }

  public void testGetLocalizedLanguageNames() {
    String[] results = LanguageUtils.getLocalizedLanguageNames(
            new String[] { "zh", "en" });
    assertEquals(Locale.CHINESE.getDisplayLanguage(), results[0]);
    assertEquals(Locale.ENGLISH.getDisplayLanguage(), results[1]);
  }

  public void testGetDefaultLangCode() {
    Locale.setDefault(Locale.CHINESE);
    assertEquals("zh", LanguageUtils.getDefaultLangCode());
  }

}
