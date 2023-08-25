package kibong.aop.member;

import kibong.aop.member.MemberService;
import kibong.aop.member.annotation.ClassAop;
import kibong.aop.member.annotation.MethodAop;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@ClassAop
@Component
public class MemberServiceImpl implements MemberService {
    @Override
    @MethodAop
    public String hello(String param) {
        return "ok";
    }

    public String internal(String param){
        return "ok";
    }
}
