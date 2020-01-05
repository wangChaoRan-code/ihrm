package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.utils.JwtUtils;
import com.ihrm.domain.system.User;
import com.ihrm.system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
//解决跨域
@CrossOrigin
//声明restController
@RestController
//设置父路径
@RequestMapping(value = "/sys")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;
    /**
     * 分配角色
     */
    @RequestMapping(value = "/user/assignRole",method = RequestMethod.POST)
    public Result assignRoles(@RequestBody Map<String,Object> map) {
        //获取被分配的用户ID
        String userId = (String) map.get("id");
        //获取角色的id列表
        List<String> roleIds =(List<String>)map.get("roleIds");
        //调用service 完成角色分配
        userService.assignRoles(userId,roleIds);
        return new Result(ResultCode.SUCCESS);

    }
    /**
     * 新建保存
     */
    @RequestMapping(value = "/user",method = RequestMethod.POST)
    public Result save(@RequestBody User user) {
        //设置保存的企业ID
        /**
         * 企业id目前使用1 以后解决
         */
        user.setCompanyId(companyId);
        user.setCompanyName(companyName);
        //调用service完成保存企业
        userService.save(user);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    /**
     *查询企业列表
     *
     */
    @RequestMapping(value = "/user",method = RequestMethod.GET)
    public Result findAll(int page,int size,@RequestParam Map map) {

        //完成查询
        Page<User> pageUser = userService.findAll(map,page,size);
        //构造返回结果
        PageResult<User> pageResult = new PageResult(pageUser.getTotalElements(),pageUser.getContent());
        return new Result(ResultCode.SUCCESS,pageResult);
    }
    /**
     * 根据Id查询department
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        User user = userService.findById(id);
        return new Result(ResultCode.SUCCESS,user);
    }
    /**
     * 根据id修改
     *
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id,@RequestBody User user) {
        //修改部门id
        user.setId(id);
        //调用service更新
        userService.update(user);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 根据id删除
     */
    @RequestMapping(value = "/user/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        userService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 登录
     * 通过servic根据mobile查询用户信息
     * 比较password
     *
     *
     */
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public Result login(@RequestBody Map<String,Object> loginMap) {
        String mobile =(String)loginMap.get("mobile");
        String password =(String)loginMap.get("password");
        //通过servic根据mobile查询用户信息
        User user = userService.findByMobile(mobile);
        if(user == null || !password.equals(user.getPassword())) {
            return new  Result(ResultCode.MOBILE_PASSWORD_ERROY);
        }else {
            //登录成功
            Map<String,Object> map = new HashMap<>();
            map.put("companyId",user.getCompanyId());
            map.put("companyName",user.getCompanyName());
            map.put("departmentId",user.getDepartmentId());
            String token = jwtUtils.createJwt(user.getId(), user.getUsername(), map);
            return new Result(ResultCode.SUCCESS,token);
        }
    }
}
