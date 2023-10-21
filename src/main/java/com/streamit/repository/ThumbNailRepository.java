package com.streamit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.streamit.model.Db.ThumbNail;

public interface ThumbNailRepository  extends JpaRepository<ThumbNail, Integer>{

}
