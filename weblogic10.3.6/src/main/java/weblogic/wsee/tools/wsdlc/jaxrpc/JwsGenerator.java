package weblogic.wsee.tools.wsdlc.jaxrpc;

import java.io.File;
import java.io.PrintStream;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.WsBuildException;
import weblogic.wsee.tools.logging.Logger;
import weblogic.wsee.tools.wsdlc.WsdlcUtils;
import weblogic.wsee.util.jspgen.GenFactory;
import weblogic.wsee.util.jspgen.ScriptException;

class JwsGenerator {
   private final JwsGenInfo jwsGenInfo;
   private final Logger logger;
   private final Object codeGenBaseData;

   JwsGenerator(JwsGenInfo var1, Logger var2, Object var3) {
      this.jwsGenInfo = var1;
      this.logger = var2;
      this.codeGenBaseData = var3;
   }

   private JwsBase getJwsBase(String var1, ClassLoader var2) throws WsBuildException {
      JwsBase var3 = null;

      try {
         if (var2 == null) {
            var3 = (JwsBase)GenFactory.create(var1);
         } else {
            var3 = (JwsBase)GenFactory.create(var1, var2);
         }

         return var3;
      } catch (ScriptException var5) {
         throw new WsBuildException("Failed to create jws implementation", var5);
      }
   }

   void generate(File var1, String var2, String var3, ClassLoader var4, String var5) throws WsBuildException {
      JwsBase var6 = this.getJwsBase(var3, var4);
      String var7 = this.jwsGenInfo.getBinding().getPackageName();
      WsdlcUtils.logVerbose(this.logger, "Generating jws " + var5 + " using package name as ", var7);
      WsdlcUtils.logVerbose(this.logger, "Generating jws " + var5 + " using class name as", var2);
      var6.setJwsGenInfo(this.jwsGenInfo);
      PrintStream var8 = ToolsUtil.createJavaSourceStream(var1, var7, var2);
      var6.setOutput(var8);
      var6.setup(this.codeGenBaseData);

      try {
         var6.generate();
      } catch (ScriptException var13) {
         throw new WsBuildException("Failed to generate jws " + var5, var13);
      } finally {
         var8.close();
      }

   }
}
