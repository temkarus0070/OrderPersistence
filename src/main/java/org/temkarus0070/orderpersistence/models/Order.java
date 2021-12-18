package org.temkarus0070.orderpersistence.models;


import javax.persistence.*;
import java.util.Collection;

@Entity
public class Order{

    public Order(){

    }
    private String clientFIO;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderNum;

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

    public Collection<Good> getGoods() {
        return goods;
    }

    public void setGoods(Collection<Good> goods) {
        this.goods = goods;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @OneToMany(mappedBy = "order")
    private Collection<org.temkarus0070.orderpersistence.models.Good> goods;

    private org.temkarus0070.orderpersistence.models.Status status;


}
