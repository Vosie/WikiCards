package org.vosie.wikicards.utils;

import org.vosie.wikicards.MainActivity;
import org.vosie.wikicards.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;

public class DialogUtilsTest extends ActivityInstrumentationTestCase2<MainActivity> implements
                                                                                   DialogInterface.OnClickListener,
                                                                                   DialogInterface.OnCancelListener {

  private boolean clicked = false;
  private boolean cancel = false;
  private AlertDialog dialog;
  private Activity activity;

  public DialogUtilsTest() {
    super(MainActivity.class);
  }

  @Override
  public void onClick(DialogInterface dialog, int which) {
    clicked = true;
  }

  @Override
  public void onCancel(DialogInterface dialog) {
    cancel = true;
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    clicked = false;
    cancel = false;
    activity = getActivity();
  }

  private void performClick(final Button button) throws Throwable {
    runTestOnUiThread(new Runnable() {
      @Override
      public void run() {
        button.performClick();
      }
    });
    getInstrumentation().waitForIdleSync();
  }

  public void testShowConfirmDialogOk() throws Throwable {

    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showConfirmDialog(activity,
                R.drawable.ic_launcher, "my dialog title", "desc", true,
                DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());
    performClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE));
    assertTrue(clicked);
    assertFalse(cancel);
    assertFalse(dialog.isShowing());
  }

  public void testShowConfirmDialogCancel() throws Throwable {
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showConfirmDialog(activity,
                R.drawable.ic_launcher, "my dialog title", "desc", true,
                DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());
    performClick(dialog.getButton(AlertDialog.BUTTON_NEGATIVE));
    assertTrue(cancel);
    assertFalse(clicked);
    assertFalse(dialog.isShowing());
  }

  public void testShowConfirmDialogNotCancellable() throws Throwable {
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showConfirmDialog(activity,
                R.drawable.ic_launcher, "my dialog title", "desc", false,
                DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog.onBackPressed();
      }
    });
    getInstrumentation().waitForIdleSync();
    assertFalse(cancel);
    assertFalse(clicked);
    assertTrue(dialog.isShowing());
  }

  public void testShowConfirmDialogCancellable() throws Throwable {
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showConfirmDialog(activity,
                R.drawable.ic_launcher, "my dialog title", "desc", true,
                DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog.onBackPressed();
      }
    });
    getInstrumentation().waitForIdleSync();
    assertTrue(cancel);
    assertFalse(clicked);
    assertFalse(dialog.isShowing());
  }

  public void testShowAlertDialogOk() throws Throwable {
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showAlertDialog(activity, "title", "msg", true,
                DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());
    performClick(dialog.getButton(AlertDialog.BUTTON_POSITIVE));
    assertTrue(clicked);
    assertFalse(cancel);
    assertFalse(dialog.isShowing());
  }

  public void testShowAlertDialogNegativeButton() throws Throwable {
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showAlertDialog(activity, "title", "msg", true,
                DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());

    assertEquals(View.GONE,
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).getVisibility());
  }

  public void testShowAlertDialogCancel() throws Throwable {
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showAlertDialog(activity, "title", "msg",
                true, DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());
    // close it with back key
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog.onBackPressed();
      }
    });
    getInstrumentation().waitForIdleSync();
    assertTrue(cancel);
    assertFalse(clicked);
    assertFalse(dialog.isShowing());

  }

  public void testShowAlertDialogCancellable() throws Throwable {
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog = DialogUtils.get().showAlertDialog(activity, "title", "msg",
                false, DialogUtilsTest.this, DialogUtilsTest.this);
      }
    });

    getInstrumentation().waitForIdleSync();
    assertTrue(dialog.isShowing());
    runTestOnUiThread(new Runnable() {

      @Override
      public void run() {
        dialog.onBackPressed();
      }
    });
    getInstrumentation().waitForIdleSync();
    assertFalse(cancel);
    assertFalse(clicked);
    assertTrue(dialog.isShowing());
  }

  // We don't need to test showMenuDialog. Most of them are the same as alert or
  // confirm dialog. So, we leave showMenuDialog testing at DialogMenu*Tests.
}
