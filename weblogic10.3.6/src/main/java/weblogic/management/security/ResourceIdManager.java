package weblogic.management.security;

import weblogic.management.security.internal.ResourceIdManagerDelegate;
import weblogic.security.acl.internal.AuthenticatedSubject;

public class ResourceIdManager {
   private static final ResourceIdManagerDelegate delegate;

   public static String[] listRegisteredResourceTypes() {
      if (delegate == null) {
         throw new RuntimeException("ResourceIdManager Delegate Unavailable");
      } else {
         return delegate.listRegisteredResourceTypes();
      }
   }

   public static void registerResourceType(AuthenticatedSubject var0, ResourceIdInfo var1) throws IllegalArgumentException {
      if (delegate == null) {
         throw new RuntimeException("ResourceIdManager Delegate Unavailable");
      } else {
         delegate.registerResourceType(var0, var1);
      }
   }

   static {
      try {
         delegate = (ResourceIdManagerDelegate)Class.forName("weblogic.security.utils.ResourceIdManagerDelegateImpl").newInstance();
      } catch (Exception var1) {
         throw new RuntimeException("ResourceIdManager delegate failure: " + var1.toString(), var1);
      }
   }
}
