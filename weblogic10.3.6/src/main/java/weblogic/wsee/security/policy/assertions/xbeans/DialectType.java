package weblogic.wsee.security.policy.assertions.xbeans;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlAnyURI;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
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

public interface DialectType extends XmlAnyURI {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(DialectType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("dialecttype7509type");

   public static final class Factory {
      public static DialectType newValue(Object var0) {
         return (DialectType)DialectType.type.newValue(var0);
      }

      public static DialectType newInstance() {
         return (DialectType)XmlBeans.getContextTypeLoader().newInstance(DialectType.type, (XmlOptions)null);
      }

      public static DialectType newInstance(XmlOptions var0) {
         return (DialectType)XmlBeans.getContextTypeLoader().newInstance(DialectType.type, var0);
      }

      public static DialectType parse(String var0) throws XmlException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      public static DialectType parse(String var0, XmlOptions var1) throws XmlException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      public static DialectType parse(File var0) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      public static DialectType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      public static DialectType parse(URL var0) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      public static DialectType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      public static DialectType parse(InputStream var0) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      public static DialectType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      public static DialectType parse(Reader var0) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      public static DialectType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      public static DialectType parse(XMLStreamReader var0) throws XmlException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      public static DialectType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      public static DialectType parse(Node var0) throws XmlException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      public static DialectType parse(Node var0, XmlOptions var1) throws XmlException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      /** @deprecated */
      public static DialectType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static DialectType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (DialectType)XmlBeans.getContextTypeLoader().parse(var0, DialectType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, DialectType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, DialectType.type, var1);
      }

      private Factory() {
      }
   }
}
