package weblogic.wsee.tools.jws;

import com.bea.util.jam.JClass;
import com.bea.util.jam.xml.JamXmlUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import javax.xml.stream.XMLStreamException;
import weblogic.descriptor.DescriptorBean;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.jws.decl.WebServiceDecl;
import weblogic.wsee.tools.jws.decl.WebServiceSEIDecl;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCProcessor;
import weblogic.wsee.tools.jws.jaxrpc.JAXRPCWebServiceInfo;
import weblogic.wsee.util.IOUtil;
import weblogic.wsee.util.NamespaceSpecifyingDescriptorManager;
import weblogic.wsee.wsdl.WsdlException;
import weblogic.wsee.wsdl.builder.WsdlDefinitionsBuilder;

public class WebServiceWriter extends JAXRPCProcessor {
   private File destDir = null;
   private String destEncoding = null;

   protected void processImpl(JAXRPCWebServiceInfo var1) throws WsBuildException {
      if (var1.getWebService().getType() != null) {
         this.destDir = this.moduleInfo.getOutputDir();
      }

      this.destEncoding = this.moduleInfo.getJwsBuildContext().getDestEncoding();

      try {
         this.writeWsdl(var1);
         if (!this.moduleInfo.isWsdlOnly()) {
            this.writeEndpointInterfaces(var1);
            this.writeJam(var1.getWebService());
            this.writeBean(var1.getJavaWsdlMappingBean(), new File(this.destDir, var1.getWebService().getArtifactName() + ".xml"));
         }

      } catch (Exception var3) {
         throw new WsBuildException("Failed to write wsdl", var3);
      }
   }

   private void writeJam(WebServiceDecl var1) throws IOException, XMLStreamException {
      String var2 = this.destEncoding;
      if (var2 == null) {
         var2 = "UTF-8";
      }

      File var3 = new File(this.destDir, var1.getArtifactName() + "-annotation.xml");
      Writer var4 = IOUtil.createEncodedFileWriter(var3, var2);

      try {
         var4.write("<?xml version=\"1.0\" encoding=\"");
         var4.write(var2);
         var4.write("\"?>\n");
         JamXmlUtils.getInstance().toXml(new JClass[]{var1.getJClass()}, var4);
         var4.flush();
      } finally {
         var4.close();
      }

   }

   private void writeBean(Object var1, File var2) throws IOException {
      assert var2 != null;

      assert var1 != null;

      DescriptorBean var3 = (DescriptorBean)var1;
      OutputStream var4 = IOUtil.createEncodedFileOutputStream(var2, this.destEncoding);

      try {
         (new NamespaceSpecifyingDescriptorManager()).writeDescriptorAsXML(var3.getDescriptor(), var4, this.destEncoding);
         var4.flush();
      } finally {
         var4.close();
      }

   }

   private void bytesToFile(File var1, byte[] var2) throws IOException {
      FileOutputStream var3 = new FileOutputStream(var1);
      var3.write(var2);
      var3.close();
   }

   private void writeEndpointInterfaces(JAXRPCWebServiceInfo var1) throws IOException {
      WebServiceSEIDecl var2 = var1.getWebService();
      String var3 = var2.getEndpointInterfaceName();
      File var4 = new File(this.destDir, var3.replace('.', '/') + ".java");
      var4.getParentFile().mkdirs();
      this.bytesToFile(var4, var1.getEndpointInterface());
   }

   private void writeWsdl(WebServiceInfo var1) throws IOException, WsdlException {
      File var2 = new File(this.destDir, var1.getWebService().getWsdlFile());
      var2.getParentFile().mkdirs();
      ((WsdlDefinitionsBuilder)var1.getDefinitions()).writeToFile(var2, this.destEncoding);
   }
}
