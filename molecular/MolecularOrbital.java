package molecular;

import subatomicparticles.ElectronicConfigurationException;
import subatomicparticles.Orbital;
import subatomicparticles.SubShell.SubShellBlock;

public class MolecularOrbital extends Orbital {

    public static final int TYPE_BONDING = 1;
    public static final int TYPE_ANTI_BONDING = 2;
    
    private int _type;
    private String orbitalName;
    private SubShellBlock block;
        
    public MolecularOrbital(int nElectrons) throws ElectronicConfigurationException {
        this(nElectrons, TYPE_BONDING);
    }

    public MolecularOrbital(int nElectrons, int orbitaltype) throws ElectronicConfigurationException {
        super(nElectrons);
        _type = orbitaltype;
    }

    public MolecularOrbital(boolean PositiveSpin, boolean NegativeSpin, int orbitaltype) {
        super(PositiveSpin, NegativeSpin);
        _type = orbitaltype;
    }

    public MolecularOrbital(boolean PositiveSpin, boolean NegativeSpin) {
        this(PositiveSpin, NegativeSpin, TYPE_BONDING);
    }

    public MolecularOrbital(boolean PositiveSpin) {
        this(PositiveSpin, false);
    }

    public MolecularOrbital() {
        this(false);
    }

    public void setSubShellBlock(SubShellBlock blk) {
        block = blk;
    }

    public SubShellBlock getSubShellBlock() {
        return block;
    }

    public int getOrbitalType() {
        return _type;
    }

    public boolean isBonding() {
        return _type == TYPE_BONDING;
    }

    public boolean isAntiBonding() {
        return _type == TYPE_ANTI_BONDING;
    }

    public String getOrbitslName() {
        return _type == TYPE_BONDING ? orbitalName : orbitalName.concat("*");
    }

}
