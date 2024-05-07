package weblogic.wsee.jws.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Method;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import weblogic.utils.io.StreamUtils;
import weblogic.wsee.util.StringUtil;

public final class Util {
   public static final int DB_TYPE_UNKNOWN = 0;
   public static final int DB_TYPE_ORACLE = 1;
   public static final int DB_TYPE_CLOUDSCAPE = 2;
   public static final int DB_TYPE_POINTBASE = 3;
   public static final int DB_TYPE_SQLSERVER = 4;
   public static final int DB_TYPE_SYBASE = 5;
   public static final int DB_TYPE_DB2 = 6;
   private static Map _datasources = new Hashtable();
   private static char[] specialChars = new char[]{'&', '<', '>'};
   private static String[] predEntities = new String[]{"&amp;", "&lt;", "&gt;"};

   public static String cleanURL(URL var0) {
      if (var0 == null) {
         return "";
      } else {
         String var1 = var0.getUserInfo();
         return var1 != null ? var0.toString().replaceFirst(var1, "xxxxx:xxxxx") : var0.toString();
      }
   }

   public static Map parseQueryString(String var0) {
      HashMap var1 = new HashMap();
      boolean var2 = var0 == null;
      int var3 = 0;
      boolean var4 = false;
      int var5 = var0.length();

      while(true) {
         while(!var2) {
            int var9 = var0.indexOf(38, var3);
            if (var9 != 0) {
               if (var9 < 0) {
                  var9 = var0.length();
                  var2 = true;
               }

               int var6 = var0.indexOf(61, var3);
               if (var6 < 0 || var6 > var9) {
                  throw new RuntimeException("Missing value:" + var0);
               }

               String var7 = var0.substring(var3, var6);
               String var8 = var0.substring(var6 + 1, var9);
               var1.put(var7, var8);
               if (var9 >= var5) {
                  var2 = true;
               } else {
                  var3 = var9 + 1;
               }
            } else {
               ++var3;
            }
         }

         return var1;
      }
   }

   public static int computeArrayDimensions(Class var0) {
      Class var1 = var0;

      int var2;
      for(var2 = 0; var1.isArray(); var1 = var1.getComponentType()) {
         ++var2;
      }

      return var2;
   }

   public static String escapeJavaString(String var0) {
      if (var0.indexOf(92) < 0 && var0.indexOf(34) < 0) {
         return var0;
      } else {
         StringBuffer var1 = new StringBuffer();

         for(int var2 = 0; var2 < var0.length(); ++var2) {
            char var3 = var0.charAt(var2);
            if (var3 == '\\') {
               var1.append("\\\\");
            } else if (var3 == '"') {
               var1.append("\\\"");
            } else {
               var1.append(var3);
            }
         }

         return var1.toString();
      }
   }

   public static String encodeXML(String var0) {
      return encodeXML(var0, specialChars, predEntities);
   }

   public static String encodeXML(String var0, char[] var1, String[] var2) {
      if (var0 == null) {
         return null;
      } else {
         int var3 = var0.length();
         boolean var4 = false;

         int var6;
         for(int var5 = 0; var5 < var3; ++var5) {
            var6 = var0.charAt(var5);

            for(int var7 = 0; var7 < var1.length; ++var7) {
               if (var6 == var1[var7]) {
                  var4 = true;
                  break;
               }
            }

            if (var4) {
               break;
            }
         }

         if (!var4) {
            return var0;
         } else {
            StringBuffer var9 = new StringBuffer();

            for(var6 = 0; var6 < var3; ++var6) {
               char var10 = var0.charAt(var6);
               boolean var8 = false;

               int var11;
               for(var11 = 0; var11 < var1.length; ++var11) {
                  if (var10 == var1[var11]) {
                     var9.append(var2[var11]);
                     break;
                  }
               }

               if (var11 >= var1.length) {
                  var9.append(var10);
               }
            }

            return var9.toString();
         }
      }
   }

   public static String stdDateFormat(long var0) {
      SimpleDateFormat var2 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      return var2.format(new Date(var0));
   }

   public static String stdDateFormat() {
      SimpleDateFormat var0 = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
      return var0.format(new Date());
   }

   public static InputStream loadResourceStream(String var0) {
      InputStream var1 = null;
      ClassLoader var2 = Thread.currentThread().getContextClassLoader();
      var1 = var2.getResourceAsStream(var0);
      return var1;
   }

   public static byte[] loadResourceBinary(String var0) throws IOException {
      Object var1 = null;
      InputStream var2 = loadResourceStream(var0);
      BufferedInputStream var3 = new BufferedInputStream(var2);
      int var4 = var3.available();
      byte[] var5 = new byte[var4];
      if (var4 > 0) {
         var3.read(var5, 0, var4);
      }

      return var5;
   }

