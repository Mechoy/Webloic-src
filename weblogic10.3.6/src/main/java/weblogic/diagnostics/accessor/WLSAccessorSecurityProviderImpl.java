package weblogic.diagnostics.accessor;

public class WLSAccessorSecurityProviderImpl implements AccessorSecurityProvider {
   public void ensureUserAuthorized(int var1) throws AccessorException {
      try {
         AccessorUtils.ensureUserAuthorized();
      } catch (Exception var3) {
         throw new AccessorException(var3.getMessage(), var3);
      }
   }
}
