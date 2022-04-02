package com.thenullproject.webhooktrader.service;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.domain.account.NewOrder;
import com.binance.api.client.domain.account.NewOrderResponseType;
import com.binance.api.client.exception.BinanceApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.BiFunction;

@Service
public class OrderService {

    @Value("${api.key}")
    private String key;

    @Value("${api.secret}")
    private String secret;

    Logger log = LoggerFactory.getLogger(OrderService.class);

    private BinanceApiRestClient client;

    @PostConstruct
    public void init() {
        var factory = BinanceApiClientFactory.newInstance(key, secret);
        client = factory.newRestClient();
    }

    public String marketOrder(char type, String quantity, String symbol) {
        if(!List.of('b', 's').contains(Character.toLowerCase(type)))
            return "invalid order type " + type;

        BiFunction<String, String, NewOrder> order = type == 'b' ? NewOrder::marketBuy : NewOrder::marketSell;

        log.info("\ncreating {} market order | {} - {} ", type == 'b' ? "buy" : "sell", symbol, quantity);

        try {
            var orderResponse = client.newOrder(order.apply(symbol, quantity).newOrderRespType(NewOrderResponseType.FULL));

            log.info("\nstatus: {}", orderResponse.getStatus());
            log.info("\norder id: {}", orderResponse.getOrderId());

        } catch(BinanceApiException e) {
            return String.format("BinanceApiException { %s }", e.getLocalizedMessage());
        }

        return "successful";

    }

}
