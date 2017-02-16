package com.huiyin.bean;

public class UpdataInfo {

	private String version;
	private String url;
	private String description;

	private int versionCode;

	public int getVersionCode() {
		return versionCode;
	}

	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "UpdataInfo [version=" + version + ", url=" + url
				+ ", description=" + description + ", versionCode="
				+ versionCode + "]";
	}

}
