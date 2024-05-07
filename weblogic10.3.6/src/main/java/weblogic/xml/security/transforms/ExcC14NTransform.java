package weblogic.xml.security.transforms;

import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import weblogic.utils.Debug;
import weblogic.xml.babel.stream.ExclusiveCanonicalWriter;
import weblogic.xml.babel.stream.XMLOutputStreamBase;
import weblogic.xml.schema.binding.internal.SoapTypes;
import weblogic.xml.schema.binding.util.StdNamespace;
import weblogic.xml.schema.types.XSDNMTokens;
import weblogic.xml.security.signature.Reference;
import weblogic.xml.security.utils.ElementFactory;
import weblogic.xml.security.utils.StreamUtils;
import weblogic.xml.security.utils.XMLStreamObserver;
import weblogic.xml.stream.Attribute;
import weblogic.xml.stream.StartElement;
import weblogic.xml.stream.XMLEvent;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLName;
import weblogic.xml.stream.XMLOutputStream;
import weblogic.xml.stream.XMLStreamException;

public class ExcC14NTransform extends C14NTransform {
   public static final String INCLUSIVE_NAMESPACES_PROPERTY_NAME = "InclusiveNamespaces";
   private String[] inclusiveNamespaces = null;
   private Map inclusiveNamespaceMap;
   private static final String TAG_INCLUSIVE_NAMESPACES = "InclusiveNamespaces";
   private static final String ATTR_PREFIX_LIST = "PrefixList";
   public static final String EXC_C14N_DEFAULT_PREFIX = "#default";
   public static final String EXC_C14N_PREFIX = "c14n";
   private static final String XSI_NAMESPACE = StdNamespace.instance().schemaInstance();
   private static final String XSD_TYPE = "type";
   private static final XMLName XSD_TYPE_NAME;
   private static final XMLName SOAP_ARRAYTYPE_NAME;
   public static final List QNAME_VALUE_ATTRIBUTES;

   public ExcC14NTransform(boolean var1) {
      super(var1);
   }

   public XMLOutputStream getXMLOutputStream() throws XMLStreamException {
      OutputStreamWriter var1;
      try {
         var1 = new OutputStreamWriter(this.dest.getOutputStream(), "UTF-8");
      } catch (UnsupportedEncodingException var3) {
         throw new AssertionError(var3);
      }

      printInclusiveNamespacesPrefixList(this.inclusiveNamespaces);
      ExclusiveCanonicalWriter var2 = new ExclusiveCanonicalWriter(var1, this.getNamespaces(), this.inclusiveNamespaces);
      if (this.withComments) {
         ((ExclusiveCanonicalWriter)var2).setWriteComments(this.withComments);
      }

      return new XMLOutputStreamBase(var2);
   }

   private static void printInclusiveNamespacesPrefixList(String[] var0) {
      if (VERBOSE) {
         String var1 = "";
         if (var0 != null) {
            for(int var2 = 0; var2 < var0.length; ++var2) {
               if (var2 != 0) {
                  var1 = var1 + " ";
               }

               var1 = var1 + var0[var2];
            }
         } else {
            var1 = "null";
         }

         Debug.say(" +++ inclusiveNamespaces : " + var1);
      }

   }

   public String getURI() {
      return this.withComments ? "http://www.w3.org/2001/10/xml-exc-c14n#WithComments" : "http://www.w3.org/2001/10/xml-exc-c14n#";
   }

   protected void toXMLInternal(XMLOutputStream var1, String var2, int var3) throws XMLStreamException {
      if (this.inclusiveNamespaces != null) {
         Attribute[] var4 = new Attribute[]{ElementFactory.createAttribute("PrefixList", XSDNMTokens.getCanonicalXml(this.inclusiveNamespaces))};
         StreamUtils.addElement(var1, "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces", (String)null, var4, var3, 2);
      }

   }

   protected void fromXMLInternal(XMLInputStream var1, String var2) throws XMLStreamException {
      if (StreamUtils.peekElement(var1, "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces")) {
         XMLEvent var3 = StreamUtils.getElement(var1, "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces");
         String var4 = StreamUtils.getAttributeByName("PrefixList", (String)null, (StartElement)var3);

         try {
            if (var4 != null) {
               this.setParameter("InclusiveNamespaces", var4);
            }
         } catch (TransformException var6) {
            throw new XMLStreamException(var6);
         }

         StreamUtils.closeScope(var1, "http://www.w3.org/2001/10/xml-exc-c14n#", "InclusiveNamespaces");
      }

   }

   public void setParameter(String var1, String var2) throws TransformException {
      if (var1.equalsIgnoreCase("InclusiveNamespaces")) {
         this.inclusiveNamespaces = XSDNMTokens.convertXml(var2, "#default");
      } else {
         throw new TransformException("Unknown property: " + var1 + "=" + var2);
      }
   }

   public String[] getInclusiveNamespaces() {
      return this.inclusiveNamespaces;
   }

   public void setInclusiveNamespaceMap(Map var1) {
      this.inclusiveNamespaceMap = var1;
      if (var1 != null && this.inclusiveNamespaces == null) {
         ArrayList var2 = new ArrayList();
         Iterator var3 = var1.entrySet().iterator();

         while(true) {
            while(var3.hasNext()) {
               Map.Entry var4 = (Map.Entry)var3.next();
               String var5 = (String)var4.getKey();
               if (var5 != null && !"".equals(var5)) {
                  var2.add(var5);
               } else {
                  var2.add("c14n");
               }
            }

            String[] var6 = new String[var2.size()];
            var2.toArray(var6);
            this.inclusiveNamespaces = var6;
            break;
         }
      }

   }

   public static void setupSign(XMLStreamObserver var0, String var1, Reference var2) {
      Transform[] var3 = var2.getTransforms();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Transform var5 = var3[var4];
         if (var5 instanceof ExcC14NTransform) {
            var0.add((String)var1, new ExcC14NSignObserver((ExcC14NTransform)var5));
         }
      }

   }

   public static void setupVerify(XMLStreamObserver var0, String var1, Reference var2) {
      Transform[] var3 = var2.getTransforms();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Transform var5 = var3[var4];
         if (var5 instanceof ExcC14NTransform) {
            ExcC14NTransform var6 = (ExcC14NTransform)var5;
            if (var6.inclusiveNamespaces != null) {
               var0.add((String)var1, new ExcC14NVerifyObserver(var6));
            }
         }
      }

   }

   public static final void registerQNameAttribute(String var0, String var1) {
      XMLName var2 = ElementFactory.createXMLName(var0, var1);
      if (!QNAME_VALUE_ATTRIBUTES.contains(var2)) {
         QNAME_VALUE_ATTRIBUTES.add(var2);
      }

   }

   static {
      XSD_TYPE_NAME = ElementFactory.createXMLName(XSI_NAMESPACE, "type", (String)null);
      SOAP_ARRAYTYPE_NAME = SoapTypes.SoapArrayType;
      QNAME_VALUE_ATTRIBUTES = new ArrayList();
      QNAME_VALUE_ATTRIBUTES.add(XSD_TYPE_NAME);
      QNAME_VALUE_ATTRIBUTES.add(SOAP_ARRAYTYPE_NAME);
   }
}
