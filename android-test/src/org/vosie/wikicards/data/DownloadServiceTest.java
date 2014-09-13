package org.vosie.wikicards.data;

import java.io.File;

import org.vosie.wikicards.test.StringUtils;
import org.vosie.wikicards.test.mock.SimpleWebServer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.test.ServiceTestCase;
import fi.iki.elonen.NanoHTTPD.Response;

public class DownloadServiceTest extends ServiceTestCase<DownloadService> {

  private static final int TEST_SERVER_PORT = 8088;
  private static final String TEST_SERVER_URI = "http://127.0.0.1:" +
          TEST_SERVER_PORT;
  private SimpleWebServer webServer;
  private int returnedResultCode;
  private Exception exceptionInService;
  private ResultReceiver resultReceiver;
  private File dummyFile;

  public DownloadServiceTest() {
    super(DownloadService.class);
  }

  @Override
  protected void setUp() throws Exception {
    webServer = new SimpleWebServer(TEST_SERVER_PORT);
    // ResultReceiver calls onReceiveResult immediate when we give null as the
    // "handle"(first) argument.
    resultReceiver = new ResultReceiver(null) {
      @Override
      protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (DownloadService.UPDATE_PROGRESS == resultCode) {
          return;
        }
        returnedResultCode = resultCode;
        if (null != resultData &&
                resultData.containsKey(DownloadService.PARAM_EXCEPTION)) {
          exceptionInService = (Exception) resultData
                  .getSerializable(DownloadService.PARAM_EXCEPTION);
        } else {
          exceptionInService = null;
        }
        synchronized (DownloadServiceTest.this) {
          DownloadServiceTest.this.notify();
        }
        super.onReceiveResult(resultCode, resultData);
      }
    };
    File output = this.getContext().getDir("downloadTest",
            Context.MODE_MULTI_PROCESS);
    dummyFile = new File(output, "a.txt");
    super.setUp();
  }

  @Override
  protected void tearDown() throws Exception {
    dummyFile.delete();
    webServer = null;
    super.tearDown();
  }

  public void testNormalDownlaod() throws Exception {
    Response res = new Response(Response.Status.OK, "text/plain", "ok");

    String testPath = "/a";
    webServer.urlToDataMap.put(testPath, res);
    webServer.start();

    Intent i = new Intent();
    i.putExtra("url", TEST_SERVER_URI + testPath);
    i.putExtra("receiver", resultReceiver);
    i.putExtra("destination", dummyFile.getAbsolutePath());

    this.startService(i);
    synchronized (this) {
      // we only wait for 10 seconds to finish this task.
      this.wait(10 * 1000);
    }
    this.shutdownService();
    assertEquals(DownloadService.DOWNLOAD_COMPLETE, returnedResultCode);
    assertEquals("ok", StringUtils.readAll(dummyFile));
    webServer.urlToDataMap.clear();
    webServer.stop();
  }

  public void testNotFoundDownlaod() throws Exception {
    String testPath = "/a";
    webServer.start();

    Intent i = new Intent();
    i.putExtra("url", TEST_SERVER_URI + testPath);
    i.putExtra("receiver", resultReceiver);

    i.putExtra("destination", dummyFile.getAbsolutePath());

    this.startService(i);
    synchronized (this) {
      // we only wait for 10 seconds to finish this task.
      this.wait(10 * 1000);
    }
    this.shutdownService();
    assertEquals(DownloadService.HTTP_ERROR, returnedResultCode);
    assertNotNull(exceptionInService);
    assertTrue(exceptionInService instanceof HTTPException);
    HTTPException httpEx = (HTTPException) exceptionInService;
    assertEquals(404, httpEx.getStatus());
    assertEquals(TEST_SERVER_URI + testPath, httpEx.getUrl());
    webServer.urlToDataMap.clear();
    webServer.stop();
  }

  public void testWrongDestinationDownlaod() throws Exception {
    Response res = new Response(Response.Status.OK, "text/plain", "ok");

    String testPath = "/a";
    webServer.urlToDataMap.put(testPath, res);
    webServer.start();

    Intent i = new Intent();
    i.putExtra("url", TEST_SERVER_URI + testPath);
    i.putExtra("receiver", resultReceiver);
    i.putExtra("destination", "/wrong destination");

    this.startService(i);
    synchronized (this) {
      // we only wait for 10 seconds to finish this task.
      this.wait(10 * 1000);
    }
    this.shutdownService();
    assertEquals(DownloadService.INTERNAL_ERROR, returnedResultCode);
    webServer.urlToDataMap.clear();
    webServer.stop();
  }
}
