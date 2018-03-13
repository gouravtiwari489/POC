package com.osi.dgen.datagen;

import static com.osi.dgen.utils.DataGenUtil.singleQuote;

import java.text.ParseException;
import java.util.Random;

import org.springframework.security.crypto.codec.Base64;

import com.osi.dgen.datagen.intf.IDataGenerator;
import com.osi.dgen.domain.Field;

public class ByteDataGenerator implements IDataGenerator {

  @Override
  public String generateData(Field field) throws ParseException {
    byte[] b = new byte[20];
    new Random().nextBytes(b);
    return singleQuote((new String(Base64.encode(b))));
  }
}
