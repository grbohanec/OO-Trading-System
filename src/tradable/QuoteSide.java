package tradable;

import static book.GlobalConstants.BookSide;
import exceptions.BadValueException;
import exceptions.NullParamException;
import price.Price;

public class QuoteSide implements Tradable {
    private final String user;
    private final String product;
    private final Price price;
    private final BookSide side;
    private final String id;
    private final int originalVolume;
    private int remainingVolume;
    private int cancelledVolume;
    private int filledVolume;

    public QuoteSide (String user, String product, Price price, int originalVolume, BookSide side)
            throws BadValueException, NullParamException {
        this.user = setUser(user);
        this.product = setProduct(product);
        this.price = setPrice(price);
        this.side = setSide(side);
        this.id = generateId();
        this.originalVolume = setOriginalVolume(originalVolume);
        this.remainingVolume = originalVolume;
        this.cancelledVolume = 0;
        this.filledVolume = 0;
    }

    private String setUser (String user) throws NullParamException, BadValueException {
        if (user == null) {
            throw new NullParamException("User is null!");
        }
        if (!user.matches("[A-Z]{3}")) {
            throw new BadValueException("User must be a 3-letter user code!");
        }
        return user;
    }

    private String setProduct(String product) throws NullParamException, BadValueException {
        if (product == null) {
            throw new NullParamException("Product is null!");
        }
        if (!product.matches("[A-Z0-9.]{1,5}")) {
            throw new BadValueException("Product must follow stock symbol pattern!");
        }
        return product;
    }

    private Price setPrice (Price price) throws NullParamException {
        if (price == null) {
            throw new NullParamException("Price is null!");
        }
        return price;
    }

    private BookSide setSide (BookSide side) throws NullParamException {
        if (side == null) {
            throw new NullParamException("Side is null!");
        }
        return side;
    }

    private String generateId () {
        return user + product + price + System.nanoTime();
    }

    private int setOriginalVolume(int originalVolume) throws BadValueException {
        if (originalVolume <= 0  || originalVolume >= 10000) {
            throw new BadValueException("Volume must be greater than 0, and less than 10,000!");
        }
        return originalVolume;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public int getCancelledVolume() {
        return cancelledVolume;
    }

    @Override
    public void setCancelledVolume(int newVol) {
        this.cancelledVolume = newVol;
    }

    @Override
    public int getRemainingVolume() {
        return remainingVolume;
    }

    @Override
    public void setRemainingVolume(int newVol) {
        this.remainingVolume = newVol;
    }

    @Override
    public TradableDTO makeTradableDTO() {
        return new TradableDTO(this.getUser(), this.getProduct(), this.getPrice(), this.getOriginalVolume(),
                this.getRemainingVolume(), this.getCancelledVolume(), this.getFilledVolume(), this.getId(),
                this.getSide());
    }

    @Override
    public Price getPrice() {
        return price;
    }

    @Override
    public void setFilledVolume(int newVol) {
        this.filledVolume = newVol;
    }

    @Override
    public int getFilledVolume() {
        return filledVolume;
    }

    @Override
    public BookSide getSide() {
        return side;
    }

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public String getProduct() {
        return product;
    }

    @Override
    public int getOriginalVolume() {
        return originalVolume;
    }

    @Override
    public String toString() {
        return String.format("%s quote side: %s %s at %s, Orig Vol: %d, Rem Vol: %d, Fill Vol: %d, CXL Vol: %d, ID: %s",
                user, side, product, price, originalVolume, remainingVolume, filledVolume, cancelledVolume, id);
    }
}
