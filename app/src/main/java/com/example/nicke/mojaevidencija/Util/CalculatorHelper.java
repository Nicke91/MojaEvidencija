package com.example.nicke.mojaevidencija.Util;

/**
 * Created on 2/22/2018.
 */

public class CalculatorHelper {

    public static int calculateRowTotal(String price, String quantity) {

        if (price.equals("") || quantity.equals("")) {
            return 0;
        }
        return Integer.parseInt(price) * Integer.parseInt(quantity);
    }

    public static int calculateTotal(int[] prices) {
        int total = 0;

        for (int price : prices) {
            total += price;
        }
        return total;
    }
}
