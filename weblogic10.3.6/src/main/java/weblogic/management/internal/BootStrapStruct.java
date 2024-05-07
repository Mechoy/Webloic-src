package weblogic.management.internal;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.protocol.AdminServerIdentity;
import weblogic.protocol.LocalServerIdentity;
import weblogic.protocol.ServerIdentity;
import weblogic.server.channels.ChannelService;
import weblogic.utils.Debug;

public class BootStrapStruct implements Externalizable {
   private static final long serialVersionUID = -737557156369970506L;
   private String adminServerName = null;
   private ServerIdentity adminIdentity;

   public BootStrapStruct(String var1) {
      this.adminServerName = var1;
   }

   public BootStrapStruct() {
   }

   public ServerIdentity getAdminIdentity() {
      return this.adminIdentity;
   }

   public String getAdminServerName() {
      return this.adminServerName;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      var1.writeUTF(this.adminServerName);
      Debug.assertion(LocalServerIdentity.getIdentity().getServerName().equals(this.adminServerName));
      var1.writeObject(LocalServerIdentity.getIdentity());
      ChannelService.exportServerChannels(LocalServerIdentity.getIdentity(), var1);
      ProductionModeHelper.exportProductionMode(var1);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.adminServerName = var1.readUTF();
      this.adminIdentity = (ServerIdentity)var1.readObject();
      AdminServerIdentity.setBootstrapIdentity(this.adminIdentity);
      ChannelService.importNonLocalServerChannels(this.adminIdentity, var1);
      ProductionModeHelper.importProductionMode(var1);
   }
}
