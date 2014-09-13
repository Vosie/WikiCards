package org.vosie.wikicards.test.mock;

import java.util.HashMap;
import java.util.Map;

import fi.iki.elonen.NanoHTTPD;

public class SimpleWebServer extends NanoHTTPD {
  public static final String TEXT_404 = "404 File Not Found";
  public Map<String, Response> urlToDataMap = new HashMap<String, Response>();

  public SimpleWebServer(int port) {
    super(port);
  }

  @Override
  public Response serve(IHTTPSession arg0) {
    if (urlToDataMap.containsKey(arg0.getUri())) {
      return urlToDataMap.get(arg0.getUri());
    } else {
      return new Response(Response.Status.NOT_FOUND, "text/plain", TEXT_404);
    }
  }

}
