package org.vosie.wikicards;

import org.vosie.wikicards.CardActivity;
import org.vosie.wikicards.R;
import org.vosie.wikicards.Settings;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

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
    Settings.selectedLanguageCode = "en";
    Settings.nativeTongue = "tw";
    setActivityInitialTouchMode(true);
    mCardActivity = getActivity();
    mNextButton = mCardActivity.findViewById(R.id.button_next);
    mPreviousButton = mCardActivity.findViewById(R.id.button_previous);
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

}
