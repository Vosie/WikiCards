package org.vosie.wikicards;

import org.vosie.wikicards.data.DownloadWordListener;
import org.vosie.wikicards.data.Word;
import org.vosie.wikicards.data.WordsStorage;
import org.vosie.wikicards.utils.ErrorUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class CardActivity extends Activity {

  private static final String TAG = "CardActivity";
  
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
    wordsStorage = new WordsStorage(this, Constants.CATEGORY_COUNTRY);
    serverIDs = wordsStorage.getServerIDs();
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
                  failOccurIndex = currentIndex--;
                  progress.dismiss();
                  ErrorUtils.handleDownloadkError(CardActivity.this, errorType,
                          failOccurIndex == 0);
                  Log.e(TAG, "error while downloading word", e);
                }
              });
    }
  }

  private void showWord(Word word) {
    wordTextView.setText(word.label);
  }
}
