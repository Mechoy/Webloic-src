package weblogic.wsee.tools.wsdlc;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.QName;
import weblogic.wsee.tools.logging.EventLevel;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.tools.source.JsFault;
import weblogic.wsee.tools.source.JsMethod;
import weblogic.wsee.util.StringUtil;
import weblogic.wsee.wsdl.WsdlDefinitions;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.WsdlSchema;
import weblogic.wsee.wsdl.WsdlSchemaImport;
import weblogic.wsee.wsdl.WsdlUtils;

public class WsdlcUtils {
   public static String throwException(JsMethod var0) {
      String var1 = "";
      JsFault[] var2 = var0.getFaults();

      for(int var3 = 0; var3 < var2.length; ++var3) {
         JsFault var4 = var2[var3];
         if (var3 == 0) {
            var1 = "throws " + var4.getJsr109MappingFileExceptionClass();
         } else {
            var1 = var1 + ", " + var4.getJsr109MappingFileExceptionClass();
         }
      }

      return var1;
   }

   public static String getReturnString(String var0) {
      if (var0.indexOf(46) <= -1 && var0.indexOf(91) <= -1) {
         if (var0.equals("void")) {
            return "return;";
         } else if (var0.equals("boolean")) {
            return "return false;";
         } else if (!var0.equals("int") && !var0.equals("byte") && !var0.equals("short") && !var0.equals("long") && !var0.equals("double") && !var0.equals("float")) {
            return var0.equals("char") ? "return '0';" : "return null;";
         } else {
            return "return 0;";
         }
      } else {
         return "return null;";
      }
   }

   public static String getWsdlFileName(WsdlDefinitions var0, String var1) {
      File var2 = new File(var1);
      String var3;
      if (var2.exists()) {
         var3 = getWsdlRelativePath(var0, var1);
         if (var3.equals(var1)) {
            var3 = var2.getName();
         }
      } else {
         URL var4 = null;

         try {
            var4 = new URL(var1);
            var3 = getWsdlRelativePath(var0, var1);
            if (var3.equals(var1)) {
               var3 = var4.getPath();
               var3 = var3.substring(var3.lastIndexOf(47) + 1, var3.length());
            }
         } catch (MalformedURLException var6) {
            return var1;
         }
      }

      if (!var3.endsWith(".wsdl")) {
         if (var3.indexOf(46) != -1) {
            var3 = var3.substring(0, var3.indexOf(46));
         }

         var3 = var3 + ".wsdl";
      }

      return var3;
   }

   public static String getWsdlResolvedFile(String var0) {
      File var1 = new File(var0);
      String var2;
      if (var1.exists()) {
         var2 = var1.getName();
      } else {
         URL var3 = null;

         try {
            var3 = new URL(var0);
            var2 = var3.getPath();
         } catch (MalformedURLException var5) {
            return var0;
         }
      }

      return var2;
   }

   public static String getWsdlRelativePath(WsdlDefinitions var0, String var1) {
      List var2 = var0.getTypes().getSchemaListWithoutImport();
      int var3 = 0;
      String var4 = null;
      WsdlSchema var5 = null;
      Iterator var6 = var2.iterator();

      while(var6.hasNext()) {
         WsdlSchema var7 = (WsdlSchema)var6.next();
         List var8 = var7.getImports();
         Iterator var9 = var8.iterator();

         while(var9.hasNext()) {
            WsdlSchemaImport var10 = (WsdlSchemaImport)var9.next();
            String var11 = var10.getSchemaLocation();
            if (var10.isRelative() && var11.indexOf("..") != -1) {
               String[] var12 = var11.split("..");
               if (var12.length > var3) {
                  var3 = var12.length;
                  var4 = var11;
                  var5 = var7;
               }
            }
         }
      }

      if (var4 != null && var5 != null) {
         try {
            return findPathRelativeToRoot(var1, WsdlUtils.constructRelativeLocation(var4, var5.getLocationUrl()));
         } catch (WsdlException var13) {
            return var1;
         }
      } else {
         return var1;
      }
   }

   private static String findPathRelativeToRoot(String var0, String var1) {
      int var2;
      for(var2 = 0; var2 < var0.length() && var2 < var1.length() && var0.charAt(var2) == var1.charAt(var2); ++var2) {
      }

      return var0.substring(var2);
   }

   public static void logVerbose(Logger var0, String var1, Object var2) {
      if (var0 != null && var2 != null) {
         var0.log(EventLevel.VERBOSE, var1 + " \"" + var2 + "\"");
      }

   }

   public static boolean equalsNSOptional(QName var0, QName var1) {
      assert var0 != null;

      assert var1 != null;

      return StringUtil.isEmpty(var0.getNamespaceURI()) ? var0.getLocalPart().equals(var1.getLocalPart()) : var0.equals(var1);
   }
}
