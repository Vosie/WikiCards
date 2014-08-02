package org.vosie.wikicards.utils;

import org.vosie.wikicards.R;
import org.vosie.wikicards.data.DownloadWordListener;

import android.app.Activity;

public class ErrorUtils {

  public static void handleDownloadkError(Activity ctx, int errorType,
          boolean closeSelf) {
    String title = getErrorTitle(ctx, errorType);
    String desc = getErrorDesc(ctx, errorType);
    DialogUtils.showAlertDialog(ctx, title, desc, false, closeSelf);
  }
  
  public static String getErrorTitle(Activity ctx,int errorType){
    String title = "";
    switch (errorType) {
      case DownloadWordListener.NETWORK_ERROR:
        title += ctx.getString(R.string.dialog_title_network_error);
        break;
      case DownloadWordListener.HTTP_ERROR:
        title += ctx.getString(R.string.dialog_title_http_error);
        break;
      case DownloadWordListener.INTERNAL_ERROR:
        title += ctx.getString(R.string.dialog_title_internal_error);
        break;
    }
    return title;
  }
  
  public static String getErrorDesc(Activity ctx,int errorType){
    String desc = "";
    switch (errorType) {
      case DownloadWordListener.NETWORK_ERROR:
        desc += ctx.getString(R.string.dialog_desc_network_error);
        break;
      case DownloadWordListener.HTTP_ERROR:
        desc += ctx.getString(R.string.dialog_desc_http_error);
        break;
      case DownloadWordListener.INTERNAL_ERROR:
        desc += ctx.getString(R.string.dialog_desc_internal_error);
        break;
    }
    return desc;
  }

}
