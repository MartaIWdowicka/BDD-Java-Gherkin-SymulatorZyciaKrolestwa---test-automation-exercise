package BDDStoryExercise.acceptace;

import BDDStoryExercise.acceptace.KingdomLifeSimulation.RoyalMember;
import BDDStoryExercise.acceptace.framework.BehaviourTestEmbedder;
import BDDStoryExercise.acceptace.KingdomLifeSimulation.KingdomLifeSimulator;
import BDDStoryExercise.acceptace.KingdomLifeSimulation.RoyalFamily;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
/**
 *  Created by Marta Wdowicka on 25 July 2017
 */
public class ThroneSuccessionScenario {
    private RoyalFamily royalFamily;
    private RoyalMember firstKingsSon;
    private RoyalMember nextKingsSecondSon;
    private RoyalMember firstKingsDaughter;
    private RoyalMember queen;
    private RoyalMember fiance;
    private final List<RoyalMember> successionHistory = new ArrayList<>();

    @Test
    public void throneSuccessionAcceptanceTests() throws Exception {
        BehaviourTestEmbedder.aBehaviouralTestRunner()
                .usingStepsFrom(this)
                .withStory("throne-succession.story")
                .run();
    }

    @Given("Royal Family Exists")
    public void givenRoyalFamilyExists() {
        royalFamily = new RoyalFamily(1377, "Longerlong");
        firstKingsSon = null;
        nextKingsSecondSon = null;
        firstKingsDaughter = null;
        queen = null;
    }

    @Given("  King is Alive")
    public void givenKingIsAlive() {
        assertNotNull("King should not be null.", royalFamily.getCurrentKing());
        assertTrue("King should be Alive.", royalFamily.getCurrentKing().isAlive());
    }

    @Given("  Queen is Alive")
    public void givenQueenIsAlive() {
        assertTrue("King should be Alive.", royalFamily.getCurrentKing().isAlive());
    }

    @Given("  There's no other Relatives with rights to throne")
    public void whenKingHaveNoOtherRelatives() {
        assertNotNull("Queen should not be null.", royalFamily.getCurrentKing().getHusbandOrWife());
        assertTrue("Queen should be Alive.", royalFamily.getCurrentKing().getHusbandOrWife().isAlive());
    }

    @When(" Last King is at his bed of death")
    public void whenLastKingIsAtHisBedOfDeath() {
        RoyalMember currentKing = royalFamily.getCurrentKing();
        royalFamily.speedupKingdomTime(currentKing.getLifespan() - currentKing.checkAge(royalFamily.getKingdomTime()));
    }

    @When(" Last King exceeds his lifespan")
    public void whenLastKingExceedsHisLifespan() {
        royalFamily.speedupKingdomTime(1000);
        royalFamily.naturalDeaths();
    }

    @When("  King Died")
    public void whenKingDied() {
        royalFamily.killKing();
    }

    @When(" \"King's Son\" is Alive")
    public void whenKingsSonIsAlive() {
        firstKingsSon = royalFamily.bearSonToTheKing(false);
    }

    @When(" Queen is Dead")
    public void whenQueenIsDead() {
        queen = royalFamily.getCurrentKing().getHusbandOrWife();
        royalFamily.killQueen();
    }
    @When(" Queen is Alive and have rights to throne")
    public void whenQueenIsAlive() {
        queen = royalFamily.getCurrentKing().getHusbandOrWife();
        queen.haveRightsToThrone();
    }

    @When("  King abdicated before death And choose Queen as a next King")
    public void whenKingAbdicatedBeforeDeathAndChooseQueenAsANextKing() {
        queen = royalFamily.getCurrentKing().getHusbandOrWife();
        royalFamily.kingAbdicates(queen);
    }

    @When(" King's Sons are all illegitimate")
    public void whenKingsSonsAreAllIllegitimate() {
        firstKingsSon = royalFamily.bearSonToTheKing(true);
    }

    @When("  Royal Son loses rights to throne")
    public void whenRoyalSonLosesRightsToThrone() {
        royalFamily.getCurrentKing().getOldestLegitimateSon().revokeRightsToTheThrone();
    }

