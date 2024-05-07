package weblogic.wsee.addressing;

import org.w3c.dom.Element;
import weblogic.wsee.message.MsgHeader;
import weblogic.wsee.message.MsgHeaderException;

public abstract class EndpointReferenceHeader extends MsgHeader {
   private EndpointReference ref = new EndpointReference();

   public EndpointReferenceHeader(EndpointReference var1) {
      this.ref = var1;
   }

   public EndpointReferenceHeader() {
   }

   public EndpointReference getReference() {
      return this.ref;
   }

   public void setReference(EndpointReference var1) {
      this.ref = var1;
   }

   public void read(Element var1) throws MsgHeaderException {
      this.ref = new EndpointReference();
      this.ref.read(var1);
   }

   public void write(Element var1) throws MsgHeaderException {
      assert this.ref != null;

      this.ref.write(var1);
   }
}
