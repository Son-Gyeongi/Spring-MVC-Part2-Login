package hello.login.web.session;

import hello.login.web.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.*;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    void sessionTest() {

        // 세션 생성
        /**
         * HttpServletResponse는 인터페이스이다. 다른 구현체를 들고 올 수 있기는 한데 다 애매하다.
         * HttpServletResponse 구현체는 톰캣이 별도로 제공
         * 스프링에서 MockHttpServletResponse()가 제공된다.
         */
        MockHttpServletResponse response = new MockHttpServletResponse();
        // value값으로 Member가 들어간다.
        Member member = new Member();
        sessionManager.createSession(member, response); // 세션 생성 후 웹 브라우저로 응답이 나갔다는 가정

        // 요청에 응답 쿠키 저장되었는지 확인하기
        MockHttpServletRequest request = new MockHttpServletRequest();
        // 웹 브라우저인 클라이언트에서 서버로 전송
        request.setCookies(response.getCookies());

        // 클라이언트에서 서버로 요청이 왔으니깐 서버에서 확인해보자.
        // 세션 조회
        Object result = sessionManager.getSession(request);
        assertThat(result).isEqualTo(member);

        // 세션 만료
        sessionManager.expire(request);
        Object expired = sessionManager.getSession(request);
        assertThat(expired).isNull(); // expired가 null이어야 정상
    }
}