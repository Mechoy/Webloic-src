package weblogic.diagnostics.instrumentation.gathering;

import weblogic.diagnostics.instrumentation.ValueRenderer;
import weblogic.ejb.container.internal.InvocationWrapper;
import weblogic.ejb.container.internal.MethodDescriptor;

public class EJBInvocationWrapperRenderer implements ValueRenderer {
   public Object render(Object var1) {
      if (var1 != null && var1 instanceof InvocationWrapper) {
         InvocationWrapper var2 = (InvocationWrapper)var1;
         MethodDescriptor var3 = var2.getMethodDescriptor();
         return var3 == null ? null : new EJBEventInfoImpl(var3.getApplicationName(), var3.getEjbComponentName(), var3.getEjbName(), var3.getMethodName());
      } else {
         return null;
      }
   }
}
