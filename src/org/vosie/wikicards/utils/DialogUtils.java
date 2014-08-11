package org.vosie.wikicards.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;

public class DialogUtils {

  /**
   * show a confirm dialog
   * 
   * @param ctx
   *          the context object of this dialog
   * @param icon
   *          the icon id
   * @param title
   *          the title string
   * @param desc
   *          the description string
   * @param okText
   *          the text shown in ok button
   * @param cancelText
   *          the text shown in cancel button
   * @param cancelable
   *          to indicate if this dialog is cancelable
   * @param click
   *          the callback function which will be called when user clicks ok.
   * @param cancel
   *          the callback function which will be called when user cancels it or
   *          press cancel button.
   */
  public static void showConfirmDialog(Context ctx, int icon,
          CharSequence title, CharSequence desc,
          CharSequence okText, CharSequence cancelText, boolean cancelable,
          DialogInterface.OnClickListener click,
          final DialogInterface.OnCancelListener cancel) {

    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
    if (icon > 0) {
      builder.setIcon(icon);
    }
    builder.setCancelable(cancelable);
    builder.setTitle(title);
    builder.setMessage(desc);
    builder.setPositiveButton(okText, click);
    builder.setNegativeButton(cancelText, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (null != cancel) {
          cancel.onCancel(dialog);
        }
      }
    });
    builder.setOnCancelListener(cancel);
    builder.show();
  }

  public static void showConfirmDialog(Context ctx, int icon,
          CharSequence title, CharSequence desc, boolean cancelable,
          DialogInterface.OnClickListener click,
          final DialogInterface.OnCancelListener cancel) {

    showConfirmDialog(ctx, icon, title, desc,
            ctx.getString(android.R.string.ok),
            ctx.getString(android.R.string.cancel), cancelable, click, cancel);
  }

  public static void showConfirmDialog(Context ctx, int icon,
          int title, int desc, int okText, int cancelText, boolean cancelable,
          DialogInterface.OnClickListener click,
          final DialogInterface.OnCancelListener cancel) {

    showConfirmDialog(ctx, icon, ctx.getString(title), ctx.getString(desc),
            ctx.getString(okText), ctx.getString(cancelText), cancelable,
            click, cancel);
  }

  public static void showConfirmDialog(Context ctx, int icon,
          int title, int desc, boolean cancelable,
          DialogInterface.OnClickListener click,
          final DialogInterface.OnCancelListener cancel) {

    showConfirmDialog(ctx, icon, ctx.getString(title), ctx.getString(desc),
            cancelable, click, cancel);
  }

  /**
   * shows an alert dialog
   * 
   * @param ctx
   *          the context of this alert dialog
   * @param title
   *          the title text
   * @param msg
   *          the message
   * @param cancelable
   *          to indicate if this dialog is cancelable.
   * @param click
   *          the callback function which will be called when user clicks ok.
   * @param cancel
   *          the callback function which will be called when user cancels it or
   *          press cancel button.
   */
  public static void showAlertDialog(Context ctx,
          CharSequence title, CharSequence msg, boolean cancelable,
          DialogInterface.OnClickListener click,
          final DialogInterface.OnCancelListener cancel) {
    Builder builder = new AlertDialog.Builder(ctx);
    builder.setTitle(title);
    builder.setMessage(msg);
    builder.setPositiveButton(android.R.string.ok, click);
    builder.setOnCancelListener(cancel);
    builder.setCancelable(cancelable);
    builder.show();
  }

  /**
   * show alert dialog only with message and make this dialog cancelable.
   * @param ctx
   * @param title
   * @param msg
   */
  public static void showAlertDialog(Context ctx, int title, int msg) {
    showAlertDialog(ctx, ctx.getResources().getString(title),
            ctx.getResources().getText(msg), true, null, null);
  }

  /**
   * shows an alert dialog with built-in click callback function to close the
   * activity or not.
   * 
   * @param ctx
   *          the activity
   * @param title
   *          the title
   * @param msg
   *          the message
   * @param cancelable
   *          to indicate if this dialog is cancelable
   * @param closeSelf
   *          to indicate if it should close activity when user click ok.
   */
  public static void showAlertDialog(final Activity ctx,
          CharSequence title, CharSequence msg, boolean cancelable,
          final boolean closeSelf) {
    showAlertDialog(ctx, title, msg, cancelable, new OnClickListener() {

      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (which == Dialog.BUTTON_POSITIVE && closeSelf) {
          ctx.finish();
        }
      }
    }, null);
  }

  public static void showMenuDialog(Context ctx, int icon, int title,
          boolean cancelable, final DialogMenuItem[] menus,
          final DialogMenuListener listener) {

    showMenuDialog(ctx, icon, ctx.getString(title), cancelable, menus,
            listener);
  }

  /**
   * show menu dialog
   * 
   * @param ctx
   *          the context of this dialog
   * @param icon
   *          the icon of this dialog. If the value is 0, it won't shows any
   *          icon for it.
   * @param title
   *          the title
   * @param cancelable
   *          to indicate if this dialog is cancelable
   * @param menus
   *          the menu list
   * @param listener
   *          the click and cancel listener.
   */
  public static void showMenuDialog(Context ctx, int icon,
          CharSequence title, boolean cancelable, final DialogMenuItem[] menus,
          final DialogMenuListener listener) {

    Builder builder = new AlertDialog.Builder(ctx);
    if (icon > 0) {
      builder.setIcon(icon);
    }
    builder.setTitle(title);
    builder.setCancelable(cancelable);
    builder.setAdapter(new DialogMenuAdapter(menus, ctx), new OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if (null != listener) {
          listener.onMenuItemClicked(menus[which]);
        }
      }
    });

    builder.setOnCancelListener(new OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialog) {
        if (null != listener) {
          listener.onDialogCancelled();
        }
      }
    });
    builder.show();
  }
}
