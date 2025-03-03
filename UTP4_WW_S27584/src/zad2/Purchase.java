/**
 *
 *  @author Wydra Weronika S27584
 *
 */

package zad2;


public class Purchase {
    private final String id;
    private final String surnameName;
    private final String item;
    private final double cost;
    private final double amount;

    public Purchase(String id, String surnameName, String item, double cost, double amount) {
        this.id = id;
        this.surnameName = surnameName;
        this.item = item;
        this.cost = cost;
        this.amount = amount;
    }

    public String getId() {
        return id;
    }

    public String getSurnameName() {
        return surnameName;
    }

    public String getItem() {
        return item;
    }

    public double getCost() {
        return cost;
    }

    public double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return getId() + ";" + getSurnameName() + ";" + getItem() + ";" + getCost() + ";" + getAmount();
    }
}
