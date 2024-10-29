package subatomicparticles;

public enum Stability {
    EMPTY,
    FILLED,
    HALF_FILLED,
    PARTIALLY_FILLED;

    static int compare(Stability s1, Stability s2) {
        if (s1 == s2)
            return 0;
        else if (s1 == FILLED && s2 == HALF_FILLED)
            return -1;
        else if (s1 == HALF_FILLED && s2 == FILLED)
            return 1;
        else if (s1 == HALF_FILLED && s2 == PARTIALLY_FILLED)
            return -1;
        else if (s1 == PARTIALLY_FILLED && s2 == HALF_FILLED)
            return 1;
        return s1.compareTo(s2);
    }
}
