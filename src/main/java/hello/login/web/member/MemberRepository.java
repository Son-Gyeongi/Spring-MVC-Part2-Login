package hello.login.web.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {
    // 간단하게 메모리로 만들어보자.(다른 방법 DB에 만드는 거)
    private static Map<Long, Member> store = new HashMap<>(); // static 사용
    private static long sequence = 0L; // static 사용

    // 저장
    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save : member = {}", member);
        store.put(member.getId(), member); // 저장소에 저장, member.getId()는 키 member는 값
        return member;
    }

    // 회원 찾기 By id
    public Member findById(Long id) {
        return store.get(id); // Map이라서 키를 넣으면 값이 나온다.
    }

    // 회원 찾기 By loginId
    public Optional<Member> findByLoginId(String loginId) {
        /*
        List<Member> all = findAll();
        for (Member m : all) {
            if (m.getLoginId().equals(loginId)) {
//                return m;
                return Optional.of(m); // Optional
            }
        }
//        return null; // 못 찾으면 null 반환
        return Optional.empty(); // Optional
         */

        // -> 자바8 람다, 스트림을 써서 코드를 줄여보자.
        return findAll().stream() // List를 stream으로 바꾼다.
                .filter(m -> m.getLoginId().equals(loginId))
                .findFirst();
    }

    // 전체 회원 찾기
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // 값인 Member가 전부 리스트로 반환된다.
    }

    // 테스트 시 초기화할 때 사용
    public void clearStore() {
        store.clear();
    }
}
