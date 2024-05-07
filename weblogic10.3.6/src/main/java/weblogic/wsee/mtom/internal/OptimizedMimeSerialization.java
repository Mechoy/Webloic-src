package weblogic.wsee.mtom.internal;

import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyAssertion;
import weblogic.wsee.policy.framework.PolicyException;

public class OptimizedMimeSerialization extends PolicyAssertion {
   public static final QName MTOM_QNAME = new QName("http://schemas.xmlsoap.org/ws/2004/09/policy/optimizedmimeserialization", "OptimizedMimeSerialization", "wsoma");

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(this.getName(), var1, "wsoma");
      return var2;
   }

   public QName getName() {
      return MTOM_QNAME;
   }
}
