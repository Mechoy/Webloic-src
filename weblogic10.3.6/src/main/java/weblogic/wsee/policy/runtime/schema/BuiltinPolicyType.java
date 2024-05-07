package weblogic.wsee.policy.runtime.schema;

import com.bea.xml.SchemaType;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
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

public interface BuiltinPolicyType extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(BuiltinPolicyType.class.getClassLoader(), "schemacom_bea_xml.system.s9820748CACE84C8553F3727CBA6A7549").resolveHandle("builtinpolicytypea338type");

   CategoryEnum.Enum[] getCategoryArray();

   CategoryEnum.Enum getCategoryArray(int var1);

   CategoryEnum[] xgetCategoryArray();

   CategoryEnum xgetCategoryArray(int var1);

   int sizeOfCategoryArray();

   void setCategoryArray(CategoryEnum.Enum[] var1);

   void setCategoryArray(int var1, CategoryEnum.Enum var2);

   void xsetCategoryArray(CategoryEnum[] var1);

   void xsetCategoryArray(int var1, CategoryEnum var2);

   void insertCategory(int var1, CategoryEnum.Enum var2);

   void addCategory(CategoryEnum.Enum var1);

   CategoryEnum insertNewCategory(int var1);

   CategoryEnum addNewCategory();

   void removeCategory(int var1);

   String getPolicyName();

   XmlString xgetPolicyName();

   void setPolicyName(String var1);

   void xsetPolicyName(XmlString var1);

   public static final class Factory {
      public static BuiltinPolicyType newInstance() {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().newInstance(BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType newInstance(XmlOptions var0) {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().newInstance(BuiltinPolicyType.type, var0);
      }

      public static BuiltinPolicyType parse(String var0) throws XmlException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType parse(String var0, XmlOptions var1) throws XmlException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      public static BuiltinPolicyType parse(File var0) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      public static BuiltinPolicyType parse(URL var0) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      public static BuiltinPolicyType parse(InputStream var0) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      public static BuiltinPolicyType parse(Reader var0) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      public static BuiltinPolicyType parse(XMLStreamReader var0) throws XmlException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      public static BuiltinPolicyType parse(Node var0) throws XmlException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      public static BuiltinPolicyType parse(Node var0, XmlOptions var1) throws XmlException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      /** @deprecated */
      public static BuiltinPolicyType parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static BuiltinPolicyType parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (BuiltinPolicyType)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyType.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, BuiltinPolicyType.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, BuiltinPolicyType.type, var1);
      }

      private Factory() {
      }
   }
}
