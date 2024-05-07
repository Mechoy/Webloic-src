package weblogic.jms.saf;

import java.util.HashMap;
import java.util.Map;
import weblogic.jms.common.DestinationImpl;
import weblogic.jms.common.DestinationImplObserver;
import weblogic.jms.common.DistributedDestinationImpl;
import weblogic.jms.common.JMSDebug;
import weblogic.jms.common.MessageImpl;
import weblogic.jms.common.WrappedDestinationImpl;
import weblogic.jms.dd.DDHandler;
import weblogic.jms.dd.DDManager;
import weblogic.jms.forwarder.ReplyHandler;

public class SAFOutgoingReplyHandler implements ReplyHandler, DestinationImplObserver {
   private String replyToSAFContextName;
   private static Map destinationTable = new HashMap();

   public static void init() {
      WrappedDestinationImpl.setObserver(new SAFOutgoingReplyHandler((String)null));
   }

   public SAFOutgoingReplyHandler(String var1) {
      this.replyToSAFContextName = var1;
   }

   public void process(MessageImpl var1) {
      DestinationImpl var2 = (DestinationImpl)var1.getJMSReplyTo();
      if (var2 != null) {
         String var3 = null;
         if (var2 instanceof DistributedDestinationImpl) {
            DDHandler var4 = DDManager.findDDHandlerByDDName(var2.getName());
            if (var4 != null) {
               var3 = var4.getJNDIName();
            }
         } else {
            synchronized(destinationTable) {
               var3 = (String)destinationTable.get(makeName(var2));
            }
         }

         if (this.replyToSAFContextName != null) {
            if (var3 != null) {
               var2.setReferenceName(this.replyToSAFContextName + "@@" + var3);
               if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
                  JMSDebug.JMSBackEnd.debug("Setting reference name to " + var2.getReferenceName());
               }
            } else if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
               JMSDebug.JMSBackEnd.debug("Cannot find reference name for " + var2.getName());
            }

         }
      }
   }

   private static String makeName(DestinationImpl var0) {
      return var0.getApplicationName() + "!" + var0.getModuleName() + "!" + var0.getName();
   }

   public void setReplyToSAFRemoteContextName(String var1) {
      this.replyToSAFContextName = var1;
   }

   public void newDestination(String var1, DestinationImpl var2) {
      synchronized(destinationTable) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Adding " + var1 + " mapping to " + makeName(var2) + " to table");
         }

         destinationTable.put(makeName(var2), var1);
      }
   }

   public void removeDestination(String var1, DestinationImpl var2) {
      synchronized(destinationTable) {
         if (JMSDebug.JMSBackEnd.isDebugEnabled()) {
            JMSDebug.JMSBackEnd.debug("Removing " + makeName(var2) + " from table");
         }

         destinationTable.remove(makeName(var2));
      }
   }
}
