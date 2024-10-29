package subatomicparticles;

public class EnergyLevel implements Comparable<EnergyLevel> {

    static enum EnergyLevelUsage {
        UNFILLED,
        PARTIALLY_FILLED,
        COMPLEATELY_FILLED
    };

    private int n;
    private int l;
    private Stability stability;
    
    private EnergyLevelUsage filledStatus;

    public EnergyLevel(int shell, int subShell) {
        n = shell;
        l = subShell;
        filledStatus = EnergyLevelUsage.UNFILLED;
    }

    public void setStability(Stability stability) {
        this.stability = stability;
    }

    public Stability getStability() {
        return this.stability;
    }

    public EnergyLevelUsage getEnergyLevelState() {
        return filledStatus;
    }

    public void setEnergyLevelState(EnergyLevelUsage state) {
        filledStatus = state;
    }

    int getEnergyLevel() {
        return n + l;
    }

    public int getShellNumber() {
        return n;
    }

    public int getSubshellNumber() {
        return l;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (obj instanceof EnergyLevel) {
            return this.compareTo((EnergyLevel) obj) == 0 ? true : false;
        }
        return super.equals(obj);
    }

    @Override
    public int compareTo(EnergyLevel other) {
        int res = Integer.compare(this.getEnergyLevel(), other.getEnergyLevel());
        if(res == 0) {
            res = Integer.compare(this.n, other.n);
            if (res == 0) {
                res = Integer.compare(this.l, other.l);
            }
        }
        return res;
    }

    public static int compare(EnergyLevel o1, EnergyLevel o2) {
        return o1.compareTo(o2);
    }

}
