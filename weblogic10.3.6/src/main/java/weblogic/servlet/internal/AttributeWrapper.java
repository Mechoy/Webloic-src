package weblogic.servlet.internal;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;
import weblogic.common.internal.PassivationUtils;
import weblogic.servlet.ReferencedAttribute;
import weblogic.utils.classloaders.ClassLoaderUtils;
import weblogic.utils.classloaders.GenericClassLoader;

public class AttributeWrapper implements Serializable {
   static final long serialVersionUID = 309266816418685703L;
   protected Object object;
   protected transient byte[] serializedObject;
   private final String intialContextPath;
   private transient int lastClassloaderHash;
   private transient boolean isLoadedByBootStrap;
   private transient boolean isWebLogicClassLoader;
   private transient int objectClassLoaderHash;
   private transient boolean forceToConvert;
   private transient long currentChecksum;
   private transient long previousChecksum;

   public AttributeWrapper(Object var1) {
      this(var1, (WebAppServletContext)null);
   }

   AttributeWrapper(Object var1, WebAppServletContext var2) {
      this.lastClassloaderHash = 0;
      this.isLoadedByBootStrap = false;
      this.isWebLogicClassLoader = false;
      this.objectClassLoaderHash = 0;
      this.currentChecksum = -1L;
      this.previousChecksum = -1L;
      this.object = var1;
      this.intialContextPath = var2 == null ? null : var2.getContextPath();
      this.saveObjectClassLoaderInfo(var1);
   }

   public final Object getObject() throws ClassNotFoundException, IOException {
      return this.getObject(true, (WebAppServletContext)null);
   }

   public Object getObject(boolean var1) throws ClassNotFoundException, IOException {
      return this.getObject(var1, (WebAppServletContext)null);
   }

   Object getObject(WebAppServletContext var1) throws ClassNotFoundException, IOException {
      return this.getObject(true, var1);
   }

   protected Object getObject(boolean var1, WebAppServletContext var2) throws ClassNotFoundException, IOException {
      synchronized(this) {
         if (!var1) {
            return this.object;
         } else if (this.serializedObject != null) {
            return this.convertBytesToObject(this.serializedObject);
         } else if (needToWrap(this.object) && !this.isOptimisticSerialization(var2)) {
            ClassLoader var4 = Thread.currentThread().getContextClassLoader();
            if (this.needToConvert(var4, var2)) {
               this.object = PassivationUtils.copy(this.object);
               this.saveObjectClassLoaderInfo(this.object);
               this.forceToConvert = false;
            }

            return this.object;
         } else {
            return this.object;
         }
      }
   }

   protected boolean isOptimisticSerialization(WebAppServletContext var1) {
      return this.intialContextPath != null && var1 != null && var1.getConfigManager().isOptimisticSerialization() && !this.intialContextPath.equals(var1.getContextPath());
   }

   protected Object convertBytesToObject(byte[] var1) throws ClassNotFoundException, IOException {
      this.object = PassivationUtils.toObject(var1);
      this.saveObjectClassLoaderInfo(this.object);
      this.serializedObject = null;
      return this.object;
   }

   static boolean needToWrap(Object var0) {
      return !(var0 instanceof String) && !(var0 instanceof Number) && !(var0 instanceof ReferencedAttribute);
   }

   public void setForceToConvert(boolean var1) {
      this.forceToConvert = var1;
   }

   public synchronized void convertToBytes() throws IOException {
      this.serializedObject = PassivationUtils.toByteArray(this.object);
   }

   protected boolean needToConvert(ClassLoader var1, WebAppServletContext var2) {
      this.reInitClassLoaderInfo();
      if (this.isLoadedByBootStrap && (this.object instanceof Iterable || this.object instanceof Map) && var2 != null && !this.intialContextPath.equals(var2.getContextPath())) {
         return true;
      } else if (this.isLoadedByBootStrap) {
         return false;
      } else if (this.forceToConvert) {
         if (HTTPDebugLogger.isEnabled()) {
            HTTPDebugLogger.debug("Attribute value is forced to be converted at lookup time with the appropriate ClassLoader");
         }

         return true;
      } else {
         int var3 = var1.hashCode();
         if (this.isSameThreadClassLoader(var3)) {
            return false;
         } else if (this.objectClassLoaderHash == var3) {
            return false;
         } else if (!this.isWebLogicClassLoader) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("WLS ignores to convert attribute value.Because, attribute value is impossible to be converted at lookup time with the appropriate ClassLoader because initial ClassLoader isn't Weblogic classloaders. To convert attribute value, initial ClassLoader should be WebLogic ClassLoader. \nCurrent classloader: " + var1 + "\nInitial context path: " + this.intialContextPath + ", current context path: " + (var2 == null ? "null" : var2.getContextPath()) + "\nObject class: " + this.object.getClass());
            }

            return false;
         } else if (!ClassLoaderUtils.isChild(this.objectClassLoaderHash, var1)) {
            if (HTTPDebugLogger.isEnabled()) {
               HTTPDebugLogger.debug("Attribute value is needed to be converted at lookup time with the appropriate ClassLoader because current ClassLoader is different with initial ClassLoader and is not child of initial ClassLoader.\nCurrent classloader: " + var1 + "\nInitial context path: " + this.intialContextPath + ", current context path: " + (var2 == null ? "null" : var2.getContextPath()) + "\nObject class: " + this.object.getClass());
            }

            return true;
         } else {
            return false;
         }
      }
   }

   protected void reInitClassLoaderInfo() {
      if (this.objectClassLoaderHash == 0) {
         this.saveObjectClassLoaderInfo(this.object);
      }

   }

   protected boolean isSameThreadClassLoader(int var1) {
      if (this.lastClassloaderHash == var1) {
         return true;
      } else {
         this.lastClassloaderHash = var1;
         return false;
      }
   }

   private void saveObjectClassLoaderInfo(Object var1) {
      ClassLoader var2 = var1.getClass().getClassLoader();
      if (var2 == null) {
         this.isLoadedByBootStrap = true;
         this.isWebLogicClassLoader = false;
      } else {
         this.objectClassLoaderHash = var2.hashCode();
         this.isWebLogicClassLoader = var2 instanceof GenericClassLoader;
      }
   }

   public void setCheckSum(long var1) {
      this.currentChecksum = var1;
   }

   public long getCheckSum() {
      return this.currentChecksum;
   }

   public void setPreviousChecksum(long var1) {
      this.previousChecksum = var1;
   }

   public long getPreviousChecksum() {
      return this.previousChecksum;
   }
}
