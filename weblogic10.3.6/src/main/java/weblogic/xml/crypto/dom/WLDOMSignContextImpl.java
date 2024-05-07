package weblogic.xml.crypto.dom;

import java.security.Key;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.dom.DOMSignContext;
import weblogic.xml.crypto.dsig.api.XMLSignature;
import weblogic.xml.crypto.wss.WSSConstants;

public class WLDOMSignContextImpl extends DOMSignContext implements WLDOMSignContext {
   private XMLSignature signature;

   public WLDOMSignContextImpl(Key var1, Node var2) {
      super(var1, var2);
      this.setProperty("weblogic.xml.crypto.idqnames", WSSConstants.BUILTIN_ID_QNAMES);
   }

   public WLDOMSignContextImpl(Key var1, Node var2, Node var3) {
      super(var1, var2, var3);
      this.setProperty("weblogic.xml.crypto.idqnames", WSSConstants.BUILTIN_ID_QNAMES);
   }

   public XMLSignature getXMLSignature() {
      return this.signature;
   }

   public void setXMLSignature(XMLSignature var1) {
      this.signature = var1;
   }
}
