package subatomicparticles;

import subatomicparticles.EnergyLevel.EnergyLevelUsage;

public class SubShell {
    
    public static enum SubShellBlock {
        S_BLOCK(0, "S"),
        P_BLOCK(1, "P"),
        D_BLOCK(2, "D"),
        F_BLOCK(3, "F"),
        UNKNOWN(4, "Unknown");

        private int n;
        private String name;

        SubShellBlock(int i, String _name) {
            n = i;
            name = _name;
        }

        int getNumber() {
            return n;
        }

        public String toString() {
            return name;
        }

        static SubShellBlock getSubShellBlock(int num) {
            switch(num) {
                case 0:
                    return S_BLOCK;
                case 1:
                    return P_BLOCK;
                case 2:
                    return D_BLOCK;
                case 3:
                    return F_BLOCK;
                default:
                    return UNKNOWN;
            }
        }

    }

    private SubShellBlock block;
    public int MAX_ELECTRONS;
    public int HALF_MAX_ELECTRONS;
    public Orbital orbitals[];
    public EnergyLevel mEnergyLevel;
    public Stability stability;

    public SubShell(int shellNum, SubShellBlock block, int nElectrons) throws ElectronicConfigurationException{
        this(shellNum, block);
        addElectrons(nElectrons);
    }

    public SubShell(int shellNum, int blockNum, int nElectrons) throws ElectronicConfigurationException {
        this(shellNum, SubShellBlock.getSubShellBlock(blockNum), nElectrons);
    }

    public SubShell(int shellNum, int blockNum) {
        this(shellNum, SubShellBlock.getSubShellBlock(blockNum));
    }

    public SubShell(int shellNum, SubShellBlock block) {
        this.block = block;
        this.mEnergyLevel = new EnergyLevel(shellNum, block.getNumber());
        switch (block) {
            case S_BLOCK:
                MAX_ELECTRONS = 2;
                HALF_MAX_ELECTRONS = 1;
                orbitals = new Orbital[1];
                break;
            case P_BLOCK:
                MAX_ELECTRONS = 6;
                HALF_MAX_ELECTRONS = 3;
                orbitals = new Orbital[3];
                break;
            case D_BLOCK:
                MAX_ELECTRONS = 10;
                HALF_MAX_ELECTRONS = 5;
                orbitals = new Orbital[5];
                break;
            case F_BLOCK:
                MAX_ELECTRONS = 14;
                HALF_MAX_ELECTRONS = 7;
                orbitals = new Orbital[7];
                break;
            default:
                MAX_ELECTRONS = 0;
                HALF_MAX_ELECTRONS = 0;
                orbitals = new Orbital[0];
                break;
        }
        for (int i = 0; i < orbitals.length; i++) {
            orbitals[i] = new Orbital();
        }
        updateStability();
    }

    public double orbitalAngualrMomentum() {
        return Math.sqrt(block.n * (block.n + 1)) * (6.625E-34 / 2 * Math.PI);
    }

    public double magneticMoment() {
        return Math.sqrt(block.n * (block.n + 1)) * 9.27E-14;
    }

    /**
     * checks weather the subshell has acheived temporary stability.
     * @return true when subshell has 2 or 8 electrons present in it, otherwise false.
     */
    public boolean isOctetOrDupletAcheived() {
        int n = getNumberElectrons();
        return n == 2 || n == 8;
    }

    public Stability getStability() {
        return this.stability;
    }

    public boolean addOneToStability() {
        int n = getNumberElectrons() + 1;
        return n == MAX_ELECTRONS || (2 * n) == MAX_ELECTRONS;
    }

    public boolean removeOneToStability() {
        int n = getNumberElectrons() - 1;
        return n == MAX_ELECTRONS || (2 * n) == MAX_ELECTRONS;
    }

    
    private void updateStability() {
        int n = getNumberElectrons();
        if (n == MAX_ELECTRONS) {
            stability = Stability.FILLED;
        } else if (2* n == MAX_ELECTRONS) {
            // i.e,  n==MAX_ELECTRONS/2
            stability = Stability.HALF_FILLED;
        } else if (n == 0) {
            stability = Stability.EMPTY;
        } else {
            stability = Stability.PARTIALLY_FILLED;
        }

        this.mEnergyLevel.setStability(stability);
    }

    /**
     * For number of electrons that can be added  to this subshell
     * @return - number of electrons which can be added to this shell Maximum.
     */
    public int available() {
        return MAX_ELECTRONS - getNumberElectrons();
    }

