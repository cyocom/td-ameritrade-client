package com.studerw.tda.model.market;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import java.util.Date;

@JsonTypeInfo(use = Id.NONE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class Market {

  @JsonProperty("category")
  private String category;

  @JsonProperty("date")
  private Date date;

  @JsonProperty("exchange")
  private String exchange;

  @JsonProperty("isOpen")
  private Boolean isOpen;

  @JsonProperty("marketType")
  private MarketType marketType;

  @JsonProperty("product")
  private String product;

  @JsonProperty("productName")
  private String productName;

  @JsonProperty("sessionHours")
  private SessionHours sessionHours;

  public String getCategory() {
    return category;
  }

  public void setCategory(String category) {
    this.category = category;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchange) {
    this.exchange = exchange;
  }

  public Boolean isOpen() {
    return isOpen;
  }

  public void setIsOpen(Boolean open) {
    isOpen = open;
  }

  public MarketType getMarketType() {
    return marketType;
  }

  public void setMarketType(MarketType marketType) {
    this.marketType = marketType;
  }

  public String getProduct() {
    return product;
  }

  public void setProduct(String product) {
    this.product = product;
  }

  public String getProductName() {
    return productName;
  }

  public void setProductName(String productName) {
    this.productName = productName;
  }

  public SessionHours getSessionHours() {
    return sessionHours;
  }

  public void setSessionHours(SessionHours sessionHours) {
    this.sessionHours = sessionHours;
  }

  @Override
  public String toString() {
    return "Market{" +
        "category='" + category + '\'' +
        ", date=" + date +
        ", exchange='" + exchange + '\'' +
        ", isOpen=" + isOpen +
        ", marketType=" + marketType +
        ", product='" + product + '\'' +
        ", productName='" + productName + '\'' +
        ", sessionHours=" + sessionHours +
        '}';
  }
}
