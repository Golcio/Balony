package balony;

import org.junit.Test;
import static org.junit.Assert.*;

public class BalonTest {

    @Test
    public void testKonstruktorZObuParametrami() {
        Polozenie polozenie = new Polozenie(100, 200);
        Balon balon = new Balon(Kolor.CZERWONY, polozenie);

        assertEquals("Oczekiwano poprawnie przypisanego koloru (CZERWONY)", Kolor.CZERWONY, balon.getKolor());
        assertEquals("Oczekiwano podanej wspolrzednej X = 100", 100, balon.getAktualnePolozenia().getWsplX());
        assertEquals("Oczekiwano podanej wspolrzednej Y = 200", 200, balon.getAktualnePolozenia().getWsplY());
        assertTrue("Nowy balon z parametrami powinien byc domyslnie oznaczony jako istniejacy", balon.isCzyIstnieje());
    }

    @Test
    public void testKonstruktorDomyslny() {
        Balon balon = new Balon();

        assertEquals("Konstruktor domyslny powinien ustawiac 'brak' koloru", Kolor.brak, balon.getKolor());
        assertFalse("Konstruktor domyslny powinien tworzyc obiekt pierwotnie oznaczany jako nieistniejacy",
                balon.isCzyIstnieje());
        assertNull("Polozenie balonu z domyslnego konstruktora powinno byc niezaalokowane (puste)",
                balon.getAktualnePolozenia());
    }
}
