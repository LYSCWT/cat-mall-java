package com.catmall.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import sun.security.util.SecurityConstants;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
//开启判断用户对某个控制层的方法是否具有访问权限的功能
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    //注入自定义的UserDetailService
//    @Autowired
//    @Lazy
//    private UserDetailsServiceImpl userDetailsServiceImpl;
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;

    //替换默认AuthenticationManager中的UserDetailService，使用数据库用户认证方式登录
    //1. 一旦通过 configure 方法自定义 AuthenticationManager实现 就回将工厂中自动配置AuthenticationManager 进行覆盖
    //2. 一旦通过 configure 方法自定义 AuthenticationManager实现 需要在实现中指定认证数据源对象 UserDetailService 实例
    //3. 一旦通过 configure 方法自定义 AuthenticationManager实现 这种方式创建AuthenticationManager对象工厂内部本地一个 AuthenticationManager 对象 不允许在其他自定义组件中进行注入
    @Override
    protected void configure(AuthenticationManagerBuilder builder) throws Exception {
//        builder.userDetailsService(userDetailsServiceImpl);
    }

    /**
     * BCryptPasswordEncoder相关知识：
     * 用户表的密码通常使用MD5等不可逆算法加密后存储，为防止彩虹表破解更会先使用一个特定的字符串（如域名）加密，然后再使用一个随机的salt（盐值）加密。
     * 特定字符串是程序代码中固定的，salt是每个密码单独随机，一般给用户表加一个字段单独存储，比较麻烦。
     * BCrypt算法将salt随机并混入最终加密后的密码，验证时也无需单独提供之前的salt，从而无需单独处理salt问题。
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    //将自定义AuthenticationManager在工厂中进行暴露,可以在任何位置注入
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    //HttpSecurity配置
    private static final String[] URL_WHITELIST = {

            "/catmall/*",
    };


    protected void configure(HttpSecurity http) throws Exception {
        http
                // 登录配置
                .formLogin()
                // 禁用session
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                // 配置拦截规则
                .and()
                .authorizeRequests()
                .antMatchers(URL_WHITELIST).permitAll()
                .anyRequest().authenticated()
                // 配置自定义的过滤器
                .and()

                .cors().and().csrf().disable()
        ;

    }

}
