package weblogic.wsee.wstx.wsc.common;

import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.WebServiceException;
import weblogic.wsee.wstx.wsc.common.types.BaseRegisterResponseType;

public class PendingRequestManager {
   private static final Logger logger = Logger.getLogger(PendingRequestManager.class.getName());
   static ConcurrentHashMap<String, ResponseBox> pendingRequests = new ConcurrentHashMap();

   public static ResponseBox reqisterRequest(String var0) {
      ResponseBox var1 = new ResponseBox();
      pendingRequests.put(var0, var1);
      return var1;
   }

   public static void removeRequest(String var0) {
      pendingRequests.remove(var0);
   }

   public static void registryReponse(String var0, BaseRegisterResponseType var1) {
      if (logger.isLoggable(Level.FINE)) {
         logger.fine("get a repsonse for request:\t" + var0);
      }

      ResponseBox var2 = (ResponseBox)pendingRequests.remove(var0);
      if (var2 != null) {
         var2.put(var1);
      } else if (logger.isLoggable(Level.FINE)) {
         logger.fine("ignore reponse for timed out request:\t" + var0);
      }

   }

   public static class ResponseBox {
      private boolean isSet = false;
      private BaseRegisterResponseType type;

      ResponseBox() {
      }

      public synchronized void put(BaseRegisterResponseType var1) {
         this.type = var1;
         this.isSet = true;
         this.notify();
      }

      public synchronized BaseRegisterResponseType getReponse(long var1) {
         long var3 = System.currentTimeMillis();

         while(!this.isSet) {
            try {
               this.wait(var1);
               long var5 = System.currentTimeMillis();
               var1 -= var5 - var3;
               if (var1 <= 0L) {
                  break;
               }

               var3 = var5;
            } catch (InterruptedException var7) {
               throw new WebServiceException(var7);
            }
         }

         return this.type;
      }
   }
}
