package molecular;

import molecular.utils.VSEPR;
import subatomicparticles.Atom;

public class Bond {

    public static enum AtomBond {
        SELF_ATOM,
        SINGLE_BOND,
        DOUBLE_BOND,
        TRIPPLE_BOND
    }

    public static enum BondType {
        COVALENT_BOND,
        IONIC_BOND,
        CONJUGATE_BOND
    }

    private double length;
    private BondedAtom bondedAtoms;
    BondType bondtype;

    public Bond(BondedAtom bAtoms) {
        bondedAtoms = bAtoms;
        length = bAtoms.getCentralAtom().getAtomicRadius() + bAtoms.getBondedAtom().getAtomicRadius();
        
        float charge1 = bAtoms.getCentralAtom().getCharge();
        float charge2 = bAtoms.getBondedAtom().getCharge();
        if((charge1 > 0 && charge2 < 0) || (charge1 < 0 && charge2 > 0)) {
            bondtype = BondType.IONIC_BOND;
        } else {
            bondtype = BondType.COVALENT_BOND;
        }
    }

    public double getBondLength() {
        return length;
    }

    public static float getBondAngle(Bond b1, Bond b2) {
        float angle = Float.POSITIVE_INFINITY;
        Atom cent;
        Atom ff;
        Atom gg;
        // get common Atom
        if (b1.bondedAtoms.getCentralAtom() == b2.bondedAtoms.getCentralAtom()) {
            // common is a
            cent = b1.bondedAtoms.getCentralAtom();
            ff = b1.bondedAtoms.getBondedAtom();
            gg = b2.bondedAtoms.getBondedAtom();
        } else if (b1.bondedAtoms.getBondedAtom()  == b2.bondedAtoms.getBondedAtom()) {
            // common is b
            cent = b1.bondedAtoms.getBondedAtom();
            ff = b1.bondedAtoms.getCentralAtom();
            gg = b2.bondedAtoms.getCentralAtom();
        } else if (b1.bondedAtoms.getCentralAtom() == b2.bondedAtoms.getBondedAtom()) {
            // common is a of first and b of second
            cent = b1.bondedAtoms.getCentralAtom();
            ff = b1.bondedAtoms.getBondedAtom();
            gg = b2.bondedAtoms.getCentralAtom();
        } else if (b1.bondedAtoms.getBondedAtom() == b2.bondedAtoms.getCentralAtom()) {
            // common is b of first and a of second
            cent = b1.bondedAtoms.getBondedAtom();
            ff = b1.bondedAtoms.getCentralAtom();
            gg = b2.bondedAtoms.getBondedAtom();
        } else {
            // no common found
            cent = null;
            ff = null;
            gg = null;
        }

        if (cent == null || ff ==null || gg == null) {
            return Float.POSITIVE_INFINITY;
        }

        // todo here
        // apply VSEPR theory
        // to calculate bond angle
        angle = VSEPR.getBondAngle(cent, ff, gg);
        return angle;
    }
    
}
