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







}
