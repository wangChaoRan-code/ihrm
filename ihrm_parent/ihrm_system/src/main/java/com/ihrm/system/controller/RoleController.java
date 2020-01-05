package com.ihrm.system.controller;

import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.PageResult;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.response.DeptListResult;
import com.ihrm.domain.system.Role;
import com.ihrm.system.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

//解决跨域
@CrossOrigin
//声明restController
@RestController
//设置父路径
@RequestMapping(value = "/sys")
public class RoleController extends BaseController {
    @Autowired
    private RoleService roleService;
    /**
     * 分配权限
     */
    @RequestMapping(value = "/role/assignPrem",method = RequestMethod.POST)
    public Result assignRoles(@RequestBody Map<String,Object> map) {
        //获取被分配的角色ID
        String roleId = (String) map.get("id");
        //获取权限的id列表
        List<String> permIds =(List<String>) map.get("permIds");
        //调用service 完成角色分配
        roleService.assignPerms(roleId,permIds);
        return new Result(ResultCode.SUCCESS);

    }


    /**
     * 新建保存
     */
    @RequestMapping(value = "/role",method = RequestMethod.POST)
    public Result save(@RequestBody Role role) {
        //设置保存的企业ID
        /**
         * 企业id目前使用1 以后解决
         */
        role.setCompanyId(companyId);
        //调用service完成保存企业
        roleService.save(role);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
         /**
         * 分页查询角色
         */
    @RequestMapping(value = "/role", method = RequestMethod.GET)
    public Result findByPage(int page,int pagesize,Role role) throws Exception {
        Page<Role> searchPage = roleService.findSearch(companyId, page, pagesize);
        PageResult<Role> pr = new PageResult(searchPage.getTotalElements(),searchPage.getContent());
        return new Result(ResultCode.SUCCESS,pr);
}
    /**
     * 根据Id查询role
     */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        Role role = roleService.findById(id);
        return new Result(ResultCode.SUCCESS,role);
    }
    /**
     * 根据id修改
     *
     */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id, @RequestBody Role role) {
        //修改部门id
        role.setId(id);
        //调用service更新
        roleService.update(role);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 根据id删除
     */
    @RequestMapping(value = "/role/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        roleService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

}
