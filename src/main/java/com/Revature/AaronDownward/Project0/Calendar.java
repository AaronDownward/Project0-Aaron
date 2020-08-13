package com.Revature.AaronDownward.Project0;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;

public class Calendar {

    private String id;
    private Map<String, Event> events;

    //constructor to intialize a new calendar with given id
    public Calendar(String id) {
        this.id = id;
        this.events = new HashMap<String, Event>();
    }

    //creates a new calendar with a given id and adds it to file
    public static void createCalendar(String id) {
        Calendar calendar = new Calendar(id);
        //@ToWorkOn implement method/use code to create filepath in robust way instead of hardcoding for my system
        String filepath = "C:\\Users\\downw\\Revature Training\\Project0\\Project0-Aaron\\user_data\\" + calendar.id + ".json";
        createCalendarFile(calendar, filepath);
        writeToCalendarFile(calendar, filepath);
    }

    //creates new event, adding to hashmap events with given id key and editing the calendar file
    public void createEvent(String eventId, String name, String date, String endDate, String startTime, String endTime,	String description, List<String> attendees) {
        this.events.put(eventId, new Event(eventId, this.id, name, date, endDate, startTime, endTime, description, attendees));
        String filepath = "C:\\Users\\downw\\Revature Training\\Project0\\Project0-Aaron\\user_data\\" + this.id + ".json";
        writeToCalendarFile(this, filepath);
	}

    public void createEvent(String[] eventDetails) {
        String eventId = eventDetails[0];
        this.events.put(eventId, new Event(eventDetails));
        String filepath = "C:\\Users\\downw\\Revature Training\\Project0\\Project0-Aaron\\user_data\\" + this.id + ".json";
        writeToCalendarFile(this, filepath);
	}

    //edits a specified parameter of an event with the given value
    public void editEvent(String eventId, String parameter, String value) {
        Event event = this.events.get(eventId);
        switch (parameter) {
            case "id":
                System.out.println("Can't change id of existing event");                
                break;
            case "calendarId":
                System.out.println("Can't change calendar of existing event");
                break;
            case "attendees":
                System.out.println("Not yet implementing way to edit attendess");
                break;
            case "name":
                event.name = value;
                break;
            case "date":
                event.date = value;
                break;
            case "startTime":
                event.startTime = value;
                break;
            case "endTime":
                event.endTime = value;
                break;
            case "description":
                event.description = value;
                break;
            default:
                System.out.println("Not an existing parameter of an event.");
        }
        String filepath = "C:\\Users\\downw\\Revature Training\\Project0\\Project0-Aaron\\user_data\\" + this.id + ".json";
        Calendar.writeToCalendarFile(this, filepath);
	}
    
    //creates a new .json file for a calendar if it doesn't exist
    private static void createCalendarFile(Calendar calendar, String filepath) {
        try {
            File file = new File(filepath);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.err.println("Unable to create file");
            e.printStackTrace();
        }
    }

    //writes json formatted text to the calendar.json file
    public static void writeToCalendarFile(Calendar calendar, String filepath) {
        try {
            String textForFile = createJSONFromCalendar(calendar);
            FileWriter writer = new FileWriter(filepath);
            writer.write(textForFile);
            writer.close();
            System.out.println("Successfully wrote to file");
        } catch (IOException e) {
            System.err.println("Problem while writing to file");
            e.printStackTrace();
        }
    }

