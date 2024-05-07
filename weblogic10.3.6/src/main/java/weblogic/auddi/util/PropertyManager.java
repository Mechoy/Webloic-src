package weblogic.auddi.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import weblogic.auddi.uddi.FatalErrorException;
import weblogic.auddi.uddi.UDDIMessages;

public class PropertyManager {
   private static PropertyHolder m_perProps = null;
   private static PropertyHolder m_runProps = new PropertyHolderImpl();
   private static String m_propFile = null;

   private PropertyManager() {
   }

   public static String getPersistentProperty(String var0) {
      if (m_perProps == null) {
         throw new IllegalStateException("Properties have not been initialized.");
      } else {
         return m_perProps.getProperty(var0);
      }
   }

   public static void setPersistentProperty(String var0, String var1) {
      if (m_perProps == null) {
         throw new IllegalStateException("Properties have not been initialized.");
      } else {
         m_perProps.setProperty(var0, var1);
      }
   }

   public static void setPersistentProperty(String var0, String var1, boolean var2) {
      if (var2 || getPersistentProperty(var0) == null) {
         setPersistentProperty(var0, var1);
      }
   }

   public static String getRuntimeProperty(String var0) {
      return m_runProps.getProperty(var0);
   }

   public static void setRuntimeProperty(String var0, String var1) {
      setRuntimeProperty(var0, var1, true);
   }

   public static void setRuntimeProperty(String var0, String var1, boolean var2) {
      Logger.trace(">>>  PropertyManager.setRuntimeProperty(), " + var0 + " : " + var1 + "(" + (var2 ? "Replace" : "Don't replace") + ")");
      if (var2 || getRuntimeProperty(var0) == null) {
         m_runProps.setProperty(var0, var1);
      }
   }

   public static PropertyHolder getRuntimePropertyHolder() {
      return m_runProps;
   }

   public static PropertyHolder getPersistentPropertyHolder() {
      if (m_perProps == null) {
         throw new IllegalStateException("Properties have not been initialized.");
      } else {
         return m_perProps;
      }
   }

   public static void initialize(String var0) throws PropertyFileException {
      initialize(var0, (String)null);
   }

   public static void initialize(String var0, String var1) throws PropertyFileException {
      Logger.trace("+PropertyManager.initialize()");
      Logger.trace("propFile : " + var0);
      Logger.trace("propFileDefault : " + var1);
      if (m_perProps != null) {
         throw new IllegalStateException("Properties have already been initialized.");
      } else {
         if (var1 != null) {
            Properties var2 = new Properties();

            try {
               FileInputStream var3 = new FileInputStream(var1);
               var2.load(var3);
               var3.close();
            } catch (IOException var4) {
               throw new PropertyFileException("Did not find default property file.");
            }

            m_perProps = new PersistentPropertyHolder(var0, var2);
         } else {
            m_perProps = new PersistentPropertyHolder(var0);
         }

         m_propFile = var0;
         updateRuntimeProperties();
         Logger.trace("-PropertyManager.initialize()");
      }
   }

   private static void updateRuntimeProperties() {
      Enumeration var0 = m_perProps.propertyNames();

      while(var0.hasMoreElements()) {
         String var1 = (String)var0.nextElement();
         m_runProps.setProperty(var1, m_perProps.getProperty(var1));
      }

   }

   public static void load(InputStream var0) throws IOException {
      Properties var1 = new Properties();
      var1.load(var0);
      m_runProps.addProperties(var1, false);
   }

   public static void load(ResourceBundle var0) throws IOException {
      Properties var1 = new Properties();
      Enumeration var2 = var0.getKeys();

      while(var2.hasMoreElements()) {
         String var3 = (String)var2.nextElement();
         var1.setProperty(var3, var0.getString(var3));
      }

      m_runProps.addProperties(var1, false);
   }

   public static void loadAsFile(String var0) throws PropertyFileException {
      Logger.trace("+PropertyManager.loadAsFile()");
      Logger.debug("PropertyManager:  loading: " + var0);

      try {
         FileInputStream var1 = new FileInputStream(var0);
         load((InputStream)var1);
      } catch (IOException var2) {
         Logger.trace("-EXCEPTION(PropertyFileException) PropertyManager.loadAsFile()");
         throw new PropertyFileException(UDDIMessages.get("error.fatalError.fileNotfound.2", var0));
      }

      Logger.trace("-PropertyManager.loadAsFile()");
   }

   public static void loadAsResource(String var0) throws PropertyFileException {
      loadAsResource(var0, (String)null, false);
   }

   public static void loadAsResource(String var0, String var1) throws PropertyFileException {
      loadAsResource(var0, var1, false);
   }

   public static void loadAsResource(String var0, String var1, boolean var2) throws PropertyFileException {
      Logger.trace("+PropertyManager.loadAsResource()");
      Logger.debug("PropertyManager:  loading: " + var0 + " (prefix:" + var1 + ")");
      InputStream var3 = null;

      try {
         var3 = PropertyManager.class.getResourceAsStream(var0);
         if (var3 == null) {
            PropertyManager.class.getClassLoader();
            var3 = ClassLoader.getSystemResourceAsStream(var0);
         }

         if (var3 == null) {
            String var4 = var0.substring(1, var0.indexOf(".properties"));
            Logger.debug("finding resource bundle : " + var4);

            try {
               ResourceBundle var5 = Util.getResource(var4);
               Logger.debug("bundle : " + var5);
               load(var5);
               return;
            } catch (MissingResourceException var16) {
            }
         }

         if (var3 == null) {
            if (var1 != null) {
               var0 = var1 + var0;
               Logger.debug("didn't find it, loading : " + var0);
               var3 = PropertyManager.class.getResourceAsStream(var0);
               if (var3 == null) {
                  PropertyManager.class.getClassLoader();
                  var3 = ClassLoader.getSystemResourceAsStream(var0);
               }
            }

            if (var3 == null) {
               Logger.trace("-EXCEPTION(PropertyFileException) PropertyManager.loadAsResource()");
               throw new PropertyFileException(UDDIMessages.get("error.fatalError.fileNotfound.2", var0));
            }
         }

         load(var3);
      } catch (IOException var17) {
         Logger.trace("-EXCEPTION(PropertyFileException) PropertyManager.loadAsResource()");
         throw new PropertyFileException(UDDIMessages.get("error.fatalError.fileNotfound.2", var0));
      } finally {
         if (var3 != null) {
            try {
               var3.close();
            } catch (IOException var15) {
            }
         }

      }

      Logger.trace("-PropertyManager.loadAsResource()");
   }

   public static void backUpPropfile() throws FatalErrorException {
      Logger.trace("+PropertyManager.backUpPropfile()");
      if (m_propFile != null) {
         try {
            Logger.debug(">>> create back up file: " + m_propFile + ".booted");
            FileOutputStream var0 = new FileOutputStream(m_propFile + ".booted");
            FileInputStream var1 = new FileInputStream(m_propFile);
            Properties var2 = new Properties();
            var2.load(var1);
            var2.store(var0, "# backup of last booted");
            var0.close();
         } catch (IOException var3) {
            Logger.trace("-EXCEPTION(IOException) PropertyManager.backUpPropfile()");
            throw new FatalErrorException("Failed to back up property file: " + m_propFile, var3);
         }

         Logger.trace("-PropertyManager.backUpPropfile()");
      }
   }
}
