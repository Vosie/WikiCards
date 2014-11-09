package org.vosie.wikicards.utils;

import org.vosie.wikicards.MainActivity;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;

public class CategoryUtilsTest extends
		ActivityInstrumentationTestCase2<MainActivity> {

	public CategoryUtilsTest() {
		super(MainActivity.class);
	}

	public void testGetName() {
		Activity act = getActivity();
		assertEquals(act.getString(CategoryUtils.CATEGORY_NAMES_ID[0]),
				CategoryUtils.getName(getActivity(),
						CategoryUtils.CATEGORY_COUNTRY));
		assertEquals(act.getString(CategoryUtils.CATEGORY_NAMES_ID[1]),
				CategoryUtils.getName(getActivity(),
						CategoryUtils.CATEGORY_ANIMAL));
		assertNull(CategoryUtils.getName(getActivity(), -999));
	}

	public void testGetResourceName() {
		assertEquals("country",
				CategoryUtils.getResourceName(CategoryUtils.CATEGORY_COUNTRY));
		assertEquals("animals",
				CategoryUtils.getResourceName(CategoryUtils.CATEGORY_ANIMAL));
		assertNull(CategoryUtils.getName(getActivity(), -999));
	}
}
