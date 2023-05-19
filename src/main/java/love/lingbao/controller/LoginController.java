package love.lingbao.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.qcloudsms.SmsSingleSenderResult;
import lombok.extern.slf4j.Slf4j;
import love.lingbao.common.R;
import love.lingbao.common.RandomCodeUtils;
import love.lingbao.common.SendMsg;
import love.lingbao.entity.User;
import love.lingbao.dto.UserDto;
import love.lingbao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/login")
@CrossOrigin
@ResponseBody
public class LoginController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private UserService userService;

    /**
     * 用户登录
     * @param user 登录用户信息体
     * @return
     */
    @PostMapping
    @RequestMapping
    public R<User> login(@RequestBody User user){//http对象用来将登录成功的用户存入session里
        //response.setHeader("Access-Control-Allow-Origin", address);
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
        httpSession.setAttribute("user",u.getId());
        httpSession.setMaxInactiveInterval(24 * 60 * 60);
        log.info(httpSession.getId());
        return R.success(u, "登录成功！");
    }

    /**
     * 发送短信验证码
     * @param phone 手机号码
     * @return
     */
    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody String phone) throws Exception {
        //response.setHeader("Access-Control-Allow-Origin", address);
        if(phone.length() > 0 && phone.charAt(phone.length() - 1) == '='){
            phone = phone.substring(0, phone.length() - 1);
        }
        log.info("/login/sendMsg post -> sendMsg: phone = {}; 发送短信验证码", phone);
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
            //存储该手机号的session,键为手机号，值为验证码，时长为1分钟
            httpSession.setAttribute(phone, code);
            httpSession.setMaxInactiveInterval(60);
            System.out.println(httpSession.getAttribute(phone));
        }
        return R.success("验证码已发送");
    }

    /**
     * 验证用户名是否存在
     * @param username 用户名
     * @return
     */
    @PostMapping("/username")
    public R<String> username(@RequestBody String username){
        //response.setHeader("Access-Control-Allow-Origin", address);
        if(username.length() > 0 && username.charAt(username.length() - 1) == '='){
            username = username.substring(0, username.length() - 1);
        }
        log.info("/login/username post -> username: username = {}; 查询用户名是否存在", username);
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
     * 生成登录验证码
     * @return
     */
    @GetMapping("/code")
    public R<String> code(){
        //response.setHeader("Access-Control-Allow-Origin", address);
        log.info("/login/code get -> ; 生成登录验证码");
        //生成4位数验证码
        String code = RandomCodeUtils.getFourValidationCode();
        return R.success(code, "用户名未注册，可以使用");
    }

    /**
     * 用户注册
     * @param userDto 用户信息体（带验证码）
     * @return
     */
    @PostMapping("/register")
    public R<String> register(@RequestBody UserDto userDto){
        //response.setHeader("Access-Control-Allow-Origin", address);
        log.info("/login/register post -> register: userDto = {}; 用户注册", userDto.toString());
        //先对验证码比对
        //如果不相等
        log.info((String) httpSession.getAttribute(userDto.getPhone()));
        if(httpSession.getAttribute(userDto.getPhone()) == null){
            return R.error("验证码未发送或验证码已过期！请重试");
        }
        if(!httpSession.getAttribute(userDto.getPhone()).equals(userDto.getCode())){
            return R.error("验证码输入错误！请重试");
        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername, userDto.getUsername());
        User user1 = userService.getOne(queryWrapper);
        if(user1 != null){
            return R.error("用户名已存在！");
        }
        if(userDto.getUsername().length() < 6){
            return R.error("用户名长度 < 6;\n用户名应为6~16位的由大小写字母、数字、下划线构成！请重试");
        }
        if(userDto.getUsername().length() > 16){
            return R.error("用户名长度 > 16;\n用户名应为6~16位的由大小写字母、数字、下划线构成！请重试");
        }
        if(userDto.getPassword().length() < 6){
            return R.error("密码长度 < 6;\n密码应为6~18位的由大小写字母、数字、下划线构成！请重试");
        }
        if(userDto.getPassword().length() > 18){
            return R.error("密码长度 > 18;\n密码应为6~18位的由大小写字母、数字、下划线构成！请重试");
        }
        if(httpSession.getAttribute(userDto.getPhone()) == null){
            return R.error("输入的手机号与验证的手机号不一致，请修改");
        }
        if(!userDto.getCode().equals(httpSession.getAttribute(userDto.getPhone()))){
            return R.error("验证码输入有误，请修改");
        }
        //之后，对用户的密码进行md5加密
        String password = userDto.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8));
        userDto.setCreateTime(LocalDateTime.now());
        userDto.setUpdateTime(LocalDateTime.now());
        userDto.setPassword(password);
        //存储新用户
        userService.saveUserDto(userDto);

        return R.success("用户" + userDto.getUsername() + "注册成功！");
    }

    /**
     * 发送短信验证码
     * @param phone 手机号码
     * @return
     */
    @PostMapping("/sendMsg1")
    public R<String> sendMsg1(@RequestBody String phone) throws Exception {
        //response.setHeader("Access-Control-Allow-Origin", address);
        if(phone.length() > 0 && phone.charAt(phone.length() - 1) == '='){
            phone = phone.substring(0, phone.length() - 1);
        }
        log.info("/login/sendMsg1 post -> sendMsg: phone = {}; 测试发送短信验证码", phone);
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
        log.info((String) httpSession.getAttribute(phone));
        //存储该手机号的session,键为手机号，值为验证码，时长为1分钟
        httpSession.setAttribute(phone, code);
        httpSession.setMaxInactiveInterval(60);
        log.info((String) httpSession.getAttribute(phone));

        return R.success("验证码已发送");
    }

    /**
     * 查询用户是否登录
     * @return
     */
    @GetMapping("/isUserLogin")
    public R<User> isUserLogin(){
        //response.setHeader("Access-Control-Allow-Origin", address);
        log.info("/login/isUserLogin get -> ; 查询用户是否登录");
        User user = userService.getById((Integer) httpSession.getAttribute("user"));
        //如果没有查询到则返回登录失败结果
        if(user == null){
            return R.error("用户未登录！");
        }
        return R.success(user);
    }

    @GetMapping("/test01")
    public void test01(){
        String phone = "15310467031";
        String code = RandomCodeUtils.getSixValidationCode();
        httpSession.setAttribute(phone, code);
        //30s
        httpSession.setMaxInactiveInterval(30);
        System.out.println(httpSession.getAttribute(phone));
    }

    @GetMapping("/test02")
    public String test02(){
        return (String) httpSession.getAttribute("15310467031");
    }

    @GetMapping("/test03")
    public String test03(){
        System.out.println("e10adc3949ba59abbe56e057f20f883e".length());
        return DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8));
    }
}
