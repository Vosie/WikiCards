package org.vosie.wikicards.utils;

import java.io.File;
import java.io.IOException;
import org.apache.commons.io.FileUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

  public static JSONObject loadJSONObjectFromFile(String filePath) 
          throws JSONException, IOException {
    return new JSONObject(FileUtils.readFileToString(new File(filePath)));
  }
}
