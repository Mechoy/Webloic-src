package weblogic.wsee.security.policy.assertions.xbeans;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlAnyURI;
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

public interface TransformType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(TransformType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("transformtype8b43type");

   String getURI();

   XmlAnyURI xgetURI();

   void setURI(String var1);

   void xsetURI(XmlAnyURI var1);

   public static final class Factory {
      public static TransformType newInstance() {
         return (TransformType)XmlBeans.getContextTypeLoader().newInstance(TransformType.type, (XmlOptions)null);
      }

      public static TransformType newInstance(XmlOptions var0) {
         return (TransformType)XmlBeans.getContextTypeLoader().newInstance(TransformType.type, var0);
      }

      public static TransformType parse(String var0) throws XmlException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      public static TransformType parse(String var0, XmlOptions var1) throws XmlException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      public static TransformType parse(File var0) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      public static TransformType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      public static TransformType parse(URL var0) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      public static TransformType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      public static TransformType parse(InputStream var0) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      public static TransformType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      public static TransformType parse(Reader var0) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      public static TransformType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      public static TransformType parse(XMLStreamReader var0) throws XmlException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      public static TransformType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      public static TransformType parse(Node var0) throws XmlException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      public static TransformType parse(Node var0, XmlOptions var1) throws XmlException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      /** @deprecated */
      public static TransformType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static TransformType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (TransformType)XmlBeans.getContextTypeLoader().parse(var0, TransformType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, TransformType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, TransformType.type, var1);
      }

      private Factory() {
      }
   }
}
