package com.example.application.views.menu;

import com.example.application.data.Shawarma;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

public class OrderForm extends FormLayout {

    Shawarma shawarma;
    Button add = new Button("+");
    Button close = new Button("Закрыть");
    Button remove = new Button("-");
    Paragraph textMedium;
    Paragraph textMedium1;

    public OrderForm() {
        addClassName("order");

        textMedium = new Paragraph();
        textMedium.setText("Название");
        textMedium.setWidth("max-content");
        textMedium.getStyle().set("font-size", "var(--lumo-font-size-m)");

        textMedium1 = new Paragraph();
        textMedium1.setText("В корзине: 0");
        textMedium1.setWidth("max-content");
        textMedium1.getStyle().set("font-size", "var(--lumo-font-size-m)");

        HorizontalLayout layoutRow = new HorizontalLayout();
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");
        layoutRow.setAlignItems(Alignment.CENTER);
        layoutRow.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutRow.add(textMedium);

        HorizontalLayout layoutRow1 = new HorizontalLayout();
        layoutRow1.addClassName(Gap.MEDIUM);
        layoutRow1.setWidth("100%");
        layoutRow1.setHeight("min-content");
        layoutRow1.setAlignItems(Alignment.CENTER);
        layoutRow1.setJustifyContentMode(JustifyContentMode.CENTER);
        layoutRow1.add(textMedium1);

        add(
            layoutRow,
            layoutRow1,
            createButtonsLayout()
        );
    }

    public void setShawarma(Shawarma shawarma, Integer count) {
        this.shawarma = shawarma;
        if (shawarma == null) {
            textMedium.setText("Name");
        } else {
            textMedium.setText(shawarma.getName());
        }

        setCount(count);
    }

    public void setCount(Integer count) {
        if (count == null) {
            textMedium1.setText("В корзине: 0");
        } else {
            textMedium1.setText("В корзине: " + count);
        }
    }

    private HorizontalLayout createButtonsLayout() {
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY); 
        remove.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    
        add.addClickListener(event -> fireEvent(new AddEvent(this, shawarma)));
        remove.addClickListener(event -> fireEvent(new RemoveEvent(this, shawarma)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));
    
        close.addClickShortcut(Key.ESCAPE);
    
        return new HorizontalLayout(add, close, remove); 
    }
    
    // Events
    public static abstract class OrderFormEvent extends ComponentEvent<OrderForm> {
        private Shawarma shawarma;

        protected OrderFormEvent(OrderForm source, Shawarma shawarma) { 
            super(source, false);
            this.shawarma = shawarma;
        }

        public Shawarma getShawarma() {
            return shawarma;
        }
    }

    public static class AddEvent extends OrderFormEvent {
        AddEvent(OrderForm source, Shawarma shawarma) {
            super(source, shawarma);
        }
    }

    public static class RemoveEvent extends OrderFormEvent {
        RemoveEvent(OrderForm source, Shawarma contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends OrderFormEvent {
        CloseEvent(OrderForm source) {
            super(source, null);
        }
    }

    public Registration addRemoveListener(ComponentEventListener<RemoveEvent> listener) { 
        return addListener(RemoveEvent.class, listener);
    }

    public Registration addAddListener(ComponentEventListener<AddEvent> listener) {
        return addListener(AddEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
