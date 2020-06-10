package com.studerw.tda.client;

import com.studerw.tda.model.account.Order;
import com.studerw.tda.model.account.OrderRequest;
import com.studerw.tda.model.account.SecuritiesAccount;
import com.studerw.tda.model.history.PriceHistReq;
import com.studerw.tda.model.history.PriceHistory;
import com.studerw.tda.model.instrument.FullInstrument;
import com.studerw.tda.model.instrument.Instrument;
import com.studerw.tda.model.instrument.Query;
import com.studerw.tda.model.marketdata.Mover;
import com.studerw.tda.model.marketdata.MoversReq;
import com.studerw.tda.model.option.OptionChain;
import com.studerw.tda.model.market.MarketType;
import com.studerw.tda.model.market.Markets;
import com.studerw.tda.model.quote.Quote;
import com.studerw.tda.model.transaction.Transaction;
import com.studerw.tda.model.transaction.TransactionRequest;
import java.util.Date;
import java.util.List;

/**
 * Main interface of the TDA Client. Implementations should be thread safe.
 * @see HttpTdaClient
 */
public interface TdaClient {

  /**
   * <p>
   * Retrieve historical intraday and end of day quote data for an equity, index, mutual fund, forex, option chain, etc. See {@link com.studerw.tda.model.AssetType}
   * for possible types, though your account must explicitly have access for some of these. TDA has not implemented all the API calls either (Sep 2019).
   * </p>
   *
   * <ul>
   *   <li>periodType: day</li>
   *   <li>period: 10</li>
   *   <li>frequencyType: minute</li>
   *   <li>frequency: 1</li>
   * </ul>
   *
   * @param symbol uppercase symbol
   * @return PriceHistory using all other TDA default request parameters. This appears to be a quote every minute for 10 days.
   **/
    PriceHistory priceHistory(String symbol);

  /**
   *
   * <p>
   * Retrieve historical intraday and end of day quote data for an equity, index, mutual fund, forex, option chain, etc. See {@link com.studerw.tda.model.AssetType}
   * for possibly types, though your account must explicitly have access for some of these. TDA has not implemented all the API calls either (Sep 2019).
   * Note that some of the parameters within the {@link com.studerw.tda.model.history.PriceHistReq} param be null, and then
   * some of the other arguments will be assumed by the non null parameters.
   * </p>
   *
   * @param priceHistReq validated object of request parameters
   * @return PriceHistory with a list of {@link com.studerw.tda.model.history.Candle} Candles based on the frequency and period / date length.
   */
  PriceHistory priceHistory(PriceHistReq priceHistReq);


  /**
   * <p>
   * Fetch Detailed quote information for one or more symbols. Currently the API allows symbol types
   * of Stocks, Options, Mutual Funds and Indexes, and ETFs. Quotes are real-time for accounts subscribed to
   * this service; otherwise, quotes are delayed according to exchange and TDA rules. The following
   * types of <em>Quote</em> are actually returned and can be casted:
   *</p>
   *
   * <ul>
   *   <li>{@link com.studerw.tda.model.quote.EquityQuote}</li>
   *   <li>{@link com.studerw.tda.model.quote.EtfQuote}</li>
   *   <li>{@link com.studerw.tda.model.quote.OptionQuote}</li>
   *   <li>{@link com.studerw.tda.model.quote.IndexQuote}</li>
   *   <li>{@link com.studerw.tda.model.quote.MutualFundQuote}</li>
   *   <li>{@link com.studerw.tda.model.quote.ForexQuote}</li>
   * </ul>
   *
   * <pre class="code">
   *  Quote quote = client.fetchQuote("ATD");
   *  EquityQuote equityQuote = (EquityQuote)quote;
   * </pre>
   *
   * @param symbols list of valid symbols. Max of 300 based on TDA docs. Index symbols need to be
   * prefixed with a <em>$</em>, e.g. <em>$INX</em> or <em>$SPX.X</em>. Options are in a format like the following:
   * <em>MSFT_061518P60</em> for a put, or <em>MSFT_061518C60</em> for a call. This is the
   * Microsoft June 6, 2018 Put/Call at $60.
   * @return list of quotes. The {@link Quote} is the base class, but all objects in the
   * list can be cast to their actual types by looking at the {@link com.studerw.tda.model.AssetType} attribute.
   * field.
   */
  List<Quote> fetchQuotes(List<String> symbols);


