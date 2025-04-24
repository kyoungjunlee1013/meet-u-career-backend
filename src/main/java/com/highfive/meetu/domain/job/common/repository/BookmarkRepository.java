package com.highfive.meetu.domain.job.common.repository;

import com.highfive.meetu.domain.job.common.entity.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findByAccount_Id(Long accountId);

}
