package hello.login.web.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    // 의존관계 주입
    private final MemberRepository memberRepository;

    // 회원 등록 폼
    @GetMapping("/add")
    public String addForm(@ModelAttribute("member") Member member) {
        // @ModelAttribute를 사용할 때 모델에 담길 이름이 Member에서 맨 앞글자를 소문자로 변경하면 member로 된다.
        // 그래서 ("member")를 안 적어주어도 되지만 타임리프로 갔을 때 IDE에서 인식을 못해서 적어주었다.

        return "members/addMemberForm"; // addMemberForm 회원가입폼으로 보낸다.
    }

    @PostMapping("/add")
    public String save(@Valid @ModelAttribute Member member, BindingResult bindingResult) {
        // java에서 제공하는 검증 @Valid

        if (bindingResult.hasErrors()) { // 에러가 있다면
            return "members/addMemberForm"; // addMemberForm 회원가입폼으로 보낸다.
        }

        memberRepository.save(member); // member 저장
        return "redirect:/"; // 홈화면으로 리다이렉트
    }
}
