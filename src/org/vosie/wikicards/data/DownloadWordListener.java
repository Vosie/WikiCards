package org.vosie.wikicards.data;

public interface DownloadWordListener {

  public static final int HTTP_ERROR = 0;
  public static final int INTERNAL_ERROR = 1;
  public static final int NETWORK_ERROR = 2;

  public void onWordReceived(Word word);

  public void onError(int errorType, Exception e);
}
