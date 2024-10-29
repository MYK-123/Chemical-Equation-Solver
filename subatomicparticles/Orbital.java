package subatomicparticles;

public class Orbital {
    
    private boolean spinPos = false;
    private boolean spinNeg = false;
    private Stability stability;


    public Orbital(int nElectrons) throws ElectronicConfigurationException{        
        switch (nElectrons) {
            case 0:
                spinNeg = false;
                spinPos = false;
                break;
            case 1:
                spinPos = true;
                spinNeg = false;
                break;
            case 2:
                spinPos = spinNeg = true;
                break;
            default:
                // when nElectrons are greater than 2
                throw new ElectronicConfigurationException("Number of electron assiged in a single orbital cannot be greater than 2");
        }
        updateStability();
    }

    public Orbital() {
        this(false);
    }

    public Orbital(boolean PositiveSpin) {
        this(PositiveSpin, false);
    }

    public Orbital(boolean PositiveSpin, boolean NegativeSpin) {
        spinPos = PositiveSpin;
        spinNeg = NegativeSpin;
        updateStability();
    }

    public void updateStability() {
        switch (getNumElectrons()) {
            case 2:
                stability = Stability.FILLED;
                break;
       
            case 1:
                stability = Stability.HALF_FILLED;
                break;
            
            case 0:
                stability = Stability.EMPTY;
                break;
            default:
                break;
        }
    }

    public Stability getStability() {
        return stability;
    }

    public int getNumElectrons() {
        if (spinNeg == true && spinPos == true) {
            return 2;
        } else if(spinPos == true && spinNeg == false) {
            return 1;
        } else {
            return 0;
        }
    }

    

    /**
     *  add an electron into the orbital and retrn orbital state
     * @return number of electrons in orbital after adding an electron
     */
    public boolean addElectron() {

        switch (getNumElectrons()) {
            case 0:
                spinPos = true;
                updateStability();
                return true;
            case 1:
                spinNeg = true;
                updateStability();
                return true;
            default:
                // electrons are already 2(full)
                // throw new ElectronicConfigurationException("Cannot add electron in the orbital as it is already full.");
                break;
        }
        return false;
    }

    /**
     *  remove an electron from the orbital and retrn orbital state
     * @return weather electron is removed or not from the orbital
     */
    public boolean removeElectron() {

        switch (getNumElectrons()) {
            case 1:
                spinPos = false;
                updateStability();
                return true;
            case 2:
                spinNeg = false;
                updateStability();
                return true;
            default:
                // electrons are already 0(empty)
                //throw new ElectronicConfigurationException("Cannot remove electron from the orbital as it is already empty.");
                break;
        }
        return false;
    }
}
