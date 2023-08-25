package kibong.aop.pointcut;

import kibong.aop.member.MemberServiceImpl;
import kibong.aop.member.TestClass;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import java.lang.reflect.Method;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class ExecutionTest {

    AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
    Method kibongMethod;
    Method testMethod;

    @BeforeEach
    public void init() throws NoSuchMethodException{
        kibongMethod = MemberServiceImpl.class.getMethod("hello", String.class);
        testMethod = TestClass.class.getMethod("hello", String.class);
    }

    @Test
    void printMethod(){
        log.info("kibongMethod={}", kibongMethod);
    }

    @Test
    void exactMatch(){
        pointcut.setExpression("execution(public String kibong.aop.member.MemberServiceImpl.hello(String))");
        log.info("pointcut={}", pointcut.matches(kibongMethod, TestClass.class));
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
    }

    @Test
    void allMatch(){
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void nameMatch(){
        pointcut.setExpression("execution(* hello(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void nameMatchStar1(){
        pointcut.setExpression("execution(* hel*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void nameMatchStar2(){
        pointcut.setExpression("execution(* *el*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void nameMatchFalse(){
        pointcut.setExpression("execution(* nono(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void packageExactMatch1(){
        pointcut.setExpression("execution(* kibong.aop.member.MemberServiceImpl.hello(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void packageExactMatch2(){
        pointcut.setExpression("execution(* kibong.aop.member.*.*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void packageExactMatchFalse(){
        pointcut.setExpression("execution(* kibong.aop.*.*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void packageMatchSubPackage1(){
        pointcut.setExpression("execution(* kibong.aop.member..*.*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void packageMatchSubPackage2(){
        pointcut.setExpression("execution(* kibong.aop..*.*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void typeExactMatch(){
        pointcut.setExpression("execution(* kibong.aop.member.MemberServiceImpl.*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void typeMatchSuperType(){
        pointcut.setExpression("execution(* kibong.aop.member.MemberService.*(..))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void typeMatchInternal() throws NoSuchMethodException {
        pointcut.setExpression("execution(* kibong.aop.member.MemberServiceImpl.*(..))");
        Method internal = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internal, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void typeMatchNoSuperTypeMethodFalse() throws NoSuchMethodException {
        pointcut.setExpression("execution(* kibong.aop.member.MemberService.*(..))");
        Method internal = MemberServiceImpl.class.getMethod("internal", String.class);
        assertThat(pointcut.matches(internal, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void argsMatch() {
        pointcut.setExpression("execution(* *(String))");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isTrue();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isTrue();
    }

    @Test
    void argsMatchNoArgs() {
        pointcut.setExpression("execution(* *())");
        assertThat(pointcut.matches(kibongMethod, MemberServiceImpl.class)).isFalse();
        assertThat(pointcut.matches(testMethod, TestClass.class)).isFalse();
    }

    @Test
    void argsMatchStar() {
        pointcut.setExpression("execution(* *(*))");
        assertThat(pointcut.matches(kibongMethod,
                MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchAll() {
        pointcut.setExpression("execution(* *(..))");
        assertThat(pointcut.matches(kibongMethod,
                MemberServiceImpl.class)).isTrue();
    }

    @Test
    void argsMatchComplex() {
        pointcut.setExpression("execution(* *(String, ..))");
        assertThat(pointcut.matches(kibongMethod,
                MemberServiceImpl.class)).isTrue();
    }


}
