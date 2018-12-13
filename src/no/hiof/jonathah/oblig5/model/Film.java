package no.hiof.jonathah.oblig5.model;

import java.time.LocalDate;

/**
 * Et {@code Film} objekt representerer en film med informasjon om tittel, beskrivelse,
 * spilletid, utgivelsesdato, regisør og filmcover. Film-klassen arver variabler og metoder fra Produksjon-klassen.
 *
 * @author Jonathan Hermansen
 */

// En film "er en" produksjon, så derfor har vi satt at den extender Produksjon
// Vi får dermed med alle egenskapene fra Produksjon "gratis"
public class Film extends Produksjon implements Comparable<Film> {

    private String posterURL;


    /**
     * Konstruerer et nytt Film objekt.
     * @param tittel tittelen på filmen
     * @param beskrivelse Beskrivelse av filmen
     * @param spilletid Lengde på film, i minutter.
     * @param utgivelsesdato Dato filmen ble gitt ut.
     * @param regisor Regisør av film.
     * @param posterURL Url til filmcover, som brukes til å vise bilde av film.
     */
    public Film(String tittel, String beskrivelse, int spilletid, LocalDate utgivelsesdato, Person regisor, String posterURL) {
        // Kaller superkontruktøren (som tilhører Produksjon), vi "sender" da tittel, beskrivelse, spilletid, utgivelsesdato og regisor videre
        super(tittel, beskrivelse, spilletid, utgivelsesdato, regisor);
        this.posterURL = posterURL;
    }

    public Film() {
        super();
    }


    public String getPosterURL() {
        return posterURL;
    }

    public void setPosterURL(String posterURL) {
        this.posterURL = posterURL;
    }

    /**
     * Overskriver toString-metoder for å bare skrive ut tittel og utgivelsesår.
     * @return Returnserer en String.
     */
    @Override
    public String toString() {
        // super - nøkkelordet her er strengt tatt ikke nødvendig, men det gjør det tydelig at metodene tilhører superklassen
        return super.getTittel() + (super.getUtgivelsesdato() != null ? " (" + super.getUtgivelsesdato().getYear() + ")" :"");// + (super.getSpilletid() > 0 ? super.getSpilletid() + " minutter" : "");

    }

    /**
     * Sammenligner tittel på filmer.
     * @param sammenligningsFilm Film som skal sammenlignes.
     * @return Returnerer int; der
     *          -1 forteller at this &lt; sammenligningsFilm,
     *          0 forteller at this == sammenligningsFilm,
     *          1 forteller at this &gt; sammenligningsFilm.
     */
    @Override
    public int compareTo(Film sammenligningsFilm) {
        // Vi sammenligner tittelen til filmene, som er en String. Så vi drar også nytte av String sin egen compareTo metode
        return this.getTittel().compareTo(sammenligningsFilm.getTittel());
    }
}
