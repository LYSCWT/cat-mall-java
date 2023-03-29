package com.catmall.controller;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.catmall.common.dto.*;
import com.catmall.common.lang.Const;
import com.catmall.common.lang.Result;
import com.catmall.entity.*;
import com.catmall.mapper.UserAddressMapper;
import com.catmall.mapper.UserRoleMapper;
import com.catmall.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/catmall")
public class UserController extends BaseController {
    @Autowired
	BCryptPasswordEncoder passwordEncoder;

	@PostMapping("/selectUserName")
	public Result selectUserName(@Validated @RequestBody SelectUserName selectUserName, HttpServletRequest request, HttpServletResponse response) {

			User user = userService.getOne(new QueryWrapper<User>().eq("name",
					selectUserName.getName()));
			if (user == null || user.getDel() == 0){
				return Result.succ("true");
			}else {
				return Result.fail(402,"该用户已存在",null);
			}
	}

	@PostMapping("/selectPhoneByUserName")
	public Result selectPhoneByUserName(@Validated @RequestBody SelectUserName selectUserName, HttpServletRequest request, HttpServletResponse response) {

		User user = userService.getOne(new QueryWrapper<User>().eq("name",
				selectUserName.getName()));
		if (user == null){
			return Result.fail(402,"该用户不存在",null);
		}else {
			return Result.succ(user.getPhone());
		}
	}

	@PostMapping("/insertUser")
	public Result insertUser(@Validated @RequestBody InsertUserInfo insertUserInfo, HttpServletRequest request, HttpServletResponse response) {

//		密码加密
		User user = new User();
		user.setPassword(SecureUtil.md5(insertUserInfo.getPassword()));
		user.setPhone(insertUserInfo.getPhone());
		user.setName(insertUserInfo.getName());
		user.setAvatar("1");
		int count = userMapper.insert(user);

		User user1 = userService.getOne(new QueryWrapper<User>().eq("name",
				insertUserInfo.getName()));
		UserRole userRole = new UserRole();
		userRole.setUserId(user1.getId().intValue());
		userRole.setRoleId(1);
		userRoleMapper.insert(userRole);

		return Result.succ(200,"添加成功","ok");
	}

	@RequestMapping("/deleteUserById")
	@ResponseBody
	public Result deleteUserById(Long id) {
		User user = new User();
		user.setDel(0);
		user.setId(id);
		int num = userMapper.updateById(user);
		return Result.succ("ok");
	}

	@PostMapping("/backPassword")
	public Result backPassword(@Validated @RequestBody LoginDto loginDto, HttpServletRequest request, HttpServletResponse response) {

		UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("name",loginDto.getName());
//		密码加密
			User user = userService.getOne(new QueryWrapper<User>().eq("name",
					loginDto.getName()));

			user.setPassword(SecureUtil.md5(loginDto.getPassword()));
			userMapper.update(user,updateWrapper);

			return Result.succ(200,"密码修改成功","密码修改成功");
	}

	@RequestMapping("/selectShopLists")
	@ResponseBody
	public Result selectUserLists(String tableName,String name) {
		if (tableName.equals("animal")) {
			if (name == null) {
				List<Animal> animals = animalMapper.selectList(null);
				return Result.succ(animals);
			} else {
				QueryWrapper<Animal> wrapper = new QueryWrapper<>();
				wrapper.like("name", name);
				List<Animal> animals = animalMapper.selectList(wrapper);
					return Result.succ(animals);
			}
		}else if(tableName.equals("food")){
			if (name == null) {
				List<Food> foods = foodMapper.selectList(null);
				return Result.succ(foods);
			} else {
				QueryWrapper<Food> wrapper = new QueryWrapper<>();
				wrapper.like("name", name);
				List<Food> foods = foodMapper.selectList(wrapper);
				return Result.succ(foods);
			}
		}else if(tableName.equals("things")){
			if (name == null) {
				List<Things> things = thingsMapper.selectList(null);
				return Result.succ(things);
			} else {
				QueryWrapper<Things> wrapper = new QueryWrapper<>();
				wrapper.like("name", name);
				List<Things> things = thingsMapper.selectList(wrapper);
				return Result.succ(things);
			}
		}else {
			return  Result.fail(403, "找不到", null);
		}
	}

