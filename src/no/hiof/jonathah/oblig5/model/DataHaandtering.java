package no.hiof.jonathah.oblig5.model;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import no.hiof.jonathah.oblig5.MainJavaFX;

import java.io.*;
import java.time.LocalDate;

public class DataHaandtering {

    public static void hente() {

        File kilde = new File("filmer_1000.csv");

        // henter filmer fra fil (.csv) og legge til som objekter i filmListeData (ObservableList).

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(kilde))) {

            String linje;

            while( (linje = bufferedReader.readLine()) != null) {
                String[] deler = linje.split(";");

                String[] date = deler[3].split("-");
                int year = Integer.parseInt(date[0]);
                int month = Integer.parseInt(date[1]);
                int day = Integer.parseInt(date[2]);

                if(deler.length == 6) {
                    Film f = new Film(deler[0], deler[1], Integer.valueOf(deler[2]), LocalDate.of(year, month, day), null, deler[5]);
                    MainJavaFX.leggTilFilm(f);
                } else {
                    Film f = new Film(deler[0], deler[1], Integer.valueOf(deler[2]), LocalDate.of(year, month, day), null, deler[4]);
                    MainJavaFX.leggTilFilm(f);
                }
            }

            bufferedReader.close();

            // System.out.println(MainJavaFX.getFilmData());

        } catch (FileNotFoundException e) {
            // Viser alertboks når filsti er feil/ikke eksisterer
            Alert alert = new Alert(AlertType.ERROR);
            alert.setTitle("Feilmelding");
            alert.setHeaderText("Feil ved lasting av filmer fra fil");
            alert.setContentText("Sjekk at riktig filsti er oppgitt");
            alert.showAndWait();

            System.out.println(e);
        } catch (IOException e) {
            // skriver ut feilmelding om det oppstår feil ved skriving til fil
            System.out.println(e);
        }



    }

    public static void skrive() {

        File kilde = new File("filmer_1000.csv");

        try (BufferedWriter bufretSkriver = new BufferedWriter(new FileWriter(kilde))) {

            for(Film enFilm : MainJavaFX.getFilmData()) {
                // Skriver fornavn og alterego til filen avskilt med ";"

                bufretSkriver.write(enFilm.getTittel() + ";" + enFilm.getBeskrivelse() + ";" + enFilm.getSpilletid() + ";" + enFilm.getUtgivelsesdato() + ";" + enFilm.getRegisor() + ";" + enFilm.getPosterURL());

                // Skriver et linjeskift
                bufretSkriver.newLine();

            }

            bufretSkriver.close();

        } catch (FileNotFoundException e) {
            // Skriver ut feilmelding om filen ikke finnes
            System.out.println(e);
        } catch (IOException e) {
            // skriver ut feilmelding om det oppstår feil ved skriving til fil
            System.out.println(e);
        }

    }


}
