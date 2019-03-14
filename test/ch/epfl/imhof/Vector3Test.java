package ch.epfl.imhof;

import static org.junit.Assert.*;


import org.junit.Test;

public class Vector3Test {
    private static final double DELTA = 0.000001;
    Vector3 vect = new Vector3(1,1,1);
    Vector3 vect2 = new Vector3(2,2,2);

    @Test
    public void test() {

        assertEquals(vect.norm(), Math.sqrt(3), DELTA);
        assertEquals(vect.scalarProduct(vect2), 6, DELTA);
        
    }

}
