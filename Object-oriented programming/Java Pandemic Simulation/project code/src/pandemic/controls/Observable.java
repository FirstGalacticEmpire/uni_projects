package pandemic.controls;

import pandemic.shops.Shop;

import java.util.ArrayList;

/**
 * Observer pattern that detects if num of sick agents has crossed the lockdown threshold.
 */
public class Observable {
    private static Observable instance;
    private int numberOfSickAgents = 0;
    private int lockdownNumberOfSickAgents;
    private final ArrayList<Shop> listOfShopsObservers = new ArrayList<>();
    private boolean flag = false;

    /**
     * @return an instance of Observable.
     */
    public static synchronized Observable getInstance() {
        if (instance == null) {
            instance = new Observable();
        }
        return instance;
    }

    /**
     * @param shop Shop to be added to observers lists
     */
    public void addObserver(Shop shop) {
        this.listOfShopsObservers.add(shop);
    }


    private void setLockdownNumberOfSickAgents() {
        EnumControlVariables enumControlVariables = EnumControlVariables.getInstance();
        this.lockdownNumberOfSickAgents = enumControlVariables.getLockdownThreshold();
    }

    /**
     * Adds sick person to the number of sick agents, checks if we have crossed
     * the lockdown threshold, if so, introduces lockdown
     */
    public void addSickPerson() {
        setLockdownNumberOfSickAgents();
        this.numberOfSickAgents += 1;
        if (!flag) {
            if (numberOfSickAgents >= lockdownNumberOfSickAgents) {
                System.out.println("LOCKDOWN");
                flag = true;
                for (Shop shop : this.listOfShopsObservers) {
                    shop.update(true);

                }
            }
        }
    }

    /**
     * Decrements  the number of sick agents, checks if we are below
     * the lockdown threshold, if so, turn off lockdown
     */
    public void removeSickPerson() {
        setLockdownNumberOfSickAgents();
        this.numberOfSickAgents -= 1;
        if (flag) {
            if (numberOfSickAgents < lockdownNumberOfSickAgents) {
                System.out.println("UNLOCKDOWN");
                for (Shop shop : this.listOfShopsObservers) {
                    shop.update(false);
                }
            }
            flag = false;
        }
    }

}
