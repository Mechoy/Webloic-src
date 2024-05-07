package weblogic.auddi.util.uuid;

import weblogic.auddi.util.Logger;
import weblogic.auddi.util.PropertyManager;

public abstract class UUIDGeneratorFactory {
   private static String s_uuidClassName = null;
   private static Class s_uuidClass = null;
   private static final String UUID_GEN_CLASS_NAME_PROPERTY = "uuidgen.class";
   private static final String DEFAULT_UUID_GEN_CLASS_NAME = "weblogic.auddi.util.uuid.JavaUUIDGenerator";
   public static final String UUID_ENV_PREFIX = "uuidgen.";
   private static UUIDGenerator s_instance = null;

   public static void reset() {
      s_uuidClassName = null;
      s_uuidClass = null;
      s_instance = null;
   }

   private UUIDGeneratorFactory() {
   }

   public static UUIDGenerator getGenerator() throws UUIDException {
      Logger.trace("+UUIDGeneratorFactory.getUUIDGenerator()");
      if (s_instance == null) {
         if (s_uuidClass == null) {
            s_uuidClass = loadClass();
         }

         try {
            s_instance = (UUIDGenerator)s_uuidClass.newInstance();
         } catch (InstantiationException var1) {
            Logger.error("Exception while attempting to instantiate subclass of UUIDGenerator: " + s_uuidClassName);
            var1.printStackTrace();
            Logger.trace("-UUIDGeneratorFactory.getUUIDGenerator()");
            throw new UUIDException(var1);
         } catch (IllegalAccessException var2) {
            Logger.error("Exception while attempting to instantiate subclass of UUIDGenerator: " + s_uuidClassName);
            var2.printStackTrace();
            Logger.trace("-UUIDGeneratorFactory.getUUIDGenerator()");
            throw new UUIDException(var2);
         }
      }

      Logger.trace("-UUIDGeneratorFactory.getUUIDGenerator()");
      return s_instance;
   }

   private static synchronized Class loadClass() throws UUIDException {
      Logger.trace("+UUIDGeneratorFactory.loadClass()");
      s_uuidClassName = PropertyManager.getRuntimeProperty("uuidgen.class");
      Class var0 = null;
      if (s_uuidClassName == null) {
         s_uuidClassName = "weblogic.auddi.util.uuid.JavaUUIDGenerator";
      }

      try {
         var0 = Class.forName(s_uuidClassName);
      } catch (ClassNotFoundException var2) {
         Logger.error("Subclass of UUIDGenerator not found in classpath: " + s_uuidClassName + " not found.");
         var2.printStackTrace();
         Logger.trace("-UUIDGeneratorFactory.loadClass()");
         throw new UUIDException(var2);
      }

      Logger.trace("-UUIDGeneratorFactory.loadClass()");
      return var0;
   }
}
