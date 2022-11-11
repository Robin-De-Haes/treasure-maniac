/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import com.javafx.experiments.scenicview.ScenicView;
import domein.DomeinController;
import domein.Hero;
import domein.Treasure;
import java.net.MalformedURLException;
import java.util.List;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * Launchpoint of the game.
 *
 * @author Robin
 */
public class Main extends Application {

    private MainPanel main;
    private static Image logo = new Image(Main.class.getResourceAsStream("/images/icons/Logo_icon.png"));
    private DomeinController controller;

    @Override
    public void start(final Stage stage) throws MalformedURLException {
        controller = new DomeinController();
        main = new MainPanel(controller);

        Scene gameScene = new Scene(main);

        stage.setScene(gameScene);
        stage.setTitle("Treasure Maniac");
        stage.setFullScreen(false);
        stage.setResizable(false);
        stage.setWidth(1000);
        stage.setHeight(720);
        stage.getIcons().add(logo);
        //ScenicView.show(gameScene);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {           //Wordt niet opgeroepen bij replayen
            @Override
            public void handle(WindowEvent t) {
                Hero hero = main.getHero();
                main.reset();                       //Chamber-round wordt ook reset

                if (hero != null && !main.getFighting() && hero.getRound() > 0 && !main.getRestarted() && !(hero.finishedGame() || !hero.getAlive())) //Spel is reeds bezig en niet net opnieuw gestart
                {
                    if (main.getChestWasOpened()) {
                        hero.setRound(hero.getRound() + 1);        //Zodat je niet kan valsspelen door weg te gaan als schat je niet aantstaat
                    }
                    controller.addHero(main.getHero());     //Hero opslaan
                    controller.removeAllTreasuresFromHero(hero);            //Voor als er dropoperaties hebben plaatsgevonden

                    List<Treasure> treasures = main.getHero().getInventory().giveTreasures();  //Hier wordt niet geraakt
                    for (Treasure treasure : treasures) {
                        controller.addTreasureToHero(main.getHero(), treasure);
                        //Schatten hier opslaan
                    }
                }
            }
        });

        stage.show();

        //ScenicView.show(gameScene);
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
