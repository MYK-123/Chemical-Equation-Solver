package subatomicparticles;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import subatomicparticles.EnergyLevel.EnergyLevelUsage;
import subatomicparticles.SubShell.SubShellBlock;

public class ElectrincConfiguration {

    private int atomicNumber;
    Shell[] shells;

    private static final int LANTHENOIDS_START = 57;
    private static final int LANTHENOIDS_END = 71;
    private static final int ACTENOIDS_START = 89;
    private static final int ACTENOIDS_END = 103;

    public ElectrincConfiguration() throws ElectronicConfigurationException {
        this(0);
    }

    public ElectrincConfiguration(int atomicNumber) throws ElectronicConfigurationException {
        this(atomicNumber, atomicNumber);
    }

    public ElectrincConfiguration(int nElectrons, int atomicNumber) throws ElectronicConfigurationException {
        this.shells = new Shell[7];
        this.atomicNumber = atomicNumber;
        addShell();
        addElectrons(nElectrons);
    }

    public void addShell() throws ElectronicConfigurationException {
        for (int j = 0; j < shells.length; j++) {
            shells[j] = new Shell(j + 1, this.atomicNumber);
        }
    }

    public boolean removeElectron() {
        return removeElectrons(1) == getNumberElectrons();
    }

    public int removeElectrons(int n) {
        return addElectrons(-n);
    }

    public boolean addElectron() {
        return addElectrons(1) == getNumberElectrons();
    }

    private boolean addElectronIn(int shellNum, int subshellNum) {
        return shells[shellNum - 1].subShells[subshellNum].addElectron();
    }

    private boolean removeElectronFrom(int shellNum, int subshellNum) {
        return shells[shellNum - 1].subShells[subshellNum].removeElectron();
    }

    public SubShell getSubShell(int shellNum, int subshellNum) {
        return shells[shellNum - 1].subShells[subshellNum];
    }

