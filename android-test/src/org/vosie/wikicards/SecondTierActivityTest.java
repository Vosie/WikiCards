package org.vosie.wikicards;

import org.vosie.wikicards.test.mock.MockMenuItem;

import android.app.ActionBar;
import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class SecondTierActivityTest extends ActivityInstrumentationTestCase2<LicenseActivity> {

  public SecondTierActivityTest() {
    super(LicenseActivity.class);
  }

  public void testOnCreate() {
    ActionBar ab = this.getActivity().getActionBar();
    assertEquals(ab.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP,
            ActionBar.DISPLAY_HOME_AS_UP);
  }

  public void testClickOnHome() throws Throwable {
    final Activity act = this.getActivity();
    final MockMenuItem mockMenu = new MockMenuItem();
    mockMenu.itemId = android.R.id.home;
    this.runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        assertTrue(act.onOptionsItemSelected(mockMenu));
      }
    });

    this.getInstrumentation().waitForIdleSync();
  }

}
