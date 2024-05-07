package weblogic.wsee.reliability.policy;

import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.wsdl.WsdlWriter;

public class SequenceExpires extends PolicyAssertion {
   public static final QName SEQUENCE_EXPIRES = new QName("http://www.bea.com/wsrm/policy", "Expires");
   public static final QName EXPIRES_ATTRIBUTE = new QName("Expires");
   private String expires;

   public SequenceExpires() {
   }

   public SequenceExpires(String var1) {
      assert var1 != null;

      this.expires = var1;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(SEQUENCE_EXPIRES, var1);
      DOMUtils.addAttribute(var2, EXPIRES_ATTRIBUTE, this.expires);
      return var2;
   }

   protected void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, SEQUENCE_EXPIRES.getLocalPart(), "http://www.bea.com/wsrm/policy");
      var2.setAttribute(var3, EXPIRES_ATTRIBUTE.getLocalPart(), (String)null, (String)this.expires);
   }

   public String getExpires() {
      return this.expires;
   }

   public void setExpires(String var1) {
      assert var1 != null;

      this.expires = var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SequenceExpires) {
         SequenceExpires var2 = (SequenceExpires)var1;
         if (var2 != null && this.expires.equals(var2.getExpires())) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return SEQUENCE_EXPIRES.hashCode();
   }

   public QName getName() {
      return SEQUENCE_EXPIRES;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.expires = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.expires);
   }
}
