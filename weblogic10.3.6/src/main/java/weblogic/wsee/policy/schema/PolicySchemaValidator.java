package weblogic.wsee.policy.schema;

import com.bea.xbean.validator.ValidatingXMLStreamReader;
import com.bea.xbean.xb.xsdschema.SchemaDocument.Factory;
import com.bea.xml.SchemaType;
import com.bea.xml.SchemaTypeLoader;
import com.bea.xml.XmlBeans;
import com.bea.xml.XmlError;
import com.bea.xml.XmlException;
import com.bea.xml.XmlObject;
import com.bea.xml.XmlOptions;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.stream.XMLStreamException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import weblogic.wsee.policy.util.PolicyHelper;
import weblogic.wsee.util.Verbose;
import weblogic.wsee.util.dom.DOMParser;
import weblogic.xml.dom.DOMStreamReader;

public class PolicySchemaValidator {
   private static boolean verbose = Verbose.isVerbose(PolicySchemaValidator.class);
   public static final boolean INCLUDE_WS_POLICY_15 = true;
   private static final String[] defaultXsds = new String[]{"/weblogic/wsee/policy/schema/xmldsig-core-schema.xsd", "/weblogic/wsee/policy/schema/oasis-200401-wss-wssecurity-secext-1.0.xsd", "/weblogic/wsee/policy/schema/oasis-200401-wss-wssecurity-utility-1.0.xsd", "/weblogic/wsee/security/policy/assertions/wls90-security-policy.xsd", "/weblogic/wsee/reliability/policy/schema/wsrm-policy-2005-02-06-RC1.1.xsd"};
   private static final String[] oldXsds = new String[]{"/weblogic/wsee/policy/schema/ws-policy.xsd"};
   private static final String[] newXsds = new String[]{"/weblogic/wsee/policy/schema/ws-policy15.xsd"};
   private SchemaTypeLoader sLoader;
   private SchemaTypeLoader sLoader15;

   public PolicySchemaValidator() throws PolicySchemaValidationException {
      ArrayList var1 = new ArrayList();
      String[] var2 = defaultXsds;
      int var3 = var2.length;

      int var4;
      String var5;
      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         var1.add(this.getClass().getResourceAsStream(var5));
      }

      var2 = oldXsds;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         var1.add(this.getClass().getResourceAsStream(var5));
      }

      this.sLoader = buildSchemaTypeLoader((InputStream[])var1.toArray(new InputStream[0]));
      var1 = new ArrayList();
      var2 = defaultXsds;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         var1.add(this.getClass().getResourceAsStream(var5));
      }

      var2 = newXsds;
      var3 = var2.length;

      for(var4 = 0; var4 < var3; ++var4) {
         var5 = var2[var4];
         var1.add(this.getClass().getResourceAsStream(var5));
      }

      this.sLoader15 = buildSchemaTypeLoader((InputStream[])var1.toArray(new InputStream[0]));
   }

   public void addSchema(InputStream var1) throws PolicySchemaValidationException {
      InputStream[] var2 = new InputStream[]{var1};
      SchemaTypeLoader var3 = buildSchemaTypeLoader(var2);
      SchemaTypeLoader[] var4 = new SchemaTypeLoader[]{this.sLoader, var3};
      this.sLoader = XmlBeans.typeLoaderUnion(var4);
   }

   public void validate(Document var1) throws PolicySchemaValidationException {
      if (null == var1) {
         throw new PolicySchemaValidationException("Bad XML, null document found");
      } else {
         XmlOptions var2 = new XmlOptions();
         var2.setLoadLineNumbers();
         var2.setLoadMessageDigest();
         Element var4 = var1.getDocumentElement();
         if (null == var4) {
            throw new PolicySchemaValidationException("Bad XML, no element found");
         } else {
            SchemaTypeLoader var5 = null;
            if (PolicyHelper.hasWsp15NamespaceUri(var4)) {
               var5 = this.sLoader15;
            } else {
               var5 = this.sLoader;
            }

            try {
               DOMStreamReader var3 = new DOMStreamReader(var1.getDocumentElement());

               while(!var3.isStartElement()) {
                  var3.next();
               }

               ArrayList var6 = new ArrayList();
               ValidatingXMLStreamReader var7 = new ValidatingXMLStreamReader();
               var7.init(var3, true, (SchemaType)null, var5, var2, var6);

               while(var7.hasNext()) {
                  var7.next();
               }

               if (!var7.isValid()) {
                  String var8 = "";

                  for(Iterator var9 = var6.iterator(); var9.hasNext(); var8 = var8 + stringFromError((XmlError)var9.next())) {
                  }

                  throw new PolicySchemaValidationException(var8);
               }
            } catch (XMLStreamException var10) {
               throw new PolicySchemaValidationException(var10);
            }
         }
      }
   }

   private static SchemaTypeLoader buildSchemaTypeLoader(InputStream[] var0) throws PolicySchemaValidationException {
      ArrayList var1 = new ArrayList();

      try {
         InputStream[] var2 = var0;
         int var3 = var0.length;

         for(int var4 = 0; var4 < var3; ++var4) {
            InputStream var5 = var2[var4];
            var1.add(Factory.parse(var5));
         }

         return XmlBeans.loadXsd((XmlObject[])var1.toArray(new XmlObject[0]));
      } catch (IOException var6) {
         throw new PolicySchemaValidationException(var6);
      } catch (XmlException var7) {
         throw new PolicySchemaValidationException(var7);
      }
   }

   private static String stringFromError(XmlError var0) {
      String var1 = XmlError.severityAsString(var0.getSeverity()) + ":" + var0.getLine() + ":" + var0.getColumn() + " " + var0.getMessage() + "\n";
      return var1;
   }

   public static void main(String[] var0) throws IOException, PolicySchemaValidationException {
      if (var0.length == 0) {
         System.out.println("Usage: java PolicySchemaValidator policyfile");
      }

      long var1 = System.currentTimeMillis();
      PolicySchemaValidator var3 = new PolicySchemaValidator();
      String var4 = var0[0];
      Document var5 = DOMParser.getDocument(var4);
      System.out.println("Loading document takes  " + (System.currentTimeMillis() - var1) + " ms.");
      var1 = System.currentTimeMillis();
      var3.validate(var5);
      System.out.println(var4 + " is valid.  Validation time: " + (System.currentTimeMillis() - var1) + " ms.");
   }
}
