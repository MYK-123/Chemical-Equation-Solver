package subatomicparticles;

public class Cation extends Ion {

    public Cation(int Z) throws AtomException {
        super(Z, 0.0f);
    }

    public Cation(int Z, float A) throws AtomException {
        super(Z, A, "");
    }

    public Cation(int Z, float A, String name) throws AtomException {
        super(Z, A, name, 0);
    }

    public Cation(int Z, float A, String name, float chargeOfAtom) throws AtomException {
        super(Z, A, name, chargeOfAtom);
    }
    
}
