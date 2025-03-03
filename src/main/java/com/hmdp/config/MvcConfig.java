package com.hmdp.config;

import com.hmdp.utils.LoginInterceptor;
import com.hmdp.utils.RefreshTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 登录拦截器
        registry.addInterceptor(new LoginInterceptor())
                .excludePathPatterns(   //排除不需要拦截的路径（放行），和已登录的用户无关（有些功能要登录之后才能使用，如：登出、我的等）
                        "/shop/**",     //店铺
                        "/voucher/**",  //优惠券
                        "/shop-type/**",    //店铺类型
                        "/upload/**",   //更新
                        "/blog/hot",    //日志
                        "/user/code",   //发送验证码
                        "/user/login"   //登录
                ).order(1);
        // token刷新的拦截器  拦截所有    设置先执行，控制拦截器顺序
        registry.addInterceptor(new RefreshTokenInterceptor(stringRedisTemplate)).addPathPatterns("/**").order(0);
    }
}