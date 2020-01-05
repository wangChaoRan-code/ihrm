package com.ihrm.company.comtroller;


import com.ihrm.common.controller.BaseController;
import com.ihrm.common.entity.Result;
import com.ihrm.common.entity.ResultCode;
import com.ihrm.company.service.CompanyService;
import com.ihrm.company.service.DepartmentService;
import com.ihrm.domain.company.Company;
import com.ihrm.domain.company.Department;
import com.ihrm.domain.company.response.DeptListResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//解决跨域
@CrossOrigin
//声明restController
@RestController
//设置父路径
@RequestMapping(value = "/company")  //company/department
public class DepartmentController extends BaseController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private CompanyService companyService;
    /**
     * 新建保存
     */
@RequestMapping(value = "/department",method = RequestMethod.POST)
    public Result save(@RequestBody Department department) {
        //设置保存的企业ID
        /**
        * 企业id目前使用1 以后解决
        */
        department.setCompanyId(companyId);
        //调用service完成保存企业
        departmentService.save(department);
        //构造返回结果
        return new Result(ResultCode.SUCCESS);
    }
    /**
     *查询企业列表
     * 制定企业Id
     *
     */
    @RequestMapping(value = "/department",method = RequestMethod.GET)
    public Result findAll() {
        //指定企业
        Company company = companyService.findById(companyId);
        //完成查询
        List<Department> list = departmentService.findAll(companyId);
        //返回结果
        DeptListResult deptListResult = new DeptListResult(company,list);
        return new Result(ResultCode.SUCCESS,deptListResult);
    }
    /**
     * 根据Id查询department
     */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable(value = "id") String id) {
        Department department = departmentService.findById(id);
        return new Result(ResultCode.SUCCESS,department);
    }
    /**
     * 根据id修改
     *
     */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable(value = "id") String id,@RequestBody Department department) {
        //修改部门id
        department.setId(id);
        //调用service更新
        departmentService.update(department);
        return new Result(ResultCode.SUCCESS);
    }
    /**
     * 根据id删除
     */
    @RequestMapping(value = "/department/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable(value = "id") String id) {
        departmentService.deleteById(id);
        return new Result(ResultCode.SUCCESS);
    }

}
