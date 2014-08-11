package org.vosie.wikicards.utils.test;

import junit.framework.TestCase;

import org.vosie.wikicards.R;
import org.vosie.wikicards.data.DownloadWordListener;
import org.vosie.wikicards.test.mock.MockContext;
import org.vosie.wikicards.test.mock.MockResources;
import org.vosie.wikicards.utils.ErrorUtils;

public class ErrorUtilsTest extends TestCase {

  public void testGetErrorTitle() {
    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String title = ErrorUtils.getErrorTitle(mock,
            DownloadWordListener.HTTP_ERROR);
    assertEquals("" + R.string.dialog_title_http_error, title);
    title = ErrorUtils.getErrorTitle(mock, DownloadWordListener.INTERNAL_ERROR);
    assertEquals("" + R.string.dialog_title_internal_error, title);
    title = ErrorUtils.getErrorTitle(mock, DownloadWordListener.NETWORK_ERROR);
    assertEquals("" + R.string.dialog_title_network_error, title);
  }

  public void testGetErrorTitleErrorCase() {
    try {
      ErrorUtils.getErrorTitle(null, 0);
      fail("An exception should be thrown when we give null as ctx");
    } catch (NullPointerException e) {
    }

    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String unknownError = ErrorUtils.getErrorTitle(mock, -1);
    assertEquals("", unknownError);
  }

  public void testGetErrorDesc() {
    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String desc = ErrorUtils.getErrorDesc(mock,
            DownloadWordListener.HTTP_ERROR);
    assertEquals("" + R.string.dialog_desc_http_error, desc);
    desc = ErrorUtils.getErrorDesc(mock, DownloadWordListener.INTERNAL_ERROR);
    assertEquals("" + R.string.dialog_desc_internal_error, desc);
    desc = ErrorUtils.getErrorDesc(mock, DownloadWordListener.NETWORK_ERROR);
    assertEquals("" + R.string.dialog_desc_network_error, desc);
  }

  public void testGetErrorDescErrorCase() {
    try {
      ErrorUtils.getErrorDesc(null, 0);
      fail("An exception should be thrown when we give null as ctx");
    } catch (NullPointerException e) {
    }

    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String unknownError = ErrorUtils.getErrorDesc(mock, -1);
    assertEquals("", unknownError);
  }

}
