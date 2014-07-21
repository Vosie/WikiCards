package org.vosie.wikicards.data;

public interface DownloadDBListener {
  public static final int HTTP_ERROR = 0;
  public static final int INTERNAL_ERROR = 1;
  public static final int NETWORK_ERROR = 2;
  
  public void onProgressing(int progress);

  public void onComplete();

  public void onError(int errorType, Exception e);
}
