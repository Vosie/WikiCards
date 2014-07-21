package org.vosie.wikicards.data;

public class HTTPException extends Exception {
  
  private static final long serialVersionUID = 1L;
  private int mStatus;
  private String mUrl;

  public HTTPException(int status, String url, String message) {
    super(message);
    mStatus = status;
    mUrl = url;
  }
  
  public int getStatus(){
    return mStatus;
  }
  
  public String getUrl(){
    return mUrl;
  }
}
