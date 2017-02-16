package com.huiyin.ui.introduce;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.huiyin.bean.BaseBean;

public class IntroduceBean extends BaseBean {

	private static final long serialVersionUID = 1L;

	@Expose
	private String content;
	@Expose
	private String title;

	/**
	 * 
	 * @return The content
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 *            The content
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 
	 * @return The title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 *            The title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	public static IntroduceBean explainJson(String json, Context context) {

		Gson gson = new Gson();
		try {
			IntroduceBean experLightBean = gson.fromJson(json,
					IntroduceBean.class);
			return experLightBean;
		} catch (Exception e) {
			Log.d("AppShowAddAppraise", e.toString());
			Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
			return null;

		}
	}
}
