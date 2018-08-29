package com.osi.datagen.domain;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Domain implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id String domainId;
  List<Object> tables;
}
