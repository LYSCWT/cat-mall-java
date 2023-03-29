//package com.catmall.controller;
//
//
//import cn.hutool.core.util.StrUtil;
//import cn.hutool.crypto.SecureUtil;
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.catmall.common.dto.*;
//import com.catmall.common.lang.Const;
//import com.catmall.common.lang.Result;
//import com.catmall.entity.SysRole;
//import com.catmall.entity.User;
//import com.catmall.entity.SysUserRole;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.Assert;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.File;
//import java.security.Principal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@RestController
//@RequestMapping("/catmall")
//public class SysUserController extends BaseController {
//
//	@Autowired
//	BCryptPasswordEncoder passwordEncoder;
//
//	@PostMapping("/selectUserName")
//	public Result selectUserName(@Validated @RequestBody SelectUserName selectUserName, HttpServletRequest request, HttpServletResponse response) {
//
//			User user = userService.getOne(new QueryWrapper<User>().eq("username",
//					selectUserName.getUsername()));
//			if (user == null){
//				return Result.succ("true");
//
//			}else {
//				return Result.fail(403,"该用户已存在",null);
//			}
//	}
//
//	@PostMapping("/backPassword")
//	public Result backPassword(@Validated @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {
//
//		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//		updateWrapper.eq("username",loginDto.getUsername());
////		密码加密
//			User user = new User();
//			user.setPassword(SecureUtil.md5(loginDto.getPassword()));
//			userMapper.update(user,updateWrapper);
//			return Result.succ(200,"密码修改成功","密码修改成功");
//	}
//
//	@PostMapping("/updateUserInfo")
//	public Result updateUserInfo(@Validated @RequestBody UserCommon userCommon, HttpServletRequest request, HttpServletResponse response) {
//
//		User user = new User();
//		user.setId(userCommon.getId());
//		if (userCommon.getCity() != null && !userCommon.getCity().equals("")){
//			user.setCity(userCommon.getCity());
//		}
//		if (userCommon.getUsername() != null && !userCommon.getUsername().equals("")){
//			user.setUsername(userCommon.getUsername());
//		}
//		if (userCommon.getEmail() != null && !userCommon.getEmail().equals("")){
//			user.setEmail(userCommon.getEmail());
//		}
//		user.setId(userCommon.getId());
//		userMapper.updateById(user);
//
//		return Result.succ(200,"信息修改成功","信息修改成功");
//
//	}
//
//	@RequestMapping("/updateDateTime")
//	@ResponseBody
//	public Result updateDateTime(String id) {
//		System.out.println("-------"+Long.valueOf(id));
//
//		User user = new User();
//		user.setId(Long.valueOf(id));
//		user.setLast_login(LocalDateTime.now());
//		userMapper.updateById(user);
//
//		return Result.succ(200,"时间刷新成功","时间刷新成功");
//
//	}
//
//	@RequestMapping("/selectUserList")
//	@ResponseBody
//	public Result selectUserList(HttpServletResponse response) {
//		List<UserManagerData> data = new ArrayList<UserManagerData>();
//		QueryWrapper<User> wrapper = new QueryWrapper<>();
//		wrapper.select("id","username","statu","created");
//		List<User> userManagers = userMapper.selectList(wrapper);
//		for(User i:userManagers){
//			UserManagerData userManagerData = new UserManagerData();
//			userManagerData.setId(i.getId());
//			userManagerData.setUsername(i.getUsername());
//			userManagerData.setStatu(i.getStatu());
//			userManagerData.setCreated(i.getCreated());
//
//			QueryWrapper<SysUserRole> wrapperR = new QueryWrapper<>();
//			wrapperR.eq("user_id",i.getId());
//			wrapperR.select("role_id");
//			List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(wrapperR);
//
//			for(SysUserRole d:sysUserRoles){
//				SysRole sysRoles = sysRoleMapper.selectById(d.getRoleId());
//				userManagerData.setRole(sysRoles.getName());
//				data.add(userManagerData);
//			}
//		}
//
//		return Result.succ(200,"查询成功",data);
//
//	}
//
//	@RequestMapping("/resetPasswordById")
//	@ResponseBody
//	public Result resetPassword(String id, HttpServletRequest request, HttpServletResponse response) {
//
//		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
//		updateWrapper.eq("id",id);
////		密码加密
//		User user = new User();
//		user.setPassword(SecureUtil.md5("123456"));
//		userMapper.update(user,updateWrapper);
//		return Result.succ(200,"重置密码成功","重置密码成功");
//	}
//
//	@RequestMapping("/deleteUserById")
//	@ResponseBody
//	public Result deleteUserById(int id) {
//		QueryWrapper<SysUserRole> wrapper1 = new QueryWrapper<>();
//		wrapper1.eq("user_id",id);
//		SysUserRole sysRole = sysUserRoleMapper.selectOne(wrapper1);
//
//		System.out.println("--------"+id);
//		sysUserRoleMapper.deleteById(sysRole.getId());
//		int count = userMapper.deleteById(id);
//		return Result.succ(200,"删除用户成功","删除用户成功");
//	}
//
//	@RequestMapping("/selectRoleList")
//	@ResponseBody
//	public Result selectRoleList(HttpServletResponse response) {
//		List<RoleName> data = new ArrayList<RoleName>();
//		QueryWrapper<SysRole> wrapper = new QueryWrapper<>();
//		wrapper.select("name");
//		List<SysRole> sysRoles = sysRoleMapper.selectList(wrapper);
//		for(SysRole i:sysRoles){
//			RoleName roleName = new RoleName();
//			roleName.setName(i.getName());
//			data.add(roleName);
//		}
//		return Result.succ(200,"查询成功",data);
//	}
//
//	@PostMapping("/insertUser")
//	public Result insertUser(@Validated @RequestBody InsertUserInfo insertUserInfo, HttpServletRequest request, HttpServletResponse response) {
//
////		密码加密
//		User sysUser = new User();
//		sysUser.setPassword(SecureUtil.md5(insertUserInfo.getPassword()));
//		sysUser.setUsername(insertUserInfo.getUsername());
//		sysUser.setCity(insertUserInfo.getCity());
//		sysUser.setCreated(LocalDateTime.now());
//		sysUser.setStatu(1);
//		sysUser.setEmail(insertUserInfo.getEmail());
//		int count = userMapper.insert(sysUser);
//
//		User user = userService.getOne(new QueryWrapper<User>().eq("username",
//				insertUserInfo.getUsername()));
//		SysUserRole sysUserRole = new SysUserRole();
//		sysUserRole.setUserId(user.getId());
//		sysUserRoleMapper.insert(sysUserRole);
//
//		return Result.succ(200,"添加成功",count);
//	}
//
//	@PostMapping("/selectUserLikeName")
//	public Result selectUserLikeName(@Validated @RequestBody SelectUserName selectUserName, HttpServletRequest request, HttpServletResponse response) {
//		System.out.println(selectUserName.getUsername());
//		List<UserManagerData> data = new ArrayList<UserManagerData>();
//		QueryWrapper<User> wrapper = new QueryWrapper<>();
//		wrapper.like("username",selectUserName.getUsername());
//		wrapper.select("id","username","statu","created");
//		List<User> userManagers = userMapper.selectList(wrapper);
//		for(User i:userManagers){
//			UserManagerData userManagerData = new UserManagerData();
//			userManagerData.setId(i.getId());
//			userManagerData.setUsername(i.getUsername());
//			userManagerData.setStatu(i.getStatu());
//			userManagerData.setCreated(i.getCreated());
//
//			QueryWrapper<SysUserRole> wrapperR = new QueryWrapper<>();
//			wrapperR.eq("user_id",i.getId());
//			wrapperR.select("role_id");
//			List<SysUserRole> sysUserRoles = sysUserRoleMapper.selectList(wrapperR);
//
//			for(SysUserRole d:sysUserRoles){
//				SysRole sysRoles = sysRoleMapper.selectById(d.getRoleId());
//				userManagerData.setRole(sysRoles.getName());
//				data.add(userManagerData);
//			}
//		}
//
//		return Result.succ(200,"查询成功",data);
//	}
//
//
//	@PostMapping("/updateImage")
//	public Result updateImage(@Validated @RequestBody MultipartFile image, String fileName,HttpServletResponse response) {
//		String base64String = null;
//		try{
//			if(!image.isEmpty()){
//				image.transferTo(new File("C:\\Users\\LONGYOUSHENG\\Desktop\\毕设系统\\cat-mall\\src\\assets\\avater\\"+fileName+".jpg"));
//			}
//
////			获取本地图片转换为base64编码
////			File file = new File("C:\\Users\\LONGYOUSHENG\\Desktop\\毕设系统\\cat-mall\\src\\assets\\avater\\"+fileName+".jpg");
////			FileInputStream fis = new FileInputStream(file);
////			long size = fis.available();
////			byte[] data = new byte[(int) size];
////			fis.read(data, 0, (int) size);
////			fis.close();
////			BASE64Encoder encoder = new BASE64Encoder();
////			base64String = encoder.encode(data);
////			System.out.println(base64String);
//
//
//		}catch (Exception e){
//			System.out.println(e);
//		}
//
//
//		return Result.succ(200,"时间刷新成功","ok");
//
//	}
//
//
//
//	@RequestMapping("/updateRoleById")
//	@ResponseBody
//	public Result updateRoleByUserName(int id,String name) {
//		QueryWrapper<SysRole> wrapper1 = new QueryWrapper<>();
//		wrapper1.eq("name",name);
//		SysRole sysRole = sysRoleMapper.selectOne(wrapper1);
//
//		QueryWrapper<SysUserRole> wrapper2 = new QueryWrapper<>();
//		wrapper2.eq("user_id",id);
//		SysUserRole sysUserRole = sysUserRoleMapper.selectOne(wrapper2);
//
//		SysUserRole sysUserRole1 = new SysUserRole();
//		sysUserRole1.setId(sysUserRole.getId());
//		sysUserRole1.setRoleId(sysRole.getId());
//		sysUserRoleMapper.updateById(sysUserRole1);
//
//		return Result.succ(200,"修改成功","修改成功");
//	}
//
//
//	@RequestMapping("/updateStatuById")
//	@ResponseBody
//	public Result updateStatuById(int id,int statu) {
//		User user = new User();
//		user.setId(Long.valueOf(id));
//		user.setStatu(statu);
//		userMapper.updateById(user);
////		System.out.println(id+"------"+statu);
//
//
//		return Result.succ(200,"修改成功","修改成功");
//	}
//
//
//	@GetMapping("/info/{id}")
//	@PreAuthorize("hasAuthority('sys:user:list')")
//	public Result info(@PathVariable("id") Long id) {
//
//		User user = userService.getById(id);
//		Assert.notNull(user, "找不到该管理员");
//
//		List<SysRole> roles = sysRoleService.listRolesByUserId(id);
//
//		user.setSysRoles(roles);
//		return Result.succ(user);
//	}
//
//	@GetMapping("/list")
//	@PreAuthorize("hasAuthority('sys:user:list')")
//	public Result list(String username) {
//
//		Page<User> pageData = userService.page(getPage(), new QueryWrapper<User>()
//				.like(StrUtil.isNotBlank(username), "username", username));
//
//		pageData.getRecords().forEach(u -> {
//
//			u.setSysRoles(sysRoleService.listRolesByUserId(u.getId()));
//		});
//
//		return Result.succ(pageData);
//	}
//
//	@PostMapping("/save")
//	@PreAuthorize("hasAuthority('sys:user:save')")
//	public Result save(@Validated @RequestBody User user) {
//
//		user.setCreated(LocalDateTime.now());
//		user.setStatu(Const.STATUS_ON);
//
//		// 默认密码
//		String password = passwordEncoder.encode(Const.DEFULT_PASSWORD);
//		user.setPassword(password);
//
//		// 默认头像
//		user.setAvatar(Const.DEFULT_AVATAR);
//
//		userService.save(user);
//		return Result.succ(user);
//	}
//
//	@PostMapping("/update")
//	@PreAuthorize("hasAuthority('sys:user:update')")
//	public Result update(@Validated @RequestBody User user) {
//
//		user.setUpdated(LocalDateTime.now());
//
//		userService.updateById(user);
//		return Result.succ(user);
//	}
//
//	@Transactional
//	@PostMapping("/delete")
//	@PreAuthorize("hasAuthority('sys:user:delete')")
//	public Result delete(@RequestBody Long[] ids) {
//
//		userService.removeByIds(Arrays.asList(ids));
//		sysUserRoleService.remove(new QueryWrapper<SysUserRole>().in("user_id", ids));
//
//		return Result.succ("");
//	}
//
//	@Transactional
//	@PostMapping("/role/{userId}")
//	@PreAuthorize("hasAuthority('sys:user:role')")
//	public Result rolePerm(@PathVariable("userId") Long userId, @RequestBody Long[] roleIds) {
//
//		List<SysUserRole> userRoles = new ArrayList<>();
//
//		Arrays.stream(roleIds).forEach(r -> {
//			SysUserRole sysUserRole = new SysUserRole();
//			sysUserRole.setRoleId(r);
//			sysUserRole.setUserId(userId);
//
//			userRoles.add(sysUserRole);
//		});
//
//		sysUserRoleService.remove(new QueryWrapper<SysUserRole>().eq("user_id", userId));
//		sysUserRoleService.saveBatch(userRoles);
//
//		// 删除缓存
//		User user = userService.getById(userId);
//		userService.clearUserAuthorityInfo(user.getUsername());
//
//		return Result.succ("");
//	}
//
//	@PostMapping("/repass")
//	@PreAuthorize("hasAuthority('sys:user:repass')")
//	public Result repass(@RequestBody Long userId) {
//
//		User user = userService.getById(userId);
//
//		user.setPassword(passwordEncoder.encode(Const.DEFULT_PASSWORD));
//		user.setUpdated(LocalDateTime.now());
//
//		userService.updateById(user);
//		return Result.succ("");
//	}
//
//	@PostMapping("/updatePass")
//	public Result updatePass(@Validated @RequestBody PassDto passDto, Principal principal) {
//
//		User user = userService.getByUsername(principal.getName());
//
//		boolean matches = passwordEncoder.matches(passDto.getCurrentPass(), user.getPassword());
//		if (!matches) {
//			return Result.fail("旧密码不正确");
//		}
//
//		user.setPassword(passwordEncoder.encode(passDto.getPassword()));
//		user.setUpdated(LocalDateTime.now());
//
//		userService.updateById(user);
//		return Result.succ("");
//	}
//}
