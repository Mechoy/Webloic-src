package weblogic.management.context;

import java.io.IOException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.kernel.KernelStatus;
import weblogic.workarea.NoWorkContextException;
import weblogic.workarea.PrimitiveContextFactory;
import weblogic.workarea.PropertyReadOnlyException;
import weblogic.workarea.SerializableWorkContext;
import weblogic.workarea.WorkContext;
import weblogic.workarea.WorkContextHelper;
import weblogic.workarea.WorkContextMap;

public class JMXContextHelper {
   private static final DebugLogger DEBUG_LOGGER = DebugLogger.getDebugLogger("DebugJMXContext");
   private static final JMXContextAccess DEFAULT_JMXCONTEXTACCESS = new DefaultJMXContextAccessImpl();
   private static JMXContextAccess SINGLETON;

   public static JMXContext getJMXContext(boolean var0) {
      return SINGLETON.getJMXContext(var0);
   }

   public static void putJMXContext(JMXContext var0) {
      SINGLETON.putJMXContext(var0);
   }

   public static void removeJMXContext() {
      SINGLETON.removeJMXContext();
   }

   public static synchronized void setJMXContextAccess(JMXContextAccess var0) {
      if (KernelStatus.isServer() && SINGLETON == DEFAULT_JMXCONTEXTACCESS && var0 != null) {
         SINGLETON = var0;
      }
   }

   static {
      SINGLETON = DEFAULT_JMXCONTEXTACCESS;
   }

   private static class DefaultJMXContextAccessImpl implements JMXContextAccess {
      private DefaultJMXContextAccessImpl() {
      }

      public JMXContext getJMXContext(boolean var1) {
         WorkContextMap var2 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
         SerializableWorkContext var3 = (SerializableWorkContext)var2.get("weblogic.management.JMXContext");
         Object var4 = null;
         if (var3 != null) {
            var4 = (JMXContext)var3.get();
         }

         if (var1 && var4 == null) {
            var4 = new JMXContextImpl();
         }

         return (JMXContext)var4;
      }

      public void putJMXContext(JMXContext var1) {
         WorkContextMap var2 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();

         try {
            var2.put("weblogic.management.JMXContext", PrimitiveContextFactory.create(var1));
         } catch (PropertyReadOnlyException var4) {
            if (JMXContextHelper.DEBUG_LOGGER.isDebugEnabled()) {
               JMXContextHelper.DEBUG_LOGGER.debug("DefaultJMXContextAccessImpl.getJMXContext(): WorkContext property is read-only: " + var4.getStackTrace());
            }
         } catch (IOException var5) {
            if (JMXContextHelper.DEBUG_LOGGER.isDebugEnabled()) {
               JMXContextHelper.DEBUG_LOGGER.debug("DefaultJMXContextAccessImpl.getJMXContext(): IOException: " + var5.getStackTrace());
            }
         }

      }

      public void removeJMXContext() {
         try {
            WorkContextMap var1 = WorkContextHelper.getWorkContextHelper().getWorkContextMap();
            WorkContext var2 = var1.get("weblogic.management.JMXContext");
            if (var2 != null) {
               var1.remove("weblogic.management.JMXContext");
            }
         } catch (NoWorkContextException var3) {
            if (JMXContextHelper.DEBUG_LOGGER.isDebugEnabled()) {
               JMXContextHelper.DEBUG_LOGGER.debug("DefaultJMXContextAccessImpl.removeJMXContext(): No WorkContext is available: " + var3.getMessage());
            }
         } catch (PropertyReadOnlyException var4) {
            if (JMXContextHelper.DEBUG_LOGGER.isDebugEnabled()) {
               JMXContextHelper.DEBUG_LOGGER.debug("DefaultJMXContextAccessImpl.removeJMXContext(): WorkContext property is read-only: " + var4.getMessage());
            }
         }

      }

      // $FF: synthetic method
      DefaultJMXContextAccessImpl(Object var1) {
         this();
      }
   }

   public interface JMXContextAccess {
      JMXContext getJMXContext(boolean var1);

      void putJMXContext(JMXContext var1);

      void removeJMXContext();
   }
}
