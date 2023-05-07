package love.lingbao.lostandfound;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.qcloudsms.SmsSingleSenderResult;
import love.lingbao.common.R;
import love.lingbao.common.SendMsg;
import love.lingbao.entity.User;
import love.lingbao.mapper.UserMapper;
import love.lingbao.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;


@SpringBootTest
class LostAndFoundApplicationTests {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired(required = false)
    private UserService userService;

    @Test
    void contextLoads() {
        List<User> users = userMapper.findAll();
        System.out.println(users.toString());
    }

    @Test
    public void test01() throws Exception {
        sendMsg("15310467031");
    }

    private void sendMsg(String phone) throws Exception {
        SmsSingleSenderResult result = SendMsg.sendMsgByTxPlatform(phone, new String[]{"123456", "3"});
        System.out.println(result.result);
    }
}


