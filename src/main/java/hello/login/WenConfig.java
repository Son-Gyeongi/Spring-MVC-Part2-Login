package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import hello.login.web.interceptor.LogInterceptor;
import hello.login.web.interceptor.LoginCheckInterceptor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.Filter;

// logFilter() 호출 -> loginCheckFilter() 호출 -> addInterceptors() 호출된다.
@Configuration
public class WenConfig implements WebMvcConfigurer {

    // 스프링부트 사용할 때 필터 등록 아래와 같이 하면 된다.
    // 왜냐하면 스프링부트가 WAS를 들고 띄우기 때문이다. WAS를 띄울 때 필터를 같이 넣어준다.
    // 실행시 LogFilter가 나올 수 있도록 FilterRegistrationBean를 등록
//    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // filter가 chain으로 여러개 들어갈 경우 순서를 정해준다.
        filterRegistrationBean.addUrlPatterns("/*"); // 중요 - 어떤 URL패턴을 할건가, /* : 모든 url에 다 적용

        return filterRegistrationBean;
    }

    // 서블릿 필터 - 인증 체크
//    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); // filter가 chain으로 여러개 들어갈 경우 순서를 정해준다.
        filterRegistrationBean.addUrlPatterns("/*"); // 중요 - 어떤 URL패턴을 할건가, /* : 모든 url에 다 적용

        return filterRegistrationBean;
    }

    // 스프링 인터셉터 - 요청 로그
    // 스프링 인터셉터 등록, implements WebMvcConfigurer
    // addInterceptors 오버라이드
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor())
                .order(1)
                .addPathPatterns("/**") // 하위 전체 경로 가능
                .excludePathPatterns("/css/**", "/*.ico", "/error"); // 해당 경로 인터셉터 하지마
        // "/**" 모든 걸 허용하지만 "/css/**", "/*.ico", "/error" 들은 뺼거다. LogInterceptor호출이 안된다.
        // 필터와 비교해보면 인터셉터는 addPathPatterns , excludePathPatterns 로 매우 정밀하게 URL 패턴을 지정할 수 있다.

        // 스프링 인터셉터 - 인증 체크
        // LoginCheckInterceptor 등록
        registry.addInterceptor(new LoginCheckInterceptor())
                .order(2)
                .addPathPatterns("/**") // 모든 경로에 대해서 다 로그체크
                .excludePathPatterns("/", "/members/add", "/login", "/logout",
                        "/css/*", "/*.ico", "/error");
    }
}
