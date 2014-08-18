package org.vosie.wikicards.test.mock;

public class MockResources extends android.test.mock.MockResources {
	@Override
	public String getString(int id) throws NotFoundException {
		return "" + id;
	}
}
