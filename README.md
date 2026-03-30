# CSC311 Project Report  
## DNA Sequence Alignment with Gap Minimization  
**Semester:** Spring 2026  

**Student Name(s):** [Add Names]  
**Student ID(s):** [Add IDs]  
**Section:** [Add Section]  
**Course:** CSC311  

---

## 1. Introduction

This project studies DNA sequence alignment using three approaches:

1. **Brute Force (Naïve / Exhaustive Search)**
2. **Greedy**
3. **Dynamic Programming (DP)**

The objective is to align two DNA sequences by inserting gaps (`-`) when needed, such that the **total alignment cost is minimized**.

---

## 2. Problem Definition

Given two DNA sequences `S1` and `S2`, each alignment position contributes:

- **Match** (same nucleotide): cost = `0`
- **Mismatch** (different nucleotides): cost = `α`
- **Gap** (nucleotide vs `-`): cost = `β`

Where:  
- `α > 0`  
- `β > 0`

If an alignment has length `L`, then total cost is:

\[
\text{Total Cost} = \sum_{i=1}^{L} \text{cost}(S1[i], S2[i])
\]

Goal: **Find an alignment with minimum total cost**.

---

## 3. Datasets Description

For this project, we used:

1. **Small fixed DNA test cases** (for correctness checking and reproducibility)
2. **Randomly generated DNA sequences** (for performance observation)

Example fixed test pairs:
- `AGT` vs `AG`
- `GATT` vs `GCT`
- `ACGT` vs `TGCA`

Example random dataset generation:
- Sequence lengths from small sizes (e.g., 4–8) for brute force
- Larger sizes used for Greedy and DP comparison

---

## 4. Algorithm 1: Brute Force (Naïve Solution)

### 4.1 Idea

The brute-force method explores **all possible alignments** by recursively trying at each step:

1. Align current characters from both strings (match/mismatch)
2. Insert a gap in sequence 2
3. Insert a gap in sequence 1

It computes total cost for each complete alignment and returns the minimum.

### 4.2 Pseudocode

```text
BF(s1, s2):
    if s1 empty and s2 empty: return cost 0, "", ""
    if s1 empty: return len(s2)*β, "-"*len(s2), s2
    if s2 empty: return len(s1)*β, s1, "-"*len(s1)

    option1 = BF(s1[1:], s2[1:]) + (0 if s1[0]==s2[0] else α)
    option2 = BF(s1[1:], s2) + β
    option3 = BF(s1, s2[1:]) + β

    return minimum(option1, option2, option3)
```

### 4.3 Explanation

This method is correct because it checks every legal alignment path and chooses minimum cost.  
However, it is highly inefficient because the number of recursive states grows exponentially.

### 4.4 Time Complexity

- **Worst case:** Exponential, approximately `O(3^(m+n))` upper-bound style behavior  
- **Best case:** Still exponential in general recursive form (no memoization), because branches are explored before minimum is known  
- **Space complexity:** `O(m+n)` recursion depth (excluding string copy overhead)

---

## 5. Algorithm 2: Greedy

### 5.1 Idea

The greedy algorithm makes a locally best choice at each step (e.g., prefer matching characters or minimal immediate penalty) without exploring all future consequences.

### 5.2 Pseudocode (Generic Greedy Structure)

```text
Greedy(s1, s2):
    i = 0, j = 0
    totalCost = 0
    aligned1 = "", aligned2 = ""

    while i < len(s1) and j < len(s2):
        choose locally best among:
            (s1[i] vs s2[j]), (s1[i] vs '-'), ('-' vs s2[j])
        append chosen alignment
        update i/j and totalCost

    append remaining characters with gaps
    return totalCost, aligned1, aligned2
```

### 5.3 Explanation

Greedy is fast and simple, but it is not guaranteed to produce the global optimum for all inputs.

---

## 6. Algorithm 3: Dynamic Programming

### 6.1 DP Formulation

Define:

