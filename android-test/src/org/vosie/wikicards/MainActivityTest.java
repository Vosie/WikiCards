package org.vosie.wikicards;

import org.vosie.wikicards.utils.CategoryUtils;

import android.content.Context;
import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Spinner;

public class MainActivityTest extends
                             ActivityInstrumentationTestCase2<MainActivity> {

  private MainActivity mActivity;

  public MainActivityTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    mActivity = getActivity();
    super.setUp();
  }

  public void testCategorySpinner() {
    String categroyName = mActivity.getString(R.string.category_label_1);
    Spinner spinner = (Spinner) mActivity.findViewById(R.id.spinner_category);
    assertEquals(CategoryUtils.CATEGORY_NAMES_ID.length, spinner.getCount());
    assertEquals(categroyName, spinner.getItemAtPosition(0).toString());
  }

  public void testSaveCategoryToPreference() throws Throwable {
    final Spinner spinner =
            (Spinner) mActivity.findViewById(R.id.spinner_category);
    final int lastIndex = CategoryUtils.CATEGORY_NAMES_ID.length - 1;
    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        spinner.setSelection(lastIndex);
      }
    });
    getInstrumentation().waitForIdleSync();
    SharedPreferences sp = mActivity.getPreferences(Context.MODE_PRIVATE);
    assertEquals(lastIndex + 1,
            sp.getInt(Constants.PREF_KEY_SELECTED_CATEGORY, 0));

    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        spinner.setSelection(lastIndex - 1);
      }
    });
    getInstrumentation().waitForIdleSync();
    sp = mActivity.getPreferences(Context.MODE_PRIVATE);
    assertEquals(lastIndex, sp.getInt(Constants.PREF_KEY_SELECTED_CATEGORY, 0));
  }

  public void testGetCategoryFromPreference() throws Throwable {
    SharedPreferences sp = mActivity.getPreferences(Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sp.edit();
    editor.putInt(Constants.PREF_KEY_SELECTED_CATEGORY,
            CategoryUtils.CATEGORY_COUNTRY);
    editor.commit();
    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        getInstrumentation().callActivityOnCreate(mActivity, null);
      }
    });
    getInstrumentation().waitForIdleSync();
    Spinner spinner = (Spinner) mActivity.findViewById(R.id.spinner_category);
    assertEquals(CategoryUtils.CATEGORY_COUNTRY, (int) spinner.getSelectedItemId());

    editor.putInt(Constants.PREF_KEY_SELECTED_CATEGORY, CategoryUtils.CATEGORY_ANIMAL);
    editor.commit();
    this.runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        getInstrumentation().callActivityOnCreate(mActivity, null);
      }
    });
    getInstrumentation().waitForIdleSync();
    spinner = (Spinner) mActivity.findViewById(R.id.spinner_category);
    assertEquals(CategoryUtils.CATEGORY_ANIMAL, (int) spinner.getSelectedItemId());
  }
}
