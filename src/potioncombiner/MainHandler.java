package potioncombiner;

//import org.tbot.client.GameObject;
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

/**
 * Created by Igor on 010. Apr 10, 15.
 */

@Manifest(authors = "Igor", name = "Potion Combiner", category = ScriptCategory.HERBLORE)
public class MainHandler extends AbstractScript {

    public boolean onStart(){
        LogHandler.log("Potion Combiner started");
        return true;
    }

    public void withdrawPots(String potType, int amount){
        //WITHDRAW CODE

        if(Bank.contains(potType)) {            //IF Energy Potion(3) Found
            Bank.withdraw(potType, amount);     //Withdraw All
            Bank.close();                       //Close bank screen
        }else if(!Bank.contains(potType)){
            LogHandler.log("No energy potions, stopping script.");  //PRINT "No energy potions"
            //return -1;//STOP SCRIPT
        }else {
            LogHandler.log("Unknown error, stopping script.");  //PRINT "No energy potions"
            //return -1;//STOP SCRIPT
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
                Time.sleep(550,700);
            }
        }
    }



    @Override
    public int loop() {

        if (Bank.isOpen()) {
            //Deposit All
            if(Inventory.getEmptySlots()<28){
                if(Bank.depositAll()){
                    Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return Inventory.getEmptySlots() == 28;
                                        }
                                    },
                            Random.nextInt(850,1000)
                    );
                }
            }

            withdrawPots("Energy potion(3)",28);
            combinePots();





        }
        else if(!(Bank.isOpen())){
            final GameObject bankBooth = GameObjects.getNearest("Bank booth");

            if (bankBooth != null && bankBooth.isOnScreen()) {
                if (Players.getLocal().getAnimation() == -1) {
                    if (bankBooth.interact("Bank")) {
                        Time.sleepUntil(new Condition() {
                                            @Override
                                            public boolean check() {
                                                return Bank.isOpen();
                                            }
                                        },
                                Random.nextInt(1200, 1600)
                        );
                    }
                }
            }
            else if (bankBooth != null && !(bankBooth.isOnScreen())) {
                if (bankBooth.distance() > 6) {
                    Path pathToBooth = Walking.findPath(bankBooth);
                    if (pathToBooth != null) {
                        pathToBooth.traverse();
                        Time.sleepUntil(new Condition() {
                                            @Override
                                            public boolean check() {
                                                return bankBooth.distance() < 6 || bankBooth.isOnScreen();
                                            }
                                        },
                                Random.nextInt(900, 1150)
                        );
                    }
                } else if (bankBooth.distance() <= 6) {
                    Camera.turnTo(bankBooth);
                    Time.sleep(300, 420);
                }
            }
            else if (bankBooth == null) {

                LogHandler.log("bankBooth is null");
            }
        }





        return 100;
    }
}
