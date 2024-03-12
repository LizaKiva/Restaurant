package com.example.application.views.login;

import com.example.application.data.Role;

import com.example.application.security.AuthenticatedUser;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.PermitAll;

@PageTitle("Redirecting..")
@Route(value = "")
@PermitAll
@Uses(Icon.class)
public class RedirectView extends Composite<VerticalLayout> implements BeforeEnterObserver {

    private boolean isAdmin;

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        
        if (isAdmin) {
            event.rerouteTo("edit_menu");
        } else {
            event.rerouteTo("menu");
        }
    }

    public RedirectView(AuthenticatedUser authenticatedUser) {
        isAdmin = authenticatedUser.get().get().getRoles().contains(Role.ADMIN);
    }
    
}
