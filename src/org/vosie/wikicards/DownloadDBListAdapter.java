package org.vosie.wikicards;

import org.vosie.wikicards.data.DownloadDBListener;
import org.vosie.wikicards.data.WordsStorage;
import org.vosie.wikicards.utils.CategoryUtils;
import org.vosie.wikicards.utils.DialogMenuItem;
import org.vosie.wikicards.utils.DialogMenuListener;
import org.vosie.wikicards.utils.DialogUtils;
import org.vosie.wikicards.utils.ErrorUtils;
import org.vosie.wikicards.utils.IconFontUtils;
import org.vosie.wikicards.utils.LanguageUtils;
import org.vosie.wikicards.utils.NetworkUtils;

import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class DownloadDBListAdapter extends BaseAdapter implements Constants,
                                                      OnClickListener,
                                                      StickyListHeadersAdapter {

  public static class DBItem {
    public String lang;
    public int categroy;

    public DBItem(String lang, int categroy) {
      this.lang = lang;
      this.categroy = categroy;
    }
  }

  private static final String TAG = "DownloadDBListAdapter";
  private Context context;

  public DownloadDBListAdapter(Context ctx) {
    this.context = ctx;
  }

  @Override
  public int getCount() {
    return SUPPORTED_LANGUAGES.length * CategoryUtils.CATEGORY_NAMES_ID.length;
  }

  @Override
  public Object getItem(int position) {
    // Sine we use language to grouping items. 
    // We use the following method to determine witch 
    // language and category is the position's correspondence.    
    int categoryCount = CategoryUtils.CATEGORY_NAMES_ID.length;
    String lang = SUPPORTED_LANGUAGES[position / categoryCount];
    int category = position + 1 > categoryCount ?
            position % categoryCount
            : position;
    return new DBItem(lang, category + 1);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    View vwRet = convertView;
    if (null == vwRet) {
      vwRet = LayoutInflater.from(context).inflate(R.layout.list_db_row,
              null);
    }

    Button delBtn = (Button) vwRet.findViewById(R.id.button_delete);
    Button dldBtn = (Button) vwRet.findViewById(R.id.button_download);
    Button optBtn = (Button) vwRet.findViewById(R.id.button_options);

    // set text
    delBtn.setText(IconFontUtils.get(IconFontUtils.REMOVE,
            context.getString(R.string.button_delete)));
    dldBtn.setText(IconFontUtils.get(IconFontUtils.DOWNLOAD,
            context.getString(R.string.button_download)));
    optBtn.setText(IconFontUtils.get(IconFontUtils.WRENCH,
            context.getString(R.string.button_options)));
    delBtn.setTypeface(Settings.iconFont);
    dldBtn.setTypeface(Settings.iconFont);
    optBtn.setTypeface(Settings.iconFont);

    // override listeners
    delBtn.setOnClickListener(this);
    dldBtn.setOnClickListener(this);
    optBtn.setOnClickListener(this);
    // hide all of them
    delBtn.setVisibility(View.GONE);
    dldBtn.setVisibility(View.GONE);
    optBtn.setVisibility(View.GONE);

    DBItem item = (DBItem) getItem(position);

    // change data
    delBtn.setTag(item);
    dldBtn.setTag(item);
    optBtn.setTag(item);

    fillData(vwRet, item.lang, item.categroy);

    return vwRet;
  }

  private void fillData(View ui, String langCode, int category) {
    boolean hasInternet = NetworkUtils.get().isNetworkAvailable(context);
    String categoryName = CategoryUtils.getName(context, category);
    WordsStorage storage = new WordsStorage(context, langCode, category);
    int totalCount = storage.getRowCount(NEUTRAL_LANG_CODE);
    int rows = Math.min(storage.getRowCount(), totalCount);

    String dbStatus = " - (" + rows + "/" + totalCount + ")";
    ((TextView) ui.findViewById(R.id.label_category_name)).setText(categoryName);
    if (0 == rows) {
      // we show download button when no data in db.
      View downloadBtn = ui.findViewById(R.id.button_download);
      downloadBtn.setVisibility(View.VISIBLE);
      // enable download button only when we have internet connection
      downloadBtn.setEnabled(hasInternet);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              context.getString(R.string.msg_empty_database));
    } else if (rows == totalCount) {
      // we show delete button when db is fully downloaded.
      ui.findViewById(R.id.button_delete).setVisibility(View.VISIBLE);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              context.getString(R.string.msg_database_downloaded) + dbStatus);
    } else if (hasInternet) {
      // we show option menu which has delete and download when db is partial
      // downloaded.
      ui.findViewById(R.id.button_options).setVisibility(View.VISIBLE);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              context.getString(R.string.msg_partial_downloaded) + dbStatus);
    } else {
      // only show delete button when we don't have internet connection.
      ui.findViewById(R.id.button_delete).setVisibility(View.VISIBLE);
      ((TextView) ui.findViewById(R.id.label_db_status)).setText(
              context.getString(R.string.msg_partial_downloaded) + dbStatus);
    }
  }

  @Override
  public void onClick(View v) {
    int id = v.getId();
    DBItem item = (DBItem) v.getTag();
    switch (id) {

      case R.id.button_delete:

        deleteDB(item.lang, item.categroy);

        break;

      case R.id.button_download:

        downloadDB(item.lang, item.categroy);

        break;

      case R.id.button_options:

        showOptions(item.lang, item.categroy);

        break;

    }

  }

  /**
   * This shows a progress dialog to show the downloading progress.
   * 
   * @param langCode
   */
  private void downloadDB(String langCode, int category) {
    final ProgressDialog progressDialog = new ProgressDialog(context);
    progressDialog.setTitle(R.string.dialog_title_download_offline_db);
    progressDialog.setMessage(LanguageUtils.getLocalizedLanguageName(langCode) +
            " " + CategoryUtils.getName(context, category));
    progressDialog.setCancelable(false);
    progressDialog.setCanceledOnTouchOutside(false);
    progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
    progressDialog.setMax(100);
    progressDialog.show();

    // we use WordsStorage to download the whole db.
    new WordsStorage(context, langCode, category)
            .downloadDB(new DownloadDBListener() {

              @Override
              public void onProgressing(int progress) {
                progressDialog.setProgress(progress);
              }

              @Override
              public void onComplete() {
                progressDialog.dismiss();
                DownloadDBListAdapter.this.notifyDataSetChanged();
              }

              @Override
              public void onError(int errorType, Exception e) {
                // Log the error and show error message.
                Log.e(TAG, "error while downloading offline db", e);
                progressDialog.dismiss();
                ErrorUtils.get().handleDownloadkError((Activity) context,
                        errorType, false);
              }
            });
  }

  private void deleteDB(final String langCode, final int category) {
    String msg = context.getString(R.string.dialog_desc_confirm_delete_db) + "\n" +
            LanguageUtils.getLocalizedLanguageName(langCode) + " " +
            CategoryUtils.getName(context, category);
    DialogUtils.get().showConfirmDialog(context,
            R.drawable.ic_launcher,
            context.getString(R.string.dialog_title_confirm_delete_db),
            msg,
            context.getString(android.R.string.ok),
            context.getString(android.R.string.cancel),
            true,
            new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {
                new WordsStorage(context, langCode, category).deleteDB();
                DownloadDBListAdapter.this.notifyDataSetChanged();
              }
            }, null);
  }

  /**
   * This function uses DialogMenu to show the option menu which contains
   * download and delete options.
   * 
   * @param langCode
   */
  private void showOptions(final String langCode, final int category) {
    // The download menu id's is 0.
    DialogMenuItem downloadMenu = DialogMenuItem.create(0, 0,
            IconFontUtils.get(IconFontUtils.DOWNLOAD) + " " +
                    context.getString(R.string.button_download));
    // The delete menu's id is 1.
    DialogMenuItem deleteMenu = DialogMenuItem.create(1, 0,
            IconFontUtils.get(IconFontUtils.REMOVE) + " " +
                    context.getString(R.string.button_delete));
    DialogUtils.get().showMenuDialog(context,
            R.drawable.ic_launcher,
            R.string.dialog_title_option_menu,
            true, // cancelable
            new DialogMenuItem[] { downloadMenu, deleteMenu }, // menus
            new DialogMenuListener() {

              @Override
              public void onMenuItemClicked(DialogMenuItem item) {
                switch (item.id) {
                  case 0:
                    downloadDB(langCode, category);
                    break;
                  case 1:
                    deleteDB(langCode, category);
                    break;
                }
              }

              @Override
              public void onDialogCancelled() {
                // do nothing.
              }
            });
  }

  @Override
  public View getHeaderView(int position, View convertView, ViewGroup parent) {
    HeaderViewHolder holder;
    if (convertView == null) {
      holder = new HeaderViewHolder();
      convertView = LayoutInflater.from(context).inflate(R.layout.list_db_header, parent, false);
      holder.text = (TextView) convertView.findViewById(R.id.db_header);
      convertView.setTag(holder);
    } else {
      holder = (HeaderViewHolder) convertView.getTag();
    }

    DBItem item = (DBItem) getItem(position);
    String langName = LanguageUtils.getLocalizedLanguageName(item.lang);
    holder.text.setText(langName);
    return convertView;
  }

  class HeaderViewHolder {
    TextView text;
  }

  @Override
  public long getHeaderId(int position) {
    return position / CategoryUtils.CATEGORY_NAMES_ID.length;
  }

}
