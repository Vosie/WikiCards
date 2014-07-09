package org.vosie.wikicards.utils;

import java.util.Locale;

public class LanguageUtils {
	public static Locale getLocaleByLangCode(String langCode) {
		return new Locale(langCode);
	}

	public static Locale[] getLocalesByLangCodes(String[] langCodes) {
		Locale[] ret = new Locale[langCodes.length];
		for (int i = 0; i < langCodes.length; i++) {
			ret[i] = getLocaleByLangCode(langCodes[i]);
		}
		return ret;
	}

	public static String getLocalizedLanguageName(String langCode) {
		return getLocaleByLangCode(langCode).getDisplayLanguage();
	}

	public static String[] getLocalizedLanguageNames(String[] langCodes) {
		String[] ret = new String[langCodes.length];
		for (int i = 0; i < langCodes.length; i++) {
			ret[i] = getLocaleByLangCode(langCodes[i]).getDisplayLanguage();
		}
		return ret;
	}
}
