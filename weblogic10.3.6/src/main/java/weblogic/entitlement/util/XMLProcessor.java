package weblogic.entitlement.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.StringTokenizer;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import weblogic.entitlement.data.EResource;
import weblogic.entitlement.data.ERole;
import weblogic.entitlement.expression.EExpression;
import weblogic.entitlement.parser.Parser;
import weblogic.security.service.MBeanResource;
import weblogic.security.service.MBeanResource.ActionType;
import weblogic.security.spi.Resource;

public class XMLProcessor {
   private static String RESOURCE_TAG = "resource";
   private static String ROLE_TAG = "role";
   private static String PREDICATE_TAG = "predicate";
   private static String EEXPR_TAG = "eexpr";
   private static String NAME_TAG = "name";
   private static String PARAM_TAG = "param";
   private static String TYPE_TAG = "type";
   private static String VALUE_TAG = "value";
   private static String AUX_TAG = "aux";
   private Document doc = null;

   public XMLProcessor(InputStream var1) throws SAXException, ParserConfigurationException, IOException {
      DocumentBuilderFactory var2 = DocumentBuilderFactory.newInstance();
      var2.setValidating(false);
      var2.setIgnoringComments(true);
      var2.setIgnoringElementContentWhitespace(true);
      var2.setCoalescing(true);
      DocumentBuilder var3 = var2.newDocumentBuilder();
      var3.setErrorHandler(new ErrorHandler() {
         public void fatalError(SAXParseException var1) throws SAXException {
            throw new SAXException("Fatal Error: " + this.getParseExceptionInfo(var1));
         }

         public void error(SAXParseException var1) throws SAXException {
            throw new SAXException("Error: " + this.getParseExceptionInfo(var1));
         }

         public void warning(SAXParseException var1) throws SAXException {
         }

         private String getParseExceptionInfo(SAXParseException var1) {
            return "URI=" + var1.getSystemId() + " Line=" + var1.getLineNumber() + ": " + var1.getMessage();
         }
      });
      this.doc = var3.parse(var1);
   }

   public void writeElements(LdiftWriter var1) throws Exception {
      NodeList var2 = this.doc.getElementsByTagName(RESOURCE_TAG);
      int var3 = 0;

      int var4;
      for(var4 = var2.getLength(); var3 < var4; ++var3) {
         Element var5 = (Element)var2.item(var3);
         String var6 = getResourceId(var5);
         EExpression var7 = Parser.parseResourceExpression(var5.getAttribute(EEXPR_TAG));
         if (var6.length() != 0 && var7 != null) {
            var1.write(new EResource(var6, var7));
         }

         NodeList var8 = var5.getElementsByTagName(ROLE_TAG);
         int var9 = 0;

         for(int var10 = var8.getLength(); var9 < var10; ++var9) {
            Element var11 = (Element)var8.item(var9);
            String var12 = var11.getAttribute(NAME_TAG);
            String var13 = var11.getAttribute(EEXPR_TAG);
            String var14 = var11.getAttribute(AUX_TAG);
            EExpression var15 = Parser.parseResourceExpression(var13);
            var1.write(new ERole(var6, var12, var15), var14);
         }
      }

      NodeList var16 = this.doc.getElementsByTagName(PREDICATE_TAG);
      var4 = 0;

      for(int var17 = var16.getLength(); var4 < var17; ++var4) {
         var1.writePredicate(((Element)var16.item(var4)).getAttribute(NAME_TAG));
      }

   }

   private static String getResourceId(Element var0) throws Exception {
      String var1 = var0.getAttribute(NAME_TAG).trim();
      if (var1.length() == 0) {
         return "";
      } else {
         NodeList var2 = var0.getElementsByTagName(PARAM_TAG);
         int var3 = var2.getLength();
         Class[] var4 = new Class[var3];
         Object[] var5 = new Object[var3];

         for(int var6 = 0; var6 < var3; ++var6) {
            Element var7 = (Element)var2.item(var6);
            var4[var6] = getClass(var7.getAttribute(TYPE_TAG));
            var5[var6] = getValue(var4[var6], var7.getAttribute(VALUE_TAG));
         }

         Resource var12 = null;

         try {
            Class var13 = Class.forName(var1);
            var12 = (Resource)var13.getConstructor(var4).newInstance(var5);
         } catch (ClassNotFoundException var10) {
            throw new Exception("Cannot find resource class: " + var1);
         } catch (Exception var11) {
            String var8 = var5.length > 0 ? String.valueOf(var5[0]) : "none";

            for(int var9 = 1; var9 < var5.length; ++var9) {
               var8 = var8 + ", " + var5[var9];
            }

            throw new Exception("Cannot instantiate resource of type: " + var1 + " with parameters: " + var8);
         }

         return var12.toString();
      }
   }

   private static Class getClass(String var0) throws ClassNotFoundException {
      try {
         if (var0.equals("weblogic.security.service.MBeanResource.ActionType")) {
            return MBeanResource.ActionType.class;
         } else {
            return var0.equals("java.lang.String[]") ? String[].class : Class.forName(var0);
         }
      } catch (ClassNotFoundException var2) {
         throw new ClassNotFoundException("Cannot instantiate class with the name: " + var0);
      }
   }

   private static Object getValue(Class var0, String var1) throws Exception {
      if (var1 != null && !var1.equalsIgnoreCase("null")) {
         Object var2 = var1;
         if (var0.isArray()) {
            StringTokenizer var3 = new StringTokenizer(var1, ",");
            int var4 = var3.countTokens();
            String[] var5 = new String[var4];

            for(int var6 = 0; var3.hasMoreTokens(); ++var6) {
               var5[var6] = var3.nextToken().trim();
            }

            var2 = var5;
         } else if (MBeanResource.ActionType.class.isAssignableFrom(var0)) {
            if (var1.equalsIgnoreCase("find")) {
               var2 = ActionType.FIND;
            } else if (var1.equalsIgnoreCase("register")) {
               var2 = ActionType.REGISTER;
            } else if (var1.equalsIgnoreCase("unregister")) {
               var2 = ActionType.UNREGISTER;
            } else if (var1.equalsIgnoreCase("write")) {
               var2 = ActionType.WRITE;
            } else if (var1.equalsIgnoreCase("read")) {
               var2 = ActionType.READ;
            } else {
               if (!var1.equalsIgnoreCase("execute")) {
                  throw new Exception("Unknown MBeanResource.ActionType: " + var1);
               }

               var2 = ActionType.EXECUTE;
            }
         } else if (Hashtable.class.isAssignableFrom(var0)) {
            var2 = new Hashtable();
         }

         return var2;
      } else {
         return null;
      }
   }
}
