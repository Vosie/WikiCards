package org.vosie.wikicards;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.vosie.wikicards.data.DownloadWordListener;
import org.vosie.wikicards.data.SoundStorage;
import org.vosie.wikicards.data.StarredWordsStorage;
import org.vosie.wikicards.data.Word;
import org.vosie.wikicards.data.WordsStorage;
import org.vosie.wikicards.utils.DialogUtils;
import org.vosie.wikicards.utils.ErrorUtils;
import org.vosie.wikicards.utils.IconFontUtils;
import org.vosie.wikicards.utils.PlayerUtils;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewAnimator;

import com.google.analytics.tracking.android.EasyTracker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.tekle.oss.android.animation.AnimationFactory;
import com.tekle.oss.android.animation.AnimationFactory.FlipDirection;

public class CardActivity extends SecondTierActivity {

  private static final String TAG = "CardActivity";

  private static int CARD_POSITION = 0;

  private Button previousButton;
  private Button nextButton;
  private TextView indexTextView;
  protected String langCode;
  private WordsStorage wordsStorage;
  protected StarredWordsStorage starredWordsStorage;
  private String[] serverIDs;
  private int total;
  private int frontFailOccurIndex;
  private int backFailOccurIndex;
  private ProgressDialog progress;
  private View currentCard;
  private View cardFront;
  private View cardBack;
  private ViewAnimator viewAnimator;
  private CardPositionSelector cardPositionSelector;
  private MediaPlayer activePlayer;
  protected SoundStorage soundStorage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_card);
    initVariables();
    initViews();
    if (serverIDs.length > 0) {
      loadWordAndShow(serverIDs[CARD_POSITION]);
    } else {
      DialogUtils.get().showAlertDialog(this,
              getString(R.string.dialog_title_tip),
              getString(R.string.dialog_desc_no_card_to_show),
              false,
              true
              );
    }
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

  private void initVariables() {
    wordsStorage = new WordsStorage(this, Settings.selectedCategory);
    starredWordsStorage = new StarredWordsStorage(this, Settings.selectedCategory);
    soundStorage = new SoundStorage(this);
    serverIDs = getServerIDs();
    frontFailOccurIndex = backFailOccurIndex = total = serverIDs.length;
    langCode = Settings.selectedLanguageCode;
    CARD_POSITION = getStartCardPosition();
    initCardPositionSelector();
  }

  protected String[] getServerIDs() {
    return wordsStorage.getServerIDs();
  }

  protected int getStartCardPosition() {
    return CARD_POSITION;
  }

  private void initViews() {
    initNavBar();
    initFlipCard();
    initButtons();
  }

  private void initButtons() {
    List<String> badList = Arrays.asList(Constants.PRONUNCIATION_BAD_LIST);
    cardFront.findViewById(R.id.button_say_word).setVisibility(
            badList.contains(Settings.selectedLanguageCode) ?
                    View.GONE : View.VISIBLE);
    cardBack.findViewById(R.id.button_say_word).setVisibility(
            badList.contains(Settings.nativeTongue) ?
                    View.GONE : View.VISIBLE);
  }

  private void initNavBar() {
    previousButton = (Button) findViewById(R.id.button_previous);
    nextButton = (Button) findViewById(R.id.button_next);
    indexTextView = (TextView) findViewById(R.id.textview_index);

    previousButton.setText(IconFontUtils.get(IconFontUtils.ARROW_LEFT));
    nextButton.setText(IconFontUtils.get(IconFontUtils.ARROW_RIGHT));
    previousButton.setTypeface(Settings.iconFont);
    nextButton.setTypeface(Settings.iconFont);

    previousButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        loadWordAndShow(serverIDs[--CARD_POSITION]);
        updateNavBar();
      }
    });

    nextButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        loadWordAndShow(serverIDs[++CARD_POSITION]);
        updateNavBar();
      }
    });

    indexTextView.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        cardPositionSelector.show(CARD_POSITION);
      }
    });

    updateNavBar();
  }

  private void initCardPositionSelector() {
    CardPositionSelector.OnSelectListener listener =
            new CardPositionSelector.OnSelectListener() {
              @Override
              public void onSelect(int item) {
                CARD_POSITION = item;
                loadWordAndShow(serverIDs[item]);
                updateNavBar();
              }
            };
    cardPositionSelector =
            new CardPositionSelector(this, serverIDs.length, listener);
  }

  private void updateNavBar() {
    int failIndex = isCradFront() ? frontFailOccurIndex : backFailOccurIndex;
    previousButton.setEnabled(CARD_POSITION > 0);
    nextButton.setEnabled(CARD_POSITION < total - 1
            && CARD_POSITION < failIndex - 1);
    indexTextView.setText(String.valueOf(CARD_POSITION + 1) +
            "/" + String.valueOf(total));
  }

  private boolean isCradFront() {
    return currentCard == cardFront;
  }

  private void initFlipCard() {
    viewAnimator = (ViewAnimator) this.findViewById(R.id.viewFlipper);
    cardFront = LayoutInflater.from(this).inflate(R.layout.card, null);
    cardBack = LayoutInflater.from(this).inflate(R.layout.card, null);
    viewAnimator.addView(cardFront);
    viewAnimator.addView(cardBack);
    currentCard = cardFront;

    viewAnimator.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        flipCard();
        updateNavBar();
      }
    });
  }

  private void flipCard() {
    currentCard = isCradFront() ? cardBack : cardFront;
    langCode = isCradFront() ?
            Settings.selectedLanguageCode : Settings.nativeTongue;
    loadWordAndShow(serverIDs[CARD_POSITION]);
    AnimationFactory.flipTransition(viewAnimator, FlipDirection.LEFT_RIGHT, 250);
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
                  updateFailOccurIndex(CARD_POSITION);
                  showErrorCard(errorType);
                  updateNavBar();
                  progress.dismiss();
                  ErrorUtils.get().handleDownloadkError(CardActivity.this, errorType,
                          CARD_POSITION == 0);
                  Log.e(TAG, "error while downloading word", e);
                }
              });
    }
  }

  private void showWord(final Word word) {
    currentCard.findViewById(R.id.general_card).setVisibility(View.VISIBLE);
    currentCard.findViewById(R.id.error_card).setVisibility(View.GONE);

    TextView wordTextView =
            (TextView) currentCard.findViewById(R.id.textview_word);
    TextView descriptionTextView =
            (TextView) currentCard.findViewById(R.id.textview_desc);
    Button goToWikiButton =
            (Button) currentCard.findViewById(R.id.button_go_to_wiki);
    Button sayWordButton =
            (Button) currentCard.findViewById(R.id.button_say_word);
    final ImageButton starButton = (ImageButton) currentCard.findViewById(R.id.button_star);

    if (starredWordsStorage.isStarred(word.serverID)) {
      starButton.setImageResource(R.drawable.star_on);
    } else {
      starButton.setImageResource(R.drawable.star_off);
    }

    starButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        if (starredWordsStorage.isStarred(word.serverID)) {
          starredWordsStorage.unStarWord(word.serverID);
          starButton.setImageResource(R.drawable.star_off);
        } else {

          starredWordsStorage.starWord(word.serverID);
          starButton.setImageResource(R.drawable.star_on);
        }
      }
    });

    goToWikiButton.setText(IconFontUtils.get(IconFontUtils.WIKIPEDIA));
    goToWikiButton.setTypeface(Settings.iconFont);
    sayWordButton.setText(IconFontUtils.get(IconFontUtils.SPEAKER));
    sayWordButton.setTypeface(Settings.iconFont);
    wordTextView.setText(word.label);
    loadImage(word.imageURL);
    descriptionTextView.setText(word.shortDesc);
    goToWikiButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(word.url));
        startActivity(intent);
      }
    });

    sayWordButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        sayWord(word);
      }
    });
  }

  private void sayWord(final Word w) {
    SoundStorage ss = soundStorage;
    ss.getSoundResource(langCode, w.serverID, new SoundStorage.Listener() {

      @Override
      public void onProgressing(int progress) {
        Log.d(TAG, "downloading progress: " + progress);
      }

      @Override
      public void onReady(File f) {
        if (!langCode.equals(w.languageCode) ||
                !serverIDs[CARD_POSITION].equals(w.serverID)) {
          // user may flip the card
          // user may change the word, we don't need to say it.
          return;
        }
        if (null != activePlayer && activePlayer.isPlaying()) {
          activePlayer.stop();
        }
        MediaPlayer mp = PlayerUtils.get().createPlayer(CardActivity.this, f,
                new PlayerUtils.Listener() {

                  @Override
                  public void onComplete(MediaPlayer mp, int error, int extra) {
                    if (activePlayer == mp) {
                      activePlayer = null;
                    }
                  }
                });
        mp.start();
        activePlayer = mp;
      }

      @Override
      public void onError(int type, Exception ex) {
        ErrorUtils.get().handleDownloadkError(CardActivity.this, type, false);
      }

    });
  }

  private void loadImage(String url) {

    final ImageView picImageView =
            (ImageView) currentCard.findViewById(R.id.imageview_photo);
    final ProgressBar loadingImageProgressBar =
            (ProgressBar) currentCard.findViewById(R.id.progressBar_loadingimage);

    picImageView.setVisibility(View.INVISIBLE);
    loadingImageProgressBar.setVisibility(View.VISIBLE);

    if (url.equals("")) {
      Picasso.with(this).load(R.drawable.ic_launcher).into(picImageView);
      picImageView.setVisibility(View.VISIBLE);
      loadingImageProgressBar.setVisibility(View.INVISIBLE);
      return;
    }

    Picasso.with(this)
            .load(url)
            .error(R.drawable.ic_launcher)
            .into(picImageView, new Callback() {

              @Override
              public void onSuccess() {
                picImageView.setVisibility(View.VISIBLE);
                loadingImageProgressBar.setVisibility(View.INVISIBLE);
              }

              @Override
              public void onError() {
                picImageView.setVisibility(View.VISIBLE);
                loadingImageProgressBar.setVisibility(View.INVISIBLE);
              }
            });
  }

  private void updateFailOccurIndex(int index) {
    if (isCradFront()) {
      frontFailOccurIndex = index;
    } else {
      backFailOccurIndex = index;
    }
  }

  private void showErrorCard(int errorType) {
    String msg = getString(R.string.label_card_error_msg) + "\n"
            + ErrorUtils.get().getErrorDesc(this, errorType);

    TextView msgTextView =
            (TextView) currentCard.findViewById(R.id.textview_error_msg);
    msgTextView.setText(msg);

    currentCard.findViewById(R.id.general_card).setVisibility(View.GONE);
    currentCard.findViewById(R.id.error_card).setVisibility(View.VISIBLE);
  }

  public int getCardPosition() {
    return CARD_POSITION;
  }

  public void setCardPosition(int p) {
    CARD_POSITION = p;
  }

  public CardPositionSelector getPositionSelector() {
    return cardPositionSelector;
  }
}
