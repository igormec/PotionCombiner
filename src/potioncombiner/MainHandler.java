package potioncombiner;

import org.tbot.internal.AbstractScript;
import org.tbot.internal.handlers.LogHandler;
import org.tbot.methods.Bank;

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
            //IF Energy Potion(3) Found
                //Withdraw All
                //Close bank screen
            //ELSE
                //PRINT "No energy potions"
                //STOP SCRIPT
        }
        else if(!(Bank.isOpen())){

        }





        return 120;
    }
}
