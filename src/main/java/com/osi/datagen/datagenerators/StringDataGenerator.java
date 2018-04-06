package com.osi.datagen.datagenerators;

import static com.osi.datagen.datageneration.service.DataGenUtil.singleQuote;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import com.osi.datagen.constant.DasConstants;
import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.datageneration.service.IUniqueDataGenerator;
import com.osi.datagen.domain.Field;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class StringDataGenerator implements IDataGenerator, IUniqueDataGenerator {
  public static Map<String, List<String>> map = new HashMap<>();
  private int begin = 1000;
  private String countryCode;
  
  public StringDataGenerator(String domainType, String preferredLocale) {
    try {
      Resource resource = new ClassPathResource("datasets//" + domainType);
      resource.getFile().getPath();
      File[] files = new File(resource.getFile().getPath()).listFiles();
      for (File file : files) {
        if (!file.isDirectory()) {
          List<String> lines = FileUtils.readLines(file, "utf-8");
           map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
        }
      }
      String[] locale = preferredLocale.split("-");
      getLocaleData(locale[1], domainType);
      setCountryCode(locale[1]);
      }
    catch (Exception e) {
     log.error("error while reading data sets",e);
     throw new RuntimeException("error while reading data sets");
     
    }
  }
  public StringDataGenerator(String domainType) {
  }
  
  @Override
  public String generateData(Field field) {

    return generateRandomDataFromDataSets(field);
  }

  public String generateUniqueData(Field field) {
    begin++;
    return singleQuote(field.getColumnName().substring(0, 4) + begin);
  }

  private String generateRandomDataFromDataSets(Field field) {
    String columnName = field.getMappedCategory();
    List<String> data = new ArrayList<>();
    List<String> societyList = map.get("societyname");
	List<String> streetList = map.get("street");
	List<String> maleNames = map.get("mempfname");
	List<String> femaleNames = map.get("fempfname");
	List<String> lastNames = map.get("emplname");
    if(columnName.equalsIgnoreCase("Address")) {
    		for(String society :  societyList) {
    			for(String street : streetList) {
    				data.add( Math.random() + society + street);
    			}
    		}
    } else if(columnName.equalsIgnoreCase("AddressLine1")) {
    	int randomPrefix = 1000;
    		for(String society : societyList) {
    			data.add( randomPrefix++ + society);
    		}
    } else if(columnName.equalsIgnoreCase("AddressLine2")) {
    		for(String street : streetList) {
    			data.add(street);
    		}
    } else if(columnName.equalsIgnoreCase("Name"))  {
    	    for(String mfname : maleNames) {
    	    	for(String lname : lastNames) {
    	    		data.add(mfname +" "+ lname);
    	    	}
    	    }
    	    for(String ffname : femaleNames) {
    	    	for(String lname : lastNames) {
    	    		data.add(ffname +" "+ lname);
    	    	}
    	    }
    }
    else {
     data = map.get(columnName.toLowerCase());
    }
    if (data != null) {
    	return singleQuote(data.get(new Random().nextInt(data.size())));
    }  else if (columnName.toLowerCase().contains("mobile") || 
    		columnName.toLowerCase().contains("phone")) {
    	return Integer.parseInt(field.getLength()) >=14 ? singleQuote("+" + this.countryCode + " " + generateMobileNumber(10)) :
    		singleQuote(generateMobileNumber(Integer.parseInt(field.getLength())).toString());
    }
    return randomString(4, columnName);
  }
  
  public Long generateMobileNumber(int count) {
	  StringBuilder number = new StringBuilder();
	  while (count-- != 0) {
		  int singleDigit = (int) (Math.random() * 10);
		  number.append(singleDigit);
	  }
	  return Long.parseLong(number.toString());
  }

  public String randomString(int count, String columnName) {
	Random rand = new Random();
    StringBuilder builder = new StringBuilder();
    while (count-- != 0) {
      builder.append(rand.nextInt(10));
    }
    return singleQuote(columnName+builder);
  }
  
  private void getLocaleData(String preferredLocale, String domainType) {
    Resource resource =
       new ClassPathResource(DasConstants.DATASETS_PATH.concat(domainType).concat("//").concat(preferredLocale));
    try {
      resource.getFile().getPath();

      File[] files = new File(resource.getFile().getPath()).listFiles();

     for (File file : files) {
        if (!file.isDirectory()) {
         List<String> lines = FileUtils.readLines(file, DasConstants.DATASETS_CHARACTERSET);
          map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
        }
      }
      getCommonData(preferredLocale);
    } catch (IOException e) {
      log.error("error while reading local data sets",e);
      throw new RuntimeException("error while reading local data sets");
    }
  }
  
  public void setCountryCode(String locale) {

	    Resource resource =
	        new ClassPathResource(DasConstants.COUNTRY_CODES_DATASETS_PATH);
	    try {
	      File file = resource.getFile();
	      List<String> countryCodes = FileUtils.readLines(file, DasConstants.DATASETS_CHARACTERSET);
	      for (String countryCode : countryCodes) {
	        if (countryCode.split(" ")[0].equals(locale)) {
	          this.countryCode = countryCode.split(" ")[1];
	          break;
	        }
	      }
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	  }

	private void getCommonData(String preferredLocale) {
		Resource resource = new ClassPathResource(
				DasConstants.DATASETS_PATH + DasConstants.DATASETS_COMMONS + "//" + preferredLocale);
		try {
			resource.getFile().getPath();

			File[] files = new File(resource.getFile().getPath()).listFiles();

			for (File file : files) {
				if (!file.isDirectory()) {
					List<String> lines = FileUtils.readLines(file, DasConstants.DATASETS_CHARACTERSET);
					map.put(file.getName().split("\\.")[0].toLowerCase(), lines);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
