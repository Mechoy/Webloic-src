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

public interface ConfigType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(ConfigType.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("configtype73c5type");

   DeploymentListenersType getDeploymentListeners();

   boolean isSetDeploymentListeners();

   void setDeploymentListeners(DeploymentListenersType var1);

   DeploymentListenersType addNewDeploymentListeners();

   void unsetDeploymentListeners();

   public static final class Factory {
      public static ConfigType newInstance() {
         return (ConfigType)XmlBeans.getContextTypeLoader().newInstance(ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType newInstance(XmlOptions var0) {
         return (ConfigType)XmlBeans.getContextTypeLoader().newInstance(ConfigType.type, var0);
      }

      public static ConfigType parse(String var0) throws XmlException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType parse(String var0, XmlOptions var1) throws XmlException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      public static ConfigType parse(File var0) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      public static ConfigType parse(URL var0) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      public static ConfigType parse(InputStream var0) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      public static ConfigType parse(Reader var0) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      public static ConfigType parse(XMLStreamReader var0) throws XmlException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      public static ConfigType parse(Node var0) throws XmlException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      public static ConfigType parse(Node var0, XmlOptions var1) throws XmlException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      /** @deprecated */
      public static ConfigType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static ConfigType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (ConfigType)XmlBeans.getContextTypeLoader().parse(var0, ConfigType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfigType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ConfigType.type, var1);
      }

      private Factory() {
      }
   }
}
