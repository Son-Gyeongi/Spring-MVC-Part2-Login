package hello.login.web.filter;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.UUID;

@Slf4j
public class LogFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("log filter init");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        // HTTP 요청이 올 때마다 doFilter()가 먼저 호출된다.
        log.info("log filter doFilter");

        HttpServletRequest httpRequest = (HttpServletRequest) request; // 부모 ServletRequest, 자식 HttpServletRequest
        // 모든 사용자의 요청 URI를 남겨보자
        String requestURI = httpRequest.getRequestURI();

        // 요청온 거 구분하기 위해서 UUDI 사용
        String uuid = UUID.randomUUID().toString();

        try {
            log.info("REQUEST [{}][{}]", uuid, requestURI);
            chain.doFilter(request, response); // 다음 필터 호출 (다음 필터 있으면 필터 호출, 없으면 서블릿 호출)
        } catch (Exception e) {
            throw e;
        } finally {
            // 위 로직 다 돈 후에 도착 (필터 -> 서블릿 -> 컨트롤러)
            log.info("RESPONSE [{}][{}]", uuid, requestURI);
        }
    }

    @Override
    public void destroy() {
        log.info("log filter destroy");
    }
}
