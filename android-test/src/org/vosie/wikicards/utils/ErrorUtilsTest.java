package org.vosie.wikicards.utils;

import junit.framework.TestCase;

import org.vosie.wikicards.R;
import org.vosie.wikicards.data.DownloadWordListener;
import org.vosie.wikicards.test.mock.MockActivity;
import org.vosie.wikicards.test.mock.MockContext;
import org.vosie.wikicards.test.mock.MockResources;
import org.vosie.wikicards.test.mock.utils.MockDialogUtils;
import org.vosie.wikicards.utils.ErrorUtils;

public class ErrorUtilsTest extends TestCase {

  public void testHandleDownloadkErrorHttpError() {
    // use mockDialogs to override the original DialogUtils's singleton.
    MockDialogUtils mockDialogs = MockDialogUtils.createInstance();

    MockActivity mockActivity = new MockActivity();
    mockActivity.resources = new MockResources();

    ErrorUtils.get().handleDownloadkError(mockActivity,
            DownloadWordListener.HTTP_ERROR, true);

    assertEquals(mockActivity, mockDialogs.activity);
    assertEquals("" + R.string.dialog_title_http_error, mockDialogs.title);
    assertEquals("" + R.string.dialog_desc_http_error, mockDialogs.msg);
    assertFalse(mockDialogs.cancelable);
    assertTrue(mockDialogs.closeSelf);
  }
  
  public void testHandleDownloadkErrorInternalError() {
    // use mockDialogs to override the original DialogUtils's singleton.
    MockDialogUtils mockDialogs = MockDialogUtils.createInstance();

    MockActivity mockActivity = new MockActivity();
    mockActivity.resources = new MockResources();

    ErrorUtils.get().handleDownloadkError(mockActivity,
            DownloadWordListener.INTERNAL_ERROR, true);

    assertEquals(mockActivity, mockDialogs.activity);
    assertEquals("" + R.string.dialog_title_internal_error, mockDialogs.title);
    assertEquals("" + R.string.dialog_desc_internal_error, mockDialogs.msg);
    assertFalse(mockDialogs.cancelable);
    assertTrue(mockDialogs.closeSelf);
  }
  
  public void testHandleDownloadkErrorNetworkError() {
    // use mockDialogs to override the original DialogUtils's singleton.
    MockDialogUtils mockDialogs = MockDialogUtils.createInstance();

    MockActivity mockActivity = new MockActivity();
    mockActivity.resources = new MockResources();

    ErrorUtils.get().handleDownloadkError(mockActivity,
            DownloadWordListener.NETWORK_ERROR, true);

    assertEquals(mockActivity, mockDialogs.activity);
    assertEquals("" + R.string.dialog_title_network_error, mockDialogs.title);
    assertEquals("" + R.string.dialog_desc_network_error, mockDialogs.msg);
    assertFalse(mockDialogs.cancelable);
    assertTrue(mockDialogs.closeSelf);
  }

  public void testGetErrorTitle() {
    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String title = ErrorUtils.get().getErrorTitle(mock,
            DownloadWordListener.HTTP_ERROR);
    assertEquals("" + R.string.dialog_title_http_error, title);
    title = ErrorUtils.get().getErrorTitle(mock,
            DownloadWordListener.INTERNAL_ERROR);
    assertEquals("" + R.string.dialog_title_internal_error, title);
    title = ErrorUtils.get().getErrorTitle(mock,
            DownloadWordListener.NETWORK_ERROR);
    assertEquals("" + R.string.dialog_title_network_error, title);
  }

  public void testGetErrorTitleErrorCase() {
    try {
      ErrorUtils.get().getErrorTitle(null, 0);
      fail("An exception should be thrown when we give null as ctx");
    } catch (NullPointerException e) {
    }

    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String unknownError = ErrorUtils.get().getErrorTitle(mock, -1);
    assertEquals("", unknownError);
  }

  public void testGetErrorDesc() {
    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String desc = ErrorUtils.get().getErrorDesc(mock,
            DownloadWordListener.HTTP_ERROR);
    assertEquals("" + R.string.dialog_desc_http_error, desc);
    desc = ErrorUtils.get().getErrorDesc(mock,
            DownloadWordListener.INTERNAL_ERROR);
    assertEquals("" + R.string.dialog_desc_internal_error, desc);
    desc = ErrorUtils.get().getErrorDesc(mock,
            DownloadWordListener.NETWORK_ERROR);
    assertEquals("" + R.string.dialog_desc_network_error, desc);
  }

  public void testGetErrorDescErrorCase() {
    try {
      ErrorUtils.get().getErrorDesc(null, 0);
      fail("An exception should be thrown when we give null as ctx");
    } catch (NullPointerException e) {
    }

    MockContext mock = new MockContext();
    mock.resources = new MockResources();
    String unknownError = ErrorUtils.get().getErrorDesc(mock, -1);
    assertEquals("", unknownError);
  }

}
