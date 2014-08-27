package org.vosie.wikicards.data;

import org.vosie.wikicards.Constants;
import org.vosie.wikicards.MainActivity;

import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;

public class StarredWordsStorageTest extends
                             ActivityInstrumentationTestCase2<MainActivity> {

  public StarredWordsStorageTest() {
    super(MainActivity.class);
  }

  private WordsStorage mWordsStorage;
  private StarredWordsStorage mStarredWordsStorage;

  private void clearStarred(){
    SQLiteDatabase db = mStarredWordsStorage.openOrCreateStarredWordsDatebase();
    try {
      db.execSQL("delete from starred");
    } finally {
      db.close();
    }
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    mWordsStorage = new WordsStorage(getActivity(), Constants.CATEGORY_COUNTRY);
    mStarredWordsStorage = new StarredWordsStorage(getActivity(), Constants.CATEGORY_COUNTRY);
    clearStarred();
  }

  public void testStarWord() {
    String serverIDs[] = mWordsStorage.getServerIDs();
    mStarredWordsStorage.starWord(serverIDs[0]);
    mStarredWordsStorage.starWord(serverIDs[1]);
    String starredIDs[] = mStarredWordsStorage.getStarredWordIDs();

    assertEquals(2, starredIDs.length);
    assertEquals(serverIDs[0], starredIDs[0]);
    assertEquals(serverIDs[1], starredIDs[1]);
  }

  public void testUnStarWord() {
    String serverIDs[] = mWordsStorage.getServerIDs();
    mStarredWordsStorage.starWord(serverIDs[0]);
    mStarredWordsStorage.unStarWord(serverIDs[0]);
    String starredIDs[] = mStarredWordsStorage.getStarredWordIDs();
    
    assertEquals(0, starredIDs.length);
  }

  public void testIsStarred(){
    String serverIDs[] = mWordsStorage.getServerIDs();
    mStarredWordsStorage.starWord(serverIDs[0]);
    
    assertTrue(mStarredWordsStorage.isStarred(serverIDs[0]));
  }
}
