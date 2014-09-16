package org.vosie.wikicards.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DatabaseUtils {
  public static boolean checkTableExist(SQLiteDatabase db, String tableName) {
    Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' " +
            "AND name='" + tableName + "';", null);
    try {
      return c.getCount() > 0;
    } finally {
      c.close();
    }
  }

  public static String getDBName(String langCode, int category) {
    return langCode + "-" + category + ".sqlite3";
  }
}