   public static String loadResourceString(String var0) throws IOException {
      String var1 = "";
      String var2 = null;
      InputStream var3 = loadResourceStream(var0);

      for(BufferedReader var4 = new BufferedReader(new InputStreamReader(var3)); (var2 = var4.readLine()) != null; var1 = var1 + var2 + "\n") {
      }

      return var1;
   }

   public static File createTempDir(String var0, String var1) throws IOException {
      File var2 = File.createTempFile(var0, var1);
      var2.delete();
      var2.mkdirs();
      var2.deleteOnExit();
      return var2;
   }

   public static void copyTextFile(String var0, String var1) throws IOException {
      FileReader var2 = new FileReader(var0);
      FileWriter var3 = new FileWriter(var1);

      int var4;
      while((var4 = var2.read()) != -1) {
         var3.write(var4);
      }

      var2.close();
      var3.close();
   }

   public static boolean copyStream(InputStream var0, File var1) {
      FileOutputStream var2 = null;

      boolean var4;
      try {
         var2 = new FileOutputStream(var1);
         StreamUtils.writeTo(var0, var2);
         var2.close();
         boolean var3 = true;
         return var3;
      } catch (IOException var16) {
         try {
            var2.close();
         } catch (Exception var15) {
         }

         var2 = null;
         var1.delete();
         var4 = false;
      } finally {
         if (var2 != null) {
            try {
               var2.close();
            } catch (Exception var14) {
            }
         }

      }

      return var4;
   }

   public static void copyReader(Reader var0, Writer var1) throws IOException {
      short var2 = 8192;
      char[] var3 = new char[var2];
      boolean var4 = false;

      int var5;
      while((var5 = var0.read(var3)) != -1) {
         var1.write(var3, 0, var5);
      }

   }

   public static void copyBinaryFile(File var0, File var1) throws IOException {
      BufferedInputStream var2 = new BufferedInputStream(new FileInputStream(var0));
      BufferedOutputStream var3 = new BufferedOutputStream(new FileOutputStream(var1));
      StreamUtils.writeTo(var2, var3);
      var2.close();
      var3.close();
   }

   public static String replaceDollarSign(String var0) {
      return var0 == null ? "" : var0.replace('$', '.');
   }

   public static String arrayToString(Object[] var0) {
      StringBuffer var1 = new StringBuffer();
      if (var0 == null) {
         var1.append("null");
      } else {
         for(int var2 = 0; var2 < var0.length; ++var2) {
            var1.append("[" + var2 + "]=" + var0[var2]);
         }
      }

      return var1.toString();
   }

   public static void recursiveCopy(File var0, File var1) throws IOException {
      assert var1.exists() : "Dest " + var1 + " does not exist!";

      assert var1.isDirectory() : "Dest " + var1 + " must be a dir!";

      if (var0.exists()) {
         var0 = var0.getCanonicalFile();
         String var2 = var0.getName();
         File var3 = new File(var1, var2);
         if (var0.isFile()) {
            copyBinaryFile(var0, var3);
         } else if (var0.isDirectory()) {
            var3.mkdirs();
            File[] var4 = var0.listFiles();

            for(int var5 = 0; var5 < var4.length; ++var5) {
               recursiveCopy(var4[var5], var3);
            }
         } else {
            assert false : "File " + var0 + "is neither a normal file or dir!";
         }

      }
   }

