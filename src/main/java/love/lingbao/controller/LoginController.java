package love.lingbao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.qcloudsms.SmsSingleSenderResult;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.R;
import love.lingbao.common.RandomCodeUtils;
import love.lingbao.common.SendMsg;
import love.lingbao.entity.User;
import love.lingbao.entity.UserInCode;
import love.lingbao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import org.testng.annotations.Test;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user 登录用户信息体
     * @return
     */
    @PostMapping
    public R<User> login(HttpServletRequest request, @RequestBody User user){//http对象用来将登录成功的用户存入session里
        log.info("/login post -> login: user = {}; 用户登录", user.toString());

        //1、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,user.getUsername());
        User u = userService.getOne(queryWrapper);

        //2、如果没有查询到则返回登录失败结果
        if(u == null){
            return R.error("用户名不存在");
        }

        //3、将页面提交的密码password进行md5加密处理
        String password = user.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //4、密码比对，如果不一致则返回登录失败结果
        if(!u.getPassword().equals(password)){
            return R.error("用户名或密码错误，请重试");
        }

        //5、登录成功，将用户id存入Session并返回登录成功结果
        request.getSession().setAttribute("user",u.getId());
        request.getSession().setMaxInactiveInterval(24 * 60 * 60);
        return R.success(u, "登录成功！");
    }

    /**
     * 发送短信验证码
     * @param phone 手机号码
     * @return
     */
    @GetMapping("/sendMsg")
    public R<String> sendMsg(HttpServletRequest request, String phone) throws Exception {
        log.info("/login/sendMsg get -> sendMsg: phone = {}; 发送短信验证码", phone);
        //判断该注册用户的手机号是否存在
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, phone);
        User user = userService.getOne(queryWrapper);
        //手机号存在，返回
        if(user != null){
            return R.error("该手机号已有绑定账号，请更换手机号后再试");
        }
        //生成6位数验证码
        String code = RandomCodeUtils.getSixValidationCode();
        //短信格式插入字符串数组，第一个是验证码，第二个是时长（分钟）
        String[] params = {code, "3"};
        //发送短信，获取发送的结果
        SmsSingleSenderResult result = SendMsg.sendMsgByTxPlatform(phone, params);
        //结果的结果码为1016，表示手机号的格式输入有误，返回
        if(result.result == 1016) return R.error("手机号格式输入有误！请重新输入");
        //其他错误，抛异常
        else if(result.result != 0){
            throw new Exception("send phone validateCode is error" + result.errMsg);
        }
        //正确情况
        else{
            //存储该手机号的session,键为手机号，值为验证码，时长为3分钟
            request.getSession().setAttribute(phone, code);
            request.getSession().setMaxInactiveInterval(3 * 60);
            System.out.println(request.getSession().getAttribute(phone));
        }
        return R.success("验证码已发送");
    }

    /**
     * 验证用户名是否存在
     * @param username 用户名
     * @return
     */
    @GetMapping("/username")
    public R<String> username(String username){
        log.info("/login/username get -> username: username = {}; 查询用户名是否存在", username);
        //1、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, username);
        User user = userService.getOne(queryWrapper);

        //2、如果没有查询到则返回登录失败结果
        if(user != null){
            return R.error("用户名已存在！");
        }
        return R.success("用户名未注册，可以使用");
    }

    /**
     * 用户注册
     * @param userInCode 用户信息体（带验证码）
     * @return
     */
    @PostMapping("/register")
    public R<String> register(HttpServletRequest request, @RequestBody UserInCode userInCode){
        log.info("/login/register post -> register: userInCode = {}; 用户注册", userInCode.toString());
        //先对验证码比对
        //如果不相等
        if(!request.getSession().getAttribute(userInCode.getUsername()).equals(userInCode.getCode())){
            return R.error("验证码输入错误！请重试");
        }
        //之后，对用户的密码进行md5加密
        String password = userInCode.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        userInCode.setCreateTime(LocalDateTime.now());
        userInCode.setUpdateTime(LocalDateTime.now());
        userInCode.setPassword(password);

        //存储新用户
        userService.save(userInCode);

        return R.success("用户" + userInCode.getUsername() + "注册成功！");
    }

    @GetMapping("/test01")
    public void test01(HttpServletRequest request){
        String phone = "15310467031";
        String code = RandomCodeUtils.getSixValidationCode();
        request.getSession().setAttribute(phone, code);
        //30s
        request.getSession().setMaxInactiveInterval(30);
        System.out.println(request.getSession().getAttribute(phone));
    }

    @GetMapping("/test02")
    public String test02(HttpServletRequest request){

        return (String) request.getSession().getAttribute("15310467031");
    }

    @GetMapping("/test03")
    public String test03(){
        System.out.println("e10adc3949ba59abbe56e057f20f883e".length());
        return DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8));
    }
}
