package weblogic.wsee.tools.clientgen.callback;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.Project;
import weblogic.wsee.callback.CallbackUtils;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.anttasks.WsdlcTask;
import weblogic.wsee.tools.clientgen.ProcessInfo;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenProcessor;
import weblogic.wsee.tools.jws.build.GeneratedCallbackJws;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.source.JsClass;
import weblogic.wsee.tools.source.JsPort;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.wsdl.Callback81WsdlExtracter;
import weblogic.wsee.wsdl.WsdlPort;
import weblogic.wsee.wsdl.WsdlService;

public class CallbackGen81Processor implements ClientGenProcessor {
   private Project project;
   private List<Jws> callbackJws = new ArrayList();

   public CallbackGen81Processor(Project var1) {
      this.project = var1;
   }

   public void process(ProcessInfo var1) throws WsBuildException {
      if (!var1.isPartialWsdl()) {
         JsService var2 = var1.getJsService();
         if (var2 != null) {
            if (CallbackUtils.has81StyleCallback(var2)) {
               String var3 = var1.getPackageName();
               if (!var3.endsWith(".callbackclient")) {
                  File var4 = var1.getDestDir();
                  String var5 = "destDir";
                  ToolsUtil.validateRequiredFile(var4, var5);
                  ToolsUtil.createDir(var4, var5);
                  List var6 = this.getSoapPortName(var2);
                  this.callbackJws.clear();
                  Iterator var7 = var6.iterator();

                  while(var7.hasNext()) {
                     String var8 = (String)var7.next();
                     WsdlcTask var9 = new WsdlcTask();
                     var9.setProject(this.project);
                     String var10 = var1.getWsdlUrl();
                     File var11 = Callback81WsdlExtracter.extract(var10, var8, var4);
                     var10 = var11.getAbsolutePath();
                     var9.setSrcWsdl(var10);
                     var9.setDestJwsDir(var4);
                     var9.setDestImplDir(var4);
                     var9.setPackageName(var3);
                     var9.setCodeGenBaseData(var1);
                     var9.setImplTemplateClassName("weblogic.wsee.tools.clientgen.callback.CallbackImpl");
                     var9.setWlw81CallbackGen(true);
                     var9.setUpgraded81Jws(true);
                     var9.setSrcPortName(var8);
                     var9.setJaxRPCWrappedArrayStyle(false);
                     var9.setTypeFamily(var1.getTypeFamily());
                     var9.setExplode(false);
                     var9.execute();
                     var11.delete();
                     JsPort var12 = var2.getPort(var8);

                     assert var12 != null;

                     JsClass var13 = var12.getEndpoint();
                     GeneratedCallbackJws var14 = new GeneratedCallbackJws();
                     var14.setFile(var13.getQualifiedName().replace('.', '/') + "CallbackImpl.java");
                     var14.srcdir(var1.getDestDir());
                     var14.setCompiledWsdl(new File(var4, var11.getName().replace('.', '_') + ".jar"));
                     this.callbackJws.add(var14);
                  }

               }
            }
         }
      }
   }

   private List<String> getSoapPortName(JsService var1) throws WsBuildException {
      ArrayList var2 = new ArrayList();
      WsdlService var3 = var1.getWsdlService();
      Iterator var4 = var3.getPorts().values().iterator();

      while(var4.hasNext()) {
         WsdlPort var5 = (WsdlPort)var4.next();
         if (var5.getName().getLocalPart().endsWith("Soap")) {
            var2.add(var5.getName().getLocalPart());
         }
      }

      if (var2.size() == 0) {
         throw new WsBuildException("No suitable SOAP port found for callback");
      } else {
         return var2;
      }
   }

   public List<Jws> getCallbackJws() {
      return this.callbackJws;
   }
}
