package com.catmall.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.catmall.common.dto.LoginDto;
import com.catmall.common.dto.UserCommon;
import com.catmall.common.lang.Result;
import com.catmall.entity.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/catmall")
public class LoginController extends BaseController{
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

            User user = userService.getOne(new QueryWrapper<User>().eq("name",
                    loginDto.getName()));
            if (user == null || user.getDel() == 0){
                return Result.fail(401,"用户名或密码错误",null);
			}
//            System.out.println(user.getPassword()+"####################"+SecureUtil.md5(loginDto.getPassword()));
//            System.out.println(user.getPassword().equals(SecureUtil.md5(loginDto.getPassword())));
            if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))){
                return Result.fail(401,"用户名或密码错误",null);
            }else {
                String jwt = jwtUtils.generateToken(loginDto.getName());
                //jwt一般放Header里面不放在数据里面，看项目
                response.setHeader("Authorization",jwt);
                response.setHeader("Access-control-Expose-Headers","Authorization");

                //查询用户，基本信息返回
                QueryWrapper<User> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("name",loginDto.getName());
                User user1 = userMapper.selectOne(queryWrapper);

                UserCommon userCommon = new UserCommon();
                userCommon.setId(user1.getId());
                userCommon.setAvatar(user1.getAvatar());
                userCommon.setName(user1.getName());
                userCommon.setEmail(user1.getEmail());
                if(user1.getSex() ==0 ){
                    userCommon.setSex("男");
                }else if(user1.getSex() ==1){
                    userCommon.setSex("女");
                }else {
                    userCommon.setSex("保密");
                }
                userCommon.setBirthday(user1.getBirthday());
                userCommon.setPhone(user1.getPhone());


                QueryWrapper<UserRole> wrapper1 = new QueryWrapper<>();
                wrapper1.eq("user_id", user1.getId().intValue());
                UserRole userRole = userRoleMapper.selectOne(wrapper1);

                Role role = roleMapper.selectById(userRole.getRoleId());

                userCommon.setRoleName(role.getName());

//			获取本地图片转换为base64编码
//			File file = new File("C:\\Users\\LONGYOUSHENG\\Desktop\\毕设系统\\cat-mall\\src\\assets\\avater\\"+fileName+".jpg");
//			FileInputStream fis = new FileInputStream(file);
//			long size = fis.available();
//			byte[] data = new byte[(int) size];
//			fis.read(data, 0, (int) size);
//			fis.close();
//			BASE64Encoder encoder = new BASE64Encoder();
//			base64String = encoder.encode(data);
//			System.out.println(base64String);

                return Result.succ(200,"登录成功",userCommon);
            }

    }
}
