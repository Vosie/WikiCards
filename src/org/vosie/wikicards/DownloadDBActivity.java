package org.vosie.wikicards;

import org.vosie.wikicards.data.DownloadDBListener;
import org.vosie.wikicards.data.WordsStorage;
import org.vosie.wikicards.utils.DialogMenuItem;
import org.vosie.wikicards.utils.DialogMenuListener;
import org.vosie.wikicards.utils.DialogUtils;
import org.vosie.wikicards.utils.ErrorUtils;
import org.vosie.wikicards.utils.IconFontUtils;
import org.vosie.wikicards.utils.LanguageUtils;
import org.vosie.wikicards.utils.NetworkUtils;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class DownloadDBActivity extends ListActivity implements Constants {

  private static final String TAG = "DownloadDBActivity";
  private LangListAdapter adapter;
  private int totalCount;
  private WordsStorage storage;
  private BroadcastReceiver networkNotifier;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    storage = new WordsStorage(this, CATEGORY_COUNTRY);
    totalCount = getRowCount(NEUTRAL_LANG_CODE);
    initAdapter();
    getActionBar().setDisplayHomeAsUpEnabled(true);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (android.R.id.home == item.getItemId()) {
      this.onBackPressed();
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  protected void onStart() {
    super.onStart();
    EasyTracker.getInstance(this).activityStart(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    EasyTracker.getInstance(this).activityStop(this);
  }

  protected void onResume() {
    initNetworkNotifier();
    super.onResume();
  }

  @Override
  protected void onPause() {
    NetworkUtils.get().removeInternetStateNotifier(this, networkNotifier);
    super.onPause();
  }

  private void initNetworkNotifier() {
    networkNotifier = NetworkUtils.get().notifyInternetState(this,
            new Runnable() {
              @Override
              public void run() {
                adapter.notifyDataSetChanged();
              }
            }, new Runnable() {
              @Override
              public void run() {
                adapter.notifyDataSetChanged();
              }
            });
  }

  private int getRowCount(String langCode) {
    return storage.getRowCount(langCode);
  }

  /**
   * The fillData is called by adapter for filling data to ui.
   * 
   * @param ui
   *          the ui which should be R.layout.list_language_row.
   * @param langCode
   *          the language code which may be en, zh, ja.
   */
  private void fillData(View ui, String langCode) {
    boolean hasInternet = NetworkUtils.get().isNetworkAvailable(this);
    String langName = LanguageUtils.getLocalizedLanguageName(langCode);
    int rows = getRowCount(langCode);
    String dbStatus = " - (" + rows + "/" + totalCount + ")";
    ((TextView) ui.findViewById(R.id.label_lang_name)).setText(langName);
    if (0 == rows) {
      // we show download button when no data in db.
      View downloadBtn = ui.findViewById(R.id.button_download);
      downloadBtn.setVisibility(View.VISIBLE);
      // enable download button only when we have internet connection
      downloadBtn.setEnabled(hasInternet);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              getString(R.string.msg_empty_database));
    } else if (rows == totalCount) {
      // we show delete button when db is fully downloaded.
      ui.findViewById(R.id.button_delete).setVisibility(View.VISIBLE);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              getString(R.string.msg_database_downloaded) + dbStatus);
    } else if (hasInternet) {
      // we show option menu which has delete and download when db is partial
      // downloaded.
      ui.findViewById(R.id.button_options).setVisibility(View.VISIBLE);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              getString(R.string.msg_partial_downloaded) + dbStatus);
    } else {
      // only show delete button when we don't have internet connection.
      ui.findViewById(R.id.button_delete).setVisibility(View.VISIBLE);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              getString(R.string.msg_partial_downloaded) + dbStatus);
    }
  }

  /**
   * This shows a progress dialog to show the downloading progress.
   * 
   * @param langCode
   */
  private void downloadDB(String langCode) {
    final ProgressDialog progressDialog = new ProgressDialog(this);
    progressDialog.setTitle(R.string.dialog_title_download_offline_db);
    progressDialog.setMessage(LanguageUtils.getLocalizedLanguageName(langCode));
    progressDialog.setCancelable(false);
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setMax(100);
    progressDialog.show();

    // we use WordsStorage to download the whole db.
    storage.downloadDB(langCode, new DownloadDBListener() {

      @Override
      public void onProgressing(int progress) {
        progressDialog.setProgress(progress);
      }

      @Override
      public void onComplete() {
        progressDialog.dismiss();
        adapter.notifyDataSetChanged();
      }

      @Override
      public void onError(int errorType, Exception e) {
        // Log the error and show error message.
        Log.e(TAG, "error while downloading offline db", e);
        progressDialog.dismiss();
        ErrorUtils.get().handleDownloadkError(DownloadDBActivity.this,
                errorType, false);
      }
    });
  }

  private void deleteDB(final String langCode) {
    String msg = getString(R.string.dialog_desc_confirm_delete_db) + "\n" +
            LanguageUtils.getLocalizedLanguageName(langCode);
    DialogUtils.get().showConfirmDialog(this,
            R.drawable.ic_launcher,
            getString(R.string.dialog_title_confirm_delete_db),
            msg,
            getString(android.R.string.ok),
            getString(android.R.string.cancel),
            true,
            new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {
                storage.deleteDB(langCode);
                adapter.notifyDataSetChanged();
              }
            }, null);
  }

  /**
   * This function uses DialogMenu to show the option menu which contains
   * download and delete options.
   * 
   * @param langCode
   */
  private void showOptions(final String langCode) {
    // The download menu id's is 0.
    DialogMenuItem downloadMenu = DialogMenuItem.create(0, 0,
            IconFontUtils.get(IconFontUtils.DOWNLOAD) + " " +
                    getString(R.string.button_download));
    // The delete menu's id is 1.
    DialogMenuItem deleteMenu = DialogMenuItem.create(1, 0,
            IconFontUtils.get(IconFontUtils.REMOVE) + " " +
                    getString(R.string.button_delete));
    DialogUtils.get().showMenuDialog(this,
            R.drawable.ic_launcher,
            R.string.dialog_title_option_menu,
            true, // cancelable
            new DialogMenuItem[] { downloadMenu, deleteMenu }, // menus
            new DialogMenuListener() {

              @Override
              public void onMenuItemClicked(DialogMenuItem item) {
                switch (item.id) {
                  case 0:
                    downloadDB(langCode);
                    break;
                  case 1:
                    deleteDB(langCode);
                    break;
                }
              }

              @Override
              public void onDialogCancelled() {
                // do nothing.
              }
            });
  }

  private void initAdapter() {
    // use LangListAdapter to show all supported languages.
    adapter = new LangListAdapter(this, new LangListAdapter.UIHandler() {

      @Override
      public void customizeUI(View ui, String langCode) {
        fillData(ui, langCode);
      }

      @Override
      public void handleButtonClick(int id, String langCode) {
        switch (id) {
          case R.id.button_delete:
            deleteDB(langCode);
            break;
          case R.id.button_download:
            downloadDB(langCode);
            break;
          case R.id.button_options:
            showOptions(langCode);
            break;
        }
      }
    });
    this.setListAdapter(adapter);
  }
}
