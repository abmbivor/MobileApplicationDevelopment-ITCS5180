package net.ddns.bivor.inclass08;

import java.io.Serializable;
import java.util.Date;

public class Expense implements Serializable {

    String name, category, amount;
    Date date;

    public Expense() {
    }

    public Expense(String name, String category, String amount, Date date) {
        this.name = name;
        this.category = category;
        this.amount = amount;
        this.date = date;
    }
}
