package com.example.application.views.register;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.application.data.Role;
import com.example.application.data.User;
import com.example.application.security.SecurityConfiguration;
import com.example.application.services.UserService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@PageTitle("Register")
@Route(value = "register")
@AnonymousAllowed
@Uses(Icon.class)
public class RegisterView extends Composite<VerticalLayout> {

    private SecurityConfiguration securityConfiguration = new SecurityConfiguration();
    private TextField usernameField;
    private PasswordField passwordField;

    public RegisterView() {
        usernameField = new TextField();
        passwordField = new PasswordField();
        Button buttonPrimary = new Button();
        Button buttonSecondary = new Button();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(JustifyContentMode.CENTER);
        getContent().setAlignItems(Alignment.CENTER);

        usernameField.setLabel("Логин");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, usernameField);
        usernameField.setWidth("min-content");

        passwordField.setLabel("Пароль");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, passwordField);
        passwordField.setWidth("min-content");

        buttonPrimary.setText("Зарегистрироваться");
        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        buttonPrimary.addClickListener(e -> register());
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        buttonSecondary.setText("Назад");
        buttonSecondary.addClickListener(e -> UI.getCurrent().navigate(""));
        buttonSecondary.setWidth("min-content");

        getContent().add(usernameField);
        getContent().add(passwordField);
        getContent().add(buttonPrimary);
        getContent().add(buttonSecondary);
    }

    private boolean checkUsername(String username) {
        if (username.isEmpty()) {
            Notification.show("Username can't be emty");
            return false;
        }

        if(userService.contains(username)) {
            Notification.show("Username is already taken");
            return false;
        }

        return true;
    }

    private void register() {
        String username = usernameField.getValue().trim();
        String password = passwordField.getValue();

        if (password.isEmpty()) {
            Notification.show("Password can't be empty");
        }

        if (!password.isEmpty() && checkUsername(username)) {
            User user = new User();
            user.setUsername(username);

            HashSet<Role> roles = new HashSet<Role>();
            roles.add(Role.USER);
            user.setRoles(roles);

            user.setHashedPassword(securityConfiguration.passwordEncoder().encode(password));
            userService.saveUser(user);

            UI.getCurrent().navigate("");
        }
    }

    @Autowired()
    private UserService userService;
}
