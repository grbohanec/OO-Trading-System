package pubsub;
import price.Price;

public class CurrentMarketSide {
    private final Price price;
    private final int volume;

    public CurrentMarketSide (Price price, int volume) {
        this.price = price;
        this.volume = volume;
    }

    public Price getPrice() {
        return price;
    }

    public int getVolume() {
        return volume;
    }

    @Override
    public String toString () {
        return (price != null ? price.toString() : "$0.00") + "x" + volume;
    }
}
