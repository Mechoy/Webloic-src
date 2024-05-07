package weblogic.wsee.jws.container;

import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;
import javax.xml.rpc.handler.GenericHandler;
import javax.xml.rpc.handler.MessageContext;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.jws.conversation.ConversationTimeout;
import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.jws.conversation.StoreConfig;
import weblogic.wsee.jws.conversation.StoreException;
import weblogic.wsee.jws.conversation.StoreManager;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.Guid;

public class ConversationLifeCycleHandler extends GenericHandler implements WLHandler {
   public static final String HANDLER_NAME = "CONVERSATION_LIFE_CYCLE_HANDLER";

   public boolean handleRequest(MessageContext var1) {
      ConversationalContainer var2 = getConversationalContainer(var1);
      if (var2 != null) {
         var2.resetIdleTime();
         if (var2.getStartUser() == null) {
            var2.setStartUser(getCallerName(var2));
         }

         if (var2.isSinglePrincipal() && !getCallerName(var2).equals(var2.getStartUser())) {
            throw new IllegalStateException("Service may only be called by the user who started the conversation.\nstarted = " + var2.getStartUser() + " caller = " + getCallerName(var2));
         }
      }

      return true;
   }

   private static ConversationalContainer getConversationalContainer(MessageContext var0) {
      Container var1 = getContainer(var0);
      return var1 instanceof ConversationalContainer ? (ConversationalContainer)var1 : null;
   }

   private static Container getContainer(MessageContext var0) {
      Container var1 = ContainerFactory.getContainer(var0);
      if (var1 == null) {
         throw new IllegalStateException("Container not found in message context");
      } else {
         return var1;
      }
   }

   public boolean handleResponse(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      ConversationalContainer var3 = getConversationalContainer(var2);
      if (var3 != null) {
         ConversationPhase var4 = (ConversationPhase)var2.getProperty("weblogic.wsee.conversation.ConversationPhase");
         if (var4 == null) {
            var4 = ConversationPhase.CONTINUE;
         }

         try {
            if (var4.equals(ConversationPhase.FINISH) || var3.isFinished()) {
               var3.finish();
               var3.destroy();
            }

            if (!var4.equals(ConversationPhase.FINISH) && !var3.isFinished()) {
               preSchedule(var3, var2);
            }

            updateStore(getStoreConfig(var2), var3, var4);
            if (!var4.equals(ConversationPhase.FINISH) && !var3.isFinished()) {
               schedule(var3);
            }
         } catch (Exception var6) {
            throw new InvokeException("Error updating conversational state: " + var6.toString(), var6);
         }
      }

      return true;
   }

   private static void preSchedule(ConversationalContainer var0, WlMessageContext var1) {
      ArrayList var2 = new ArrayList();
      if (var0.getMaxAgeTime() > 0L) {
         ConversationTimeout var3 = new ConversationTimeout(ContainerEvent.TYPE.EVENT_AGE_TIMEOUT, var0.getURI(), var0.getId(), getStoreConfig(var1), var0.getMaxAgeTime());
         String var4 = Guid.generateGuid();
         ConversationTimeoutListener var5 = new ConversationTimeoutListener(var4, var3);
         var2.add(var5);
      }

      if (var0.getMaxIdleSeconds() > 0L) {
         long var8 = var0.getLastReqTime() + var0.getMaxIdleSeconds() * 1000L;
         ConversationTimeout var9 = new ConversationTimeout(ContainerEvent.TYPE.EVENT_IDLE_TIMEOUT, var0.getURI(), var0.getId(), getStoreConfig(var1), var8);
         String var6 = Guid.generateGuid();
         ConversationTimeoutListener var7 = new ConversationTimeoutListener(var6, var9);
         var2.add(var7);
      }

      if (var2.size() > 0) {
         var0.setTimeoutListeners(var2);
      }

   }

   private static void schedule(ConversationalContainer var0) {
      if (var0.getTimeoutListeners() != null) {
         Iterator var1 = var0.getTimeoutListeners().iterator();

         while(var1.hasNext()) {
            ConversationTimeoutListener var2 = (ConversationTimeoutListener)var1.next();
            var2.schedule();
         }
      }

   }

   public boolean handleClosure(MessageContext var1) {
      return this.handleResponse(var1);
   }

   private static String getCallerName(ConversationalContainer var0) {
      return var0.getCallerPrincipal() == null ? "null" : var0.getCallerPrincipal().getName();
   }

   private static StoreConfig getStoreConfig(MessageContext var0) {
      return (StoreConfig)var0.getProperty("weblogic.wsee.conversation.StoreConfig");
   }

   private static void updateStore(StoreConfig var0, ConversationalContainer var1, ConversationPhase var2) throws StoreException {
      Store var3 = StoreManager.getStore(var0);
      if (var2.equals(ConversationPhase.START)) {
         if (!var1.isFinished()) {
            var3.insert(var1);
         }
      } else if (!var2.equals(ConversationPhase.FINISH) && !var1.isFinished()) {
         var3.update(var1);
      } else {
         var3.delete(var1.getId());
      }

   }

   public QName[] getHeaders() {
      return new QName[0];
   }
}
