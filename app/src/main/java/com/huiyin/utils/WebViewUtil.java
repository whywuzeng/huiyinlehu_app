package com.huiyin.utils;

import com.huiyin.api.URLs;

public class WebViewUtil {
	
	/**
	 * 初始化webView加载数据
	 * 
	 * @param str
	 * @return
	 */
	public static String initWebView(String str) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta id=\"viewport\" name=\"viewport\" content=\"width=device-width,height=device-height,initial-scale=1.0,maximum-scale=1.0,user-scalable=yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
		buffer.append("<meta name=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />");
		buffer.append("<title>webview</title>");
		buffer.append("<base href=\"" + URLs.IMAGE_URL + "\" />");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<div style=\"line-height:1.5em;color:#707070;padding:0 0 10px 0\">");
		buffer.append(str);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String initWebView(String title, String str) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta id=\"viewport\" name=\"viewport\" content=\"width=device-width,height=device-height,initial-scale=0.5,maximum-scale=1.0,user-scalable=yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
		buffer.append("<meta name=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />");
		buffer.append("<title>webview</title>");
		buffer.append("<base href=\"" + URLs.IMAGE_URL + "\" />");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<h3 style=\"text-align:center;color:#333333;font-size:28px;\">");
		buffer.append(title);
		buffer.append("</h3>");
		buffer.append("<div style=\"line-height:1.5em;color:#707070;padding:0 0 10px 0\">");
		buffer.append(str);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String initWebView(String title, String time, String str) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta id=\"viewport\" name=\"viewport\" content=\"width=device-width,height=device-height,initial-scale=1.0,maximum-scale=1.0,user-scalable=yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
		buffer.append("<meta name=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />");
		buffer.append("<title>webview</title>");
		buffer.append("<base href=\"" + URLs.IMAGE_URL + "\" />");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<h7 style=\"text-align:left;display:block;margin-bottom:4px;color:#333333;font-size:16px;\">");
		buffer.append(title);
		buffer.append("</h7>");
		buffer.append("<h7 style=\"text-align:left;display:block;color:#666666;font-size:10px;\">");
		buffer.append(time);
		buffer.append("</h7>");
		buffer.append("<hr style=\"color:#707070;\"/>");
		// buffer.append("<div style=\"line-height:1.5em;color:#707070;padding:0 0 10px 0\">");
		buffer.append(str);
		// buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String initWebView(String title, String time, String author, String str) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta id=\"viewport\" name=\"viewport\" content=\"width=device-width,height=device-height,initial-scale=1.0,maximum-scale=1.0,user-scalable=yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
		buffer.append("<meta name=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />");
		buffer.append("<title>webview</title>");
		buffer.append("<base href=\"" + URLs.IMAGE_URL + "\" />");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<h7 style=\"text-align:left;display:block;margin-bottom:4px;color:#333333;font-size:14px;\">");
		buffer.append(title);
		buffer.append("</h7>");
		buffer.append("<h7 style=\"text-align:left;display:block;color:#666666;font-size:10px;\">");
		buffer.append("published:" + time);
		buffer.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		buffer.append("Author:" + author);
		buffer.append("</h7>");
		buffer.append("<hr style=\"color:#707070;\"/>");
		// buffer.append("<div style=\"line-height:1.5em;color:#707070;font-size:24px;padding:0 0 10px 0\">");
		buffer.append(str);
		// buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String initWebView2(String title, String time, String author) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta id=\"viewport\" name=\"viewport\" content=\"width=device-width,height=device-height,initial-scale=1.0,maximum-scale=1.0,user-scalable=yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
		buffer.append("<meta name=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />");
		buffer.append("<title>webview</title>");
		buffer.append("<base href=\"" + URLs.IMAGE_URL + "\" />");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<h3 style=\"text-align:center;color:#333333;font-size:28px;\">");
		buffer.append(title);
		buffer.append("</h3>");
		buffer.append("<h4 style=\"text-align:center;width:100%;display:block;color:#666666;font-size:20px;\">");
		buffer.append("published:" + time);
		buffer.append("&nbsp;&nbsp;&nbsp;&nbsp;");
		buffer.append("Author:" + author);
		buffer.append("</h4>");
		buffer.append("<hr style=\"color:#707070;\"/>");

		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String initWebViewFor19(String str) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta id=\"viewport\" name=\"viewport\" content=\"width=device-width,height=device-height,initial-scale=0.1,maximum-scale=1.0,user-scalable=false\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
		buffer.append("<meta name=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />");
		buffer.append("<style>img{width:100%;}</style>");
		buffer.append("<style>table{width:100%;}</style>");
		buffer.append("<title>webview</title>");
		buffer.append("<base href=\"" + URLs.IMAGE_URL + "\" />");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<div style=\"line-height:1.5em;color:#707070;padding:0 0 10px 0\">");
		buffer.append(str);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}

	public static String initWebViewForClub(String content, String about) {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<!DOCTYPE html>");
		buffer.append("<html>");
		buffer.append("<head>");
		buffer.append("<meta charset=\"utf-8\">");
		buffer.append("<meta id=\"viewport\" name=\"viewport\" content=\"width=device-width,height=device-height,initial-scale=0.1,maximum-scale=1.0,user-scalable=false\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-capable\" content=\"yes\" />");
		buffer.append("<meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\" />");
		buffer.append("<meta name=\"black\" name=\"apple-mobile-web-app-status-bar-style\" />");
		buffer.append("<style>img{width:100%;}</style>");
		buffer.append("<style>table{width:100%;}</style>");
		buffer.append("<title>webview</title>");
		buffer.append("<base href=\"" + URLs.IMAGE_URL + "\" />");
		buffer.append("</head>");
		buffer.append("<body>");
		buffer.append("<div style=\"line-height:1.5em;color:#00000000;padding:0 0 10px 0\">");
		buffer.append(content);
		buffer.append("</div>");
		buffer.append("<div style=\"line-height:1.5em;color:#00000000;padding:0 0 10px 0\">");
		buffer.append(about);
		buffer.append("</div>");
		buffer.append("</body>");
		buffer.append("</html>");
		return buffer.toString();
	}
	
}
