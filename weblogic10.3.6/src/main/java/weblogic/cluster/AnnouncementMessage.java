package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.rmi.spi.HostID;

final class AnnouncementMessage implements GroupMessage, Externalizable {
   private static final long serialVersionUID = -5211845034589522488L;
   ArrayList items;

   AnnouncementMessage(ArrayList var1) {
      this.items = var1;
   }

   public void execute(HostID var1) {
      AnnouncementManager.theOne().receiveAnnouncement(var1, this);
   }

   public String toString() {
      return "Announcement numItems:" + this.items.size();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ((WLObjectOutput)var1).writeArrayList(this.items);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.items = ((WLObjectInput)var1).readArrayList();
   }

   public AnnouncementMessage() {
   }
}
