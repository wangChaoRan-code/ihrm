package com.ihrm.company.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyService {
    @Autowired
    private CompanyDao companyDao;
    @Autowired
    private IdWorker idWorker;

    /**
     * 保存
     * 1.配置IDwork（id生成器）
     * 在service中注入IDwork
     * 通过IDwork生产id
     * 保存企业
     */
    public void add(Company company){
        //基本属性设置
        String id = idWorker.nextId()+"";
        company.setId(id);
        //默认的状态
        company.setAuditState("0");//0未审核  1 已审核
        company.setState(1);  //0未激活  1已激活
        companyDao.save(company);
    }

    /**
     * 更新
     * 1参数：Company
     * 2根据ID查询
     * 3设置修改参数
     * 4调用Dao完成更新
     *
     */
    public void update(Company company) {
        Company temp = companyDao.findById(company.getId()).get();
        temp.setName(company.getName());
        temp.setCompanyPhone(company.getCompanyPhone());
        companyDao.save(temp);

    }


    /**
     * 删除
     */
    public void deleteById(String id){
        companyDao.deleteById(id);
    }

    /**
     * 根据ID查询
     */
    public Company findById(String id) {
        return companyDao.findById(id).get();
    }

    /**
     * 查询列表
     */
    public List<Company> findAll(){
        return companyDao.findAll();
    }



}
