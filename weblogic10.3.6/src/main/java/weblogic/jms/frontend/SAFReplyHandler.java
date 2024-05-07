package weblogic.jms.frontend;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDManager;
import weblogic.jms.dd.DDStatusListener;

public final class SAFReplyHandler implements DDStatusListener {
   private static Map idMap;
   private static SAFReplyHandler singleton = new SAFReplyHandler();

   public static void process(MessageImpl var0) {
      DestinationImpl var1 = (DestinationImpl)var0.getJMSReplyTo();
      if (var1 != null) {
         String var2 = var1.getReferenceName();
         if (var2 != null) {
            var1.setReferenceName((String)null);
            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("Reply to with a reference name of " + var2);
            }

            synchronized(singleton) {
               if (idMap == null) {
                  init();
               }

               DDHandler var4 = (DDHandler)idMap.get(var2);
               if (var4 == null) {
                  if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                     JMSDebug.JMSFrontEnd.debug(var2 + ": not found");
                  }

               } else {
                  if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                     JMSDebug.JMSFrontEnd.debug(var2 + " found as destination: " + var4.getName());
                  }

                  var0.setJMSReplyTo(var4.getDDImpl());
               }
            }
         }
      }
   }

   private static synchronized void init() {
      idMap = new HashMap();
      Iterator var0 = DDManager.getAllDDHandlers();

      while(var0.hasNext()) {
         DDHandler var1 = (DDHandler)var0.next();
         if (var1.getReferenceName() != null) {
            if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
               JMSDebug.JMSFrontEnd.debug("Adding " + var1.getName() + " to table with referenceName " + var1.getReferenceName());
            }

            idMap.put(var1.getReferenceName(), var1);
         }
      }

      DDHandler.addGeneralStatusListener(singleton, 20);
   }

   public void statusChangeNotification(DDHandler var1, int var2) {
      synchronized(this) {
         Iterator var4 = idMap.values().iterator();

         while(var4.hasNext()) {
            DDHandler var5 = (DDHandler)var4.next();
            if (var5 == var1) {
               var4.remove();
               break;
            }
         }

         if ((var2 & 16) == 0) {
            if (var1.getReferenceName() != null) {
               if (JMSDebug.JMSFrontEnd.isDebugEnabled()) {
                  JMSDebug.JMSFrontEnd.debug("Adding " + var1.getName() + " to table with referenceName " + var1.getReferenceName());
               }

               idMap.put(var1.getReferenceName(), var1);
            }

         }
      }
   }
}
