package BDDStoryExercise.acceptace.KingdomLifeSimulation;

import static org.junit.Assert.assertTrue;
/**
 * Created by Marta Wdowicka on 15.07.2017.
 */

public class KingdomLifeSimulator {

    public static void simulateLife(RoyalFamily royalFamily, int forHowManyYears, boolean randomExecutionPath) {
        int a = 0;
        int expectedKingdomTime;
        int yearsPassed = 0;
        RoyalMember bastard;
        while (yearsPassed < forHowManyYears) {
            if (randomExecutionPath) {
                a = (int) Math.random() * 12 + 1;
                yearsPassed++;
            } else {
                a++;
                yearsPassed++;
            }
            switch (a) {
                case 1:
                    System.out.println("Assassin succeeded to kill King!");
                    String result = royalFamily.killKing();
                    if (result.equals("Dynasty ended.")) { break; }
                    assertTrue("King was not killed!", result.equals("Success."));
                    break;
                case 2:
                    System.out.println("Rioters are marching on King's castle!");
                    System.out.println("Rioters succeeded on breaking into King's castle!");
                    royalFamily.abdicateKingForcefully();
                    break;
                case 3:
                    System.out.println("At last 15 years had passed...");
                    expectedKingdomTime = royalFamily.getKingdomTime();
                    royalFamily.speedupKingdomTime(15);
                    assertTrue("Kingdom Time didn't moved fifteen years forward!",
                               royalFamily.getKingdomTime() == expectedKingdomTime + 15);
                    break;
                case 4:
                    RoyalMember daughter = royalFamily.bearDaughterToTheKing(false);
                    assertTrue("Daughter to King wasn't born",
                               daughter != null && !daughter.isTheKing() && !daughter.getIsMale());
                    break;
                case 5:
                    RoyalMember son = royalFamily.bearSonToTheKing(false);
                    assertTrue("Daughter to King wasn't born",
                               son != null && !son.isTheKing() && son.getIsMale());
                    break;
                case 6:
                    System.out.println("King's Daughters and Sons were executed by Sacred Ruler of Noria Empire's order.");
                    royalFamily.killAllKingsChildren();
                    daughter = royalFamily.getCurrentKing().getOldestLegitimateDaughter();
                    son = royalFamily.getCurrentKing().getOldestLegitimateSon();
                    System.out.println("Son:" + ((son == null) ? " is dead." : " is still alive!"));
                    System.out.println("Daughter:" + ((daughter == null) ? " is dead." : " is still alive!"));
                    assertTrue("At least one King's male Heir is still alive after all royal children were executed.",
                               royalFamily.getCurrentKing().getOldestLegitimateDaughter() == null
                                         && royalFamily.getCurrentKing().getOldestLegitimateSon() == null);
                    break;
                case 7:
                    System.out.println("There is an attempt on assassination of King's Daughters plotted by Sacred Ruler of Noria Empire.");
                    royalFamily.killKingsDaughters();
                    assertTrue("At least one King's male Heir is still alive after royal assassination",
                               royalFamily.getCurrentKing().getOldestLegitimateDaughter() == null);
                    break;
                case 8:
                    System.out.println("There is an attempt on assassination of King's Sons plotted by Sacred Ruler of Noria Empire.");
                    royalFamily.killKingsSons();
                    assertTrue("At least one King's male Heir is still alive after royal assassination",
                               royalFamily.getCurrentKing().getOldestLegitimateSon() == null);
                    break;
                case 9:
                    System.out.println("Queen was Killed by Royal Assassin!");
                    boolean queenWasAlive = royalFamily.getCurrentKing().getHusbandOrWife().isAlive();
                    royalFamily.killQueen();
                    assertTrue("Queen wasn't killed by royal assassins!",
                               !queenWasAlive || queenWasAlive != royalFamily.getCurrentKing().getHusbandOrWife().isAlive());
                    break;
                case 10:
                    System.out.println("Bastard son of King was born.");
                    bastard = royalFamily.bearSonToTheKing(true);
                    assertTrue("Bastard Son have rights to throne by default, but shouldn't!", !bastard.haveRightsToThrone());
                    break;
                case 11:
                    System.out.println("Bastard Daughter was born to King.");
                    bastard = royalFamily.bearDaughterToTheKing(true);
                    assertTrue("Bastard Daughter have rights to throne by default, but shouldn't!", !bastard.haveRightsToThrone());
                    break;
                default:
                    royalFamily.generateRandomRoyalFamilyMembers(3);
            }
            if (!letAYearPass(royalFamily, a)) {
                yearsPassed = forHowManyYears;
            }
        }
        royalFamily.showSuccessionHistory();
    }

    public static boolean letAYearPass(RoyalFamily royalFamily, int a) {
        if (royalFamily.getCurrentKing() == null) {
            System.out.println("At last Royal dynasty succumbed to threatful fate. No one survived. Royal dynasty ended.");
            return false;
        } else {
            if (royalFamily.naturalDeaths(royalFamily.getSuccessionHistory().get(0), royalFamily.getKingdomTime())) {
                    int expectedKingdomTime = royalFamily.getKingdomTime();
                    if (a != 3) { royalFamily.speedupKingdomTime(1); }
                    assertTrue("Kingdom Time didn't moved a year forward!",
                               royalFamily.getKingdomTime() == expectedKingdomTime + 1);
                }
        }
        return true;
    }
}



