package subatomicparticles;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Valencies {

    private enum GROUP_ELEMENT {
        S1, S2,
        P1, P2, P3, P4, P5, P6,
        D1, D2, D3, D4, D5, D6, D7, D8, D9, D10, 
        F1, F2,

        ERROR
    };


    private List<Valency> arr;
    private GROUP_ELEMENT groupElement;
    private int nElectrons;
    private int sp_nElectrons;
    private int _N;

    /**
     * 
     * @param ns - no. electron in s subshell
     * @param np - no. electron in p subshell
     * @param nd - no. electron in d subshell
     * @param nf - no. electron in f subshell
     * @param N  - no. of shell
     * @return - an {@link Valencies} object
     */
    public static Valencies parse(int ns, int np, int nd, int nf, int N) {
        if (N <= 0)
            return null;
        Valencies valencies = new Valencies();
        valencies._N = N;
        valencies.groupElement = valencies.getElementGroup(ns, np, nd, nf, N);
        valencies.setValencies();
        valencies.nElectrons = ns + np + nd + nf;
        valencies.sp_nElectrons = ns + np;
        return valencies;
    }

    public int getValenceShellNo() {
        return _N;
    }

    public void setValencies() {
        if (groupElement == GROUP_ELEMENT.ERROR) {
            if (!arr.isEmpty()) {
                arr.clear();
            }
        }

        switch (groupElement) {
            case S1:arr.add(new Valency(1));break;
            case S2:arr.add(new Valency(2));break;

            case P1:arr.add(new Valency(3));break;
            case P2:arr.add(new Valency(4));break;
            case P3:arr.add(new Valency(-3));break;
            case P4:arr.add(new Valency(-2));break;
            case P5:arr.add(new Valency(-1));break;
            case P6:arr.add(new Valency(0));break;
            
            case D1:arr.add(new Valency(3));break;
            case D2:arr.add(new Valency(4));break;
            case D3:
            arr.add(new Valency(5));
            arr.add(new Valency(4));
            break;
            case D4:arr.add(new Valency(2));break;
            case D5:
            arr.add(new Valency(7));
            arr.add(new Valency(4));
            arr.add(new Valency(2));
            break;
            case D6:
            arr.add(new Valency(2));
            arr.add(new Valency(3));
            break;
            case D7:
            arr.add(new Valency(3));
            arr.add(new Valency(2));
            break;
            case D8:arr.add(new Valency(2));break;
            case D9:
            arr.add(new Valency(2));
            arr.add(new Valency(1));
            break;
            case D10:arr.add(new Valency(2));break;
            
            case F1:arr.add(new Valency(3));break;
            case F2:
            arr.add(new Valency(3));
            arr.add(new Valency(4));
            break;
            
            default:
            if(!arr.isEmpty()) arr.clear();
            break;
        }
    }

    public boolean is_S_blockElement(int ns, int np, int nd, int nf) {        
        return (ns == 1 || ns == 2) && np == 0 && nd == 0 && nf == 0;
    }

    public boolean is_P_blockElement(int ns, int np, int nd, int nf) {        
        return ns == 2 && (np != 0);
    }

    public boolean is_D_blockElement(int ns, int np, int nd, int nf) {        
        return (ns == 1 || ns == 2) && np == 0 && nd != 0 && nf == 0;
    }

    public boolean is_F_blockElement(int ns, int np, int nd, int nf) {        
        return ns == 2 && np == 0 && ((nd == 0 || nd == 1 || nd == 2) && (nf >= 0 && nf <= 14));
    }

    public GROUP_ELEMENT getElementGroup(int ns, int np, int nd, int nf, int N) {
        if (N <= 0) return GROUP_ELEMENT.ERROR;

        // s block elements
        if (is_S_blockElement(ns, np, nd, nf)) {
            if (ns == 1) {
                return GROUP_ELEMENT.S1;
            } else if (ns == 2) {
                return GROUP_ELEMENT.S2;
            } else {
                return GROUP_ELEMENT.ERROR;
            }
        }

        // p block elements
        if (is_P_blockElement(ns, np, nd, nf)) {
            if (np == 1) {
                return GROUP_ELEMENT.P1;
            } else if (np == 2) {
                return GROUP_ELEMENT.P2;
            } else if (np == 3) {
                return GROUP_ELEMENT.P3;
            } else if (np == 4) {
                return GROUP_ELEMENT.P4;
            } else if (np == 5) {
                return GROUP_ELEMENT.P5;
            } else if (np == 6) {
                return GROUP_ELEMENT.P6;
            } else {
                return GROUP_ELEMENT.ERROR;
            }
        }
        // d block elements
        if (is_D_blockElement(ns, np, nd, nf)) {
            if (nd == 1) {
                return GROUP_ELEMENT.D1;
            } else if (nd == 2) {
                return GROUP_ELEMENT.D2;
            } else if (nd == 3) {
                return GROUP_ELEMENT.D3;
            } else if (nd == 4) {
                return GROUP_ELEMENT.D4;
            } else if (nd == 5) {
                return GROUP_ELEMENT.D5;
            } else if (nd == 6) {
                return GROUP_ELEMENT.D6;
            } else if (nd == 7) {
                return GROUP_ELEMENT.D7;
            } else if (nd == 8) {
                return GROUP_ELEMENT.D8;
            } else if (nd == 9) {
                return GROUP_ELEMENT.D9;
            } else if (nd == 10) {
                return GROUP_ELEMENT.D10;
            } else {
                return GROUP_ELEMENT.ERROR;
            }
        }
        // f block elements
        // no need for checking here as if code executi0on reaches here then,
        // it is but obviously f block element.
        if (N == 6) {
            return GROUP_ELEMENT.F1;
        } else if (N == 7) {
            return GROUP_ELEMENT.F2;
        }
        return GROUP_ELEMENT.ERROR;
    }

    public int getNumValencies() {
        return arr.size();
    }

    public boolean isSingleValency() {
        return getNumValencies() == 1;
    }

    public boolean isVariableValency() {
        return !isSingleValency();
    }

    private Valencies(Collection<? extends Valency> valencies) {
        arr = new ArrayList<>(valencies);
    }

    private Valencies() {
        arr = new ArrayList<>();
    }

    public Valency getValency(int index) {
        return arr.get(index);
    }

    public int getNumElectrons() {
        return nElectrons;
    }

    public int getNumberOfSPElecgtrons() {
        return sp_nElectrons;
    }

    public Valencies clone() {
        return new Valencies(arr);
    }

}
