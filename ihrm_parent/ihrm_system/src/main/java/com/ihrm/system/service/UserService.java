package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.domain.system.Role;
import com.ihrm.domain.system.User;
import com.ihrm.system.dao.RoleDao;
import com.ihrm.system.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private IdWorker idWorker;
    /**
     * 保存用户
     */
    public void save(User user){
        //设置主键  调用Dao 保存
        String id =  idWorker.nextId()+"";
        user.setPassword("123456"); //设置初始密码
        user.setEnableState(1);  //默认启用状态
        user.setId(id);
        userDao.save(user);
    }

    /**
     * 更新部门
     */
    public void update(User user) {
        //先查询有没有
        User targer = userDao.findById(user.getId()).get();
        //设置用户属性
        targer.setUsername(user.getUsername());
        targer.setPassword(user.getPassword());
        targer.setDepartmentId(user.getDepartmentId());
        targer.setDepartmentName(user.getDepartmentName());
        userDao.save(targer);

    }

    /**
     * 根据ID查询
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }


    /**
     * 查询全部用户列表
     * 前端传入参数  map集合形式
     * hasdept
     * departmentId
     *companyId
     *
     */
    public Page findAll(Map<String,Object> map, int page, int size) {
        //需要查询条件
        Specification<User> spec = new Specification<User>() {
            @Override
            /**
             * 动态拼接查询条件
             */
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据请求的companyId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("companyId"))) {
                    list.add(criteriaBuilder.equal(root.get("companyId").as(String.class),(String)map.get("companyId")));
                }
                //根据请求的departmentId是否为空构造查询条件
                if (!StringUtils.isEmpty(map.get("departmentId"))) {
                    list.add(criteriaBuilder.equal(root.get("departmentId").as(String.class),(String)map.get("departmentId")));
                }
                if(!StringUtils.isEmpty(map.get("hasDept"))) {
                    //根据hasDept判断是否分配部门 0未分配  1已分配
                    if("0".equals((String) map.get("hasDept"))){
                        list.add(criteriaBuilder.isNull(root.get("departmentId")));
                    }else {
                        list.add(criteriaBuilder.isNotNull(root.get("departmentId")));
                    }


                }


                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        //分页
        Page<User> pageUser = userDao.findAll(spec, new PageRequest(page-1, size));

        return pageUser;
    }
    /**
     * 删除用户
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }
    /**
     * 分配角色
     *
     */
    public void assignRoles(String userId,List<String> roleIds) {
        //根据id查询用户
        User user = userDao.findById(userId).get();
        //设置用户角色集合
        Set<Role> roles = new HashSet<>();
        for (String roleId : roleIds) {
            Role role = roleDao.findById(roleId).get();
            roles.add(role);
        }
        //设置用户和集合的关系
        user.setRoles(roles);
        //更新用户
        userDao.save(user);
    }
    /**
     * 登录
     *根据mobile查询用户
     */
    public User findByMobile(String mobile){
        return  userDao.findByMobile(mobile);

    }

}
