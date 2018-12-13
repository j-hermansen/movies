package no.hiof.jonathah.oblig5.model;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * {@code Produksjon} klassen representerer en produksjon av enten en film eller en tv serie. Både Film-klassen
 * og TvSerie-klassen arver fra Produksjon-klassen.
 *
 * @author Jonathan Hermansen
 */

// Vi ønsker ikke å kunne lage egne objekter av Produksjon, og har derfor satt den til abstrakt
public abstract class Produksjon {
    private String tittel, beskrivelse;
    private int spilletid;
    private LocalDate utgivelsesdato;
    private Person regisor;
    private ArrayList<Rolle> rolleListe = new ArrayList<>();

    /**
     * Konstruktør som Film- og TvSerie-klassen arver fra, for å opprette Film objekt eller TvSerie objekt.
     * @param tittel Tittel på Film/Tv Serie
     * @param beskrivelse Beskrivelse av Film/Tv Serie
     * @param spilletid Spilletid i minutter
     * @param utgivelsesdato Utgivelsesdato
     * @param regisor Regisører.
     */
    public Produksjon(String tittel, String beskrivelse, int spilletid, LocalDate utgivelsesdato, Person regisor) {
        this.tittel = tittel;
        this.beskrivelse = beskrivelse;
        this.spilletid = spilletid;
        this.utgivelsesdato = utgivelsesdato;
        this.regisor = regisor;
    }

    public Produksjon() {
        tittel = "";
        beskrivelse = "";
        spilletid = 0;
        utgivelsesdato = LocalDate.MIN;
        regisor = new Person();
    }

    public void leggTilEnRolle(Rolle enRolle) {
        rolleListe.add(enRolle);
    }

    /**
     * Metode som legger til flere roller samtidig ved hjelp av en ArrayList med roller som parameter.
     * @param rolleListe ArrayList som inneholder roller.
     */
    public void leggTilMangeRoller(ArrayList<Rolle> rolleListe) {
        // Vi legger alle elementene fra rolleListen vi sender inn som parameter, til rolleListen som hører til denne produksjonen
        this.rolleListe.addAll(rolleListe);
    }


    /**
     * Henter ut rolleliste.
     * @return Returnerer en ArrayList med alle roller i Film/Tv Serie
     */
    public ArrayList<Rolle> getRolleListe() {
        // Vi lager en kopi av listen vi har, slik at den interne listen ikke kan manipuleres direkte utenfor klassen (innkapsling)
        return new ArrayList<>(rolleListe);
    }

    public String getTittel() {
        return tittel;
    }

    public void setTittel(String tittel) {
        this.tittel = tittel;
    }

    public int getSpilletid() {
        return spilletid;
    }

    public void setSpilletid(int spilletid) {
        this.spilletid = spilletid;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public LocalDate getUtgivelsesdato() {
        return utgivelsesdato;
    }

    public void setUtgivelsesdato(LocalDate utgivelsesdato) {
        this.utgivelsesdato = utgivelsesdato;
    }

    public Person getRegisor() {
        return regisor;
    }

    public void setRegisor(Person regisor) {
        this.regisor = regisor;
    }

    @Override
    public String toString() {
        return tittel + " " + utgivelsesdato.getYear();
    }
}