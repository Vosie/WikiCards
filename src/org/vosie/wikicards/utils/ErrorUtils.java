package org.vosie.wikicards.utils;

import org.vosie.wikicards.R;
import org.vosie.wikicards.data.DownloadWordListener;

import android.app.Activity;

public class ErrorUtils {

  public static void handleDownloadkError(Activity ctx, int errorType,
          boolean closeSelf) {
    String title = "";
    String desc = "";
    switch (errorType) {
      case DownloadWordListener.NETWORK_ERROR:
        title = ctx.getString(R.string.dialog_title_network_error);
        desc = ctx.getString(R.string.dialog_desc_network_error);
        break;
      case DownloadWordListener.HTTP_ERROR:
        title = ctx.getString(R.string.dialog_title_http_error);
        desc = ctx.getString(R.string.dialog_desc_http_error);
        break;
      case DownloadWordListener.INTERNAL_ERROR:
        title = ctx.getString(R.string.dialog_title_internal_error);
        desc = ctx.getString(R.string.dialog_desc_internal_error);
        break;
    }
    DialogUtils.showAlertDialog(ctx, title, desc, false, closeSelf);
  }

}
