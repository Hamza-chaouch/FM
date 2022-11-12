package com.solaries.fileManager.repositories;

import com.solaries.fileManager.entities.SODocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SODocumentRepository extends JpaRepository<SODocument,Long> {
   // @Query("SELECT s.id ,s.filePath , s.name , s.createdAt , s.size  FROM SODocument s where s.name = :name")
    @Query(value = "select new com.solaries.fileManager.entities.SODocument(s.id ,s.filePath , s.name , s.createdAt , s.size) FROM SODocument s where s.name = :name ")
    SODocument findByName (@Param("name") String name);

}
