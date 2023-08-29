package hello.login.login;

import hello.login.web.member.Member;
import hello.login.web.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 로그인 핵심 비즈니스 로직
 */
@Service // 스프링 빈으로 등록
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    /**
     * @return - null이면 로그인 실패
     * 회원을 조회한 다음에 파라미터로 넘어온 password와 비교해서 같으면 회원을 반환하고,
     * 만약 password가 다르면 null 을 반환
     */
    public Member login(String loginId, String password) {
        /*
        Optional<Member> findMemberOptional = memberRepository.findByLoginId(loginId);
        Member member = findMemberOptional.get(); // Optional에서 get()을 하면 Member가 꺼내져 나온다. 없으면 예외가 터진다.
        if (member.getPassword().equals(password)) {
            return member;
        } else {
            return null;
        }
         */

        // 위 코드를 더 간단하게
        /*
        Optional<Member> byLoginId = memberRepository.findByLoginId(loginId);
        // Optional이라서 바로 filter 기능 쓸 수 있다.
        // password가 같으면 Member 반환하고 같지 않으면 null을 반환한다.
        byLoginId.filter(m -> m.getPassword().equals(password))
                .orElse(null);
         */

        // 위 코드를 더 간단하게 한줄로, 자바8 Optional, Stream 공부하자
        return memberRepository.findByLoginId(loginId)
                .filter(m -> m.getPassword().equals(password))
                .orElse(null);
    }
}
