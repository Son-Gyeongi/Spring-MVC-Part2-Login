package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

// 서블릿 필터 - 인증 체크
@Slf4j
public class LoginCheckFilter implements Filter {
    // Filter의 default메서드는 인터페이스이지만 구현하지 않아도 된다.

    /**
     * 로그인 안 한 사용자가 접근할 수 있는 url (허용)
     * '/'는 로그인과 상관없이 들어와야 한다.
     * '/members/add' 로그인 안 한 사용자 가입 할 수 있게 해야한다.
     */
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout", "/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        // 경로 받자
        String requestURI = httpRequest.getRequestURI();

        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // 로직 작성
        try {
            // 인증이 잘 동작하는 지 체크
            log.info("인증 체크 필터 시작 = {}", requestURI);

            // whitelist면 if문 안 타고 바로 chain.doFilter로 이동
            // 로그인 체크 해야하는 경로인지 확인 (체크 로직)
            if (isLoginCheckPath(requestURI)) {
                log.info("인증 체크 로직 실행 = {}", requestURI);
                HttpSession session = httpRequest.getSession(false);
                if (session == null || session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                    log.info("미인증 사용자 요청 ={}", requestURI);
                    // 로그인으로 redirect
                    httpResponse.sendRedirect("/login?redirectURL=" + requestURI); // 로그인 페이지로 보내고 로그인 후 다시 해당 페이지로 온다.
                    return; // 중요. 서블릿이나 컨트롤러 호출 하지 않겠다. 미인증 사용자는 다음으로 진행하지 않고 끝
                }
            }

            chain.doFilter(request, response);
        } catch (Exception e) { // 예외 로깅 가능 하지만, 톰캣까지 예외를 보내주어야 한다.
            throw e;
        } finally {
            log.info("인증 체크 필터 종료 = {}", requestURI);
        }
    }

    /**
     * 인증 체크 메서드
     * 화이트 리스트의 경우 인증 체크 안한다.
     * 로그인 체크해야하는 패스(경로)인지 확인
     */
    private boolean isLoginCheckPath(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }
}
