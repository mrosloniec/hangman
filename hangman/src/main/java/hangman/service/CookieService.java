package hangman.service;

import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Service
public class CookieService {


	public Cookie createNewCookie(String name, String value) {
		return new Cookie(name, value);
	}

	public Cookie getCookie(Cookie[] cookies, String name) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(name)) {
					return cookie;
				}
			}
		}
		return null;
	}

	public void addCookie(HttpServletResponse response, String name, String value) {
		Cookie cookie = new Cookie(name, value);
		cookie.setPath("/hangman/");
		cookie.setMaxAge(2000000);
		response.addCookie(cookie);
	}

}
