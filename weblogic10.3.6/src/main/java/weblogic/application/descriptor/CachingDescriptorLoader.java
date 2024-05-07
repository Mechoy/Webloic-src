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

public abstract class CachingDescriptorLoader extends AbstractDescriptorLoader {
   private static final boolean isServer = Kernel.isServer();
   private static final DescriptorCache cache = DescriptorCache.getInstance();
   private static final boolean DEBUG = false;
   private static final boolean useCaching = Boolean.getBoolean("weblogic.EnableDescriptorCache");
   private final ApplicationAccess access;

   public CachingDescriptorLoader(VirtualJarFile var1, File var2, DeploymentPlanBean var3, String var4) {
      super(var1, var2, var3, var4);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader(File var1, File var2, DeploymentPlanBean var3, String var4) {
      super(var1, var2, var3, var4);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader(GenericClassLoader var1) {
      super(var1);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader(GenericClassLoader var1, File var2, DeploymentPlanBean var3, String var4) {
      super(var1, var2, var3, var4);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader(DescriptorManager var1, GenericClassLoader var2, File var3, DeploymentPlanBean var4, String var5) {
      super(var1, var2, var3, var4, var5);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader(DescriptorManager var1, GenericClassLoader var2) {
      super(var1, var2);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader(VirtualJarFile var1) {
      super(var1);
      this.access = isServer ? ApplicationAccess.getApplicationAccess() : null;
   }

   public CachingDescriptorLoader(File var1) {
      super(var1);
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

   private Descriptor superCreateDescriptor(InputStream var1) throws IOException, XMLStreamException {
      return super.createDescriptor(var1);
   }

   private Descriptor superCreateDescriptor(InputStream var1, boolean var2) throws IOException, XMLStreamException {
      return super.createDescriptor(var1, var2);
   }

   protected Descriptor createDescriptor(InputStream var1) throws IOException, XMLStreamException {
      if (isServer && this.getDeploymentPlan() == null) {
         File var2 = this.findCacheDir();
         if (var2 == null) {
            return super.createDescriptor(var1);
         } else {
            IOHelperImpl var3 = new IOHelperImpl(useCaching);
            var3.setValidate(cache.hasChanged(var2, var3));

            DescriptorBean var4;
            try {
               var4 = (DescriptorBean)cache.parseXML(var2, var3);
            } catch (IOException var7) {
               cache.removeCRC(var2);
               throw var7;
            } catch (XMLStreamException var8) {
               cache.removeCRC(var2);
               throw var8;
            } catch (Throwable var9) {
               cache.removeCRC(var2);
               IOException var6 = new IOException(var9.toString());
               var6.initCause(var9);
               throw var6;
            }

            return var4 == null ? null : var4.getDescriptor();
         }
      } else {
         return super.createDescriptor(var1);
      }
   }

   private class IOHelperImpl implements DescriptorCache.IOHelper {
      private boolean useCaching = false;
      private boolean validate = true;

      public IOHelperImpl(boolean var2) {
         this.useCaching = var2;
      }

      public InputStream openInputStream() throws IOException {
         return CachingDescriptorLoader.this.getInputStream();
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
         DescriptorImpl var2 = DescriptorImpl.beginConstruction(false, CachingDescriptorLoader.READONLY_DESCRIPTOR_MANAGER_SINGLETON.instance, (DescriptorCreationListener)null, (BeanCreationInterceptor)null);
         DescriptorBean var3 = null;

         try {
            var3 = this.readCachedDescriptor(var1);
         } finally {
            DescriptorImpl.endConstruction(var3);
         }

         return var3;
      }

      public Object parseXML(InputStream var1) throws IOException, XMLStreamException {
         Descriptor var2 = CachingDescriptorLoader.this.superCreateDescriptor(var1, this.validate);
         return var2.getRootBean();
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
