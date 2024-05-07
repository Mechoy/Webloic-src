package weblogic.jms.dispatcher;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.rmi.extensions.PortableRemoteObject;

public final class DispatcherWrapper extends weblogic.messaging.dispatcher.DispatcherWrapper {
   static final long serialVersionUID = -569390197367234160L;

   public DispatcherWrapper() {
   }

   DispatcherWrapper(weblogic.messaging.dispatcher.DispatcherImpl var1) {
      super(var1);
   }

   protected void writeExternalInterop(ObjectOutput var1) throws IOException {
      var1.writeObject(DispatcherImpl.THE_ONE);
      var1.writeObject(DispatcherImpl.THE_ONE);
   }

   protected void readExternalInterop(ObjectInput var1) throws IOException, ClassNotFoundException {
      DispatcherRemote var2 = (DispatcherRemote)PortableRemoteObject.narrow(var1.readObject(), DispatcherRemote.class);
      DispatcherOneWay var3 = (DispatcherOneWay)PortableRemoteObject.narrow(var1.readObject(), DispatcherOneWay.class);
      DispatcherInteropAdapter var4 = new DispatcherInteropAdapter(var2, var3);
      this.dispatcherRemote = var4;
      this.dispatcherOneWay = var4;
   }
}
