package weblogic.wsee.security.policy.assertions.xbeans;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlAnyURI;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlBoolean;
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

public interface SecurityTokenType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(SecurityTokenType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("securitytokentype54b6type");

   String getTokenType();

   XmlAnyURI xgetTokenType();

   boolean isSetTokenType();

   void setTokenType(String var1);

   void xsetTokenType(XmlAnyURI var1);

   void unsetTokenType();

   boolean getIncludeInMessage();

   XmlBoolean xgetIncludeInMessage();

   boolean isSetIncludeInMessage();

   void setIncludeInMessage(boolean var1);

   void xsetIncludeInMessage(XmlBoolean var1);

   void unsetIncludeInMessage();

   String getDerivedFromTokenType();

   XmlAnyURI xgetDerivedFromTokenType();

   boolean isSetDerivedFromTokenType();

   void setDerivedFromTokenType(String var1);

   void xsetDerivedFromTokenType(XmlAnyURI var1);

   void unsetDerivedFromTokenType();

   public static final class Factory {
      public static SecurityTokenType newInstance() {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().newInstance(SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType newInstance(XmlOptions var0) {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().newInstance(SecurityTokenType.type, var0);
      }

      public static SecurityTokenType parse(String var0) throws XmlException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType parse(String var0, XmlOptions var1) throws XmlException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      public static SecurityTokenType parse(File var0) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      public static SecurityTokenType parse(URL var0) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      public static SecurityTokenType parse(InputStream var0) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      public static SecurityTokenType parse(Reader var0) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      public static SecurityTokenType parse(XMLStreamReader var0) throws XmlException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      public static SecurityTokenType parse(Node var0) throws XmlException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      public static SecurityTokenType parse(Node var0, XmlOptions var1) throws XmlException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      /** @deprecated */
      public static SecurityTokenType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static SecurityTokenType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (SecurityTokenType)XmlBeans.getContextTypeLoader().parse(var0, SecurityTokenType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, SecurityTokenType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, SecurityTokenType.type, var1);
      }

      private Factory() {
      }
   }
}