    //formats a calendar object into json text
    //somewhat brute force method of converting/writing object as json, considering finding more elegant/easier to control/edit method
    private static String createJSONFromCalendar(Calendar calendar) {
        String json = "{\n\t\"calendarId\": \"" + calendar.id + "\",\n\t\"events\": [";
        int eventCounter = 0;
        for (Event event : calendar.events.values()) {
            eventCounter++;
            json += "\n\t\t{\n\t\t\t\"eventId\": \"" + event.id + "\",";
            json += "\n\t\t\t\"calendarId\": \"" + event.calendarId + "\",";
            json += "\n\t\t\t\"eventName\": \"" + event.name + "\",";
            json += "\n\t\t\t\"date\": \"" + event.date + "\",";
            json += "\n\t\t\t\"endDate\": \"" + event.endDate + "\",";
            json += "\n\t\t\t\"startTime\": \"" + event.startTime + "\",";
            json += "\n\t\t\t\"endTime\": \"" + event.endTime + "\",";
            json += "\n\t\t\t\"description\": \"" + event.description + "\",";
            json += "\n\t\t\t\"attendees\": [";
            for (int i = 0; i < event.attendees.size(); i++) {
                json += "\n\t\t\t\t{\n\t\t\t\t\t\"attendeeName\": \"" + event.attendees.get(i) + "\"\n\t\t\t\t}";
                if (i < event.attendees.size() - 1) { 
                    json += ","; 
                }             
            }
            json += "\n\t\t\t]\n\t\t}";
            if (eventCounter < calendar.events.size()) { 
                json += ",";
            }
        }
        json += "\n\t]\n}";
        
        return json;
    }

    //returns the Event with the given id
	public Event getEventById(String eventId) {
		return this.events.get(eventId);
    }
    
    //accesses the json file: calendarId.json and returns a Calendar instantiated to those values
    public static Calendar getCalendarById(String calendarId) {
        String filepath = "C:\\Users\\downw\\Revature Training\\Project0\\Project0-Aaron\\user_data\\" + calendarId + ".json";
        ArrayList<String> textFromFile = readCalendarFile(filepath);
        Calendar calendar = createCalendarObjFromFile(textFromFile);
		return calendar;
	}

    //uses the text from a calendarId.json file and constructs an object initialized to values in text
    //somewhat brute force method and prone to problems if json file doesn't follow exact format
    private static Calendar createCalendarObjFromFile(ArrayList<String> textFromFile) {
        String calendarId = returnJSONValue(textFromFile.get(1));
        Calendar calendar = new Calendar(calendarId);

        Event event;
        for(int i = 4; i < textFromFile.size(); i++) {
            if(textFromFile.get(i).contains("eventId")) {
                String eventId = returnJSONValue(textFromFile.get(i++));
                //already have calendarId variable store, skip to next line
                i++;
                String eventName = returnJSONValue(textFromFile.get(i++));
                String date = returnJSONValue(textFromFile.get(i++));
                String endDate = returnJSONValue(textFromFile.get(i++));
                String startTime = returnJSONValue(textFromFile.get(i++));
                String endTime = returnJSONValue(textFromFile.get(i++));
                String description = returnJSONValue(textFromFile.get(i));
                ArrayList<String> attendees = new ArrayList<String>();
                while (textFromFile.get(i + 3).contains("attendeeName")) {
                    i += 3;
                    attendees.add(returnJSONValue(textFromFile.get(i)));
                }
                event = new Event(eventId, calendarId, eventName, date, endDate, startTime, endTime, description, attendees);
                calendar.events.put(eventId, event);
            }
        }
        return calendar;
    }

    //extracts the value from a line of a JSON code
    //pulls value from between the second set of quotation marks
    //ex:           "key": "value"
    private static String returnJSONValue(String jsonLine) {
        int endIndex = jsonLine.lastIndexOf('\"');
        int startIndex = jsonLine.lastIndexOf('\"', endIndex - 1) + 1;
        String value = jsonLine.substring(startIndex, endIndex);
        return value;
    }

    //returns a string array list of each line from a text file
    private static ArrayList<String> readCalendarFile(String filepath) {
        ArrayList<String> textFromFile = new ArrayList<String>();
        try {
            File file = new File(filepath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                textFromFile.add(scanner.nextLine());
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.err.println("Problem finding or scanning file");
            e.printStackTrace();
        }
        return textFromFile;
    }

	public static boolean calendarExists(String calendarId) {
		return true;
	}

	public void deleteEvent(String eventId) {
        events.remove(eventId);
        String filepath = "C:\\Users\\downw\\Revature Training\\Project0\\Project0-Aaron\\user_data\\" + this.id + ".json";
        Calendar.writeToCalendarFile(this, filepath);
	}

	public void replaceEvent(Event event, String eventId) {
        this.events.put(eventId, event);
	}

}