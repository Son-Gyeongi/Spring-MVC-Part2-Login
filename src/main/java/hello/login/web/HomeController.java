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
    @GetMapping("/")
    public String homeLoginV2(HttpServletRequest request, Model model) {

        // 로그인 판단여부
        // 세션 관리자에 저장된 회원 정보 조회
        Member member = (Member) sessionManager.getSession(request); // Object -> Member 캐스팅(형변환)

        // 로그인
        if (member == null) { // DB에 없을 경우
            return "home";
        }
        // 로그인 성공한 사용자, 쿠키가 있는 사용자
        model.addAttribute("member", member);
        return "loginHome";
    }
}