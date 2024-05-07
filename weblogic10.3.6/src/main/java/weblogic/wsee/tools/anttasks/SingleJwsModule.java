package weblogic.wsee.tools.anttasks;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.types.Path;
import weblogic.wsee.tools.ToolsUtil;
import weblogic.wsee.tools.jws.build.Jws;
import weblogic.wsee.tools.jws.context.JwsBuildContext;
import weblogic.wsee.tools.jws.decl.port.HttpPort;
import weblogic.wsee.tools.jws.decl.port.HttpsPort;
import weblogic.wsee.tools.jws.decl.port.JmsPort;
import weblogic.wsee.util.StringUtil;

public class SingleJwsModule extends JwsModule {
   private static final boolean FLATTEN_DEFAULT_FILENAME = false;
   private Jws jws = new Jws();

   SingleJwsModule(JwscTask var1, JwsBuildContext var2) {
      super(var1, var2);
   }

   public HttpsPort createWLHttpsTransport() {
      HttpsPort var1 = this.jws.createWLHttpsTransport();
      var1.setBuildContext(super.getBuildContext());
      return var1;
   }

   public HttpPort createWLHttpTransport() {
      HttpPort var1 = this.jws.createWLHttpTransport();
      var1.setBuildContext(super.getBuildContext());
      return var1;
   }

   public JmsPort createWLJmsTransport() {
      JmsPort var1 = this.jws.createWLJmsTransport();
      var1.setBuildContext(super.getBuildContext());
      return var1;
   }

   public void setCompiledWsdl(File var1) {
      this.jws.setCompiledWsdl(var1);
   }

   public void setFile(String var1) {
      this.jws.setFile(var1);
      this.initializeName();
   }

   public void setType(String var1) {
      this.jws.setType(var1);
   }

   private void initializeName() {
      String var1 = this.getName();
      if (StringUtil.isEmpty(var1)) {
         var1 = this.jws.getFile().substring(0, this.jws.getFile().lastIndexOf(46));
         this.setName(ToolsUtil.normalize(var1));
      }

   }

   /** @deprecated */
   @Deprecated
   public void setIncludeSchemas(Path var1) {
      this.getTask().getProject().log("DEPRECATED - Use of includeSchemas is deprecated.  These will be ignored.", 1);
   }

   Collection<Jws> getJwsFiles() {
      return Collections.singleton(this.jws);
   }

   void reset() {
      this.jws = new Jws();
   }

   void validateImpl() {
      if (StringUtil.isEmpty(this.jws.getFile())) {
         throw new BuildException("No file specified in jws");
      }
   }
}
