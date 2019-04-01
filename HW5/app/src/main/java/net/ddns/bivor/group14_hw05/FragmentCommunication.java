package net.ddns.bivor.group14_hw05;

public interface FragmentCommunication {
    void respond (Expense expense, int index);
    void delete (Expense expense);
    void respondEdit (Expense expense, int index);
}
