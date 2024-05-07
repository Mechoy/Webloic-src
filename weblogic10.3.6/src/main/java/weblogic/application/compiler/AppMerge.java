package weblogic.application.compiler;

import weblogic.application.compiler.flow.AppMergerFlow;
import weblogic.application.compiler.flow.CompilerFlow;
import weblogic.application.compiler.flow.InitLibrariesFlow;
import weblogic.application.compiler.flow.InitPlanFlow;
import weblogic.application.compiler.flow.PrepareInputFlow;
import weblogic.application.compiler.flow.SetupFlow;
import weblogic.application.utils.LibraryUtils;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.utils.BadOptionException;
import weblogic.utils.compiler.Tool;
import weblogic.utils.compiler.ToolFailureException;

public class AppMerge extends Tool {
   private final CompilerCtx ctx;
   private final FlowDriver.CompilerFlowDriver driver;

   public AppMerge(String[] var1) {
      super(var1);
      this.ctx = new CompilerCtx();
      String var2 = ".appmergegen_" + System.currentTimeMillis();
      CompilerFlow[] var3 = new CompilerFlow[]{new SetupFlow(this.ctx, var2), new PrepareInputFlow(this.ctx), new InitPlanFlow(this.ctx), new InitLibrariesFlow(this.ctx, true), new AppMergerFlow(this.ctx)};
      this.driver = new FlowDriver.CompilerFlowDriver(var3);
   }

   public AppMerge(String[] var1, WebLogicDeployableObjectFactory var2, boolean var3, boolean var4, boolean var5) {
      this(var1);
      this.ctx.setObjectFactory(var2);
      if (var3) {
         this.ctx.disableLibraryMerge();
      }

      this.ctx.setVerifyLibraryReferences(var4);
      if (var5) {
         this.ctx.keepLibraryRegistrationOnExit();
      }

   }

   public AppMerge(String[] var1, WebLogicDeployableObjectFactory var2, boolean var3, boolean var4, boolean var5, boolean var6) {
      this(var1, var2, var3, var4, var5);
      if (var6) {
         this.ctx.setBasicView();
      }

   }

   public void prepare() {
      this.setRequireExtraArgs(true);
      this.setUsageName("weblogic.appmerge");
      this.opts.setUsageArgs("<ear, jar or war file or directory>");
      this.opts.addOption("output", "file", "Specifies an alternate output archive or directory.  If not set, output will be placed in the source archive or directory.");
      this.opts.addOption("plan", "file", "Specifies an optional deployment plan.");
      this.opts.addOption("plandir", "directory", "Specifies an optional deployment plan directory.");
      this.opts.markPrivate("plandir");
      this.opts.addFlag("verbose", "More output");
      this.opts.addFlag("nopackage", "do not create archived package");
      this.opts.addFlag("readonly", "Don't write out merged app to disk. Used with direct merge() invocation.");
      this.opts.markPrivate("readonly");
      this.opts.markPrivate("nopackage");
      this.opts.addFlag("ignoreMissingLibRefs", "Don't verify that all the libraries referred in the application are specified in command line library options. This may cause failures to load up application in case such libraries were needed");
      this.opts.markPrivate("ignoreMissingLibRefs");
      this.opts.addOption("classpath", "path", "Classpath to use.");
      this.opts.markPrivate("classpath");
      this.opts.addFlag("writeInferredDescriptors", "Write out the descriptors with inferred information including annotations.");
      this.opts.addOption("lightweight", "deployed application name", "Generate Deployment View by using lightweight flow which directly get information from the server which application already deployed on.");
      this.opts.markPrivate("lightweight");
      LibraryUtils.addLibraryUsage(this.opts);
   }

   public void runBody() throws ToolFailureException {
      try {
         this.opts.setOption("noexit", "true");
         this.ctx.setOpts(this.opts);
      } catch (BadOptionException var2) {
      }

      try {
         this.driver.compile();
      } catch (ToolFailureException var3) {
         if (this.ctx.isVerbose() && var3.getCause() != null) {
            var3.getCause().printStackTrace();
         }

         throw var3;
      }
   }

   public EditableDeployableObject merge() throws ToolFailureException {
      try {
         this.run();
         return this.ctx.getDeployableApplication();
      } catch (Exception var2) {
         throw new ToolFailureException("Exception in AppMerge flows' progression", var2);
      }
   }

   public void cleanup() throws ToolFailureException {
      try {
         this.driver.cleanup();
      } catch (ToolFailureException var2) {
         if (this.ctx.isVerbose() && var2.getCause() != null) {
            var2.getCause().printStackTrace();
         }

         throw var2;
      }
   }

   public static void main(String[] var0) throws Exception {
      AppMerge var1 = new AppMerge(var0);
      var1.run();
      var1.cleanup();
   }
}
