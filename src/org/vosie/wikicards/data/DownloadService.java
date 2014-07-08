package org.vosie.wikicards.data;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.io.IOUtils;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

public class DownloadService extends IntentService {

  public static final int UPDATE_PROGRESS = 0;
  public static final int DOWNLOAD_COMPLETE = 1;
  public static final int HTTP_ERROR = 2;
  public static final int INTERNAL_ERROR = 3;

  public static final String PARAM_EXCEPTION = "exception";
  public static final String PARAM_PROGRESS = "progress";
  private ResultReceiver mReceiver;

  public DownloadService() {
    super("DownloadService");
  }

  @Override
  protected void onHandleIntent(Intent intent) {
    String urlToDownload = intent.getStringExtra("url");
    mReceiver = (ResultReceiver) intent.getParcelableExtra("receiver");

    try {
      // In order to report download progress, we use our-owned version to
      // copy byte by byte.
      copyFileFromUrl(urlToDownload, intent.getStringExtra("destination"));
      mReceiver.send(DOWNLOAD_COMPLETE, null);

    } catch (IOException e) {
      sendError(INTERNAL_ERROR, urlToDownload, e);
      e.printStackTrace();
    } catch (HTTPException e) {
      sendError(HTTP_ERROR, urlToDownload, e);
      e.printStackTrace();
    }
  }

  private void sendError(int type, String url, Exception e) {
    Bundle errorBundle = new Bundle();
    errorBundle.putSerializable(PARAM_EXCEPTION, e);
    mReceiver.send(HTTP_ERROR, errorBundle);
  }

  private void copyFileFromUrl(String urlToDownload, String destination)
          throws IOException, HTTPException {
    URL url = new URL(urlToDownload);
    HttpURLConnection httpConnection = null;
    InputStream input = null;
    OutputStream output = null;
    try {
      httpConnection = (HttpURLConnection) url.openConnection();
      if (httpConnection.getResponseCode() != HttpURLConnection.HTTP_OK) {
        int status = httpConnection.getResponseCode();
        throw new HTTPException(status, urlToDownload,
                String.valueOf(status) + " " 
                        + httpConnection.getResponseMessage());
      }

      int fileLength = httpConnection.getContentLength();
      input = new BufferedInputStream(url.openStream());
      output = new FileOutputStream(destination);
      byte data[] = new byte[512 * 1024];
      long total = 0;
      int count;

      while ((count = input.read(data)) != -1) {
        total += count;
        Bundle resultData = new Bundle();
        resultData.putInt(PARAM_PROGRESS, (int) (total * 100 / fileLength));
        mReceiver.send(UPDATE_PROGRESS, resultData);
        output.write(data, 0, count);
      }

      output.flush();
      if (total != fileLength) {
        String msg = "Total download count is not match file length," +
                " total: " + String.valueOf(total) +
                " fileLength:" + String.valueOf(fileLength);
        throw new HTTPException(httpConnection.getResponseCode(), url.getPath(), 
                msg);
      }
    } finally {
      IOUtils.closeQuietly(input);
      IOUtils.closeQuietly(output);
      if (httpConnection != null) {
        httpConnection.disconnect();
      }
    }
  }
}
