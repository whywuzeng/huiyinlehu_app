package com.huiyin.utils.imageupload;

import java.io.File;
import android.os.Environment;
import android.text.format.Time;

public class ImageFolder {

	public static File getTempImageName() {
		Time t = new Time("GMT+8");
		t.setToNow();
		int year = t.year;
		int month = t.month;
		int day = t.monthDay;
		int hour = t.hour;
		int minute = t.minute;
		int second = t.second;
		String filename = "" + year + month + day + hour + minute + second;

		File welcomeimgFlord = new File(Environment.getExternalStorageDirectory().getPath() + "/huiyinlehu/cache/tempImage");
		if (!welcomeimgFlord.exists()) {
			welcomeimgFlord.mkdirs();
		}

		File tempImage = new File(welcomeimgFlord, "thewomen" + filename + ".jpg");
		// Uri fileImageFilePath = Uri.fromFile(tempImage);

		return tempImage;
	}

}