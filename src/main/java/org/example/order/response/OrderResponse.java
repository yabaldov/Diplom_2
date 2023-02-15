package org.example.order.response;

public class OrderResponse {

    private Boolean success;
    private String name;
    private OrderPlaced order;

    public OrderResponse() {
    }

    public OrderResponse(Boolean success, String name, OrderPlaced order) {
        this.success = success;
        this.name = name;
        this.order = order;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public OrderPlaced getOrder() {
        return order;
    }

    public void setOrder(OrderPlaced order) {
        this.order = order;
    }
}
