package weblogic.jms.safclient.agent;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import javax.jms.JMSException;
import org.w3c.dom.Document;
import weblogic.jms.safclient.MessageMigrator;
import weblogic.jms.safclient.admin.ConfigurationUtils;
import weblogic.jms.safclient.agent.internal.Agent;
import weblogic.jms.safclient.agent.internal.RemoteContext;
import weblogic.jms.safclient.jndi.ContextImpl;

public final class AgentManager {
   private static final String SEPARATOR = "!";
   private Agent agent;
   private HashMap remoteContextMap = new HashMap();
   private HashMap errorHandlerMap = new HashMap();
   private ContextImpl context;
   private static int managerSequenceNumber;
   private static Object managerSequenceNumberLock = new Object();
   private static int agentNumber;
   private static Object numberLock = new Object();
   private static final String AGENT_COMPONENT = "ClientSAFAgent";

   public static String constructDestinationName(String var0, String var1) {
      return var0 + "!" + var1;
   }

   private static String getAgentName() {
      if (MessageMigrator.revertBugFix) {
         int var0;
         synchronized(numberLock) {
            var0 = agentNumber++;
         }

         return "ClientSAFAgent" + var0;
      } else {
         return "ClientSAFAgent0";
      }
   }

   public AgentManager(Document var1, ContextImpl var2, char[] var3, File var4) throws JMSException {
      boolean var5 = true;

      try {
         this.context = var2;
         String var6 = getAgentName();
         this.agent = new Agent(var6, var4);
         ConfigurationUtils.doAgent(var1, this.agent);
         ConfigurationUtils.doRemoteContexts(var1, this.agent, this.remoteContextMap, var3);
         ConfigurationUtils.doErrorHandlers(var1, this.errorHandlerMap);
         this.agent.initialize();
         ConfigurationUtils.doImportedDestinationGroup(var1, this.remoteContextMap, this.errorHandlerMap, this.agent, this.context);
         ConfigurationUtils.resolveErrorDestinations(this.errorHandlerMap, this.context);
         var5 = false;
      } finally {
         if (var5) {
            try {
               this.shutdown();
            } catch (Throwable var12) {
            }
         }

      }

   }

   public void shutdown() throws JMSException {
      synchronized(this.remoteContextMap) {
         Iterator var2 = this.remoteContextMap.values().iterator();

         while(true) {
            if (!var2.hasNext()) {
               this.remoteContextMap.clear();
               break;
            }

            RemoteContext var3 = (RemoteContext)var2.next();
            var3.shutdown();
         }
      }

      synchronized(this.errorHandlerMap) {
         this.errorHandlerMap.clear();
      }

      if (this.agent != null) {
         this.agent.shutdown();
         this.agent = null;
      }

   }

   public DestinationImpl getDestination(String var1, String var2) {
      return this.context.getDestination(var1, var2);
   }

   public static String getManagerSequence() {
      int var0;
      synchronized(managerSequenceNumberLock) {
         var0 = managerSequenceNumber++;
      }

      return "." + var0;
   }
}
