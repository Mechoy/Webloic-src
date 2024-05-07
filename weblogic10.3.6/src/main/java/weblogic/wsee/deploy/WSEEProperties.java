package weblogic.wsee.deploy;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import weblogic.wsee.util.Verbose;

public class WSEEProperties {
   public static final String NAME = "app-weblogic-webservices.xml";
   public static final String ASYNC_CP = "AsyncResponseBean.contextPath";
   public static final String ASYNC_SU = "AsyncResponseBean.serviceUri";
   private Properties props = new Properties();
   private static boolean verbose = Verbose.isVerbose(WSEEProperties.class);

   public WSEEProperties(ClassLoader var1) throws IOException {
      InputStream var2 = var1.getResourceAsStream("app-weblogic-webservices.xml");
      if (var2 != null) {
         this.load(var2);
      } else if (verbose) {
         Verbose.log((Object)("app-weblogic-webservices.xml was not found in the given classLoader: " + var1.toString()));
      }

   }

   public WSEEProperties(File var1) throws IOException {
      File var2 = this.buildFile(var1);
      if (var2.exists()) {
         this.load(new FileInputStream(var2));
      }

   }

   private void load(InputStream var1) throws IOException {
      try {
         this.props.loadFromXML(var1);
      } finally {
         if (var1 != null) {
            var1.close();
         }

      }

   }

   public void setProperty(String var1, String var2) {
      this.props.setProperty(var1, var2);
   }

   public String getProperty(String var1) {
      return this.props.getProperty(var1);
   }

   public void save(File var1) throws IOException {
      File var2 = this.buildFile(var1);
      if (var2.exists() && !var2.delete()) {
         throw new IOException("Unable to delete app-weblogic-webservices.xml");
      } else {
         var1.mkdirs();
         var2.createNewFile();
         FileOutputStream var3 = null;

         try {
            var3 = new FileOutputStream(var2);
            this.props.storeToXML(var3, "Created by WebLogic. Do not edit.");
         } finally {
            if (var3 != null) {
               var3.close();
            }

         }

      }
   }

   private File buildFile(File var1) {
      if (var1 == null) {
         throw new IllegalArgumentException("dir is null");
      } else if (!var1.isDirectory()) {
         throw new IllegalArgumentException("dir must be a directory");
      } else {
         return new File(var1, "app-weblogic-webservices.xml");
      }
   }

   public String getNormalizedProperty(String var1, String var2) {
      if (var1 == null) {
         throw new IllegalArgumentException("key must not be null");
      } else if (var2 == null) {
         return null;
      } else {
         if (verbose) {
            Verbose.log((Object)("Getting deployment property: " + var1));
         }

         String var3 = this.props.getProperty(var1, "");
         if (verbose) {
            Verbose.log((Object)(var1 + " value = " + var3));
         }

         return var2.replace("@" + var1 + "@", var3);
      }
   }
}