	@PostMapping("/updateImage")
	public Result updateImage(@Validated @RequestBody MultipartFile image, String fileName ,String id) {
		String base64String = null;
		try{
			if(!image.isEmpty()){
				image.transferTo(new File("C:\\Users\\LONGYOUSHENG\\Desktop\\毕设系统\\cat-mall\\src\\assets\\avatar\\"+fileName+".jpg"));
				User user = userMapper.selectById(Long.valueOf(id));
				user.setAvatar(fileName);
				int num = userMapper.updateById(user);
			}
		}catch (Exception e){
			System.out.println(e);
		}

		return Result.succ(200,"更新头像成功",fileName);

	}

	@PostMapping("/updateUserById")
	public Result updateUserById(@Validated @RequestBody UpdateUserById updateUserById  ) {
//		System.out.println(updateUserById);
		User user = userMapper.selectById(updateUserById.getId());
		user.setName(updateUserById.getName());
		user.setBirthday(updateUserById.getBirthday());
		if (updateUserById.getSex().equals("男")){
			user.setSex(0);
		}else if(updateUserById.getSex().equals("女")){
			user.setSex(1);
		}else {
			user.setSex(2);
		}
		int num = userMapper.updateById(user);

		return Result.succ("ok");
	}

	@PostMapping("/updateUserSecurityById")
	public Result updateUserSecurityById(@Validated @RequestBody UpdateUserSecurityById updateUserSecurityById  ) {
		User user = userMapper.selectById(updateUserSecurityById.getId());
		if (!updateUserSecurityById.getPhone().equals("")){
			user.setPhone(updateUserSecurityById.getPhone());
		}
		if (!updateUserSecurityById.getEmail().equals("")){
			user.setEmail(updateUserSecurityById.getEmail());
		}else {
			user.setEmail("");
		}
		int num = userMapper.updateById(user);
		return Result.succ("ok");
	}

	@PostMapping("/updateAddressById")
	public Result updateAddressById(@Validated @RequestBody UpdateAddress updateAddress  ) {
		boolean i = false;
		Address address = addressMapper.selectById(updateAddress.getId());
//		System.out.println(updateAddress.getAddress().equals(""));
		if (!updateAddress.getAddress().equals("")){
			address.setAddress(updateAddress.getAddress());
			i = true;
		}
		if (!updateAddress.getPeople().equals("")){
			address.setPeople(updateAddress.getPeople());
			i = true;
		}
		if (!updateAddress.getPhone().equals("")){
			address.setPhone(updateAddress.getPhone());
			i = true;
		}

		if (i){
			int num = addressMapper.updateById(address);
		}

		return Result.succ("ok");
	}

	@RequestMapping("/selectAddress")
	@ResponseBody
	public Result selectAddress(Long id) {
		List<Address> data = new ArrayList<Address>();

		QueryWrapper<UserAddress> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id", id.intValue());
		List<UserAddress>  userAddresses = userAddressMapper.selectList(wrapper);

		for(UserAddress i:userAddresses){
			Address address = addressService.getOne(new QueryWrapper<Address>().eq("id",
					i.getAddressId()));
			data.add(address);
		}
		System.out.println(userAddresses);
		return Result.succ(data);
	}

	@RequestMapping("/deleteAddress")
	@ResponseBody
	public Result deleteAddress(Long id) {
		UserAddress userAddress = userAddressService.getOne(new QueryWrapper<UserAddress>().eq("address_id",
				id.intValue()));
		int num1 = userAddressMapper.deleteById(userAddress.getId());

		int num2 = addressMapper.deleteById(id);

		return Result.succ("ok");
	}

