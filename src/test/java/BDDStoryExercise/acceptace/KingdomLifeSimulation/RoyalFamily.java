package BDDStoryExercise.acceptace.KingdomLifeSimulation;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by Marta Wdowicka on 15.07.2017.
 */

public class RoyalFamily {
    private RoyalMember currentKing;
    private final String royalFamilyName;
    private final List<RoyalMember> successionHistory = new ArrayList<>();
    private int kingdomTime;
    private final List<RoyalName> girlNames = new ArrayList<>();
    private final List<RoyalName> boyNames = new ArrayList<>();

    public RoyalFamily(int kingdomTime, String rulingFamilyName) {
        setKingdomTime(kingdomTime);
        this.royalFamilyName = rulingFamilyName;
        RoyalMember firstKing = new RoyalMember(createName(true, rulingFamilyName), kingdomTime, true, true, null, null, true, 50);
        this.currentKing = firstKing;
        successionHistory.add(firstKing);
        RoyalMember firstQueen = new RoyalMember(createName(false, rulingFamilyName), kingdomTime, false, false, null, null, true, 50);
        marryTheKingOrQueen(firstQueen);
    }

    public String getRoyalFamilyName() {
        return this.royalFamilyName;
    }

    public RoyalMember getCurrentKing() {
        return this.currentKing;
    }

    public List<RoyalMember> getSuccessionHistory() {
        return this.successionHistory;
    }

    public void showSuccessionHistory() {
        System.out.println(" ");
        System.out.println("Behold list of kings selected up to this moment!:");
        int b = 0;
        for (RoyalMember a : this.successionHistory) {
            b++;
            if (a != null && b == successionHistory.size()) {
                System.out.println((char) 9819 + " " + a.getName());
            } else {
                System.out.println((char) 9812 + " " + a.getName());
            }
        }
        System.out.println(" ");
    }

    public void setKingdomTime(int year) {
        this.kingdomTime = year;
    }

    public void speedupKingdomTime(int years) {
        this.kingdomTime = this.kingdomTime + years;
        System.out.println("Glorious year " + getKingdomTime() + " have started. ");
    }

    public int getKingdomTime() {
        return this.kingdomTime;
    }

    public boolean naturalDeaths(){
        return naturalDeaths(getSuccessionHistory().get(0), getKingdomTime());
    }

    public boolean naturalDeaths(RoyalMember currentRelative, int kingdomTime ) {
        RoyalMember secondHalf = currentRelative.getHusbandOrWife();
        int x = currentRelative.checkAge(kingdomTime, getCurrentKing());
        int y = secondHalf.checkAge(kingdomTime, getCurrentKing());
        if (x==500 || y==500 ){
            killKing();
        }
        if (currentRelative.getDescendants().size()!=0){
            for ( RoyalMember a : currentRelative.getDescendants()){
                naturalDeaths(a, kingdomTime);
            }
        }
        if (secondHalf.getDescendants().size()!=0) {
            for (RoyalMember a : currentRelative.getDescendants()) {
                if ((secondHalf.getIsMale()) ? !a.getMother().equals(currentRelative) : !a.getFather().equals(currentRelative)) {
                    naturalDeaths(a, kingdomTime);
                }
            }
        }
        return true;
    }

    public RoyalMember chooseRoyalSuccessor() {
        if (currentKing == null) {
            return null;
        }
        if (currentKing.getOldestLegitimateSon() != null) {
            return currentKing.getOldestLegitimateSon();
        } else if (currentKing.getOldestLegitimateDaughter() != null) {
            return currentKing.getOldestLegitimateDaughter();
        } else if (this.successionHistory.get(0) != currentKing) {
            return getClosestKingsRelative(currentKing);
        }
        return null;
    }

    public RoyalMember getClosestKingsRelative(RoyalMember royalMember) {
        RoyalMember heir = null;
        RoyalMember maleParent = royalMember.getFather();
        RoyalMember femaleParent = royalMember.getMother();
        boolean checkMotherSide = (femaleParent != null && (femaleParent.haveRightsToThrone() || femaleParent.wasTheKing())) ? true : false;
        boolean checkFatherSide = (maleParent != null && (maleParent.haveRightsToThrone() || maleParent.wasTheKing())) ? true : false;
        if (checkFatherSide) { heir = getHeir(maleParent); }
        if (heir == null && checkMotherSide) { heir = getHeir(femaleParent); }
        if (heir == null && checkFatherSide) { heir = getClosestKingsRelative(maleParent); }
        if (heir == null && checkMotherSide) { heir = getClosestKingsRelative(femaleParent); }
        return heir;
    }

