package balony;

import org.junit.Test;
import static org.junit.Assert.*;

public class HighScoreTest {

        @Test
        public void testGettersSetters() {
                HighScore wynik = new HighScore("Gracz123", 500);

                assertEquals("Zwrocony nick musi byc adekwatny do nazwy podczas inicjalizacji", "Gracz123",
                                wynik.getNick());
                assertEquals("Zwrocona wartosc punktowa po uzyciu gettera musi byc dokladnie identyczna", 500,
                                wynik.getPunkty());

                // Test modyfikacji punktów
                wynik.setPunkty(1250);
                assertEquals("Gettery powinny poprawnie odzwierciedlac swieza uaktualniona wartosc z uzyciem setterow",
                                1250,
                                wynik.getPunkty());

                // Test modyfikacji nazwy
                wynik.setNick("NowyNick");
                assertEquals("Gettery powinny poprawnie odzwierciedlac swieza uaktualniona nazwe obiektu", "NowyNick",
                                wynik.getNick());
        }
}
