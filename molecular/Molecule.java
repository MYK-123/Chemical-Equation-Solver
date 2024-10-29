package molecular;

import java.util.ArrayList;

import molecular.Bond.AtomBond;
import subatomicparticles.Atom;

public class Molecule {

    Atom centralAtom;

    ArrayList<BondedAtom> atoms;
    ArrayList<MolecularOrbital> mOrbitals;
    int bond_order;

    public Molecule() {
        centralAtom = null;
        atoms = new ArrayList<>();
        mOrbitals = new ArrayList<>();
        bond_order = 0;
    }

    /**
     * molecule exist only if bond order is not equal to 0
     * @return
     */
    public boolean moleculeExists() {
        return bond_order != 0;
    }

    public void update() {
        int ne = 0;
        ne += centralAtom.getValencies().getNumberOfSPElecgtrons();
        for (int i = 0; i < atoms.size(); i++) {
            ne += atoms.get(i).getBondedAtom().getValencies().getNumberOfSPElecgtrons();
        }
        mOrbitals.clear();
        
        // TODO: Calculate Molecular Orbital
    }
    
    public ArrayList<BondedAtom> getLinkedAtoms(Atom atom) {
        if (atom == null) return null;
        ArrayList<BondedAtom> atms = new ArrayList<>();
        for (BondedAtom bondedAtom : atoms) {
            if (bondedAtom.isCentralAtom(atom) || bondedAtom.getBondedAtom() == atom) {
                atms.add(bondedAtom);
            }
        }
        return atms;
    }

    public void addAtom(Atom atom, AtomBond bond) {
        if (atom == null) return;
        if (centralAtom == null) {
            if (atoms.size() != 0) {
                centralAtom = atoms.get(0).getCentralAtom();
            } else {
                centralAtom = atom;
                atoms.add(new BondedAtom(centralAtom, atom, AtomBond.SELF_ATOM));
                return;
            }
        }
        atoms.add(new BondedAtom(centralAtom, atom, bond));
    }

}