	@PostMapping("/insertAddress")
	public Result insertAddress(@Validated @RequestBody InsertAddress insertAddress  ) {
		Address address = new Address();
		address.setAddress(insertAddress.getAddress());
		address.setPhone(insertAddress.getPhone());
		address.setPeople(insertAddress.getPeople());
		int count = addressMapper.insert(address);

		QueryWrapper<Address> wrapper = new QueryWrapper<>();
		wrapper.eq("people", insertAddress.getPeople());
		wrapper.eq("phone", insertAddress.getPhone());
		wrapper.eq("address", insertAddress.getAddress());
		List<Address> addresses = addressMapper.selectList(wrapper);

		for (Address i:addresses){
			UserAddress userAddress = new UserAddress();
			userAddress.setUserId(insertAddress.getId());
			userAddress.setAddressId(i.getId().intValue());
			int count1 = userAddressMapper.insert(userAddress);
		}
		return Result.succ("ok");
	}

	@PostMapping("/updateCarById")
	public Result updateCarById(@Validated @RequestBody UpdateCarById updateCarById  ) {

		QueryWrapper<Car> wrapper = new QueryWrapper<>();
		wrapper.eq("serial", updateCarById.getSerial());
		wrapper.eq("user_id", updateCarById.getId());
		Car car = carMapper.selectOne(wrapper);

		car.setCount(car.getCount()+1);

		int num = carMapper.updateById(car);

		return Result.succ("ok");
	}

	@PostMapping("/updateCarCountById")
	public Result updateCarCountById(@Validated @RequestBody UpdateCarById updateCarById  ) {
		Car car = carMapper.selectById((long)updateCarById.getId());
		car.setCount(updateCarById.getCount());
		car.setUserId(updateCarById.getUserId());

		int num = carMapper.updateById(car);

		return Result.succ("ok");
	}

	@RequestMapping("/selectCarByUserId")
	@ResponseBody
	public Result selectCar(Long id) {
		List<CarData> data = new ArrayList<CarData>();

		List<Animal> animals = animalMapper.selectList(null);
		List<Food> foods = foodMapper.selectList(null);
		List<Things> things = thingsMapper.selectList(null);

		QueryWrapper<Car> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id", id);
		List<Car> cars = carMapper.selectList(wrapper);

		for (Car i:cars){
			for (Animal a:animals){
				if(a.getSerial().equals(i.getSerial())){
					CarData carData=new CarData();
					carData.setUserId(i.getUserId());
					carData.setSerial(i.getSerial());
					carData.setImgUrl(a.getImgUrl());
					carData.setName(a.getName());
					carData.setPrice(a.getPrice());
					carData.setId(i.getId());
					carData.setCount(i.getCount());

					data.add(carData);
				}
			}
			for (Food f:foods){
				if(f.getSerial().equals(i.getSerial())){
					CarData carData=new CarData();
					carData.setUserId(i.getUserId());
					carData.setSerial(i.getSerial());
					carData.setImgUrl(f.getImgUrl());
					carData.setName(f.getName());
					carData.setPrice(f.getPrice());
					carData.setId(i.getId());
					carData.setCount(i.getCount());
					data.add(carData);
				}
			}
			for (Things t:things){
				if(t.getSerial().equals(i.getSerial())){
					CarData carData=new CarData();
					carData.setUserId(i.getUserId());
					carData.setSerial(i.getSerial());
					carData.setImgUrl(t.getImgUrl());
					carData.setName(t.getName());
					carData.setPrice(t.getPrice());
					carData.setId(i.getId());
					carData.setCount(i.getCount());
					data.add(carData);
				}
			}
		}

		return Result.succ(data);
	}

	@RequestMapping("/deleteCarByCarId")
	@ResponseBody
	public Result deleteCarByCarId( Long id) {
		int num = carMapper.deleteById(id);
		System.out.println(""+id);
		return Result.succ("ok");
	}

	@RequestMapping("/deleteCarByUserId")
	@ResponseBody
	public Result deleteCarByUserId(Long id) {
		int num = carMapper.deleteById(id);
		return Result.succ(num);
	}