    /**
     * @algorithm - 
     *  fill electron in first shell
     *      fill electron in first subshell
     *  fill electron in second shell
     *      fill electron in first subshell of second shell
     * 
     * @return
     */
    public int addElectrons(int n) {
        if (n == 0)
            return 0;

        int nn = getNumberElectrons();
        int ne = nn + n;

        if (ne < 0)
            return ne;

        List<EnergyLevel> energyLvls = new ArrayList<>();
        
        for (int i = 1; i <= 7; i++) {
            if(ne <= 0)
            break;
            for (int j = 0; j < i; j++) {
                if(ne <= 0)
                    break;
                if(energyLvls.add(new EnergyLevel(i, j)))
                    ne--;
            }
        }
        energyLvls.sort(new Comparator<EnergyLevel>() {
            @Override
            public int compare(EnergyLevel o1, EnergyLevel o2) {
                return EnergyLevel.compare(o1, o2);
                //return r;// == 0 ? -1: r;
            }
        });

        addShell(); // reset all shells

        int nnn = nn + n;
        int added = 0;
        for (int N = 0; N < energyLvls.size(); N++) {
            EnergyLevel energyLevel = energyLvls.get(N);
            EnergyLevel energyLevelPrev = (N-1) > 0 ? energyLvls.get(N - 1) : null;
            EnergyLevel energyLevelNext = (N+1) < energyLvls.size() ? energyLvls.get(N + 1) : null;
            if (nnn <= 0) {
                break;
            }

            if (shells[energyLevel.getShellNumber() - 1] == null){
                shells[energyLevel.getShellNumber() - 1] = new Shell(energyLevel.getShellNumber(), this.atomicNumber);
            }
            SubShell sss = getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber());
            EnergyLevel eL = sss != null ? sss.getEnergyLevel() : null;
            if (energyLevel.equals(eL)) {
                EnergyLevel.EnergyLevelUsage state = eL.getEnergyLevelState();
                FILLING_LOOP:
                while (state != EnergyLevelUsage.COMPLEATELY_FILLED) {
                    if (nnn <= 0) {
                        break;
                    }
                    if (state == EnergyLevelUsage.UNFILLED || state == EnergyLevelUsage.PARTIALLY_FILLED) {
                        SubShell cSubShell = getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber());
                        int blockNum = cSubShell.getBlockNumber();
                        if (blockNum == SubShellBlock.S_BLOCK.getNumber() || blockNum == SubShellBlock.P_BLOCK.getNumber()) {
                            // method for only s and p block of subshell
                            if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                added++;
                                nnn--;
                            }

                        } else if (blockNum == SubShellBlock.D_BLOCK.getNumber()) {
                            // method for d block of subshell only

                            Stability currStability = getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber()).getStability();
                            if (currStability == Stability.HALF_FILLED || currStability == Stability.FILLED) {
                                // currently in more-stable state
                                if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                    added++;
                                    nnn--;
                                }

                            } else {
                                // currently not in more-stable state
                                if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                    added++;
                                    nnn--;
                                }

                                if (getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber()).addOneToStability()) {
                                    // adding one to it will make it stable
                                    if (nnn == 0) {
                                        if (getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber()).addOneToStability()) {
                                            // one more is needed to make it more-stable
                                            // so we will get it from prevSubshell
                                            // e.g, if currSubshell is 3d thenwe will get it from 4s to 3d to make currSubshell more stable
                                            assert(energyLevelPrev != null);
                                            // to assure myself that prev Subshell is not null
                                            if (energyLevelPrev.getSubshellNumber() == SubShellBlock.S_BLOCK.getNumber()) {
                                                if(removeElectronFrom(energyLevelPrev.getShellNumber(), energyLevelPrev.getSubshellNumber())) {
                                                    added--;
                                                    nnn++;
                                                }
                                                
                                                if (nnn > 0) {
                                                    if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                                        added++;
                                                        nnn--;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    
                                } else if (getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber()).removeOneToStability()) {
                                    // removing one will make it more stable
                                    if (removeElectronFrom(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                        added--;
                                        nn++;
                                    }
                                }
                            }

                            
                        } else if (blockNum == SubShellBlock.F_BLOCK.getNumber()) {
                            // method for f block subshell only

                            if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                added++;
                                nnn--;
                            }

                            int nCurrElectrons = getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber()).getNumberElectrons();
                            int nNextElectrons = getSubShell(energyLevelNext.getShellNumber(), energyLevelNext.getSubshellNumber()).getNumberElectrons();
                            if (atomicNumber >= LANTHENOIDS_START && atomicNumber <= LANTHENOIDS_END){
                                // LANTHENOIDS
                                if (atomicNumber == LANTHENOIDS_START) {
                                    // no f electrons are present in lanthenum
                                    if (removeElectronFrom(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                        added--;
                                        nnn++;
                                    }
                                    break FILLING_LOOP;
                                }

                                if (nCurrElectrons == 2 || nCurrElectrons == 8) {
                                    if(nNextElectrons == 0) {
                                        if (removeElectronFrom(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                            added--;
                                            nnn++;
                                            if (addElectronIn(energyLevelNext.getShellNumber(), energyLevelNext.getSubshellNumber())) {
                                                added++;
                                                nnn--;
                                            }
                                        }
                                    } else {
                                        if (removeElectronFrom(energyLevelNext.getShellNumber(), energyLevelNext.getSubshellNumber())) {
                                            added--;
                                            nnn++;
                                            if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                                added++;
                                                nnn--;
                                            }
                                        }
                                    }
                                }
                                
                            } else if (atomicNumber >= ACTENOIDS_START && atomicNumber <= ACTENOIDS_END){
                                // ACTENOIDS
                                // Some actonoids are not configgured properly
                                // have to do it again
                                //
                                if ((atomicNumber == ACTENOIDS_START /* 89 */ || atomicNumber == 90) && energyLevel.getShellNumber() == 5) {
                                    // thorium
                                    if (removeElectronFrom(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                        added--;
                                        nnn++;
                                    }
                                    break FILLING_LOOP;
                                }

                                if (nCurrElectrons == 5 || nCurrElectrons == 8) {
                                    if(nNextElectrons == 0) {
                                        if (removeElectronFrom(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                            added--;
                                            nnn++;
                                            if (addElectronIn(energyLevelNext.getShellNumber(), energyLevelNext.getSubshellNumber())) {
                                                added++;
                                                nnn--;
                                            }
                                        }
                                    } else {
                                        if (removeElectronFrom(energyLevelNext.getShellNumber(), energyLevelNext.getSubshellNumber())) {
                                            added--;
                                            nnn++;
                                            if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                                added++;
                                                nnn--;
                                            }
                                        }
                                    }
                                } else if (nCurrElectrons > 2 && nNextElectrons == 0){
                                    if (nnn == 0) {
                                        if (removeElectronFrom(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                            added--;
                                            nnn++;
                                        }
                                        if (addElectronIn(energyLevelNext.getShellNumber(), energyLevelNext.getSubshellNumber())) {
                                            added++;
                                            nnn--;
                                        }
                                    }
                                }
                                
                            } else {
                                // not f-block element
                                // i.e, f-block is fully filled
                                if (addElectronIn(energyLevel.getShellNumber(), energyLevel.getSubshellNumber())) {
                                    added++;
                                    nnn--;
                                }
                            }
                        }      
                         
                    }
                    //state = eL.getEnergyLevelState();
                    state = getSubShell(energyLevel.getShellNumber(), energyLevel.getSubshellNumber()).getEnergyLevelState();
                }
            }
        }
        return added;
    }

    @Override
    public String toString() {
        return "Electronic Configuration : " + configurationString();
    }

    public String configurationString2() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < shells.length; i++) {
            for (int j = 0; j < shells[i].subShells.length; j++) {
                SubShell s = shells[i].subShells[j];
                if (s != null) {
                    builder.append(s.toString());
                }
            }
        }

        // remove last 2 charecters (", ") for formatted output.
        return builder.length() > 2 ? builder.substring(0, builder.length() - 2) : builder.toString();
    }

    public String configurationString() {
        List<EnergyLevel> energyLvls = new ArrayList<>();
        int ne = getNumberElectrons();
        for (int i = 1; i <= 7; i++) {
            if(ne <= 0)
            break;
            for (int j = 0; j < i; j++) {
                if(ne <= 0)
                    break;
                if(energyLvls.add(new EnergyLevel(i, j)))
                    ne--;
            }
        }
        energyLvls.sort(new Comparator<EnergyLevel>() {
            @Override
            public int compare(EnergyLevel o1, EnergyLevel o2) {
                return EnergyLevel.compare(o1, o2);
                //return r;// == 0 ? -1: r;
            }
        });
        StringBuffer sb = new StringBuffer();
        for (EnergyLevel energyLevel : energyLvls) {
            SubShell subShell = shells[energyLevel.getShellNumber() - 1].subShells[energyLevel.getSubshellNumber()];
            if (subShell != null)
                sb.append(subShell.toString());
        }
        // remove last 2 charecters (", ") for formatted output.
        return sb.length() > 2 ? sb.substring(0, sb.length() - 2) : sb.toString();
    }

    public double atomicRadius() {
        double radius = 0.0f;
        for (int i = 0; i < shells.length; i++) {
            System.out.println("prev radius" + radius);
            if (shells[i].getNumberElectrons() == 0)
                break;
            radius = shells[i].getBhorRadius();
            System.out.println(shells[i].getNumberElectrons());
        }
        return Math.abs(radius);
    }

    public Valencies getValencies() {
        String[] arr = configurationString().split(", ");   // electronic configuration in split form
        // generally last indexes are s, f, d, p order if none of the subshells are absent

        int nSShell[] = new int[4];
        int nSShell_E[] = new int[4];
        
        final int index_S = 0;
        final int index_P = 1;
        final int index_D = 2;
        final int index_F = 3;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i].contains("S")) {
                nSShell[index_S] = arr[i].charAt(0) - '0';
                nSShell_E[index_S] = Integer.valueOf(arr[i].substring(arr[i].indexOf("S") + 1)).intValue();
                
            } else if (arr[i].contains("P")) {
                nSShell[index_P] = arr[i].charAt(0) - '0';
                nSShell_E[index_P] = Integer.valueOf(arr[i].substring(arr[i].indexOf("P") + 1)).intValue();
            } else if (arr[i].contains("D")) {
                nSShell[index_D] = arr[i].charAt(0) - '0';
                nSShell_E[index_D] = Integer.valueOf(arr[i].substring(arr[i].indexOf("D") + 1)).intValue();
            } else if (arr[i].contains("F")) {
                nSShell[index_F] = arr[i].charAt(0) - '0';
                nSShell_E[index_F] = Integer.valueOf(arr[i].substring(arr[i].indexOf("F") + 1)).intValue();
            }
        }

        // for Debug purpose only
        // for (int i = 0; i < 4; i++) {
        //     System.out.println("SPDF??? " + nSShell[i] + "  " + nSShell_E[i]);
        // }
        

        // Now validate all subshells weather they are outer or not
        // and output only outermost subshells
        // zero out all other subshels

        
        int NNNN = 0;
        // s is always correct;
        NNNN = nSShell[index_S];
        // p is correct only if nsx  and Npy
        // i.e, n==N (if N<n then p is becomes obselete)
        if (NNNN != nSShell[index_P]) {
            nSShell_E[index_P] = 0;
        }
        // d is correct only if nsx and Ndy and npz
        // and N = n-1 only
        // otherwise d becomes obselete
        if (NNNN != (nSShell[index_D] + 1)) {
            nSShell_E[index_D] = 0;
        }
        // f is correct only if nsx and N2fxx and N1dy and npz
        // and N1 = n-1 only
        // and N2 = N1-1 or N2 = n-2 only
        // otherwise f becomes obselete
        if (NNNN != (nSShell[index_F] + 2)) {
            nSShell_E[index_F] = 0;
        }

        return Valencies.parse(nSShell_E[index_S],
                 nSShell_E[index_P],
                 nSShell_E[index_D],
                 nSShell_E[index_F],
                 nSShell[index_S]);

    }

    public Shell getValenceShell(Valencies valencies) {
        if(valencies == null || valencies.getValenceShellNo() > shells.length 
        || valencies.getValenceShellNo() < 1){
            return null;
        }
        return shells[valencies.getValenceShellNo() - 1];
    }

    public int getNumberElectrons() {
        int n = 0;
        for (int i = 0; i < shells.length; i++) {
            n += shells[i].getNumberElectrons();
        }
        return n;
    }
    
    public float shieldingConstant() {
        float effect = 0.0f;

        final float SHELL_CONST_LAST = 0.35f;
        final float SHELL_CONST_SECONT_LAST = 0.85f;
        final float SHELL_CONST_THIRD_LAST = 1.0f;

        int lastShell = 0;
        for (int i = 0; i < shells.length; i++) {
            for (int j = 0; j < shells[i].subShells.length; j++) {
                SubShell s = shells[i].subShells[j];
                if (s != null && s.getNumberElectrons() != 0) {
                    if (lastShell <= i) {
                        lastShell = i;
                    }
                }
            }
        }

        int shellsUsed = 0;
        boolean DorFfound = false;

        for (int i = lastShell; i >= 0; i--) {

            int nsp = 0, nd = 0, nf = 0;
            float constant;

            if (shellsUsed == 0) {
                constant = SHELL_CONST_LAST;
            } else if (shellsUsed == 1) {
                constant = SHELL_CONST_SECONT_LAST;
            } else if (shellsUsed == 2) {
                constant = SHELL_CONST_THIRD_LAST;
            } else {
                constant = SHELL_CONST_THIRD_LAST;
            }

            if (DorFfound) {
                constant = SHELL_CONST_THIRD_LAST;
            }

            
            for (int j = 0; j < shells[i].subShells.length; j++) {
                SubShell s = shells[i].subShells[j];
                if (s != null && s.getNumberElectrons() != 0) {
                    int block = s.getBlockNumber();
                    if (block == SubShellBlock.S_BLOCK.getNumber() || block == SubShellBlock.P_BLOCK.getNumber()) {
                        nsp += s.getNumberElectrons();
                    } else if (block == SubShellBlock.D_BLOCK.getNumber()) {
                        nd += s.getNumberElectrons();
                        DorFfound = true;
                    } else if (block == SubShellBlock.F_BLOCK.getNumber()) {
                        nf += s.getNumberElectrons();
                        DorFfound = true;
                    }

                    if (constant == SHELL_CONST_LAST) {
                        nsp = 1;
                    }
                    
                }
            }
            effect += constant * nsp + constant * nd + constant * nf;
            System.out.println("EE + " + constant);
            shellsUsed++;
        }
        
        return effect;
    }

    public float setShieldingEffect() {
        float effectZ = atomicNumber - shieldingConstant();
        for (int i = 0; i < shells.length; i++) {
            shells[i].setEffectiveAtomicNumber(effectZ);
        }
        return effectZ;
    }

    public double getIonizationEnergy() {
        
        int lastShell = 0;
        for (int i = 0; i < shells.length; i++) {
            for (int j = 0; j < shells[i].subShells.length; j++) {
                SubShell s = shells[i].subShells[j];
                if (s != null && s.getNumberElectrons() != 0) {
                    if (lastShell <= i) {
                        lastShell = i;
                    }
                }
            }
        }

        return Math.abs(shells[lastShell].getStationaryOrbitEnergy());
    }

}
