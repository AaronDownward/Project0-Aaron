package com.Revature.AaronDownward.Project0;

import java.util.ArrayList;
import java.util.concurrent.Callable;

import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

@Command(name = "viewcal", description = "View a specified calendar")
public class ViewCalendarCommand implements Callable<Integer> {

    @Option(names = {"-w", "--wizard"}, paramLabel = "View Calendar Wizard", description = "Indication that user would like to go through wizard to select viewing options")
    private boolean wizard;

    @Option(names = {"-c", "--calendar"}, paramLabel = "Calendar ID", description = "The calendar ID")
    private String calendarId;

    @Option(names = {"-d", "--day"}, arity = "0..1", fallbackValue = "today", paramLabel = "View day", description = "Views a day's events. If a date is passed into the option, views that day, otherwise defaults to viewing the current date")
    private String dayDate;

    @Option(names = {"-e", "--week"}, arity = "0..1", fallbackValue = "today", paramLabel = "View week", description = "Views a week's events. If a date is passed into the option, views the week starting from that date, otherwise defaults to viewing the current week")
    private String weekDate;

    @Option(names = {"-m", "--month"}, arity = "0..1", fallbackValue = "current", paramLabel = "View month", description = "Views a month's events. If a month is passed into the option, views that month, otherwise defaults to viewing the current week")
    private String month;

    @Option(names = {"-u", "--custom"}, arity = "2", paramLabel = "Custom view", description = "Views the events for a custom time period. Requires two dates to passed in, then displays all the events for that period")
    private String[] customDates;

    @Override
    public Integer call() {
        if (wizard) {
            ArrayList<String> wizardParams = UserInput.viewCalendarWizard();
            if(wizardParams == null) {
                System.out.println("The view calendar wizard didn't work properly");
                return 3;
            }
            calendarId = wizardParams.get(0);
            switch (wizardParams.get(1)) {
                case "d": 
                    dayDate = wizardParams.get(2);
                    break;
                case "w":
                    weekDate = wizardParams.get(2);
                    break;
                case "m":
                    month = wizardParams.get(2);
                    break;
                case "c":
                    customDates = new String[2];
                    customDates[0] = wizardParams.get(2);
                    customDates[1] = wizardParams.get(3);
                    break;
                default:
                    System.out.println("The view calendar wizard didn't work properly");
                    return 1;
            }
        }
        if (dayDate != null) {
            Print.printDay(calendarId, dayDate);
        } else if (weekDate != null) {
            Print.printWeek(calendarId, weekDate);
        } else if (month != null) {
            Print.printMonth(calendarId, month);
        } else if (customDates != null) {
            Print.printCustom(calendarId, customDates);
        } else {
            System.out.println("Something went wrong with parameters passed in");
            return 2;
        }

        return 0;
    }

}
