package weblogic.wsee.tools.wseegen.schemas;

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

public interface ConfigDocument extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(ConfigDocument.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("config827bdoctype");

   ConfigType getConfig();

   void setConfig(ConfigType var1);

   ConfigType addNewConfig();

   public static final class Factory {
      public static ConfigDocument newInstance() {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().newInstance(ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument newInstance(XmlOptions var0) {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().newInstance(ConfigDocument.type, var0);
      }

      public static ConfigDocument parse(String var0) throws XmlException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument parse(String var0, XmlOptions var1) throws XmlException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      public static ConfigDocument parse(File var0) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      public static ConfigDocument parse(URL var0) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      public static ConfigDocument parse(InputStream var0) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      public static ConfigDocument parse(Reader var0) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      public static ConfigDocument parse(XMLStreamReader var0) throws XmlException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      public static ConfigDocument parse(Node var0) throws XmlException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      public static ConfigDocument parse(Node var0, XmlOptions var1) throws XmlException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      /** @deprecated */
      public static ConfigDocument parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static ConfigDocument parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (ConfigDocument)XmlBeans.getContextTypeLoader().parse(var0, ConfigDocument.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfigDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfigDocument.type, var1);
      }

      private Factory() {
      }
   }
}
