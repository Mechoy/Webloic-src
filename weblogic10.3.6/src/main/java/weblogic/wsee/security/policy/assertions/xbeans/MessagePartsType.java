package weblogic.wsee.security.policy.assertions.xbeans;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlOptions;
import com.bea.xml.XmlString;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public interface MessagePartsType extends XmlString {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(MessagePartsType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("messagepartstype5884type");

   String getDialect();

   DialectType xgetDialect();

   boolean isSetDialect();

   void setDialect(String var1);

   void xsetDialect(DialectType var1);

   void unsetDialect();

   public static final class Factory {
      public static MessagePartsType newInstance() {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().newInstance(MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType newInstance(XmlOptions var0) {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().newInstance(MessagePartsType.type, var0);
      }

      public static MessagePartsType parse(String var0) throws XmlException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType parse(String var0, XmlOptions var1) throws XmlException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      public static MessagePartsType parse(File var0) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      public static MessagePartsType parse(URL var0) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      public static MessagePartsType parse(InputStream var0) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      public static MessagePartsType parse(Reader var0) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      public static MessagePartsType parse(XMLStreamReader var0) throws XmlException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      public static MessagePartsType parse(Node var0) throws XmlException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      public static MessagePartsType parse(Node var0, XmlOptions var1) throws XmlException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      /** @deprecated */
      public static MessagePartsType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static MessagePartsType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (MessagePartsType)XmlBeans.getContextTypeLoader().parse(var0, MessagePartsType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, MessagePartsType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, MessagePartsType.type, var1);
      }

      private Factory() {
      }
   }
}
