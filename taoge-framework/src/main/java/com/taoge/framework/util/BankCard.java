package com.taoge.framework.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author xuejingtao
 * @since 2021/6/4 15:31
 **/
public class BankCard {

    private static final Pattern BASE = Pattern.compile("^[1-9][0-9]{12,18}$");

    private final String bankCode;

    public BankCard(String bankCode) {
        this.bankCode = bankCode;
    }

    public boolean verify() {
        Matcher matcher = BASE.matcher(bankCode);
        if(!matcher.matches()) {
            return false;
        }
        char luhn = luhn(bankCode.substring(0, bankCode.length() - 1));
        return bankCode.charAt(bankCode.length() - 1) == luhn;
    }

    private char luhn(String nonCheckNumber) {
        char[] numbers = nonCheckNumber.toCharArray();
        int sum = 0;
        for (int i = numbers.length - 1, j = 0; i >= 0; i--, j++) {
            int k = numbers[i] - '0';
            if((j % 2) == 0) {
                k <<= 1;
                k = k / 10 + k % 10;
            }
            sum += k;
        }
        return (char)((10 - sum % 10) % 10 + '0');
    }

    public static void main(String[] args) {
        BankCard bankCard1 = new BankCard("6226220181690651");
        BankCard bankCard2 = new BankCard("6226220181620651");
        BankCard bankCard3 = new BankCard("6226222181620651");
        System.out.println(bankCard1.verify());
        System.out.println(bankCard2.verify());
        System.out.println(bankCard3.verify());
    }
}
