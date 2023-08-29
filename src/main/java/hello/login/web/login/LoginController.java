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
    public String login(@Valid @ModelAttribute LoginForm form, BindingResult bindingResult) {
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

        // 로그인 성공 처리 TODO

        return "redirect:/"; // 로그인이 되면 홈으로 보내기
    }
}
