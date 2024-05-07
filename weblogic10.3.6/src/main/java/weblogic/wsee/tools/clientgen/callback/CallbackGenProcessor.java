package weblogic.wsee.tools.clientgen.callback;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.tools.ant.Project;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.anttasks.WsdlcTask;
import weblogic.wsee.tools.clientgen.ProcessInfo;
import weblogic.wsee.tools.clientgen.jaxrpc.ClientGenProcessor;
import weblogic.wsee.tools.jws.UpgradedJwsWsdlWriter;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.source.JsClass;
import weblogic.wsee.tools.source.JsService;
import weblogic.wsee.wsdl.WsdlPartnerLinkType;
import weblogic.wsee.wsdl.WsdlService;

public class CallbackGenProcessor implements ClientGenProcessor {
   private Project project;
   private List<Jws> callbackJws = new ArrayList();

   public CallbackGenProcessor(Project var1) {
      this.project = var1;
   }

   public void process(ProcessInfo var1) throws WsBuildException {
      if (!var1.isPartialWsdl()) {
         JsService var2 = var1.getJsCallbackService();
         if (var2 != null) {
            WsdlService var3 = var2.getWsdlService();
            WsdlPartnerLinkType var4 = (WsdlPartnerLinkType)var3.getDefinitions().getExtension("PartnerLinkType");
            if (var4 != null) {
               String var5 = var1.getPackageName();
               if (!var5.endsWith(".callbackclient")) {
                  File var6 = var1.getDestDir();
                  String var7 = "destDir";
                  ToolsUtil.validateRequiredFile(var6, var7);
                  ToolsUtil.createDir(var6, var7);
                  WsdlcTask var8 = new WsdlcTask();
                  var8.setProject(this.project);
                  var8.setSrcWsdl(var1.getWsdlUrl());
                  var8.setDestJwsDir(var6);
                  var8.setDestImplDir(var6);
                  var8.setPackageName(var5);
                  var8.setCodeGenBaseData(var1);
                  var8.setImplTemplateClassName("weblogic.wsee.tools.clientgen.callback.CallbackImpl");
                  var8.setSrcServiceName(var3.getName().toString());
                  var8.setJaxRPCWrappedArrayStyle(false);
                  var8.setTypeFamily(var1.getTypeFamily());
                  var8.setExplode(true);
                  if (UpgradedJwsWsdlWriter.narrow(var1.getJsService().getWsdlService()) != null) {
                     var8.setUpgraded81Jws(true);
                  }

                  var8.execute();
                  Iterator var9 = var1.getJsCallbackService().getEndpoints();

                  assert var9.hasNext() : "Service has no endpoints";

                  JsClass var10 = (JsClass)var9.next();
                  this.callbackJws.clear();
                  Jws var11 = new Jws();
                  var11.setFile(var10.getQualifiedName().replace('.', '/') + "Impl.java");
                  var11.srcdir(var1.getDestDir());
                  this.callbackJws.add(var11);
               }
            }
         }
      }
   }

   public List<Jws> getCallbackJws() {
      return this.callbackJws;
   }
}
