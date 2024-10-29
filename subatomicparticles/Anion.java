package subatomicparticles;

public class Anion extends Ion {
    
    public Anion(int Z) throws AtomException {
        super(Z, 0.0f);
    }

    public Anion(int Z, float A) throws AtomException {
        super(Z, A, "");
    }

    public Anion(int Z, float A, String name) throws AtomException {
        super(Z, A, name, 0);
    }

    public Anion(int Z, float A, String name, float chargeOfAtom) throws AtomException {
        super(Z, A, name, chargeOfAtom);
    }

        
}
