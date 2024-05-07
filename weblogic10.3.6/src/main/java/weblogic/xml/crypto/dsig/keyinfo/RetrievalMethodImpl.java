package weblogic.xml.crypto.dsig.keyinfo;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import weblogic.xml.crypto.api.Data;
import weblogic.xml.crypto.api.MarshalException;
import weblogic.xml.crypto.api.URIReferenceException;
import weblogic.xml.crypto.api.XMLCryptoContext;
import weblogic.xml.crypto.api.XMLStructure;
import weblogic.xml.crypto.dsig.DsigConstants;
import weblogic.xml.crypto.dsig.KeyInfoObjectFactory;
import weblogic.xml.crypto.dsig.TransformImpl;
import weblogic.xml.crypto.dsig.WLXMLStructure;
import weblogic.xml.crypto.dsig.api.keyinfo.RetrievalMethod;
import weblogic.xml.crypto.utils.StaxUtils;

public class RetrievalMethodImpl extends KeyInfoObjectBase implements RetrievalMethod, XMLStructure, WLXMLStructure, KeyInfoObjectFactory {
   private static final String URI_ATTRIBUTE = "URI";
   private static final String TYPE_ATTRIBUTE = "Type";
   private static final String TRANSFORMS_ELEMENT = "Transforms";
   private String uri;
   private List transforms;
   private String type;

   RetrievalMethodImpl(String var1, String var2, List var3) {
      this.uri = var1;
      this.type = var2;
      this.transforms = var3;
   }

   public static void init() {
      register(new RetrievalMethodImpl((String)null, (String)null, (List)null));
   }

   public QName getQName() {
      return DsigConstants.RETRIEVALMETHOD_QNAME;
   }

   public Object newKeyInfoObject(XMLStreamReader var1) throws MarshalException {
      RetrievalMethodImpl var2 = new RetrievalMethodImpl((String)null, (String)null, (List)null);
      var2.read(var1);
      return var2;
   }

   public boolean isFeatureSupported(String var1) {
      return false;
   }

   public List getTransforms() {
      return this.transforms != null ? Collections.unmodifiableList(this.transforms) : null;
   }

   public String getURI() {
      return this.uri;
   }

   public void write(XMLStreamWriter var1) throws MarshalException {
      try {
         var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
         var1.writeAttribute("URI", this.uri);
         if (this.type != null) {
            var1.writeAttribute("Type", this.type);
         }

         if (this.transforms != null && !this.transforms.isEmpty()) {
            var1.writeStartElement("http://www.w3.org/2000/09/xmldsig#", "Transforms");
            Iterator var2 = this.transforms.iterator();

            while(var2.hasNext()) {
               TransformImpl var3 = (TransformImpl)var2.next();
               var3.write(var1);
            }

            var1.writeEndElement();
         }

         var1.writeEndElement();
      } catch (XMLStreamException var4) {
         throw new MarshalException(var4);
      }
   }

   public Data dereference(XMLCryptoContext var1) throws URIReferenceException {
      return var1.getURIDereferencer().dereference(this, var1);
   }

   public void read(XMLStreamReader var1) throws MarshalException {
      try {
         var1.require(1, "http://www.w3.org/2000/09/xmldsig#", "RetrievalMethod");
         this.uri = var1.getAttributeValue((String)null, "URI");
         this.type = var1.getAttributeValue((String)null, "Type");
         int var2 = var1.nextTag();
         if (var2 == 1) {
            StaxUtils.readStart(var1, "http://www.w3.org/2000/09/xmldsig#", "Transforms");

            while(!var1.isEndElement()) {
               TransformImpl.readTransform(var1);
            }

            StaxUtils.readEnd(var1, "http://www.w3.org/2000/09/xmldsig#", "Transforms");
         }

      } catch (XMLStreamException var3) {
         throw new MarshalException(var3);
      }
   }

   public String getType() {
      return this.type;
   }
}
