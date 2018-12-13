package no.hiof.jonathah.oblig5.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Et {@code TvSerie} objekt representerer en tv serie med tittel,
 * beskrivelse, utgivelsesdato, liste med episoder. Klassen inneholder også diverse metode.
 *
 * @author Jonathan Hermansen
 */

public class TvSerie implements Comparable<TvSerie> {
    private String tittel;
    private String beskrivelse;
    private LocalDate utgivelsesdato;
    private ArrayList<Episode> episodeListe = new ArrayList<>();
    private int gjennomSnittligSpilletid;
    private int antallSesonger;

    /**
     * Kontruktør for å opprette Tv Serie objekt.
     * @param tittel Tittel på serien
     * @param beskrivelse Beskrivelse
     * @param utgivelsesdato Utgivelsesdato.
     */
    public TvSerie(String tittel, String beskrivelse, LocalDate utgivelsesdato) {
        this.tittel = tittel;
        this.beskrivelse = beskrivelse;
        this.utgivelsesdato = utgivelsesdato;
    }

    /**
     * Metode for å hente ut skuespillere i en Tv Serie.
     * @return Returnerer en liste med skuespillere.
     */
    public ArrayList<Rolle> hentCast() {
        // Lager en ny rolle som vi skal fylle opp med alle roller som spilles i episodene
        ArrayList<Rolle> returnRolleListe = new ArrayList<>();

        // Går gjennom hver enkelt episode i episodelisten
        for (Episode enEpisode : episodeListe) {
            // Går gjennom hver enkelt rolle som er listet opp i en episode
            for (Rolle enRolle : enEpisode.getRolleListe()) {
                // Undersøker om vi har lagt til akkurat denne rollen i listen før
                if (!returnRolleListe.contains(enRolle)) {
                    // Hvis vi ikke har det, legger vi den til i lista
                    returnRolleListe.add(enRolle);
                }
            }

        }

        return returnRolleListe;
    }

    /**
     * Metode for å hente skuespiller med antall episoder spilt.
     * @return Returnerer et "HashMap" med Rolle og Integer (antall episoder).
     */
    // Har løst dette ved hjelp av HashMap fordi dette var en av "bonusoppgavene" har også laget en egen metode som teller antallet for en rolle
    public HashMap<Rolle, Integer> hentCastMedAntallEpisoderSpilt() {
        // Lager en HashMap som skal holde på en Rolle, og antallet episoder rollen har spilt
        HashMap<Rolle, Integer> returnCast = new HashMap<>();

        // Går gjennom hver enkelt episode i episodelisten
        for (Episode enEpisode : episodeListe) {
            // Går gjennom hver enkelt rolle som er listet opp i en episode
            for (Rolle enRolle : enEpisode.getRolleListe()) {
                // Undersøker om vi har lagt til denne rollen før
                if (returnCast.containsKey(enRolle)) {
                    // Hvis vi har det, henter vi ut antallet episoder rollen har vært i
                    Integer antallSpilt = returnCast.get(enRolle);
                    // Vi erstatter så antalletspiltverdien med den gamle verdien + 1
                    returnCast.replace(enRolle, ++antallSpilt);
                }
                else {
                    // Hvis vi ikke har den fra før, legg den til og sett antallet episoder spilt til 1
                    returnCast.put(enRolle, 1);
                }
            }
        }

        return returnCast;
    }

    /**
     * Metode som henter antall episoder en skuespiller har vært med i.
     * @param enRolle Skulespiller.
     * @return Returnerer en int (antall episoder)
     */
    public int hentAntalletEpisoderEnRolleHarSpilt(Rolle enRolle) {
        int antallGangerSpilt = 0;

        // Går gjennom hver enkelt episode i episodelisten
        for (Episode enEpisode : episodeListe) {
            // Sjekker om rollen finnes i rollelisten for denne episoden
            if (enEpisode.getRolleListe().contains(enRolle)) {
                // Hvis den gjør det plusser vi på antallGangerSpilt med 1
                antallGangerSpilt++;
            }
        }

        return antallGangerSpilt;
    }


