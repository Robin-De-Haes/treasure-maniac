/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Field for buttonbased input of integers.
 *
 * @author Robin
 */
public class NumberField extends GridPane {

    private int number;
    private TextField txf;
    private Button groter, kleiner;

    /**
     * Default constructor for a numberfield.
     */
    public NumberField() {
        initializeComponents();
    }

    /**
     * Builds a numberfield. Every numberfield consists of 1 button for
     * decreasing, a textfield showing the number and 1 button for increasing.
     */
    private void initializeComponents() {
        number = 0;
        txf = new TextField(String.valueOf(number));
        txf.setEditable(false);
        groter = new Button(">");
        kleiner = new Button("<");

        getStylesheets().add("css/UI.css");
        groter.getStyleClass().add("panel-button");
        kleiner.getStyleClass().add("panel-button");

        add(kleiner, 0, 0);
        add(txf, 1, 0, 2, 1);
        add(groter, 3, 0);

        txf.setMaxWidth(100);
        txf.setMinWidth(100);
        txf.setMaxHeight(40);
        txf.setMinHeight(40);
        txf.setAlignment(Pos.CENTER);

        kleiner.setMaxHeight(40);
        kleiner.setMinHeight(40);
        kleiner.setMaxWidth(50);
        kleiner.setMinWidth(50);
        kleiner.setAlignment(Pos.CENTER);

        groter.setMaxHeight(40);
        groter.setMinHeight(40);
        groter.setMaxWidth(50);
        groter.setMinWidth(50);
        groter.setAlignment(Pos.CENTER);

        groter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //Controle op max en min gebeurt in HeroPanel zelf
                number++;
                setNumber(number);
            }
        }
        );

        kleiner.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                number--;
                setNumber(number);
            }
        }
        );
    }

    /**
     * Give the number.
     *
     * @return the number shown in the NumberField
     */
    public int getNumber() {
        return Integer.parseInt(txf.getText());
    }

    /**
     * Set the number inside the NumberField
     */
    public void setNumber(int number) {
        this.number = number;
        txf.setText(String.valueOf(number));
    }

    /**
     * Give the textProperty of the TextField-part of the NumberField
     *
     * @return the textProperty of the TextField-part of the NumberField
     */
    public StringProperty textProperty() {
        return txf.textProperty();
    }
}