	@PostMapping("/insertCar")
	public Result insertCar(@Validated @RequestBody InsertCar insertCar  ) {
		Car car = new Car();
		car.setSerial(insertCar.getSerial());
		car.setUserId(insertCar.getId());
		if (insertCar.getCount() != null){
			car.setCount(Integer.parseInt(insertCar.getCount()));
		}else {
			car.setCount(1);
		}
		int count1 = carMapper.insert(car);

		QueryWrapper<Car> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id", insertCar.getId());
		wrapper.eq("serial", insertCar.getSerial());
		Car data = carMapper.selectOne(wrapper);

		return Result.succ(data);
	}

	@RequestMapping("/selectCarLikeName")
	@ResponseBody
	public Result selectCarLikeName(String name) {
		List<CarData> data = new ArrayList<CarData>();

		QueryWrapper<Animal> wrapper1 = new QueryWrapper<>();
		wrapper1.like("name", name);
		List<Animal> animals = animalMapper.selectList(wrapper1);

		QueryWrapper<Food> wrapper2 = new QueryWrapper<>();
		wrapper2.like("name", name);
		List<Food> foods = foodMapper.selectList(wrapper2);

		QueryWrapper<Things> wrapper3 = new QueryWrapper<>();
		wrapper3.like("name", name);
		List<Things> things = thingsMapper.selectList(wrapper3);

		for (Animal i:animals){
			QueryWrapper<Car> wrapper = new QueryWrapper<>();
			wrapper.eq("serial", i.getSerial());
			List<Car> data1 = carMapper.selectList(wrapper);
			if (data1!=null){
				for(Car n:data1){
					CarData carData=new CarData();
					carData.setCount(n.getCount());
					carData.setId(n.getId());
					carData.setSerial(n.getSerial());
					carData.setImgUrl(i.getImgUrl());
					carData.setUserId(n.getUserId());
					data.add(carData);
				}
			}
		}

		for (Food i:foods){
			QueryWrapper<Car> wrapper = new QueryWrapper<>();
			wrapper.eq("serial", i.getSerial());
			List<Car> data1 = carMapper.selectList(wrapper);
			if (data1!=null){
				for(Car n:data1){
					CarData carData=new CarData();
					carData.setCount(n.getCount());
					carData.setId(n.getId());
					carData.setSerial(n.getSerial());
					carData.setImgUrl(i.getImgUrl());
					carData.setUserId(n.getUserId());
					data.add(carData);
				}
			}
		}

		for (Things i:things){
			QueryWrapper<Car> wrapper = new QueryWrapper<>();
			wrapper.eq("serial", i.getSerial());
			List<Car> data1 = carMapper.selectList(wrapper);
			if (data1!=null){
				for(Car n:data1){
					CarData carData=new CarData();
					carData.setCount(n.getCount());
					carData.setId(n.getId());
					carData.setSerial(n.getSerial());
					carData.setImgUrl(i.getImgUrl());
					carData.setUserId(n.getUserId());
					data.add(carData);
				}
			}
		}

		return Result.succ(data);
	}

	@RequestMapping("/selectOrderLikeName")
	@ResponseBody
	public Result selectOrderLikeName(String name) {
		List<Animal> animals = animalMapper.selectList(null);
		List<Food> foods = foodMapper.selectList(null);
		List<Things> things = thingsMapper.selectList(null);

		QueryWrapper<Catmallorder> wrapper = new QueryWrapper<>();
		wrapper.like("name", name);
		List<Catmallorder> catmallorders = catmallorderMapper.selectList(wrapper);

		List<OrderData> data = new ArrayList<OrderData>();

		for(Catmallorder i:catmallorders){
			OrderData data1 = new OrderData();
			data1.setId(i.getId());
			data1.setCreated(i.getCreated());
			data1.setName(i.getName());
			data1.setNum(i.getNum());
			data1.setOrdernum(i.getOrdernum());
			data1.setPeople(i.getPeople());
			data1.setTotal(i.getTotal());
			data1.setStatu(i.getStatu());
			for(Animal a:animals){
				if (a.getName().equals(i.getName())){
					data1.setImgUrl(a.getImgUrl());
				}
			}
			for(Food f:foods){
				if (f.getName().equals(i.getName())){
					data1.setImgUrl(f.getImgUrl());
				}
			}
			for(Things t:things){
				if (t.getName().equals(i.getName())){
					data1.setImgUrl(t.getImgUrl());
				}
			}
			data.add(data1);
		}

		return Result.succ(data);
	}