    public EnergyLevel.EnergyLevelUsage getEnergyLevelState() {
        int n = getNumberElectrons();
        if (n == 0) {
            return EnergyLevelUsage.UNFILLED;
        } else if (n == MAX_ELECTRONS) {
            return EnergyLevelUsage.COMPLEATELY_FILLED;        
        } else {
            return EnergyLevelUsage.PARTIALLY_FILLED;
        }
    }

    public int getEnergyLevelInt() {
        return mEnergyLevel.getEnergyLevel();
    }

    public EnergyLevel getEnergyLevel() {
        return mEnergyLevel;
    }

    public int getBlockNumber() {
        return this.block.getNumber();
    }
    public int getNumberElectrons() {
        int n = 0;
        for (Orbital orbital : orbitals) {
            n += orbital.getNumElectrons();
        }
        return n;
    }

    public boolean addElectron() {
        boolean b = __addElectron();
        mEnergyLevel.setEnergyLevelState(getEnergyLevelState());
        updateStability();
        return b;
    }

    private boolean __addElectron() {
        try {
            int n = getNumberElectrons();
            return (n+1) == addElectrons(1);
        } catch (ElectronicConfigurationException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    private boolean isPairingEnabled() {
        return 2 * getNumberElectrons() >= MAX_ELECTRONS;
    }

    public int addElectrons(int nElectron) throws ElectronicConfigurationException {
        if( (getNumberElectrons() + nElectron) > MAX_ELECTRONS) 
            throw new ElectronicConfigurationException("Cannot add " + nElectron + " electron(s) in this orbital as " + getNumberElectrons() +  " as it has not enough space in it.");
        int ne = nElectron;
        for(int j = 1; j <= 2; j++) {
            for(int i = 0; i < orbitals.length; i++ ) {
                if(ne > 0){
                    if(!isPairingEnabled()) {
                        if(orbitals[i].getNumElectrons() == 0) {
                            if(orbitals[i].addElectron())
                                ne--;
                        }
                    } else {
                        if (orbitals[i].getNumElectrons() == 1) {
                            if(orbitals[i].addElectron())
                                ne--;
                        }
                    }
                }
            }
        }
        mEnergyLevel.setEnergyLevelState(getEnergyLevelState());
        updateStability();
        return getNumberElectrons();
    }

    public int removeElectrons(int n) {
        for (int i = 0; i < n; i++) {
            removeElectron();
        }
        updateStability();
        return getNumberElectrons();
    }

    public boolean removeElectron() {
        boolean b = __removeElectron();
        mEnergyLevel.setEnergyLevelState(getEnergyLevelState());
        updateStability();
        return b;
    }

    private boolean __removeElectron() {
        int nE = getNumberElectrons();
        switch (block) {
            case S_BLOCK:
                switch (nE) {
                    case 1:
                    case 2:
                        return orbitals[0].removeElectron();
                    default:
                        break;
                }
                break;
            case P_BLOCK:
                switch (nE) {
                    case 1:
                    case 4:
                        return orbitals[0].removeElectron();
                    case 2:
                    case 5:
                        return orbitals[1].removeElectron();
                    case 3:
                    case 6:
                        return orbitals[2].removeElectron();
                    default:
                        break;
                }
                break;
            case D_BLOCK:
                switch (nE) {
                    case 1:
                    case 6:
                        return orbitals[0].removeElectron();
                    case 2:
                    case 7:
                        return orbitals[1].removeElectron();
                    case 3:
                    case 8:
                        return orbitals[2].removeElectron();
                    case 4:
                    case 9:
                        return orbitals[3].removeElectron();
                    case 5:
                    case 10:
                        return orbitals[4].removeElectron();
                    default:
                        break;
                }
                break;
            case F_BLOCK:
                switch (nE) {
                    case 1:
                    case 8:
                        return orbitals[0].removeElectron();
                    case 2:
                    case 9:
                        return orbitals[1].removeElectron();
                    case 3:
                    case 10:
                        return orbitals[2].removeElectron();
                    case 4:
                    case 11:
                        return orbitals[3].removeElectron();
                    case 5:
                    case 12:
                        return orbitals[4].removeElectron();
                    case 6:
                    case 13:
                        return orbitals[5].removeElectron();
                    case 7:
                    case 14:
                        return orbitals[6].removeElectron();
                    default:
                        break;
                }
                break;
            default:
                break;
        }
        return false;
    }

    @Override
    public String toString() {
        int nE = this.getNumberElectrons();
        if (nE == 0)
            return "";
        return String.format("%d%s%d, ", this.mEnergyLevel.getShellNumber(), this.block.toString() /* subshell */, nE);
    }

}