  /**
   * <p>
   * Fetch Detailed quote information for one or more symbols. Currently the API allows symbol types
   * of Stocks, Options, Mutual Funds and Indexes, and ETFs. Quotes are real-time for accounts subscribed to
   * this service; otherwise, quotes are delayed according to exchange and TDA rules.
   * </p>
   *
   * <pre class="code">
   *  Quote quote = client.fetchQuote("ATD");
   *  EquityQuote equityQuote = (EquityQuote)quote;
   * </pre>
   *
   *<ul>
   *  <li>{@link com.studerw.tda.model.quote.EquityQuote}</li>
   *  <li>{@link com.studerw.tda.model.quote.EtfQuote}</li>
   *  <li>{@link com.studerw.tda.model.quote.OptionQuote}</li>
   *  <li>{@link com.studerw.tda.model.quote.IndexQuote}</li>
   *  <li>{@link com.studerw.tda.model.quote.MutualFundQuote}</li>
   *  <li>{@link com.studerw.tda.model.quote.ForexQuote}</li>
   *</ul>
   *
   * @param symbol list of valid symbols. Max of 300 based on TDA docs. Index symbols need to be
   * prefixed with a <em>$</em>, e.g. <em>$INX</em> or <em>$SPX.X</em>. Options are in a format like the following:
   * <em>MSFT_061518P60</em> for a put, or <em>MSFT_061518C60</em> for a call. This is the
   * Microsoft June 6, 2018 Put/Call at $60.
   * @return a quote. The {@link Quote} is the base class, but all quotes can be cast to their
   * actual types by looking at the {@code com.studerw.tda.model.quote.Quote.assetType} field.
   */
  Quote fetchQuote(String symbol);

  /**
   * Fetch an account by the id. By default, balances are included. Positions and Orders can
   * also be included based on the parameters.
   * @param accountId the account. Most users only have a single account
   * @param positions whether to include positions
   * @param orders whether to include orders
   * @return {@link SecuritiesAccount} with the passed id.
   */
  SecuritiesAccount getAccount(String accountId, boolean positions, boolean orders);

  /**
   * Fetch all your accounts. By default, balances are included. Positions and Orders can
   * also be included based on the parameters.
   * @param positions whether to include positions
   * @param orders whether to include orders
   * @return List of all the user's {@link SecuritiesAccount}.
   */
  List<SecuritiesAccount> getAccounts(boolean positions, boolean orders);

  /**
   * Provides detailed information on positions and balances in the account.
   * Uses the default account of the user, both balances and positions are returned,
   * quotes will not be suppressed, and balances will not be returned in 'alternative format'.
   * @return {@link com.studerw.tda.model.BalancesAndPositions}
   */
//  BalancesAndPositions fetchBalancesAndPositions();

  /**
   * Provides detailed information on positions and balances in the account.
   * @param accountId of the associated account. If it is empty or null, the default account will be used.
   * All other parameters are set as default.
   * @return {@link com.studerw.tda.model.BalancesAndPositions}
   */
//  BalancesAndPositions fetchBalancesAndPositions(String accountId);

  /**
   * Cancel an open order or the balance of partially filled orders. The default accountId will
   * be used.
   * @param orderIds - one or more order ids
   * @return {@link CancelOrder}
   */
//  CancelOrder cancelTrade(List<String> orderIds);

  /**
   * Cancel an open order or the balance of partially filled orders. The default accountId will
   * be used.
   * @param accountId the id of the account on which the trade was placed
   * @param orderIds - one or more order ids
   * @return {@link CancelOrder}
   */
  void placeOrder(String accountId, Order order);

