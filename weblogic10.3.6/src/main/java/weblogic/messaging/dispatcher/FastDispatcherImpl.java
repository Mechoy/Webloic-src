package weblogic.messaging.dispatcher;

import weblogic.rmi.extensions.server.RemoteReference;
import weblogic.rmi.extensions.server.SmartStubInfo;
import weblogic.rmi.internal.Stub;

public final class FastDispatcherImpl extends DispatcherImpl implements SmartStubInfo {
   private final String objectHandlerClassName;
   private final DispatcherObjectHandler objectHandler;

   FastDispatcherImpl(String var1, DispatcherId var2, DispatcherImpl var3) {
      super(var1, var2, var3.getWorkManager(), var3.getOneWayWorkManager(), var3.getObjectHandlerClassName());
      this.objectHandlerClassName = var3.getObjectHandlerClassName();
      this.objectHandler = DispatcherObjectHandler.load(this.objectHandlerClassName);
   }

   public DispatcherObjectHandler getObjectHandler() {
      return this.objectHandler;
   }

   public Object getSmartStub(Object var1) {
      RemoteReference var2 = ((Stub)var1).getRemoteRef();
      return new DispatcherProxy(var2.getObjectID(), var2.getHostID(), this.objectHandlerClassName);
   }
}
