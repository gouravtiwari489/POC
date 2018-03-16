package com.osi.datagen.datagen;

import static com.osi.datagen.utils.DataGenUtil.singleQuote;
import java.text.ParseException;
import java.util.Random;

import org.springframework.security.crypto.codec.Base64;
import com.osi.datagen.datagen.intf.IDataGenerator;
import com.osi.datagen.domain.Field;

public class ByteDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) throws ParseException {
    byte[] b = new byte[20];
    new Random().nextBytes(b);
    return singleQuote((new String(Base64.encode(b))));
  }
}
