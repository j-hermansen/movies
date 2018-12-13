package no.hiof.jonathah.oblig5;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;
import no.hiof.jonathah.oblig5.controller.RedigerFilmDialogController;
import no.hiof.jonathah.oblig5.model.DataHaandtering;
import no.hiof.jonathah.oblig5.model.Film;

import java.io.IOException;
import java.util.Comparator;

/**
 * MainJavaFX klassen for kjøring av program med vinduer (scener)
 */

public class MainJavaFX extends Application implements Comparator<Film> {
    private static ObservableList<Film> filmListeData = FXCollections.observableArrayList();
    private Stage primaryStage;
    public static MainJavaFX mainJavaFX;

    public MainJavaFX() {

        // Setter den statiske mainJavaFX objektet til vår egen instanse, slik at den blir tilgjengelig fra alle andre klasser
        MainJavaFX.mainJavaFX = this;

    }

    @Override
    public void start(Stage primaryStage) {

        // Legger til filmer i ListView når programmet startes
        DataHaandtering.hente();

        try {

            this.primaryStage = primaryStage;

            // Setter tittelen til vinduet
            primaryStage.setTitle("MovieTime - Superintuitiv applikasjon for håndtering av mediainformasjon");

            // Lager et objekt fxmlLoader og instansierer det
            FXMLLoader fxmlLoader = new FXMLLoader();
            // Setter hvilken FXML fil vi skal laste inn
            fxmlLoader.setLocation(getClass().getResource("view/FilmOversikt.fxml"));
            // Laster inn FXML innhold og legger det i et parent objekt
            Parent filmOversiktLayout = fxmlLoader.load();

            // Setter hovedscenen, og legger filmOversiktLayoutet inn i den, setter også bredden og høyden på vinduet
            Scene hovedScene = new Scene(filmOversiktLayout, 1000, 500);

            // Setter hovedscenen
            primaryStage.setScene(hovedScene);

            // Viser hele vinduet
            primaryStage.show();
        }
        catch (IOException | IllegalStateException exception) {
            visAlert(exception.getMessage());
        }
    }

    @Override
    public void stop() {

        // Skriver nylig lagde filmer til .csv fil
        DataHaandtering.skrive();
    }

    public boolean visRedigerFilmDialog(Film film) {
        try {
            // Oppretter og instansierer fxmlLoader
            FXMLLoader fxmlLoader = new FXMLLoader();

            // Setter hvilken FXML fil vi skal laste inn
            fxmlLoader.setLocation(getClass().getResource("view/RedigerFilmDialog.fxml"));
            // Laster inn FXML innhold og legger det i et parent objekt
            Parent dialogLayout = fxmlLoader.load();

            // Oppretter og instansierer vinduet
            Stage dialogStage = new Stage();
            // Setter tittelen
            dialogStage.setTitle("Rediger film");
            // Setter modality til WINDOW_MODAL, dette gjør at vinduet må lukkes før man kan gjøre noe mer i "hovedvinduet"
            dialogStage.initModality(Modality.WINDOW_MODAL);
            // Setter eier til å være hovedvinduet ("hovedstagen")
            dialogStage.initOwner(primaryStage);

            // Lager en ny scene og legger til layoutet fra fxml-fila
            Scene dialogScene = new Scene(dialogLayout);
            // Legger scenen til vinduet
            dialogStage.setScene(dialogScene);

            // Henter ut kontrolleren som er knyttet til fxml'en
            RedigerFilmDialogController redigerController = fxmlLoader.getController();
            // Setter vinduet, slik at kontrolleren har mulighet til å lukkke det når den er ferdig (knapper trykket)
            redigerController.setDialogStage(dialogStage);
            // Setter hvilken film som skal redigeres
            redigerController.setFilmSomSkalRedigeres(film);

            // Viser dialogvinduet, og venter med å gå videre til det er lukket
            dialogStage.showAndWait();

            // Dialogvinduet er blitt lukket, og vi undersøker hvordan vi ble avsluttet (om OK ble valgt, eller om vi avsluttet på annnen måte)
            return redigerController.erOkValgt();
        }
        catch (IOException | IllegalStateException exception) {
            visAlert(exception.getMessage());
            return false;
        }
    }

    private void visAlert(String melding) {
        Alert newAlert = new Alert(Alert.AlertType.ERROR);
        newAlert.setTitle("Feil");
        newAlert.setHeaderText(null);
        newAlert.setContentText("Noe gikk feil! " + melding);

        newAlert.showAndWait();
    }


    // Legger til en film i ObservableList
    public static void leggTilFilm(Film film) {
        filmListeData.add(film);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static ObservableList<Film> getFilmData() {

        return filmListeData;

    }

    @Override
    public int compare(Film o1, Film o2) {
        return o1.toString().compareTo(o2.toString());
    }

}
