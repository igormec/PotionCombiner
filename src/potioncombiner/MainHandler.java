package potioncombiner;

//import org.tbot.client.GameObject;

import org.tbot.bot.TBot;
import org.tbot.graphics.MouseTrail;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.Manifest;
import org.tbot.internal.ScriptCategory;
import org.tbot.internal.event.listeners.PaintListener;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.*;
import org.tbot.methods.Mouse;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.GameObject;
import org.tbot.wrappers.Item;


import javax.swing.*;
import java.awt.*;

/**
 * Created by Igor on 010. Apr 10, 15.
 */

@Manifest(authors = "Igor_mec", name = "Potion Combiner", category = ScriptCategory.HERBLORE, description = "Combines (3) doses to (4) doses.", version = 1.1)
public class MainHandler extends AbstractScript implements PaintListener {


    private final MouseTrail mt = new MouseTrail();
    private String potionChoice;
    private final GUI gui = new GUI();

    public boolean onStart() {

        LogHandler.log("Potion Combiner started");
        gui.setVisible(true);
        Mouse.setSpeed((int)(Mouse.getSpeed()*1.3));
        while(gui.isVisible()){
            Time.sleep(500);
            //if(sc()){return -1;}
        }

        potionChoice = gui.finalChoice;

        return true;
    }

    //if sc returns true, the bot has been stopped
    private boolean sc() {
        return !TBot.getBot().getScriptHandler().getScript().isRunning();
    }


