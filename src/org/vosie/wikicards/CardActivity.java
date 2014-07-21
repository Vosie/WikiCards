package org.vosie.wikicards;

import org.vosie.wikicards.data.DownloadWordListener;
import org.vosie.wikicards.data.Word;
import org.vosie.wikicards.data.WordsStorage;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CardActivity extends Activity {

  private Button previousButton;
  private Button nextButton;
  private TextView wordTextView;
  private TextView indexTextView;
  private String langCode;
  private WordsStorage wordsStorage;
  private String[] serverIDs;
  private int total;
  private int currentIndex = 0;
  private ProgressDialog progress;
  private int failOccurIndex;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card);
    initVariables();
    initViews();
    loadWordAndShow(serverIDs[currentIndex]);
  }

  private void initVariables() {
    wordsStorage = new WordsStorage(this);
    serverIDs = wordsStorage.getServerIDs(0);
    failOccurIndex = total = serverIDs.length;
    langCode = Settings.selectedLanguageCode;
  }

  private void initViews() {
    previousButton = (Button) findViewById(R.id.button_previous);
    nextButton = (Button) findViewById(R.id.button_next);
    wordTextView = (TextView) findViewById(R.id.textview_word);
    indexTextView = (TextView) findViewById(R.id.textview_index);

    updateButtonsEnable();
    updateIndexTextView();

    previousButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        loadWordAndShow(serverIDs[--currentIndex]);
        updateButtonsEnable();
        updateIndexTextView();
      }
    });

    nextButton.setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View arg0) {
        loadWordAndShow(serverIDs[++currentIndex]);
        updateButtonsEnable();
        updateIndexTextView();
      }
    });
  }

  private void updateButtonsEnable() {
    previousButton.setEnabled(currentIndex > 0);
    nextButton.setEnabled(currentIndex < total - 1
            && currentIndex < failOccurIndex - 1);
  }

  private void updateIndexTextView() {
    indexTextView.setText(String.valueOf(currentIndex + 1) +
            "/" + String.valueOf(total));
  }

  private void loadWordAndShow(String serverID) {
    Word word = wordsStorage.getWordFromLocal(serverID, langCode);
    if (word != null) {
      showWord(word);
    } else {
      progress = ProgressDialog.show(this,
              getString(R.string.dialog_title_downloading),
              getString(R.string.dialog_desc_downloading), true);
      wordsStorage.getWordFromServer(serverID, langCode,
              new DownloadWordListener() {

                @Override
                public void onWordReceived(Word word) {
                  progress.dismiss();
                  showWord(word);
                }

                @Override
                public void onError(int errorType, Exception e) {
                  String title = "";
                  String desc = "";
                  switch (errorType) {
                    case DownloadWordListener.NETWORK_ERROR:
                      title = getString(R.string.dialog_title_network_error);
                      desc = getString(R.string.dialog_desc_network_error);
                      break;
                    case DownloadWordListener.HTTP_ERROR:
                      title = getString(R.string.dialog_title_http_error);
                      desc = getString(R.string.dialog_desc_http_error);
                      break;
                    case DownloadWordListener.INTERNAL_ERROR:
                      title = getString(R.string.dialog_title_internal_error);
                      desc = getString(R.string.dialog_desc_internal_error);
                      break;
                  }
                  failOccurIndex = currentIndex--;
                  progress.dismiss();
                  ShowAlertDialog(title, desc, failOccurIndex == 0);
                }
              });
    }
  }

  private void showWord(Word word) {
    wordTextView.setText(word.label);
  }

  private void ShowAlertDialog(String title, String msg, final boolean closeSelf)
  {
    Builder builder = new AlertDialog.Builder(this);
    builder.setTitle(title);
    builder.setMessage(msg);
    builder.setPositiveButton(android.R.string.ok,
            new DialogInterface.OnClickListener() {

              @Override
              public void onClick(DialogInterface dialog, int which) {
                if (closeSelf && AlertDialog.BUTTON_POSITIVE == which) {
                  CardActivity.this.finish();
                }
              }
            });
    AlertDialog dialog = builder.create();
    dialog.setCanceledOnTouchOutside(false);
    dialog.show();
  }
}
