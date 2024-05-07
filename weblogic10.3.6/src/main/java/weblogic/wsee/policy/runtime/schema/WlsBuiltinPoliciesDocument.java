package weblogic.wsee.policy.runtime.schema;

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

public interface WlsBuiltinPoliciesDocument extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(WlsBuiltinPoliciesDocument.class.getClassLoader(), "schemacom_bea_xml.system.s9820748CACE84C8553F3727CBA6A7549").resolveHandle("wlsbuiltinpolicies8360doctype");

   WlsBuiltinPolicies getWlsBuiltinPolicies();

   void setWlsBuiltinPolicies(WlsBuiltinPolicies var1);

   WlsBuiltinPolicies addNewWlsBuiltinPolicies();

   public static final class Factory {
      public static WlsBuiltinPoliciesDocument newInstance() {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().newInstance(WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument newInstance(XmlOptions var0) {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().newInstance(WlsBuiltinPoliciesDocument.type, var0);
      }

      public static WlsBuiltinPoliciesDocument parse(String var0) throws XmlException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument parse(String var0, XmlOptions var1) throws XmlException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      public static WlsBuiltinPoliciesDocument parse(File var0) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      public static WlsBuiltinPoliciesDocument parse(URL var0) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      public static WlsBuiltinPoliciesDocument parse(InputStream var0) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      public static WlsBuiltinPoliciesDocument parse(Reader var0) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      public static WlsBuiltinPoliciesDocument parse(XMLStreamReader var0) throws XmlException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      public static WlsBuiltinPoliciesDocument parse(Node var0) throws XmlException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      public static WlsBuiltinPoliciesDocument parse(Node var0, XmlOptions var1) throws XmlException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      /** @deprecated */
      public static WlsBuiltinPoliciesDocument parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static WlsBuiltinPoliciesDocument parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (WlsBuiltinPoliciesDocument)XmlBeans.getContextTypeLoader().parse(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, WlsBuiltinPoliciesDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, WlsBuiltinPoliciesDocument.type, var1);
      }

      private Factory() {
      }
   }

   public interface WlsBuiltinPolicies extends XmlObject {
      SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(WlsBuiltinPolicies.class.getClassLoader(), "schemacom_bea_xml.system.s9820748CACE84C8553F3727CBA6A7549").resolveHandle("wlsbuiltinpoliciesbe0felemtype");

      BuiltinPolicyType[] getBuiltinPolicyArray();

      BuiltinPolicyType getBuiltinPolicyArray(int var1);

      int sizeOfBuiltinPolicyArray();

      void setBuiltinPolicyArray(BuiltinPolicyType[] var1);

      void setBuiltinPolicyArray(int var1, BuiltinPolicyType var2);

      BuiltinPolicyType insertNewBuiltinPolicy(int var1);

      BuiltinPolicyType addNewBuiltinPolicy();

      void removeBuiltinPolicy(int var1);

      public static final class Factory {
         public static WlsBuiltinPolicies newInstance() {
            return (WlsBuiltinPolicies)XmlBeans.getContextTypeLoader().newInstance(WlsBuiltinPoliciesDocument.WlsBuiltinPolicies.type, (XmlOptions)null);
         }

         public static WlsBuiltinPolicies newInstance(XmlOptions var0) {
            return (WlsBuiltinPolicies)XmlBeans.getContextTypeLoader().newInstance(WlsBuiltinPoliciesDocument.WlsBuiltinPolicies.type, var0);
         }

         private Factory() {
         }
      }
   }
}
