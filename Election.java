import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * An Election consists of the candidates running for office, the ballots that
 * have been cast, and the total number of voters. This class implements the
 * ranked choice voting algorithm.
 * 
 * Ranked choice voting uses this process:
 * <ol>
 * <li>Rather than vote for a single candidate, a voter ranks all the
 * candidates. For example, if 3 candidates are running on the ballot, a voter
 * identifies their first choice, second choice, and third choice.
 * <li>The first-choice votes are tallied. If any candidate receives &gt; 50% of
 * the votes, that candidate wins.
 * <li>If no candidate wins &gt; 50% of the votes, the candidate(s) with the
 * lowest number of votes is(are) eliminated. For each ballot in which an
 * eliminated candidate is the first choice, the 2nd ranked candidate is now the
 * top choice for that ballot.
 * <li>Steps 2 &amp; 3 are repeated until a candidate wins, or all remaining
 * candidates have exactly the same number of votes. In the case of a tie, there
 * would be a separate election involving just the tied candidates.
 * </ol>
 */
public class Election {
    // All candidates that were in the election initially. If a candidate is
    // eliminated, they will still stay in this array.
    private final Candidate[] candidates;

    // The next slot in the candidates array to fill.
    private int nextCandidate;

    /**
     * Create a new Election object. Initially, there are no candidates or
     * votes.
     * 
     * @param numCandidates the number of candidates in the election.
     */
    public Election(int numCandidates) {
        this.candidates = new Candidate[numCandidates];
    }

    /**
     * Adds a candidate to the election
     * 
     * @param name the candidate's name
     */
    public void addCandidate(String name) {
        candidates[nextCandidate] = new Candidate(name);
        nextCandidate++;
    }

    /**
     * Adds a completed ballot to the election.
     * 
     * @param ranks A correctly formulated ballot will have exactly 1 entry with
     *              a rank of 1, exactly one entry with a rank of 2, etc. If
     *              there are n candidates on the ballot, the values in the rank
     *              array passed to the constructor will be some permutation of
     *              the numbers 1 to n.
     * @throws IllegalArgumentException if the ballot is not valid.
     */
    public void addBallot(int[] ranks) {
        if (!isBallotValid(ranks)) {
            throw new IllegalArgumentException("Invalid ballot");
        }
        Ballot newBallot = new Ballot(ranks);
        assignBallotToCandidate(newBallot);
    }

    /**
     * Checks that the ballot is the right length and contains a permutation of
     * the numbers 1 to n, where n is the number of candidates.
     * 
     * @param ranks the ballot to check
     * @return true if the ballot is valid.
     */
    private boolean isBallotValid(int[] ranks) {
        if (ranks.length != candidates.length) {
            return false;
        }
        int[] sortedRanks = Arrays.copyOf(ranks, ranks.length);
        Arrays.sort(sortedRanks);
        for (int i = 0; i < sortedRanks.length; i++) {
            if (sortedRanks[i] != i + 1) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get a list of all candidates
     * 
     * @return an ArrayList contains all candidates
     */
    List<Candidate> getCandidates() {
        return Arrays.asList(candidates);
    }

    /**
     * Determines which candidate is the top choice on the ballot and gives the
     * ballot to that candidate.
     * 
     * @param newBallot a ballot that is not currently assigned to a candidate
     */
    private void assignBallotToCandidate(Ballot newBallot) {
        int candidate = newBallot.getTopCandidate();
        candidates[candidate].addBallot(newBallot);
    }

    /**
     * Apply the ranked choice voting algorithm to identify the winner.
     * 
     * @return If there is a winner, this method returns a list containing just
     *         the winner's name is returned. If there is a tie, this method
     *         returns a list containing the names of the tied candidates.
     */
    public List<String> selectWinner() {
        ArrayList<String> winner = new ArrayList<String>();
        List<Candidate> candidateList = getCandidates();

        // case1: there is only one candidate
        if (candidates.length == 1) {
            winner.add(candidates[0].getName());
            return winner;
        } else if (votesEqual(candidateList)) {
            // case4: all candidates receive an equal number of votes
            for (Candidate i : candidateList) {
                winner.add(i.getName());
            }
            return winner;
        }

        // case 3: one of the candidates receives more than 50% of the votes
        else if (votesMoreThanHalf(candidateList) != null) {
            winner.add(votesMoreThanHalf(candidateList).getName());
            return winner;
        } else {
            // case 5&6&7&8
            List<Candidate> remain = reassign(candidateList);
            for (Candidate i : remain) {
                winner.add(i.getName());
            }
        }

        return winner;
    }

    /**
     * This function keeps reassigning ballots in which an eliminated candidate
     * is the first choice to their second choice until only one candidate left,
     * the candidates left receive an equal number of votes, or there is a
     * candidate who receives more than 50% of the total votes
     * 
     * @param candidateList: the list of candidates in the election.
     * @return an integer that indicates the number of candidates left after one
     *         round of elimination and reassigning.
     */
    List<Candidate> reassign(List<Candidate> candidateList) {

        // base cases
        if (candidateList.size() == 1) {
            return candidateList;
        } else if (votesEqual(candidateList)) {
            return candidateList;
        } else if (votesMoreThanHalf(candidateList) != null) {
            List<Candidate> winnerList = new ArrayList<Candidate>();
            winnerList.add(votesMoreThanHalf(candidateList));
            return winnerList;
        }
        List<Candidate> candidateListTemp = new ArrayList<>(candidateList);
        // find the lowest number of votes
        int minVotes = Integer.MAX_VALUE;
        for (Candidate i : candidateListTemp) {
            if (i.getVotes() < minVotes) {
                minVotes = i.getVotes();
            }
        }
        // get a list of minVotes if there are multiple candidates with the
        // lowest votes
        ArrayList<Candidate> minVotesList = new ArrayList<Candidate>();
        for (Candidate i : candidateListTemp) {
            if (minVotes == i.getVotes()) {
                minVotesList.add(i);
            }
        }

        // eliminate the candidates with lowest votes and reassign them.
        List<Ballot> ballotReassign = new ArrayList<Ballot>();
        for (Candidate i : minVotesList) {
            ballotReassign.addAll(i.eliminate());
        }
        for (Ballot i : ballotReassign) {
            i.eliminateCandidate(i.getTopCandidate());
            assignBallotToCandidate(i);

        }

        candidateListTemp.removeAll(minVotesList);

        return reassign(candidateListTemp);
    }

    /**
     * Check whether the candidates in a list have the same number of votes.
     * 
     * @param candidateList: the list of candidates that needs to be checked.
     * @return a boolean that indicates whether the votes are equal.
     */
    boolean votesEqual(List<Candidate> candidateList) {

        for (Candidate i : candidateList) {
            int voteNum = candidateList.get(0).getVotes();
            if (i.getVotes() != voteNum) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check whether there is a candidate that has more than 50% of the votes.
     * 
     * @param candidateList is the list of candidates that needs to be checked.
     * @return if there is a candidate who has more than 50% of the votes, it
     *         returns the name of that candidate. Otherwise, return a string
     *         "LessThanHalf".
     */
    Candidate votesMoreThanHalf(List<Candidate> candidateList) {

        Candidate maxVotes = new Candidate("MaxVotes");
        int totalVotes = 0;
        for (Candidate i : candidateList) {
            if (maxVotes.getVotes() < i.getVotes()) {
                maxVotes = i;
            }
            totalVotes += i.getVotes();

        }
        if (maxVotes.getVotes() > (0.5) * totalVotes) {
            return maxVotes;
        }
        return null;
    }

}
