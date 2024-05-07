package weblogic.cluster;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import weblogic.common.WLObjectInput;
import weblogic.common.WLObjectOutput;
import weblogic.rmi.spi.HostID;

final class AttributesMessage implements GroupMessage, Externalizable {
   private static final long serialVersionUID = 2610797619397430239L;
   MemberAttributes attributes;

   AttributesMessage(MemberAttributes var1) {
      this.attributes = var1;
   }

   public void execute(HostID var1) {
      AttributeManager.theOne().receiveAttributes(var1, this);
   }

   public String toString() {
      return "Attributes " + this.attributes;
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      ((WLObjectOutput)var1).writeObjectWL(this.attributes);
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      this.attributes = (MemberAttributes)((WLObjectInput)var1).readObjectWL();
   }

   public AttributesMessage() {
   }
}
