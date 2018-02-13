package com.datagenerator.demo.serviceImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.datagenerator.demo.domain.GenerateDataObject;
import com.datagenerator.demo.utils.Worker;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class DataGenerationService {
	
	private static final String FKURI = "http://localhost:9080/datagenerator/getFKRelation";

	private static final String URI = "http://localhost:9080/datagenerator/upload?domainType=SupplyChain";
	private static RestTemplate restTemplate = new RestTemplate();
	private static HttpHeaders headers = new HttpHeaders();
	private static ObjectMapper mapper = new ObjectMapper();
	public static List<LinkedHashMap<String, LinkedHashMap<String, String>>> tableMap = null;
	private static List<GenerateDataObject> tableList = null;

	
	public void generateData () {
		MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
		try {
			bodyMap.add("file", getUserFileResource());
			headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
			headers.setContentType(MediaType.MULTIPART_FORM_DATA);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(bodyMap, headers);
			ResponseEntity<String> json = restTemplate.exchange(URI, HttpMethod.POST, requestEntity, String.class);
			System.out.println(json.getBody());
			tableMap = mapper.readValue(json.getBody(),
					new TypeReference<List<LinkedHashMap<String, LinkedHashMap<String, String>>>>() {
					});
			System.out.println("tableMap ::::" + tableMap);
		
		
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> jsonList = restTemplate.exchange(FKURI, HttpMethod.GET, entity, String.class);
		 tableList = mapper.readValue(jsonList.getBody(),
				new TypeReference<List<GenerateDataObject>>() {
				});
		System.out.println("size "+tableList.size());
		System.out.println(tableList);
		threadService(tableList);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void threadService(List<GenerateDataObject> tableList) {

		CountDownLatch countDownLatch = new CountDownLatch(tableList.size());
		try {
			List<Thread> workers = new ArrayList<>();
			for (GenerateDataObject generateDataObject : tableList) {
				Thread worker = new Thread(new Worker(generateDataObject, countDownLatch));
				workers.add(worker);
			}
			workers.forEach(Thread::start);
			countDownLatch.await();
			System.out.println("Count down latch released");
			} catch (InterruptedException e) {
				e.printStackTrace();
		}

	}

	

	public static Resource getUserFileResource() throws IOException {

		return new ClassPathResource("mysqlsampledatabase.sql");
	}

}
