package weblogic.server.channels;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.ObjectStreamException;
import weblogic.protocol.ChannelList;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;

class ChannelListImpl implements ChannelList {
   private ServerIdentity id;
   private RemoteChannelService service;

   public ChannelListImpl() {
      this.id = LocalServerIdentity.getIdentity();
      this.service = (RemoteChannelService)RemoteChannelServiceImpl.getInstance();
   }

   public ChannelListImpl(ServerIdentity var1) {
      this.id = var1;
   }

   public ServerIdentity getIdentity() {
      return this.id;
   }

   public RemoteChannelService getChannelService() {
      return this.service;
   }

   protected Object writeReplace() throws ObjectStreamException {
      return !ChannelService.hasChannels(this.id) ? null : this;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeObject(this.id);
      var1.writeObject(this.service);
      ChannelService.exportServerChannels(this.id, var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.id = (ServerIdentity)var1.readObject();
      this.service = (RemoteChannelService)var1.readObject();
      ChannelService.importServerChannels(this.id, var1);
   }
}
