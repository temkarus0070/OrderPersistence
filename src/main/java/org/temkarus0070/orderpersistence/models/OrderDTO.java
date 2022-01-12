package org.temkarus0070.orderpersistence.models;

import java.util.List;
import java.util.Objects;

public class OrderDTO {
    private Long orderNum;

    private String clientFIO;


    private List<GoodDTO> goods;

    public OrderDTO(Long orderNum, String clientFIO, List<GoodDTO> goods, Status status) {
        this.orderNum = orderNum;
        this.clientFIO = clientFIO;
        this.goods = goods;
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return Objects.equals(orderNum, orderDTO.orderNum) && Objects.equals(clientFIO, orderDTO.clientFIO) && Objects.equals(goods, orderDTO.goods) && status == orderDTO.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderNum, clientFIO, goods, status);
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public String getClientFIO() {
        return clientFIO;
    }

    public void setClientFIO(String clientFIO) {
        this.clientFIO = clientFIO;
    }

    public List<GoodDTO> getGoods() {
        return goods;
    }

    public void setGoods(List<GoodDTO> goods) {
        this.goods = goods;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status;
}
