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

public interface AlgorithmType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(AlgorithmType.class.getClassLoader(), "schemacom_bea_xml.system.s7BDA940CB9F04DBD9B06BD7C8459FD58").resolveHandle("algorithmtype8b20type");

   String getURI();

   XmlAnyURI xgetURI();

   boolean isSetURI();

   void setURI(String var1);

   void xsetURI(XmlAnyURI var1);

   void unsetURI();

   public static final class Factory {
      public static AlgorithmType newInstance() {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().newInstance(AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType newInstance(XmlOptions var0) {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().newInstance(AlgorithmType.type, var0);
      }

      public static AlgorithmType parse(String var0) throws XmlException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType parse(String var0, XmlOptions var1) throws XmlException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      public static AlgorithmType parse(File var0) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      public static AlgorithmType parse(URL var0) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      public static AlgorithmType parse(InputStream var0) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      public static AlgorithmType parse(Reader var0) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      public static AlgorithmType parse(XMLStreamReader var0) throws XmlException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      public static AlgorithmType parse(Node var0) throws XmlException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      public static AlgorithmType parse(Node var0, XmlOptions var1) throws XmlException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      /** @deprecated */
      public static AlgorithmType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static AlgorithmType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (AlgorithmType)XmlBeans.getContextTypeLoader().parse(var0, AlgorithmType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, AlgorithmType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, AlgorithmType.type, var1);
      }

      private Factory() {
      }
   }
}
