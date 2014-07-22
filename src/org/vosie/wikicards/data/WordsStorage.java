package org.vosie.wikicards.data;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import org.apache.commons.io.IOUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.vosie.wikicards.utils.JSONUtils;
import org.vosie.wikicards.utils.NetworkUtils;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

public class WordsStorage {

  protected static final String TAG = "WordsStorage";

  protected static final String CREATE_TABLE = "CREATE TABLE words ("
          + "_id          INTEGER PRIMARY KEY AUTOINCREMENT,"
          + "label        TEXT    NOT NULL,"
          + "languageCode TEXT    NOT NULL,"
          + "serverID     TEXT    NOT NULL,"
          + "url          TEXT    NOT NULL,"
          + "latitude     TEXT,"
          + "longtitude   TEXT,"
          + "imageURL     TEXT,"
          + "shortDesc    TEXT,"
          + "category     INTEGER DEFAULT ( 0 ))";

  protected static final String CREATE_INDEX = "CREATE UNIQUE INDEX IF NOT " +
          "EXISTS serverIDIndex ON words(serverID)";

  private Context mContext;

  public WordsStorage(Context context) {
    mContext = context;

    if (!checkBaseDBExist()) {
      mContext.openOrCreateDatabase("base.sqlite3", Context.MODE_PRIVATE, null);

      InputStream input = null;
      OutputStream output = null;
      try {
        input = mContext.getAssets().open("base.sqlite3");
        output = new FileOutputStream(mContext.getDatabasePath("base.sqlite3"));
        IOUtils.copy(input, output);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        IOUtils.closeQuietly(input);
        IOUtils.closeQuietly(output);
      }
    }
  }

  private boolean checkBaseDBExist() {
    return mContext.getDatabasePath("base.sqlite3").isFile();
  }

  public String[] getServerIDs(int CategoryID) {

    ArrayList<String> serverIDs = new ArrayList<String>();

    SQLiteDatabase db = mContext.openOrCreateDatabase("base.sqlite3",
            Context.MODE_PRIVATE, null);

    Cursor cursor = db.rawQuery("select serverID from words", null);

    while (cursor.moveToNext()) {
      serverIDs.add(cursor.getString(0));
    }

    return serverIDs.toArray(new String[serverIDs.size()]);
  }

  public Word getWordFromLocal(String serverID, String langCode) {

    SQLiteDatabase db = openOrCreateWordsDatebase(langCode);

    Cursor cursor = db.rawQuery("select * from words where serverID = ?",
            new String[] { serverID });

    Word word = null;
    if (cursor.moveToFirst()) {
      word = new Word();
      word.serverID = cursor.getString(cursor.getColumnIndex("serverID"));
      word.label = cursor.getString(cursor.getColumnIndex("label"));
      word.languageCode = cursor.getString(cursor.getColumnIndex("languageCode"));
      word.url = cursor.getString(cursor.getColumnIndex("url"));
      word.latitude = cursor.getString(cursor.getColumnIndex("latitude"));
      word.longitude = cursor.getString(cursor.getColumnIndex("longtitude"));
      word.imageURL = cursor.getString(cursor.getColumnIndex("imageURL"));
      word.shortDesc = cursor.getString(cursor.getColumnIndex("shortDesc"));
      word.category = cursor.getInt(cursor.getColumnIndex("category"));
    }
    cursor.close();
    return word;
  }

  private SQLiteDatabase openOrCreateWordsDatebase(String langCode) {
    SQLiteDatabase db = mContext.openOrCreateDatabase(langCode + ".sqlite3",
            Context.MODE_PRIVATE, null);

    if (!checkTableExist(db, "words")) {
      db.execSQL(CREATE_TABLE);
      db.execSQL(CREATE_INDEX);
    }

    return db;
  }

  private boolean checkTableExist(SQLiteDatabase db, String tableName) {
    Cursor c = db.rawQuery("SELECT * FROM sqlite_master WHERE type='table' " +
            "AND name='" + tableName + "';", null);
    boolean exist = c.getCount() > 0;
    c.close();
    return exist;
  }

  public void getWordFromServer(String serverID, String langCode,
          DownloadWordListener listener) {

    if (!NetworkUtils.isNetworkAvailable(mContext)) {
      listener.onError(DownloadWordListener.NETWORK_ERROR,
              new Exception("network is not available"));
      return;
    }

    String tempFilePath = mContext.getCacheDir().getPath() + "/" +
            serverID.split("/")[1] + ".json";

    WordReceiver receiver = new WordReceiver(new Handler(), tempFilePath);
    receiver.setAutoInsert(true);
    receiver.setWordStorageListener(listener);

    Intent intent = new Intent(mContext, DownloadService.class);
    intent.putExtra("url", "http://api.vosie.org/wikicards/" + langCode + "/" +
            serverID + ".json");
    intent.putExtra("receiver", receiver);
    intent.putExtra("destination", tempFilePath);
    mContext.startService(intent);

  }

