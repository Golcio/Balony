/**
 * balon g³owny obiekt w grze
 */
public class Balon {
    Kolor kolor;
    boolean czyIstnieje;

    /**
     * Konstruktor tworzacy balon w okreslonym kolorze .
     *
     * @param kolor kolor balonu
     *
     */
    public Balon(Kolor kolor) {
        this.kolor = kolor;
        czyIstnieje=true;
    }
    /**
     * Konstruktor domyslny tworzacy "puste" miejsce na planszy.
     *
     *
     */

    public Balon() {
        kolor=Kolor.brak;
        czyIstnieje=false;

    }

    public Kolor getKolor() {
        return kolor;
    }

    public void setKolor(Kolor kolor) {
        this.kolor = kolor;
    }

    public boolean isCzyIstnieje() {
        return czyIstnieje;
    }

    public void setCzyIstnieje(boolean czyIstnieje) {
        this.czyIstnieje = czyIstnieje;
    }
}
