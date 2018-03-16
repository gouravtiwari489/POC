package com.osi.datagen.domain;

import java.io.Serializable;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document
public class Domain implements Serializable {

  private static final long serialVersionUID = 1L;
  @Id String domainId;
  List<Object> tables;
}
