package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

// 세션 정보와 타임아웃 설정
@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(HttpServletRequest request) {
        // 세션을 받아보자.
        HttpSession session = request.getSession(false);
        if (session == null) {
            return "세션이 없습니다.";
        }

        // 세션이 있으면 세션 데이터를 반복문으로 쭉 돌려보자.
        // 세션 데이터 출력
        session.getAttributeNames().asIterator()
                .forEachRemaining(name ->
                        log.info("session name = {}, value = {}", name, session.getAttribute(name)));

        // 세션이 기본으로 제공하는 데이터들 확인
        log.info("sessionId = {}", session.getId());
        log.info("maxInactiveInterval = {}", session.getMaxInactiveInterval()); // 세션의 유효 시간(초)
        log.info("creationTime = {}", new Date(session.getCreationTime())); // 세션 생성 시간
        log.info("lastAccessedTime = {}", new Date(session.getLastAccessedTime())); // 세션에 마지막으로 접근한 시간
        log.info("isNew = {}", session.isNew()); // 새로운 세션인가 클라이언트가 서버에 요청해서 있는 세션이 나온건가
        // 이미 생성된 세션을 쓰면 false

        return "세션 출력";
    }
}
