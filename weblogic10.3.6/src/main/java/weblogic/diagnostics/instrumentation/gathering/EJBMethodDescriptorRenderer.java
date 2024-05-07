package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.ejb.container.internal.MethodDescriptor;

public class EJBMethodDescriptorRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 != null && var1 instanceof MethodDescriptor) {
         MethodDescriptor var2 = (MethodDescriptor)var1;
         return new EJBEventInfoImpl(var2.getApplicationName(), var2.getEjbComponentName(), var2.getEjbName(), var2.getMethodName());
      } else {
         return null;
      }
   }
}
