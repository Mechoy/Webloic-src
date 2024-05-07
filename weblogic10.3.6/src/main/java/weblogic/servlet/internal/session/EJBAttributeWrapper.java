package weblogic.servlet.internal.session;

import javax.ejb.Handle;
import weblogic.ejb.spi.BusinessHandle;
import weblogic.servlet.internal.AttributeWrapper;

final class EJBAttributeWrapper extends AttributeWrapper {
   private static final long serialVersionUID = -3141160212794756249L;

   public EJBAttributeWrapper(Handle var1) {
      super(var1);
   }

   public EJBAttributeWrapper(BusinessHandle var1) {
      super(var1);
   }
}
