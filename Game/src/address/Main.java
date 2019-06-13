package address;


import address.models.Score;
import address.models.ScoreWraper;
import address.views.Controller;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.prefs.Preferences;

/**
 * 
 * @author Kacper Ratajczak
 *
 */

public class Main extends Application {
    private Stage primaryStage;
    private ObservableList<Score> scoreData = FXCollections.observableArrayList();
    private boolean scoreFileFound = true;
    private Scene scene1;
    @FXML
    private Button str;
    @FXML
    private Button credits;
    
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Puzzle");
        primaryStage.getIcons().add(new Image("http://icons.iconarchive.com/icons/giannis-zographos/spanish-football-club/16/FC-Barcelona-icon.png"));
        
        
        
        FXMLLoader loader1 = new FXMLLoader();
        loader1.setLocation(Main.class.getResource("./views/MainScreenView.fxml"));
        AnchorPane rtLayout = loader1.load();
        
        Scene menuScene = new Scene(rtLayout);
        
        primaryStage.setScene(menuScene);
        primaryStage.setResizable(false);
        primaryStage.show();
        
        str = (Button) menuScene.lookup("#str");
        str.setOnAction(e -> showMainLayout());
        
        
        
        
    }

    private void showMainLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("./views/View.fxml"));
            AnchorPane rootLayout = loader.load();
            Controller controller = loader.getController();
            int w = 600 +(controller.getTilesInArow()+1)*5;
            rootLayout.setMaxWidth(w);
            rootLayout.setMinWidth(w);
            rootLayout.setPrefWidth(w);

            rootLayout.setMaxHeight(45+w);
            rootLayout.setMinHeight(45+w);
            rootLayout.setPrefHeight(45+w);

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
            System.out.println(primaryStage.getWidth());

            controller.setMainApp( this );
            if(controller.getImageName().equals("camp.jpg")) primaryStage.setTitle("Camp Nou Puzzle");
            
            

        }catch(IOException e) {
            e.printStackTrace();
        }

        
        File file = getScoreFilePath();
        if (file != null) {
            loadScoreDataFromFile(file);
        }
    }

    public File getScoreFilePath() {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        String filePath = prefs.get("filePath", null);
        if (filePath != null) {
            return new File(filePath);
        } else {
            return null;
        }
    }

    private void setProductFilePath(File file) {
        Preferences prefs = Preferences.userNodeForPackage(Main.class);
        if (file != null) {
            prefs.put("filePath", file.getPath());
        } else {
            prefs.remove("filePath");
        }
    }

    private void loadScoreDataFromFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ScoreWraper.class);
            Unmarshaller um = context.createUnmarshaller();

            // Reading XML from the file and unmarshalling.
            ScoreWraper wrapper = (ScoreWraper) um.unmarshal(file);

            scoreData.clear();
            scoreData.addAll(wrapper.getScores());

            // Save the file path to the registry.
            setProductFilePath(file);

        } catch (Exception e) { // catches ANY exception
            scoreFileFound = false;
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data");
            alert.setContentText("Could not load data from file:\n" + file.getPath());

            alert.showAndWait();
        }
    }

    public void saveProductDataToFile(File file) {
        try {
            JAXBContext context = JAXBContext.newInstance(ScoreWraper.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Wrapping our product data.
            ScoreWraper wrapper = new ScoreWraper();
            wrapper.setScores(scoreData);

            // Marshalling and saving XML to the file.
            m.marshal(wrapper, file);

            // Save the file path to the registry.
            setProductFilePath(file);
        } catch (Exception e) { // catches ANY exception
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data");
            alert.setContentText("Could not save data to file:\n" + file.getPath());

            alert.showAndWait();
        }
        
        
    }
    
    
    public void showScoreTable() {
        Platform.runLater(() -> {
            primaryStage.setMaxWidth(primaryStage.getWidth()+200);
            primaryStage.setWidth(primaryStage.getWidth()+200);
        });

    }
    

    public void hideScoreTable() {
        Platform.runLater(() -> primaryStage.setWidth(primaryStage.getWidth()-200));

    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Score> getScoreData() {
        return scoreData;
    }

    public boolean isScoreFileFound() {
        return scoreFileFound;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
