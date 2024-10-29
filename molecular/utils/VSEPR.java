package molecular.utils;

import molecular.BondedAtom;
import molecular.Molecule;
import molecular.Hybridisation.HybridisationTypes;
import subatomicparticles.Atom;

public class VSEPR {

    public static int noLonePair(Atom centralAtom, Molecule molecule) {
        int nE = centralAtom.getValenceElectrons();
        // nE = nE - sharedPair;
        for (BondedAtom bAtom : centralAtom.getBondedAtoms()) {
            if (bAtom.getCentralAtom() == centralAtom || bAtom.getBondedAtom() == centralAtom){
                // remove shared elecrrons from central atom
                nE -= bAtom.getNumberOfBondNumber();
            }
        }
        return nE;
    }

    public static int noBondedPair(Atom centralAtom, Molecule molecule) {
        int bonded = 0;
        for (BondedAtom bondedAtom : molecule.getLinkedAtoms(centralAtom)) {
            bonded += bondedAtom.getBondedAtom().getValenceElectrons();
        }
        return bonded;
    }

    public static int noElectonPair(Atom centralAtom, Molecule molecule) {
        //0.5 * centralAtom.getValenceElectrons() + notAtomsLinkedToCentralAtom;
        int pp = 0;
        for (BondedAtom bondedAtom : molecule.getLinkedAtoms(centralAtom)) {
            pp += bondedAtom.getBondedAtom().getValenceElectrons();
        }
        return (centralAtom.getValenceElectrons() + pp) / 2;
    }
    
    public static float getBondAngle(Atom centralAtom, Atom atom1, Atom atom2) {
        if (atom1.getMolecule() == null || atom2.getMolecule() == null) {
            return Float.POSITIVE_INFINITY;
        }
        if(atom1.getMolecule() != atom2.getMolecule() || atom1 == atom2){
            return Float.POSITIVE_INFINITY;
        }
        
        HybridisationTypes hybridisation = centralAtom.getHybridisation().getHybridisationType();
        if(hybridisation == HybridisationTypes.SS){
            return 180.0f;
        };


        // TODO:Calculate bond angle
        return 0.0f;
    }

}
