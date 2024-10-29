package subatomicparticles;

public class Ion extends Atom {
    
    public Ion(int Z) throws AtomException {
        super(Z, 0.0f);
    }

    public Ion(int Z, float A) throws AtomException {
        super(Z, A, "");
    }

    public Ion(int Z, float A, String name) throws AtomException {
        super(Z, A, name, 0);
    }

    public Ion(int Z, float A, String name, float chargeOfAtom) throws AtomException {
        super(Z, A, name, chargeOfAtom);
    }

}
