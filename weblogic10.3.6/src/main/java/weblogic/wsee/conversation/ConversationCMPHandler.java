package weblogic.wsee.conversation;

import java.util.Iterator;
import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.application.utils.ApplicationVersionUtils;
import weblogic.jws.Conversational;
import weblogic.jws.DatabaseStore;
import weblogic.jws.FileStore;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.deploy.VersioningHelper;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.jws.container.Container;
import weblogic.wsee.jws.container.ContainerFactory;
import weblogic.wsee.jws.conversation.ConversationState;
import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.jws.conversation.StoreConfig;
import weblogic.wsee.jws.conversation.StoreException;
import weblogic.wsee.jws.conversation.StoreManager;
import weblogic.wsee.message.WlMessageContext;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.util.Verbose;

public class ConversationCMPHandler extends ConversationHandler implements WLHandler {
   public static final String CONVERSATION_STORE_CONFIG = "weblogic.wsee.conversation.StoreConfig";
   public static final String CONVERSATION_LOCK = "weblogic.wsee.conversation.Lock";
   public static final boolean verbose = Verbose.isVerbose(ConversationCMPHandler.class);

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      ConversationPhase var3 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
      if (var3 != null && var3 != ConversationPhase.NONE) {
         String var4 = this.getConversationId(var2);

         try {
            LockManager.Lock var5 = LockManager.getInstance().lock(var4);
            var1.setProperty("weblogic.wsee.conversation.Lock", var5);
         } catch (InterruptedException var10) {
            throw new JAXRPCException(var10);
         }

         Store var12 = this.getStore(var4, var2);
         if (var3 != ConversationPhase.START) {
            ConversationState var6 = null;

            try {
               var6 = loadState(var2, var12, var4);
            } catch (RuntimeException var11) {
               Iterator var8 = var2.getPropertyNames();

               while(var8.hasNext()) {
                  String var9 = (String)var8.next();
                  Verbose.log((Object)(var9 + " = " + var2.getProperty(var9)));
               }

               Verbose.log((Object)var2.getDispatcher().getWsPort().getEndpoint().getJwsClass().getName());
               Verbose.log((Object)Thread.currentThread().getContextClassLoader());
               throw var11;
            }

            var2.setProperty("weblogic.wsee.jws.container", var6);
            Container var7 = ContainerFactory.getContainer(var1);
            Conversational var13 = (Conversational)var7.getTargetJWS().getClass().getAnnotation(Conversational.class);
            if (var13 != null && var13.runAsStartUser()) {
               var2.setProperty("weblogic.wsee.ejb.altRunAs", var6.getAltAuthenticatedSubject());
               var2.setProperty("weblogic.wsee.wss.subject", var6.getAltAuthenticatedSubject());
            }
         }

         return true;
      } else {
         return true;
      }
   }

   protected Store getStore(String var1, WlMessageContext var2) {
      try {
         StoreConfig var3 = this.getStoreConfig(var2);
         Store var4 = StoreManager.getStore(var3);
         var2.setProperty("weblogic.wsee.conversation.StoreConfig", var3);
         return var4;
      } catch (Throwable var5) {
         throw new JAXRPCException("Could not get Store for conversation with id " + var1, var5);
      }
   }

   private StoreConfig getStoreConfig(WlMessageContext var1) {
      StoreConfig var2 = null;
      Class var3 = var1.getDispatcher().getWsPort().getEndpoint().getJwsClass();
      DatabaseStore var4 = (DatabaseStore)var3.getAnnotation(DatabaseStore.class);
      if (var4 == null) {
         var2 = new StoreConfig("file");
         FileStore var5 = (FileStore)var3.getAnnotation(FileStore.class);
         if (var5 != null && !StringUtil.isEmpty(var5.storeName())) {
            var2.put("storeName", var5.storeName());
         }
      } else {
         var2 = new StoreConfig("database");
      }

      return var2;
   }

   private String getConversationId(WlMessageContext var1) {
      String var2 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");

      assert var2 != null;

      return var2;
   }

   protected static ConversationState loadState(WlMessageContext var0, Store var1, String var2) {
      try {
         ConversationState var3 = var1.readForUpdate(var2);
         if (var3 == null) {
            throw new JAXRPCException("Conversation " + var2 + " not found in store: " + var1);
         } else {
            return var3;
         }
      } catch (StoreException var4) {
         throw new JAXRPCException("Could not load conversation with id " + var2, var4);
      }
   }

   public boolean handleResponse(MessageContext var1) {
      return this.finish(var1);
   }

   public boolean handleClosure(MessageContext var1) {
      return this.finish(var1);
   }

   public boolean handleFault(MessageContext var1) {
      return this.finish(var1);
   }

   private boolean finish(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      ConversationPhase var3 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
      if (var3 != null && var3 != ConversationPhase.NONE) {
         Container var4 = AsyncUtil.getContainer(var1);
         if (var3.equals(ConversationPhase.FINISH) || var4 == null || var4.isFinished()) {
            LockManager.getInstance().destroy(this.getConversationId(WlMessageContext.narrow(var1)));
         }

         LockManager.Lock var5 = (LockManager.Lock)var1.getProperty("weblogic.wsee.conversation.Lock");
         if (var5 != null) {
            var5.release();
         }

         String var6 = ApplicationVersionUtils.getCurrentVersionId();
         if (var6 != null) {
            String var7 = ApplicationVersionUtils.getApplicationName(ApplicationVersionUtils.getCurrentApplicationId());
            updateInstanceCount(var7, (String)var2.getProperty("weblogic.wsee.version.appversion.id"), var3);
         }

         return true;
      } else {
         return true;
      }
   }

   private static void updateInstanceCount(String var0, String var1, ConversationPhase var2) {
      byte var3 = 0;
      if (var2.equals(ConversationPhase.START)) {
         var3 = 1;
      } else if (var2.equals(ConversationPhase.FINISH)) {
         var3 = -1;
      }

      if (var3 != 0) {
         VersioningHelper.updateCount(var0, var1, var3);
      }

   }
}
