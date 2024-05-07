package weblogic.j2ee;

import java.util.EmptyStackException;
import weblogic.connector.external.TrackableConnection;
import weblogic.ejb.spi.BeanInfo;
import weblogic.ejb.spi.SessionBeanInfo;
import weblogic.kernel.ThreadLocalStack;
import weblogic.utils.Debug;
import weblogic.utils.DebugCategory;

public final class MethodInvocationHelper {
   private static final DebugCategory debug = Debug.getCategory("weblogic.j2ee.MethodInvocation.debug");
   private static final DebugCategory verbose = Debug.getCategory("weblogic.j2ee.MethodInvocation.verbose");
   private static final ThreadLocalStack threadMethodStorage = new ThreadLocalStack(true);

   public static void pushConnectionObject(TrackableConnection var0) {
      if (threadMethodStorage.peek() != null) {
         pushObject(var0);
      }
   }

   public static void pushMethodObject(BeanInfo var0) {
      if (var0 == null || var0.getIsResourceRef()) {
         Object var1 = new Object();
         pushObject(var1);
      }
   }

   private static void pushObject(Object var0) {
      if (verbose.isEnabled()) {
         Debug.say("pushMethodObject to push: '" + var0.toString() + "' " + "', " + " currentMethod is: '" + getCurrentObject() + "' " + "' ");
      }

      threadMethodStorage.push(var0);
   }

   public static boolean popMethodObject(BeanInfo var0) {
      if (var0 != null && !var0.getIsResourceRef()) {
         return false;
      } else {
         boolean var1 = false;
         boolean var2 = var0 instanceof SessionBeanInfo && !((SessionBeanInfo)var0).isStateful();

         for(Object var3 = popObject(); var3 instanceof TrackableConnection; var3 = popObject()) {
            TrackableConnection var4 = (TrackableConnection)var3;
            if (var2 && var4.isLocalTransactionInProgress()) {
               var1 = true;
            }

            var4.connectionClosed();
         }

         return var1;
      }
   }

   private static Object popObject() {
      if (verbose.isEnabled()) {
         Debug.say("popObject,  before pop is: '" + getCurrentObject() + "'");
      }

      Object var0 = null;

      try {
         var0 = threadMethodStorage.pop();
      } catch (EmptyStackException var2) {
      }

      if (verbose.isEnabled()) {
         Debug.say("popObject,  after  pop is: '" + getCurrentObject() + "'");
      }

      return var0;
   }

   public static Object getCurrentObject() {
      return threadMethodStorage.get();
   }
}
