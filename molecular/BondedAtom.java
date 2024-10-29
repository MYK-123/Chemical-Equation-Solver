package molecular;

import molecular.Bond.AtomBond;
import subatomicparticles.Atom;

public class BondedAtom {

    private Atom centralAtom;
    private Atom other;
    private Bond _bond;
    private AtomBond atomBondTYPE;

    public BondedAtom(Atom cenAtom, Atom otherAtom, AtomBond bond) {
        centralAtom = cenAtom;
        other = otherAtom;
        atomBondTYPE = bond;
        _bond = new Bond(this);
    }

    public Bond getBond() {
        return _bond;
    }

    public boolean isCentralAtom(Atom atom) {
        return centralAtom == atom;
    }

    public Atom getCentralAtom() {
        return centralAtom;
    }

    public Atom getBondedAtom() {
        return other;
    }

    public AtomBond getNumberOfBond() {
        return atomBondTYPE;
    }

    public int getNumberOfBondNumber() {
        if (atomBondTYPE == AtomBond.SINGLE_BOND) {
            return 1;
        } else if (atomBondTYPE == AtomBond.DOUBLE_BOND) {
            return 2;
        } else if (atomBondTYPE == AtomBond.TRIPPLE_BOND) {
            return 3;
        }
        return 0;
    }

}
