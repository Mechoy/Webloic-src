package weblogic.wsee.reliability.policy11;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import javax.xml.namespace.QName;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.framework.DOMUtils;
import weblogic.wsee.policy.framework.PolicyException;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.wsdl.WsdlWriter;
import weblogic.xml.dom.DOMProcessingException;

public class ExactlyOnce implements Externalizable {
   private static final long serialVersionUID = 1L;
   public static final QName NAME = new QName(WsrmConstants.RMVersion.latest().getPolicyNamespaceUri(), "ExactlyOnce");

   public Element serialize(Document var1) throws PolicyException {
      Element var2 = DOMUtils.createElement(NAME, var1);
      return var2;
   }

   public void write(Element var1, WsdlWriter var2) {
      var2.addChild(var1, NAME.getLocalPart(), NAME.getNamespaceURI());
   }

   protected void read(Element var1) throws DOMProcessingException {
   }

   public boolean equals(Object var1) {
      return var1 instanceof ExactlyOnce;
   }

   public int hashCode() {
      return NAME.hashCode();
   }

   public QName getName() {
      return NAME;
   }

   public void readExternal(ObjectInput var1) throws IOException, ClassNotFoundException {
   }

   public void writeExternal(ObjectOutput var1) throws IOException {
   }
}
