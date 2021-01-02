package id.ac.ui.cs.mobileprogramming.hadlina.moneytracker.utils;

public class Calculator {

    static{
        System.loadLibrary("native-lib");
    }

    native public double add(double a, double b);

    native public double subtract(double a, double b);

    native public double multiply(double a, double b);

    native public double divide(double a, double b);
}
