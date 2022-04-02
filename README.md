## Binance Webhook Trader 📈
This is a very simple Spring Boot API to recieve webhook notifications for buy and sell orders. The application will start by validating a passphrase in the request body and then proceed to place a market order using the Binance API.

I use this in conjunction with a custom PineScript strategy with webhook alerts inside TradingView. You can read more about that [here](https://www.tradingview.com/support/solutions/43000481368-strategy-alerts/).

Make sure to provide values for the three properties inside `application.properties`:

```properties
webhook.passphrase=${WEBHOOK_PASSPHRASE}
api.key=${BINANCE_API_KEY}
api.secret=${BINANCE_API_SECRET}
```

> You should generate a long and random string to use as the webhook passphrase.

Happy trading!
