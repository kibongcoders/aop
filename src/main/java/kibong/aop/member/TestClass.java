package kibong.aop.member;

import org.springframework.stereotype.Component;

@Component
public class TestClass {
    public String hello(String param) {
        return param;
    }
}
