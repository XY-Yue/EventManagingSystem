package userinterface;

import java.util.List;

/**
 * This class is an presenter class of VIPSystem. It extends the abstract class UserPresenter.
 * This class displays the message to the VIPSystem about what selections can be performed.
 * @author Group0694
 * @version 2.0.0
 */
public class VIPPresenter extends UserPresenter {

    /**
     * Displays the description of additional message status and sending options basing on VIP's authority.
     */
    @Override
    void printMessageAccessMenu() {
        List<String> options = super.getGeneralMessageAccessOptionMenu();
        options.add("view Speaker contact information of signed up events");
        super.printMenu(options, super.getReturnToMessage());
    }

    /**
     * Displays the description of additional event attending options basing on VIP's authority.
     */
    @Override
    void printEventScheduleMenu() {
        List<String> options = super.getGeneralEventScheduleOptionMenu();
        options.add("view all VIP only events");
        options.add("View VIP only events attendable");
        options.add("View VIP events attending");
        super.printMenu(options, super.getReturnToMessage());
    }
}
