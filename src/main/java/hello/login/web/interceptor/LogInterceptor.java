package hello.login.web.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

// // 스프링 인터셉터 - 요청 로그
@Slf4j
public class LogInterceptor implements HandlerInterceptor {
    // 스프링이 제공하는 HandlerInterceptor 사용

    public static final String LOG_ID = "logId";

    /**
     * preHandle : 컨트롤러 호출 전에 호출된다. (더 정확히는 핸들러 어댑터 호출 전에 호출된다.)
     * preHandle 의 응답값이 true 이면 다음으로 진행하고, false 이면 더는 진행하지 않는다. false
     * 인 경우 나머지 인터셉터는 물론이고, 핸들러 어댑터도 호출되지 않는다. 그림에서 1번에서 끝나버린다.
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 모든 사용자의 요청 URI를 남겨보자
        String requestURI = request.getRequestURI();
        // 요청온 거 구분하기 위해서 UUID 사용
        String uuid = UUID.randomUUID().toString();

        // 로그 response할 때 uuid를 afterCompletion()에 넘겨야 한다.
        // httpRequest는 들어왔다가 나갈때 까지 하나의 사용자라는 게 보장이 된다.
        request.setAttribute(LOG_ID, uuid);

        /**
         * @RequestMapping를 사용하는 경우 - HandlerMethod 핸들러가 사용된다.
         * 정적 리소스를 사용하는 경우 - ResourceHttpRequestHandler가 사용된다.
         */
        if (handler instanceof HandlerMethod) { // HandlerMethod 타입인지 확인
            HandlerMethod hm = (HandlerMethod) handler;// 호출할 컨트롤러 메서드의 모든 정보가 포함되어 있다.
        }

        // handler : 어떤 컨트롤러 호출되는지 확인
        log.info("REQUEST [{}][{}][{}]", uuid, requestURI, handler);
        return true; // 컨트롤러, 핸들러 어댑터, 핸들러 실제 호출된다, false인 경우 아예 끝나버린다.
    }

    // postHandle : 컨트롤러 호출 후에 호출된다. (더 정확히는 핸들러 어댑터 호출 후에 호출된다.)
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        log.info("postHandle [{}]", modelAndView);
    }

    // afterCompletion : 뷰가 렌더링 된 이후에 호출된다, 완전히 끝날 때 호출된다.
    // preHandle과 postHandle 예외 상황에서 안 찍힌다. 그래서 끝날 때 afterCompletion에서 찍어야한다.
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        String requestURI = request.getRequestURI();
        // uuid 받아서 쓸 수 있다.
        String logId = (String) request.getAttribute(LOG_ID);

        log.info("RESPONSE [{}][{}][{}]", logId, requestURI, handler);
        if (ex != null) { // 예외가 null이 아니면
            log.error("afterCompletion error!!", ex);
        }
    }
}
