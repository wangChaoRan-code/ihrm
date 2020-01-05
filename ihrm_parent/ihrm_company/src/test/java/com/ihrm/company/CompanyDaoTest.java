package com.ihrm.company;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.ihrm.company.dao.CompanyDao;
import com.ihrm.domain.company.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


/**
 * 自定义dao继承接口
 *
 *
 */

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class CompanyDaoTest {
    @Autowired
    private CompanyDao companyDao;

    @Test
    public void test(){

//        companyDao.save();  保存或更新（id）
//        companyDao.deleteById();     根据id删除
//        companyDao.findById();            根据id查询
//        companyDao.findAll()  查询全部


        Company company = companyDao.findById("1").get();
        System.out.println(company);
    }
}
