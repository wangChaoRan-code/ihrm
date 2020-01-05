package com.ihrm.system.service;

import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.Role;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.RoleDao;
import com.sun.xml.internal.ws.spi.db.WrapperComposite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private IdWorker idWorker;


    /**
     * 保存用户
     */
    public void save(Role role){
        //设置主键  调用Dao 保存
        role.setId(idWorker.nextId()+"");
        roleDao.save(role);
    }

    /**
     * 更新部门
     */
    public void update(Role role) {
        //先查询有没有
        Role targer = roleDao.findById(role.getId()).get();
        //设置用户属性
        targer.setDescription(role.getDescription());
        targer.setName(role.getName());
        roleDao.save(targer);

    }

    /**
     * 根据ID查询
     */
    public Role findById(String id) {
        return roleDao.findById(id).get();
    }


    /**
     * 查询全部用户列表
     * 前端传入参数  map集合形式
     * hasdept
     * departmentId
     *companyId
     *
     */
    public Page<Role> findSearch(String companyId, int page, int size) {
        Specification<Role> specification = new Specification<Role>() {

            public Predicate toPredicate(Root<Role> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                return cb.equal(root.get("companyId").as(String.class),companyId);
            }
        };
        return roleDao.findAll(specification, PageRequest.of(page-1, size));
    }
    /**
     * 删除角色
     */
    public void deleteById(String id) {
        roleDao.deleteById(id);
    }

    /**
     *
     * 分配权限
     *
     */

    public void assignPerms(String roleId, List<String> permIds) {
        //根据id查询角色
        Role role = roleDao.findById(roleId).get();
        //构造权限的集合
        Set<Permission> perms = new HashSet<>();
        for (String permId : permIds) {
            Permission permission = permissionDao.findById(permId).get();
            //根据父Id和类型查询Api权限列表
            List<Permission> apiList = permissionDao.findByTypeAndPid(PermissionConstants.PY_API,permission.getPid());
            perms.addAll(apiList);//自定义赋予api权限
            perms.add(permission);//当前菜单 按钮权限
        }
        //设置权限与角色的关系
        role.setPermissions(perms);
        //更新角色
        roleDao.save(role);
    }
}