    public RoyalMember getHeir(RoyalMember royalMember) {
        if (royalMember.getOldestLegitimateSon() != null) {
            return royalMember.getOldestLegitimateSon();
        } else if (royalMember.getOldestLegitimateDaughter() != null) {
            return royalMember.getOldestLegitimateDaughter();
        }
        return null;
    }

    public String killKing() {
        if (currentKing == null) { return "There was no King to be killed."; }
        if (!currentKing.payForFuneral()) { return "King was already dead."; }
        this.currentKing = chooseRoyalSuccessor();
        if (this.currentKing == null) {
            System.out.println("Our beloved Ruler, last of his lineage, is dead. Royal dynasty ended.");
            return "Dynasty ended.";
        }
        successionHistory.add(this.currentKing);
        return "Success.";
    }

    public void abdicateKingForcefully() {
        getCurrentKing().forcedAbdication();
    }

    public boolean kingAbdicates(RoyalMember successor) {
        try {
            currentKing.abdication(successor);
            this.currentKing = successor;
            successionHistory.add(this.currentKing);
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

    public void killQueen() {
        try {
            if (currentKing.getHusbandOrWife() != null && currentKing.getHusbandOrWife().isAlive()) {
                currentKing.getHusbandOrWife().payForFuneral();
            }
        } catch (Error e) {}
    }

    public void marryTheKingOrQueen(RoyalMember wifeOrHusband) {
        currentKing.marry(wifeOrHusband);
    }

    public void generateRandomRoyalFamilyMembers(int howMany) {
        RoyalMember descendant;
        int descendantsCount;
        List<RoyalMember> lineage;

        for (int i = howMany + 1 ; i!=0; i--) {
            descendant = getSuccessionHistory().get(0);
            lineage = new ArrayList<>();
            descendantsCount = descendant.getDescendants().size();
            lineage.add(descendant);
            while (descendantsCount != 0) {
                descendant = descendant.getDescendants().get(random(0, descendantsCount - 1));
                lineage.add(descendant);
                descendantsCount = descendant.getDescendants().size();
            }
            descendant = lineage.get(random(0, lineage.size()));
            if (!descendant.isAlive()) {
                for (int a = lineage.size(); a > 0 ; a --){
                    if (lineage.get(a).isAlive()){
                        descendant = lineage.get(a);
                    }
                }
                if (!descendant.isAlive()) {
                    continue;
                }
            }
            boolean secondHalfAlive = true;
            try {
                secondHalfAlive = descendant.getHusbandOrWife().isAlive();
            } catch (NullPointerException e) {
            }
            if (descendant.getHusbandOrWife() == null || !secondHalfAlive) {
                RoyalMember futureWifeOrHusband = createRandomHuman("Rothclad", true, descendant.getIsMale(), null, null);
                descendant.marry(futureWifeOrHusband);
            } else {
                if (descendant.checkAge(getKingdomTime()) >= 18) {
                    RoyalMember futureChild = createRandomHuman(getRoyalFamilyName(), false, descendant.getIsMale(), (descendant.getIsMale()) ? descendant : descendant.getHusbandOrWife(), (!descendant.getIsMale()) ? descendant.getHusbandOrWife() : descendant);
                    descendant.bornAChild(futureChild);
                } else {
                    RoyalMember futureChild = createRandomHuman(getRoyalFamilyName(), false, descendant.getIsMale(), (descendant.getIsMale()) ? descendant : descendant.getHusbandOrWife(), (!descendant.getIsMale()) ? descendant.getHusbandOrWife() : descendant);
                    descendant.getFather().bornAChild(futureChild);
                }
            }
        }
    }

    public RoyalMember createRandomHuman(String royalFamilyName, Boolean willBeMarried, boolean fianceGender, RoyalMember father, RoyalMember mother) {
        if ((willBeMarried) ? fianceGender : random(1, 2) == 2) {
            return new RoyalMember(createName(false, royalFamilyName), getKingdomTime(), false, false, father, mother, !willBeMarried && father.haveRightsToThrone(), random(1, 50));
        } else {
            return new RoyalMember(createName(true, royalFamilyName), getKingdomTime(), true, false, father, mother, !willBeMarried && father.haveRightsToThrone(), random(1, 50));
        }
    }

    public RoyalMember bearDaughterToTheKing(boolean isIllegitimateChild) {
        return bearDaughterToRoyalFamilyMember(currentKing, isIllegitimateChild);
    }

    public RoyalMember bearDaughterToRoyalFamilyMember(RoyalMember royalMember, boolean isIllegitimateChild) {
        RoyalMember child = new RoyalMember(createName(false, ((isIllegitimateChild) ? "Sword" : royalFamilyName)), getKingdomTime(), false, false, royalMember, royalMember.getHusbandOrWife(), ((!isIllegitimateChild) ? true : false), 50);
        royalMember.bornAChild(child);
        return child;
    }

    public RoyalMember bearSonToTheKing(boolean isIllegitimateChild) {
        return bearSonToRoyalFamilyMember(currentKing, isIllegitimateChild);
    }

    public RoyalMember bearSonToRoyalFamilyMember(RoyalMember royalMember, boolean isIllegitimateChild) {
        RoyalMember child = new RoyalMember(createName(true, ((isIllegitimateChild) ? "Sword" : royalFamilyName)), getKingdomTime(), true, false, royalMember, royalMember.getHusbandOrWife(), ((!isIllegitimateChild) ? true : false), 50);
        royalMember.bornAChild(child);
        return child;
    }

    public boolean killKingsSons() {
        return killKingsKids(true);
    }

    public boolean killKingsDaughters() {
        return killKingsKids(false);
    }

    public boolean killKingsKids(boolean isMale) {
        if (currentKing == null) {
            System.out.println("Assassin: There is no King for me to kill King's" + ((isMale) ? " Sons " : "Daughters..."));
            return false;
        }
        while ((isMale) ? currentKing.getOldestLegitimateSon() != null : currentKing.getOldestLegitimateDaughter() != null) {
            if (isMale) {
                currentKing.getOldestLegitimateSon().payForFuneral();
            } else {
                currentKing.getOldestLegitimateDaughter().payForFuneral();
            }
        }
        return true;
    }

    public void killAllKingsChildren() {
        killKingsSons();
        killKingsDaughters();
    }

    public class RoyalName {
        int usages;
        String name;

        RoyalName(String name) {
            usages = 0;
            this.name = name;
        }

        public String usagesToRomanNumbers() {
            int usagesCounter = this.usages;
            String s = "";
            while (usagesCounter >= 10) {
                s += "X";
                usagesCounter -= 10;
            }
            while (usagesCounter >= 9) {
                s += "IX";
                usagesCounter -= 9;
            }
            while (usagesCounter >= 5) {
                s += "V";
                usagesCounter -= 5;
            }
            while (usagesCounter >= 4) {
                s += "IV";
                usagesCounter -= 4;
            }
            while (usagesCounter >= 1) {
                s += "I";
                usagesCounter -= 1;
            }
            return s;
        }

        public String getName(String FamilyName) {
            usages++;
            return name + " " + FamilyName + " the " + usagesToRomanNumbers();
        }
    }

    public void initializeMaleNames() {
        this.boyNames.add(new RoyalName("Goliad"));
        this.boyNames.add(new RoyalName("Gareth"));
        this.boyNames.add(new RoyalName("David"));
        this.boyNames.add(new RoyalName("Cladert"));
        this.boyNames.add(new RoyalName("Gustav"));
        this.boyNames.add(new RoyalName("Darek"));
    }

    public void initializeFemaleNames() {
        this.girlNames.add(new RoyalName("Amber"));
        this.girlNames.add(new RoyalName("Ronnie"));
        this.girlNames.add(new RoyalName("Sophia"));
        this.girlNames.add(new RoyalName("Vivienne"));
        this.girlNames.add(new RoyalName("Juva"));
        this.girlNames.add(new RoyalName("Golia"));
    }

    public String createName(boolean isMale, String RulingFamilyName) {
        int a = random(1, 6);
        if (isMale) {
            if (boyNames.isEmpty()) {
                initializeMaleNames();
            }
            return this.boyNames.get(a - 1).getName(RulingFamilyName);
        } else {
            if (girlNames.isEmpty()) {
                initializeFemaleNames();
            }
            return this.girlNames.get(a - 1).getName(RulingFamilyName);
        }
    }

    public static int random(int min, int max) {
        return (int) (Math.random() * max) + min;
    }
}