  /**
   * Partially complete
   *  Detailed order status for an account, allowing filtering of orders by status, entry date,
   *  order id, and other criteria.  OrderStatus is a synchronous request.
   *  If you need an update, you need to issue the request again.
   *
   * @param orderIds one or more order ids.
   * @return {@link com.studerw.tda.model.OrderStatus}
   */
  List<Order> fetchOrders(String accountId, OrderRequest orderRequest);

  /**
   * Partially complete
   *  Detailed order status for an account, allowing filtering of orders by status, entry date,
   *  order id, and other criteria.  OrderStatus is a synchronous request.
   *  If you need an update, you need to issue the request again.
   *
   * @param orderIds one or more order ids.
   * @param accountId the account on which the orders were placed
   * @return {@link com.studerw.tda.model.OrderStatus}
   */
  List<Order> fetchOrders(OrderRequest orderRequest);

  /**
   * Fetch a list of all order statuses
   * @return {@link com.studerw.tda.model.OrderStatus}
   */
  List<Order> fetchOrders();

  /**
   *  Get the date and time of the last order status activity for the primary account associated with the login userID, or the explicitly specified account.
   * @return  {@link com.studerw.tda.model.LastOrderStatus}
   */
  Order fetchOrder(String accountId, Long orderId);

  /**
   * Partially complete....
   * @param symbol in TDA format (e.g. {@code MSFT_061821C120} which denotes MSFT for June 18, 2021 Call at $20.
   * @return {@link com.studerw.tda.model.OptionChain}
   */
  void cancelOrder(String accountId, String orderId);

  /**
   * Provides the ability to lookup symbols for stocks and ETFs.
   *f
   * @param matchStr The string being searched for. Partial name of the company for example <em>Bank
   * of Amer</em> would return <em>Bank of America</em>. Used, perhaps, for auto select fields.
   * @return a LookupResponse
   */
  Instrument getInstrumentByCUSIP(String cusip);

  /**
   *  A snapshot of the five main indices.
   * @return {@link MarketSnapshot} MarketSnapshot
   */
  Instrument getBond(String cusip);

  /**
   * This is just an alias method for {@link TdaClient#fetchMarketOverview()}  due to the way TDA names their services.
   * @return {@link MarketSnapshot} MarketSnapshot
   */
  List<Instrument> queryInstruments(Query query);

  /**
   * Place a Trade.
   * @param accountId the account under which the order is to be placed
   * @param order the order to place
   *
   * @see <a href="https://developer.tdameritrade.com/content/place-order-samples">Place Order Samples</a>
   */
  FullInstrument getFundamentalData(String id);

  /** Note that this call can return an empty list on days the market is closed.
   * Top 10 (up or down) movers by value or percent for a particular market
   *
   * @param moversReq Index must be set, the other fields can be null which will use TDA defaults.
   * @return List of top 10 market movers defined by the request.
   */
  List<Mover> fetchMovers(MoversReq moversReq);

  /**
   * <p>
   * Fetch all orders for a given account using the criteria of the orderRequest.
   * You can just use a blank order to use sane defaults.
   * </p>
   *
   * @param symbol - should be upper case (e.g. <em>MSFT</em>)
   * @return an option chain using all TDA Default parameters
   */
  OptionChain getOptionChain(String symbol);


  /**
   *
   * @param accountId the orders from only this account
   * @param orderRequest the request.
   * @return list of orders specified by the {@link OrderRequest} param.
   */
  List<Transaction> fetchTransactions(String accountId);

  /**
   * Cancel an order by accountId and orderId.
   * @param accountId the orders from only this account
   * @param orderId the order to cancel
   */
  List<Transaction> fetchTransactions(String accountId, TransactionRequest request);

  /**
   *
   * @param accountId the account under which this transactions occurred
   * @param transactionId transaction id
   * @return single transaction
   */
  Transaction getTransaction(String accountId, Long transactionId);

  /**
   * Fetches the Market Hour Data for Markets of the specific Market Type
   * @param date date for which to fetch market hours data for
   * @param marketType type of markets
   */
  Markets fetchMarketHours(Date date, MarketType marketType);

}
