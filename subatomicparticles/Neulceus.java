package subatomicparticles;

public class Neulceus {

    int noNutrons = 0;
    int nProtons = 0;


    public Neulceus(int atomicNumber, float atomicMass) {
        nProtons = atomicNumber;
        noNutrons = (int)atomicMass - atomicNumber;
    }

    public float getCharge() {
        return nProtons * Proton.CHARGE;
    }
     
    public double getChargeCoulumb() {
        return nProtons * Proton.CHARGE_IN_COLOUMB;
    }

    public double getMass() {
        return (nProtons * Proton.MASS) + (noNutrons * Neutron.MASS);
    }

    public double getMassKG() {
        return (nProtons * Proton.MASS_IN_KG) + (noNutrons * Neutron.MASS_IN_KG);
    }
}