    /**
     * Metode som legger til episode i en tv serie.
     * @param episode Episoden som skal legger til.
     */
    public void leggTilEpisode(Episode episode) {
        // Undersøker om episoden sitt sesongNummer er høyere enn antallSesonger pluss 1
        if (antallSesonger+1 < episode.getSesongNummer()) {
            System.out.println("FEIL: Det er ikke mulig å legge til episoder for sesong " + episode.getSesongNummer() + ", den er " + (episode.getSesongNummer()  - antallSesonger) + " høyere enn antallet sesonger");
        }
        else {
            // Legger til episode til listen
            episodeListe.add(episode);
            // Oppdaterer gjennomsnittligspilletid
            oppdaterGjennomsnittligSpilletid();

            // Sjekker om episoden sitt sesongNummer er akkurat 1 høyere enn antallSesonger, i så fall, oppdater den
            if (antallSesonger+1 == episode.getSesongNummer())
                antallSesonger++;
        }

    }

    /**
     * Metode som henter alle epsider i en sesong.
     * @param sesongNummer Sesong man vil hente ut antall episoder fra.
     * @return Returnerer en liste med episoder.
     */
    public ArrayList<Episode> hentEpisoderISesong(int sesongNummer) {
        // Lager og instansierer et ArrayList-objekt som skal holde på alle episodene i sesongen vi har valgt
        ArrayList<Episode> episoderISesong = new ArrayList<>();

        // Går gjennom alle episodene til denne tvserien og sjekker hvilken sesong den tilhører
        for (Episode episode : episodeListe) {
            // Sjekker om episoden tilhører sesongen vi ønsker
            if (episode.getSesongNummer() == sesongNummer) {
                // Den gjør det, da legger vi den til i den nye listen vår
                episoderISesong.add(episode);
            }
        }

        // Returnerer listen med alle episoder i sesongen som ble valgt
        return episoderISesong;
    }

    /**
     * Metode som oppdaterer gjennomsnitts spilletid i Tv Serie.
     */
    private void oppdaterGjennomsnittligSpilletid() {
        // Lager en variabel for å holde på den totale spilletiden
        int totalSpilletid = 0;

        // Går gjennom alle episodene til tvserien og legger sammen spilletid til totalSpilletid
        for (Episode episode : episodeListe) {
            totalSpilletid += episode.getSpilletid();
        }

        // Beregner gjennomsnittligSpilletid, ved å dele den totalespilletiden, på antallet episoder vi har
        gjennomSnittligSpilletid = totalSpilletid / episodeListe.size();
    }

    public String getTittel() {
        return tittel;
    }

    public void setTittel(String tittel) {
        this.tittel = tittel;
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

    public ArrayList<Episode> getEpisodeListe() {
        // Vi lager en kopi av listen vi har, slik at den interne listen ikke kan manipuleres direkte utenfor klassen (innkapsling)
        return new ArrayList<>(episodeListe);
    }

    public int getGjennomSnittligSpilletid() {
        return gjennomSnittligSpilletid;
    }

    public int getAntallSesonger() {
        return antallSesonger;
    }

    @Override
    public String toString() {
        return tittel + " - " + utgivelsesdato.getYear();
    }

    /**
     * Metode som sorterer tv serier etter tittel.
     * @param sammenligningsTvSerie Tv serie som skal sammenlignes.
     * @return Returnerer int; der
     *          -1 forteller at this &lt; sammenligningsTvSerie,
     *          0 forteller at this == sammenligningsTvSerie,
     *          1 forteller at this &gt; sammenligningsTvSerie.
     */
    @Override
    public int compareTo(TvSerie sammenligningsTvSerie) {
        // Vi sammenligner tittelen til TvSeriene, som er en String. Så vi drar også nytte av String sin egen compareTo metode
        return this.getTittel().compareTo(sammenligningsTvSerie.getTittel());
    }
}