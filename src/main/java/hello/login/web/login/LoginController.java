package hello.login.web.login;

import hello.login.login.LoginService;
import hello.login.web.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // form 보여주기
    @GetMapping("/login")
    public String loginForm(@ModelAttribute("loginForm") LoginForm form) {
        return "login/loginForm";
    }

    // 실제 로그인 처리가 되는 로직
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult,
                        HttpServletResponse response) {
        if (bindingResult.hasErrors()) { // 에러가 있다면
            return "login/loginForm";
        }

        // 성공 로직
        Member loginMember = loginService.login(form.getLoginId(), form.getPassword());

        // 회원을 못 찾거나 id, password가 맞지 않을 때
        if (loginMember == null) {
            // reject()는 글로벌 오류이다. 필드 오류 아님
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 로그인 성공 처리
        // 쿠키 만들기 - 쿠키에 시간 정보를 주지 않으면 세션 쿠키(브라우저 종료시 모두 종료)
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        // 쿠키 생성한 것을 서버에서 HTTP응답을 보낼때 response에 넣어서 보내야 한다.
        response.addCookie(idCookie);

        return "redirect:/"; // 로그인이 되면 홈으로 보내기
    }

    // 로그아웃
    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // 쿠키 지우기
        expireCookie(response, "memberId");
        return "redirect:/"; // home으로 이동
    }

    // 쿠키 지우는 방법 - 쿠키 시간을 다 없애면 된다.
    private void expireCookie(HttpServletResponse response, String cookieName) {
        Cookie cookie = new Cookie(cookieName, null);
        cookie.setMaxAge(0); // 쿠키가 0이라서 웹 브라우저로 넘어가면 종료가 된다.
        response.addCookie(cookie);
    }
}
