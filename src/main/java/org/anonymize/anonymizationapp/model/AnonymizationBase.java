package org.anonymize.anonymizationapp.model;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.deidentifier.arx.ARXLattice.ARXNode;
import org.deidentifier.arx.ARXResult;
import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;



/**
 * This class provides a base class for data anonymization.
 *
 * @author Fabian Prasser
 * @author Florian Kohlmayer
 * @utilized and edited by Jake Murphy Smith
 */
public abstract class AnonymizationBase {

    /**
     * Prints a given data handle.
     *
     * @param handle
     */
    protected static void print(DataHandle handle) {
        final Iterator<String[]> itHandle = handle.iterator();
        print(itHandle);
    }

    /**
     * Prints a given iterator.
     *
     * @param iterator
     */
    protected static void print(Iterator<String[]> iterator) {
        while (iterator.hasNext()) {
            System.out.print("   ");
            System.out.println(Arrays.toString(iterator.next()));
        }
    }

    /**
     * Prints java array.
     *
     * @param array
     */
    protected static void printArray(String[][] array) {
        System.out.print("{");
        for (int j=0; j<array.length; j++){
            String[] next = array[j];
            System.out.print("{");
            for (int i = 0; i < next.length; i++) {
                String string = next[i];
                System.out.print("\"" + string + "\"");
                if (i < next.length - 1) {
                    System.out.print(",");
                }
            }
            System.out.print("}");
            if (j<array.length-1) {
                System.out.print(",\n");
            }
        }
        System.out.println("}");
    }

    /**
     * Prints a given data handle.
     *
     * @param handle
     */
    protected static void printHandle(DataHandle handle) {
        final Iterator<String[]> itHandle = handle.iterator();
        printIterator(itHandle);
    }
    
    /**
     * Prints java array.
     *
     * @param iterator
     */
    protected static void printIterator(Iterator<String[]> iterator) {
        while (iterator.hasNext()) {
            String[] next = iterator.next();
            System.out.print("[");
            for (int i = 0; i < next.length; i++) {
                String string = next[i];
                System.out.print(string);
                if (i < next.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
    }
    
    /**
     * Prints the ARXResult object.
     *
     * @param result
     * @param data
     */
    protected static String[] printResult(final ARXResult result, final Data data) {
    	String[] stats = new String[2];
        // Print time
        final DecimalFormat df1 = new DecimalFormat("#####0.00");
        final String sTotal = df1.format(result.getTime() / 1000d) + "s";
        System.out.println(" - Time needed: " + sTotal);
        stats[0] = sTotal;
        // Extract
        final ARXNode optimum = result.getGlobalOptimum();
        final List<String> qis = new ArrayList<String>(data.getDefinition().getQuasiIdentifyingAttributes());

        if (optimum == null) {
            System.out.println(" - No solution found!");
            stats[0] = " - No solution found!";
            stats[1] = " - No solution found!";
            return stats;
        }

        // Initialize
        final StringBuffer[] identifiers = new StringBuffer[qis.size()];
        final StringBuffer[] generalizations = new StringBuffer[qis.size()];
        int lenIdentifier = 0;
        int lenGeneralization = 0;
        for (int i = 0; i < qis.size(); i++) {
            identifiers[i] = new StringBuffer();
            generalizations[i] = new StringBuffer();
            identifiers[i].append(qis.get(i));
            generalizations[i].append(optimum.getGeneralization(qis.get(i)));
            if (data.getDefinition().isHierarchyAvailable(qis.get(i)))
                generalizations[i].append("/").append(data.getDefinition().getHierarchy(qis.get(i))[0].length - 1);
            lenIdentifier = Math.max(lenIdentifier, identifiers[i].length());
            lenGeneralization = Math.max(lenGeneralization, generalizations[i].length());
        }

        // Padding
        for (int i = 0; i < qis.size(); i++) {
            while (identifiers[i].length() < lenIdentifier) {
                identifiers[i].append(" ");
            }
            while (generalizations[i].length() < lenGeneralization) {
                generalizations[i].insert(0, " ");
            }
        }
        String infoLossTotal = result.getGlobalOptimum().getLowestScore() + " / " + result.getGlobalOptimum().getHighestScore();
        // Print
        System.out.println(" - Information loss: " + infoLossTotal );
        String infoLossMinor = result.getGlobalOptimum().getLowestScore() + "";
        
        stats[1] = infoLossMinor; 
        System.out.println(" - Optimal generalization");
        for (int i = 0; i < qis.size(); i++) {
            System.out.println("   * " + identifiers[i] + ": " + generalizations[i]);
        }
        System.out.println(" - Statistics");
        System.out.println(result.getOutput(result.getGlobalOptimum(), false).getStatistics().getEquivalenceClassStatistics());
        return stats;
    }
}
