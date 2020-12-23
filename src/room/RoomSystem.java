package room;

import java.util.*;

/**
 * An upper controller of room
 * Stores RoomManager and RoomPresenter
 * Gets input from user and gives corresponding moves by calling methods from RoomManager
 * @author Group0694
 * @version 2.0.0
 */
public class RoomSystem {
    private final RoomManager roomManager;
    private RoomPresenter presenter;

    /**
     * Constructs a RoomSystem object
     * @param rooms An instance of RoomManager
     */
    public RoomSystem(RoomManager rooms) {
        this.roomManager = rooms;
        this.presenter = new RoomPresenter();
    }

    private int inputCapacity(Scanner sc){
        String capacity = sc.nextLine();
        int numCapacity = 0;
        while(numCapacity <= 0) {
            try {
                numCapacity = Integer.parseInt(capacity);

            } catch (NumberFormatException e) {
                presenter.invalidNumber();
                capacity = sc.nextLine();
                continue;
            }
            if(numCapacity <= 0){
                presenter.invalidNumber();
                capacity = sc.nextLine();
            }
        }
        return numCapacity;
    }

    private int validateInputTime(String input, boolean start) {
        // Return -2 if not integer, -1 not time range, else time
        try {
            int num = Integer.parseInt(input);
            if (start) {
                if (0 <= num && num <= 23) {
                    return num;
                }
            } else {
                if (1 <= num && num <= 24) {
                    return num;
                }
            }
            return -1;
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    private List<Integer[]> inputTimeSlots(Scanner c) {
        presenter.printInputTimeSlots();
        List<Integer[]> lst = new ArrayList<>();
        String input;
        int len;
        boolean start = true;
        Integer[] timeLst = new Integer[2];
        do {
            len = lst.size();
            presenter.inputStartOrEndTime(start ? "start " + (len + 1) : "end " + (len + 1));
            input = c.nextLine();
            if (input.equals("s")) {
                return lst;
            } else if (input.equals("r")) {
                start = true;
                if(len >= 1) {
                    Integer[] time = lst.remove(len - 1);
                    presenter.printRemoveTimeSlots(time);
                } else {
                    presenter.printEmptyTimeSlots();
                }
            } else {
                // We start ask user to input time slots
                int num = this.validateInputTime(input, start);
                if (num == -1) {
                    presenter.invalidTimeRange(start);
                    continue;
                } else if (num == -2) {
                    presenter.printInvalidInput();
                    continue;
                }
                // Now assume valid time range
                if(len == 0) {
                    if (start) {
                        timeLst[0] = num;
                    } else {
                        // Check num > timeLst[0]
                        if (num <= timeLst[0]) {
                            presenter.timeOverlap();
                            continue;
                        }
                        timeLst[1] = num;
                        lst.add(new Integer[]{timeLst[0], timeLst[1]});
                    }
                }
                else {
                    Integer[] time = lst.get(len - 1);
                    if (start) {
                        // Check time is <= previous end Time
                        if (num < time[1]) {
                            presenter.timeOverlap();
                            continue;
                        }
                        timeLst[0] = num;
                    } else {
                        if (num <= timeLst[0]) {
                            presenter.timeOverlap();
                            continue;
                        }
                        timeLst[1] = num;
                        lst.add(new Integer[]{timeLst[0], timeLst[1]});
                    }
                }
                start = !start;
            }
        } while (true);
    }

    // It solved the problem if user input their own time, i.e. 0-1, 2-3, 3-4, 5-6, 7-8, 8-10, 10-11
    // It will covert into 0-1, 2-4, 5-6, 7-11
    private List<Integer[]> refactorList(List<Integer[]> lst) {
        // Assume the length >= 1
        List<Integer[]> refactorLst = new ArrayList<>();
        Integer[] refactorItem = lst.get(0);
        int i = 1;
        while (i < lst.size()) {
            Integer[] nextItem = lst.get(i);
            if (refactorItem[1].equals(nextItem[0])) {
                refactorItem[1] = nextItem[1];
            } else {
                refactorLst.add(refactorItem);
                refactorItem = nextItem;
            }
            i ++;
        }
        // if (i > 1) {
        refactorLst.add(refactorItem);
        // }
        return refactorLst;
    }

    private Integer[][] inputRoomTimeSlots(Scanner c) {
        presenter.printTimeMenu();
        String choice;
        do {
            choice = c.nextLine();
            switch(choice) {
                case "0":
                    return new Integer[][] {{0, 24}};
                case "1":
                    return new Integer[][] {{9, 17}};
                case "2":
                    List<Integer[]> lst = inputTimeSlots(c);
                    if (lst.size() == 0) {
                        presenter.emptySlots();
                        return null;
                    }
                    return refactorList(lst).toArray(new Integer[0][0]);
                case "r":
                    presenter.printRedoMessage("Return to the previous page");
                    return null;
                default:
                    presenter.printInvalidInput();
                    presenter.getInput();
            }
        } while(true);
    }

    /**
     * Adds a new room
     */
    //add a new room
    public void addRoom () {
        Scanner sc = new Scanner(System.in);
        String roomName = getNewRoomName(sc);
        if (roomName == null) return;

        presenter.askNewRoomCapacity();
        int numCapacity = inputCapacity(sc);

        // Ask for available time slots
        Integer[][] availableTime = this.inputRoomTimeSlots(sc);
        if (availableTime == null) {
            // Error message is already printed
            return;
        }

        List<String> features = getRoomFeatures(sc);

        presenter.addANewRoom(roomManager.addRoom(numCapacity, availableTime, roomName, features));
        presenter.printRoomInfo(new ArrayList<>(Collections.singletonList(roomManager.printRoom(roomName))));

    }

    private String getNewRoomName(Scanner sc) {
        presenter.askNewRoomName();
        String roomName = sc.nextLine();
        if (roomName.equals("")) {
            presenter.roomNameEmpty();
            return null;
        }else if (roomManager.hasRoom(roomName)){
            presenter.roomExistMessage();
            return null;
        }
        return roomName;
    }

    private List<String> getRoomFeatures(Scanner sc){
        presenter.askRoomFeatures();
        List<String> availableFeatures = new ArrayList<>();
        roomManager.getAllFeatures().forEachRemaining(availableFeatures::add);
        List<String> featuresForRoom = new ArrayList<>();
        availableFeatures.add("Add new features");// Add a new feature not in the list

        while (true) {
            presenter.printMenu(availableFeatures, "Stop adding new features");
            String choice = sc.nextLine();
            int index;
            if (choice.equalsIgnoreCase("r")) {
                return featuresForRoom;
            }
            try {
                index = Integer.parseInt(choice);
            }catch (NumberFormatException e) {
                presenter.printInvalidInput();
                presenter.getInput();
                continue;
            }
            if (index < 0 || index >= availableFeatures.size()) {
                presenter.printInvalidInput();
                continue;
            }
            if (index != availableFeatures.size() - 1) {// remove the selected feature from available list then continue
                String feature = availableFeatures.remove(index);
                featuresForRoom.add(feature);
                presenter.printActionMessage("This room now has " + feature + " as a feature.");
            }
            else {
                String newFeature = addNewFeature(sc);
                if (newFeature != null) {
                    featuresForRoom.add(newFeature);
                    presenter.printActionMessage("This room now has " + newFeature + " as a feature.");
                }
            }
        }
    }

    private String addNewFeature(Scanner sc){
        presenter.askNewFeature();
        String feature = sc.nextLine();

        if (feature.equals("") || roomManager.hasFeature(feature)) return null;
        else {
            roomManager.addFeature(feature);
            presenter.printActionMessage("Added " + feature + " to features history list.");
            return feature;
        }
    }

    /**
     * Displays the String representation of room info
     */
    public void getRoomsInfo() {
        presenter.printRoomInfo(roomManager.getRoomsInfo());
    }

}
