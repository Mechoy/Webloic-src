package weblogic.messaging.dispatcher;

public class CrossDomainUtilCommon {
   public boolean isSameDomain(Dispatcher var1, Dispatcher var2) {
      if (var1 instanceof DispatcherImpl && var2 instanceof DispatcherImpl) {
         return true;
      } else {
         if (var1 instanceof DispatcherWrapperState && var2 instanceof DispatcherWrapperState) {
            DispatcherWrapperState var3 = (DispatcherWrapperState)var1;
            DispatcherWrapperState var4 = (DispatcherWrapperState)var2;
            if (!(var3.getRemoteDelegate() instanceof DispatcherProxy) || !(var4.getRemoteDelegate() instanceof DispatcherProxy)) {
               return true;
            }

            DispatcherProxy var5 = (DispatcherProxy)var3.getRemoteDelegate();
            DispatcherProxy var6 = (DispatcherProxy)var4.getRemoteDelegate();
            String var7 = var5.getRJVM().getID().getDomainName();
            String var8 = var6.getRJVM().getID().getDomainName();
            if (var7 != null && var8 != null && var7.equals(var8)) {
               return true;
            }
         }

         return false;
      }
   }

   public boolean isSameDomain(Dispatcher var1, DispatcherWrapper var2) {
      if (var1 instanceof DispatcherImpl && DispatcherManager.isLocal(var2)) {
         return true;
      } else {
         if (var1 instanceof DispatcherWrapperState) {
            DispatcherWrapperState var3 = (DispatcherWrapperState)var1;
            if (!(var3.getRemoteDelegate() instanceof DispatcherProxy) || !(var2.getRemoteDispatcher() instanceof DispatcherProxy)) {
               return true;
            }

            DispatcherProxy var4 = (DispatcherProxy)var3.getRemoteDelegate();
            DispatcherProxy var5 = (DispatcherProxy)var2.getRemoteDispatcher();
            String var6 = var4.getRJVM().getID().getDomainName();
            String var7 = var5.getRJVM().getID().getDomainName();
            if (var6 != null && var7 != null && var6.equals(var7)) {
               return true;
            }
         }

         return false;
      }
   }
}
