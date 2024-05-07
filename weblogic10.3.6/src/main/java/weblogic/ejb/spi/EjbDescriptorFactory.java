package weblogic.ejb.spi;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public final class EjbDescriptorFactory {
   private static EjbDescriptorReader reader;

   public static EjbDescriptorReader getEjbDescriptorReader() {
      if (reader == null) {
         try {
            reader = (EjbDescriptorReader)Class.forName("weblogic.ejb.container.dd.xml.EjbDescriptorReaderImpl").newInstance();
         } catch (Exception var1) {
            throw new AssertionError("Couldn't load DDUtils: " + var1);
         }
      }

      return reader;
   }

   public static EjbDescriptorBean createDescriptorFromJarFile(JarFile var0) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return getEjbDescriptorReader().createDescriptorFromJarFile(var0);
   }

   public static EjbDescriptorBean createDescriptorFromJarFile(JarFile var0, File var1) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return getEjbDescriptorReader().createDescriptorFromJarFile(var0, var1);
   }

   public static EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var0) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return getEjbDescriptorReader().createDescriptorFromJarFile(var0);
   }

   public static EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var0, File var1, File var2, DeploymentPlanBean var3, String var4, String var5) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return getEjbDescriptorReader().createDescriptorFromJarFile(var0, var1, var2, var3, var4, var5);
   }

   public static EjbDescriptorBean createReadOnlyDescriptorFromJarFile(VirtualJarFile var0, GenericClassLoader var1) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return getEjbDescriptorReader().createReadOnlyDescriptorFromJarFile(var0, var1);
   }

   public static EjbDescriptorBean createReadOnlyDescriptorFromJarFile(VirtualJarFile var0, File var1, File var2, DeploymentPlanBean var3, String var4, String var5, GenericClassLoader var6, VirtualJarFile[] var7) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException {
      return getEjbDescriptorReader().createReadOnlyDescriptorFromJarFile(var0, var1, var2, var3, var4, var5, var6, var7);
   }
}
