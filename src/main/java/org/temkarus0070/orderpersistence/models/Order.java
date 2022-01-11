package org.temkarus0070.orderpersistence.models;


import org.springframework.lang.NonNull;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "ORDERS")
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "order_num")
    @NonNull
    private Long orderNum;

    public Order(){
    }

    @Column
    @NonNull
    private String clientFIO;

    @OneToMany(mappedBy = "order",cascade = {CascadeType.ALL})
    private List<Good> goods;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(orderNum, order.orderNum) && Objects.equals(clientFIO, order.clientFIO) && Objects.equals(goods, order.goods) && status == order.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNum, clientFIO, goods, status);
    }

    @Enumerated(value = EnumType.ORDINAL)
    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public List<Good> getGoods() {
        return goods;
    }

    public void setGoods(List<Good> goods) {
        this.goods = goods;
    }





    public String getClientFIO() {
        return clientFIO;
    }

    public void setClientFIO(String clientFIO) {
        this.clientFIO = clientFIO;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }







}
