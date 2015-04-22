package potioncombiner;

//import org.tbot.client.GameObject;

import org.tbot.bot.TBot;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Item;


import javax.swing.*;

/**
 * Created by Igor on 010. Apr 10, 15.
 */

@Manifest(authors = "Igor_mec", name = "Potion Combiner", category = ScriptCategory.HERBLORE, description = "Combines (3) doses to (4) doses.", version = 1.0)
public class MainHandler extends AbstractScript {


    private String potionChoice;
    private final GUI gui = new GUI();

    public boolean onStart() {

        LogHandler.log("Potion Combiner started");
        gui.setVisible(true);

        return true;
    }


    private void statusCheck(){
        if(!TBot.getBot().getScriptHandler().getScript().isRunning()){

        }
    }


    public boolean withdrawPots(String potType, int amount){
        //WITHDRAW CODE
        if(Bank.getOpenTab()!=0){
            Bank.openTab(0);
            Time.sleep(800,1100);
        }
        Boolean containsPotType = Bank.contains(potType);
        Time.sleep(1500, 1800);

        if(containsPotType) {            //IF Energy Potion(3) Found

            LogHandler.log(potType + " FOUND.");
            Time.sleep(1000);


           // do {
                LogHandler.log("There are "+ Inventory.getCount());
                LogHandler.log("Withdrawing " + potType);
                Time.sleep(200);

                Bank.withdraw(potType, amount);     //Withdraw All
                Time.sleep(850, 1000);
            //}while(Inventory.getEmptySlots() != 0 /*|| Inventory.getCount() >= 1*/);

            /*******
             *
             *
             *
             *
             * ^^^^ ABOVE LOOP NEVER ENDS IF PERSON DOESN'T HAVE ENOUGH POTIONS
             * CHECK TO MAKE SURE PLAYER HAS AT LEAST ONE POT ( ABOVE CONDITION or CONDITION TO THE LEFT
             *
             *
             *
             *
             *
             */
            LogHandler.log("Potions WITHDRAWN.");
            Time.sleep(750);


            LogHandler.log("Closing bank");
            Time.sleep(1000);

            Bank.close();                       //Close bank screen


            LogHandler.log("Bank closed");
            Time.sleep(1000);

            return true;

        }else if(!Bank.contains(potType)){
            LogHandler.log("Potions NOT FOUND! Stopping script.");  //PRINT "No energy potions"
            return false;//STOP SCRIPT
        }else {
            LogHandler.log("Unknown error, stopping script.");  //PRINT "No energy potions"
            return false;//STOP SCRIPT
        }
    }

    //Uses slot grabbing (Rewrite for more modular design)
    //Requires Full inventory of potions
    public void combinePots(){
        LogHandler.log("Time to combine potions.");
        Time.sleep(1000);

        Item potToEmpty, potToFill;
        for(int x=0; x<=24; x+=4){
            if(TBot.getBot().getScriptHandler().getScript().isRunning()) {
                potToEmpty = Inventory.getInSlot(x);
                for (int y = 0; y < 3; y++) {
                    if(TBot.getBot().getScriptHandler().getScript().isRunning()) {
                        potToFill = Inventory.getInSlot(x + y + 1);
                        Inventory.useItemOn(potToEmpty, potToFill);
                        Time.sleep(600, 750);
                        TBot.getBot().getScriptHandler().getScript().isRunning();
                    }else{
                        break;
                    }
                }
            }else{
                break;
            }
        }

        LogHandler.log("Potion combining COMPLETE.");
        Time.sleep(1000);

    }