   public static void recursiveDelete(File var0) throws IOException {
      if (var0.exists()) {
         if (var0.isDirectory()) {
            String[] var1 = var0.list();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               recursiveDelete(new File(var0, var1[var2]));
            }
         }

         var0.delete();
      }
   }

   public static void recursivePrint(File var0) throws IOException {
      if (var0.exists()) {
         System.out.println(var0.getPath());
         if (var0.isDirectory()) {
            String[] var1 = var0.list();

            for(int var2 = 0; var2 < var1.length; ++var2) {
               recursivePrint(new File(var0, var1[var2]));
            }
         }

      }
   }

   public static Class getObjectTypeFromPrimitiveType(Class var0) {
      if (var0.equals(Boolean.TYPE)) {
         return Boolean.class;
      } else if (var0.equals(Character.TYPE)) {
         return Character.class;
      } else if (var0.equals(Byte.TYPE)) {
         return Byte.class;
      } else if (var0.equals(Short.TYPE)) {
         return Short.class;
      } else if (var0.equals(Integer.TYPE)) {
         return Integer.class;
      } else if (var0.equals(Long.TYPE)) {
         return Long.class;
      } else if (var0.equals(Float.TYPE)) {
         return Float.class;
      } else {
         return var0.equals(Double.TYPE) ? Double.class : null;
      }
   }

   public static Class getPrimitiveTypeFromObjectType(Class var0) {
      if (var0.equals(Boolean.class)) {
         return Boolean.TYPE;
      } else if (var0.equals(Character.class)) {
         return Character.TYPE;
      } else if (var0.equals(Byte.class)) {
         return Byte.TYPE;
      } else if (var0.equals(Short.class)) {
         return Short.TYPE;
      } else if (var0.equals(Integer.class)) {
         return Integer.TYPE;
      } else if (var0.equals(Long.class)) {
         return Long.TYPE;
      } else if (var0.equals(Float.class)) {
         return Float.TYPE;
      } else {
         return var0.equals(Double.class) ? Double.TYPE : null;
      }
   }

   public static Connection getConnection(String var0) throws NamingException, SQLException {
      DataSource var1 = (DataSource)_datasources.get(var0);
      if (var1 == null) {
         InitialContext var2 = null;

         try {
            var2 = getInitialContext();
            var1 = (DataSource)var2.lookup(var0);
            _datasources.put(var0, var1);
         } finally {
            if (var2 != null) {
               var2.close();
            }

         }
      }

      Connection var6 = var1.getConnection();
      return var6;
   }

   private static InitialContext getInitialContext() throws NamingException {
      Hashtable var0 = new Hashtable();
      var0.put("java.naming.factory.initial", "weblogic.jndi.WLInitialContextFactory");
      return new InitialContext(var0);
   }

   public static int getDBType(Connection var0) throws SQLException {
      byte var1 = 0;
      DatabaseMetaData var2 = var0.getMetaData();
      String var3 = var2.getDatabaseProductName();
      if (var3 == null) {
         var3 = "<not found>";
      }

      String var4 = getDBDriverName(var2);
      String var5 = var3.toLowerCase(Locale.ENGLISH);
      String var6 = var4.toLowerCase(Locale.ENGLISH);
      if (isOracle(var5, var6)) {
         var1 = 1;
      } else if (isPointbase(var5, var6)) {
         var1 = 3;
      } else if (isMsSqlServer(var5, var6)) {
         var1 = 4;
      } else if (isSybase(var5, var6)) {
         var1 = 5;
      } else if (isDB2(var5, var6)) {
         var1 = 6;
      }

      return var1;
   }

   private static String getDBDriverName(DatabaseMetaData var0) throws SQLException {
      String var1 = Config.getProperty("weblogic.jws.dbtype");
      if (var1 == null && var0 != null) {
         var1 = var0.getDriverName();
      }

      if (var1 == null) {
         var1 = "<not found>";
      }

      return var1;
   }

   private static boolean isOracle(String var0, String var1) {
      return var0.indexOf("oracle") != -1 || var1.indexOf("oracle") != -1;
   }

   private static boolean isPointbase(String var0, String var1) {
      return var0.indexOf("pointbase") != -1 || var1.indexOf("pointbase") != -1;
   }

   private static boolean isMsSqlServer(String var0, String var1) {
      return (var0.indexOf("sql server") != -1 || var0.indexOf("sqlserver") != -1) && var1.indexOf("sybase") == -1;
   }

   private static boolean isSybase(String var0, String var1) {
      return var0.indexOf("sybase") != -1 || var0.indexOf("jconnect") != -1 || var0.indexOf("sql server") != -1 && var1.indexOf("sybase") != -1;
   }

   private static boolean isDB2(String var0, String var1) {
      return var0.indexOf("db2") != -1 || var1.indexOf("db2") != -1;
   }

   public static boolean isValidURI(String var0) {
      try {
         new URI(var0);
         return true;
      } catch (URISyntaxException var2) {
         return false;
      }
   }

   public static Class<?> getEIClass(Class<?> var0) {
      Class var1 = null;
      WebService var2 = (WebService)var0.getAnnotation(WebService.class);
      if (var2 != null) {
         String var3 = var2.endpointInterface();
         if (!StringUtil.isEmpty(var3)) {
            try {
               var1 = var0.getClassLoader().loadClass(var3);
            } catch (ClassNotFoundException var5) {
               var1 = null;
            }
         }
      }

      return var1;
   }

   public static boolean isWebMethod(Method var0, Class<?> var1) {
      Method var2 = var0;
      if (var1 != null) {
         try {
            var2 = var1.getMethod(var0.getName(), var0.getParameterTypes());
         } catch (Exception var4) {
            return false;
         }
      }

      return var2.isAnnotationPresent(WebMethod.class);
   }

   public static class TypeError extends Exception {
      public String description;
      public int offset;

      public TypeError(String var1, int var2) {
         this.description = var1;
         this.offset = var2;
      }
   }

   public static class Type {
      public String packageName;
      public String className;
      public int arrayDepth;
      public boolean isBuiltin;

      public String getQualifiedName() {
         return this.packageName.equals("") ? this.className : this.packageName + "." + this.className;
      }
   }
}
