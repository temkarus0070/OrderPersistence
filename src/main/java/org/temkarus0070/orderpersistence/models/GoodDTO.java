package org.temkarus0070.orderpersistence.models;

public class GoodDTO {
    private long id;
    private String name;
    private double price;
    private int count;
    private double sum;

    public GoodDTO(long id, String name, double price, int count, double sum) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.count = count;
        this.sum = sum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getSum() {
        return sum;
    }

    public void setSum(double sum) {
        this.sum = sum;
    }
}