    @Override
    public int loop() {
        LogHandler.log("Start of loop");
        /*/***NOTE***///  Add: if bank is closed and inventory is empty, open bank(Open bank logic should be separate method)
        /*/***NOTE***  The loop method will check if the stop button is pressed every time it restarts,
                       therefore, the loop will finish doing its current runthrough before aborting any actions.   ///*/


        while(gui.isVisible()){
            Time.sleep(500);
        }

        potionChoice = gui.finalChoice;

        LogHandler.log("The potion choice is: "+potionChoice);
        if (Bank.isOpen()) {
            LogHandler.log("The bank window is already open.");
            Time.sleep(1000);


            //Deposit All
            int inv = Inventory.getEmptySlots();
            LogHandler.log("There are: " + Inventory.getCount());

            if (inv < 28) {

                LogHandler.log("There are only "+inv+" empty slots. Inventory not empty.");

                Bank.depositAll();
                Time.sleep(1000);

                inv = Inventory.getEmptySlots();
                LogHandler.log("There are now "+inv+" empty slots in the inventory.");
                Time.sleep(1000);

                Time.sleep(Random.nextInt(1100, 1300));
            }

            LogHandler.log(inv + " empty slots in the inventory.");
            Time.sleep(1000);

            LogHandler.log("Calling Withdraw method.");
            Time.sleep(1000);

            if(withdrawPots(potionChoice, 28)){

                LogHandler.log("Potion withdrawal SUCCESSFUL!");
                Time.sleep(1000);

            }else{
                LogHandler.log("Potion withdrawal FAILED! Stopping script.");
                Time.sleep(1000);
                LogHandler.log("Exiting main loop");
                return -1;
            }
            Time.sleep(850, 1000);


            LogHandler.log("Calling combine pots method.");
            Time.sleep(1000);


            combinePots();

            LogHandler.log("Restarting loop");
            return 150;


        } else if (!Bank.isOpen()) {

            LogHandler.log("Bank screen is not open.");
            Time.sleep(1000);

            LogHandler.log("Searching for nearest bank booth");
            Time.sleep(1000);

            final GameObject bankBooth = GameObjects.getNearest("Bank booth");


            if (bankBooth != null && bankBooth.isOnScreen()) {

                LogHandler.log("Bank booth FOUND ON SCREEN");
                Time.sleep(1000);

                if (Players.getLocal().getAnimation() == -1) {

                    LogHandler.log("Player is idle.");
                    Time.sleep(1000);

                    /*Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return Players.getLocal().getAnimation() == -1;
                                        }
                                    },
                            Random.nextInt(2000, 2500)
                    );*/

                    LogHandler.log("Opening bank");
                    Time.sleep(1000);

                    bankBooth.interact("Bank");
                    Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return Bank.isOpen();
                                        }
                                    },
                            Random.nextInt(3000, 4000)
                    );
                    if(Bank.isOpen()){

                        LogHandler.log("Bank screen is now OPEN.");
                        Time.sleep(1000);

                    }else{

                        LogHandler.log("Bank has not been opened.");
                        Time.sleep(1000);

                    }

                    LogHandler.log("Restart loopp");
                    Time.sleep(500);

                    return 150;

                }else{

                    LogHandler.log("Player not Idle, waiting");
                    //Time.sleep(1000);
                    Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return Players.getLocal().getAnimation() == -1;
                                        }
                                    },
                            Random.nextInt(3500, 5000)
                    );

                }
            } else if (bankBooth != null && !(bankBooth.isOnScreen())) {

                LogHandler.log("Bank FOUND NOT ON SCREEN.");
                Time.sleep(500);

                if (bankBooth.distance() > 6) {
                    do{
                        LogHandler.log("Bank is further than 6 tiles. Searching for path to bank booth...");
                        Time.sleep(500);

                        Path pathToBooth = Walking.findPath(bankBooth);
                        if (pathToBooth != null) {

                            LogHandler.log("Path Found. Begin walking.");
                            Time.sleep(250);

                            pathToBooth.traverse();
                            Time.sleepUntil(new Condition() {
                                                @Override
                                                public boolean check() {
                                                    return bankBooth.distance() < 6 || bankBooth.isOnScreen();
                                                }
                                            }, Random.nextInt(2800, 3000)
                            );

                            if(bankBooth.distance() < 6){

                                LogHandler.log("Distance is less tan 6 tiles.");
                                Time.sleep(750);

                            }else if(bankBooth.isOnScreen()){

                                LogHandler.log("Bank booth is on screen.");
                                Time.sleep(1000);

                            }else{

                                LogHandler.log("Bank booth is still too far and not on screen.");
                                Time.sleep(1000);

                            }
                        }
                    }while(bankBooth.distance() > 6);

                    LogHandler.log("Restart loop");
                    Time.sleep(500);

                    return 120;

                } else if (bankBooth.distance() <= 6) {

                    LogHandler.log("Distance to bank booth is less than 6 tiles, turning camera.");
                    Time.sleep(250);

                    Camera.turnTo(bankBooth);
                    Time.sleep(1200, 1600);

                    LogHandler.log("Searching for path...");
                    Time.sleep(500);

                    Path pathToBooth = Walking.findPath(bankBooth);
                    if (pathToBooth != null) {

                        LogHandler.log("Path FOUND. Beginning walk");
                        Time.sleep(250);

                        pathToBooth.traverse();/***********************/

                        Time.sleep(2000);
                        LogHandler.log("Opening bank");

                        bankBooth.interact("Bank");
                        Time.sleepUntil(new Condition() {
                                            @Override
                                            public boolean check() {
                                                return Bank.isOpen();
                                            }
                                        },
                                Random.nextInt(3000, 4000)
                        );
                        if(Bank.isOpen()){

                            LogHandler.log("Bank screen is now OPEN.");
                            Time.sleep(1000);

                        }else{

                            LogHandler.log("Bank has not been opened.");
                            Time.sleep(1000);

                        }
                    }
                } else if (bankBooth == null) {

                    LogHandler.log("Could not find bank booth.");
                }
            }
        }
        else{
            LogHandler.log("Apparently the bank screen is neither open or closed.....");
            Time.sleep(1000);
        }


        LogHandler.log("Exiting main loop");
        return -1;
    }
}
