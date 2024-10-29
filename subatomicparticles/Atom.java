package subatomicparticles;

import java.util.ArrayList;
import java.util.Arrays;

import molecular.BondedAtom;
import molecular.Hybridisation;
import molecular.Molecule;
import molecular.Bond.AtomBond;
import molecular.utils.VSEPR;

public class Atom {

    int AtomicNumber;
    float AtomicMass;
    double atomicRadius;
    String elementName;
    Neulceus neulceus;
    float charge;
    float effectiveAtomicZ;
    double IonizationEnergy;
    double Eaffinity;
    double Enegetivity;
    ElectrincConfiguration configuration;
    Valencies valencies;
    Molecule partOfMolecule;
    Hybridisation hybridisation;
    ArrayList<Orbital> lonePairElecrons;
    ArrayList<Orbital> bondedElecrons;

    private int MAX_CONNECTED_ATOMS;

    public Atom(int Z) throws AtomException {
        this(Z, 0.0f);
    }

    public Atom(int Z, float A) throws AtomException {
        this(Z, A, "");
    }

    public Atom(int Z, float A, String name) throws AtomException {
        this(Z, A, name, 0);
    }

    public Atom(int Z, float A, String name, float chargeOfAtom) throws AtomException {
        AtomicNumber = Z;
        AtomicMass = A;
        elementName = name;
        charge = chargeOfAtom;
        partOfMolecule = null;
        hybridisation = null;
        neulceus = new Neulceus(AtomicNumber, AtomicMass);
        try {
            configuration = new ElectrincConfiguration(Z, Z);
            updateProperties();
        } catch (ElectronicConfigurationException e) {
            throw new AtomException("Atom intialization Failed due to improper Electronicc configuration\n" + e.getMessage());
        }
    }

    public float getCharge() {
        return charge;
    }

    private void updateProperties() {
        effectiveAtomicZ = configuration.setShieldingEffect();
        atomicRadius = configuration.atomicRadius();
        valencies = configuration.getValencies();
        IonizationEnergy = configuration.getIonizationEnergy();
        Eaffinity = calculateAffinity();
        Enegetivity = calculateElectronegetivity();

        Shell vShell = getValenceShell();
        lonePairElecrons.clear();
        if(vShell.containsSubshellS()){
            lonePairElecrons.addAll(Arrays.asList(vShell.subShells[Shell.SUBSHELL_S].orbitals));
        }
        if(vShell.containsSubshellP()){
            lonePairElecrons.addAll(Arrays.asList(vShell.subShells[Shell.SUBSHELL_P].orbitals));
        }
    }

    public double calculateAffinity() {
        int ne = configuration.getNumberElectrons() + 1;
        double IE1 = configuration.getIonizationEnergy();
        ElectrincConfiguration temp = new ElectrincConfiguration(ne, AtomicNumber);
        double IE2 = temp.getIonizationEnergy();
        return Math.abs(IE1 - IE2);
    }

    public double calculateElectronegetivity() {
        return (Eaffinity + IonizationEnergy) / 2.0f;
    }

    public void addCharge(float Charge) {
        charge += Charge;
    }

    public void removeCharge(float Charge) {
        charge -= Charge;
    }

    public boolean addElectron() {
        boolean b = configuration.addElectron();
        updateProperties();
        return b;
    }

    public int addElectrons(int n) {
        int i = configuration.addElectrons(n);
        updateProperties();
        return i;
    }

    public boolean removeElectron() {
        boolean b = configuration.removeElectron();
        updateProperties();
        return b;
    }

    public int removeElectrons(int n) {
        int i = configuration.removeElectrons(n);
        updateProperties();
        return i;
    }

    public void setElementName(String name) {
        elementName = name;
    }

    public String getElectronicConfiguration() {
        return configuration.configurationString();
    }

    public String getElectronicConfiguration2() {
        return configuration.configurationString2();
    }

    public Shell getValenceShell() {
        return configuration.getValenceShell(configuration.getValencies());
    }

