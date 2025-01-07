package book;

import exceptions.DataValidationException;
import price.exceptions.InvalidPriceOperation;
import price.Price;
import pubsub.CurrentMarketTracker;
import tradable.Quote;
import tradable.Tradable;
import tradable.TradableDTO;
import user.UserManager;

import static book.GlobalConstants.BookSide;

public class ProductBook {
    private final String product;
    private final ProductBookSide buySide;
    private final ProductBookSide sellSide;

    public ProductBook (String product) {
        this.product = setProduct(product);
        this.buySide = new ProductBookSide(BookSide.BUY);
        this.sellSide = new ProductBookSide(BookSide.SELL);
    }

    private String setProduct(String product) {
        if (product == null || !product.matches("[A-Z0-9.]{1,5}")) {
            throw new IllegalArgumentException("Invalid product symbol.");
        }
        return product;
    }

    public TradableDTO add(Tradable t) throws DataValidationException, InvalidPriceOperation {
        TradableDTO dto;
        if (t.getSide() == BookSide.BUY) {
            dto = buySide.add(t);
        } else {
            dto = sellSide.add(t);
        }
        tryTrade();
        updateMarket();
        return dto;
    }

    public TradableDTO[] add (Quote qte) throws DataValidationException {
        TradableDTO buyDTO = buySide.add(qte.getQuoteSide(BookSide.BUY));
        TradableDTO sellDTO = sellSide.add(qte.getQuoteSide(BookSide.SELL));
        tryTrade();
        return new TradableDTO[] {buyDTO, sellDTO};
    }

    public TradableDTO cancel(BookSide side, String orderId) throws DataValidationException, InvalidPriceOperation {
        TradableDTO dto;
        if (side == BookSide.BUY) {
            dto = buySide.cancel(orderId);
        } else {
            dto = sellSide.cancel(orderId);
        }
        updateMarket();
        return dto;
    }

    public void tryTrade() throws DataValidationException {
        Price topBuyPrice = buySide.topOfBookPrice();
        Price topSellPrice = sellSide.topOfBookPrice();
        while (topBuyPrice != null && topSellPrice != null && topBuyPrice.compareTo(topSellPrice) >= 0) {
            int topBuyVolume = buySide.topOfBookVolume();
            int topSellVolume = sellSide.topOfBookVolume();
            int volumeToTrade = Math.min(topBuyVolume, topSellVolume);

            sellSide.tradeOut(topSellPrice, volumeToTrade);
            buySide.tradeOut(topBuyPrice, volumeToTrade);

            topBuyPrice = buySide.topOfBookPrice();
            topSellPrice = sellSide.topOfBookPrice();
        }
    }

    public TradableDTO[] removeQuotesForUser(String userName) throws DataValidationException, InvalidPriceOperation {
        TradableDTO buyDTO = buySide.removeQuotesForUser(userName);
        if (buyDTO != null) {
            UserManager.getInstance().getUser(buyDTO.user).addTradable(buyDTO);
        }
        TradableDTO sellDTO = sellSide.removeQuotesForUser(userName);
        if (sellDTO != null) {
            UserManager.getInstance().getUser(sellDTO.user).addTradable(sellDTO);
        }
        updateMarket();
        return new TradableDTO[] {buyDTO, sellDTO};
    }

    private void updateMarket() throws InvalidPriceOperation {
        Price topBuyPrice = buySide.topOfBookPrice();
        int topBuyVolume = buySide.topOfBookVolume();
        Price topSellPrice = sellSide.topOfBookPrice();
        int topSellVolume = sellSide.topOfBookVolume();

        CurrentMarketTracker.getInstance().updateMarket(product, topBuyPrice, topBuyVolume, topSellPrice, topSellVolume);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("--------------------------------------------\nProduct: ");
        sb.append(product).append("\n");
        sb.append(buySide).append(sellSide);
        sb.append("--------------------------------------------");
        return sb.toString();
    }

}
