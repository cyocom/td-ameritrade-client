package com.studerw.tda.model.market;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MarketHours {

  @JsonProperty("start")
  private Date start;

  @JsonProperty("end")
  private Date end;

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public Date getEnd() {
    return end;
  }

  public void setEnd(Date end) {
    this.end = end;
  }

  @Override
  public String toString() {
    return "MarketHours{" +
        "start=" + start +
        ", end=" + end +
        '}';
  }
}
