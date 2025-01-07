package tradable;

import static book.GlobalConstants.BookSide;
import exceptions.BadValueException;
import exceptions.NullParamException;
import price.Price;

public class Quote {
    private final String user;
    private final String product;
    private final QuoteSide buySide;
    private final QuoteSide sellSide;

    public Quote (String symbol, Price buyPrice, int buyVolume, Price sellPrice, int sellVolume, String userName)
            throws BadValueException, NullParamException {
        this.user = setUser(userName);
        this.product = setProduct(symbol);
        this.buySide = new QuoteSide(userName, symbol, buyPrice, buyVolume, BookSide.BUY);
        this.sellSide = new QuoteSide(userName, symbol, sellPrice, sellVolume, BookSide.SELL);
    }

    private String setUser(String user) throws NullParamException, BadValueException {
        if (user == null) {
            throw new NullParamException("User is null!");
        }
        if (!user.matches("[A-Z]{3}")) {
            throw new BadValueException("User must be a 3-letter user code!");
        }
        return user;
    }

    private String setProduct (String product) throws NullParamException, BadValueException {
        if (product == null) {
            throw new NullParamException("Product is null!");
        }
        if (!product.matches("[A-Z0-9.]{1,5}")) {
            throw new BadValueException("Product must follow stock symbol pattern!");
        }
        return product;
    }

    public QuoteSide getQuoteSide(BookSide sideIn) {
        if (sideIn == BookSide.BUY) {
            return buySide;
        } else {
            return sellSide;
        }
    }

    public String getSymbol() {
        return product;
    }

    public String getUser() {
        return user;
    }
}
