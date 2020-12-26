package userinterface;

import account.*;
import event.EventManager;
import message.MessagingManager;
import room.RoomManager;

/**
 * UserSystemFactory is a factory class to construct a new UserSystem
 * UserSystemFactory is given type, username, MessageSystem, EventSystem, RoomSystem and
 * AccountSystem for the new UserSystem as parameter.
 * If the type is attendee, then it constructs a new AttendeeSystem with the given parameters.
 * If the type is speaker, then it constructs a new SpeakerSystem with the given parameters.
 * If the type is organizer, then it constructs a new OrganizerSystem with the given parameters.
 * If the type is VIP, then it constructs a new VIPSystem with the given parameters.
 * Else returns null.
 * @author Group0694
 * @version 2.0.0
 */
public class UserSystemFactory {

    /**
     * Constructs a subclass of UserSystem based on the type
     * @param type the type of the user logged in
     * @param username The username of the account logged in
     * @param messagingManager A copy of the MessagingManager use case
     * @param accountManager A copy of the AccountManager use case
     */
    public UserSystem makeUserSys(String type, String username, AccountManager accountManager,
                                  MessagingManager messagingManager){
        switch (type){
            case "Attendee":
                return new AttendeeSystem(username, messagingManager, accountManager);
            case "Organizer":
                return new OrganizerSystem(username, messagingManager, accountManager);
            case "Speaker":
                return new SpeakerSystem(username, messagingManager, accountManager);
            case "VIP":
                return new VIPSystem(username, messagingManager, accountManager);
            default: return null;
        }
    }
}
