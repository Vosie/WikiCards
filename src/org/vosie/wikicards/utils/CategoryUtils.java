package org.vosie.wikicards.utils;

import org.vosie.wikicards.R;

import android.content.Context;

public class CategoryUtils {
  public static final int CATEGORY_COUNTRY = 1;
  public static final int CATEGORY_ANIMAL = 2;

  public static final int[] CATEGORY_NAMES_ID = new int[] {
          R.string.category_label_1,
          R.string.category_label_2,
  };

  private static final String[] CATEGORY_NAMES = new String[] {
          "country",
          "animals"
  };

  public static String getName(Context cxt, int category) {    
    return (category > 0 && category < CATEGORY_NAMES_ID.length + 1) ? 
            cxt.getString(CATEGORY_NAMES_ID[category - 1]) : null;
  }

  public static String getResourceName(int category) {
    return (category > 0 && category < CATEGORY_NAMES_ID.length + 1) ? 
            CATEGORY_NAMES[category - 1] : null;
  }
}