    private boolean withdrawPots(String potType, int amount){
        //WITHDRAW CODE
        if(Bank.getOpenTab()!=0){
            Bank.openTab(0);
            Time.sleep(800,1100);
        }
        if(sc()){return false;}
        boolean containsPotType = Bank.contains(potType);
        LogHandler.log(potType + " FOUND = "+containsPotType);

        if(containsPotType) {
            LogHandler.log(potType + " FOUND.");
            //Time.sleep(1000);

            boolean empty;
            do {
                if(sc()){return false;}
                //LogHandler.log("There are " + Inventory.getCount());
                LogHandler.log("Withdrawing " + potType);
                //Time.sleep(200);

                Bank.withdraw(potType, amount);     //Withdraw All
                Time.sleep(950, 1100);
                LogHandler.log("There are " + Inventory.getCount());
                empty = Inventory.getCount() < 1;
                Time.sleep(200);
            } while(empty);

            if(sc()){return false;}

            //}while(Inventory.getEmptySlots() != 0 /*|| Inventory.getCount() >= 1*/);

            /**^^^^ ABOVE LOOP NEVER ENDS IF PERSON DOESN'T HAVE ENOUGH POTIONS
             * CHECK TO MAKE SURE PLAYER HAS AT LEAST ONE POT ( ABOVE CONDITION or CONDITION TO THE LEFT             *
             */
            LogHandler.log("Potions WITHDRAWN.");
            //Time.sleep(750);


            LogHandler.log("Closing bank");
            //Time.sleep(1000);

            Bank.close();                       //Close bank screen
            Time.sleep(800);

            LogHandler.log("Bank closed");
            //Time.sleep(1000);

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
    private boolean combinePots(String potType){
        LogHandler.log("Time to combine potions.");
        //Time.sleep(1000);

        Item potToEmpty, potToFill;

        for(int row=0; row<=24; row+=4){
            if(sc()){return false;}
            potToEmpty = Inventory.getInSlot(row);
            for (int col = 1; col < 4; col++) {
                potToFill = Inventory.getInSlot(row + col);
                LogHandler.log("Combining: "+ potToEmpty.getName()+"  with  "+potToFill.getName());
                Inventory.useItemOn(potToEmpty, potToFill);
                Time.sleep(500, 600);
                potToEmpty = Inventory.getInSlot(row);
                if(sc()){return false;}
            }
        }

        /*
        String dose1 = potType.substring(0,(potType.length() - 2)) + "1)";
        LogHandler.log("Dose 1 is called: " + dose1);

        //Prayer potion(3)
        //length = 16
        //lastIndex = 15
        //0, length - 3  =  0, 13  = "Prayer potion("

        LogHandler.log("There are "+Inventory.getCount(dose1)+"  "+dose1);
        LogHandler.log("There are "+Inventory.getCount(potType)+"  "+potType);
        LogHandler.log(Inventory.getCount(dose1) >= 1);
        LogHandler.log(Inventory.getCount(dose1) == Inventory.getCount(potType));

        String arr="Inventory: [";

        for(int x=0; x<Inventory.getInventory().length;x++){
            arr += Integer.toString(Inventory.getInventory()[x]);
            if(x!=Inventory.getInventory().length-1){
                arr += ", ";
            }
        }
        arr += "]";

        LogHandler.log(arr);

        if(Inventory.getCount(dose1) >= 1  &&  Inventory.getCount(dose1) == Inventory.getCount(potType)) {
            for (int x = 0; x < Inventory.getCount(dose1); x++) {
                potToFill = Inventory.getItemClosestToMouse(potType);
                potToEmpty = Inventory.getItemClosestToMouse(dose1);
                if(sc()){return false;}
                LogHandler.log("Combining: "+ potToEmpty.getName()+"  with  "+potToFill.getName());
                if (potToFill != null && potToEmpty != null) {
                    Inventory.useItemOn(potToEmpty, potToFill);
                    Time.sleep(400, 550);
                }
            }
        }
            //return true;
        //}

    //[INFO] Thu Apr 23 08:23:19 EDT 2015: potioncombiner.MainHandler: Inventory: [161, 2440, 2440, 157, 229, 2440, 2440, 2440, 229, 2440, 2440, 2440, 161, 2440, 157, 2440, 161, 2440, 157, 2440, 229, 2440, 2440, 2440, 161, 2440, 157, 2440]
        /*
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
*/
        LogHandler.log("Potion combining COMPLETE.");
        return true;
        //Time.sleep(1000);

    }



    @Override
    public int loop() {
        LogHandler.log("Start of loop");
        /*/***NOTE***///  Add: if bank is closed and inventory is empty, open bank(Open bank logic should be separate method)
        /*/***NOTE***  The loop method will check if the stop button is pressed every time it restarts,
                       therefore, the loop will finish doing its current runthrough before aborting any actions.   ///*/




        LogHandler.log("The potion choice is: "+potionChoice);
        if (Bank.isOpen()) {
            LogHandler.log("The bank window is already open.");
            //Time.sleep(1000);


            //Deposit All
            int inv = Inventory.getEmptySlots();
            LogHandler.log("There are " + Inventory.getCount()+" items in the inventory.");

            if (inv < 28) {

                LogHandler.log("There are only "+inv+" empty slots. Inventory not empty.");

                Bank.depositAll();
                Time.sleep(850);

                inv = Inventory.getEmptySlots();
                LogHandler.log("There are now "+inv+" empty slots in the inventory.");
                //Time.sleep(1000);

                //Time.sleep(Random.nextInt(1100, 1300));
            }

            if(sc()){return -1;}

            LogHandler.log(inv + " empty slots in the inventory.");
            //Time.sleep(1000);

            LogHandler.log("Calling Withdraw method.");
           // Time.sleep(1000);

            if(withdrawPots(potionChoice, 28)){

                LogHandler.log("Potion withdrawal SUCCESSFUL!");
                //Time.sleep(1000);

            }else{
                LogHandler.log("Potion withdrawal FAILED! Stopping script.");
                //Time.sleep(1000);
                //LogHandler.log("Exiting main loop");
                return -1;
            }
            //Time.sleep(850, 1000);

            if(sc()){return -1;}

            LogHandler.log("Calling combine pots method.");
            //Time.sleep(1000);



            if(combinePots(potionChoice)){

                LogHandler.log("Potion combining SUCCESSFUL!");
                //Time.sleep(1000);

            }else{
                LogHandler.log("Potion combining FAILED! Stopping script.");
                //Time.sleep(1000);
                //LogHandler.log("Exiting main loop");
                return -1;
            }

            LogHandler.log("Restarting loop");
            return 150;

        } else if (!Bank.isOpen()) {

            LogHandler.log("Bank screen is not open.");
            //Time.sleep(1000);

            LogHandler.log("Searching for nearest bank booth");
            //Time.sleep(1000);

            final GameObject bankBooth = GameObjects.getNearest("Bank booth");

            if(sc()){return -1;}

            if (bankBooth != null && bankBooth.isOnScreen()) {

                LogHandler.log("Bank booth FOUND ON SCREEN");
                //Time.sleep(1000);

                if (Players.getLocal().getAnimation() == -1) {

                    LogHandler.log("Player is idle.");
                    //Time.sleep(1000);

                    /*Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return Players.getLocal().getAnimation() == -1;
                                        }
                                    },
                            Random.nextInt(2000, 2500)
                    );*/

                    LogHandler.log("Opening bank");
                    //Time.sleep(1000);

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
                        //Time.sleep(1000);

                    }else{

                        LogHandler.log("Bank has not been opened.");
                        //Time.sleep(1000);

                    }
                    if(sc()){return -1;}
                    //LogHandler.log("Restart loopp");
                    //Time.sleep(500);

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
                    if(sc()){return -1;}

                }
            } else if (bankBooth != null && !(bankBooth.isOnScreen())) {

                LogHandler.log("Bank FOUND NOT ON SCREEN.");
                //Time.sleep(500);

                if (bankBooth.distance() > 6) {
                    do{
                        LogHandler.log("Bank is further than 6 tiles. Searching for path to bank booth...");
                        //Time.sleep(500);

                        Path pathToBooth = Walking.findPath(bankBooth);
                        if(sc()){return -1;}
                        if (pathToBooth != null) {

                            LogHandler.log("Path Found. Begin walking.");
                            //Time.sleep(250);

                            pathToBooth.traverse();
                            if(sc()){return -1;}
                            Time.sleepUntil(new Condition() {
                                                @Override
                                                public boolean check() {
                                                    return bankBooth.distance() < 6 || bankBooth.isOnScreen();
                                                }
                                            }, Random.nextInt(2800, 3000)
                            );
                            if(sc()){return -1;}

                            if(bankBooth.distance() < 6){

                                LogHandler.log("Distance is less tan 6 tiles.");
                                //Time.sleep(750);

                            }else if(bankBooth.isOnScreen()){

                                LogHandler.log("Bank booth is on screen.");
                                //Time.sleep(1000);

                            }else{

                                LogHandler.log("Bank booth is still too far and not on screen.");
                                //Time.sleep(1000);

                            }
                        }
                    }while(bankBooth.distance() > 6);

                    LogHandler.log("Restart loop");
                    //Time.sleep(500);

                    return 120;

                } else if (bankBooth.distance() <= 6) {

                    LogHandler.log("Distance to bank booth is less than 6 tiles, turning camera.");
                    //Time.sleep(250);

                    Camera.turnTo(bankBooth);
                    Time.sleep(1200, 1600);
                    if(sc()){return -1;}

                    LogHandler.log("Searching for path...");
                    //Time.sleep(500);

                    Path pathToBooth = Walking.findPath(bankBooth);
                    if (pathToBooth != null) {

                        LogHandler.log("Path FOUND. Beginning walk");
                        //Time.sleep(250);

                        if(sc()){return -1;}

                        pathToBooth.traverse();/***********************/

                        Time.sleep(2000);

                        if(sc()){return -1;}

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

                        if(sc()){return -1;}

                        if(Bank.isOpen()){

                            LogHandler.log("Bank screen is now OPEN.");
                            //Time.sleep(1000);

                        }else{

                            LogHandler.log("Bank has not been opened.");
                            //Time.sleep(1000);

                        }
                    }
                } else if (bankBooth == null) {

                    LogHandler.log("Could not find bank booth.");
                    if(sc()){return -1;}
                }
            }
        }
        else{
            LogHandler.log("Apparently the bank screen is neither open or closed.....");
            //Time.sleep(1000);
        }

            LogHandler.log("Exiting main loop");
        return -1;
    }

    @Override
    public void onRepaint(Graphics g) {

        mt.draw(g);

    }
}
