package no.hiof.jonathah.oblig5.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.util.Callback;
import no.hiof.jonathah.oblig5.MainJavaFX;
import no.hiof.jonathah.oblig5.model.Film;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.Optional;

public class FilmOversiktController {
    // Alle feltvariablene for komponenter definert i tilhørende .fxml
    @FXML
    private ListView<Film> filmListView;
    @FXML
    private Text tittelText;
    @FXML
    private TextField utgivelsesdatoTextField, spilletidTextField;
    @FXML
    private TextArea beskrivelseTextArea;
    @FXML
    private Button redigerButton;
    @FXML
    private Button nyButton;
    @FXML
    private ImageView filmBilde;
    @FXML
    private String tittelSynkende;
    @FXML
    private ChoiceBox sorteringsListe;

    private String valgtSortering;




    // Feltvariabler som tilhører FilmOversiktController, som ikke finnes i FXML fil
    private MainJavaFX mainApp;


    // Metode initialize som blir kalt når vi laster inn den tilhørende fxml'en
    // Den blir teknisk sett kjørt etter kontruktøren, og det er garantert at alle komponenter er opprettet
    // i.e. alle knapper, tekstområder etc. slik at vi kan gjøre noe med de
    @FXML
    private void initialize() {


        // Setter vår lokale referanse, bare så vi slipper å skrive "MainJavaFX.mainJavaFX" hver gang vi trenger den
        mainApp = MainJavaFX.mainJavaFX;

        // Setter ListViewet vårt til å inneholde det vi har i vår ObservableList
        filmListView.setItems(mainApp.getFilmData());

        filmListView.setCellFactory(new Callback<ListView<Film>, ListCell<Film>>() {
            @Override
            public ListCell<Film> call(ListView<Film> filmListView) {
                return new FilmCelle();
            }
        });

        redigerButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // Henter ut den filmen vi har valgt (altså den vi skal redigere)
                Film enFilm = filmListView.getSelectionModel().getSelectedItem();

                // Undersøker om den ikke er null (altså at det er faktisk er valgt en film)
                if (enFilm != null) {
                    // Viser det nye vinduet, og sender filmen som skal redigeres, får tilbake true/false avhengig av hvordan det gikk
                    boolean filmRedigert = mainApp.visRedigerFilmDialog(enFilm);

                    if (filmRedigert) {
                        // (liten hack for å få ListViewet til å oppdatere seg med ny tittel)
                        // Setter første elementet til valgt, før den som ble redigert
                        filmListView.getSelectionModel().selectFirst();
                        // Setter filmen som er redigert til å være valgt
                        filmListView.getSelectionModel().select(enFilm);
                        oppdaterFilmInformasjon(enFilm);
                    }
                }
                // Hvis ikke viser vi en liten alert melding om at film må velges
                else {
                    Alert alertDialog = new Alert(Alert.AlertType.INFORMATION);
                    alertDialog.setTitle("Ingen film valgt");
                    alertDialog.setHeaderText("Vennligst velg en film");
                    alertDialog.showAndWait();
                }
            }
        });

        // Setter opp at vi vil ha beskjed hver gang en film endres, slik at vi kan oppdatere grensesnittet
        filmListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Film>() {
            @Override
            public void changed(ObservableValue<? extends Film> observable, Film gammelFilm, Film nyFilm) {
                // Kaller vår egen metode for å oppdatere grensesnittet med den filmen vi har valgt
                oppdaterFilmInformasjon(nyFilm);
            }
        });

        // Setter den første filmen i lista som valgt
        filmListView.getSelectionModel().selectFirst();


        // Sortere filmer
        sorteringsListe.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                int selected = (Integer) newValue;

                // sjekker hvilke sorteringsmetode som er valgt, og sorterer deretter.
                switch (selected) {
                    case 0:
                        FXCollections.sort(MainJavaFX.getFilmData(), new Comparator<Film>() {
                            @Override
                            public int compare(Film o1, Film o2) {
                                return o1.getTittel().toLowerCase().compareTo(o2.getTittel().toLowerCase());
                            }
                        });
                        break;

                    case 1:
                        FXCollections.sort(MainJavaFX.getFilmData(), new Comparator<Film>() {
                            @Override
                            public int compare(Film o1, Film o2) {
                                return o2.getTittel().toLowerCase().compareTo(o1.getTittel().toLowerCase());
                            }
                        });
                        break;

                    case 2:
                        FXCollections.sort(MainJavaFX.getFilmData(), new Comparator<Film>() {
                            @Override
                            public int compare(Film o1, Film o2) {
                                return Integer.valueOf(o1.getUtgivelsesdato().getYear()).compareTo(o2.getUtgivelsesdato().getYear());
                            }
                        });
                        break;

                    case 3:
                        FXCollections.sort(MainJavaFX.getFilmData(), new Comparator<Film>() {
                            @Override
                            public int compare(Film o1, Film o2) {
                                return Integer.valueOf(o2.getUtgivelsesdato().getYear()).compareTo(o1.getUtgivelsesdato().getYear());
                            }
                        });
                        break;

                     default:
                         break;

                }
            }
        });

    }

    @FXML
    public void nyButtonClicked(ActionEvent actionEvent) {
        // Oppretter og instansierer et nytt filmobjekt
        Film nyFilm = new Film();

        // Setter utgivelsesdato til dagens dato
        nyFilm.setUtgivelsesdato(LocalDate.now());

        // Viser det nye vinduet, og sender objektet inn for å fylles med data, får tilbake true/false avhengig av hvordan det gikk
        boolean nyFilmLagetVellyket = mainApp.visRedigerFilmDialog(nyFilm);

        // Sjekker om den nye filmen ble laget
        if (nyFilmLagetVellyket) {
            // Henter ut lista med filmer, og legger til den nye filmen som ble laget
            mainApp.getFilmData().add(nyFilm);
            // Setter at den nye filmen er valgt
            filmListView.getSelectionModel().select(nyFilm);
        }
    }

    @FXML
    public void slettButtonClicked(ActionEvent actionEvent) {
        // Henter ut filmen som er valgt, som er den som skal slettes
        Film enFilm = filmListView.getSelectionModel().getSelectedItem();

        // Sjekker om det faktisk er valgt en film, eller om den er null
        if (enFilm != null) {
            // Viser en dialog om brukeren er helt sikker på at han ønsker å slette filmen
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Slette film");
            alert.setHeaderText(null);
            alert.setContentText("Er du sikker på at du ønsker å slette filmen " + enFilm.getTittel() + "?");

            // Viser boksen og får tilbake hvilken knapp som er trykket på
            Optional<ButtonType> result = alert.showAndWait();
            // Sjekker om OK ble trykket på
            if (result.orElse(ButtonType.OK) == ButtonType.OK){
                // Vi tømmer informasjonsfeltet (det kan være siste filmen vi sletter, slik at vi ikke har noen ny å vise)
                tomfilmInfo();
                // Velger filmen som var før i lista
                filmListView.getSelectionModel().selectPrevious();
                // Henter ut lista med filmer og tar bort den valgte filmen
                mainApp.getFilmData().remove(enFilm);
            }

            // TO DO: Slette filmen fra .csv fil

        }
    }

    private void oppdaterFilmInformasjon(Film enFilm) {
        // Sjekker om filmen vi får inn, ikke er null
        if (enFilm != null) {
            // Setter filmtittelen til titteltekstfeltet
            tittelText.setText(enFilm.getTittel());
            // Setter beskrivelsen til beskrivelsetekstfeltet
            beskrivelseTextArea.setText(enFilm.getBeskrivelse());
            // Sjekker om filmens utgivelsesdato er MIN
            // Hvis den ikke er det, legger vi utgivelsesdatoen til i tekstfeltet
            // Hvis den er MIN, setter vi tekstfeltet til å være tomt
            utgivelsesdatoTextField.setText(enFilm.getUtgivelsesdato() != LocalDate.MIN ? enFilm.getUtgivelsesdato().toString() : "");

            // Undersøker om spilletid er høyere enn 0
            // Er den det legger vi spilletiden + minutter til tekstfeltet
            // Hvis den er 0 (eller teknisk sett lavere), setter vi tekstfeltet til å være tomt
            spilletidTextField.setText(enFilm.getSpilletid() > 0 ? enFilm.getSpilletid() + " minutter" : "");

            // lager et bildeobjekt
            Image image = new Image("https://image.tmdb.org/t/p/w500" + (enFilm.getPosterURL() == null ? "" : enFilm.getPosterURL()));

            // legger til standard-bilde når ny film blir laget
            filmBilde.setImage(enFilm.getPosterURL().startsWith("https://") ? new Image(enFilm.getPosterURL()) : image);


        }
    }

    static class FilmCelle extends ListCell<Film> {

        @Override
        protected void updateItem(Film enFilm, boolean empty) {
            super.updateItem(enFilm, empty);
            setText(enFilm == null ? "" : enFilm.getTittel() + " - " + "(" + enFilm.getUtgivelsesdato().getYear() + ")");
        }
    }

    // Setter alle tekstfeltene til å være tomme
    private void tomfilmInfo() {
        tittelText.setText("");
        beskrivelseTextArea.setText("");
        utgivelsesdatoTextField.setText("");
        spilletidTextField.setText("");
    }
}

