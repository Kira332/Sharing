package com.Dao;

//import com.pojo.Focus;
import com.pojo.Focus;
import com.pojo.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FocusDao extends JpaRepository<Focus,Long> {
    List<Focus> findAllByFansName(String fansName);
    List<Focus> findAllByIdolName(String idolName);
    Focus findByFansNameAndIdolName(String fansName,String idolName);
    boolean existsByIdolNameAndFansName(String idolName,String fansName);
}