    public int getValenceElectrons() {
        int n = 0;
        Shell vShell = getValenceShell();
        if (vShell == null) {
            return 0;
        }
        if(vShell.containsSubshellS()){
            n += configuration.getSubShell(vShell.shellNum, Shell.SUBSHELL_S).getNumberElectrons();
        }
        if (vShell.containsSubshellP()){
            n += configuration.getSubShell(vShell.shellNum, Shell.SUBSHELL_P).getNumberElectrons();
        };
        return n;
    }

    public Valencies getValencies() {
        return valencies;
    }

    public double getAtomicRadius() {
        return atomicRadius;
    }

    public double getIonizationEnergy() {
        return IonizationEnergy;
    }

    public double getElectronAffinity() {
        return Eaffinity;
    }

    public double getElectroNegetivity() {
        return Enegetivity;
    }

    public static int compareRadius(Atom a1, Atom a2) {
        return Double.compare(a1.atomicRadius, a2.atomicRadius);
    }

    public Anion makeAnion(int charge) {
        addElectrons(charge);
        removeCharge(charge);
        return (Anion) this;
    }

    public Cation makeCation(int charge) {
        removeElectrons(charge);
        addCharge(charge);
        return (Cation) this;
    }

    /**
     * compare Ionization Potential
     * @param a1
     * @param a2
     * @return
     */
    public static int compareIP(Atom a1, Atom a2) {
        return Double.compare(a1.IonizationEnergy, a2.IonizationEnergy);
    }

    /**
     * compare Electron Affinity
     * @param a1
     * @param a2
     * @return
     */
    public static int compareEA(Atom a1, Atom a2) {
        return Double.compare(a1.Eaffinity, a2.Eaffinity);
    }

    /**
     * compare Electron Negetivity
     * @param a1
     * @param a2
     * @return
     */
    public static int compareEN(Atom a1, Atom a2) {
        return Double.compare(a1.Enegetivity, a2.Enegetivity);
    }

    /**
     * compare Metallic Charecters
     * @param a1
     * @param a2
     * @return
     */
    public static int compareMetallicNature(Atom a1, Atom a2) {
        return Double.compare(a1.atomicRadius, a2.atomicRadius);
    }

    public ArrayList<BondedAtom> getBondedAtoms() {
        if (partOfMolecule == null) {
            return null;
        }
        return partOfMolecule.getLinkedAtoms(this);
    }

    public boolean addConnectedAtom(Atom atom, AtomBond bondT){
        if (atom == null || atom == this) {
            return false;
        }
        if(partOfMolecule == null){
            createMolecule();
        }
        if (partOfMolecule.getLinkedAtoms(this).size() >= MAX_CONNECTED_ATOMS) {
            return false;
        }
        partOfMolecule.addAtom(atom, bondT);
        return true;
    }

    public void addConnectedAtoms(ArrayList<BondedAtom> atoms) {
        for (BondedAtom bond : atoms) {
            Atom other = bond.isCentralAtom(this) ? bond.getBondedAtom() : bond.getCentralAtom();
            addConnectedAtom(other, bond.getNumberOfBond());
        }
    }

    public int getLonePairNumber() {
        return VSEPR.noLonePair(this, partOfMolecule);
    }

    public Molecule createMolecule() {
        if (partOfMolecule != null) 
            return partOfMolecule;
        Molecule molecule = new Molecule();
        molecule.addAtom(this, AtomBond.SINGLE_BOND);
        partOfMolecule = molecule;
        return molecule;
    }

    public Molecule getMolecule() {
        return partOfMolecule;
    }

    public void setPartOfMolecule(Molecule molecule) {
        partOfMolecule = molecule;
    }

    public Hybridisation getHybridisation() {
        return hybridisation;
    }

    public void updateHybridisation() {
        if (hybridisation != null) {
            hybridisation = new Hybridisation(this);
        }
        hybridisation.updateProperties();
    }

    public boolean canAddMoreAtoms() {
        int n = 0;
        for (BondedAtom bondedAtom : partOfMolecule.getLinkedAtoms(this)) {
            n += bondedAtom.getNumberOfBondNumber();
        }
        // TODO: check this condition again
        return n < valencies.getNumberOfSPElecgtrons();
    }



}
