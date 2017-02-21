package edu.ub.pis2016.gbarquero.pis2016_tasca1;

/**
 * Created by German on 15/02/2017.
 */

public class Currencies {
    public static final int EURO = 0;
    public static final int DOLLAR = 1;

    public static int getFullStringId(int currency){
        switch(currency){
            case EURO:
                return R.string.euro_full;
            case DOLLAR:
                return R.string.dollar_full;
            default:
                return -1;
        }
    }

    public static int getSymbolStringId(int currency){
        switch(currency){
            case EURO:
                return R.string.euro;
            case DOLLAR:
                return R.string.dollar;
            default:
                return -1;
        }
    }
}
