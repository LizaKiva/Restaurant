package com.example.application.views.editmenu;

import com.example.application.data.Shawarma;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;
import com.vaadin.flow.shared.Registration;

public class EditForm extends FormLayout {

    Shawarma shawarma;
    private Binder<Shawarma> binder = new BeanValidationBinder<>(Shawarma.class);
   
    TextField name = new TextField("Name");
    IntegerField price = new IntegerField("Price");
    IntegerField time = new IntegerField("Time");
    IntegerField count = new IntegerField("Count");

    Button save = new Button("Сохранить");
    Button delete = new Button("Удалить");
    Button close = new Button("Отмена");

    public EditForm() {
        addClassName("editor");
        
        /// brokie :(
        //binder.bindInstanceFields(this);

        binder.forField(name).bind(Shawarma::getName, Shawarma::setName);
        binder.forField(price).bind(Shawarma::getPrice, Shawarma::setPrice);
        binder.forField(time).bind(Shawarma::getTime, Shawarma::setTime);
        binder.forField(count).bind(Shawarma::getCount, Shawarma::setCount);

        add(
            name,
            price,
            time,
            count,
            createButtonsLayout());
    }

    public void setShawarma(Shawarma shawarma) {
        this.shawarma = shawarma;
        binder.readBean(shawarma);
    }
    
    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY); 
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> ValidateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, shawarma)));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        save.addClickShortcut(Key.ENTER); 
        close.addClickShortcut(Key.ESCAPE);

        return new HorizontalLayout(save, delete, close); 
    }

    private void ValidateAndSave() {
        try {
            binder.writeBean(shawarma);
            fireEvent(new SaveEvent(this, shawarma));
        } catch (ValidationException e) {
            e.printStackTrace();
        }
    }

    // Events
    public static abstract class EditFormEvent extends ComponentEvent<EditForm> {
        private Shawarma shawarma;

        protected EditFormEvent(EditForm source, Shawarma shawarma) { 
            super(source, false);
            this.shawarma = shawarma;
        }

        public Shawarma getShawarma() {
            return shawarma;
        }
    }

    public static class SaveEvent extends EditFormEvent {
        SaveEvent(EditForm source, Shawarma shawarma) {
            super(source, shawarma);
        }
    }

    public static class DeleteEvent extends EditFormEvent {
        DeleteEvent(EditForm source, Shawarma contact) {
            super(source, contact);
        }
    }

    public static class CloseEvent extends EditFormEvent {
        CloseEvent(EditForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { 
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
