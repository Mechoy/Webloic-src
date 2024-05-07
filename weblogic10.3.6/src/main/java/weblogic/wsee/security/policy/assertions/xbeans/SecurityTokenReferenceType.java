package weblogic.wsee.security.policy.assertions.xbeans;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public interface SecurityTokenReferenceType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(SecurityTokenReferenceType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("securitytokenreferencetype82cbtype");

   public static final class Factory {
      public static SecurityTokenReferenceType newInstance() {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().newInstance(SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType newInstance(XmlOptions var0) {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().newInstance(SecurityTokenReferenceType.type, var0);
      }

      public static SecurityTokenReferenceType parse(String var0) throws XmlException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType parse(String var0, XmlOptions var1) throws XmlException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      public static SecurityTokenReferenceType parse(File var0) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      public static SecurityTokenReferenceType parse(URL var0) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      public static SecurityTokenReferenceType parse(InputStream var0) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      public static SecurityTokenReferenceType parse(Reader var0) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      public static SecurityTokenReferenceType parse(XMLStreamReader var0) throws XmlException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      public static SecurityTokenReferenceType parse(Node var0) throws XmlException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      public static SecurityTokenReferenceType parse(Node var0, XmlOptions var1) throws XmlException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      /** @deprecated */
      public static SecurityTokenReferenceType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static SecurityTokenReferenceType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (SecurityTokenReferenceType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenReferenceType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, SecurityTokenReferenceType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, SecurityTokenReferenceType.type, var1);
      }

      private Factory() {
      }
   }
}
