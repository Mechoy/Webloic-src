package weblogic.management.security.authentication;

import javax.management.InvalidAttributeValueException;
import javax.management.MBeanException;
import javax.management.modelmbean.ModelMBean;
import javax.management.modelmbean.RequiredModelMBean;

public class IdentityAsserterImpl extends AuthenticationProviderImpl {
   public IdentityAsserterImpl(ModelMBean var1) throws MBeanException {
      super(var1);
   }

   protected IdentityAsserterImpl(RequiredModelMBean var1) throws MBeanException {
      super(var1);
   }

   private IdentityAsserterMBean getMyMBean() {
      try {
         return (IdentityAsserterMBean)this.getProxy();
      } catch (MBeanException var2) {
         throw new AssertionError(var2);
      }
   }

   public boolean validateActiveTypes(String[] var1) throws InvalidAttributeValueException {
      String[] var2 = this.getMyMBean().getSupportedTypes();
      int var3 = 0;

      while(true) {
         if (var1 != null && var3 < var1.length) {
            String var4 = var1[var3];
            if (var4 != null && var4.length() >= 1) {
               boolean var5 = false;

               for(int var6 = 0; !var5 && var2 != null && var6 < var2.length; ++var6) {
                  if (var4.equals(var2[var6])) {
                     var5 = true;
                  }
               }

               if (!var5) {
                  return false;
               }

               ++var3;
               continue;
            }

            return false;
         }

         return true;
      }
   }
}
