package weblogic.messaging.saf.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import weblogic.messaging.saf.common.SAFDebug;

public final class SAFAgentFactoryInternal {
   private final List agentList = new ArrayList();
   private final Random random = new Random();
   private int agentIndx = -2;

   public synchronized void addAgent(AgentImpl var1) {
      if (!this.agentList.contains(var1)) {
         this.agentList.add(var1);
      }

   }

   public synchronized void removeAgent(AgentImpl var1) {
      int var2 = this.agentList.indexOf(var1);
      if (var2 != -1) {
         this.agentList.remove(var1);
         if (this.agentIndx >= var2) {
            --this.agentIndx;
         }

      }
   }

   public AgentImpl getAgentImpl(String var1) {
      AgentImpl var2 = null;
      synchronized(this) {
         Object var4;
         if (var1 != null) {
            var4 = new ArrayList();
            Iterator var5 = this.agentList.iterator();

            while(var5.hasNext()) {
               Object var6 = var5.next();
               AgentImpl var7 = (AgentImpl)var6;
               if (var7.getStoreName().equals(var1)) {
                  ((List)var4).add(var7);
               }
            }
         } else {
            var4 = this.agentList;
         }

         int var10 = ((List)var4).size();
         if (var10 == 0) {
            return null;
         }

         if (this.agentIndx == -2) {
            this.agentIndx = this.random.nextInt(var10);
         } else {
            ++this.agentIndx;
         }

         this.agentIndx %= var10;
         if (this.agentIndx > var10 - 1) {
            this.agentIndx = var10 - 1;
         }

         var2 = (AgentImpl)((List)var4).get(this.agentIndx);
      }

      if (SAFDebug.SAFSendingAgent.isDebugEnabled() && SAFDebug.SAFReceivingAgent.isDebugEnabled()) {
         SAFDebug.SAFSendingAgent.debug("SAFAgentFactory.getSAFAgent = " + var2);
      }

      return var2;
   }

   synchronized boolean haveSendingAgentAvailable() {
      return this.agentList.size() != 0;
   }
}
