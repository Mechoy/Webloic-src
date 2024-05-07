package weblogic.wsee.tools.clientgen.serviceref;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import weblogic.application.descriptor.NamespaceURIMunger;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.DescriptorManager;
import weblogic.descriptor.EditableDescriptorManager;
import weblogic.j2ee.descriptor.ParamValueBean;
import weblogic.j2ee.descriptor.ServiceRefBean;
import weblogic.j2ee.descriptor.ServiceRefHandlerBean;
import weblogic.j2ee.descriptor.wl.WeblogicWseeClientHandlerChainBean;
import weblogic.j2ee.descriptor.wl.WeblogicWseeStandaloneclientBean;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.clientgen.ProcessInfo;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenProcessor;

public class ServiceRefProcessor implements ClientGenProcessor {
   ProcessInfo info;

   public void process(ProcessInfo var1) throws WsBuildException {
      this.info = var1;
      EditableDescriptorManager var2 = new EditableDescriptorManager();
      WeblogicWseeStandaloneclientBean var3 = (WeblogicWseeStandaloneclientBean)var2.createDescriptorRoot(WeblogicWseeStandaloneclientBean.class).getRootBean();
      ServiceRefBean var4 = var3.createServiceRef();
      var4.setServiceQname(var1.getJsService().getWsdlService().getName());
      var4.setServiceRefName("unused information");
      var4.setServiceInterface(var1.getPackageName() + "." + var1.getStubInfo().getServiceName());
      String var5 = var1.getPackageName().replace('.', '/') + "/";
      var4.setWsdlFile(var5 + var1.getWsdlFileName());
      var4.setJaxrpcMappingFile(var5 + var1.getMappingFileName());
      this.fillHandlerChain(var4);
      this.writeout(var3, var5);
   }

   private void fillHandlerChain(ServiceRefBean var1) throws WsBuildException {
      File var2 = this.info.getHandlerChain();
      if (var2 != null) {
         if (!var2.exists()) {
            throw new WsBuildException("Handler chain file " + var2.getAbsolutePath() + " doesn't exist.");
         } else {
            FileInputStream var3 = null;
            WeblogicWseeClientHandlerChainBean var4 = null;

            try {
               var3 = new FileInputStream(var2);
               DescriptorManager var5 = new DescriptorManager();
               String[] var6 = new String[]{"http://www.bea.com/ns/weblogic/90", "http://www.bea.com/ns/weblogic/weblogic-wsee-clientHandlerChain"};
               var4 = (WeblogicWseeClientHandlerChainBean)var5.createDescriptor(new NamespaceURIMunger(var3, "http://xmlns.oracle.com/weblogic/weblogic-wsee-clientHandlerChain", var6)).getRootBean();
            } catch (XMLStreamException var16) {
               throw new WsBuildException("Failed to load handler chain file " + var16, var16);
            } catch (ClassCastException var17) {
               throw new WsBuildException("Failed to load handler chain file " + var17, var17);
            } catch (IOException var18) {
               throw new WsBuildException("Failed to load handler chain file " + var18, var18);
            } finally {
               if (var3 != null) {
                  try {
                     var3.close();
                  } catch (IOException var15) {
                  }
               }

            }

            ServiceRefHandlerBean[] var20 = var4.getHandlers();

            for(int var21 = 0; var21 < var20.length; ++var21) {
               ServiceRefHandlerBean var7 = var20[var21];
               this.copyHandlerInfo(var7, var1.createHandler());
            }

         }
      }
   }

   private void copyHandlerInfo(ServiceRefHandlerBean var1, ServiceRefHandlerBean var2) {
      String[] var3 = var1.getDescriptions();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         var2.addDescription(var3[var4]);
      }

      String[] var14 = var1.getDisplayNames();

      for(int var5 = 0; var5 < var14.length; ++var5) {
         var2.addDisplayName(var14[var5]);
      }

      String[] var15 = var1.getPortNames();

      for(int var6 = 0; var6 < var15.length; ++var6) {
         var2.addPortName(var15[var6]);
      }

      String[] var16 = var1.getSoapRoles();

      for(int var7 = 0; var7 < var16.length; ++var7) {
         var2.addSoapRole(var16[var7]);
      }

      QName[] var17 = var1.getSoapHeaders();

      for(int var8 = 0; var8 < var17.length; ++var8) {
         var2.addSoapHeader(var17[var8]);
      }

      var2.setHandlerClass(var1.getHandlerClass());
      var2.setHandlerName(var1.getHandlerName());
      ParamValueBean[] var18 = var1.getInitParams();

      for(int var9 = 0; var9 < var18.length; ++var9) {
         ParamValueBean var10 = var18[var9];
         ParamValueBean var11 = var2.createInitParam();
         var11.setParamName(var10.getParamName());
         var11.setParamValue(var10.getParamValue());
         String[] var12 = var10.getDescriptions();

         for(int var13 = 0; var13 < var12.length; ++var13) {
            var11.addDescription(var12[var13]);
         }
      }

   }

   private void writeout(WeblogicWseeStandaloneclientBean var1, String var2) throws WsBuildException {
      String var3 = var2 + this.info.getStubInfo().getServiceName() + "_internaldd.xml";
      File var4 = new File(this.info.getDestDir(), var3);
      FileOutputStream var5 = null;

      try {
         var4.getParentFile().mkdirs();
         var5 = new FileOutputStream(var4);
         DescriptorBean var6 = (DescriptorBean)var1;
         (new EditableDescriptorManager()).writeDescriptorAsXML(var6.getDescriptor(), var5, "UTF-8");
      } catch (IOException var14) {
         throw new WsBuildException(var14);
      } finally {
         try {
            var5.close();
         } catch (Throwable var13) {
         }

      }

   }
}
