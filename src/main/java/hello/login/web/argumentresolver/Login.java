package hello.login.web.argumentresolver;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// ArgumentResolver 활용
@Target(ElementType.PARAMETER) // 파라미터에만 사용
@Retention(RetentionPolicy.RUNTIME) // 리플렉션 등을 활용할 수 있도록 런타임까지 어노테이션 정보가 남아있다.
public @interface Login {
}
