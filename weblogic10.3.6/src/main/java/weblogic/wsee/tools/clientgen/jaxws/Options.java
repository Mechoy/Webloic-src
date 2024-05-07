package weblogic.wsee.tools.clientgen.jaxws;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Properties;
import org.apache.tools.ant.types.FileSet;
import weblogic.wsee.tools.xcatalog.CatalogOptions;
import weblogic.wsee.util.Verbose;

public class Options extends CatalogOptions {
   private boolean genRuntimeCatalog = true;
   private String wsdllocationOverride = null;
   private boolean copyWsdl = false;
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
   private File xauthfile = null;
   private Properties systemProperties = new Properties();
   private static HashSet<String> supportedSysPropNames = new HashSet();

   public void addSysemProperty(String var1, String var2) {
      if (supportedSysPropNames.contains(var1)) {
         this.systemProperties.setProperty(var1, var2);
      } else {
         Verbose.say("System property \"" + var2 + "\" is not supported");
      }

   }

   Properties getSystemProperties() {
      return this.systemProperties;
   }

   public boolean isGenRuntimeCatalog() {
      return this.genRuntimeCatalog;
   }

   public void setGenRuntimeCatalog(boolean var1) {
      this.genRuntimeCatalog = var1;
   }

   public String getWsdllocationOverride() {
      return this.wsdllocationOverride;
   }

   public void setWsdllocationOverride(String var1) {
      this.wsdllocationOverride = var1;
   }

   public boolean isCopyWsdl() {
      return this.copyWsdl;
   }

   public void setCopyWsdl(boolean var1) {
      this.copyWsdl = var1;
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

   public File getXauthfile() {
      return this.xauthfile;
   }

   public void setXauthfile(File var1) {
      this.xauthfile = var1;
   }

   static {
      supportedSysPropNames.add("javax.net.ssl.trustStore");
      supportedSysPropNames.add("javax.net.ssl.trustStorePassword");
      supportedSysPropNames.add("javax.net.ssl.keyStore");
      supportedSysPropNames.add("javax.net.ssl.keyStorePassword");
      supportedSysPropNames.add("java.protocol.handler.pkgs");
      supportedSysPropNames.add("weblogic.security.SSL.trustedCAKeyStore");
      supportedSysPropNames.add("weblogic.security.SSL.ignoreHostnameVerification");
   }
}
