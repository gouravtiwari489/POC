package com.osi.datagen.datagenerators;

import static com.osi.datagen.datageneration.service.DataGenUtil.singleQuote;

import com.osi.datagen.datageneration.service.IDataGenerator;
import com.osi.datagen.domain.Field;
import java.text.ParseException;
import java.util.Random;
import org.springframework.security.crypto.codec.Base64;

public class ByteDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) throws ParseException {
    byte[] b = new byte[20];
    new Random().nextBytes(b);
    return singleQuote((new String(Base64.encode(b))));
  }
}
