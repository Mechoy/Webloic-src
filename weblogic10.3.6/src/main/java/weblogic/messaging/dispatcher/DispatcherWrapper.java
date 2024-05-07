package weblogic.messaging.dispatcher;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectOutput;
import weblogic.common.internal.PeerInfo;
import weblogic.common.internal.PeerInfoable;
import weblogic.common.internal.VersionInfoFactory;
import weblogic.kernel.KernelStatus;
import weblogic.messaging.common.MessagingUtilities;
import weblogic.rmi.extensions.PortableRemoteObject;

public class DispatcherWrapper implements Externalizable {
   static final long serialVersionUID = -569390197367234160L;
   private static final byte EXTVERSION = 1;
   private static final byte DISPATCHER_VERSION = 8;
   private String name;
   private DispatcherId dispatcherId;
   private PeerInfo peerInfo;
   protected DispatcherRemote dispatcherRemote;
   protected DispatcherOneWay dispatcherOneWay;
   private InvocableManager appInvocableManager;

   public DispatcherWrapper(DispatcherImpl var1) {
      this.dispatcherId = var1.getId();
      this.name = var1.getName();
      this.dispatcherRemote = var1;
      this.dispatcherOneWay = var1;
      this.peerInfo = VersionInfoFactory.getPeerInfoForWire();
   }

   public void setAppInvocableManager(InvocableManager var1) {
      this.appInvocableManager = var1;
   }

   public InvocableManager getAppInvocableManager() {
      return this.appInvocableManager;
   }

   public DispatcherRemote getRemoteDispatcher() {
      return this.dispatcherRemote;
   }

   public DispatcherOneWay getOneWayDispatcher() {
      return this.dispatcherOneWay;
   }

   public PeerInfo getPeerInfo() {
      return this.peerInfo;
   }

   public final String getName() {
      return this.name;
   }

   public final DispatcherId getId() {
      return this.dispatcherId;
   }

   public final void setId(DispatcherId var1) {
      this.dispatcherId = var1;
   }

   public DispatcherWrapper() {
   }

   protected void writeExternalInterop(ObjectOutput var1) throws IOException {
   }

   protected void readExternalInterop(ObjectInput var1) throws IOException, ClassNotFoundException {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeByte(1);
      var1.writeByte(8);
      if (this.name == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         var1.writeUTF(this.name);
      }

      if (this.dispatcherId == null) {
         var1.writeBoolean(false);
      } else {
         var1.writeBoolean(true);
         this.dispatcherId.writeExternal(var1);
      }

      var1.writeObject(this.peerInfo);
      if (var1 instanceof PeerInfoable) {
         PeerInfo var2 = ((PeerInfoable)var1).getPeerInfo();
         if (var2.compareTo(PeerInfo.VERSION_DIABLO) < 0) {
            this.writeExternalInterop(var1);
            return;
         }
      }

      if (KernelStatus.isApplet() && var1 instanceof WLObjectOutput) {
         ((WLObjectOutput)var1).writeObjectWL(this.dispatcherRemote);
         ((WLObjectOutput)var1).writeObjectWL(this.dispatcherOneWay);
      } else {
         var1.writeObject(this.dispatcherRemote);
         var1.writeObject(this.dispatcherOneWay);
      }

   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      byte var2 = var1.readByte();
      if (var2 != 1) {
         throw MessagingUtilities.versionIOException(var2, 1, 1);
      } else {
         byte var3 = var1.readByte();
         if (var3 != 8) {
            throw MessagingUtilities.versionIOException(var3, 8, 8);
         } else {
            if (var1.readBoolean()) {
               this.name = var1.readUTF();
            }

            if (var1.readBoolean()) {
               this.dispatcherId = new DispatcherId();
               this.dispatcherId.readExternal(var1);
            }

            this.peerInfo = (PeerInfo)var1.readObject();
            if (this.peerInfo.compareTo(PeerInfo.VERSION_DIABLO) >= 0) {
               this.dispatcherRemote = (DispatcherRemote)PortableRemoteObject.narrow(var1.readObject(), DispatcherRemote.class);
               this.dispatcherOneWay = (DispatcherOneWay)PortableRemoteObject.narrow(var1.readObject(), DispatcherOneWay.class);
            } else {
               this.readExternalInterop(var1);
            }

         }
      }
   }
}
