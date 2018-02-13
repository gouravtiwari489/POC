package com.datagenerator.demo.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.datagenerator.demo.domain.GenerateDataObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.datagenerator.demo.utils.Worker;

public class GenerateDataThreadsDemo {

	private static final String URI = "http://localhost:9080/datagenerator/getFKRelation";
	private static RestTemplate restTemplate = new RestTemplate();
	private static HttpHeaders headers = new HttpHeaders();
	private static ObjectMapper mapper = new ObjectMapper();

	public static void main(String argc[]) throws Exception {

		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> entity = new HttpEntity<String>("parameters", headers);
		ResponseEntity<String> json = restTemplate.exchange(URI, HttpMethod.GET, entity, String.class);
		List<GenerateDataObject> tableList = mapper.readValue(json.getBody(),
				new TypeReference<List<GenerateDataObject>>() {
				});
		System.out.println("size " + tableList.size());
		System.out.println(tableList);
		threadService(tableList);
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

}
