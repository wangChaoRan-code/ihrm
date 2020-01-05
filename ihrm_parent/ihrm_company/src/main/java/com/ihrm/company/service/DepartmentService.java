package com.ihrm.company.service;


import com.ihrm.common.service.BaseService;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.company.dao.DepartmentDao;
import com.ihrm.domain.company.Department;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Optional;

@Service
public class DepartmentService extends BaseService {
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private IdWorker idWorker;


    /**
     * 保存部门
     */
    public void save(Department department){
        //设置主键  调用Dao 保存
        String id =  idWorker.nextId()+"";
        department.setId(id);
        departmentDao.save(department);
    }

    /**
     * 更新部门
     */
    public void update(Department department) {
        //先查询有没有
        Department dept = departmentDao.findById(department.getId()).get();
        //设置部门属性
        dept.setCode(department.getCode());
        dept.setCompanyId(department.getCompanyId());
        dept.setManager(department.getManager());
        dept.setName(department.getName());
        departmentDao.save(dept);

    }

    /**
     * 根据ID查询
     */
    public Department findById(String id) {
        return departmentDao.findById(id).get();
    }


    /**
     * 查询全部部门列表
     */
    public List<Department> findAll(String companyId) {
        /**
         * 构造查询条件
         *
         */
//        Specification<Department> spec = new Specification<Department>() {
//            @Override
//            /**
//             * root 包含了所有的对象属性
//             * criteriaQuery  更加高级的查询 一般不用
//             * criteriaBuilder  构造查询条件
//             */
//            public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                return criteriaBuilder.equal(root.get("companyId").as(String.class),companyId);
//            }
//        };
        return departmentDao.findAll(getSpec(companyId));
    }
    /**
     * 删除部门
     */
    public void deleteById(String id) {
        departmentDao.deleteById(id);
    }
}
