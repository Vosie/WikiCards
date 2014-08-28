package org.vosie.wikicards.test;

import org.vosie.wikicards.CardActivity;
import org.vosie.wikicards.R;
import org.vosie.wikicards.Settings;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
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

  public void testRemeberCardPosotion_save() {
    assertEquals(0, mCardActivity.getCardPosition());
    TouchUtils.clickView(this, mNextButton);
    mCardActivity.finish();
    this.setActivity(null);
    mCardActivity = getActivity();
    assertEquals(1, mCardActivity.getCardPosition());
    TouchUtils.clickView(this, mPreviousButton);
    mCardActivity.finish();
    this.setActivity(null);
    mCardActivity = getActivity();
    assertEquals(0, mCardActivity.getCardPosition());
  }

}
