package com.datagenerator.demo.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.datagenerator.demo.domain.GenerateDataObject;
import com.datagenerator.demo.serviceImpl.MapToListTransformerService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TableStructureExtractorTest {
	
	@InjectMocks
	private TableStructureExtractor tableStructureExtractor;
	
	@Mock
	private MapToListTransformerService  mapToListTransformerService;
	
	@Test
	public void shouldCreateParentChildMapping() throws FileNotFoundException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("mysql-test.sql").getFile());
		List<GenerateDataObject> derivedList = tableStructureExtractor.getFKMap(file);
		assertEquals("offices", derivedList.get(0).getTableName());
		assertEquals("employees", derivedList.get(0).getChildTableName().get(0).getTableName());
		assertEquals("productlines", derivedList.get(1).getTableName());
		assertEquals("products", derivedList.get(1).getChildTableName().get(0).getTableName());
	}
	
	@Test
	public void shouldNotCreateParentChildMappingIfCyclicDepenpendent() throws FileNotFoundException {
		
		ClassLoader classLoader = getClass().getClassLoader();
		File file = new File(classLoader.getResource("my-sql-test-2.sql").getFile());
		List<GenerateDataObject> derivedList = tableStructureExtractor.getFKMap(file);
		assertNotEquals("offices", derivedList.get(0).getTableName());
		assertEquals("productlines", derivedList.get(0).getTableName());
		assertEquals("products", derivedList.get(0).getChildTableName().get(0).getTableName());
	}
	
}
