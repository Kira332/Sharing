package com.Dao;

import com.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegisterDao extends JpaRepository<User,Long> {
    List<User> findAllByPhone(String phone);
    List<User> findAllByUsername(String phone);
}
