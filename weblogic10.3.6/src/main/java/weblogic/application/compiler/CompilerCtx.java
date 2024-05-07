package weblogic.application.compiler;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.DescriptorUpdater;
import weblogic.application.internal.FlowContext;
import weblogic.application.io.Ear;
import weblogic.deploy.api.model.EditableDeployableObject;
import weblogic.deploy.api.model.WebLogicDeployableObjectFactory;
import weblogic.deployment.PersistenceUnitViewer;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.j2ee.descriptor.wl.DeploymentPlanBean;
import weblogic.j2ee.descriptor.wl.WeblogicApplicationBean;
import weblogic.j2ee.descriptor.wl.WeblogicExtensionBean;
import weblogic.utils.Getopt2;
import weblogic.utils.jars.VirtualJarFile;

public final class CompilerCtx implements DescriptorUpdater {
   private Getopt2 opts;
   private VirtualJarFile vSource;
   private File sourceFile;
   private File outputDir;
   private String classpathArg;
   private String targetArchive;
   private boolean isSplitDir = false;
   private boolean verbose;
   private EARModule[] modules = null;
   private EARModule[] customModules = null;
   private ApplicationDescriptor appDesc;
   private ApplicationBean appDD;
   private WeblogicApplicationBean wlappDD;
   private WeblogicExtensionBean wlExtDD;
   private Ear ear = null;
   private FlowContext appCtx = null;
   private String planName;
   private File planFile;
   private DeploymentPlanBean plan;
   private File configDir;
   private Map factoryMap;
   private boolean readOnlyInvocation;
   private boolean verifyLibraryReferences;
   private boolean mergedDisabled;
   private File tempDir;
   private boolean unregisterLibrariesOnExit;
   private PersistenceUnitViewer perViewer;
   private boolean basicView;
   private String partialOutputTarget;
   private WebLogicDeployableObjectFactory objectFactory;
   private EditableDeployableObject deployableApplication;
   private boolean writeInferredDescriptors;
   private File manifestFile;
   private String lightWeightAppName;

   public CompilerCtx() {
      this.factoryMap = Collections.EMPTY_MAP;
      this.readOnlyInvocation = false;
      this.verifyLibraryReferences = true;
      this.mergedDisabled = false;
      this.tempDir = null;
      this.unregisterLibrariesOnExit = true;
      this.perViewer = null;
      this.basicView = false;
      this.partialOutputTarget = null;
      this.objectFactory = null;
      this.deployableApplication = null;
      this.writeInferredDescriptors = false;
      this.manifestFile = null;
      this.lightWeightAppName = null;
   }

   public Getopt2 getOpts() {
      return this.opts;
   }

   public void setOpts(Getopt2 var1) {
      this.opts = var1;
   }

   public VirtualJarFile getVSource() {
      return this.vSource;
   }

   public void setVSource(VirtualJarFile var1) {
      this.vSource = var1;
   }

   public File getSourceFile() {
      return this.sourceFile;
   }

   public void setSourceFile(File var1) {
      this.sourceFile = var1;
   }

   public String getSourceName() {
      return this.sourceFile.getName();
   }

   public File getOutputDir() {
      return this.outputDir;
   }

   public void setOutputDir(File var1) {
      this.outputDir = var1;
   }

   public String getClasspathArg() {
      return this.classpathArg;
   }

   public void setClasspathArg(String var1) {
      this.classpathArg = var1;
   }

   public String getTargetArchive() {
      return this.targetArchive;
   }

   public void setTargetArchive(String var1) {
      this.targetArchive = var1;
   }

   public boolean isVerbose() {
      return this.verbose;
   }

   public void setVerbose(boolean var1) {
      this.verbose = var1;
   }

   public ApplicationBean getApplicationDD() {
      return this.appDD;
   }

   public WeblogicApplicationBean getWLApplicationDD() {
      return this.wlappDD;
   }

   public ApplicationDescriptor getApplicationDescriptor() {
      return this.appDesc;
   }

   public void setApplicationDescriptor(ApplicationDescriptor var1) throws IOException, XMLStreamException {
      this.appDesc = var1;
      this.appDD = var1.getApplicationDescriptor();
      this.wlappDD = var1.getWeblogicApplicationDescriptor();
      this.wlExtDD = var1.getWeblogicExtensionDescriptor();
   }

