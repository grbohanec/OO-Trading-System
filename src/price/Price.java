package price;

import java.util.Objects;
import price.exceptions.InvalidPriceOperation;

public class Price implements Comparable<Price> {

    private final int value;

    public Price (int value) {
        this.value = value;
    }

    public boolean isNegative () {
        return value < 0;
    }

    public Price add (Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("price.Price is null!");
        }
        return new Price (this.value + p.value);
    }

    public Price subtract (Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("price.Price is null!");
        }
        return new Price (this.value - p.value);
    }

    public Price multiply (int n) {
        return new Price (this.value * n);
    }

    public boolean greaterOrEqual (Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("greaterOrEqual price.Price parameter is null!");
        }
        return this.value >= p.value;
    }

    public boolean lessOrEqual (Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("lessOrEqual price.Price parameter is null!");
        }
        return this.value <= p.value;
    }

    public boolean greaterThan (Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("greaterThan price.Price parameter is null!");
        }
        return this.value > p.value;
    }

    public boolean lessThan (Price p) throws InvalidPriceOperation {
        if (p == null) {
            throw new InvalidPriceOperation("lessThan price.Price parameter is null!");
        }
        return this.value < p.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return value == price.value;
    }

    @Override
    public int compareTo(Price p) {
        return this.value - p.value;
    }

    @Override
    public String toString() {
        double dollarValue = value / 100.0;
        return String.format("$%,.2f", dollarValue);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}
