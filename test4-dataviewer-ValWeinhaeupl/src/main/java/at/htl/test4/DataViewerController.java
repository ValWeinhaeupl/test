package at.htl.test4;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.converter.FormatStringConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataViewerController {
    Stage stage;

    @FXML
    private Label lName;

    @FXML
    private TextField tfFilename;

    @FXML
    private Button btSelectFile;

    @FXML
    private ChoiceBox<String> cbGemeinde;

    @FXML
    private ProgressBar pbProgress;


    @FXML
    private AreaChart<?, ?> chart;

    @FXML
    private Label lStatus;

    Service importservice;

    @FXML
    public void initialize() {
        // TODO Ihren Namen hier einsetzen
        lName.setText("<<Ihr Name>>");

        chart.getXAxis().setLabel("Jahr");
        ((NumberAxis)chart.getXAxis()).setTickLabelFormatter(new FormatStringConverter<>(new DecimalFormat("0000")));
        chart.getYAxis().setLabel("Bewohner");
    }


    @FXML
    void importAction(ActionEvent event) throws IOException {
        // TODO: Aufgabe 1 - Befüllen von ChoiceBox mit den möglichen Jahren und den vorhandenen Gemeinden

        importservice = new Service() {
            @Override
            protected Task createTask() {
                Task task = new ImportTask(tfFilename.getText(), cbGemeinde);
                return task;
            }
        };
    pbProgress.progressProperty().bind(importservice.progressProperty());

    importservice.restart();
        }




        // statusAction("Importing...");
        // statusOk("Gemeindeliste wurde aktualisiert!");
        // statusError("Fehler beim Auslesen der Daten: " + e);

        // TODO: Aufgabe 5 - Ergänzen des Eintrags "Gesamt"



    @FXML
    void redrawAction(ActionEvent event) throws IOException {
        // TODO: Aufgabe 2 - Aufbereiten der Datensätze für das Chart
        // TODO: Aufgabe 5 - Gesamteinwohner für OÖ anzeigen

        // statusAction("Aufbereitung der Daten...");
        // statusOk("Chart aktualisiert!");

        chart.getData().clear();

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName(cbGemeinde.getSelectionModel().getSelectedItem());


        Files.lines(Path.of(tfFilename.getText()))
                .skip(1)
                .map(s -> s.split(";"))
                .filter(s -> s[2].equals(cbGemeinde.getSelectionModel().getSelectedItem()))

                .forEach(s -> {
                    System.out.println(s[4]);
                    dataSeries1.getData().add(new XYChart.Data(Integer.valueOf(s[3]) , Integer.valueOf(s[4])));

                });


        // Beispiel-Sourcecode zum Demonstrieren der Funktionsweise



        chart.getData().add(dataSeries1);

        addChartToolTips();
    }

    @FXML
    void dbImportAction(ActionEvent event) {
        // TODO: Aufgabe 4 - Datenbankimport hier anstoßen. Die Implementierung soll in einer eigenen Klasse stattfinden

        // statusAction("Datenbank-Import läuft....");
        // statusOk("DB-Import abgeschlossen");
    }

    void addChartToolTips() {







        // TODO: Aufgabe 3 - umschreiben von For-Schleife auf Streams
        for (int s=0; s<chart.getData().size(); s++) {
            XYChart.Series series = chart.getData().get(s);

            for (int d=0; d<series.getData().size(); d++) {
                XYChart.Data data = (XYChart.Data)series.getData().get(d);

                Tooltip.install(data.getNode(), new Tooltip("Jahr " + data.getXValue() + ": " + data.getYValue() + " Einwohner"));
                data.getNode().setOnMouseEntered(mouseEvent -> data.getNode().getStyleClass().add("onHover"));
                data.getNode().setOnMouseExited(mouseEvent -> data.getNode().getStyleClass().remove("onHover"));
            }
        }

    }


    //<editor-fold desc="//Ready methods.... no need to change...">
    @FXML
    void selectFileAction(ActionEvent event) {
        FileChooser fc = new FileChooser();
        fc.setTitle("CSV-Datei auswählen...");
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV-Datei", "*.csv"));
        fc.setInitialDirectory(new File("."));
        File file = fc.showOpenDialog(stage);
        if (file != null)
            tfFilename.setText(file.getAbsolutePath());
    }
    //</editor-fold>


    //<editor-fold desc="// Helper Methods for Status-Messages">
    void statusOk(String text) {
        lStatus.setTextFill(Color.GREEN);
        lStatus.setText(text);
    }

    void statusError(String text) {
        lStatus.setTextFill(Color.RED);
        lStatus.setText(text);
    }

    void statusAction(String text) {
        lStatus.setTextFill(Color.BLACK);
        lStatus.setText(text);
    }
    //</editor-fold>
}