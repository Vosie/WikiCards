package org.vosie.wikicards.data;

import java.io.File;
import java.io.FileWriter;

import org.vosie.wikicards.Constants;
import org.vosie.wikicards.MainActivity;
import org.vosie.wikicards.test.mock.ActivityWrapper;
import org.vosie.wikicards.utils.DatabaseUtils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.ResultReceiver;
import android.test.ActivityInstrumentationTestCase2;

public class WordsStorageTest extends ActivityInstrumentationTestCase2<MainActivity> {

  private class TestActivityWrapper extends ActivityWrapper {
    public Intent gotServiceIntent;

    public TestActivityWrapper(Activity act) {
      super(act);
    }

    @Override
    public ComponentName startService(Intent service) {
      gotServiceIntent = service;
      return service.getComponent();
    }
  }

  private class TestDownloadWordListener implements DownloadWordListener {
    public Word got;

    public void onWordReceived(Word word) {
      got = word;
    }

    public void onError(int errorType, Exception e) {
    }
  };

  public WordsStorageTest() {
    super(MainActivity.class);
  }

  public void testGetDBName() {

  }

  public void testWordsStorage() {
    Activity act = this.getActivity();
    // check the base file and delete it before the test.
    File db = act.getDatabasePath(DatabaseUtils.getDBName("base",
            Constants.CATEGORY_COUNTRY));
    if (db.exists()) {
      db.delete();
    }

    new WordsStorage(act, Constants.CATEGORY_COUNTRY);
    assertTrue(db.exists());
    assertTrue(db.length() > 0);
  }

  public void testGetServerIDs() {
    WordsStorage ws = new WordsStorage(this.getActivity(),
            Constants.CATEGORY_COUNTRY);
    String[] ids = ws.getServerIDs();
    assertEquals(249, ids.length);
  }

  public void testGetRowCount() {
    Activity act = this.getActivity();
    File db = act.getDatabasePath(DatabaseUtils.getDBName("en",
            Constants.CATEGORY_COUNTRY));
    // check and delete english database for testing inexisted db.
    if (db.exists()) {
      db.delete();
    }
    WordsStorage ws = new WordsStorage(act, Constants.CATEGORY_COUNTRY);
    assertEquals(249, ws.getRowCount("base"));
    assertEquals(0, ws.getRowCount("en"));
  }

  public void testDeleteDB() {
    Activity act = this.getActivity();
    // check the base file and delete it before the test.
    File db = act.getDatabasePath(DatabaseUtils.getDBName("base",
            Constants.CATEGORY_COUNTRY));

    WordsStorage ws = new WordsStorage(act,
            Constants.CATEGORY_COUNTRY);
    ws.deleteDB("base");
    assertFalse(db.exists());
  }

  public void testGetWordFromLocal() {
    WordsStorage ws = new WordsStorage(this.getActivity(),
            Constants.CATEGORY_COUNTRY);
    Word word = ws.getWordFromLocal("country/TWN", "base");

    assertNotNull(word);
    assertEquals(Constants.CATEGORY_COUNTRY, word.category);
    assertEquals("en", word.languageCode);
    assertEquals("country/TWN", word.serverID);
    assertTrue(null != word.imageURL && !"".equals(word.imageURL));
  }

  public void testGetWordFromServer() throws Exception {
    Activity act = this.getActivity();
    TestActivityWrapper wrapper = new TestActivityWrapper(act);
    WordsStorage ws = new WordsStorage(wrapper, Constants.CATEGORY_COUNTRY);
    TestDownloadWordListener listener = new TestDownloadWordListener();
    // remove handler to invoke callback of resultreceiver immediately.
    ws.setHandler(null);
    ws.getWordFromServer("country/TWN", "zh", listener);

    assertEquals("http://api.vosie.org/wikicards/zh/country/TWN.json",
            wrapper.gotServiceIntent.getStringExtra("url"));
    ResultReceiver rr = (ResultReceiver) wrapper.gotServiceIntent
            .getParcelableExtra("receiver");
    assertNotNull(rr);
    String destination = wrapper.gotServiceIntent.getStringExtra("destination");
    assertNotNull(destination);
    File f = new File(destination);
    FileWriter writer = new FileWriter(f);
    writer.write("{\"label\":\"label\"," +
            "\"languageCode\":\"zh\"," +
            "\"serverID\":\"country/TWN\"," +
            "\"url\":\"url\"," +
            "\"latitude\":\"latitude\"," +
            "\"longitude\":\"longitude\"," +
            "\"imageURL\":\"imageURL\"," +
            "\"shortDesc\":\"shortDesc\"," +
            "\"category\":1}");
    writer.close();
    rr.send(DownloadService.DOWNLOAD_COMPLETE, null);

    assertNotNull(listener.got);
    assertEquals("label", listener.got.label);
    assertEquals("zh", listener.got.languageCode);
    assertEquals("country/TWN", listener.got.serverID);
    assertEquals("url", listener.got.url);
    assertEquals("imageURL", listener.got.imageURL);
    assertEquals("latitude", listener.got.latitude);
    assertEquals("longitude", listener.got.longitude);
    assertEquals("shortDesc", listener.got.shortDesc);
    assertEquals(1, listener.got.category);

    File db = act.getDatabasePath(DatabaseUtils.getDBName("zh",
            Constants.CATEGORY_COUNTRY));
    assertTrue(db.exists());
    assertEquals(1, ws.getRowCount("zh"));
    db.delete();

  }

  public void testDownloadDB() {
    TestActivityWrapper wrapper = new TestActivityWrapper(getActivity());
    WordsStorage ws = new WordsStorage(wrapper, Constants.CATEGORY_COUNTRY);
    ws.downloadDB("zh", new DownloadDBListener() {
      @Override
      public void onProgressing(int progress) {
      }

      @Override
      public void onComplete() {
      }

      @Override
      public void onError(int errorType, Exception e) {
      }
    });
    assertEquals("http://api.vosie.org/wikicards/zh/country/zh.sqlite3",
            wrapper.gotServiceIntent.getStringExtra("url"));
    assertNotNull(wrapper.gotServiceIntent.getParcelableExtra("receiver"));
    assertNotNull(wrapper.gotServiceIntent.getStringExtra("destination"));
  }
}
