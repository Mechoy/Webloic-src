package weblogic.wsee.buffer2.api.wls;

import com.sun.xml.ws.api.pipe.Fiber;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.logging.Level;
import weblogic.jws.jaxws.client.async.FiberBox;
import weblogic.security.acl.internal.AuthenticatedSubject;
import weblogic.security.service.PrivilegedActions;
import weblogic.wsee.buffer2.api.common.BufferingDispatch;
import weblogic.wsee.buffer2.api.common.BufferingFeature;
import weblogic.wsee.jaxws.persistence.PersistentContext;
import weblogic.wsee.jaxws.persistence.PersistentMessage;
import weblogic.wsee.jaxws.persistence.PersistentMessageFactory;
import weblogic.wsee.jaxws.persistence.PersistentObject;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;

public final class BufferingDispatch_WLS extends BufferingDispatch {
   private static String WLS_MDB_CLASSNAME = "weblogic.wsee.buffer2.internal.wls.BufferingMDB_WLS";

   public String getMDBClassName() {
      return WLS_MDB_CLASSNAME;
   }

   protected void executeDeliverPlatform(final Object var1, final boolean var2, final BufferingFeature var3) {
      try {
         AuthenticatedSubject var4 = (AuthenticatedSubject)AccessController.doPrivileged(PrivilegedActions.getKernelIdentityAction());
         PrivilegedExceptionAction var5 = new PrivilegedExceptionAction<BufferingDispatch.NullObject>() {
            public BufferingDispatch.NullObject run() throws Exception {
               if (var1 instanceof PersistentMessage) {
                  PersistentMessage var1x = (PersistentMessage)var1;
                  BufferingDispatch_WLS.this.deliver(var1x, var2, var3);
               } else {
                  PersistentObject var5 = (PersistentObject)var1;
                  DestinationMessageInfo var2x = (DestinationMessageInfo)var5.getObj();
                  FiberBox var3x = var2x.getSuspendedFiber();
                  if (BufferingDispatch_WLS.LOGGER.isLoggable(Level.FINE)) {
                     BufferingDispatch_WLS.LOGGER.fine("Got suspended fiber msgInfo from buffered message; " + var3x);
                  }

                  if (var3x != null) {
                     Fiber var4 = var3x.get();
                     if (BufferingDispatch_WLS.LOGGER.isLoggable(Level.FINE)) {
                        BufferingDispatch_WLS.LOGGER.fine("Resuming suspended fiber from buffered message; " + var4);
                     }

                     var4.resume(var4.getPacket(), true);
                  }
               }

               return null;
            }
         };
         PersistentContext var6;
         if (var1 instanceof PersistentMessage) {
            var6 = ((PersistentMessage)var1).getContext();
         } else {
            var6 = ((PersistentObject)var1).getContext();
         }

         PersistentMessageFactory.getInstance().runActionInContext(var6, var4, var5);
      } catch (Throwable var7) {
         if (var7 instanceof RuntimeException) {
            throw (RuntimeException)var7;
         } else {
            throw new RuntimeException(var7);
         }
      }
   }
}
