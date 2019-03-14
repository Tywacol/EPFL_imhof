package ch.epfl.imhof.dem;


import static org.junit.Assert.*;

import java.awt.image.Kernel;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ReliefShaderTest {
/*   
    @Test
    // fonctionne =]
    public void kernelTest() {
        
        ReliefShader relief = new ReliefShader(null, null, null);
         
        float[] data = relief.createKernel(0.9);
        for (int i = 0; i < data.length; i++) {
            System.out.println(data[i]);
        }
      
        Kernel transposedKer = new Kernel(1,3, data);
        float[] transposedData = transposedKer.getKernelData(null);
        for (int i = 0; i < transposedData.length; i++) {
           // System.out.println(transposedData[i]);
        }
    }
*/
    
    public static void main(String[] args) { 
    List<Integer> l = new ArrayList<>();
    l.add(3);
    
    
    double[] d = new double[3];
    System.out.println(d.length);
    
    int[] array = {12,1,4};
    String string = "Hoo";
    System.out.println(array.length);
    System.out.println(string.length());
    }
    
    
    
}
