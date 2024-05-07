package weblogic.messaging.dispatcher;

public final class IIOPClientCrossDomainUtil implements CrossDomainUtil {
   public boolean isRemoteDomain(DispatcherWrapper var1) {
      return false;
   }

   public boolean isSameDomain(Dispatcher var1, Dispatcher var2) {
      return true;
   }

   public boolean isSameDomain(Dispatcher var1, DispatcherWrapper var2) {
      return true;
   }
}
