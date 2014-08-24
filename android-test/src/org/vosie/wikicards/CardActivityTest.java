package org.vosie.wikicards;

import org.vosie.wikicards.CardActivity;
import org.vosie.wikicards.CardPositionSelector;
import org.vosie.wikicards.R;
import org.vosie.wikicards.Settings;

import android.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CardActivityTest extends
                             ActivityInstrumentationTestCase2<CardActivity> {

  private CardActivity mCardActivity;
  private View mNextButton;
  private View mPreviousButton;

  public CardActivityTest() {
    super(CardActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    Settings.selectedLanguageCode = "base";
    Settings.nativeTongue = "tw";
    setActivityInitialTouchMode(true);
    mCardActivity = getActivity();
    mNextButton = mCardActivity.findViewById(R.id.button_next);
    mPreviousButton = mCardActivity.findViewById(R.id.button_previous);
    mCardActivity.setCardPosition(0);
  }

  public void testRemeberCardPosotion_save() throws Throwable {
    assertEquals(0, mCardActivity.getCardPosition());
    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        mNextButton.performClick();
      }
    });
    getInstrumentation().waitForIdleSync();
    mCardActivity.finish();
    this.setActivity(null);
    mCardActivity = getActivity();
    assertEquals(1, mCardActivity.getCardPosition());
    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        mPreviousButton.performClick();
      }
    });
    getInstrumentation().waitForIdleSync();
    mCardActivity.finish();
    this.setActivity(null);
    mCardActivity = getActivity();
    assertEquals(0, mCardActivity.getCardPosition());
  }

  public void testPositionSelectorDialog_show() throws Throwable {
    final TextView idxTv =
            (TextView) mCardActivity.findViewById(R.id.textview_index);
    CardPositionSelector selector = mCardActivity.getPositionSelector();
    AlertDialog dialog = selector.getDialog();

    performClick(idxTv);
    assertTrue(dialog.isShowing());
    performClick(dialog.getButton(AlertDialog.BUTTON_NEGATIVE));
    assertFalse(dialog.isShowing());

    performClick(idxTv);
    assertTrue(dialog.isShowing());
    performClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE));
    assertFalse(dialog.isShowing());
  }

  public void testCardPositionSelector_select() throws Throwable {
    final TextView idxTv =
            (TextView) mCardActivity.findViewById(R.id.textview_index);
    final CardPositionSelector selector = mCardActivity.getPositionSelector();
    AlertDialog dialog = selector.getDialog();

    performClick(idxTv);
    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        selector.setCurrentItem(10);
      }
    });
    getInstrumentation().waitForIdleSync();
    performClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE));
    assertEquals(10, mCardActivity.getCardPosition());
    
    performClick(idxTv);
    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        selector.setCurrentItem(20);
      }
    });
    getInstrumentation().waitForIdleSync();
    performClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE));
    assertEquals(20, mCardActivity.getCardPosition());
  }

  public void testCardPositionSelector_showCorrectPosition() throws Throwable {
    final TextView idxTv =
            (TextView) mCardActivity.findViewById(R.id.textview_index);
    CardPositionSelector selector = mCardActivity.getPositionSelector();
    AlertDialog dialog = selector.getDialog();

    mCardActivity.setCardPosition(10);
    performClick(idxTv);
    assertEquals(10, selector.getCurrentItem());
    performClick(dialog.getButton(AlertDialog.BUTTON_NEGATIVE));

    mCardActivity.setCardPosition(20);
    performClick(idxTv);
    assertEquals(20, selector.getCurrentItem());
    performClick(dialog.getButton(AlertDialog.BUTTON_NEGATIVE));
  }

  private void performClick(final View view) throws Throwable {
    runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        view.performClick();
      }
    });
    getInstrumentation().waitForIdleSync();
  }

}
