package weblogic.xml.crypto.encrypt.api.dom;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import weblogic.xml.crypto.URIDereferenceUtils;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.api.dom.DOMIdMap;
import weblogic.xml.crypto.encrypt.api.XMLEncryptContext;

public class DOMEncryptContext implements DOMIdMap, XMLEncryptContext {
   private String baseURI;
   private URIDereferencer dereferencer;
   private KeySelector keyselector;
   private final Map properties;
   private static final String DEFAULT_BASE_URI = "";
   private Node nextSibling;
   private Node parent;
   private static final String ID_QNAMES_PROPERTY = "weblogic.xml.crypto.idqnames";

   public DOMEncryptContext(Key var1) {
      this(var1, (Node)null);
   }

   public DOMEncryptContext(Key var1, Node var2) {
      this(var1, var2, (Node)null);
   }

   public DOMEncryptContext(Key var1, Node var2, Node var3) {
      this.properties = new HashMap();
      if (var1 != null) {
         this.keyselector = KeySelector.singletonKeySelector(var1);
      }

      this.baseURI = "";
      this.parent = var2;
      this.nextSibling = var3;
      this.dereferencer = DOMURIDerferencer.getInstance();
   }

   public String getBaseURI() {
      return this.baseURI;
   }

   public KeySelector getKeySelector() {
      return this.keyselector;
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public URIDereferencer getURIDereferencer() {
      return this.dereferencer;
   }

   public void setBaseURI(String var1) {
      this.baseURI = var1;
   }

   public void setKeySelector(KeySelector var1) {
      this.keyselector = var1;
   }

   public Object setProperty(String var1, Object var2) {
      return this.properties.put(var1, var2);
   }

   public void setURIDereferencer(URIDereferencer var1) {
      this.dereferencer = var1;
   }

   public Element getElementById(String var1) {
      Set var2 = (Set)this.getProperty("weblogic.xml.crypto.idqnames");
      Element var3 = (Element)URIDereferenceUtils.findNodeById(var1, var2, this.parent);
      return var3;
   }

   public void setIdAttributeNS(Element var1, String var2, String var3) {
   }

   public Node getNextSibling() {
      return this.nextSibling;
   }

   public Node getParent() {
      return this.parent;
   }

   public void setParent(Node var1) {
      this.parent = var1;
   }

   public void setNextSibling(Node var1) {
      this.nextSibling = var1;
   }
}
