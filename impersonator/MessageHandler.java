/*
 * Copyright (C) 2014 Rajdeep Das <rajdeepd@iitk.ac.in>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package impersonator;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Rajdeep Das <rajdeepd@iitk.ac.in>
 */
public class MessageHandler {
    
    public KnowledgeBase knowledgeBase;
    
    private Map<String, Integer> sequenceCounts;
    private String userName;
    
    public MessageHandler(String userName) {
        this.userName=userName;
        this.knowledgeBase=new KnowledgeBase(userName);
        this.sequenceCounts=new HashMap<>();
    }
    
    public String generateReply(String participant, String message) {
        
        if(this.sequenceCounts.containsKey(participant)) {
            this.sequenceCounts.put(participant, this.sequenceCounts.get(participant)+1);
        } else this.sequenceCounts.put(participant, 0);
        
        String reply=this.knowledgeBase.getReply(participant,message, this.sequenceCounts.get(participant));
        
        return reply;
    }
    
}
