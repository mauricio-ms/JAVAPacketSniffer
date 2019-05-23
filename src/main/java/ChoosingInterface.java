
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.ArrayList;

public class ChoosingInterface extends Application {
    private Stage window;
    ListView<String> networkInterfacesList;
    @Override
    public void start(Stage primaryStage) throws Exception{
        window=primaryStage;
        primaryStage.setTitle("JAVA Packet Sniffer");
        BorderPane borderPane=new BorderPane();
        borderPane.setTop(addTopVBox());
        borderPane.setCenter(addListView());
        borderPane.setBottom(addBottomPane());
        primaryStage.setScene(new Scene(borderPane, 500, 500));
        primaryStage.show();
    }

    public VBox addTopVBox(){
        VBox topVBox=new VBox();topVBox.setSpacing(8);
        Label welcomeLabel=new Label("Welcome to The JAVA Packet Sniffer !");
        welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        welcomeLabel.setTextFill(Color.web("#FFFFFF"));
        Label label=new Label("Choose one of your available network interfaces :");
        label.setTextFill(Color.web("#FFFFFF"));
        topVBox.setMargin(welcomeLabel,new Insets(10,0,0,80));
        topVBox.setMargin(label,new Insets(0,0,10,10));
        topVBox.setStyle("-fx-background-color: #336699;");
        topVBox.getChildren().addAll(welcomeLabel,label);
        return topVBox;
    }
    public VBox addListView(){
        VBox centerVBox=new VBox();
        ObservableList<String> list=FXCollections.<String>observableArrayList(JNetPCapWork.fetchAvailableInterfaces());
        networkInterfacesList=new ListView<>(list);
        networkInterfacesList.setOrientation(Orientation.VERTICAL);
        centerVBox.setStyle("-fx-background-color: #336699;");
        centerVBox.setMargin(networkInterfacesList,new Insets(0,10,0,10));
        centerVBox.getChildren().add(networkInterfacesList);
        return centerVBox;
    }
    private HBox addBottomPane() {
        HBox bottomHBox=new HBox();
        bottomHBox.setAlignment(Pos.BOTTOM_RIGHT);
        Button okButton=new Button("OK");Button cancelButton=new Button("Cancel");
        bottomHBox.setPadding(new Insets(15, 12, 15, 12));
        bottomHBox.setSpacing(10);
        bottomHBox.setStyle("-fx-background-color: #336699;");
        okButton.setPrefSize(100, 20);cancelButton.setPrefSize(100, 20);
        cancelButton.setOnAction(event -> {window.close();});
        okButton.setOnAction(event -> {
            try {
                moveToAnotherWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        bottomHBox.getChildren().addAll(okButton,cancelButton);
        return bottomHBox;
    }

    private void moveToAnotherWindow() throws Exception {
        int chosenIndex=networkInterfacesList.getSelectionModel().getSelectedIndex();
        if (chosenIndex == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR, "Please select a network interface .", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }else {
            PacketSniffingWindow packetSniffingWindow=new PacketSniffingWindow(chosenIndex);
            window.close();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
