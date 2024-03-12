package com.example.application.views.editmenu;

import com.example.application.data.Shawarma;
import com.example.application.security.AuthenticatedUser;
import com.example.application.services.BalanceService;
import com.example.application.services.ShawarmaService;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import jakarta.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

@PageTitle("Edit Menu")
@Route(value = "edit_menu")
@RolesAllowed("ADMIN")
@Uses(Icon.class)
public class EditMenuView extends Composite<VerticalLayout> {

    private EditForm editForm;
    private Grid basicGrid;
    Paragraph balanceText;

    public EditMenuView(AuthenticatedUser authenticatedUser) {
        HorizontalLayout layoutRow = new HorizontalLayout();
        HorizontalLayout layoutRow2 = new HorizontalLayout();
        HorizontalLayout layoutRow4 = new HorizontalLayout();
        Button buttonSecondary = new Button();
        Avatar avatar = new Avatar();
        HorizontalLayout layoutRow3 = new HorizontalLayout();
        balanceText = new Paragraph();
        Button buttonPrimary = new Button();
        Button buttonPrimary1 = new Button();
        basicGrid = new Grid(Shawarma.class);
        editForm = new EditForm(); 

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");

        layoutRow2.addClassName(Gap.MEDIUM);
        layoutRow2.setWidth("100%");
        layoutRow2.setHeight("min-content");
        layoutRow2.setAlignItems(Alignment.CENTER);
        layoutRow2.setJustifyContentMode(JustifyContentMode.END);

        buttonSecondary.setText("Выйти");
        buttonSecondary.addClickListener(e -> authenticatedUser.logout());
        buttonSecondary.setWidth("min-content");

        avatar.setName(authenticatedUser.get().get().getUsername());

        layoutRow3.setWidthFull();
        getContent().setFlexGrow(1.0, layoutRow3);
        layoutRow3.addClassName(Gap.MEDIUM);
        layoutRow3.setWidth("100%");
        layoutRow3.getStyle().set("flex-grow", "1");

        buttonPrimary1.setText("Рассчитать выручку");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary1);
        buttonPrimary1.setWidth("min-content");
        buttonPrimary1.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary1.addClickListener(e -> updateBalance());

        balanceText.setText("Выручка ещё не была рассчитана");
        balanceText.setWidth("100%");
        balanceText.getStyle().set("font-size", "var(--lumo-font-size-m)");

        buttonPrimary.setText("Добавить шаурму");
        layoutRow3.setAlignSelf(FlexComponent.Alignment.CENTER, buttonPrimary);
        buttonPrimary.setWidth("min-content");
        buttonPrimary.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonPrimary.addClickListener(e -> addShawarma());

        getContent().setAlignSelf(FlexComponent.Alignment.CENTER, basicGrid);
        basicGrid.setWidth("100%");
        basicGrid.setHeight("600px");
        basicGrid.removeAllColumns();
        basicGrid.addColumn("name");
        basicGrid.addColumn("price");
        basicGrid.addColumn("time");
        basicGrid.addColumn("count");
        setGridSampleData(basicGrid);
        basicGrid.getStyle().set("flex-grow", "0");
        basicGrid.asSingleSelect().addValueChangeListener(e -> editShawarma((Shawarma) e.getValue()));

        getContent().add(layoutRow);
        layoutRow.add(layoutRow2);
        layoutRow2.add(buttonSecondary);
        layoutRow2.add(avatar);

        getContent().add(layoutRow3);
        layoutRow3.add(buttonPrimary1);
        layoutRow3.add(balanceText);
        layoutRow3.add(buttonPrimary);

        editForm.setWidth("min-content");
        editForm.addSaveListener(this::saveShawarma);
        editForm.addDeleteListener(this::deleteShawarma);
        editForm.addCloseListener(e -> closeEditor());

        layoutRow4.setWidthFull();
        layoutRow4.add(basicGrid);
        layoutRow4.add(editForm);
        getContent().add(layoutRow4);

        /// Initial State Setup
        closeEditor();
    }

    private void updateBalance() {
        balanceText.setText("Выручка: " + balanceService.get((long) 1).get().getBalance() + "₽");
    }

    private void deleteShawarma(EditForm.DeleteEvent event) {
        shawarmaService.deleteShawarma(event.getShawarma());
        updateGrid();
        closeEditor();
    }
    
    private void saveShawarma(EditForm.SaveEvent event) {
        shawarmaService.saveShawarma(event.getShawarma());
        updateGrid();
        closeEditor();
    }

    private void updateGrid() {
        basicGrid.setItems(shawarmaService.getAll());
    }

    private void addShawarma() {
        basicGrid.asSingleSelect().clear();
        editShawarma(new Shawarma());
    }
    
    private void editShawarma(Shawarma shawarma) {
        if (shawarma == null) {
            closeEditor();
        } else {
            editForm.setShawarma(shawarma);
            editForm.setVisible(true);
            addClassName("editor");
        }
    }

    private void closeEditor() {
        editForm.setShawarma(null);
        editForm.setVisible(false);
        removeClassName("editor");
    }

    private void setGridSampleData(Grid grid) {
        grid.setItems(query -> shawarmaService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)))
                .stream());
    }

    @Autowired()
    private ShawarmaService shawarmaService;
    @Autowired()
    private BalanceService balanceService;
}
