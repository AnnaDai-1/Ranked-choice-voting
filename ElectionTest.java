import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ElectionTest {
    Election electionTest1;
    Election electionTest2;
    Election electionTest3;
    Election electionTest4;
    Election electionTest5;
    Election electionTest6;
    Election electionTest7;
    Election electionTest8;
    Election electionTest9;
    Election electionTest10;
    Election electionTest11;
    Election electionTest12;

    private List<Candidate> candidateList = new ArrayList<Candidate>();

    @BeforeEach
    void setUp() throws Exception {
        electionTest1 = RankedChoiceVoting.initializeElection("test1.txt");
        electionTest2 = RankedChoiceVoting.initializeElection("test2.txt");
        electionTest3 = RankedChoiceVoting.initializeElection("test3.txt");
        electionTest4 = RankedChoiceVoting.initializeElection("test4.txt");
        electionTest5 = RankedChoiceVoting.initializeElection("test5.txt");
        electionTest6 = RankedChoiceVoting.initializeElection("test6.txt");
        electionTest7 = RankedChoiceVoting.initializeElection("test7.txt");
        electionTest8 = RankedChoiceVoting.initializeElection("test8.txt");
        electionTest9 = RankedChoiceVoting.initializeElection("test9.txt");
        electionTest10 = RankedChoiceVoting.initializeElection("test10.txt");
        electionTest11 = RankedChoiceVoting.initializeElection("test11.txt");
        electionTest12 = RankedChoiceVoting.initializeElection("test12.txt");
    }

    @Test
    /*
     * test helper method votesEquals
     */
    void testVotesEqual() {
        candidateList = electionTest4.getCandidates();
        assertTrue(electionTest4.votesEqual(candidateList));
        assertEquals(false,
                electionTest2.votesEqual(electionTest2.getCandidates()));
    }

    @Test
    /*
     * test helper method reassign
     */
    void testReassign() {

        candidateList = electionTest1.reassign(electionTest1.getCandidates());
        assertEquals(1, candidateList.size());

    }

    @Test
    /*
     * Testing the helper method votesMoreThanHalf
     */
    void testMoreThanHalf() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Beyonce");
        candidateList = electionTest2.getCandidates();
        List<String> nameList = new ArrayList<String>();

        if (electionTest2.votesMoreThanHalf(candidateList) != null) {
            nameList.add(
                    electionTest2.votesMoreThanHalf(candidateList).getName());

        }
        assertEquals(winnerList, nameList);
    }

    @Test
    /*
     * There is only one candidate
     */
    void testOneCandidate() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Lizzo");

        assertEquals(winnerList, electionTest3.selectWinner());
    }

    @Test
    /*
     * there is only one Ballot
     */
    void testOneBallot() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Taylor Swift");
        assertEquals(winnerList, electionTest5.selectWinner());
    }

    @Test
    /*
     * Before any elimination, one wins more than50%
     */
    void testOneWinsBeforeElimination() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Beyonce");
        assertEquals(winnerList, electionTest2.selectWinner());
    }

    @Test
    /*
     * All candidates receives an equal number of votes before any eliminations
     */
    void testEqualVotes() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Beyonce");
        winnerList.add("Lizzo");
        winnerList.add("Taylor Swift");
        assertEquals(winnerList, electionTest4.selectWinner());
    }

    @Test
    /*
     * After one round of elimination, one wins more than50%
     */
    void testOneRoundOneWins() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Beyonce");
        assertEquals(winnerList, electionTest1.selectWinner());
    }

    @Test
    /*
     * After one round of elimination, only one candidate Left
     */
    void testOneRoundOneLeft() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Beyonce");
        assertEquals(winnerList, electionTest6.selectWinner());
    }

    @Test
    /*
     * After one round of elimination, one wins more than 50%
     */
    void testMutipleRoundsOneWins() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Lizzo");
        assertEquals(winnerList, electionTest7.selectWinner());

    }

    @Test
    /*
     * After one round of elimination, all candidates receives an equal number
     * of votes
     */
    void testOneRoundTie() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Beyonce");
        winnerList.add("Taylor Swift");
        assertEquals(winnerList, electionTest8.selectWinner());
    }

    @Test
    /*
     * After multiple rounds of elimination, all candidates receives an equal
     * number of votes
     */
    void testMultipleRoundTie() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Lizzo");
        winnerList.add("Billie");
        assertEquals(winnerList, electionTest9.selectWinner());
    }

    @Test
    /*
     * There are multiple candidates with the lowest votes
     */
    void testMultipleLowest() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Billie");
        assertEquals(winnerList, electionTest10.selectWinner());
    }

    @Test
    /*
     * There are no candidates
     */
    void ZeroCandidate() {

        List<String> winnerList = new ArrayList<String>();
        assertEquals(winnerList, electionTest11.selectWinner());
    }

    @Test
    /*
     * There are no ballots
     */
    void ZeroBallot() {

        List<String> winnerList = new ArrayList<String>();
        winnerList.add("Beyonce");
        winnerList.add("Lizzo");
        winnerList.add("Taylor Swift");
        assertEquals(winnerList, electionTest12.selectWinner());
    }

}
