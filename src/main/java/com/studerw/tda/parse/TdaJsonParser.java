package com.studerw.tda.parse;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studerw.tda.model.account.Order;
import com.studerw.tda.model.account.SecuritiesAccount;
import com.studerw.tda.model.history.PriceHistory;
import com.studerw.tda.model.instrument.FullInstrument;
import com.studerw.tda.model.instrument.Instrument;
import com.studerw.tda.model.marketdata.Mover;
import com.studerw.tda.model.option.OptionChain;
import com.studerw.tda.model.quote.Quote;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TdaJsonParser {

  private static final Logger LOGGER = LoggerFactory.getLogger(TdaJsonParser.class);

  /**
   * @param in inputstream of JSON from rest call to TDA. The stream will be closed upon return.
   * @return list of objects that extend Quote.
   */
  public List<Quote> parseQuotes(InputStream in) {
    LOGGER.trace("parsing quotes...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      LinkedHashMap<String, Quote> quotesMap = DefaultMapper.fromJson(bIn, new TypeReference<LinkedHashMap<String, Quote>>() {});
      LOGGER.debug("returned a map of size: {}", quotesMap.size());

      List<Quote> quotes = new ArrayList<>();
      quotesMap.forEach((k, v) -> quotes.add(v));
      return quotes;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @param in {@link InputStream} of JSON from TDA; the stream will be closed upon return.
   * @return PriceHistory
   */
  public PriceHistory parsePriceHistory(InputStream in) {
    LOGGER.trace("parsing quotes...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final PriceHistory priceHistory = DefaultMapper.fromJson(bIn, PriceHistory.class);
      LOGGER.debug("returned a price history for {} of size: {}", priceHistory.getSymbol(),
          priceHistory.getCandles().size());
      return priceHistory;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @param in {@link InputStream} of JSON from TDA; the stream will be closed upon return.
   * @return SecuritiesAccount
   */
  public SecuritiesAccount parseAccount(InputStream in) {
    LOGGER.trace("parsing securitiesAccount...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      //Cannot use defaultMapper because account is wrapped in '{ securitiesAccount: {...} }'
      final ObjectMapper objMapper = new ObjectMapper();
      objMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

      final SecuritiesAccount securitiesAccount = objMapper.readValue(bIn, SecuritiesAccount.class);
      LOGGER.debug("returned a securitiesAccount of type: {}",
          securitiesAccount.getClass().getName());
      return securitiesAccount;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   * @param in {@link InputStream} of JSON from TDA; the stream will be closed upon return.
   * @return List of SecuritiesAccounts
   */
  public List<SecuritiesAccount> parseAccounts(InputStream in) {
    LOGGER.trace("parsing securitiesAccounts...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {

      TypeReference<List<Map<String, SecuritiesAccount>>> typeReference = new TypeReference<List<Map<String, SecuritiesAccount>>>() {
      };
      ObjectMapper mapper = new ObjectMapper();
      List<SecuritiesAccount> accounts = new ArrayList<>();
      List<Map<String, SecuritiesAccount>> maps = mapper.readValue(bIn, typeReference);
      for (Map<String, SecuritiesAccount> map : maps) {
        if (map.size() != 1 && map.containsKey("securitiesAccount")) {
          throw new IllegalStateException("Expecting of json list of securitiesAccount");
        }
        SecuritiesAccount securitiesAccount = map.get("securitiesAccount");
        accounts.add(securitiesAccount);
      }

      LOGGER.debug("returned a a list of {} securitiesAccounts: {}", accounts.size(), accounts);
      return accounts;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public Order parseOrder(InputStream in) {
    LOGGER.trace("parsing order...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      final Order order = DefaultMapper.fromJson(bIn, Order.class);
      LOGGER.debug("Returned order of id: {}", order.getOrderId());
      return order;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public List<Order> parseOrders(InputStream in) {
    LOGGER.trace("parsing orders...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      TypeReference<List<Order>> typeReference = new TypeReference<List<Order>>() {};
      final List<Order> orders= DefaultMapper.fromJson(bIn, typeReference);
      LOGGER.debug("Returned list of orders of size: {}", orders.size());
      return orders;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param in of json that is either an array of instruments or a map keyed by symbol
   * @return a single parsed Instrument from the list
   */
  public Instrument parseInstrumentArraySingle(InputStream in) {
    LOGGER.trace("parsing instrument array...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      TypeReference<List<Instrument>> typeReference = new TypeReference<List<Instrument>>() {};
      List<Instrument> instruments = DefaultMapper.fromJson(bIn, typeReference);
      if (instruments.size() != 1) {
        throw new RuntimeException("Excepting a json array of Instruments from TDA");
      }
      Instrument instrument = instruments.get(0);
      LOGGER.debug("Returned instrument: {}", instrument);
      return instrument;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param in of json that is either an array of instruments or a map keyed by symbol
   * @return list of instruments
   */
  public List<Instrument> parseInstrumentMap(InputStream in) {
    LOGGER.trace("parsing instrument map...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      TypeReference<Map<String, Instrument>> typeReference = new TypeReference<Map<String, Instrument>>() {};
      Map<String, Instrument> instruments = DefaultMapper.fromJson(bIn, typeReference);
      LOGGER.debug("Returned instruments map of size: {}", instruments.size());
      return new ArrayList(instruments.values());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  /**
   *
   * @param in of json that is either an array of instruments or a map keyed by symbol
   * @return list of full instruments
   */
  public List<FullInstrument> parseFullInstrumentMap(InputStream in) {
    LOGGER.trace("parsing full instrument map...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      TypeReference<Map<String, FullInstrument>> typeReference = new TypeReference<Map<String, FullInstrument>>() {};
      Map<String, FullInstrument> instruments = DefaultMapper.fromJson(bIn, typeReference);
      LOGGER.debug("Returned full instruments map of size: {}", instruments.size());
      return new ArrayList(instruments.values());
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public List<Mover> parseMovers(InputStream in) {
    LOGGER.trace("parsing movers...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      TypeReference<List<Mover>> typeReference = new TypeReference<List<Mover>>() {};
      final List<Mover> movers = DefaultMapper.fromJson(bIn, typeReference);
      LOGGER.debug("Returned list of movers of size: {}", movers.size());
      return movers;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public OptionChain parseOptionChain(InputStream in) {
    LOGGER.trace("parsing option chain...");
    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
      TypeReference<OptionChain> typeReference = new TypeReference<OptionChain>() {};
      final OptionChain optionChain = DefaultMapper.fromJson(bIn, typeReference);
      LOGGER.debug("Returned optionChain: {}", optionChain);
      return optionChain;
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

//  public <T> T parseTdaJson(InputStream in, Class<T> type){
//    try (BufferedInputStream bIn = new BufferedInputStream(in)) {
//      LOGGER.debug("parsing JSON input to type: {}", type.getName());
//      final T tdaPojo = DefaultMapper.fromJson(in, new TypeReference<T>(){});
//      return tdaPojo;
//    } catch (IOException e) {
//      e.printStackTrace();
//      throw new RuntimeException(e);
//    }
//  }
}
