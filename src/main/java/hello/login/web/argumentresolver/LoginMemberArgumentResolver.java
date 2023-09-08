package hello.login.web.argumentresolver;

import hello.login.web.SessionConst;
import hello.login.web.member.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

// ArgumentResolver 활용
@Slf4j
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    // MethodParameter 지원하는지 확인
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        log.info("supportsParameter 실행");

        // parameter 정보가 넘어온다.
        // hasParameterAnnotation - 파라미터에 어노테이션이 있는지 물어본다.
        // HomeController에 파라미터 @Login Member loginMember 확인
        boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class); // @Login 어노테이션 붙어있는가
        boolean hasMemberType = Member.class.isAssignableFrom(parameter.getParameterType());// Member 클래스인가

        return hasLoginAnnotation && hasMemberType; // 2개를 다 만족하면(true) resolveArgument()가 실행된다.
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        log.info("resolveArgument 실행");

        // HttpServletRequest가 필요하다.
        HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        HttpSession session = request.getSession(false); // true를 하면 의미없는 세션이 만들어진다.
        if (session == null) {
            return null;
        }

        // session이 있으면 member가 반환이 된다.
        return session.getAttribute(SessionConst.LOGIN_MEMBER);
    }
}
