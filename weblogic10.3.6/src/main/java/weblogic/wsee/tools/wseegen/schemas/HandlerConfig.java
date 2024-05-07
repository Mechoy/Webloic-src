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

public interface HandlerConfig extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(HandlerConfig.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("handlerconfig5099doctype");

   HandlerConfigType getHandlerConfig();

   void setHandlerConfig(HandlerConfigType var1);

   HandlerConfigType addNewHandlerConfig();

   public static final class Factory {
      public static HandlerConfig newInstance() {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().newInstance(HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig newInstance(XmlOptions var0) {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().newInstance(HandlerConfig.type, var0);
      }

      public static HandlerConfig parse(String var0) throws XmlException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig parse(String var0, XmlOptions var1) throws XmlException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      public static HandlerConfig parse(File var0) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      public static HandlerConfig parse(URL var0) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      public static HandlerConfig parse(InputStream var0) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      public static HandlerConfig parse(Reader var0) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      public static HandlerConfig parse(XMLStreamReader var0) throws XmlException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      public static HandlerConfig parse(Node var0) throws XmlException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      public static HandlerConfig parse(Node var0, XmlOptions var1) throws XmlException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      /** @deprecated */
      public static HandlerConfig parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static HandlerConfig parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (HandlerConfig)XmlBeans.getContextTypeLoader().parse(var0, HandlerConfig.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, HandlerConfig.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, HandlerConfig.type, var1);
      }

      private Factory() {
      }
   }
}
