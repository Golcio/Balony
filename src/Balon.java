import javax.swing.*;
import java.awt.*;

/**
 * balon g�owny obiekt w grze
 */
public class Balon {
    Kolor kolor;
    boolean czyIstnieje;
    private Image obrazekBalonu;

    public Polozenie getAktualnePolozenia() {
        return aktualnePolozenia;
    }

    public void setAktualnePolozenia(Polozenie aktualnePolozenia) {
        this.aktualnePolozenia = aktualnePolozenia;
    }

    Polozenie aktualnePolozenia;

    public Image getObrazekBalonu() {
        return  obrazekBalonu;
    }

    public void setObrazekBalonu(Image obrazekBalonu) {
        this.obrazekBalonu = obrazekBalonu;
    }



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
