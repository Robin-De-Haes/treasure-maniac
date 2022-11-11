package gui;

import domein.DomeinController;
import domein.Treasure;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;

/**
 * Panel containing detailed information about a treasure, excluding the
 * avatar-image
 *
 * @author Robin De Haes
 */
public class DetailTreasure extends GridPane {

    private DomeinController controller;
    private ImageView ivPower, ivDefense, ivSpeed, ivAwareness, ivValue;

    private Label lblDetailName, lblDetailDescription, lblDetailPower, lblDetailDefense, lblDetailSpeed, lblDetailAwareness, lblDetailValue;
    private static Image iPower = new Image(Main.class.getResourceAsStream("/images/icons/Sword.png"));
    private static Image iDefense = new Image(Main.class.getResourceAsStream("/images/icons/Shield.png"));
    private static Image iSpeed = new Image(Main.class.getResourceAsStream("/images/icons/Speed.png"));
    private static Image iAwareness = new Image(Main.class.getResourceAsStream("/images/icons/Awareness.png"));
    private static Image iValue = new Image(Main.class.getResourceAsStream("/images/icons/Value.png"));
    //Alle images zijn static zodat ze slecht een keer moeten gecreëerd worden en niet voor elk panel opnieuw
    //Elk panel krijg gewoon een nieuwe view op dezelfde image

    /**
     * Constructor for DetailTreasure
     *
     * @param controller the DomeinController that will be used for
     * communication purposes
     */
    public DetailTreasure(DomeinController controller) {
        this.controller = controller;
        //Vaste breedte meegeven, zodat scherm niet onnodig meeschuift bij invoeren te lang waarden
        /*this.setMinWidth(250);
         this.setMaxWidth(250);*/
        //Afzonderlijke elementen hebben nu een vaste locatie en breedte
        buildPanel();
    }

    /**
     * The method for building the actual DetailTreasurePanel. Is used in
     * {@link #DetailTreasure(domein.DomeinController) DetailTreasure-constructor}.
     */
    private void buildPanel() {
        //View op de icoontjes creëren en correcte grootte instellen
        ivPower = new ImageView(iPower);
        ivPower.setFitHeight(24);
        ivPower.setFitWidth(24);

        ivDefense = new ImageView(iDefense);
        ivDefense.setFitHeight(24);
        ivDefense.setFitWidth(24);

        ivSpeed = new ImageView(iSpeed);
        ivSpeed.setFitHeight(24);
        ivSpeed.setFitWidth(24);

        ivAwareness = new ImageView(iAwareness);
        ivAwareness.setFitHeight(24);
        ivAwareness.setFitWidth(24);

        ivValue = new ImageView(iValue);
        ivValue.setFitHeight(24);
        ivValue.setFitWidth(24);

        //Naam en description van de schat
        lblDetailName = new Label("Name");
        lblDetailName.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        lblDetailName.setMaxWidth(190);
        lblDetailName.setMinWidth(190);
        lblDetailDescription = new Label("\"Description\"");
        lblDetailDescription.setFont(Font.font("Arial", FontPosture.ITALIC, 16));
        lblDetailDescription.setMaxWidth(250);
        lblDetailDescription.setMinWidth(250);

        //Icoontjes toevoegen aan panel samen met de bijhorende beginwaarde 0
        lblDetailPower = new Label("-", ivPower);
        lblDetailPower.setMaxWidth(50);
        lblDetailPower.setMinWidth(50);
        lblDetailDefense = new Label("-", ivDefense);
        lblDetailDefense.setMaxWidth(50);
        lblDetailDefense.setMinWidth(50);
        lblDetailSpeed = new Label("-", ivSpeed);
        lblDetailSpeed.setMaxWidth(50);
        lblDetailSpeed.setMinWidth(50);
        lblDetailAwareness = new Label("-", ivAwareness);
        lblDetailAwareness.setMaxWidth(50);
        lblDetailAwareness.setMinWidth(50);
        lblDetailValue = new Label("-", ivValue);
        lblDetailValue.setMaxWidth(75);
        lblDetailValue.setMinWidth(75);

        //Padding
        HBox padding1 = new HBox();
        padding1.setMinWidth(20);
        HBox padding2 = new HBox();
        padding2.setMinWidth(40);

        //Toevoegen van alle labels en padding
        add(lblDetailName, 0, 0, 6, 1);
        add(lblDetailDescription, 0, 1, 6, 2);
        add(lblDetailPower, 0, 3);
        add(lblDetailDefense, 0, 4);
        add(padding1, 1, 3, 1, 2);
        add(lblDetailSpeed, 2, 3);
        add(lblDetailAwareness, 2, 4);
        add(padding2, 3, 4, 1, 2);
        add(lblDetailValue, 5, 3);
    }