	@PostMapping("/updateOrderById")
	public Result updateOrderById(@Validated @RequestBody UpDateOrder upDateOrder ) {
		Catmallorder catmallorder = catmallorderMapper.selectById(upDateOrder.getId());
		catmallorder.setStatu(upDateOrder.getStatu());
		catmallorderMapper.updateById(catmallorder);

		return Result.succ("ok");
	}

	@RequestMapping("/selectOrder")
	@ResponseBody
	public Result selectOrder(Long id) {
		List<Animal> animals = animalMapper.selectList(null);
		List<Food> foods = foodMapper.selectList(null);
		List<Things> things = thingsMapper.selectList(null);

		QueryWrapper<Catmallorder> wrapper = new QueryWrapper<>();
		wrapper.eq("user_id", id);
		List<Catmallorder> catmallorders = catmallorderMapper.selectList(wrapper);
		List<OrderData> data = new ArrayList<OrderData>();

		for(Catmallorder i:catmallorders){
			OrderData data1 = new OrderData();
			data1.setId(i.getId());
			data1.setCreated(i.getCreated());
			data1.setName(i.getName());
			data1.setNum(i.getNum());
			data1.setOrdernum(i.getOrdernum());
			data1.setPeople(i.getPeople());
			data1.setTotal(i.getTotal());
			data1.setStatu(i.getStatu());
			for(Animal a:animals){
				if (a.getName().equals(i.getName())){
					data1.setImgUrl(a.getImgUrl());
				}
			}
			for(Food f:foods){
				if (f.getName().equals(i.getName())){
					data1.setImgUrl(f.getImgUrl());
				}
			}
			for(Things t:things){
				if (t.getName().equals(i.getName())){
					data1.setImgUrl(t.getImgUrl());
				}
			}
			data.add(data1);
		}


		return Result.succ(data);
	}

	@RequestMapping("/deleteOrder")
	@ResponseBody
	public Result deleteOrder(Long id) {
		int num = catmallorderMapper.deleteById(id);
		return Result.succ(num);
	}

	@PostMapping("/insertOrder")
	public Result insertOrder(@Validated @RequestBody InsertOrder insertOrder  ) {
		Random r = new Random();
		String numstr = "";
		int i = 0;
		while (i < 10){
			numstr = numstr + r.nextInt(10);
			i++;
		}

		Address address = addressService.getOne(new QueryWrapper<Address>().eq("id",
				insertOrder.getAddressid()));

		Catmallorder catmallorder = new Catmallorder();
		catmallorder.setCreated(LocalDateTime.now());
		catmallorder.setName(insertOrder.getName());
		catmallorder.setPeople(address.getPeople());
		catmallorder.setNum(insertOrder.getNum());
		catmallorder.setStatu(0);
		catmallorder.setOrdernum(numstr);
		catmallorder.setTotal(insertOrder.getTotal());
		catmallorder.setUserId(insertOrder.getId());
		System.out.println(catmallorder);

		int count = catmallorderMapper.insert(catmallorder);

		return Result.succ("ok");
	}

	@PostMapping("/updateRoleById")
	public Result updateRoleById(@Validated @RequestBody UpdateRole updateRole  ) {
		UserRole userRole = userRoleService.getOne(new QueryWrapper<UserRole>().eq("user_id",
				updateRole.getUserId()));
		System.out.println(userRole);
		if (userRole.getRoleId() == 3){
			return Result.fail("最高等级");
		}else{
			userRole.setRoleId(userRole.getRoleId()+1);
			int num = userRoleMapper.updateById(userRole);
			return Result.succ(num);
		}
	}


}
//
//	@Autowired
//	BCryptPasswordEncoder passwordEncoder;

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

