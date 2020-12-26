package conferencemain;

import data.DataSaver;
import data.UserDataConverter;
import event.EventManager;
import message.MessagingManager;
import room.RoomManager;
import account.AccountManager;

/**
 * A controller class can initialize messageSearchingSystem, eventSystem, roomSystem, accountSystem,
 * and conferenceRegisterSystem
 * @author Group0694
 * @version 2.0.0
 */
public class InitializingSystem{
    // Storing all 4 managers to avoid long parameters for most methods in this class, and convenience
    // Knowing that this class only responsible for creating these use cases and sending them to gateway
    private AccountManager accountManager;
    private MessagingManager messagingManager;

    /* helper for run() */
    private boolean getData(){
        try {
            this.readMessage();

            this.readAccount();

            return true;
        } catch (ClassNotFoundException cnfe){
            return false;
        }
    }

    /* helper for run() */
    private void saveData(){
        DataSaver<MessagingManager> messageSaver = new DataSaver<>();
        messageSaver.saveToFile(messageSaver.getSrcPath("MessageDataBase.ser"), messagingManager);

        DataSaver<AccountManager> accountSaver = new DataSaver<>();
        accountSaver.saveToFile(accountSaver.getSrcPath("UserInfoDataBase.ser"), accountManager);
    }

    /**
     * initialize by get all data needed in messageSearchingSystem, eventSystem, roomSystem, accountSystem and save data if
     * the program starts successfully and user choose to save data
     */
    public void run(){
        if (this.getData()) {
            WelcomeSystem welcome = new WelcomeSystem(accountManager, messagingManager);
            if (welcome.start()) {
                this.saveData();
            }
        }
    }

    private void readAccount() throws ClassNotFoundException {
        DataSaver<AccountManager> readAccount = new DataSaver<>();
        this.accountManager = readAccount.readFromFile(readAccount.getSrcPath("UserInfoDataBase.ser"));
        if (this.accountManager == null) {
            UserDataConverter converter = new UserDataConverter();
            converter.convertData(readAccount.getSrcPath("UserTxtDataBase.txt"),
                    readAccount.getSrcPath("UserInfoDataBase.ser"), readAccount);
            this.accountManager = readAccount.readFromFile(readAccount.getSrcPath("UserInfoDataBase.ser"));
        }
    }

    private void readMessage() throws ClassNotFoundException {
        DataSaver<MessagingManager> readMessage = new DataSaver<>();
        this.messagingManager = readMessage.readFromFile(readMessage.getSrcPath("MessageDataBase.ser"));
        if (this.messagingManager == null) this.messagingManager = new MessagingManager();
    }

}
