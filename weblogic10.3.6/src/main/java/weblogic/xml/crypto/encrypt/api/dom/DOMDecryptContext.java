package weblogic.xml.crypto.encrypt.api.dom;

import java.security.Key;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import org.w3c.dom.Element;
import weblogic.xml.crypto.URIDereferenceUtils;
import weblogic.xml.crypto.api.KeySelector;
import weblogic.xml.crypto.api.URIDereferencer;
import weblogic.xml.crypto.api.dom.DOMIdMap;
import weblogic.xml.crypto.encrypt.api.XMLDecryptContext;

public class DOMDecryptContext implements XMLDecryptContext, DOMIdMap {
   private String baseURI;
   private URIDereferencer dereferencer;
   private KeySelector keyselector;
   private final Map properties;
   private static final String DEFAULT_BASE_URI = "";
   private final Element node;
   public static final String ID_QNAMES_PROPERTY = "weblogic.xml.crypto.idqnames";

   public DOMDecryptContext(Key var1, Element var2) {
      this(KeySelector.singletonKeySelector(var1), var2);
   }

   public DOMDecryptContext(KeySelector var1, Element var2) {
      this.properties = new HashMap();
      this.keyselector = var1;
      this.node = var2;
      this.dereferencer = DOMURIDerferencer.getInstance();
      this.baseURI = "";
   }

   public URIDereferencer getURIDereferencer() {
      return this.dereferencer;
   }

   public void setURIDereferencer(URIDereferencer var1) {
      this.dereferencer = var1;
   }

   public KeySelector getKeySelector() {
      return this.keyselector;
   }

   public void setKeySelector(KeySelector var1) {
      this.keyselector = var1;
   }

   public String getBaseURI() {
      return this.baseURI;
   }

   public void setBaseURI(String var1) {
      this.baseURI = var1;
   }

   public Object getProperty(String var1) {
      return this.properties.get(var1);
   }

   public Object setProperty(String var1, Object var2) {
      return this.properties.put(var1, var2);
   }

   public Element getElementById(String var1) {
      Set var2 = (Set)this.getProperty("weblogic.xml.crypto.idqnames");
      Element var3 = (Element)URIDereferenceUtils.findNodeById(var1, var2, this.node);
      return var3;
   }

   public void setIdAttributeNS(Element var1, String var2, String var3) {
      Set var4 = (Set)this.getProperty("weblogic.xml.crypto.idqnames");
      var4.add(new QName(var2, var3));
   }

   public Element getNode() {
      return this.node;
   }
}