    @When(" King's Son is non existent, illegitimate or dead")
    public void whenKingsSonIsNonExistentOrDead() {
        royalFamily.killKingsSons();
        royalFamily.bearSonToTheKing(true);
    }

    @When("  King's Daughter is Alive")
    public void whenKingsDaughterIsAlive() {
        firstKingsDaughter = royalFamily.bearDaughterToTheKing(false);
    }

    @When("  King's Daughter are all illegitimate")
    public void whenKingsDaughterAreAllIllegitimate() {
        firstKingsDaughter = royalFamily.bearDaughterToTheKing(true);
    }

    @When("  Royal Daughter loses rights to throne")
    public void whenRoyalDaughterLosesRightsToThrone() {
        royalFamily.getCurrentKing().getOldestLegitimateDaughter().revokeRightsToTheThrone();
    }

    @When("  King's Daughter is non existent, illegitimate or dead")
    public void whenKingsDaughterIsAliveOrDead() {
        royalFamily.killKingsDaughters();
        royalFamily.bearDaughterToTheKing(true);
    }

    @When("  New Human Fiance is created")
    public void whenNewHumanFianceIsCreated() {
        fiance = royalFamily.createRandomHuman(royalFamily.getRoyalFamilyName(),true, false, null, null );
    }

    @When("  King's Daughter is married to Fiance")
    public void whenKingsDaughterIsMarriedToFiance() {
        firstKingsDaughter = royalFamily.getCurrentKing().getOldestLegitimateDaughter();
        firstKingsDaughter.marry(fiance);
    }

    @When(" King have a Son And a Daughter")
    public void whenKingHaveASonAndADaughter() {
        successionHistory.add(royalFamily.getCurrentKing());
        royalFamily.bearDaughterToTheKing(false);
        firstKingsSon = royalFamily.bearSonToTheKing(false);
        successionHistory.add(firstKingsSon);
    }

    @When("  King's Son have two Sons and a Daughter")
    public void whenKingsSonHaveTwoSonsAndADaughter() {
        successionHistory.add(royalFamily.bearSonToRoyalFamilyMember(royalFamily.getCurrentKing().getOldestLegitimateSon(), false));
        successionHistory.add(royalFamily.bearSonToRoyalFamilyMember(royalFamily.getCurrentKing().getOldestLegitimateSon(), false));
        successionHistory.add(firstKingsDaughter = royalFamily.bearDaughterToRoyalFamilyMember(royalFamily.getCurrentKing().getOldestLegitimateSon(), false));
    }

    @When(" 100 years passed")
    public void when100YearsPassed() {
        royalFamily.speedupKingdomTime(100);
    }

    @When("  Two people are generated as random royal family members")
    public void whenTwoPeopleAreGeneratedAsRandomRoyalFamilyMembers() {
        royalFamily.speedupKingdomTime(19);
        royalFamily.generateRandomRoyalFamilyMembers(1);
    }

    @Then("  Queen is Alive")
    public void thenQueenIsAlive() {
        assertTrue("New Queen supposed to be alive.", royalFamily.getCurrentKing().getHusbandOrWife().isAlive());
    }

    @Then(" Last King is Alive")
    public void thenLastKingIsAlive() {
        royalFamily.naturalDeaths();
        assertNotNull("Last king wasn't alive during his last year of life.", royalFamily.getCurrentKing());
    }

    @Then(" Oldest King's Daughter inherits the throne")
    public void thenOldestKingsDaughterInheritsTheThrone() {
        assertEquals("failed - King's Daughter didn't inherited throne",
                firstKingsDaughter.getName(), royalFamily.getCurrentKing().getName());
    }

    @Then(" Oldest \"King's Son\" inherits the throne")
    public void thenOldestKingsSonInheritsTheThrone() {
        assertEquals("failed - King's Son didn't inherited throne",
                firstKingsSon.getName(), royalFamily.getCurrentKing().getName());
    }

    @Then(" Queen inherits the throne")
    public void thenQueenInheritsTheThrone() {
        assertEquals("failed - Queen didn't inherited throne", queen.getName(), royalFamily.getCurrentKing().getName());
    }

