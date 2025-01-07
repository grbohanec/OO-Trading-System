package user;

import exceptions.BadValueException;
import exceptions.NullParamException;
import pubsub.CurrentMarketObserver;
import pubsub.CurrentMarketSide;
import tradable.TradableDTO;
import java.util.HashMap;

public class User implements CurrentMarketObserver {
    private final String userId;
    private HashMap<String, TradableDTO> tradables;
    private HashMap<String, CurrentMarketSide[]> currentMarkets;

    public User (String userId) throws NullParamException, BadValueException {
        this.userId = setUser(userId);
        this.tradables = new HashMap<>();
        this.currentMarkets = new HashMap<>();
    }

    public String getUserId() {
        return userId;
    }

    private String setUser (String userId) throws NullParamException, BadValueException {
        if (userId == null) {
            throw new NullParamException("User is null!");
        }
        if (!userId.matches("[A-Z]{3}")) {
            throw new BadValueException("User must be a 3-letter user code!");
        }
        return userId;
    }

    public void addTradable (TradableDTO o) {
        if (o == null) {
            return;
        }
        tradables.put(o.id, o);
    }

    public boolean hasTradableWithRemainingQty() {
        for (TradableDTO tradable : tradables.values()) {
            if (tradable.remainingVolume > 0) {
                return true;
            }
        }
        return false;
    }

    public TradableDTO getTradableWithRemainingQty() {
        for (TradableDTO tradable : tradables.values()) {
            if (tradable.remainingVolume > 0) {
                return tradable;
            }
        }
        return null;
    }

    @Override
    public void updateCurrentMarket(String symbol, CurrentMarketSide buySide, CurrentMarketSide sellSide) {
        currentMarkets.put(symbol, new CurrentMarketSide[]{buySide, sellSide});
    }

    public String getCurrentMarkets() {
        StringBuilder sb = new StringBuilder();
        for (String symbol : currentMarkets.keySet()) {
            CurrentMarketSide[] sides = currentMarkets.get(symbol);
            sb.append(symbol)
                    .append(" ")
                    .append(sides[0].toString())
                    .append(" - ")
                    .append(sides[1].toString())
                    .append("\n");
        }
        return sb.toString();
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("User Id: ").append(userId).append("\n");
        for (TradableDTO tradable : tradables.values()) {
            sb.append("\tProduct: ").append(tradable.product).append(", Price: ").append(tradable.price)
                    .append(", OriginalVolume: ").append(tradable.originalVolume).append(", RemainingVolume: ")
                    .append(tradable.remainingVolume).append(", CancelledVolume: ").append(tradable.cancelledVolume)
                    .append(", FilledVolume: ").append(tradable.filledVolume).append(", User: ").append(tradable.user)
                    .append(", Side: ").append(tradable.side).append(", Id: ").append(tradable.id).append("\n");
        }
        return sb.toString();
    }
}
