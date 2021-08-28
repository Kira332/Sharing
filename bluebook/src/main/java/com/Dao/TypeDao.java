package com.Dao;

import com.pojo.Type;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TypeDao extends JpaRepository<Type,Long> {

}
