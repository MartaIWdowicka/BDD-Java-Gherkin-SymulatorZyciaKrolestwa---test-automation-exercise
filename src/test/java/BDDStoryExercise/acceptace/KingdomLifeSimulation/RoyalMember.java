package BDDStoryExercise.acceptace.KingdomLifeSimulation;

import java.util.ArrayList;
import java.util.List;
/**
 * * Created by Marta Wdowicka on 15.07.2017.
 */

public class RoyalMember {
    private final String name;
    private int birthdayDate;
    private boolean isTheKing;
    private boolean wasTheKing;
    private boolean isAlive;
    private final boolean isMale;
    private final int lifespan;
    private final RoyalMember mother;
    private final RoyalMember father;
    private boolean haveRightsToThrone;
    private RoyalMember husbandOrWife;
    private final List<RoyalMember> directDescendants = new ArrayList<>();

    RoyalMember(String name, int birthdayDate, boolean isMale, boolean isTheKing, RoyalMember father, RoyalMember mother, boolean hasRightsToThrone, int baseLifeSpan) {
        if (name!=null){
            this.name = name;
        }else{
            this.name = "Anon Sword";
        }
        this.birthdayDate = birthdayDate;
        this.isMale = isMale;
        this.isAlive = true;
        this.isTheKing = isTheKing;
        this.wasTheKing = isTheKing;
        this.haveRightsToThrone = hasRightsToThrone;
        this.father = father;
        this.mother = mother;
        this.lifespan = baseLifeSpan + 18;
    }

    public String getName() {
        return this.name;
    }

    public int getLifespan() {
        return this.lifespan;
    }

    public boolean getIsMale() {
        return this.isMale;
    }

    public RoyalMember getHusbandOrWife() {
        return this.husbandOrWife;
    }

    public boolean isTheKing() {
        return this.isTheKing;
    }

    public boolean wasTheKing() {
        return this.wasTheKing;
    }

    public boolean isAlive() {
        return this.isAlive;
    }

    public RoyalMember getMother() {
        return this.mother;
    }

    public RoyalMember getFather() {
        return this.father;
    }

    public List<RoyalMember> getDescendants() {
        return this.directDescendants;
    }

    public boolean haveRightsToThrone() {
        return this.haveRightsToThrone;
    }

    public void giveRightsToTheThrone() {
        this.haveRightsToThrone = true;
    }

    public void revokeRightsToTheThrone() {
        this.haveRightsToThrone = false;
    }

    public void marry(RoyalMember lovedOne) {
        this.husbandOrWife = lovedOne;
        announcement("The kingdom cherish this sacred day!", " married " + lovedOne.getName() + "!", isTheKing());
    }

    public int checkAge(int kingdomTime) {
        return checkAge(kingdomTime, null);
    }

    public int checkAge(int kingdomTime, RoyalMember currentKing) {
        if (kingdomTime - this.birthdayDate > lifespan) {
            if (currentKing!=null && currentKing.getName().equals(this.name)){
                return 500;
            }
            payForFuneral();
            return 1000;
        }
        return kingdomTime - birthdayDate;
    }

    public boolean payForFuneral() {
        if (this.isAlive) {
            this.isAlive = false;
            announcement("Dreadful is the day.", " is dead!", isTheKing());
            return true;
        }
        return false;
    }

    public void bornAChild(RoyalMember child) {
        this.directDescendants.add(child);
        announcement("A child was born to", ". Child's name is " + child.getName() + "!", isTheKing());
    }

    public RoyalMember getOldestLegitimateSon() {
        return getOldestLegitimateChild(true);
    }

    public RoyalMember getOldestLegitimateDaughter() {
        return getOldestLegitimateChild(false);
    }

    public RoyalMember getOldestLegitimateChild(boolean isMale) {
        RoyalMember heir = null;
        for (RoyalMember descendant : this.directDescendants) {
            if (((isMale)? descendant.isMale : !descendant.isMale) && descendant.haveRightsToThrone() && descendant.isAlive()) {
                if (heir == null || descendant.birthdayDate < heir.birthdayDate) {
                    heir = descendant;
                }
            }
        }
        return heir;
    }

    public void longLifeTheNewKing() {
        this.wasTheKing = true;
        this.isTheKing = true;
        announcement("Hail to", " shall our Highest Ruler life forever!", true);
        announcement("", " sends wishes of good health to all of " + ((!isMale) ? "her" : "his") + " new Subjects!", true);
    }

    public void forcedAbdication() {
        this.isTheKing = false;
        this.haveRightsToThrone = true;
        System.out.println("Royal dynasty was overthrown by rioters!");
    }

    public void abdication(RoyalMember chosenHeir) {
        this.isTheKing = false;
        this.haveRightsToThrone = false;
        announcement("Long life", " forever even after! Our beloved Highest Ruler abdicated and choose " + chosenHeir.getName() + " as Heir!", false);
        chosenHeir.giveRightsToTheThrone();
        chosenHeir.longLifeTheNewKing();
    }

    public String announcement(String message, String message2, boolean isKingOrQueen) {
        StringBuffer a = new StringBuffer();
        if (this.isMale) {
            a.append(message).append(((isKingOrQueen) ? " King " : " ")).append(this.name).append(message2);
            System.out.println(a.toString());
            return a.toString();
        } else {
            a.append(message).append(((isKingOrQueen) ? (((message.equals(""))?"":" ")+ " Queen ") : " "))
            .append(this.name).append(message2);
            System.out.println(a.toString());
            return a.toString();
        }
    }
}