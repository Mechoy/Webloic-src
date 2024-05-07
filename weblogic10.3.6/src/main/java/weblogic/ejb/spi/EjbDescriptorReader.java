package weblogic.ejb.spi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarFile;
import javax.xml.stream.XMLStreamException;
import weblogic.j2ee.descriptor.EjbJarBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicEjbJarBean;
import weblogic.utils.classloaders.GenericClassLoader;
import weblogic.utils.jars.VirtualJarFile;
import weblogic.xml.process.ProcessorFactory;
import weblogic.xml.process.XMLParsingException;
import weblogic.xml.process.XMLProcessingException;

public interface EjbDescriptorReader {
   EjbDescriptorBean createDescriptorFromJarFile(JarFile var1) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException;

   EjbDescriptorBean createDescriptorFromJarFile(JarFile var1, File var2) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException;

   EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var1) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException;

   EjbDescriptorBean createDescriptorFromJarFile(VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, String var5, String var6) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException;

   EjbDescriptorBean createReadOnlyDescriptorFromJarFile(VirtualJarFile var1, GenericClassLoader var2) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException;

   EjbDescriptorBean createReadOnlyDescriptorFromJarFile(VirtualJarFile var1, File var2, File var3, DeploymentPlanBean var4, String var5, String var6, GenericClassLoader var7, VirtualJarFile[] var8) throws IOException, XMLParsingException, XMLProcessingException, XMLStreamException;

   void loadWeblogicRDBMSJarMBeans(EjbDescriptorBean var1, VirtualJarFile var2, ProcessorFactory var3, boolean var4) throws Exception;

   WeblogicEjbJarBean parseWebLogicEjbJarXML(EjbJarDescriptor var1, DeploymentPlanBean var2, File var3, String var4, InputStream var5, EjbJarBean var6, String var7, boolean var8) throws IOException, XMLStreamException;
}
