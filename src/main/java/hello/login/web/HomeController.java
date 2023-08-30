package hello.login.web;

import hello.login.web.member.Member;
import hello.login.web.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    //    @GetMapping("/")
    public String home() {
        return "home";
    }

    // 새로운 홈 화면 - 로그인 처리까지 되는 화면
    @GetMapping("/")
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
}