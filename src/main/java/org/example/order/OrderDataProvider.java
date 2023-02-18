package org.example.order;

import java.util.List;

public class OrderDataProvider {

    public static List<Order> getDefaultListOfOrders() {
        return List.of(
                new Order(List.of("61c0c5a71d1f82001bdaaa6c")),
                new Order(List.of("61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa77")),
                new Order(List.of("61c0c5a71d1f82001bdaaa6d", "61c0c5a71d1f82001bdaaa70", "61c0c5a71d1f82001bdaaa77"))
        );

    }
}
