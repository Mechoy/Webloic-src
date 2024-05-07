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

public interface BuiltinPolicyDocument extends XmlObject {
   SchemaType type = (SchemaType)XmlBeans.typeSystemForClassLoader(BuiltinPolicyDocument.class.getClassLoader(), "schemacom_bea_xml.system.s9820748CACE84C8553F3727CBA6A7549").resolveHandle("builtinpolicyfa4edoctype");

   BuiltinPolicyType getBuiltinPolicy();

   void setBuiltinPolicy(BuiltinPolicyType var1);

   BuiltinPolicyType addNewBuiltinPolicy();

   public static final class Factory {
      public static BuiltinPolicyDocument newInstance() {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().newInstance(BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument newInstance(XmlOptions var0) {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().newInstance(BuiltinPolicyDocument.type, var0);
      }

      public static BuiltinPolicyDocument parse(String var0) throws XmlException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument parse(String var0, XmlOptions var1) throws XmlException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      public static BuiltinPolicyDocument parse(File var0) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument parse(File var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      public static BuiltinPolicyDocument parse(URL var0) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument parse(URL var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      public static BuiltinPolicyDocument parse(InputStream var0) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument parse(InputStream var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      public static BuiltinPolicyDocument parse(Reader var0) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument parse(Reader var0, XmlOptions var1) throws XmlException, IOException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      public static BuiltinPolicyDocument parse(XMLStreamReader var0) throws XmlException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument parse(XMLStreamReader var0, XmlOptions var1) throws XmlException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      public static BuiltinPolicyDocument parse(Node var0) throws XmlException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      public static BuiltinPolicyDocument parse(Node var0, XmlOptions var1) throws XmlException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      /** @deprecated */
      public static BuiltinPolicyDocument parse(XMLInputStream var0) throws XmlException, XMLStreamException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static BuiltinPolicyDocument parse(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return (BuiltinPolicyDocument)XmlBeans.getContextTypeLoader().parse(var0, BuiltinPolicyDocument.type, var1);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, BuiltinPolicyDocument.type, (XmlOptions)null);
      }

      /** @deprecated */
      public static XMLInputStream newValidatingXMLInputStream(XMLInputStream var0, XmlOptions var1) throws XmlException, XMLStreamException {
         return XmlBeans.getContextTypeLoader().newValidatingXMLInputStream(var0, BuiltinPolicyDocument.type, var1);
      }

      private Factory() {
      }
   }
}
