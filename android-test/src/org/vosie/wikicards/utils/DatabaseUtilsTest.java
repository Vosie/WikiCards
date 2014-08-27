package org.vosie.wikicards.utils;

import org.vosie.wikicards.MainActivity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

public class DatabaseUtilsTest extends ActivityInstrumentationTestCase2<MainActivity> {

  private static final String DB_NAME = "test_db";

  public DatabaseUtilsTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    getActivity().deleteDatabase(DB_NAME);
  }

  public void testCheckTableExist() {
    String testTableName = "test";
    String create_table = "CREATE TABLE " + testTableName + " ("
            + "_id     TEXT PRIMARY KEY"
            + ")";

    SQLiteDatabase db = getActivity().openOrCreateDatabase(
            DB_NAME,
            Context.MODE_PRIVATE,
            null);
    
    
    assertFalse(DatabaseUtils.checkTableExist(db, testTableName));
    
    db.execSQL(create_table);
    
    assertTrue(DatabaseUtils.checkTableExist(db, testTableName));
    
    db.close();
  }

}