   public WeblogicExtensionBean getWLExtensionDD() {
      return this.wlExtDD;
   }

   public void createApplicationContext() {
      if (this.appCtx == null) {
         this.appCtx = new BuildtimeApplicationContext(this);
      }

   }

   public FlowContext getApplicationContext() {
      return this.appCtx;
   }

   public void setModules(EARModule[] var1) {
      this.modules = var1;
   }

   public EARModule[] getModules() {
      return this.modules;
   }

   public boolean isSplitDir() {
      return this.isSplitDir;
   }

   public void setSplitDir() {
      this.isSplitDir = true;
   }

   public void setEar(Ear var1) {
      if (this.ear != null) {
         throw new AssertionError("An EAR can't be set twice on this Context");
      } else {
         this.ear = var1;
         this.getApplicationContext().addClassFinder(var1.getClassFinder());
      }
   }

   public Ear getEar() {
      return this.ear;
   }

   public void setPlanFile(File var1) {
      this.planFile = var1;
   }

   public File getPlanFile() {
      return this.planFile;
   }

   public void setPlanName(String var1) {
      this.planName = var1;
   }

   public String getPlanName() {
      return this.planName;
   }

   public void setPlanBean(DeploymentPlanBean var1) {
      this.plan = var1;
   }

   public DeploymentPlanBean getPlanBean() {
      return this.plan;
   }

   public void setConfigDir(File var1) {
      this.configDir = var1;
   }

   public File getConfigDir() {
      return this.configDir;
   }

   public Map getCustomModuleFactories() {
      return this.factoryMap;
   }

   public void setCustomModuleFactories(Map var1) {
      this.factoryMap = var1;
   }

   public EARModule[] getCustomModules() {
      return this.customModules;
   }

   public void setCustomModules(EARModule[] var1) {
      this.customModules = var1;
   }

   public WebLogicDeployableObjectFactory getObjectFactory() {
      return this.objectFactory;
   }

   public void setObjectFactory(WebLogicDeployableObjectFactory var1) {
      this.objectFactory = var1;
   }

   public boolean isReadOnlyInvocation() {
      return this.readOnlyInvocation;
   }

   public void setReadOnlyInvocation() {
      this.readOnlyInvocation = true;
   }

   public EditableDeployableObject getDeployableApplication() {
      return this.deployableApplication;
   }

   public void setDeployableApplication(EditableDeployableObject var1) {
      this.deployableApplication = var1;
   }

   public boolean verifyLibraryReferences() {
      return this.verifyLibraryReferences;
   }

   public void setVerifyLibraryReferences(boolean var1) {
      this.verifyLibraryReferences = var1;
   }

   public void disableLibraryMerge() {
      this.mergedDisabled = true;
   }

   public boolean isMergeDisabled() {
      return this.mergedDisabled;
   }

   public File getTempDir() {
      return this.tempDir;
   }

   public void setTempDir(File var1) {
      this.tempDir = var1;
   }

   public boolean isWriteInferredDescriptors() {
      return this.writeInferredDescriptors;
   }

   public void setWriteInferredDescriptors() {
      this.writeInferredDescriptors = true;
   }

   public void setManifestFile(File var1) {
      this.manifestFile = var1;
   }

   public File getManifestFile() {
      return this.manifestFile;
   }

   public void setPerViewer(PersistenceUnitViewer var1) {
      this.perViewer = var1;
   }

   public PersistenceUnitViewer getPerViewer() {
      return this.perViewer;
   }

   public void keepLibraryRegistrationOnExit() {
      this.unregisterLibrariesOnExit = false;
   }

   public boolean unregisterLibrariesOnExit() {
      return this.unregisterLibrariesOnExit;
   }

   public void setBasicView() {
      this.basicView = true;
   }

   public boolean isBasicView() {
      return this.basicView;
   }

   public void setLightWeightAppName(String var1) {
      this.lightWeightAppName = var1;
   }

   public String getLightWeightAppName() {
      return this.lightWeightAppName;
   }

   public String getPartialOutputTarget() {
      return this.partialOutputTarget;
   }

   public void setPartialOutputTarget(String var1) {
      this.partialOutputTarget = var1;
   }
}
