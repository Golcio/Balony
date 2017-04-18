/**
 * przechowuje wsp�rzedne pola gry
 */
public class Polozenie {
    private int wsplX;
    private int wsplY;

    public Polozenie(int wsplX, int wsplY) {
        this.wsplX = wsplX;
        this.wsplY = wsplY;
    }

    public int getWsplX() {
        return wsplX;
    }

    public void setWsplX(int wsplX) {
        this.wsplX = wsplX;
    }

    public int getWsplY() {
        return wsplY;
    }

    public void setWsplY(int wsplY) {
        this.wsplY = wsplY;
    }

    @Override
    public boolean equals(Object inny) {
        if (inny==null) return false;
        if (inny==this) return true;
        if (!(inny instanceof Polozenie)) return  false;
        Polozenie innePolozenie =(Polozenie)inny;
        if (innePolozenie.getWsplY()==this.getWsplY()&&innePolozenie.getWsplX()==this.getWsplX())
            return true;
        else return false;
    }
}
