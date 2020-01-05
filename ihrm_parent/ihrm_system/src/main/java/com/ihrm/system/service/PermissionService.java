package com.ihrm.system.service;

import com.ihrm.common.entity.ResultCode;
import com.ihrm.common.exception.CommonException;
import com.ihrm.common.utils.BeanMapUtils;
import com.ihrm.common.utils.IdWorker;
import com.ihrm.common.utils.PermissionConstants;
import com.ihrm.domain.system.Permission;
import com.ihrm.domain.system.PermissionApi;
import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.PermissionPoint;
import com.ihrm.system.dao.PermissionApiDao;
import com.ihrm.system.dao.PermissionDao;
import com.ihrm.system.dao.PermissionMenuDao;
import com.ihrm.system.dao.PermissionPointDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@Service
@Transactional  //事物相关支持
public class PermissionService {
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PermissionMenuDao permissionMenuDao;
    @Autowired
    private PermissionPointDao permissionPointDao;
    @Autowired
    private PermissionApiDao permissionApiDao;
    @Autowired
    private IdWorker idWorker;


    /**
     * 保存权限
     */
    public void save(Map<String,Object> map) throws Exception {
        //设置主键  调用Dao 保存
        String id =  idWorker.nextId()+"";
        //根据map构造permission对象
        Permission permission = BeanMapUtils.mapToBean(map,Permission.class);
        permission.setId(id);
        //根据类型构造不同的资源对象（菜单 按钮 api）
        int type = permission.getType();
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu Menu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                Menu.setId(id);
                permissionMenuDao.save(Menu);
                break;
                case PermissionConstants.PY_POINT:
                    PermissionPoint point = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                    point.setId(id);
                    permissionPointDao.save(point);
                 break;
                 case PermissionConstants.PY_API:
                     PermissionApi api = BeanMapUtils.mapToBean(map,PermissionApi.class);
                     api.setId(id);
                     permissionApiDao.save(api);
                     break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //保存
        permissionDao.save(permission);
    }

    /**
     * 更新权限
     */
    public void update(Map<String,Object> map) throws Exception {
        Permission perm = BeanMapUtils.mapToBean(map,Permission.class);
        //通过传递的id查询权限
        Permission permission = permissionDao.findById(perm.getId()).get();
        permission.setCode(perm.getCode());
        permission.setName(perm.getName());
        permission.setDescription(perm.getDescription());
        permission.setEnVisible(perm.getEnVisible());
        //根据类型构造不同资源
        //根据类型构造不同的资源对象（菜单 按钮 api）
        int type = perm.getType();
        switch (type) {
            case PermissionConstants.PY_MENU:
                PermissionMenu Menu = BeanMapUtils.mapToBean(map,PermissionMenu.class);
                Menu.setId(perm.getId());
                permissionMenuDao.save(Menu);
                break;
            case PermissionConstants.PY_POINT:
                PermissionPoint point = BeanMapUtils.mapToBean(map,PermissionPoint.class);
                point.setId(perm.getId());
                permissionPointDao.save(point);
                break;
            case PermissionConstants.PY_API:
                PermissionApi api = BeanMapUtils.mapToBean(map,PermissionApi.class);
                api.setId(perm.getId());
                permissionApiDao.save(api);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
        //保存
        permissionDao.save(permission);
    }

    /**
     * 根据ID查询
     * 查询权限
     * 根据权限的类型查询资源
     * 构造Map集合
     */
    public Map<String, Object> findById(String id) throws CommonException {
        Permission perm = permissionDao.findById(id).get();
        int type = perm.getType();
        Object object = null;
        if (type == PermissionConstants.PY_MENU) {
            object = permissionMenuDao.findById(id).get();
        }else if (type == PermissionConstants.PY_POINT) {
            object = permissionPointDao.findById(id).get();
        }else if (type == PermissionConstants.PY_API) {
            object = permissionApiDao.findById(id).get();
        }else {
            throw new CommonException(ResultCode.FAIL);
        }
        Map<String, Object> map = BeanMapUtils.beanToMap(object);
        map.put("name",perm.getName());
        map.put("type",perm.getType());
        map.put("code",perm.getCode());
        map.put("description",perm.getDescription());
        map.put("pid",perm.getPid());
        map.put("enVisible",perm.getEnVisible());
        return map;
    }


    /**
     * 查询全部权限列表
     *type 0 菜单+按钮（权限点） 1 menu 2point  3api
     * enVisible  0 select all saas 平台最高权限   1 查询企业权限
     * pid father id
     *
     */
    public List<Permission> findAll(Map<String,Object> map) {
        //需要查询条件
        Specification<Permission> spec = new Specification<Permission>() {
            @Override
            /**
             * 动态拼接查询条件
             */
            public Predicate toPredicate(Root<Permission> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList<>();
                //根据父id查询
                if (!StringUtils.isEmpty(map.get("pid"))) {
                    list.add(criteriaBuilder.equal(root.get("pid").as(String.class),(String)map.get("pid")));
                }
                //根据请求的enVisible查询
                if (!StringUtils.isEmpty(map.get("enVisible"))) {
                    list.add(criteriaBuilder.equal(root.get("enVisible").as(String.class),(String)map.get("enVisible")));
                }
                if(!StringUtils.isEmpty(map.get("type"))) {
                    String ty = (String) map.get("type");
                    CriteriaBuilder.In<Object> in = criteriaBuilder.in(root.get("type"));
                    if("0".equals(ty)) {
                        in.value(1).value(2);
                    }else {
                        in.value(Integer.parseInt(ty));
                    }
                }


                return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
            }
        };
        return permissionDao.findAll(spec);
    }
    /**
     * 删除权限
     * 删除权限
     * 删除权限对应的资源
     */
    public void deleteById(String id) throws CommonException {
        Permission perm = permissionDao.findById(id).get();
        permissionDao.delete(perm);
        int type = perm.getType();
        switch (type) {
            case PermissionConstants.PY_MENU:
                permissionMenuDao.deleteById(id);
                break;
            case PermissionConstants.PY_POINT:
                permissionPointDao.deleteById(id);
                break;
            case PermissionConstants.PY_API:
                permissionApiDao.deleteById(id);
                break;
            default:
                throw new CommonException(ResultCode.FAIL);
        }
    }
}
