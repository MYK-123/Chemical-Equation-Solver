package molecular;

import java.util.ArrayList;

import subatomicparticles.Atom;
import subatomicparticles.Orbital;

public class Hybridisation {
    
    public enum HybridisationTypes {
      SS,
      SP,
      SP2,
      SP3,
      SP3D,
      SP3D2,

      UNKNOWN
    };

    public enum Shape {
        NA,
        LINEAR,
        TRAGONAL_PLANER,
        BENT,
        TETRAHEDRAL,
        PYRAMIDAL,
        TRAGONAL_BIPYRAMIDAL,
        SEE_SAW,
        T_SHAPED,
        OCTAHEDRAL,
        SQUARE_PYRAMIDAL,
        SQUARE_PLANER
    };

    private HybridisationTypes type;
    private Shape shape;
    private Atom ownerAtom;
    private ArrayList<Orbital> hybridOrbitals;

    public Hybridisation(Atom atom) {
        ownerAtom = atom;
        hybridOrbitals = null;
        updateProperties();
    }

    public Atom getOwnerAtom() {
        return ownerAtom;
    }

    public void updateProperties() {
        calculateHybridOrbitals();
        calculateHybridisationType();
        calculateShape();
    }


    private void calculateHybridisationType() {
        switch (hybridOrbitals.size()) {
            case 1:type = HybridisationTypes.SS;break;
            case 2:type = HybridisationTypes.SP;break;
            case 3:type = HybridisationTypes.SP2;break;
            case 4:type = HybridisationTypes.SP3;break;
            case 5:type = HybridisationTypes.SP3D;break;
            case 6:type = HybridisationTypes.SP3D2;break;
            default:type = HybridisationTypes.UNKNOWN;break;
        }
    }
    
    private boolean addOrbital(int nOrbital) {
        if (nOrbital < 0 || nOrbital >= hybridOrbitals.size()) {
            return hybridOrbitals.add(new Orbital());
        }
        return true;
    }

    private boolean addElectron(int nOrbital) {
        if (nOrbital < 0 || nOrbital >= hybridOrbitals.size()) {
            return false;
        }
        return hybridOrbitals.get(nOrbital).addElectron();
    }

    private void calculateHybridOrbitals() {
        if(hybridOrbitals == null) {
            hybridOrbitals = new ArrayList<>();
        }
        hybridOrbitals.clear();
        int ne = 0;
        ne += ownerAtom.getValenceElectrons();
        for (BondedAtom bondedAtom : ownerAtom.getBondedAtoms()) {
            ne += bondedAtom.getNumberOfBondNumber();
        }
        int nOrbital = 0;
        while (ne > 0) {
            if (addElectron(nOrbital)) {
                ne--;
            } else {
                nOrbital++;
                addOrbital(nOrbital);
            }
        }
    }

    public HybridisationTypes getHybridisationType() {
        return type;
    }

    public String getHybridisationTypeStr() {
        switch (type) {
            case SS: return "N/A";
            case SP: return "SP";
            case SP2: return "SP2";
            case SP3: return "SP3";
            case SP3D: return "SP3D";
            case SP3D2: return "SP3D2";
            default:break;
        }
        return "Unknown";
    }

    private void calculateShape() {
        int lonePairs = ownerAtom.getLonePairNumber();
        if (lonePairs == 0) {
         switch (type) {
            case SP: shape = Shape.LINEAR;break;
            case SP2: shape = Shape.TRAGONAL_PLANER;break;
            case SP3: shape = Shape.TETRAHEDRAL;break;
            case SP3D: shape = Shape.TRAGONAL_BIPYRAMIDAL;break;
            case SP3D2: shape = Shape.OCTAHEDRAL;break;
            default: shape = Shape.NA;break;
         }   
        }
        else if (lonePairs == 1) {
            switch (type) {
                case SP: shape = Shape.LINEAR;break;
                case SP2: shape = Shape.BENT;break;
                case SP3: shape = Shape.PYRAMIDAL;break;
                case SP3D: shape = Shape.SEE_SAW;break;
                case SP3D2: shape = Shape.SQUARE_PYRAMIDAL;break;
                default: shape = Shape.NA;break;
             }
        }
        else if (lonePairs == 2) {
            switch (type) {
                case SP2: shape = Shape.LINEAR;break;
                case SP3: shape = Shape.BENT;break;
                case SP3D: shape = Shape.T_SHAPED;break;
                case SP3D2: shape = Shape.SQUARE_PLANER;break;
                default: shape = Shape.NA;break;
             }
        }
        else if (lonePairs == 3) {
            switch (type) {
                case SP3D: shape = Shape.LINEAR;break;
                default: shape = Shape.NA;break;
             }
        }
        else {
            shape = Shape.NA;
        }
    }

    public Shape getShape() {
        return shape;
    }

    public String getShapeName() {
        switch (shape) {
            case NA: return "N/A";
            case BENT: return "Bent";
            case LINEAR: return "Linear";
            case OCTAHEDRAL: return "Octahedral";
            case PYRAMIDAL: return "Pyramidal";
            case SEE_SAW: return "See-Saw";
            case SQUARE_PLANER: return "Square Planer";
            case SQUARE_PYRAMIDAL: return "Square Pyramidal";
            case TETRAHEDRAL: return "Tetrahedral";
            case TRAGONAL_BIPYRAMIDAL: return "Tragonal BiPyramidal";
            case TRAGONAL_PLANER: return "Tragonal Planer";
            case T_SHAPED: return "T-Shaped";
            default:break;
        }
        return null;
    }

}
