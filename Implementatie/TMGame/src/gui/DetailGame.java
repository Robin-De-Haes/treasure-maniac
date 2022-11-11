/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import domein.Chamber;
import domein.GameProcesses;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;

/**
 * Small panel containing the round and score of the hero has reached.
 *
 * @author Robin
 */
public class DetailGame extends Pane {

    private Label scoreLbl, roundLbl;

    /**
     * Default constructor for DetailGame.
     */
    public DetailGame() {
        getStylesheets().add("css/UI.css");
        getStyleClass().add("detailgame");
        scoreLbl = new Label();
        roundLbl = new Label();
        double width = 300.0;
        double height = 50.0;
        double padding = 25;

        setMinWidth(width);
        setMinHeight(2 * height + padding);
        setMaxWidth(width);
        setMaxHeight(height * 2 + padding);

        roundLbl.setMinWidth(width);
        roundLbl.setMinHeight(height);
        roundLbl.setMaxWidth(width);
        roundLbl.setMaxHeight(height);
        roundLbl.setAlignment(Pos.CENTER);

        scoreLbl.setMinWidth(width);
        scoreLbl.setMinHeight(height);
        scoreLbl.setMaxWidth(width);
        scoreLbl.setMaxHeight(height);
        scoreLbl.setAlignment(Pos.CENTER);

        roundLbl.relocate(0, padding / 3.0);
        scoreLbl.relocate(0, height + 2 * padding / 3.0);

        getChildren().addAll(roundLbl, scoreLbl);
    }

    /**
     * Updating DetailGame to the latest changes in the game.
     *
     * @param score the score the hero has reached
     */
    public void update(String score) {
        if (GameProcesses.isMaxRound()) {
            roundLbl.setText("FINAL ROUND");
        } else {
            roundLbl.setText("Round: " + Chamber.getRound());
        }
        scoreLbl.setText("Score: " + score);
    }
}
