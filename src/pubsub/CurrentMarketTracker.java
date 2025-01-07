package pubsub;
import price.Price;
import price.exceptions.InvalidPriceOperation;

public class CurrentMarketTracker {
    private static CurrentMarketTracker instance;

    private CurrentMarketTracker () {}

    public static CurrentMarketTracker getInstance() {
        if (instance == null) {
            instance = new CurrentMarketTracker();
        }
        return instance;
    }

    public void updateMarket(String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume) throws InvalidPriceOperation {
        String marketWidth = "$0.00";

        if (buyPrice != null && sellPrice != null) {
            Price widthPrice = (sellPrice.subtract(buyPrice));
            marketWidth = widthPrice.toString();
        }

        CurrentMarketSide buySide = new CurrentMarketSide(buyPrice, buyVolume);
        CurrentMarketSide sellSide = new CurrentMarketSide(sellPrice, sellVolume);
        System.out.println("*********** Current Market ***********");
        System.out.println("* " + symbol + "   " + buySide + " - " + sellSide + " [" + marketWidth + "]");
        System.out.println("**************************************");

        CurrentMarketPublisher.getInstance().acceptCurrentMarket(symbol, buySide, sellSide);
    }
}
