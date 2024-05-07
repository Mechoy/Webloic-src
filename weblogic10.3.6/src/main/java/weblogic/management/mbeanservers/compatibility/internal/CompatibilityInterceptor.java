package weblogic.management.mbeanservers.compatibility.internal;

import java.io.IOException;
import javax.management.AttributeNotFoundException;
import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import weblogic.management.WebLogicObjectName;
import weblogic.management.jmx.mbeanserver.WLSMBeanServerInterceptorBase;

public class CompatibilityInterceptor extends WLSMBeanServerInterceptorBase {
   CompatibilityInterceptor() {
   }

   public Object getAttribute(ObjectName var1, String var2) throws InstanceNotFoundException, AttributeNotFoundException, MBeanException, ReflectionException, IOException {
      if (var2.equals("ObjectName")) {
         if (var1 instanceof WebLogicObjectName) {
            return var1;
         }

         try {
            return new WebLogicObjectName(var1);
         } catch (MalformedObjectNameException var4) {
         }
      }

      return super.getAttribute(var1, var2);
   }
}
