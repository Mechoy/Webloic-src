package weblogic.application.descriptor;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationAccess;
import weblogic.application.ApplicationContextInternal;
import weblogic.descriptor.BeanCreationInterceptor;
import weblogic.descriptor.Descriptor;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorCache;
import weblogic.descriptor.DescriptorCreationListener;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.internal.DescriptorImpl;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.kernel.Kernel;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;

public abstract class CachingDescriptorLoader2 extends AbstractDescriptorLoader2 {
   private static final boolean isServer = Kernel.isServer();
   private static final DescriptorCache cache = DescriptorCache.getInstance();
   private static final boolean DEBUG = false;
   private static final boolean useCaching = Boolean.getBoolean("weblogic.EnableDescriptorCache");
   private final ApplicationAccess access;

   public CachingDescriptorLoader2(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super(var1, var2, var3, var4, var5);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader2(File var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super(var1, var2, var3, var4, var5);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader2(GenericClassLoader var1, String var2) {
      super(var1, var2);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader2(GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4, String var5) {
      super(var1, var2, var3, var4, var5);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader2(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5, String var6) {
      super(var1, var2, var3, var4, var5, var6);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader2(DescriptorManager var1, GenericClassLoader var2, String var3) {
      super(var1, var2, var3);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader2(VirtualJarFile var1, String var2) {
      super(var1, var2);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader2(File var1, String var2) {
      super(var1, var2);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   protected File findCacheDir() {
      ApplicationContextInternal var1 = this.access.getCurrentApplicationContext();
      return var1 == null ? null : this.findCacheDir(var1.getDescriptorCacheDir());
   }

   protected File findCacheDir(File var1) {
      String var2 = this.getModuleName();
      if (var2 == null) {
         var2 = "";
      }

      return new File(var1, var2 + File.separator + this.getDocumentURI());
   }

   protected Descriptor createDescriptor() throws IOException, XMLStreamException {
      if (isServer && this.getDeploymentPlan() == null) {
         File var9 = this.findCacheDir();
         if (var9 == null) {
            DescriptorBean var10 = super.loadDescriptorBean();
            return var10 == null ? null : var10.getDescriptor();
         } else {
            IOHelperImpl var2 = new IOHelperImpl(useCaching);
            var2.setValidate(cache.hasChanged(var9, var2));

            DescriptorBean var3;
            try {
               var3 = (DescriptorBean)cache.parseXML(var9, var2);
            } catch (IOException var6) {
               cache.removeCRC(var9);
               throw var6;
            } catch (XMLStreamException var7) {
               cache.removeCRC(var9);
               throw var7;
            } catch (Throwable var8) {
               cache.removeCRC(var9);
               IOException var5 = new IOException(var8.toString());
               var5.initCause(var8);
               throw var5;
            }

            return var3 == null ? null : var3.getDescriptor();
         }
      } else {
         DescriptorBean var1 = this.loadDescriptorBean();
         return var1 == null ? null : var1.getDescriptor();
      }
   }

   private class IOHelperImpl implements DescriptorCache.IOHelper {
      private boolean useCaching = false;
      private boolean validate = true;

      public IOHelperImpl(boolean var2) {
         this.useCaching = var2;
      }

      public InputStream openInputStream() throws IOException {
         return CachingDescriptorLoader2.this.getInputStream();
      }

      private DescriptorBean readCachedDescriptor(File var1) throws IOException {
         ObjectInputStream var2 = null;

         DescriptorBean var3;
         try {
            var2 = new ObjectInputStream(new FileInputStream(var1));
            var3 = (DescriptorBean)var2.readObject();
         } catch (ClassNotFoundException var12) {
            throw (IOException)(new IOException(var12.getMessage())).initCause(var12);
         } finally {
            if (var2 != null) {
               try {
                  var2.close();
               } catch (IOException var11) {
               }
            }

         }

         return var3;
      }

      public Object readCachedBean(File var1) throws IOException {
         DescriptorImpl var2 = DescriptorImpl.beginConstruction(false, CachingDescriptorLoader2.READONLY_DESCRIPTOR_MANAGER_SINGLETON.instance, (DescriptorCreationListener)null, (BeanCreationInterceptor)null);
         DescriptorBean var3 = null;

         try {
            var3 = this.readCachedDescriptor(var1);
         } finally {
            DescriptorImpl.endConstruction(var3);
         }

         return var3;
      }

      public Object parseXML(InputStream var1) throws IOException, XMLStreamException {
         return CachingDescriptorLoader2.this.loadDescriptorBean();
      }

      public boolean useCaching() {
         return this.useCaching;
      }

      protected void setValidate(boolean var1) {
         this.validate = var1;
      }
   }

   private static class READONLY_DESCRIPTOR_MANAGER_SINGLETON {
      static DescriptorManager instance = new DescriptorManager();
   }
}
