package com.ihrm.system.dao;

import com.ihrm.domain.system.PermissionMenu;
import com.ihrm.domain.system.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PermissionMenuDao extends JpaRepository<PermissionMenu,String>, JpaSpecificationExecutor<PermissionMenu> {
}
