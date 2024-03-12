package com.example.application.views.menu;

import com.example.application.data.Order;
import com.example.application.data.Shawarma;
import com.example.application.data.Status;
import com.example.application.kitchen.Kitchen;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.OrderService;
import com.example.application.services.ShawarmaService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

import jakarta.annotation.security.RolesAllowed;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

//@Push
@PageTitle("Menu")
@Route(value = "menu")
@RolesAllowed("USER")
@Uses(Icon.class)
public class MenuView extends Composite<VerticalLayout> {

    AuthenticatedUser authenticatedUser;

    private OrderForm orderForm;
    Paragraph timeText;
    Paragraph priceText;
    Button orderButton;
    Button refreshButton;
    Grid basicGrid;
    Paragraph orderPriceText;
    Button cancelButton;
    Button payButton;
    ProgressBar progressBar;

    /// User values
    private Integer totalCost = 0;
    private Integer totalTime = 0; 
    private HashMap<Shawarma, Integer> cart = new HashMap<Shawarma, Integer>();

    public MenuView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;

        HorizontalLayout layoutRow = new HorizontalLayout();
        H1 h1 = new H1();
        Button buttonSecondary = new Button();
        Avatar avatar = new Avatar();
        VerticalLayout layoutColumn2 = new VerticalLayout();
        basicGrid = new Grid(Shawarma.class);
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        HorizontalLayout layoutRow3 = new HorizontalLayout();
        timeText = new Paragraph();
        priceText = new Paragraph();
        HorizontalLayout layoutRow4 = new HorizontalLayout();
        orderButton = new Button();
        Hr hr = new Hr();
        HorizontalLayout layoutRow5 = new HorizontalLayout();
        H3 h3 = new H3();
        orderPriceText = new Paragraph();
        refreshButton = new Button();
        cancelButton = new Button();
        payButton = new Button();
        progressBar = new ProgressBar();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        Paragraph textSmall = new Paragraph();
        HorizontalLayout layoutRow6 = new HorizontalLayout();
        orderForm = new OrderForm();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutRow.setAlignItems(Alignment.CENTER);
        layoutRow.setJustifyContentMode(JustifyContentMode.END);

        h1.setText("Shawerma Inc.");
        h1.getStyle().set("flex-grow", "1");

        buttonSecondary.setText("Выйти");
        buttonSecondary.addClickListener(e -> authenticatedUser.logout());
        buttonSecondary.setWidth("min-content");

        avatar.setName(authenticatedUser.get().get().getUsername());

        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");
        layoutColumn2.setJustifyContentMode(JustifyContentMode.START);
        layoutColumn2.setAlignItems(Alignment.CENTER);

        basicGrid.setWidth("100%");
        basicGrid.setHeight("300px");
        basicGrid.getStyle().set("flex-grow", "0");
        basicGrid.removeAllColumns();
        basicGrid.addColumn("name");
        basicGrid.addColumn("price");
        basicGrid.addColumn("time");
        basicGrid.addColumn("count");
        setGridSampleData(basicGrid);
        basicGrid.asSingleSelect().addValueChangeListener(e -> orderShawarma((Shawarma) e.getValue()));

