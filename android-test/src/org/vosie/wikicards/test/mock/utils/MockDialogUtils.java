package org.vosie.wikicards.test.mock.utils;

import org.vosie.wikicards.utils.DialogUtils;

import android.app.Activity;
import android.content.Context;

public class MockDialogUtils extends DialogUtils {
  public static MockDialogUtils createInstance() {
    MockDialogUtils ret = new MockDialogUtils();
    synchronized (DialogUtils.class) {
      DialogUtils.instance = ret;
    }
    return ret;
  }

  public Activity activity;
  public Context context;
  public CharSequence title;
  public CharSequence msg;
  public boolean cancelable;
  public boolean closeSelf;

  @Override
  public void showAlertDialog(Activity ctx, CharSequence title,
          CharSequence msg, boolean cancelable, boolean closeSelf) {

    this.activity = ctx;
    this.title = title;
    this.msg = msg;
    this.cancelable = cancelable;
    this.closeSelf = closeSelf;
  }
}
