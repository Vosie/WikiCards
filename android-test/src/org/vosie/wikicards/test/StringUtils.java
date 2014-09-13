package org.vosie.wikicards.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.io.IOUtils;

public class StringUtils {
  public static String readAll(File f) throws IOException {
    if (!f.exists()) {
      return null;
    }

    // This is a dangerous behavior. A file may have file size larger than int.
    // But we cast it as int which may loose some data file. In test case, it is
    // fine to do so. But we shouldn't do it in production code.
    byte[] data = new byte[(int) f.length()];
    FileInputStream is = new FileInputStream(f);
    try {
      IOUtils.readFully(is, data);
      return new String(data);
    } finally {
      is.close();
    }

  }
}
