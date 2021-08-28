package com.Dao;

import com.pojo.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User,String> {
    User findByUsername(String username);
    User findByPhone(String phone);
    @Modifying
    @Query(value = "update user u set u.password= ?1 where u.phone=?2",nativeQuery = true)
    void updUserPwd(String password, String phone);
}
