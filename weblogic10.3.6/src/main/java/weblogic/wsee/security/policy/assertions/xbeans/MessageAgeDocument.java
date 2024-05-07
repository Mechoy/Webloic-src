package weblogic.wsee.security.policy.assertions.xbeans;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import com.bea.xml.XmlPositiveInteger;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigInteger;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public interface MessageAgeDocument extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(MessageAgeDocument.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("messageage6dcfdoctype");

   MessageAge getMessageAge();

   void setMessageAge(MessageAge var1);

   MessageAge addNewMessageAge();

   public static final class Factory {
      public static MessageAgeDocument newInstance() {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().newInstance(MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument newInstance(XmlOptions var0) {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().newInstance(MessageAgeDocument.type, var0);
      }

      public static MessageAgeDocument parse(String var0) throws XmlException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument parse(String var0, XmlOptions var1) throws XmlException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      public static MessageAgeDocument parse(File var0) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      public static MessageAgeDocument parse(URL var0) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      public static MessageAgeDocument parse(InputStream var0) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      public static MessageAgeDocument parse(Reader var0) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      public static MessageAgeDocument parse(XMLStreamReader var0) throws XmlException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      public static MessageAgeDocument parse(Node var0) throws XmlException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      public static MessageAgeDocument parse(Node var0, XmlOptions var1) throws XmlException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      /** @deprecated */
      public static MessageAgeDocument parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static MessageAgeDocument parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (MessageAgeDocument)XmlBeans.getContextTypeLoader().parse(var0, MessageAgeDocument.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, MessageAgeDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, MessageAgeDocument.type, var1);
      }

      private Factory() {
      }
   }

   public interface MessageAge extends XmlObject {
      SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(MessageAge.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("messageage7503elemtype");

      BigInteger getAge();

      XmlPositiveInteger xgetAge();

      boolean isSetAge();

      void setAge(BigInteger var1);

      void xsetAge(XmlPositiveInteger var1);

      void unsetAge();

      public static final class Factory {
         public static MessageAge newInstance() {
            return (MessageAge)XmlBeans.getContextTypeLoader().newInstance(MessageAgeDocument.MessageAge.type, (XmlOptions)null);
         }

         public static MessageAge newInstance(XmlOptions var0) {
            return (MessageAge)XmlBeans.getContextTypeLoader().newInstance(MessageAgeDocument.MessageAge.type, var0);
         }

         private Factory() {
         }
      }
   }
}
