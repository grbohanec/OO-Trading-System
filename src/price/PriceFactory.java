package price;

import exceptions.BadValueException;
import price.exceptions.InvalidPriceOperation;
import java.util.HashMap;

public abstract class PriceFactory {
    private static HashMap<Integer, Price> priceHash = new HashMap<>();

    public static Price makePrice (int cents) {
        if (priceHash.containsKey(cents)) {
            return priceHash.get(cents);
        }
        Price p = new Price(cents);
        priceHash.put(cents, p);
        return p;
    }

    public static Price makePrice (String stringValueIn) throws InvalidPriceOperation, BadValueException {
        if (stringValueIn == null) {
            throw new InvalidPriceOperation("Input string is null or empty!");
        }

        if (stringValueIn.isEmpty()) {
            throw new BadValueException("String is empty!");
        }

        stringValueIn = stringValueIn.replace("$", "").replace(",", "");

        boolean isNegative = false;
        int totalCents = 0;

        if (stringValueIn.startsWith("-")) {
            isNegative = true;
            stringValueIn = stringValueIn.substring(1);
        }

        String[] parts = stringValueIn.split("\\.");
        int dollars = 0;
        int cents = 0;

        if (!parts[0].isEmpty()) {
            dollars = Integer.parseInt(parts[0]);
        }

        if (parts.length > 1 && !parts[1].isEmpty()) {
            if (parts[1].length() > 2 ) {
                cents = Integer.parseInt(parts[1].substring(0, 2));
            }
            else {
                cents = Integer.parseInt(parts[1]);
            }
        }

        totalCents = dollars * 100 + cents;
        if (isNegative) {
            totalCents *= -1;
        }

        return new Price(totalCents);
    }
}