  private class WordReceiver extends ResultReceiver {

    DownloadWordListener mListener = null;
    private String mTempFilePath;
    private boolean mAutoInsert = false;

    public WordReceiver(Handler handler, String tempFilePath) {
      super(handler);
      mTempFilePath = tempFilePath;
    }

    public void setAutoInsert(boolean auto) {
      mAutoInsert = auto;
    }

    public void setWordStorageListener(DownloadWordListener listener) {
      mListener = listener;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
      super.onReceiveResult(resultCode, resultData);

      switch (resultCode) {
        case DownloadService.DOWNLOAD_COMPLETE:
          Word word = getWordFromFilePath(mTempFilePath);

          if (mAutoInsert && word != null) {
            insertWordToLocal(word);
          }

          if (mListener != null && word != null) {
            mListener.onWordReceived(word);
          }
          break;

        case DownloadService.HTTP_ERROR:
          if (mListener != null) {
            mListener.onError(DownloadWordListener.HTTP_ERROR,
                    (Exception) resultData
                            .getSerializable(DownloadService.PARAM_EXCEPTION));
          }
          break;

        case DownloadService.INTERNAL_ERROR:
          if (mListener != null) {
            mListener.onError(DownloadWordListener.INTERNAL_ERROR,
                    (Exception) resultData
                            .getSerializable(DownloadService.PARAM_EXCEPTION));
          }
          break;
      }
    }

    private Word getWordFromFilePath(String path) {
      try {
        JSONObject jsonWord = JSONUtils.loadJSONObjectFromFile(path);
        Word word = new Word();
        word.serverID = jsonWord.getString("serverID");
        word.label = jsonWord.getString("label");
        word.languageCode = jsonWord.getString("languageCode");
        word.url = jsonWord.getString("url");
        word.latitude = jsonWord.getString("latitude");
        word.longitude = jsonWord.getString("longitude");
        word.imageURL = jsonWord.getString("imageURL");
        word.shortDesc = jsonWord.getString("shortDesc");
        word.category = jsonWord.getInt("category");
        return word;
      } catch (JSONException | IOException e) {
        if (mListener != null) {
          mListener.onError(DownloadWordListener.INTERNAL_ERROR, e);
        }
        e.printStackTrace();
      }
      return null;
    }
  }

  private void insertWordToLocal(Word word) {
    SQLiteDatabase db = openOrCreateWordsDatebase(word.languageCode);

    ContentValues values = new ContentValues();
    values.put("serverID", word.serverID);
    values.put("label", word.label);
    values.put("languageCode", word.languageCode);
    values.put("url", word.url);
    values.put("latitude", word.latitude);
    values.put("longtitude", word.longitude);
    values.put("imageURL", word.imageURL);
    values.put("shortDesc", word.shortDesc);
    values.put("category", word.category);

    db.insertWithOnConflict("words", null, values,
            SQLiteDatabase.CONFLICT_REPLACE);
  }

  public void downloadDB(String langCode, int categoryID,
          final DownloadDBListener listener) {

    if (!NetworkUtils.isNetworkAvailable(mContext)) {
      listener.onError(DownloadDBListener.NETWORK_ERROR,
              new Exception("network is not available"));
      return;
    }

    String DBName = langCode + ".sqlite3";

    Intent intent = new Intent(mContext, DownloadService.class);
    intent.putExtra("url", "http://api.vosie.org/wikicards/" + langCode + "/" +
            getCategoryName(categoryID) + "/" + DBName);

    intent.putExtra("receiver", new ResultReceiver(new Handler()) {
      @Override
      protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);

        if (listener == null) {
          return;
        }

        switch (resultCode) {

          case DownloadService.UPDATE_PROGRESS:
            listener.onProgressing(resultData
                    .getInt(DownloadService.PARAM_PROGRESS));
            break;

          case DownloadService.DOWNLOAD_COMPLETE:
            listener.onComplete();
            break;

          case DownloadService.HTTP_ERROR:
            listener.onError(DownloadDBListener.HTTP_ERROR,
                    (Exception) resultData
                            .getSerializable(DownloadService.PARAM_EXCEPTION));
            break;

          case DownloadService.INTERNAL_ERROR:
            listener.onError(DownloadDBListener.INTERNAL_ERROR,
                    (Exception) resultData
                            .getSerializable(DownloadService.PARAM_EXCEPTION));
            break;
        }
      }

    });

    intent.putExtra("destination", mContext.getDatabasePath(DBName).getPath());
    mContext.startService(intent);

  }

  private String getCategoryName(int id) {
    return id == 0 ? "country" : "";
  }
}
