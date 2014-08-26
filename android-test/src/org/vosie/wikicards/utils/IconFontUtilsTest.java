package org.vosie.wikicards.utils;

import junit.framework.TestCase;

import org.vosie.wikicards.utils.IconFontUtils;

public class IconFontUtilsTest extends TestCase {
  public void testGet() {
    assertEquals(String.valueOf((char) IconFontUtils.WIKIPEDIA) + " test",
            IconFontUtils.get(IconFontUtils.WIKIPEDIA, "test"));
  }
}
