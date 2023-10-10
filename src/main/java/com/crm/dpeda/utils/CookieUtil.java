package com.crm.dpeda.utils;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Cookie工具类
 */
public class CookieUtil {

	/**
	 * 设置Cookie
	 * @param key Cookie名称
	 * @param value Cookie Value
	 * @param domain
	 * @param response
	 * @return void
	 */
	public static void setCookie(String key, String value, String domain, HttpServletResponse response) {
		try {
			value = URLEncoder.encode(value, "UTF-8");
			if (StringUtils.isNotBlank(value)) {
                value = value.replaceAll("\\+", "%20");
			}
			Cookie cookie = new Cookie(key, value);
			cookie.setMaxAge(-1);
			cookie.setPath("/");
			cookie.setDomain(domain);
			response.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 设置Cookie
	 * @param key Cookie名称
	 * @param value Cookie Value
	 * @param domain
	 * @param response
	 * @return void
	 */
	public static void setCookieNoEncode(String key, String value, String domain, HttpServletResponse response) {
		Cookie cookie = new Cookie(key, value);
		cookie.setMaxAge(-1);
		cookie.setPath("/");
		cookie.setDomain(domain);
		response.addCookie(cookie);
	}

	/**
	 * 获取Cookie
	 * 注意：这样有个问题，这里找到我们的cookies都是我们之前加密过的cookie,需要我们进行解码
	 * 		就要需要我们调用 LoginUserUtil 类 解决
	 * @param request
	 * @param key
	 * @return java.lang.String
	 */
	public static String getCookieValue(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		Cookie cookie = null;
		//注意这里是从多个cookies中找到你的要的那个cookies,在数组中循环找
		if (cookies != null) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(key)) {
					cookie = cookies[i];
				}
			}
		}
		//判断你要的cookie是否为空
		if (cookie != null) {
			try {
				return URLDecoder.decode(cookie.getValue(), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 清除cookie
	 * @param cookieName
	 * @param request
	 * @param response
	 * @return void
	 */
	public static void deleteCookie(String cookieName, HttpServletRequest request, HttpServletResponse response) {
		Cookie[] arr_cookie = request.getCookies();
		if (arr_cookie != null && arr_cookie.length > 0) {
			for (Cookie cookie : arr_cookie) {
				if (cookie.getName().equals(cookieName)) {
					cookie.setValue("");
					cookie.setMaxAge(0);
					cookie.setPath("/");
					response.addCookie(cookie);
				}
			}
		}
	}
}
