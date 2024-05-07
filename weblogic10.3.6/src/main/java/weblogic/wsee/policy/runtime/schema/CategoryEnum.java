package weblogic.wsee.policy.runtime.schema;

import com.bea.xml.SchemaType;
import com.bea.xml.StringEnumAbstractBase;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlOptions;
import com.bea.xml.XmlString;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Node;
import weblogic.xml.stream.XMLInputStream;
import weblogic.xml.stream.XMLStreamException;

public interface CategoryEnum extends XmlString {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(CategoryEnum.class.getClassLoader(), "schemacom_bea_xml.system.s9820748CACE84C8553F3727CBA6A7549").resolveHandle("categoryenum903atype");
   Enum WLS_92 = CategoryEnum.Enum.forString("WLS92");
   Enum WSSP_12 = CategoryEnum.Enum.forString("WSSP12");
   Enum WSSP_12_2007 = CategoryEnum.Enum.forString("WSSP12_2007");
   Enum WSRM = CategoryEnum.Enum.forString("WSRM");
   Enum SAML = CategoryEnum.Enum.forString("SAML");
   Enum MTOM = CategoryEnum.Enum.forString("MTOM");
   Enum WSMC = CategoryEnum.Enum.forString("WSMC");
   Enum WSSC = CategoryEnum.Enum.forString("WSSC");
   Enum WSSC_13 = CategoryEnum.Enum.forString("WSSC13");
   Enum WSSC_14 = CategoryEnum.Enum.forString("WSSC14");
   Enum WSP_15 = CategoryEnum.Enum.forString("WSP15");
   Enum TRANSPORT = CategoryEnum.Enum.forString("TRANSPORT");
   Enum WSS_10 = CategoryEnum.Enum.forString("WSS10");
   Enum WSS_11 = CategoryEnum.Enum.forString("WSS11");
   Enum PROTECTION = CategoryEnum.Enum.forString("PROTECTION");
   Enum INTERNAL = CategoryEnum.Enum.forString("INTERNAL");
   int INT_WLS_92 = 1;
   int INT_WSSP_12 = 2;
   int INT_WSSP_12_2007 = 3;
   int INT_WSRM = 4;
   int INT_SAML = 5;
   int INT_MTOM = 6;
   int INT_WSMC = 7;
   int INT_WSSC = 8;
   int INT_WSSC_13 = 9;
   int INT_WSSC_14 = 10;
   int INT_WSP_15 = 11;
   int INT_TRANSPORT = 12;
   int INT_WSS_10 = 13;
   int INT_WSS_11 = 14;
   int INT_PROTECTION = 15;
   int INT_INTERNAL = 16;

   StringEnumAbstractBase enumValue();

   void set(StringEnumAbstractBase var1);

   public static final class Factory {
      public static CategoryEnum newValue(Object var0) {
         return (CategoryEnum)CategoryEnum.type.newValue(var0);
      }

      public static CategoryEnum newInstance() {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().newInstance(CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum newInstance(XmlOptions var0) {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().newInstance(CategoryEnum.type, var0);
      }

      public static CategoryEnum parse(String var0) throws XmlException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum parse(String var0, XmlOptions var1) throws XmlException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      public static CategoryEnum parse(File var0) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      public static CategoryEnum parse(URL var0) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      public static CategoryEnum parse(InputStream var0) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      public static CategoryEnum parse(Reader var0) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      public static CategoryEnum parse(XMLStreamReader var0) throws XmlException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      public static CategoryEnum parse(Node var0) throws XmlException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      public static CategoryEnum parse(Node var0, XmlOptions var1) throws XmlException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      /** @deprecated */
      public static CategoryEnum parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static CategoryEnum parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (CategoryEnum)XmlBeans.getContextTypeLoader().parse(var0, CategoryEnum.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, CategoryEnum.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, CategoryEnum.type, var1);
      }

      private Factory() {
      }
   }

   public static final class Enum extends StringEnumAbstractBase {
      static final int INT_WLS_92 = 1;
      static final int INT_WSSP_12 = 2;
      static final int INT_WSSP_12_2007 = 3;
      static final int INT_WSRM = 4;
      static final int INT_SAML = 5;
      static final int INT_MTOM = 6;
      static final int INT_WSMC = 7;
      static final int INT_WSSC = 8;
      static final int INT_WSSC_13 = 9;
      static final int INT_WSSC_14 = 10;
      static final int INT_WSP_15 = 11;
      static final int INT_TRANSPORT = 12;
      static final int INT_WSS_10 = 13;
      static final int INT_WSS_11 = 14;
      static final int INT_PROTECTION = 15;
      static final int INT_INTERNAL = 16;
      public static final StringEnumAbstractBase.Table table = new StringEnumAbstractBase.Table(new Enum[]{new Enum("WLS92", 1), new Enum("WSSP12", 2), new Enum("WSSP12_2007", 3), new Enum("WSRM", 4), new Enum("SAML", 5), new Enum("MTOM", 6), new Enum("WSMC", 7), new Enum("WSSC", 8), new Enum("WSSC13", 9), new Enum("WSSC14", 10), new Enum("WSP15", 11), new Enum("TRANSPORT", 12), new Enum("WSS10", 13), new Enum("WSS11", 14), new Enum("PROTECTION", 15), new Enum("INTERNAL", 16)});
      private static final long serialVersionUID = 1L;

      public static Enum forString(String var0) {
         return (Enum)table.forString(var0);
      }

      public static Enum forInt(int var0) {
         return (Enum)table.forInt(var0);
      }

      private Enum(String var1, int var2) {
         super(var1, var2);
      }

      private Object readResolve() {
         return forInt(this.intValue());
      }
   }
}
