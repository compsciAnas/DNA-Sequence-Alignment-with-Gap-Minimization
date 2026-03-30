import java.util.Random;

// Helper class to store the winning alignment details
class AlignmentResult {
    int cost;
    String alignedS1;
    String alignedS2;

    public AlignmentResult(int cost, String alignedS1, String alignedS2) {
        this.cost = cost;
        this.alignedS1 = alignedS1;
        this.alignedS2 = alignedS2;
    }
}

public class DNAAlignmentBruteForce {

    /**
     * Finds the optimal DNA sequence alignment using Brute Force.
     * Returns ONLY the single best AlignmentResult.
     */
    public static AlignmentResult getOptimalAlignment(String s1, String s2, int alpha, int beta) {
        
        // -----------------------------------------
        // BASE CASES (The end of a branch)
        // -----------------------------------------
        if (s1.isEmpty() && s2.isEmpty()) {
            return new AlignmentResult(0, "", "");
        }

        if (s1.isEmpty()) {
            String gaps = "-".repeat(s2.length());
            int cost = s2.length() * beta;
            return new AlignmentResult(cost, gaps, s2);
        }

        if (s2.isEmpty()) {
            String gaps = "-".repeat(s1.length());
            int cost = s1.length() * beta;
            return new AlignmentResult(cost, s1, gaps);
        }

        // -----------------------------------------
        // RECURSIVE CHOICES (Branching out)
        // -----------------------------------------
        
        // CHOICE 1: Align the first characters together (Match or Mismatch)
        int costChoice1 = (s1.charAt(0) == s2.charAt(0)) ? 0 : alpha;
        AlignmentResult rec1 = getOptimalAlignment(s1.substring(1), s2.substring(1), alpha, beta);
        int totalCost1 = costChoice1 + rec1.cost;

        // CHOICE 2: Insert a gap in Sequence 2
        int costChoice2 = beta;
        AlignmentResult rec2 = getOptimalAlignment(s1.substring(1), s2, alpha, beta);
        int totalCost2 = costChoice2 + rec2.cost;

        // CHOICE 3: Insert a gap in Sequence 1
        int costChoice3 = beta;
        AlignmentResult rec3 = getOptimalAlignment(s1, s2.substring(1), alpha, beta);
        int totalCost3 = costChoice3 + rec3.cost;

        // -----------------------------------------
        // EVALUATE AND SELECT THE BEST
        // -----------------------------------------
        
        // Find the absolute minimum cost among the three choices
        int minCost = Math.min(totalCost1, Math.min(totalCost2, totalCost3));

        // Rebuild and return the sequence strings for whichever choice won
        if (minCost == totalCost1) {
            return new AlignmentResult(
                totalCost1, 
                s1.charAt(0) + rec1.alignedS1, 
                s2.charAt(0) + rec1.alignedS2
            );
        } else if (minCost == totalCost2) {
            return new AlignmentResult(
                totalCost2, 
                s1.charAt(0) + rec2.alignedS1, 
                "-" + rec2.alignedS2
            );
        } else {
            return new AlignmentResult(
                totalCost3, 
                "-" + rec3.alignedS1, 
                s2.charAt(0) + rec3.alignedS2
            );
        }
    }

    /**
     * Generates a random DNA sequence of a specified length.
     */
    public static String generateRandomDNA(int length) {
        char[] nucleotides = {'A', 'C', 'G', 'T'};
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(nucleotides[random.nextInt(nucleotides.length)]);
        }
        return sb.toString();
    }

    // ==========================================
    // SAMPLE RUN
    // ==========================================
    public static void main(String[] args) {
        // Generate random sequences
        String sequence1 = generateRandomDNA(5);
        String sequence2 = generateRandomDNA(4);
        
        int alpha = 2; // Mismatch penalty
        int beta = 3;  // Gap penalty
        
        System.out.println("Sequence 1: " + sequence1);
        System.out.println("Sequence 2: " + sequence2);
        System.out.println("Alpha (Mismatch): " + alpha + ", Beta (Gap): " + beta);
        System.out.println("========================================");
        
        // Run the brute force algorithm
        AlignmentResult optimal = getOptimalAlignment(sequence1, sequence2, alpha, beta);
        
        System.out.println("🏆 OPTIMAL ALIGNMENT FOUND 🏆");
        System.out.println("Lowest Cost: " + optimal.cost);
        System.out.println("  " + optimal.alignedS1);
        System.out.println("  " + optimal.alignedS2);
    }
}