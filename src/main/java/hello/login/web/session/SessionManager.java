package hello.login.web.session;

import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 세션 관리해주는 기능
 */
@Component // 스프링 빈으로 등록
public class SessionManager {

    public static final String SESSION_COOKIE_NAME = "mySessionId";
    // String - sessionId, Object - 객체를 보관
    private Map<String, Object> sessionStore = new ConcurrentHashMap<>();
    // ConcurrentHashMap<>는 동시성 이슈로 인해서 사용, 동시에 여러 요청/여러 쓰레드가 sessionStore에 접근할 거다.

    /**
     * 세션 생성 - 객체인 Object에 값을 넣으면 세션을 만들어준다.
     * * sessionId 생성 (임의의 추정 불가능한 랜덤 값)
     * * 세션 저장소에 sessionId와 보관할 값 저장
     * * sessionId로 응답 쿠키를 생성해서 클라이언트에 전달
     */
    public void createSession(Object value, HttpServletResponse response) {

        // 세션 id를 생성하고, 값을 세션에 저장
        String sessionId = UUID.randomUUID().toString(); // 랜덤값 만들기
        sessionStore.put(sessionId, value);

        // 쿠키 생성
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        response.addCookie(mySessionCookie);
    }

    /**
     * 세션 조회
     */
    public Object getSession(HttpServletRequest request) {
        // 쿠키 찾기
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie == null) {
            return null;
        }

        // 성공 로직
        return sessionStore.get(sessionCookie.getValue());
    }

    /**
     * 세션 만료
     */
    public void expire(HttpServletRequest request) {
        // 쿠키 찾기
        Cookie sessionCookie = findCookie(request, SESSION_COOKIE_NAME);
        if (sessionCookie != null) {
            sessionStore.remove(sessionCookie.getValue());
        }
    }

    // 쿠키 찾기
    public Cookie findCookie(HttpServletRequest request, String cookieName) {
        /*
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(SESSION_COOKIE_NAME)) {
                return sessionStore.get(cookie.getValue());
            }
        }
         */

        if (request.getCookies() == null) {
            return null;
        }
        // Arrays.stream() - 배열을 stream으로 바꿔준다.
        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findAny()
                .orElse(null);
    }
}