    @Then(" Second \"King's First Son\" inherits the throne")
    public void thenKingsFirstSonInheritsTheThrone() {
        royalFamily.getSuccessionHistory().get(1);
        assertEquals("Second King's First Son did not inherited the throne",
                royalFamily.getSuccessionHistory().get(1).getOldestLegitimateSon(), royalFamily.getCurrentKing());
    }

    @Then(" Second \"King's Second Son\" inherits the throne")
    public void thenKingsSecondSonInheritsTheThrone() {
        int b = 1;
        for (RoyalMember a : royalFamily.getSuccessionHistory().get(1).getDescendants()) {
            if (a.getIsMale() && b != 2) {
                b++;
            } else if (a.getIsMale() && b == 2)
                nextKingsSecondSon = a;
        }
        assertEquals("Second King's Second Son did not inherited the throne",
                nextKingsSecondSon, royalFamily.getCurrentKing());
    }

    @Then(" First King's Daughter inherits the throne")
    public void thenFirstKingsDaughterInheritsTheThrone() {
        assertEquals("Second King's Second Son did not inherited the throne",
                royalFamily.getSuccessionHistory().get(0).getOldestLegitimateDaughter(), royalFamily.getCurrentKing());
    }

    @Then(" Succession History should show list of King-King-King-Queen")
    public void thenSuccessionHistoryShouldShowListOfKingKingKingQueen() {
        System.out.println("Expected:  ");
        for (RoyalMember a : this.successionHistory){
            System.out.println(a.getName());
        }
        System.out.println("  ");
        System.out.println("Actual:  ");
        for (RoyalMember a : royalFamily.getSuccessionHistory()){
            System.out.println(a.getName());
        }
        assertEquals("Succession history do not match expected", this.successionHistory, royalFamily.getSuccessionHistory());
    }

    @Then(" Royal dynasty ends")
    public void thenRoyalDynastyEnds() {
        assertNull("failed - Dynasty should have ended due to no legitimate Heir, but new king name wasn't null. ",
                royalFamily.getCurrentKing());
    }

    @Then(" King have one child")
    public void thenKingHaveOneChild() {
        assertTrue("King supposed to have one new child.", royalFamily.getCurrentKing().getDescendants().size()==1);
    }

    @Then(" King's Daughter is married")
    public void thenKingsDaughterIsMarried() {
       assertEquals("King's Daughter was supposed to be married woman. ", firstKingsDaughter.getHusbandOrWife(), fiance);
    }

    @Then("  King's Daughter Husband do not have rights to throne")
    public void thenKingsDaughterHusbandDoNotHaveRightsToThrone() {
        assertTrue("King's Daughter was supposed to be married woman. ", !firstKingsDaughter.getHusbandOrWife().haveRightsToThrone());
    }

    @Then(" Random User executing methods randomly shouldn't find any errors")
    public void thenRandomUserExecutingMethodsRandomlyShouldntFindAnyErrors() {
        KingdomLifeSimulator.simulateLife(royalFamily, 20, true);
    }

    @Then(" Random User is executing all methods once in sequence shouldn't find any errors")
    public void thenRandomUserIsExecutingAllMethodsOnceInSequenceShouldntFindAnyErrors() {
        KingdomLifeSimulator.simulateLife(royalFamily, 13, false);
    }

    @Then(" Kingdom Time Changed")
    public void thenKingdomTimeChanged() {
        assertTrue("Kingdom time should move by 100 years but did not.", royalFamily.getKingdomTime() == 1477);
    }

    @Then(" Royal Announcement works")
    public void thenRoyalAnnouncementWorks() {
        String a = royalFamily.getCurrentKing().announcement("String1", " String2", true);
        assertEquals("King Announcement don't work as expected.",
                     "String1 King " + royalFamily.getCurrentKing().getName() + " String2", a);
        String b = royalFamily.getCurrentKing().announcement("String1 ", " String2", false);
        assertEquals("Regular royal family member Announcement don't work as expected.",
                     "String1  " + royalFamily.getCurrentKing().getName() + " String2", b);
    }
}