package com.example.application.kitchen;

import java.util.LinkedList;
import java.util.Queue;

import com.example.application.data.Order;
import com.example.application.data.Status;
import com.example.application.services.OrderService;
import com.vaadin.flow.component.progressbar.ProgressBar;

public class Kitchen {
    private static Thread slave0;
    private static Thread slave1;
    private static Thread slave2;
    private static Queue<String> queue = new LinkedList<String>();
    private static OrderService orderService;

    public class Data {
        private Data(String username, ProgressBar progressBar) {
            this.username = username;
            this.progressBar = progressBar;
        }

        public String username;
        public ProgressBar progressBar;
    }

    public void initialize() {
        Runnable cook = () -> {
            while (true) {
                String username = queue.poll();
                if (username != null) {
                    long start = System.currentTimeMillis();
                    while (true) {
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        Order order = orderService.findByUsername(username);
                        if (order.getStatus() == Status.READY || order.getCost() == 0) {
                            break;
                        }

                        Float percent = order.getCompleted();
                        long finish = System.currentTimeMillis();
                        long timeElapsed = finish - start;
                        start = finish;
                        double doubleValue = ((Number)timeElapsed).doubleValue();
                        Float resultPercent = (float) (percent + doubleValue / (order.getTime().doubleValue() * 1000));
                        if (resultPercent >= 1f) {
                            resultPercent = 1f;
                            order.setStatus(Status.READY);
                        }
                        
                        order.setCompleted(resultPercent);
                        orderService.saveOrder(order);
                    }
                }
            }
        };

        slave0 = new Thread(cook);
        slave1 = new Thread(cook);
        slave2 = new Thread(cook);

        slave0.start();
        slave1.start();
        slave2.start();
    }

    public void addOrder(String username, OrderService orderService) {
        Kitchen.orderService = orderService;
        queue.add(username);
    }
}
