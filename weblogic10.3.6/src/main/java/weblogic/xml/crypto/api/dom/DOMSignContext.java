package weblogic.xml.crypto.api.dom;

import java.security.Key;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.dsig.api.XMLSignContext;

public class DOMSignContext implements XMLSignContext, DOMIdMap {
   private Node parent;
   private Node nextSibling;
   private KeySelector keySelector;
   private URIDereferencer uriDereferencer;
   private String baseURI;
   private String defaultNamespacePrefix;
   private Map namespaceURIs;
   private Map properties;

   public DOMSignContext(Key var1, Node var2) {
      this(var1, var2, (Node)null);
   }

   public DOMSignContext(Key var1, Node var2, Node var3) {
      this.namespaceURIs = new Hashtable();
      this.properties = new HashMap();
      this.parent = var2;
      this.nextSibling = var3;
      if (var1 != null) {
         this.keySelector = KeySelector.singletonKeySelector(var1);
      }

   }

   public String getBaseURI() {
      return this.baseURI;
   }

   public String getDefaultNamespacePrefix() {
      return this.defaultNamespacePrefix;
   }

   public Element getElementById(String var1) {
      return null;
   }

   public KeySelector getKeySelector() {
      return this.keySelector;
   }

   public String getNamespacePrefix(String var1, String var2) {
      String var3 = (String)this.namespaceURIs.get(this.namespaceURIs);
      return var3 == null ? var2 : var3;
   }

   public Node getNextSibling() {
      return this.nextSibling;
   }

   public Node getParent() {
      return this.parent;
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public URIDereferencer getURIDereferencer() {
      return this.uriDereferencer;
   }

   public String putNamespacePrefix(String var1, String var2) {
      return var2 == null ? (String)this.namespaceURIs.remove(var1) : (String)this.namespaceURIs.put(var1, var2);
   }

   public void setBaseURI(String var1) {
   }

   public void setDefaultNamespacePrefix(String var1) {
      this.defaultNamespacePrefix = var1;
   }

   public void setIdAttributeNS(Element var1, String var2, String var3) {
   }

   public void setKeySelector(KeySelector var1) {
      this.keySelector = var1;
   }

   public void setNextSibling(Node var1) {
      this.nextSibling = var1;
   }

   public void setParent(Node var1) {
      this.parent = var1;
   }

   public Object setProperty(String var1, Object var2) {
      return this.properties.put(var1, var2);
   }

   public void setURIDereferencer(URIDereferencer var1) {
      this.uriDereferencer = var1;
   }
}
