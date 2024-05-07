package weblogic.wsee.tools.wseegen.schemas;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import com.sun.java.xml.ns.j2Ee.PortComponentHandlerType;
import com.sun.java.xml.ns.j2Ee.String;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public interface HandlerChainType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(HandlerChainType.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("handlerchaintype408atype");

   String getHandlerChainName();

   void setHandlerChainName(String var1);

   String addNewHandlerChainName();

   PortComponentHandlerType[] getHandlerArray();

   PortComponentHandlerType getHandlerArray(int var1);

   int sizeOfHandlerArray();

   void setHandlerArray(PortComponentHandlerType[] var1);

   void setHandlerArray(int var1, PortComponentHandlerType var2);

   PortComponentHandlerType insertNewHandler(int var1);

   PortComponentHandlerType addNewHandler();

   void removeHandler(int var1);

   public static final class Factory {
      public static HandlerChainType newInstance() {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().newInstance(HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType newInstance(XmlOptions var0) {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().newInstance(HandlerChainType.type, var0);
      }

      public static HandlerChainType parse(java.lang.String var0) throws XmlException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType parse(java.lang.String var0, XmlOptions var1) throws XmlException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      public static HandlerChainType parse(File var0) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      public static HandlerChainType parse(URL var0) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      public static HandlerChainType parse(InputStream var0) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      public static HandlerChainType parse(Reader var0) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      public static HandlerChainType parse(XMLStreamReader var0) throws XmlException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      public static HandlerChainType parse(Node var0) throws XmlException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      public static HandlerChainType parse(Node var0, XmlOptions var1) throws XmlException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      /** @deprecated */
      public static HandlerChainType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static HandlerChainType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (HandlerChainType)XmlBeans.getContextTypeLoader().parse(var0, HandlerChainType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, HandlerChainType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, HandlerChainType.type, var1);
      }

      private Factory() {
      }
   }
}
