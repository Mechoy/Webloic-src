package weblogic.wsee.bind.types;

import com.bea.staxb.buildtime.internal.bts.XmlTypeName;
import com.bea.xml.XmlException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import weblogic.wsee.bind.runtime.DeserializerContext;
import weblogic.wsee.bind.runtime.SerializerContext;
import weblogic.wsee.util.Verbose;

public class SOAPElementArrayTypeMapper implements TypeMapper {
   private static final boolean verbose = Verbose.isVerbose(SOAPElementArrayTypeMapper.class);
   private static boolean WRITE_ELEMENT_WILDCARD_ARRAY_WRAPPER = false;
   private static final QName anyElementWildCardArrayElementQName = XmlTypeName.forElementWildCardArrayElement().getQName();
   private static final QName anyElementWildCardArrayTypeQName = XmlTypeName.forElementWildCardArrayType().getQName();
   static final String XSI_NS = "http://www.w3.org/2001/XMLSchema-instance";

   public void serializeType(SOAPElement var1, Object var2, Class var3, QName var4, QName var5, SerializerContext var6, boolean var7, String var8) throws XmlException {
      this.serialize(var2, var1, var5, var4);
   }

   private void serialize(Object var1, SOAPElement var2, QName var3, QName var4) throws XmlException {
      if (verbose) {
         Verbose.here();
      }

      boolean var5 = !var4.equals(anyElementWildCardArrayElementQName) && !var4.equals(anyElementWildCardArrayTypeQName) || var4.equals(anyElementWildCardArrayElementQName) || var4.equals(anyElementWildCardArrayTypeQName) && WRITE_ELEMENT_WILDCARD_ARRAY_WRAPPER;
      if (verbose) {
         Verbose.log((Object)(" xmlTypeOrXmlElementQName='" + var4 + "'"));
      }

      if (var5) {
         if (verbose) {
            Verbose.log((Object)("writing SOAPElement array wrapper " + var3.getNamespaceURI() + " ; " + var3.getLocalPart()));
         }

         SOAPElement var6;
         try {
            var6 = var2.addChildElement(var3.getLocalPart(), "", var3.getNamespaceURI());
            if (var1 == null) {
               var6.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:nil", "true");
               return;
            }
         } catch (Exception var9) {
            throw new XmlException("Failed to add array wrapper element", var9);
         }

         var2 = var6;
      } else if (verbose) {
         Verbose.log((Object)("for element wildcard <any/> NOT writing SOAPElement array wrapper " + var3.getNamespaceURI() + " ; " + var3.getLocalPart()));
      }

      if (var1 instanceof SOAPElement[]) {
         SOAPElement[] var10 = (SOAPElement[])((SOAPElement[])var1);

         try {
            for(int var7 = 0; var7 < var10.length; ++var7) {
               var2.addChildElement(var10[var7]);
            }
         } catch (SOAPException var8) {
            throw new XmlException("Failed to add child SOAPElement to SOAPArray wrapper, " + var8);
         }
      } else {
         if (var1 != null) {
            throw new XmlException("object type unknown: " + var1.getClass());
         }

         var2.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:nil", "true");
      }

   }

   public SOAPElement[] deserializeType(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException {
      if (verbose) {
         Verbose.here();
      }

      return this.deserializeElement(var1, var2, var3, var4, var5);
   }

   public void serializeElement(SOAPElement var1, Object var2, Class var3, QName var4, SerializerContext var5, boolean var6, String var7) throws XMLStreamException, XmlException {
      this.serialize(var2, var1, var4, var4);
   }

   public SOAPElement[] deserializeElement(SOAPElement var1, Class var2, QName var3, DeserializerContext var4, boolean var5) throws XmlException {
      if (verbose) {
         Verbose.here();
      }

      if (var1 == null) {
         return null;
      } else if (hasXsiNil(var1)) {
         return null;
      } else {
         ArrayList var6 = new ArrayList();
         NodeList var7 = var1.getChildNodes();
         int var8 = var7.getLength();

         for(int var9 = 0; var9 < var8; ++var9) {
            Node var10 = var7.item(var9);
            var6.add(var10);
         }

         SOAPElement[] var12 = (SOAPElement[])((SOAPElement[])Array.newInstance(SOAPElement.class, var8));
         SOAPElement[] var11 = (SOAPElement[])((SOAPElement[])var6.toArray(var12));
         return var11;
      }
   }

   private static boolean hasXsiNil(Node var0) {
      if (!var0.hasAttributes()) {
         return false;
      } else {
         Node var1 = var0.getAttributes().getNamedItemNS("http://www.w3.org/2001/XMLSchema-instance", "nil");
         if (var1 == null) {
            return false;
         } else if (var1.getNodeType() != 2) {
            return false;
         } else {
            String var2 = var1.getNodeValue();
            if (var2 == null) {
               return false;
            } else {
               return var2.equalsIgnoreCase("true");
            }
         }
      }
   }
}
