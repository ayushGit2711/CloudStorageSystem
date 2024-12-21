package com.ayushspring.cloudstoragesystem.repository;


import com.ayushspring.cloudstoragesystem.model.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByUploadedByUsername(String username);
}
