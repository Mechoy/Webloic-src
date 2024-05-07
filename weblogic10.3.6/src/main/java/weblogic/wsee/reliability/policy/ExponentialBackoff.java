package weblogic.wsee.reliability.policy;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.wsdl.WsdlWriter;

public class ExponentialBackoff implements Externalizable {
   public static final QName EXPONENTIAL_BACKOFF = new QName("http://schemas.xmlsoap.org/ws/2005/02/rm/policy", "ExponentialBackoff");

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(EXPONENTIAL_BACKOFF, var1);
      return var2;
   }

   public void write(Element var1, WsdlWriter var2) {
      var2.addChild(var1, EXPONENTIAL_BACKOFF.getLocalPart(), "http://schemas.xmlsoap.org/ws/2005/02/rm/policy");
   }

   public boolean equals(Object var1) {
      return var1 != null && var1 instanceof ExponentialBackoff;
   }

   public int hashCode() {
      return EXPONENTIAL_BACKOFF.hashCode();
   }

   public QName getName() {
      return EXPONENTIAL_BACKOFF;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
   }
}
