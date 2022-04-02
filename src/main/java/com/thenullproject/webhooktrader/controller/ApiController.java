package com.thenullproject.webhooktrader.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.thenullproject.webhooktrader.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ApiController {

    Logger log = LoggerFactory.getLogger(ApiController.class);

    @Value("${webhook.passphrase}")
    private String passphrase;

    @Autowired
    private OrderService orderService;

    @PostMapping(path = "/webhook", consumes="application/json", produces = "application/json")
    public void webhook(@RequestBody JsonNode json) {

        if(!json.get("passphrase").asText().equals(passphrase)) {
            log.error("request with invalid passphrase " + json.get("passphrase").asText()); // return "{ \"status\": \"invalid passphrase\" }";
            return;
        }

        char type = json.get("strategy").get("order_action").asText().charAt(0);
        String quantity = json.get("strategy").get("order_contracts").asText();
        String symbol = json.get("ticker").asText();

        String message = orderService.marketOrder(type, quantity, symbol);

        log.info(message);
        // return String.format("{ \"status\": \"%s\" }", message);
    }
}
