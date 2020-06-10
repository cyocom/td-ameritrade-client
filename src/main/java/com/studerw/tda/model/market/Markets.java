package com.studerw.tda.model.market;


import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
@JsonSubTypes({
    @JsonSubTypes.Type(value = Markets.class, name = "equity"),
    @JsonSubTypes.Type(value = Markets.class, name = "bond"),
    @JsonSubTypes.Type(value = Markets.class, name = "future"),
    @JsonSubTypes.Type(value = Markets.class, name = "option"),
    @JsonSubTypes.Type(value = Markets.class, name = "forex")
})
public class Markets {

  @JsonAnySetter
  private Map<String, Object> otherFields = new HashMap<>();

  public Map<String, Object> getOtherFields() {
    return otherFields;
  }

  public void setOtherFields(Map<String, Object> otherFields) {
    this.otherFields = otherFields;
  }

  public Market getMarket(String market){
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.convertValue(otherFields.get(market), Market.class);
  }

  @Override
  public String toString() {
    return "Markets{" +
        "otherFields=" + otherFields +
        '}';
  }
}
