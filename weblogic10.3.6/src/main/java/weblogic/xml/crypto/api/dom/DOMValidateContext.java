package weblogic.xml.crypto.api.dom;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.dsig.api.XMLValidateContext;

public class DOMValidateContext implements XMLValidateContext, DOMIdMap {
   private KeySelector keySelector;
   private Node node;
   private URIDereferencer uriDereferencer;
   private Map properties = new HashMap();
   private String baseURI;

   public DOMValidateContext(Key var1, Node var2) {
      this.keySelector = KeySelector.singletonKeySelector(var1);
      this.node = var2;
   }

   public DOMValidateContext(KeySelector var1, Node var2) {
      this.keySelector = var1;
      this.node = var2;
   }

   public String getBaseURI() {
      return this.baseURI;
   }

   public Element getElementById(String var1) {
      return null;
   }

   public KeySelector getKeySelector() {
      return this.keySelector;
   }

   public Node getNode() {
      return this.node;
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public URIDereferencer getURIDereferencer() {
      return this.uriDereferencer;
   }

   public void setBaseURI(String var1) {
      this.baseURI = var1;
   }

   public void setIdAttributeNS(Element var1, String var2, String var3) {
   }

   public void setKeySelector(KeySelector var1) {
      this.keySelector = var1;
   }

   public void setNode(Node var1) {
      this.node = var1;
   }

   public Object setProperty(String var1, Object var2) {
      return this.properties.put(var1, var2);
   }

   public void setURIDereferencer(URIDereferencer var1) {
      this.uriDereferencer = var1;
   }
}
