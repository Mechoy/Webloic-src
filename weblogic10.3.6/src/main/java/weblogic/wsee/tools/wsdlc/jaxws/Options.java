package weblogic.wsee.tools.wsdlc.jaxws;

import java.util.ArrayList;
import java.util.Collection;
import javax.xml.namespace.QName;
import org.apache.tools.ant.types.FileSet;

public class Options {
   private QName serviceName;
   private boolean debug = false;
   private boolean optimize = false;
   private String debugLevel = null;
   private boolean failonerror = true;
   private boolean verbose = false;
   private boolean includeantruntime = true;
   private boolean includejavaruntime = false;
   private boolean fork = false;
   private Collection<FileSet> depends = new ArrayList();
   private Collection<FileSet> produces = new ArrayList();

   public void setServiceName(QName var1) {
      this.serviceName = var1;
   }

   public QName getServiceName() {
      return this.serviceName;
   }

   public String getDebugLevel() {
      return this.debugLevel;
   }

   public void setDebugLevel(String var1) {
      this.debugLevel = var1;
   }

   public boolean isFailonerror() {
      return this.failonerror;
   }

   public void setFailonerror(boolean var1) {
      this.failonerror = var1;
   }

   public boolean isDebug() {
      return this.debug;
   }

   public void setDebug(boolean var1) {
      this.debug = var1;
   }

   public boolean isOptimize() {
      return this.optimize;
   }

   public void setOptimize(boolean var1) {
      this.optimize = var1;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public boolean isIncludeantruntime() {
      return this.includeantruntime;
   }

   public void setIncludeantruntime(boolean var1) {
      this.includeantruntime = var1;
   }

   public boolean isIncludejavaruntime() {
      return this.includejavaruntime;
   }

   public void setIncludejavaruntime(boolean var1) {
      this.includejavaruntime = var1;
   }

   public boolean isFork() {
      return this.fork;
   }

   public void setFork(boolean var1) {
      this.fork = var1;
   }

   public void addConfiguredDepends(FileSet var1) {
      this.depends.add(var1);
   }

   public Collection<FileSet> getConfiguredDepends() {
      return this.depends;
   }

   public void addConfiguredProduces(FileSet var1) {
      this.produces.add(var1);
   }

   public Collection<FileSet> getConfiguredProduces() {
      return this.produces;
   }
}
