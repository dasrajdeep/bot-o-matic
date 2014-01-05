package impersonator;

import com.google.code.chatterbotapi.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AutoBOT {
    
    private ChatterBotFactory factory;
    private ChatterBot bot;
    private ChatterBotSession botsession;
    
    public AutoBOT() {
        factory = new ChatterBotFactory();
        try {
            bot=factory.create(ChatterBotType.CLEVERBOT);
            botsession = bot.createSession();
        } catch (Exception ex) {
            Logger.getLogger(AutoBOT.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getAutoReply(String message) {
        try {
            return botsession.think(message);
        } catch (Exception ex) {
            Logger.getLogger(AutoBOT.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
            
}

