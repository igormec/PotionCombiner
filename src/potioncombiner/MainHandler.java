package potioncombiner;

//import org.tbot.client.GameObject;
import org.tbot.internal.AbstractScript;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Bank;
import org.tbot.methods.GameObjects;
import org.tbot.methods.Random;
import org.tbot.methods.Time;
import org.tbot.methods.tabs.Inventory;
import org.tbot.methods.walking.Path;
import org.tbot.methods.walking.Walking;
import org.tbot.util.Condition;
import org.tbot.wrappers.GameObject;

/**
 * Created by Igor on 010. Apr 10, 15.
 */
public class MainHandler extends AbstractScript {

    public boolean onStart(){
        LogHandler.log("Potion Combiner started");
        return true;
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
            String pot3 = "Energy potion(3)";
            if(Bank.contains(pot3)) {         //IF Energy Potion(3) Found
                Bank.withdraw(pot3, 28);      //Withdraw All
                Bank.close();                 //Close bank screen
            }else if(!Bank.contains(pot3)){
                LogHandler.log("No energy potions, stopping script.");  //PRINT "No energy potions"
                return -1;//STOP SCRIPT
            }else{
                LogHandler.log("Unknown error, stopping script.");  //PRINT "No energy potions"
                return -1;//STOP SCRIPT


        }
        else if(!(Bank.isOpen())){
            final GameObject bankBooth = GameObjects.getNearest("Bank booth");
            if(bankBooth.distance() > 8){
                Path pathToBooth = Walking.findPath(bankBooth);
                if(pathToBooth != null){
                    pathToBooth.traverse();
                    Time.sleepUntil(new Condition() {
                                        @Override
                                        public boolean check() {
                                            return bankBooth.distance() < 8 || bankBooth.isOnScreen();
                                        }
                                    },
                            Random.nextInt(900, 1100)
                    );
                }
            }
        }





        return 100;
    }
}
