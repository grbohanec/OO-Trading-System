package book;

import java.util.Random;
import java.util.HashMap;
import java.util.ArrayList;

import tradable.*;
import book.GlobalConstants.BookSide;
import exceptions.DataValidationException;
import price.exceptions.InvalidPriceOperation;
import user.UserManager;

public class ProductManager {
    private static ProductManager instance;
    private HashMap<String, ProductBook> productBooks;

    private ProductManager() {
        productBooks = new HashMap<>();
    }

    public static ProductManager getInstance() {
        if (instance == null) {
            instance = new ProductManager();
        }
        return instance;
    }

    public void addProduct(String symbol) throws DataValidationException {
        if (symbol == null) {
            throw new DataValidationException("Symbol is null!");
        }

        if (!symbol.matches("[A-Z0-9.]{1,5}")) {
            throw new DataValidationException("Symbol does not match requirements");
        }

        productBooks.put(symbol, new ProductBook(symbol));
    }

    public ProductBook getProductBook(String symbol) throws DataValidationException {
        ProductBook productBook = productBooks.get(symbol);
        if (productBook == null) {
            throw new DataValidationException("Product Book does not exist!");
        }
        return productBook;
    }

    public String getRandomProduct() throws DataValidationException {
        if (productBooks.isEmpty()) {
            throw new DataValidationException("No products exist!");
        }
        Object[] productSymbols = productBooks.keySet().toArray();
        return (String) productSymbols[new Random().nextInt(productSymbols.length)];
    }

    public TradableDTO addTradable(Tradable o) throws DataValidationException, InvalidPriceOperation {
        if (o == null) {
            throw new DataValidationException("Tradable is null!");
        }
        ProductBook productBook = getProductBook(o.getProduct());
        TradableDTO tradableDTO = productBook.add(o);
        UserManager.getInstance().addToUser(o.getUser(), o.makeTradableDTO());
        return tradableDTO;
    }

    public TradableDTO[] addQuote(Quote q) throws DataValidationException, InvalidPriceOperation {
        if (q == null) {
            throw new DataValidationException("Quote is null!");
        }
        ProductBook productBook = getProductBook(q.getSymbol());
        productBook.removeQuotesForUser(q.getUser());
        TradableDTO buyDTO = addTradable(q.getQuoteSide(BookSide.BUY));
        TradableDTO sellDTO = addTradable(q.getQuoteSide(BookSide.SELL));
        return new TradableDTO[]{buyDTO, sellDTO};
    }

    public TradableDTO cancel(TradableDTO o) throws DataValidationException, InvalidPriceOperation {
        if (o == null) {
            throw new DataValidationException("TradableDTO is null!");
        }
        ProductBook productBook = getProductBook(o.product);
        TradableDTO cancelledDTO = productBook.cancel(o.side, o.id);
        if (cancelledDTO == null) {
            System.out.println("Failure to cancel");
            return null;
        }
        return cancelledDTO;
    }

    public TradableDTO[] cancelQuote(String symbol, String user) throws DataValidationException, InvalidPriceOperation {
        if (user == null) {
            throw new DataValidationException("User is null!");
        }

        if (symbol == null) {
            throw new DataValidationException("Symbol is null!");
        }

        ProductBook productBook = getProductBook(symbol);
        if (productBook == null) {
            throw new DataValidationException("Product Book does not exist for the specified symbol");
        }
        return productBook.removeQuotesForUser(user);
    }

    public ArrayList<String> getProductList() {
        return new ArrayList<>(productBooks.keySet());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (ProductBook productBook : productBooks.values()) {
            sb.append(productBook).append("\n");
        }
        return sb.toString();
    }
}
