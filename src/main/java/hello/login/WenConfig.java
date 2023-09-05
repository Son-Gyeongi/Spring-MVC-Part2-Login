package hello.login;

import hello.login.web.filter.LogFilter;
import hello.login.web.filter.LoginCheckFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;

@Configuration
public class WenConfig {

    // 스프링부트 사용할 때 필터 등록 아래와 같이 하면 된다.
    // 왜냐하면 스프링부트가 WAS를 들고 띄우기 때문이다. WAS를 띄울 때 필터를 같이 넣어준다.
    // 실행시 LogFilter가 나올 수 있도록 FilterRegistrationBean를 등록
    @Bean
    public FilterRegistrationBean logFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LogFilter());
        filterRegistrationBean.setOrder(1); // filter가 chain으로 여러개 들어갈 경우 순서를 정해준다.
        filterRegistrationBean.addUrlPatterns("/*"); // 중요 - 어떤 URL패턴을 할건가, /* : 모든 url에 다 적용

        return filterRegistrationBean;
    }

    // 서블릿 필터 - 인증 체크
    @Bean
    public FilterRegistrationBean loginCheckFilter() {
        FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<>();
        filterRegistrationBean.setFilter(new LoginCheckFilter());
        filterRegistrationBean.setOrder(2); // filter가 chain으로 여러개 들어갈 경우 순서를 정해준다.
        filterRegistrationBean.addUrlPatterns("/*"); // 중요 - 어떤 URL패턴을 할건가, /* : 모든 url에 다 적용

        return filterRegistrationBean;
    }
}
