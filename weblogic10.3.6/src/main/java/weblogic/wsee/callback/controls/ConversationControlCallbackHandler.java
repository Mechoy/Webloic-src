package weblogic.wsee.callback.controls;

import javax.xml.rpc.JAXRPCException;
import javax.xml.rpc.handler.MessageContext;
import weblogic.logging.NonCatalogLogger;
import weblogic.wsee.async.AsyncUtil;
import weblogic.wsee.conversation.ConversationCMPHandler;
import weblogic.wsee.conversation.ConversationPhase;
import weblogic.wsee.conversation.LockManager;
import weblogic.wsee.handler.WLHandler;
import weblogic.wsee.jws.container.ContainerFactory;
import weblogic.wsee.jws.conversation.ConversationState;
import weblogic.wsee.jws.conversation.Store;
import weblogic.wsee.jws.conversation.StoreConfig;
import weblogic.wsee.jws.conversation.StoreException;
import weblogic.wsee.jws.conversation.StoreManager;
import weblogic.wsee.message.WlMessageContext;

public class ConversationControlCallbackHandler extends ConversationCMPHandler implements WLHandler {
   private static final boolean verbose = true;
   NonCatalogLogger _logger = new NonCatalogLogger(ConversationControlCallbackHandler.class.getName());

   public boolean handleRequest(MessageContext var1) {
      WlMessageContext var2 = WlMessageContext.narrow(var1);
      Object var3 = var2.getProperty("weblogic.wsee.callback.controls.ControlCallbackData");
      if (var3 == null) {
         return true;
      } else {
         String var4 = (String)var2.getProperty("weblogic.wsee.conversation.ConversationId");
         if (var4 == null) {
            return true;
         } else {
            try {
               LockManager.Lock var5 = (LockManager.Lock)var1.getProperty("weblogic.wsee.conversation.Lock");
               if (var5 != null) {
                  this._logger.debug("\n\n\n!!!!!! ERROR SOMEONE OBTAINED THE LOCK BEFORE I DID..THIS IS NOT GOOD!\n\n");
               }

               this._logger.debug("---> Obtaining conversation lock for control callbacks: " + var4);
               LockManager.Lock var6 = LockManager.getInstance().lock(var4);
               this._logger.debug("---> Obtained conversation lock for control callbacks: " + var4 + " ... lock=" + var6);
               var1.setProperty("weblogic.wsee.conversation.Lock", var6);
            } catch (InterruptedException var8) {
               throw new JAXRPCException(var8);
            }

            Store var9 = this.getStore(var4, var2);

            try {
               ConversationState var10 = ConversationCMPHandler.loadState(var2, var9, var4);
               var2.setProperty("weblogic.wsee.jws.container", var10);
               var2.setProperty("weblogic.wsee.ejb.altRunAs", var10.getAltAuthenticatedSubject());
               return true;
            } catch (JAXRPCException var7) {
               var7.printStackTrace();
               var1.setProperty("weblogic.wsee.local.invoke.throwable", var7);
               throw var7;
            }
         }
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
      if (var1.getProperty("weblogic.wsee.callback.controls.ControlCallbackData") == null) {
         return true;
      } else {
         boolean var9 = false;

         String var3;
         try {
            var9 = true;
            this.updateStoreIfNeeded(var1);
            var9 = false;
         } catch (StoreException var10) {
            var3 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
            var10.printStackTrace();
            this._logger.debug("Could not save the conversation state ", var10);
            throw new JAXRPCException("Could not save the conversation state " + var3, var10);
         } finally {
            if (var9) {
               LockManager.Lock var5 = (LockManager.Lock)var1.getProperty("weblogic.wsee.conversation.Lock");
               if (var5 != null) {
                  var5.release();
                  String var6 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
                  this._logger.debug("---> Released jws conversation lock for control callbacks: " + var6 + " ... lock=" + var5);
               }

            }
         }

         LockManager.Lock var2 = (LockManager.Lock)var1.getProperty("weblogic.wsee.conversation.Lock");
         if (var2 != null) {
            var2.release();
            var3 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
            this._logger.debug("---> Released jws conversation lock for control callbacks: " + var3 + " ... lock=" + var2);
         }

         return true;
      }
   }

   private void updateStoreIfNeeded(MessageContext var1) throws StoreException {
      String var2 = (String)var1.getProperty("weblogic.wsee.conversation.ConversationId");
      if (var2 == null) {
         this._logger.debug("updateStore(): ConversationId is null must not be conversational!");
      } else {
         this._logger.debug("updateStore():In Conversation! ConversationId is " + var2);
         StoreConfig var3 = (StoreConfig)var1.getProperty("weblogic.wsee.conversation.StoreConfig");
         if (var3 != null) {
            ConversationState var4 = (ConversationState)ContainerFactory.getContainer(var1);
            if (var4 != null) {
               Store var5 = StoreManager.getStore(var3);
               if (this.isConversationFinished(var1)) {
                  this._logger.debug("Deleting conversation state since conversation has finished");
                  var5.delete(var2);
                  LockManager.getInstance().destroy(var2);
               } else {
                  var5.update(var4);
               }
            }
         }

      }
   }

   private boolean isConversationFinished(MessageContext var1) {
      ConversationPhase var2 = (ConversationPhase)var1.getProperty("weblogic.wsee.conversation.ConversationPhase");
      return var2 != null && var2.equals(ConversationPhase.FINISH) || AsyncUtil.getContainer(var1) != null && AsyncUtil.getContainer(var1).isFinished();
   }
}
