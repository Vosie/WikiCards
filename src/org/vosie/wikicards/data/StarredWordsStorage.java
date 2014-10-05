package org.vosie.wikicards.data;

import java.util.ArrayList;

import org.vosie.wikicards.utils.DatabaseUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class StarredWordsStorage {

  private Context mContext;
  private int mCategory;

  public StarredWordsStorage(Context context, int category) {
    mContext = context;
    mCategory = category;
  }

  public SQLiteDatabase openOrCreateStarredWordsDatebase() {
    String create_starred_table = "CREATE TABLE starred ("
            + "serverID     TEXT PRIMARY KEY"
            + ")";

    SQLiteDatabase db = mContext.openOrCreateDatabase(
            "starred-" + mCategory,
            Context.MODE_PRIVATE,
            null);

    if (!DatabaseUtils.checkTableExist(db, "starred")) {
      db.execSQL(create_starred_table);
    }

    return db;
  }

  public void starWord(String serverID) {
    SQLiteDatabase db = openOrCreateStarredWordsDatebase();
    try {
      ContentValues values = new ContentValues();
      values.put("serverID", serverID);
      db.insertWithOnConflict("starred", null, values,
              SQLiteDatabase.CONFLICT_IGNORE);
    } finally {
      db.close();
    }
  }

  public void unStarWord(String serverID) {
    SQLiteDatabase db = openOrCreateStarredWordsDatebase();
    try {
      db.delete("starred", "serverID = ?", new String[] { serverID });
    } finally {
      db.close();
    }
  }

  public String[] getStarredWordIDs() {
    ArrayList<String> serverIDs = new ArrayList<String>();
    SQLiteDatabase db = openOrCreateStarredWordsDatebase();
    try {
      Cursor cursor = db.rawQuery("select serverID from starred", null);

      while (cursor.moveToNext()) {
        serverIDs.add(cursor.getString(0));
      }

      return serverIDs.toArray(new String[0]);
    } finally {
      db.close();
    }
  }

  public boolean isStarred(String serverID) {
    SQLiteDatabase db = openOrCreateStarredWordsDatebase();
    Cursor cursor;
    try {
      cursor = db.rawQuery("select * from starred where serverID = ?",
              new String[] { serverID });
      try {
        return cursor.moveToFirst();
      } finally {
        cursor.close();
      }
    } finally {
      db.close();
    }
  }
}
