package weblogic.wsee.tools.wseegen.schemas;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import com.sun.java.xml.ns.j2Ee.FullyQualifiedClassType;
import com.sun.java.xml.ns.j2Ee.ParamValueType;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public interface ListenerType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(ListenerType.class.getClassLoader(), "schemacom_bea_xml.system.s19C868EA707F69BC3737E9D413CDF781").resolveHandle("listenertype7977type");

   FullyQualifiedClassType getListenerClass();

   void setListenerClass(FullyQualifiedClassType var1);

   FullyQualifiedClassType addNewListenerClass();

   ParamValueType[] getInitParamArray();

   ParamValueType getInitParamArray(int var1);

   int sizeOfInitParamArray();

   void setInitParamArray(ParamValueType[] var1);

   void setInitParamArray(int var1, ParamValueType var2);

   ParamValueType insertNewInitParam(int var1);

   ParamValueType addNewInitParam();

   void removeInitParam(int var1);

   public static final class Factory {
      public static ListenerType newInstance() {
         return (ListenerType)XmlBeans.getContextTypeLoader().newInstance(ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType newInstance(XmlOptions var0) {
         return (ListenerType)XmlBeans.getContextTypeLoader().newInstance(ListenerType.type, var0);
      }

      public static ListenerType parse(String var0) throws XmlException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType parse(String var0, XmlOptions var1) throws XmlException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      public static ListenerType parse(File var0) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      public static ListenerType parse(URL var0) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      public static ListenerType parse(InputStream var0) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      public static ListenerType parse(Reader var0) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      public static ListenerType parse(XMLStreamReader var0) throws XmlException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      public static ListenerType parse(Node var0) throws XmlException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      public static ListenerType parse(Node var0, XmlOptions var1) throws XmlException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      /** @deprecated */
      public static ListenerType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static ListenerType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (ListenerType)XmlBeans.getContextTypeLoader().parse(var0, ListenerType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ListenerType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, ListenerType.type, var1);
      }

      private Factory() {
      }
   }
}
