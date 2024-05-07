package weblogic.iiop;

import weblogic.protocol.Identity;
import weblogic.protocol.ServerChannelManager;
import weblogic.rjvm.JVMID;

public final class RoutingContext extends ServiceContext {
   private ConnectionKey connectionKey;
   private Identity identity;
   private static RoutingContext routingContext = new RoutingContext();

   public RoutingContext() {
      super(1111834888);
      this.connectionKey = new ConnectionKey(JVMID.localID().address().getHostAddress(), ServerChannelManager.findLocalServerPort(ProtocolHandlerIIOP.PROTOCOL_IIOP));
      this.identity = JVMID.localID().getTransientIdentity();
   }

   protected RoutingContext(IIOPInputStream var1) {
      super(1111834888);
      this.readEncapsulatedContext(var1);
   }

   public static final RoutingContext getRoutingContext() {
      return routingContext;
   }

   public ConnectionKey getConnectionKey() {
      return this.connectionKey;
   }

   public Identity getIdentity() {
      return this.identity;
   }

   public void write(IIOPOutputStream var1) {
      this.writeEncapsulatedContext(var1);
   }

   protected void readEncapsulation(IIOPInputStream var1) {
      this.connectionKey = new ConnectionKey(var1);
      this.identity = Identity.read(var1);
   }

   protected void writeEncapsulation(IIOPOutputStream var1) {
      this.connectionKey.writeForChannel(var1);
      this.identity.write(var1);
   }

   public String toString() {
      return "RoutingContext: connectionKey = " + this.connectionKey + " identity = " + this.identity;
   }
}
