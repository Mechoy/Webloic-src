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

public class SequenceQOS extends PolicyAssertion {
   public static final QName SEQUENCE_QOS = new QName("http://www.bea.com/wsrm/policy", "QOS");
   public static final QName QOS_ATTRIBUTE = new QName("QOS");
   private String qos;

   public SequenceQOS() {
   }

   public SequenceQOS(String var1) {
      assert var1 != null;

      this.qos = var1;
   }

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(SEQUENCE_QOS, var1);
      DOMUtils.addAttribute(var2, QOS_ATTRIBUTE, this.qos);
      return var2;
   }

   protected void write(Element var1, WsdlWriter var2) {
      Element var3 = var2.addChild(var1, "QOS", "http://www.bea.com/wsrm/policy");
      var2.setAttribute(var3, QOS_ATTRIBUTE.getLocalPart(), (String)null, (String)this.qos);
   }

   public String getQos() {
      return this.qos;
   }

   public void setQos(String var1) {
      assert var1 != null;

      this.qos = var1;
   }

   public boolean equals(Object var1) {
      if (var1 instanceof SequenceQOS) {
         SequenceQOS var2 = (SequenceQOS)var1;
         if (var2 != null && this.qos.equals(var2.getQos())) {
            return true;
         }
      }

      return false;
   }

   public int hashCode() {
      return SEQUENCE_QOS.hashCode();
   }

   public QName getName() {
      return SEQUENCE_QOS;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
      super.readExternal(var1);
      this.qos = var1.readUTF();
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
      super.writeExternal(var1);
      var1.writeUTF(this.qos);
   }
}
