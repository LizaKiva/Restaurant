package com.example.application.views.payment;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.data.Balance;
import com.example.application.data.Order;
import com.example.application.data.Status;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.BalanceService;
import com.example.application.services.OrderService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PageTitle("Payment")
@Route(value = "payment")
@PermitAll
public class PaymentView extends Div {

    AuthenticatedUser authenticatedUser;

    private TextField cardNumber;
    private TextField cardholderName;
    private Select<Integer> month;
    private Select<Integer> year;
    private ExpirationDateField expiration;
    private PasswordField csc;
    private Button cancel;
    private Button submit;
    private Button skip;

    /**
     * Matches Visa, MasterCard, American Express, Diners Club, Discover, and JCB
     * cards. See https://stackoverflow.com/a/9315696
     */
    private String CARD_REGEX = "^(?:4[0-9]{12}(?:[0-9]{3})?|[25][1-7][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35d{3})d{11})$";

    public PaymentView(AuthenticatedUser authenticatedUser) {
        this.authenticatedUser = authenticatedUser;
        addClassName("payment-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        cancel.addClickListener(e -> {
            UI.getCurrent().navigate("");
        });
        submit.addClickListener(e -> {
            processPayment();
        });
        skip.addClickListener(e -> {
            payOrder();
        });
    }

    private Component createTitle() {
        return new H3("Форма поддержки начинающих разработчиков");
    }

    private Component createFormLayout() {
        cardNumber = new TextField("Credit card number");
        cardNumber.setPlaceholder("1234 5678 9123 4567");
        cardNumber.setPattern(CARD_REGEX);
        cardNumber.setAllowedCharPattern("[\\d ]");
        cardNumber.setRequired(true);
        cardNumber.setErrorMessage("Дай свою настоящюю кредитку! >_<");

        cardholderName = new TextField("Cardholder name");
        cardholderName.setRequired(true);

        month = new Select<>();
        month.setPlaceholder("Month");
        month.setItems(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        year = new Select<>();
        year.setPlaceholder("Year");
        year.setItems(20, 21, 22, 23, 24, 25);

        expiration = new ExpirationDateField("Expiration date", month, year);   
        csc = new PasswordField("CSC");
        csc.setRequired(true);

        FormLayout formLayout = new FormLayout();
        formLayout.add(cardNumber, cardholderName, expiration, csc);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");

        submit = new Button("Дать денег");
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        cancel = new Button("Назад");

        skip = new Button("У меня нет кредитки");
        skip.addThemeVariants(ButtonVariant.LUMO_ERROR);

        buttonLayout.add(submit);
        buttonLayout.add(cancel);
        buttonLayout.add(skip);
        return buttonLayout;
    }

    private void processPayment() {
        if (cardNumber.isInvalid()) {
            return;
        }
        
        if (cardNumber.isEmpty() || csc.isEmpty() || cardholderName.isEmpty()) {
            Notification.show("Кажется вы забыли какие-то поля");
            return;
        }

        payOrder();
    }

    private void payOrder() {
        String username = authenticatedUser.get().get().getUsername();
        Order order = orderService.findByUsername(username);

        if (order == null) {
            Notification.show("У вас нет заказа");
            return;
        }

        if (order.getStatus() == Status.COOKING) {
            Notification.show("Ваш заказ ещё не готов");
            return;
        }

        Balance balance = balanceService.get((long) 1).get();
        balance.setBalance(balance.getBalance() + order.getCost());
        order.setCost(0);
        order.setTime(0);
        order.setCompleted(0f);

        balanceService.saveBalance(balance);
        orderService.saveOrder(order);

        UI.getCurrent().navigate("");
    }

    private class ExpirationDateField extends CustomField<String> {
        public ExpirationDateField(String label, Select<Integer> month, Select<Integer> year) {
            setLabel(label);
            HorizontalLayout layout = new HorizontalLayout(month, year);
            layout.setFlexGrow(1.0, month, year);
            month.setWidth("100px");
            year.setWidth("100px");
            add(layout);
        }

        @Override
        protected String generateModelValue() {
            // Unused as month and year fields part are of the outer class
            return "";
        }

        @Override
        protected void setPresentationValue(String newPresentationValue) {
            // Unused as month and year fields part are of the outer class
        }

    }

    @Autowired()
    private OrderService orderService;
    @Autowired()
    private BalanceService balanceService;

}
