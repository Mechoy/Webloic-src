package weblogic.management.mbeanservers.compatibility.internal;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.management.Attribute;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.management.RuntimeMBeanException;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.ConfigurationException;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;

public class ReparentingInterceptor extends WLSMBeanServerInterceptorBase {
   private static DebugLogger debug = DebugLogger.getDebugLogger("CompatabilityMBeanServer");
   private static final String[] CREATOR_SIGNATURE = new String[]{"java.lang.String", "javax.management.ObjectName"};
   private static final String[] DESTROYER_SIGNATURE = new String[]{"javax.management.ObjectName"};
   private static Set BEAN_TYPE = new HashSet(Arrays.asList("ForeignJMSServer", "JMSSessionPool"));
   private static Set ADDERS = new HashSet(Arrays.asList("addConnectionFactory", "addDestination", "addConnectionConsumer"));
   private static Set REMOVERS = new HashSet(Arrays.asList("removeConnectionFactory", "removeDestination", "removeConnectionConsumer"));

   public void setAttribute(ObjectName var1, Attribute var2) throws InstanceNotFoundException, AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException, IOException {
      if (var2.getName().equals("Parent")) {
         if (debug.isDebugEnabled()) {
            debug.debug("Reparenting for object " + var1);
         }

         ConfigurationException var5;
         try {
            String var3 = var1.getKeyProperty("Type");
            String var12 = var1.getKeyProperty("Name");
            String var13 = "create" + var3;
            String var6 = "destroy" + var3;
            ObjectName var7 = (ObjectName)super.getAttribute(var1, "Parent");
            ObjectName var8 = (ObjectName)var2.getValue();
            super.invoke(var8, var13, new Object[]{var12, var1}, CREATOR_SIGNATURE);
            super.invoke(var7, var6, new Object[]{var1}, DESTROYER_SIGNATURE);
         } catch (MBeanException var9) {
            Exception var11 = var9.getTargetException();
            if (var11 instanceof ConfigurationException) {
               throw var9;
            }

            var5 = new ConfigurationException(var11);
            throw new MBeanException(var5);
         } catch (RuntimeMBeanException var10) {
            RuntimeException var4 = var10.getTargetException();
            if (var4 instanceof ConfigurationException) {
               throw var10;
            }

            var5 = new ConfigurationException(var4);
            throw new MBeanException(var5);
         }
      } else {
         super.setAttribute(var1, var2);
      }

   }

   public Object invoke(ObjectName var1, String var2, Object[] var3, String[] var4) throws InstanceNotFoundException, MBeanException, ReflectionException, IOException {
      String var5 = var1.getKeyProperty("Type");
      if (var5 != null && var5.endsWith("Config")) {
         var5 = var5.substring(0, var5.length() - 6);
      }

      Boolean var6 = Boolean.TRUE;
      if (BEAN_TYPE.contains(var5) && var3 != null & var3.length == 1 && var3[0] instanceof ObjectName) {
         ObjectName var7 = (ObjectName)var3[0];
         String var8 = var7.getKeyProperty("Type");
         if (var8.endsWith("Config")) {
            var8 = var8.substring(0, var8.length() - 6);
         }

         String var9 = var7.getKeyProperty("Name");
         String var10 = "create" + var8;
         String var11 = "destroy" + var8;
         if (ADDERS.contains(var2)) {
            try {
               if (debug.isDebugEnabled()) {
                  debug.debug("Adding for object " + var1 + " method " + var2);
               }

               super.invoke(var1, var10, new Object[]{var9, var7}, CREATOR_SIGNATURE);
               ObjectName var12 = (ObjectName)super.getAttribute(var1, "Parent");
               super.invoke(var12, var11, new Object[]{var7}, DESTROYER_SIGNATURE);
               return var6;
            } catch (AttributeNotFoundException var13) {
               throw new Error(var13);
            }
         }

         if (REMOVERS.contains(var2)) {
            if (debug.isDebugEnabled()) {
               debug.debug("Destroying for object " + var1 + " method " + var2);
            }

            super.invoke(var1, var11, new Object[]{var9, var7}, CREATOR_SIGNATURE);
            return var6;
         }
      }

      return super.invoke(var1, var2, var3, var4);
   }
}
