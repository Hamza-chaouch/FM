package com.solaries.fileManager.services.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

public interface StorageService {



	void store(MultipartFile file);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	// initiallement commande line
	void deleteAll();
	void init();


	List<?> loadAllFiles ();

	void deleteFile (String filename);
}
