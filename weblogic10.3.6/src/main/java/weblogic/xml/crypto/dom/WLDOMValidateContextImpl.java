package weblogic.xml.crypto.dom;

import java.security.Key;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.dom.DOMValidateContext;
import weblogic.xml.crypto.wss.WSSConstants;

public class WLDOMValidateContextImpl extends DOMValidateContext implements WLDOMValidateContext {
   private Node signatureNode;

   public WLDOMValidateContextImpl(Key var1, Node var2) {
      super(var1, var2);
      this.setProperty("weblogic.xml.crypto.idqnames", WSSConstants.BUILTIN_ID_QNAMES);
   }

   public WLDOMValidateContextImpl(KeySelector var1, Node var2) {
      super(var1, var2);
      this.setProperty("weblogic.xml.crypto.idqnames", WSSConstants.BUILTIN_ID_QNAMES);
   }

   public void setSignatureNode(Node var1) {
      this.signatureNode = var1;
   }

   public Node getSignatureNode() {
      return this.signatureNode;
   }
}