    /**
     * Set the text of the name-label
     *
     * @param lblDetailName new text in name-label
     */
    public void setName(String lblDetailName) {
        this.lblDetailName.setText(lblDetailName);
    }

    /**
     * Set the text of the description-label
     *
     * @param lblDetailDescription new text in description-label
     */
    public void setDescription(String lblDetailDescription) {
        this.lblDetailDescription.setText("\"" + format(lblDetailDescription) + "\"");
    }

    /**
     * Set the text of the power-label
     *
     * @param lblDetailPower new text in power-label
     */
    public void setPower(String lblDetailPower) {
        this.lblDetailPower.setText("" + lblDetailPower);
    }

    /**
     * Set the text of the defense-label
     *
     * @param lblDetailDefense new text in defense-label
     */
    public void setDefense(String lblDetailDefense) {
        this.lblDetailDefense.setText(lblDetailDefense + "");
    }

    /**
     * Set the text of the speed-label
     *
     * @param lblDetailSpeed new text in speed-label
     */
    public void setSpeed(String lblDetailSpeed) {
        this.lblDetailSpeed.setText("" + lblDetailSpeed);
    }

    /**
     * Set the text of the awareness-label
     *
     * @param lblDetailAwareness new text in awareness-label
     */
    public void setAwareness(String lblDetailAwareness) {
        this.lblDetailAwareness.setText("" + lblDetailAwareness);
    }

    /**
     * Set the text of the value-label
     *
     * @param lblDetailValue new text in value-label
     */
    public void setValue(String lblDetailValue) {
        this.lblDetailValue.setText(lblDetailValue);
    }

    /**
     * Formats a description by starting on a new line when needed.
     *
     * @param description sentence that needs to be formatted
     * @return the formatted description
     */
    private String format(String description) {
        int line = Treasure.getMAX_DESCRIPTION() / 2;
        String temp = description;
        String firstHalf;
        String secondHalf;
        if (temp.length() > line) {
            temp = description.substring(0, line);
            int newLine = temp.lastIndexOf(" ");
            if (newLine < 0) {
                firstHalf = description.substring(0, temp.length() - 1) + "-\n";
                secondHalf = " " + description.substring(temp.length() - 1);
            } else {
                firstHalf = description.substring(0, newLine) + "\n";
                secondHalf = " " + description.substring(newLine + 1);
            }
            String extra = "";
            if (secondHalf.length() > line) {
                temp = secondHalf.substring(0, line);
                newLine = temp.lastIndexOf(" ");
                if (newLine == 0) {         //Er staat vooraan een spatie
                    secondHalf = secondHalf.substring(0, temp.length() - 1) + "-\n";
                    extra = " " + secondHalf.substring(temp.length() - 3,secondHalf.length()-2);            //- en newlinecharacter niet meetellen
                } else {
                    secondHalf = secondHalf.substring(0, newLine) + "\n";
                    extra = " " + secondHalf.substring(newLine + 1,secondHalf.length()-2);
                }

            }
            return firstHalf + secondHalf + extra;
        } else {
            return description;
        }
    }
}
