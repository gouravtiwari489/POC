package com.osi.datagen.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.osi.datagen.domain.CustomUserDetails;
import com.osi.datagen.service.DataGenerationService;
import com.osi.datagen.service.LogoutService;
import com.osi.datagen.utils.ZipUtil;
import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
public class DownloadController {
	
	@Autowired
	public DataGenerationService dataGenerationService;
	@Autowired
	public LogoutService logoutService;
	@Value("${file.download.path}")
	public String fileDownloadPath;
	@Value("${file.types}")
	public String[] allFileTypes;

	@PostMapping("/download")
	public @ResponseBody byte[] downloadExcelFile(
				@RequestParam(name = "fileType", required = true) String fileType,
				@RequestParam(name = "rowCount", required = true) int rowCount,
				@RequestParam(name = "domainType", required = true) String domainType,
				@RequestParam(name = "updatedMappedData", required = true) String updatedMappedData)
						throws Exception {
			log.info("@@@@@@@@@@@@@@@@@ rowCount   " + rowCount + "   @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
			List<String> fileTypes = new ArrayList<>(Arrays.asList(allFileTypes));

			if (user.getMap() == null
					|| !user.getMap().containsKey(fileType)
					|| user.getMappedData() == null
					|| !user.getMappedData().equals(updatedMappedData)
					|| user.getMap().get(fileType) != rowCount) {
				logoutService.clearPreExistingUserData(fileDownloadPath, updatedMappedData, user, fileTypes);
				dataGenerationService.generateData(updatedMappedData, fileType, rowCount, domainType);
				if (user.getMap() == null) user.setMap(new HashMap<String, Integer>());
				(user.getMap()).put(fileType, rowCount);
				user.setMappedData(updatedMappedData);
			}

			String filePath = ZipUtil.createZipFiles(fileType, user);
			BufferedInputStream isr = new BufferedInputStream(new FileInputStream(new File(filePath)));
			byte[] bytes = IOUtils.toByteArray(isr);
			isr.close();
			return bytes;
		}
}