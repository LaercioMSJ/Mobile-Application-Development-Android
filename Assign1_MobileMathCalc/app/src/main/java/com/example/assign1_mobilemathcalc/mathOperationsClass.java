package com.example.assign1_mobilemathcalc;

public class mathOperationsClass {

    public double negPos(double value){
        if (value == 0) {
            return value;
        }
        else {
            return -1*value;
        }
    }

    public boolean decimal(double value){
        if (value % 1 == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    public String calculate(double firstValue, String operator, double secondValue){
        double resultInDouble = Double.NaN;
        String resultInString = "NaN";

        switch (operator) {
            case "+":
                resultInDouble = addition(firstValue, secondValue);
                break;
            case "−":
                resultInDouble = subtraction(firstValue, secondValue);
                break;
            case "×":
                resultInDouble = multiplication(firstValue, secondValue);
                break;
            case "÷":
                if (secondValue == 0) {
                    resultInString = "NaN";
                } else {
                    resultInDouble = division(firstValue, secondValue);
                }
                break;
        }

        if (resultInDouble % 1 == 0) {
            resultInString = String.valueOf((int) resultInDouble);
        }
        else if (resultInDouble % 1 != 0) {
            resultInString = String.valueOf(resultInDouble);
        }

        return resultInString;
    }

    public double addition(double firstValue, double secondValue){
        return firstValue+secondValue;
    }

    public double subtraction(double firstValue, double secondValue){
        return firstValue-secondValue;
    }

    public double multiplication(double firstValue, double secondValue){
        return firstValue*secondValue;
    }

    public double division(double firstValue, double secondValue){return firstValue/secondValue;
    }

}
