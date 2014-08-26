package org.vosie.wikicards.utils.test;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.vosie.wikicards.MainActivity;
import org.vosie.wikicards.utils.JSONUtils;

import android.content.Context;
import android.test.ActivityInstrumentationTestCase2;

public class JSONUtilsTest extends
                          ActivityInstrumentationTestCase2<MainActivity> {

  private File tmpFolder;

  public JSONUtilsTest() {
    super(MainActivity.class);
  }

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    tmpFolder = this.getInstrumentation().getTargetContext().getDir("tmp",
            Context.MODE_MULTI_PROCESS);
  }
  
  @Override
  protected void tearDown() throws Exception {
    tmpFolder.delete();
    super.tearDown();
  }

  public void testLoadJSONObjectFromFile() throws IOException, JSONException {
    File dummyFile = new File(tmpFolder, "dummyFile");
    try {
      FileUtils.write(dummyFile, "{\"a\": true, \"b\": \"text\"}");

      JSONObject obj = JSONUtils.loadJSONObjectFromFile(
              dummyFile.getAbsolutePath());
      assertNotNull(obj);
      assertTrue(obj.has("a"));
      assertTrue(obj.getBoolean("a"));
      assertTrue(obj.has("b"));
      assertEquals("text", obj.getString("b"));
    } finally {
      dummyFile.delete();
    }
  }

  public void testLoadJSONObjectFromFileBrokenJSON() throws IOException {
    File dummyFile = new File(tmpFolder, "brokenJSONFile");

    try {
      FileUtils.write(dummyFile, "{");

      JSONUtils.loadJSONObjectFromFile(dummyFile.getAbsolutePath());
      assertTrue("We should get a JSONException here", false);
    } catch (JSONException ex) {
    } finally {
      dummyFile.delete();
    }
  }

  public void testLoadJSONObjectFromFileNoFile() throws JSONException {
    try {
      JSONUtils.loadJSONObjectFromFile("/dummy/??");
      assertTrue("We should get a IOException here", false);
    } catch (IOException ex) {
    }
  }

}
