package com.solaries.fileManager.repositories;

import com.solaries.fileManager.entities.SODocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SODocumentRepository extends JpaRepository<SODocument,Long> {
}
