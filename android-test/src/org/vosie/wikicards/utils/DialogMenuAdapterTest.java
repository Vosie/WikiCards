package org.vosie.wikicards.utils;

import org.vosie.wikicards.MainActivity;
import org.vosie.wikicards.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class DialogMenuAdapterTest extends ActivityInstrumentationTestCase2<MainActivity> {

  public DialogMenuAdapterTest() {
    super(MainActivity.class);
  }

  private DialogMenuItem[] testItems = new DialogMenuItem[] {
          DialogMenuItem.create(1, R.drawable.button_blue, "test1"),
          DialogMenuItem.create(2, 0, IconFontUtils.get(IconFontUtils.ARROW_RIGHT, "test2")),
          DialogMenuItem.create(3, 0, "test3")
  };

  private DialogMenuAdapter testTarget;
  private AlertDialog dialog;
  private DialogMenuItem clickedItem;
  private boolean cancelled;

  @Override
  protected void setUp() throws Exception {
    testTarget = new DialogMenuAdapter(testItems,
            this.getInstrumentation().getTargetContext());
    clickedItem = null;
    cancelled = false;
    super.setUp();
  }

  public void testDialogMenuAdapter() {
    assertNotNull(testTarget);
  }

  public void testGetCount() {
    assertEquals(3, testTarget.getCount());
  }

  public void testGetItem() {
    assertEquals(testItems[0], testTarget.getItem(0));
    assertEquals(testItems[1], testTarget.getItem(1));
    assertEquals(testItems[2], testTarget.getItem(2));
    try {
      testTarget.getItem(3);
      fail("it should throw exception when we try to access out of bound" +
              " indices");
    } catch (Exception ex) {
      assertTrue("exception found, that's correct.", true);
    }
  }

  public void testGetItemId() {
    assertEquals(0, testTarget.getItemId(0));
    assertEquals(1, testTarget.getItemId(1));
    assertEquals(2, testTarget.getItemId(2));
  }

  public void testGetView() {
    // test item 1
    View v = testTarget.getView(0, null, null);
    assertNotNull(v);
    assertTrue(v instanceof TextView);
    assertEquals("test1", ((TextView) v).getText());
    assertNotNull(((TextView) v).getCompoundDrawables()[0]);
    // test item 2
    v = testTarget.getView(1, null, null);
    assertNotNull(v);
    assertTrue(v instanceof TextView);
    assertEquals(IconFontUtils.get(IconFontUtils.ARROW_RIGHT, "test2"),
            ((TextView) v).getText());
    assertNull(((TextView) v).getCompoundDrawables()[0]);
    // test item 3
    v = testTarget.getView(2, null, null);
    assertNotNull(v);
    assertTrue(v instanceof TextView);
    assertEquals("test3", ((TextView) v).getText());
    assertNull(((TextView) v).getCompoundDrawables()[0]);
  }

  public void testShowMenuDialog() throws Throwable {
    final Activity act = getActivity();
    runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        dialog = DialogUtils.get().showMenuDialog(act, 0, "title",
                true, testItems, new DialogMenuListener() {

                  @Override
                  public void onMenuItemClicked(DialogMenuItem item) {
                    clickedItem = item;
                  }

                  @Override
                  public void onDialogCancelled() {
                    cancelled = true;
                  }
                });
      }
    });
    getInstrumentation().waitForIdleSync();
    final ListView listView = dialog.getListView();
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        listView.performItemClick(listView.getChildAt(1), 1,
                listView.getItemIdAtPosition(1));
      }

    });
    getInstrumentation().waitForIdleSync();
    assertNotNull(clickedItem);
    assertEquals(2, clickedItem.id);
    assertEquals(testItems[1], clickedItem);
    // close it with back key
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog.onBackPressed();
      }
    });
    getInstrumentation().waitForIdleSync();
    assertTrue("Dialog should be cancelled", cancelled);
  }
}
