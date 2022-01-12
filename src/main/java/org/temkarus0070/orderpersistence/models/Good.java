package org.temkarus0070.orderpersistence.models;


import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;


@Entity
@Table(name = "GOODS")
public class Good implements Serializable {
    public Good() {
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    private long id;

    @Column
    @NonNull
    private String name;

    @Column
    @NonNull
    private double price;

    @Column
    @NonNull
    private int count;

    @Column
    @NonNull
    private double sum;

    @JsonBackReference
    @ManyToOne(optional = false)
    @NonNull
    private Order order;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Good good = (Good) o;
        return id == good.id && Double.compare(good.price, price) == 0 && count == good.count && Double.compare(good.sum, sum) == 0 && Objects.equals(name, good.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, count, sum);
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

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
