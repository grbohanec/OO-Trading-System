package tradable;

import price.Price;
import static book.GlobalConstants.BookSide;

public class TradableDTO {
    public final String user;
    public final String product;
    public final Price price;
    public final int originalVolume;
    public final int remainingVolume;
    public final int cancelledVolume;
    public final int filledVolume;
    public final String id;
    public final BookSide side;

    public TradableDTO(String user, String product, Price price, int originalVolume,
                       int remainingVolume, int cancelledVolume, int filledVolume,
                       String id, BookSide side) {
        this.user = user;
        this.product = product;
        this.price = price;
        this.originalVolume = originalVolume;
        this.remainingVolume = remainingVolume;
        this.cancelledVolume = cancelledVolume;
        this.filledVolume = filledVolume;
        this.id = id;
        this.side = side;
    }

    @Override
    public String toString() {
        return String.format("\tProduct: %s, Price: %s, OriginalVolume: %d, RemainingVolume: %d, CancelledVolume: %d, FilledVolume: %d, User: %s, Side: %s, Id: %s",
                product, price, originalVolume, remainingVolume, cancelledVolume, filledVolume, user, side, id);
    }
}
