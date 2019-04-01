package net.ddns.bivor.group14_hw05;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Date;

public class Expense implements Serializable {

    String name, cost, datePicked, imageURL;
    Date date;

    public Expense() {
    }

    public Expense(String name, String cost, String datePicked, Date date, String imageURL) {
        this.name = name;
        this.cost = cost;
        this.datePicked = datePicked;
        this.date = date;
        this.imageURL = imageURL;
    }

    public static Comparator<Expense> COMPARE_BY_COST = new Comparator<Expense>() {
        public int compare(Expense one, Expense other) {
            Float a = Float.parseFloat(one.cost);
            Float b = Float.parseFloat(other.cost);
            return a.compareTo(b);
        }
    };

    public static Comparator<Expense> COMPARE_BY_DATE = new Comparator<Expense>() {
        public int compare(Expense one, Expense other) {
            return one.date.compareTo(other.date);
        }
    };

}
