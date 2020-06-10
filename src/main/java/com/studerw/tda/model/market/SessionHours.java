package com.studerw.tda.model.market;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SessionHours {

  @JsonProperty("preMarket")
  private List<MarketHours> preMarket;

  @JsonProperty("regularMarket")
  private List<MarketHours> regularMarket;

  @JsonProperty("postMarket")
  private List<MarketHours> postMarket;

  public List<MarketHours> getPreMarket() {
    return preMarket;
  }

  public void setPreMarket(List<MarketHours> preMarket) {
    this.preMarket = preMarket;
  }

  public List<MarketHours> getRegularMarket() {
    return regularMarket;
  }

  public void setRegularMarket(List<MarketHours> regularMarket) {
    this.regularMarket = regularMarket;
  }

  public List<MarketHours> getPostMarket() {
    return postMarket;
  }

  public void setPostMarket(List<MarketHours> postMarket) {
    this.postMarket = postMarket;
  }

  @Override
  public String toString() {
    return "SessionHours{" +
        "preMarket=" + preMarket +
        ", regularMarket=" + regularMarket +
        ", postMarket=" + postMarket +
        '}';
  }
}
