package weblogic.xml.crypto.encrypt.api.dom;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import weblogic.xml.crypto.dsig.api.CanonicalizationMethod;
import weblogic.xml.crypto.encrypt.api.TBEXML;
import weblogic.xml.dom.NodeListImpl;

public class DOMTBEXML extends TBEXML {
   private final NodeList nodeList;
   public static final String MIME_TYPE = "text/xml";
   public static final String DEFAULT_ENCODING = "UTF-8";

   public DOMTBEXML(NodeList var1, CanonicalizationMethod var2) {
      super("http://www.w3.org/2001/04/xmlenc#Content", "text/xml", "UTF-8", var2);
      this.nodeList = var1;
   }

   public DOMTBEXML(Element var1, CanonicalizationMethod var2) {
      super("http://www.w3.org/2001/04/xmlenc#Element", "text/xml", "UTF-8", var2);
      NodeListImpl var3 = new NodeListImpl();
      var3.add(var1);
      this.nodeList = var3;
   }

   public NodeList getNodeList() {
      return this.nodeList;
   }
}
