package com.itxiaoer.commons.sample.jpa.repository;

import com.itxiaoer.commons.sample.jpa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author : liuyk
 */
public interface UserRepository extends JpaRepository<User, String>, JpaSpecificationExecutor<User> {
}
