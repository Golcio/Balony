import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;

public class FixEncoding {
    public static void main(String[] args) throws Exception {
        File dir = new File("src/main/java/balony");
        File[] files = dir.listFiles((d, name) -> name.endsWith(".java"));
        for (File f : files) {
            String content = new String(Files.readAllBytes(Paths.get(f.getAbsolutePath())), StandardCharsets.UTF_8);

            content = content.replace("wspÄŹĹĽËťrzedne", "współrzędne");
            content = content.replace("obsÄŹĹĽËťugujaca", "obsługująca");
            content = content.replace("poÄŹĹĽËťozenie", "położenie");
            content = content.replace("bieÄŹĹĽËťÄŹĹĽËťca", "bieżąca");
            content = content.replace("jeÄŹĹĽËťeli", "jeżeli");
            content = content.replace("rzÄ™dÄŹĹĽËťw", "rzędów");
            content = content.replace("balonÄŹĹĽËťw", "balonów");
            content = content.replace("odczytaÄŹĹĽËť", "odczytać");
            content = content.replace("listÄŹĹĽËť", "listę");
            content = content.replace("wyjĹ›Ä‡Ëť", "wyjść");
            content = content.replace("sÄąâ€šuchacz", "słuchacz");
            content = content.replace("zdarzeÄąâ€ž", "zdarzeń");
            content = content.replace("ÄŹĹĽËť", "ł"); // fallback

            content = content.replace("Ĺ‚", "ł");
            content = content.replace("Ĺ›", "ś");
            content = content.replace("Ĺš", "Ś");
            content = content.replace("Ä‡", "ć");
            content = content.replace("Ä™", "ę");
            content = content.replace("Ăł", "ó");
            content = content.replace("Ä…", "ą");
            content = content.replace("Ä„", "Ą");
            content = content.replace("ĹĽ", "ż");
            content = content.replace("Ĺş", "ź");

            // specific to 'Ł' because it has invisible character sometimes.
            content = content.replaceAll("Ĺ(?!\\w)(.)?atwy", "Łatwy");
            content = content.replace("Ĺ atwy", "Łatwy");
            content = content.replace("Ĺ  ", "Ł "); // with space
            content = content.replace("Ĺ ", "Ł");

            content = content.replace("TwĂłj wynik:", "Twój wynik:");
            content = content.replace("punktĂłw", "punktów");
            content = content.replace("swĂłj", "swój");
            content = content.replace("WystÄ…piĹ‚", "Wystąpił");
            content = content.replace("bĹ‚Ä…d", "błąd");
            content = content.replace("BĹ‚Ä…d", "Błąd");
            content = content.replace("NieĹ‚adnie", "Nieładnie");

            content = content.replace("GrÄ™", "Grę");
            content = content.replace("TrudnoĹ›ci", "Trudności");
            content = content.replace("WyjĹ›cie", "Wyjście");
            content = content.replace("WynikĂłw", "Wyników");
            content = content.replace("wyjĹ›Ä‡", "wyjść");
            content = content.replace("zaĹ‚adowaÄ‡", "załadować");

            content = content.replace("Ĺ atwy", "Łatwy");

            content = content.replace("wygrywajÄ…cy", "wygrywający");

            Files.write(Paths.get(f.getAbsolutePath()), content.getBytes(StandardCharsets.UTF_8));
        }
        System.out.println("Zrobione.");
    }
}
