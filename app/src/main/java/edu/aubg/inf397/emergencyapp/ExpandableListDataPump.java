package edu.aubg.inf397.emergencyapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class ExpandableListDataPump {
    public static ArrayList<String> getInfo(int id) {
        ArrayList<String> contactDetail = new ArrayList<>();

        switch (id) {
            case 1:
                contactDetail.add("Zhulieta Kuzmanska");
                contactDetail.add("Psychologist");
                contactDetail.add("6203 - ABF Center");
                contactDetail.add("9:30 AM - 5:30 PM");
                contactDetail.add("+359889533119");
                break;
            case 2:
                contactDetail.add("Ventsislav Daskalov");
                contactDetail.add("Doctor");
                contactDetail.add("Skaptopara 1 Hall");
                contactDetail.add("8:00 AM - 5:00 PM");
                contactDetail.add("+359887361454");
                break;
            case 3:
            case 4:
                contactDetail.add("Ilko Vangelov");
                contactDetail.add("Security");
                contactDetail.add("108A - Main Building");
                contactDetail.add("24 hours a day");
                contactDetail.add("+359889626308");
                break;
        }

        return contactDetail;
    }

    public static HashMap<String, List<String>> getData(int id) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<>();
        List<String> howDoIKnow = new ArrayList<>();
        List<String> untilHelpArrives = new ArrayList<>();

        switch (id) {
            case 1:
                howDoIKnow.add("You see someone who is weeping.");
                howDoIKnow.add("You hear someone who is yelling.");
                untilHelpArrives.add("Remain with the person and calm them.");
                untilHelpArrives.add("Attempt to find out what has happened.");
                break;
            case 2:
                howDoIKnow.add("You see someone who is injured.");
                howDoIKnow.add("You see someone who has fainted.");
                untilHelpArrives.add("Remain with the person and calm them.");
                untilHelpArrives.add("Attempt to find out what has happened.");
                break;
            case 3:
                howDoIKnow.add("You witness aggressive behavior.");
                howDoIKnow.add("You hear someone issuing threats.");
                untilHelpArrives.add("Try to avoid intervening if possible.");
                untilHelpArrives.add("Promptly retreat to a safe distance.");
                break;
            case 4:
                howDoIKnow.add("You see someone carrying a weapon.");
                howDoIKnow.add("You find a weapon left on campus.");
                untilHelpArrives.add("Do NOT try to apprehend the person.");
                untilHelpArrives.add("Promptly retreat to a safe distance.");
                break;
        }

        expandableListDetail.put("How do I know?", howDoIKnow);
        expandableListDetail.put("Until help arrives:", untilHelpArrives);
        return expandableListDetail;
    }
}