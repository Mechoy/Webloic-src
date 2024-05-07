package weblogic.ejb.container.dd.xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.util.Locale;
import weblogic.ejb.container.EJBLogger;
import weblogic.ejb.container.deployer.IncomprehensibleMethodSignatureException;
import weblogic.logging.Loggable;
import weblogic.management.descriptors.Encoding;
import weblogic.utils.AssertionError;
import weblogic.utils.StringUtils;
import weblogic.utils.io.XMLDeclaration;

public final class DDUtils {
   private static final boolean debug = false;

   public static String getMethodName(String var0) throws IncomprehensibleMethodSignatureException {
      int var2 = var0.indexOf(40);
      int var3 = var0.indexOf(41);
      String var1;
      Loggable var4;
      if (var2 == -1) {
         if (var3 != -1) {
            var4 = EJBLogger.loginvalidMethodSignatureLoggable(var0);
            throw new IncomprehensibleMethodSignatureException(var4.getMessage());
         }

         var1 = var0;
      } else {
         if (var3 == -1) {
            var4 = EJBLogger.loginvalidMethodSignatureLoggable(var0);
            throw new IncomprehensibleMethodSignatureException(var4.getMessage());
         }

         var1 = var0.substring(0, var2);
      }

      return var1;
   }

   public static String[] getMethodParams(String var0) throws IncomprehensibleMethodSignatureException {
      int var2 = var0.indexOf(40);
      int var3 = var0.indexOf(41);
      String[] var1;
      if (var2 == -1 && var3 == -1) {
         var1 = null;
      } else {
         if (var2 == -1 || var3 == -1) {
            Loggable var5 = EJBLogger.loginvalidMethodSignatureLoggable(var0);
            throw new IncomprehensibleMethodSignatureException(var5.getMessage());
         }

         var1 = StringUtils.splitCompletely(var0.substring(var2 + 1, var3), ",", false);

         for(int var4 = 0; var4 < var1.length; ++var4) {
            var1[var4] = var1[var4].trim();
         }
      }

      return var1;
   }

   public static String getMethodSignature(String var0, String[] var1) {
      if (var1 != null && !"*".equals(var0)) {
         String[] var2 = new String[var1.length];

         for(int var3 = 0; var3 < var1.length; ++var3) {
            var2[var3] = makeMethodParam(var1[var3]);
         }

         return var0 + "(" + StringUtils.join(var2, ",") + ")";
      } else {
         return var0;
      }
   }

   public static String getMethodSignature(Method var0) {
      String var1 = var0.getName();
      Class[] var2 = var0.getParameterTypes();
      String[] var3 = new String[var2.length];

      for(int var4 = 0; var4 < var2.length; ++var4) {
         var3[var4] = var2[var4].getCanonicalName();
      }

      return getMethodSignature(var1, var3);
   }

   public static String getMethodSignature(String var0, Class[] var1) {
      String[] var2 = new String[var1.length];

      for(int var3 = 0; var3 < var1.length; ++var3) {
         var2[var3] = var1[var3].getCanonicalName();
      }

      return getMethodSignature(var0, var2);
   }

   public static String getEjbHomeMethodSignature(Method var0) {
      String var1 = var0.getName();
      String var2 = "ejbHome" + var1.substring(0, 1).toUpperCase(Locale.ENGLISH);
      if (var1.length() > 1) {
         var2 = var2 + var1.substring(1);
      }

      return getMethodSignature(var2, var0.getParameterTypes());
   }

   static String makeMethodParam(String var0) {
      if (!var0.endsWith("[]")) {
         return var0;
      } else {
         StringBuffer var1 = new StringBuffer();
         int var2 = var0.length() - 2;

         while(true) {
            var1.append('[');
            if (var2 < 2 || var0.charAt(var2 - 2) != '[' || var0.charAt(var2 - 1) != ']') {
               return var1.append(arrayClassName(var0.substring(0, var2).trim())).toString();
            }

            var2 -= 2;
         }
      }
   }

   private static String arrayClassName(String var0) {
      if (var0.equals("byte")) {
         return "B";
      } else if (var0.equals("char")) {
         return "C";
      } else if (var0.equals("int")) {
         return "I";
      } else if (var0.equals("long")) {
         return "J";
      } else if (var0.equals("float")) {
         return "F";
      } else if (var0.equals("double")) {
         return "D";
      } else if (var0.equals("short")) {
         return "S";
      } else {
         return var0.equals("boolean") ? "Z" : "L" + var0 + ";";
      }
   }

   public static int isoStringToInt(String var0) throws IllegalArgumentException {
      if (var0.equalsIgnoreCase("TransactionSerializable")) {
         return 8;
      } else if (var0.equalsIgnoreCase("TransactionRepeatableRead")) {
         return 4;
      } else if (var0.equalsIgnoreCase("TransactionReadCommitted")) {
         return 2;
      } else if (var0.equalsIgnoreCase("TransactionReadCommittedForUpdate")) {
         return 2;
      } else if (var0.equalsIgnoreCase("TransactionReadCommittedForUpdateNoWait")) {
         return 2;
      } else if (var0.equalsIgnoreCase("TransactionReadUncommitted")) {
         return 1;
      } else if (var0.equalsIgnoreCase("TransactionNone")) {
         return 0;
      } else if (var0.equalsIgnoreCase("TRANSACTION_SERIALIZABLE")) {
         return 8;
      } else if (var0.equalsIgnoreCase("TRANSACTION_REPEATABLE_READ")) {
         return 4;
      } else if (var0.equalsIgnoreCase("TRANSACTION_READ_COMMITTED")) {
         return 2;
      } else if (var0.equalsIgnoreCase("TRANSACTION_READ_COMMITTED_FOR_UPDATE")) {
         return 2;
      } else if (var0.equalsIgnoreCase("TRANSACTION_READ_COMMITTED_FOR_UPDATE_NO_WAIT")) {
         return 2;
      } else if (var0.equalsIgnoreCase("TRANSACTION_READ_UNCOMMITTED")) {
         return 1;
      } else if (var0.equalsIgnoreCase("TRANSACTION_NONE")) {
         return 0;
      } else {
         throw new IllegalArgumentException("Bad isolation level: " + var0);
      }
   }

   public static int concurrencyStringToInt(String var0) {
      boolean var1 = true;
      byte var2;
      if ("Exclusive".equalsIgnoreCase(var0)) {
         var2 = 1;
      } else if ("database".equalsIgnoreCase(var0)) {
         var2 = 2;
      } else if ("ReadOnlyExclusive".equalsIgnoreCase(var0)) {
         var2 = 4;
      } else if ("ReadOnly".equalsIgnoreCase(var0)) {
         var2 = 5;
      } else {
         if (!"Optimistic".equalsIgnoreCase(var0)) {
            throw new AssertionError("Bad concurrency setting: " + var0);
         }

         var2 = 6;
      }

      return var2;
   }

   public static String getXMLEncoding(InputStream var0, String var1) throws IOException {
      String var2 = null;
      var0.mark(1048576);

      try {
         XMLDeclaration var3 = new XMLDeclaration();
         var3.parse(var0);
         var2 = var3.getEncoding();
      } finally {
         var0.reset();
      }

      validateEncoding(var2, var1);
      return var2;
   }

   private static void validateEncoding(String var0, String var1) throws IOException {
      if (var0 != null && Encoding.getIANA2JavaMapping(var0) == null && Encoding.getJava2IANAMapping(var0) == null && !Charset.isSupported(var0)) {
         throw new UnsupportedEncodingException(var1 + " uses invalid encoding");
      }
   }
}
