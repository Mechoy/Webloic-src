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

public interface ListenerListType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(ListenerListType.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("listenerlisttypea935type");

   ListenerType[] getListenerArray();

   ListenerType getListenerArray(int var1);

   int sizeOfListenerArray();

   void setListenerArray(ListenerType[] var1);

   void setListenerArray(int var1, ListenerType var2);

   ListenerType insertNewListener(int var1);

   ListenerType addNewListener();

   void removeListener(int var1);

   public static final class Factory {
      public static ListenerListType newInstance() {
         return (ListenerListType)XmlBeans.getContextTypeLoader().newInstance(ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType newInstance(XmlOptions var0) {
         return (ListenerListType)XmlBeans.getContextTypeLoader().newInstance(ListenerListType.type, var0);
      }

      public static ListenerListType parse(String var0) throws XmlException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType parse(String var0, XmlOptions var1) throws XmlException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      public static ListenerListType parse(File var0) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      public static ListenerListType parse(URL var0) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      public static ListenerListType parse(InputStream var0) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      public static ListenerListType parse(Reader var0) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      public static ListenerListType parse(XMLStreamReader var0) throws XmlException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      public static ListenerListType parse(Node var0) throws XmlException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      public static ListenerListType parse(Node var0, XmlOptions var1) throws XmlException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      /** @deprecated */
      public static ListenerListType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static ListenerListType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (ListenerListType)XmlBeans.getContextTypeLoader().parse(var0, ListenerListType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ListenerListType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ListenerListType.type, var1);
      }

      private Factory() {
      }
   }
}