        layoutRow2.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow2);
        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");

        layoutRow3.setHeightFull();
        layoutRow2.setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.addClassName(Padding.XSMALL);
        layoutRow3.getStyle().set("flex-grow", "1");
        layoutRow3.setHeight("min-content");
        layoutRow3.setAlignItems(Alignment.CENTER);
        layoutRow3.setJustifyContentMode(JustifyContentMode.START);

        timeText.setText("Время приготовления: 0сек");
        timeText.setWidth("max-content");
        timeText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        priceText.setText("Сумма: 0₽");
        priceText.setWidth("max-content");
        priceText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        layoutRow4.setHeightFull();
        layoutRow2.setFlexGrow(1.0, layoutRow4);
        layoutRow4.addClassName(Gap.MEDIUM);
        layoutRow4.addClassName(Padding.XSMALL);
        layoutRow4.setWidth("100%");
        layoutRow4.setHeight("min-content");
        layoutRow4.setAlignItems(Alignment.CENTER);
        layoutRow4.setJustifyContentMode(JustifyContentMode.END);

        orderButton.setText("Заказать");
        orderButton.setEnabled(false);
        layoutRow4.setAlignSelf(FlexComponent.Alignment.CENTER, orderButton);

        orderButton.setWidth("min-content");
        orderButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        orderButton.addClickListener(e -> orderAction());

        layoutRow5.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutRow5);
        layoutRow5.addClassName(Gap.MEDIUM);
        layoutRow5.setWidth("100%");
        layoutRow5.setHeight("min-content");

        h3.setText("Ваш заказ:");
        layoutRow5.setAlignSelf(FlexComponent.Alignment.START, h3);
        h3.getStyle().set("flex-grow", "1");

        orderPriceText.setText("0₽");
        layoutRow5.setAlignSelf(FlexComponent.Alignment.END, orderPriceText);
        orderPriceText.setWidth("max-content");
        orderPriceText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        refreshButton.setText("Что с моим заказом?");
        layoutRow5.setAlignSelf(FlexComponent.Alignment.CENTER, refreshButton);
        refreshButton.setWidth("min-content");
        refreshButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        refreshButton.addClickListener(e -> updateOrder());

        cancelButton.setText("Отменить");
        cancelButton.setEnabled(false);
        layoutRow5.setAlignSelf(FlexComponent.Alignment.CENTER, cancelButton);
        cancelButton.setWidth("min-content");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        cancelButton.addClickListener(e -> cancelOrder());

        payButton.setText("Оплатить");
        payButton.setEnabled(false);
        layoutRow5.setAlignSelf(FlexComponent.Alignment.CENTER, payButton);
        payButton.setWidth("min-content");
        payButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        payButton.addClickListener(e -> UI.getCurrent().navigate("payment"));

        progressBar.setValue(0);

        layoutColumn3.setWidthFull();
        layoutColumn2.setFlexGrow(1.0, layoutColumn3);
        layoutColumn3.setSpacing(false);
        layoutColumn3.setPadding(false);
        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn3.setJustifyContentMode(JustifyContentMode.END);
        layoutColumn3.setAlignItems(Alignment.END);

        textSmall.setText("© 2024 Егорова Елизавета. All rights reserved.");
        layoutColumn3.setAlignSelf(FlexComponent.Alignment.END, textSmall);
        textSmall.setWidth("max-content");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");

        getContent().add(layoutRow);
        layoutRow.add(h1);
        layoutRow.add(buttonSecondary);
        layoutRow.add(avatar);

        getContent().add(layoutColumn2);

        orderForm.setWidth("min-content");
        orderForm.addAddListener(this::addShawarma);
        orderForm.addRemoveListener(this::removeShawarma);
        orderForm.addCloseListener(e -> closeEditor());

        layoutRow6.setWidthFull();
        layoutRow6.add(basicGrid);
        layoutRow6.add(orderForm);

        layoutColumn2.add(layoutRow6);
        layoutColumn2.add(layoutRow2);
    
        layoutRow2.add(layoutRow3);
        layoutRow3.add(timeText);
        layoutRow3.add(priceText);

        layoutRow2.add(layoutRow4);
        layoutRow4.add(orderButton);

        layoutColumn2.add(hr);
        layoutColumn2.add(layoutRow5);

        layoutRow5.add(h3);
        layoutRow5.add(orderPriceText);
        layoutRow5.add(refreshButton);
        layoutRow5.add(cancelButton);
        layoutRow5.add(payButton);

        layoutColumn2.add(progressBar);
        layoutColumn2.add(layoutColumn3);

        layoutColumn3.add(textSmall);

        /// Initial State Setup
        closeEditor();
    }

    private void orderCart() {
        Kitchen kitchen = new Kitchen();
        kitchen.addOrder(authenticatedUser.get().get().getUsername(), orderService);

        totalCost = 0;
        totalTime = 0;

        for (HashMap.Entry<Shawarma, Integer> item : cart.entrySet()) {
            Shawarma shawarma = item.getKey();
            shawarma.setCount(shawarma.getCount() - item.getValue());
            shawarmaService.saveShawarma(shawarma);
        }

        cart.clear();
        updateGrid();
        updateCart();
        updateOrder();
    }

    private void cancelOrder() {
        String username = authenticatedUser.get().get().getUsername();
        Order order = orderService.findByUsername(username);

        order.setCompleted(0f);
        order.setCost(0);
        order.setTime(0);
        order.setStatus(Status.COOKING);

        for (HashMap.Entry<Long, Integer> item : order.getCart().entrySet()) {
            Shawarma shawarma = shawarmaService.get(item.getKey()).get();
            shawarma.setCount(shawarma.getCount() + item.getValue());
            shawarmaService.saveShawarma(shawarma);
        }

        order.setCart(new HashMap<>());

        orderService.saveOrder(order);

        updateGrid();
        updateOrder();
    }

    private void orderAction() {
        String username = authenticatedUser.get().get().getUsername();
        Order order = orderService.findByUsername(username);
        
        if (order == null) {
            Order newOrder = new Order();
            newOrder.setUsername(username);
            newOrder.setCost(totalCost);
            newOrder.setTime(totalTime);
            newOrder.setStatus(Status.COOKING);
            newOrder.setCompleted(0f);

            HashMap<Long, Integer> indexCart = new HashMap<>();
            for (HashMap.Entry<Shawarma, Integer> item : cart.entrySet()) {
                indexCart.put(item.getKey().getId(), item.getValue());
            }
            
            newOrder.setCart(indexCart);
            orderService.saveOrder(newOrder);

            orderCart();
        } else {
            order.setCompleted((order.getTime() * order.getCompleted()) / (order.getTime() + totalTime));
            order.setCost(order.getCost() + totalCost);
            order.setTime(order.getTime() + totalTime);

            HashMap<Long, Integer> indexCart = order.getCart();
            for (HashMap.Entry<Shawarma, Integer> item : cart.entrySet()) {
                Integer count = indexCart.get(item.getKey().getId());
                if (count != null) {
                    indexCart.replace(item.getKey().getId(), count + item.getValue());
                } else {
                    indexCart.put(item.getKey().getId(), item.getValue());
                }
            }
            
            order.setCart(indexCart);

            orderService.saveOrder(order);

            orderCart();
        }
    }

    private void updateOrder() {
        String username = authenticatedUser.get().get().getUsername();
        Order order = orderService.findByUsername(username);
        if (order != null) {
            orderPriceText.setText(order.getCost() + "₽");
            if (order.getStatus() == Status.COOKING) {
                payButton.setEnabled(false);
                cancelButton.setEnabled(true);
            } else {
                payButton.setEnabled(true);
                cancelButton.setEnabled(false);
            }

            if (order.getCost() == 0) {
                payButton.setEnabled(false);
                cancelButton.setEnabled(false);
            }

            progressBar.setValue(order.getCompleted());
        } else {
            orderPriceText.setText("0₽");
            payButton.setEnabled(false);
            cancelButton.setEnabled(false);
            progressBar.setValue(0);
        }
    }

    private void updateGrid() {
        basicGrid.setItems(shawarmaService.getAll());
    }

    private void updateCart() {
        priceText.setText("Сумма: " + totalCost + "₽");
        timeText.setText("Время приготовления: " + totalTime + "сек");

        orderButton.setEnabled(totalCost > 0);
        String username = authenticatedUser.get().get().getUsername();
        Order order = orderService.findByUsername(username);
        if (order != null && order.getStatus() == Status.READY) {
            orderButton.setEnabled(false);
        }
    }

    private void removeShawarma(OrderForm.RemoveEvent event) {
        Shawarma shawarma = event.getShawarma();

        Integer count = cart.get(shawarma);
        if (count != null && count > 0) {
            cart.replace(shawarma, count - 1);
            totalCost -= shawarma.getPrice();
            totalTime -= shawarma.getTime();

            updateCart();
            orderForm.setCount(cart.get(shawarma));
        }
    }

    private void addShawarma(OrderForm.AddEvent event) {
        Shawarma shawarma = event.getShawarma();

        Integer count = cart.get(shawarma);
        if (count != null && count < shawarma.getCount()) {
            cart.replace(shawarma, count + 1);
            totalCost += shawarma.getPrice();
            totalTime += shawarma.getTime();
            updateCart();
            orderForm.setCount(cart.get(shawarma));
        } else if (count == null) {
            cart.put(shawarma, 1);
            totalCost += shawarma.getPrice();
            totalTime += shawarma.getTime();
            updateCart();
            orderForm.setCount(cart.get(shawarma));
        }
    }

    private void closeEditor() {
        orderForm.setShawarma(null, 0);
        orderForm.setVisible(false);
        removeClassName("editor");
    }

    private void setGridSampleData(Grid grid) {
        grid.setItems(query -> shawarmaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    private void orderShawarma(Shawarma shawarma) {
        if (shawarma == null) {
            closeEditor();
        } else {
            orderForm.setShawarma(shawarma, cart.get(shawarma));
            orderForm.setVisible(true);
            addClassName("editor");
        }
    }

    @Autowired()
    private ShawarmaService shawarmaService;
    @Autowired()
    private OrderService orderService;
}
