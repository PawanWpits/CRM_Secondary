package com.wpits.services.serviceImpls;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.wpits.exceptions.BadApiRequestException;
import com.wpits.services.FileService;

@Service
public class FileServiceImpl implements FileService {
	
	private Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

	@Override
	public String uploadFile(MultipartFile file, String path) throws IOException {
		//abc.png  original file ko . ke baad tod lenge like .png
		String originalFilename = file.getOriginalFilename();
		//System.out.println(originalFilename);
		logger.info("fileName {}",originalFilename);
		
		String fileName = UUID.randomUUID().toString();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
		String fileNameWithExtension = fileName+extension;
		//String fullPathWithFileName = path + File.separator + fileNameWithExtension;
		String fullPathWithFileName = path +fileNameWithExtension;
		
		if (extension.equalsIgnoreCase(".png") || extension.equalsIgnoreCase(".jpg") || extension.equalsIgnoreCase(".jpeg")) {
						
			File folder = new File(path);
			
			if (!folder.exists()) {
				
				folder.mkdirs();
			}			
			
			Files.copy(file.getInputStream(), Paths.get(fullPathWithFileName));
			return fileNameWithExtension;	
			
		}else {
			throw new BadApiRequestException("file with this" +extension + " not allowed !!");
		}
	}

	@Override
	public InputStream getResource(String path, String name) throws FileNotFoundException {
		 	
			//String fullPath = path + File.separator + name;
			
			String fullPath = path + name;
	        InputStream inputStream = new FileInputStream(fullPath);
	        
	        return inputStream;
	}

}
