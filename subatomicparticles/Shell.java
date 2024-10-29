package subatomicparticles;


public class Shell {

    private int atomicNumber;
    public int shellNum;
    private int MAX_SUBSHELLS;
    private int MAX_ELECTRONS;
    private float effectiveAtomicNumber = 1.0f;

    public static final int SUBSHELL_S = 0;
    public static final int SUBSHELL_P = 1;
    public static final int SUBSHELL_D = 2;
    public static final int SUBSHELL_F = 3;

    protected SubShell[] subShells;

    public Shell(int shellNum, int atomicNumber) {
        if (shellNum <= 0)
            throw new ElectronicConfigurationException("Shell not created as minimum shell no that can exist is 1.");
        this.atomicNumber = atomicNumber;    
        this.shellNum = shellNum;
        this.MAX_SUBSHELLS = shellNum;
        this.MAX_ELECTRONS = 2 * shellNum * shellNum;
        this.subShells = new SubShell[shellNum];
        addSubshells();
    }

    public void setEffectiveAtomicNumber(float effectiveZ) {
        effectiveAtomicNumber = effectiveZ;
    }

    /**
     * Modified Bhor Radius using Effective Atomic Number
     * @param orbitNum
     * @return bhor radius in angstorm
     */
    public double getBhorRadius() {
        return 0.529f * (Math.pow(shellNum , 2) / (double)effectiveAtomicNumber);
    }

    public double getStationaryOrbitEnergy() {
        return (-13.6f) * Math.pow(((float)effectiveAtomicNumber/ (float) shellNum), 2);
    }

    public double getStationaryOrbitEnergyinJoule() {
        return -1 * (2.18E-18f) * Math.pow(((float)effectiveAtomicNumber/ (float) shellNum), 2);
    }

    public double getPotentialEnergy() {
        return 2 * getStationaryOrbitEnergy();
    }

    public double getPotentialEnergyinJoule() {
        return 2 * getStationaryOrbitEnergyinJoule();
    }

    /**
     * 
     * @return speed of electron in m/s
     */
    public float speedOfElectron() {
        return 2.18E+6f * ((float)atomicNumber / (float)shellNum);
    }

    public double revlutionPerSecond() {
        return speedOfElectron() / ( 2 * (float)Math.PI * getBhorRadius());
    }

    public int getMaxElectrons() {
        return this.MAX_ELECTRONS;
    }

    public boolean validForSubShellAddition(int subshellNumber) {
        //  1s 2s 2p 3s 3p 4s 3d 4p
        return subshellNumber <= MAX_SUBSHELLS;
    }

    public void addSubshells() {
        for (int j = 0; j < subShells.length; j++) {
            if(validForSubShellAddition(j)) {
                SubShell.SubShellBlock block = SubShell.SubShellBlock.getSubShellBlock(j);
                if(block != SubShell.SubShellBlock.UNKNOWN) {
                    subShells[j] = new SubShell(this.shellNum, block);
                }
            }
        }
    }


    /**
     * Subshell numbers are( s -> 0, p -> 1, d -> 2, f -> 3)
     * @param subshellNumber
     * @return
     */
    public int getEnergyLevelInt(int subshellNumber) {
        if (subshellNumber >= MAX_SUBSHELLS) {
            return -1;
        }
        return this.subShells[subshellNumber].getEnergyLevelInt();
    }

    /**
     * Subshell numbers are( s -> 0, p -> 1, d -> 2, f -> 3)
     * @param subshellNumber
     * @return
     */
    public EnergyLevel getEnergyLevel(int subshellNumber) {
        if (subshellNumber >= MAX_SUBSHELLS) {
            return null;
        }
        return this.subShells[subshellNumber].getEnergyLevel();
    }


    public boolean containsSubshellS() {
        return MAX_SUBSHELLS > SUBSHELL_S;
    }

    public boolean containsSubshellP() {
        return MAX_SUBSHELLS > SUBSHELL_P;
    }

    public boolean containsSubshellD() {
        return MAX_SUBSHELLS > SUBSHELL_D;
    }

    public boolean containsSubshellF() {
        return MAX_SUBSHELLS > SUBSHELL_F;
    }

    /**
     * add an electron in this shell.
     * @return weather electron is added in the shell or not.
     */
    public boolean addElectron() {
        return addElectrons(1) == 1; // 1 electron added
    }

    /**
     * add electrons to this shell
     * @param number of elecreons added
     * @return
     */
    public int addElectrons(int n) {
        int nn = n;
        if(this.getNumberElectrons() < this.getMaxElectrons()) {
            for (int j = 0; j < this.subShells.length; j++) {
                if (nn > 0) {
                    if(this.subShells[j].available() > 0) {
                        if(this.subShells[j].addElectron())
                            nn--;
                    }
                } else {
                    break;
                }
            }
        }
        return n - nn;
    }

    public boolean removeElectron() {
        return removeElectrons(1) == 1; // to remove 1 electron.
    }

    /**
     * remove electrons from the shell.
     * @param int number of electrons to remove.
     * @return number of electrons removed.
     */
    public int removeElectrons(int n) {
        int nn = n;
        for (int i = subShells.length - 1; i >= 0; i--) {
            if (nn > 0) {
                if(subShells[i].removeElectron())
                    nn--;
            } else {
                break;
            }
        }
        return n - nn;
    }

    public int getNumberElectrons() {
        int n = 0;
        for(int i = 0; i < subShells.length; i++) {
            n += subShells[i] != null ? subShells[i].getNumberElectrons() : 0;
        }
        return n;
    }

}
