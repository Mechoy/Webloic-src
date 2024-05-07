package weblogic.xml.crypto.dsig.keyinfo;

import java.security.KeyException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import org.w3c.dom.Node;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyInfo;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyName;
import weblogic.xml.crypto.dsig.api.keyinfo.KeyValue;
import weblogic.xml.crypto.utils.StaxUtils;
import weblogic.xml.crypto.wss.SecurityTokenReferenceImpl;
import weblogic.xml.crypto.wss.provider.SecurityTokenReference;

public class KeyInfoImpl implements XMLStructure, KeyInfo, WLXMLStructure {
   public static final String KEYINFO_ELEMENT = "KeyInfo";
   private List content;
   private String id;
   private List keyNames;
   private List publicKeys;
   private List securityTokenRefs;

   KeyInfoImpl(List var1) {
      this(var1, (String)null);
   }

   KeyInfoImpl(List var1, String var2) {
      this.keyNames = new ArrayList();
      this.publicKeys = new ArrayList();
      this.securityTokenRefs = new ArrayList();

      try {
         this.setContent(var1);
      } catch (KeyException var4) {
         throw new IllegalArgumentException(var4.getMessage());
      }

      this.id = var2;
   }

   public KeyInfoImpl() {
      this.keyNames = new ArrayList();
      this.publicKeys = new ArrayList();
      this.securityTokenRefs = new ArrayList();
      this.content = new ArrayList();
   }

   public List getContent() {
      return Collections.unmodifiableList(this.content);
   }

   private void setContent(List var1) throws KeyException {
      this.content = var1;
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         Object var3 = var2.next();
         if (var3 instanceof KeyName) {
            this.keyNames.add(((KeyName)var3).getName());
         }

         if (var3 instanceof KeyValue) {
            this.publicKeys.add(((KeyValue)var3).getPublicKey());
         }

         if (var3 instanceof SecurityTokenReference) {
            this.securityTokenRefs.add(var3);
         }
      }

   }

   public String getId() {
      return this.id;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "KeyInfo");
         if (this.id != null) {
            var1.writeAttribute("Id", this.id);
         }

         Iterator var2 = this.content.iterator();

         while(var2.hasNext()) {
            Object var3 = var2.next();
            if (var3 instanceof SecurityTokenReference) {
               SecurityTokenReference var4 = (SecurityTokenReference)var3;
               Node var5 = SecurityTokenReferenceImpl.getStrNode(var4);
               StaxUtils.writeNode(var1, var5);
            } else {
               ((WLXMLStructure)var3).write(var1);
            }
         }

         var1.writeEndElement();
      } catch (XMLStreamException var6) {
         throw new MarshalException("Failed to write element KeyInfo", var6);
      }
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         this.id = StaxUtils.getAttributeValue("http://www.w3.org/2000/09/xmldsig#", "Id", var1);
         var1.nextTag();

         while(!"KeyInfo".equals(var1.getLocalName()) || !"http://www.w3.org/2000/09/xmldsig#".equals(var1.getNamespaceURI())) {
            Object var2 = KeyInfoObjectBase.readKeyInfoObject(var1);
            this.content.add(var2);
            if (var2 instanceof KeyName) {
               this.keyNames.add(((KeyName)var2).getName());
            }

            if (var2 instanceof KeyValue) {
               this.publicKeys.add(((KeyValue)var2).getPublicKey());
            }

            if (var2 instanceof SecurityTokenReference) {
               this.securityTokenRefs.add(var2);
            }

            if (!(var2 instanceof SecurityTokenReference)) {
               var1.nextTag();
            } else if (!var1.isStartElement() && !var1.isEndElement()) {
               var1.nextTag();
            }
         }

         StaxUtils.forwardToEndElement("http://www.w3.org/2000/09/xmldsig#", "KeyInfo", var1);
      } catch (XMLStreamException var3) {
         throw new MarshalException("Failed to instantiate object for child of KeyInfo element.", var3);
      } catch (KeyException var4) {
         throw new MarshalException("Failed to get public key from KeyValue object.", var4);
      }
   }

   public String getKeyName() {
      return (String)this.keyNames.get(0);
   }

   public Iterator getPublicKeys() {
      return this.publicKeys.iterator();
   }

   public Iterator getKeyNames() {
      return this.keyNames.iterator();
   }

   public Iterator getSecurityTokenReferences() {
      return this.securityTokenRefs.iterator();
   }
}
