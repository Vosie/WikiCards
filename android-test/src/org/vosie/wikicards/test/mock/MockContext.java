package org.vosie.wikicards.test.mock;

import android.content.res.Resources;

public class MockContext extends android.test.mock.MockContext {

	public Resources resources;

	@Override
	public Resources getResources() {
		return resources;
	}
}
