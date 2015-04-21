package potioncombiner;

//import org.tbot.client.GameObject;
import org.tbot.gui.frame.TFrame;
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

@Manifest(authors = "Igor", name = "Potion Combiner", category = ScriptCategory.HERBLORE, description = "Combines (3) doses to (4) doses.", version = 1.0)
public class MainHandler extends AbstractScript {


    //String potionChoice = "";
    //TFrame fr = new TFrame(5,100);

    //boolean pressed = false;
    public boolean onStart() {
        LogHandler.log("Potion Combiner started");
        //LogHandler.log("Creating new UI");
        //GUI g = new GUI();
        //g.setVisible(true);
        //pressed = g.getButtonPressed();

        /*Time.sleepUntil(new Condition() {
            @Override
            public boolean check() {
                return pressed;
            }
        });*/
        //LogHandler.log("Returning from onStart");
        return true;
    }


    public boolean withdrawPots(String potType, int amount){
        //WITHDRAW CODE

        if(Bank.contains(potType)) {            //IF Energy Potion(3) Found

            LogHandler.log("Prayer potions(3) FOUND.");
            Time.sleep(1000);


            LogHandler.log("Withdrawing prayer potions(3).");
            Time.sleep(1000);

            Bank.withdraw(potType, amount);     //Withdraw All

            LogHandler.log("Potions WITHDRAWN.");
            Time.sleep(1000);


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
        //String[] pots = {"", "Energy potion(1)", "Energy potion(2)", "Energy potion(3)", "Energy potion(4)"};
        Item potToEmpty, potToFill;
        for(int x=0; x<=24; x+=4){
            potToEmpty = Inventory.getInSlot(x);
            for(int y=0; y<3; y++){
                potToFill = Inventory.getInSlot(x+y+1);
                Inventory.useItemOn(potToEmpty, potToFill);
                Time.sleep(600,750);
            }
        }
    }



    @Override
    public int loop() {

        ///***NOTE***///  Add: if bank is closed and inventory is empty, open bank(Open bank logic should be separate method)



        if (Bank.isOpen()) {
            LogHandler.log("The bank window is already open.");
            Time.sleep(1000);


            //Deposit All
            int inv = Inventory.getEmptySlots();
            if (inv < 28) {


                LogHandler.log("There are only "+inv+" empty slots. Inventory not empty.");
                Time.sleep(1000);


                Bank.depositAll();

                inv = Inventory.getEmptySlots();
                LogHandler.log("There are now "+inv+" empty slots in the inventory.");
                Time.sleep(1000);

                Time.sleep(Random.nextInt(1100, 1300));
            }

            LogHandler.log(inv+" empty slots in the inventory.");
            Time.sleep(1000);

            LogHandler.log("Calling Withdraw method.");
            Time.sleep(1000);

            if(withdrawPots("Prayer potion(3)", 28)){

                LogHandler.log("Potion withdrawal SUCCESSFUL!");
                Time.sleep(1000);

            }else{
                LogHandler.log("Potion withdrawal FAILED!");
                Time.sleep(1000);
                return -1;
            }
            Time.sleep(850, 1000);


            LogHandler.log("Calling combine pots method.");
            Time.sleep(1000);


            combinePots();


        } else if (!Bank.isOpen()) {
            final GameObject bankBooth = GameObjects.getNearest("Bank booth");

            if (bankBooth != null && bankBooth.isOnScreen()) {
                if (Players.getLocal().getAnimation() == -1) {
                    Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return Players.getLocal().getAnimation() == -1;
                                        }
                                    },
                            Random.nextInt(2000, 2500)
                    );
                    bankBooth.interact("Bank");
                    Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return Bank.isOpen();
                                        }
                                    },
                            Random.nextInt(1200, 1600)
                    );

                }
            } else if (bankBooth != null && !(bankBooth.isOnScreen())) {
                if (bankBooth.distance() > 6) {
                    Path pathToBooth = Walking.findPath(bankBooth);
                    if (pathToBooth != null) {
                        pathToBooth.traverse();
                        Time.sleepUntil(new Condition() {
                                            @Override
                                            public boolean check() {
                                                return bankBooth.distance() < 6 || bankBooth.isOnScreen();
                                            }
                                        }, Random.nextInt(2800, 3000)
                        );
                    }
                } else if (bankBooth.distance() <= 6) {
                    Camera.turnTo(bankBooth);
                    Time.sleep(800, 1000);
                    Path pathToBooth = Walking.findPath(bankBooth);
                    if (pathToBooth != null) {
                        pathToBooth.traverse();
                        Time.sleepUntil(new Condition() {
                                            @Override
                                            public boolean check() {
                                                return bankBooth.isOnScreen();
                                            }
                                        },
                                Random.nextInt(2800, 3000)
                        );
                        bankBooth.interact("Bank");
                        Time.sleepUntil(new Condition() {
                                            @Override
                                            public boolean check() {
                                                return Bank.isOpen();
                                            }
                                        },
                                Random.nextInt(1200, 1600)
                        );
                    }
                } else if (bankBooth == null) {

                    LogHandler.log("bankBooth is null");
                }
            }
        }
        //LogHandler.log("Returning from main loop");
        return -1;
    }
}
