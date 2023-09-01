package hello.login.web;

import hello.login.web.member.Member;
import hello.login.web.member.MemberRepository;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 새로운 홈 화면 - 로그인 처리까지 되는 화면
//    @GetMapping("/")
    public String homeLogin(@CookieValue(name = "memberId", required = false) Long memberId,
                            Model model) {
        /*
            먼저 쿠키를 받는다.
            스프링에서 제공하는 @CookieValue 애노테이션
            required = false - 로그인 안 한 사용자도 들어올 수 있다.
         */

        if (memberId == null) {
            return "home";
        }

        // 로그인
        Member loginMember = memberRepository.findById(memberId);
        if (loginMember == null) { // DB에 없을 경우
            return "home";
        }
        // 로그인 성공한 사용자, 쿠키가 있는 사용자
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
    // 직접 만든 세션 적용
//    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 로그인 판단여부
        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request); // Object -> Member 캐스팅(형변환)

        // 로그인
        if (member == null) { // DB에 없을 경우
            return "home";
        }

        model.addAttribute("member", member);
        return "loginHome";
    }
    // 서블릿 HTTP 세션1
    @GetMapping("/")
    public String homeLoginV3(HttpServletRequest request, Model model) {

        // 세션 찾기
        // 세션은 메모리를 쓰는 거여서 꼭 필요할 때만 생성하자.
        HttpSession session = request.getSession(false); // 로그인 안된 사용자 세션 만들어 줄 필요없다.
        if (session == null) {
            return "home";
        }

        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER); // Object -> Member 캐스팅(형변환)

        // 세션에 회원 데이터가 없으면 home으로 이동
        if (loginMember == null) { // DB에 없을 경우
            return "home";
        }

        // 세션이 유지되면(세션이 유지되고 loginMember데이터가 있는 걸 확인이 되면) 로그인으로 이동
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}