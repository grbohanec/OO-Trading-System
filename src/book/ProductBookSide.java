package book;

import exceptions.DataValidationException;
import price.Price;
import tradable.QuoteSide;
import tradable.Tradable;
import tradable.TradableDTO;
import user.UserManager;

import static book.GlobalConstants.BookSide;
import java.util.*;


public class ProductBookSide {
    private final BookSide side;
    private final HashMap<Price, ArrayList<Tradable>> bookEntries;


    public ProductBookSide(BookSide side) {
        this.side = Objects.requireNonNull(side, "Side is null!");
        this.bookEntries = new HashMap<>();
    }

    public TradableDTO add(Tradable o) throws DataValidationException {
        Price price = o.getPrice();
        bookEntries.putIfAbsent(price, new ArrayList<Tradable>());
        bookEntries.get(price).add(o);
        System.out.printf("ADD: %s: %s%n", side, o);
        return o.makeTradableDTO();
    }

    public TradableDTO cancel(String tradableId) throws DataValidationException {
        for (Map.Entry<Price, ArrayList<Tradable>> entry : bookEntries.entrySet()) {
            ArrayList<Tradable> tradables = entry.getValue();
            for (Tradable tradable : tradables) {
                if (tradable.getId().equals(tradableId)) {
                    tradables.remove(tradable);
                    tradable.setCancelledVolume(tradable.getCancelledVolume() + tradable.getRemainingVolume());
                    tradable.setRemainingVolume(0);
                    if (tradables.isEmpty()) {
                        bookEntries.remove(entry.getKey());
                    }
                    String type = tradable.getClass() == QuoteSide.class ? "quote" : "order";
                    System.out.printf("CANCEL: %s %s: %s Cxl Qty: %s\n", side, type, tradableId, tradable.getCancelledVolume());
                    UserManager.getInstance().addToUser(tradable.getUser(), tradable.makeTradableDTO());
                    return tradable.makeTradableDTO();
                }
            }
        }
        return null;
    }

    public TradableDTO removeQuotesForUser(String userName) throws DataValidationException {
        for (Map.Entry<Price, ArrayList<Tradable>> entry : bookEntries.entrySet()) {
            ArrayList<Tradable> tradables = entry.getValue();
            for (Tradable tradable : tradables) {
                if (tradable.getUser().equals(userName)) {
                    TradableDTO dto = cancel(tradable.getId());
                    return dto;
                }
            }
        }
        return null;
    }

    public Price topOfBookPrice() {
        if (bookEntries.isEmpty()) {
            return null;
        }
        if (side == BookSide.BUY) {
            return Collections.max(bookEntries.keySet());
        } else {
            return Collections.min(bookEntries.keySet());
        }
    }

    public int topOfBookVolume() {
        Price topPrice = topOfBookPrice();

        if (topPrice == null) {
            return 0;
        }

        ArrayList<Tradable> tradablesAtTopPrice = bookEntries.get(topPrice);

        int totalVolume = 0;
        for (Tradable tradable : tradablesAtTopPrice) {
            totalVolume += tradable.getRemainingVolume();
        }

        return totalVolume;
    }

    public void tradeOut(Price price, int vol) throws DataValidationException {
        int remainingVol = vol;
        ArrayList<Tradable> tradables = bookEntries.get(price);

        Iterator<Tradable> iterator = tradables.iterator();
        while (iterator.hasNext() && remainingVol > 0) {
            Tradable tradable = iterator.next();
            if (tradable.getRemainingVolume() <= remainingVol) {
                iterator.remove();
                int tradableVol = tradable.getRemainingVolume();
                tradable.setFilledVolume(tradableVol + tradable.getFilledVolume());
                tradable.setRemainingVolume(0);
                remainingVol -= tradableVol;
                System.out.println("\tFULL FILL: (" + side + " " + tradableVol + ") " + tradable);
                UserManager.getInstance().addToUser(tradable.getUser(), tradable.makeTradableDTO());
            } else {
                tradable.setFilledVolume(remainingVol + tradable.getFilledVolume());
                tradable.setRemainingVolume(tradable.getRemainingVolume() - remainingVol);
                System.out.println("\tPARTIAL FILL: (" + side + " " + remainingVol + ") " + tradable);
                UserManager.getInstance().addToUser(tradable.getUser(), tradable.makeTradableDTO());
                remainingVol = 0;
            }
        }

        if (tradables.isEmpty()) {
            bookEntries.remove(price);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Side: ").append(side).append("\n");

        ArrayList<Price> prices = new ArrayList<>(bookEntries.keySet());
        if (side == BookSide.BUY) {
            prices.sort(Collections.reverseOrder());
        } else {
            prices.sort(Comparator.naturalOrder());
        }
        for (Price price : prices) {
            sb.append("\tPrice: ").append(price).append("\n");
            for (Tradable tradable : bookEntries.get(price)) {
                sb.append("  ").append("\t\t").append(tradable).append("\n");
            }
        }
        return sb.toString();
    }
}
