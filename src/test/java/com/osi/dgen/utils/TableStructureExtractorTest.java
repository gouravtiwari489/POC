package com.osi.dgen.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doNothing;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class TableStructureExtractorTest {

  @InjectMocks private TableStructureExtractor tableStructureExtractor;

//  @Mock private CustomTokenConverter customTokenConverter;

  @Test
  public void shouldCreateParentChildMapping() throws Exception {

    ClassLoader classLoader = getClass().getClassLoader();
    File file = new File(classLoader.getResource("mysql-test.sql").getFile());
//    doNothing()
//        .when(customTokenConverter)
//        .setAdditionalInfo("orderedFKList", new HashMap<Integer, List<String>>());

    LinkedHashMap<String, LinkedHashMap<String, String>> derivedList =
        tableStructureExtractor.searchforTableName(file, false);
    assertThat(derivedList).isNotEmpty().hasSize(9);
    assertThat(derivedList).containsKey("d_calendar");
    assertThat(derivedList).containsKey("d_campus");
    assertThat(derivedList).containsKey("d_exam");
  }

  /*@Test
  public void shouldNotCreateParentChildMappingIfCyclicDepenpendent() throws Exception {

  	ClassLoader classLoader = getClass().getClassLoader();
  	File file = new File(classLoader.getResource("my-sql-test-2.sql").getFile());
  	LinkedHashMap<String, LinkedHashMap<String, String>> derivedList = tableStructureExtractor.searchforTableName(file);
  	assertNotEquals("offices", derivedList.get(0).getTableName());
  	assertEquals("productlines", derivedList.get(0).getTableName());
  	assertEquals("products", derivedList.get(0).getChildTableName().get(0).getTableName());
  }*/

}
