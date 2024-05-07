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

public interface DeploymentListenersType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(DeploymentListenersType.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("deploymentlistenerstype023atype");

   ListenerListType getClient();

   boolean isSetClient();

   void setClient(ListenerListType var1);

   ListenerListType addNewClient();

   void unsetClient();

   ListenerListType getServer();

   boolean isSetServer();

   void setServer(ListenerListType var1);

   ListenerListType addNewServer();

   void unsetServer();

   public static final class Factory {
      public static DeploymentListenersType newInstance() {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().newInstance(DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType newInstance(XmlOptions var0) {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().newInstance(DeploymentListenersType.type, var0);
      }

      public static DeploymentListenersType parse(String var0) throws XmlException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType parse(String var0, XmlOptions var1) throws XmlException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      public static DeploymentListenersType parse(File var0) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      public static DeploymentListenersType parse(URL var0) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      public static DeploymentListenersType parse(InputStream var0) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      public static DeploymentListenersType parse(Reader var0) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      public static DeploymentListenersType parse(XMLStreamReader var0) throws XmlException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      public static DeploymentListenersType parse(Node var0) throws XmlException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      public static DeploymentListenersType parse(Node var0, XmlOptions var1) throws XmlException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      /** @deprecated */
      public static DeploymentListenersType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static DeploymentListenersType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (DeploymentListenersType)XmlBeans.getContextTypeLoader().parse(var0, DeploymentListenersType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, DeploymentListenersType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, DeploymentListenersType.type, var1);
      }

      private Factory() {
      }
   }
}