\[
DP[i][j] = \text{minimum cost to align } S1[1..i] \text{ with } S2[1..j]
\]

Transition:

\[
DP[i][j] = \min
\begin{cases}
DP[i-1][j-1] + \text{match/mismatch cost}\\
DP[i-1][j] + \beta\\
DP[i][j-1] + \beta
\end{cases}
\]

Base cases:

- `DP[0][0] = 0`
- `DP[i][0] = i * β`
- `DP[0][j] = j * β`

### 6.2 Pseudocode

```text
DPAlign(s1, s2):
    create table DP[m+1][n+1]
    initialize first row and first column with gap costs

    for i from 1 to m:
        for j from 1 to n:
            diag = DP[i-1][j-1] + (0 if s1[i-1]==s2[j-1] else α)
            up   = DP[i-1][j] + β
            left = DP[i][j-1] + β
            DP[i][j] = min(diag, up, left)

    backtrack to reconstruct alignment
    return DP[m][n], alignedS1, alignedS2
```

### 6.3 Explanation

DP avoids repeated subproblems using a table, giving optimal solution efficiently compared to brute force.

---

## 7. Complexity Comparison Table

```text
+-------------------+-----------------------------+-------------------------+
| Algorithm         | Best Time Complexity        | Worst Time Complexity   |
+-------------------+-----------------------------+-------------------------+
| Brute Force       | Exponential (practically)   | Exponential             |
| Greedy            | O(m + n)                    | O(m + n)                |
| Dynamic Programming| O(m * n)                   | O(m * n)                |
+-------------------+-----------------------------+-------------------------+
```

Where:
- `m = length(S1)`
- `n = length(S2)`

---

## 8. Sample Run

### Input
- `S1 = AGT`
- `S2 = AG`
- `α = 2`
- `β = 3`

### Example Output (Brute Force)
- Minimum Cost: `3`
- Alignment:
  - `AGT`
  - `AG-`

(Include actual console screenshots/output from your implementation for BF, Greedy, and DP.)

---

## 9. Source Code

- Included in project ZIP:
  - `DNAAlignmentBruteForce.java`
  - `DNAAlignmentGreedy.java`
  - `DNAAlignmentDP.java`
- Code formatting in report: **Courier New, size 8** (as requested)
- Code is commented and uses meaningful variable names

---

## 10. Challenges and How We Solved Them

### Challenge 1: Brute-force runtime explosion
- **Issue:** Number of recursive calls increases very quickly even for medium sequence lengths.
- **Solution:** Kept brute-force tests on small inputs and added call/explored-state counting for analysis.

### Challenge 2: Alignment reconstruction
- **Issue:** Need not only minimum cost but also actual aligned strings.
- **Solution:** Stored or reconstructed alignment path (especially in DP backtracking).

### Challenge 3: Fair comparison across algorithms
- **Issue:** Different algorithms have different assumptions and performance ranges.
- **Solution:** Used consistent scoring (`α`, `β`) and same input pairs for comparison.

### Challenge 4: Balancing readability and correctness
- **Issue:** Recursive and DP logic can become hard to read.
- **Solution:** Used clear helper methods, comments, and meaningful variable names.

---

## 11. Conclusion

This project demonstrates the trade-off between correctness and efficiency:

- **Brute Force**: always optimal but impractical for long sequences
- **Greedy**: very fast but may be suboptimal
- **Dynamic Programming**: optimal and efficient enough for realistic sequence sizes

Hence, DP is generally the best practical solution for DNA alignment with gap minimization.

---

## 12. Submission Checklist (Project Requirement Compliance)

- [ ] Cover page with names, IDs, section, semester (Spring 2026)
- [ ] Description + pseudocode for BF, Greedy, DP
- [ ] Complexity table (best/worst) with explanation
- [ ] Sample runs included
- [ ] Source code included and commented
- [ ] Challenges section included
- [ ] Peer evaluation submitted individually on LMS
- [ ] ZIP filename format: `<G#_CSC311_Project>.zip`
