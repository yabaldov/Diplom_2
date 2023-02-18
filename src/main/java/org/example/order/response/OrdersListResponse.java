package org.example.order.response;

import java.util.List;

public class OrdersListResponse {

    private Boolean success;
    private List<OrderInList> orders;
    private Integer total;
    private Integer totalToday;

    public OrdersListResponse() {
    }

    public OrdersListResponse(Boolean success, List<OrderInList> orders, Integer total, Integer totalToday) {
        this.success = success;
        this.orders = orders;
        this.total = total;
        this.totalToday = totalToday;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<OrderInList> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderInList> orders) {
        this.orders = orders;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getTotalToday() {
        return totalToday;
    }

    public void setTotalToday(Integer totalToday) {
        this.totalToday = totalToday;
    }
}
