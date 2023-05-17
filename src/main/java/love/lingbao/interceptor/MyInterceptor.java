package love.lingbao.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class MyInterceptor implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }
        /*String token = request.getHeader("token");
        if (StringUtils.isEmpty(token)){
            throw new IOException("token为空");
        }*/
        Object user = request.getSession().getAttribute("user");
        //log.info("拦截器token={}，user={}",token,user);
        log.info("拦截器user={}",user);
        if (user == null) {
            throw new IOException("用户未登录");
        }else {
            return true;
        }
    }

}

