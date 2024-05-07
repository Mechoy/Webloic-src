package weblogic.ant.taskdefs.build;

import com.bea.wls.ejbgen.ant.EJBGenAntTask;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import javax.xml.stream.XMLStreamException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Javac;
import weblogic.application.ApplicationDescriptor;
import weblogic.application.DescriptorUpdater;
import weblogic.application.library.LibraryContext;
import weblogic.j2ee.descriptor.ApplicationBean;
import weblogic.utils.classloaders.ClassFinder;
import weblogic.utils.classloaders.MultiClassFinder;

public final class BuildCtx implements LibraryContext, DescriptorUpdater {
   private Project project;
   private Javac javacTask;
   private File srcDir;
   private File destDir;
   private EJBGenAntTask ejbgen;
   private ApplicationDescriptor appDesc;
   private final Collection libraryModules = new ArrayList();
   private final MultiClassFinder classFinders = new MultiClassFinder();
   private Map contextRootOverrideMap;

   public Project getProject() {
      return this.project;
   }

   public void setProject(Project var1) {
      this.project = var1;
   }

   public Javac getJavacTask() {
      return this.javacTask;
   }

   public void setJavacTask(Javac var1) {
      this.javacTask = var1;
   }

   public File getSrcDir() {
      return this.srcDir;
   }

   public void setSrcDir(File var1) {
      this.srcDir = var1;
   }

   public File getDestDir() {
      return this.destDir;
   }

   public void setDestDir(File var1) {
      this.destDir = var1;
   }

   public EJBGenAntTask getEJBGen() {
      return this.ejbgen;
   }

   public void setEJBGen(EJBGenAntTask var1) {
      this.ejbgen = var1;
   }

   public void registerLink(File var1) {
      this.registerLink((String)null, var1);
   }

   public void registerLink(String var1, File var2) {
      this.libraryModules.add(var2);
   }

   public ApplicationBean getApplicationDD() {
      try {
         return this.appDesc.getApplicationDescriptor();
      } catch (IOException var2) {
         throw new AssertionError(var2);
      } catch (XMLStreamException var3) {
         throw new AssertionError(var3);
      }
   }

   public ApplicationDescriptor getApplicationDescriptor() {
      return this.appDesc;
   }

   public void notifyDescriptorUpdate() {
   }

   public void addClassFinder(ClassFinder var1) {
      this.classFinders.addFinder(var1);
   }

   public ClassFinder getLibraryClassFinder() {
      return this.classFinders;
   }

   public String getRefappName() {
      return this.srcDir.getName();
   }

   public File[] getLibraryFiles() {
      return (File[])((File[])this.libraryModules.toArray(new File[this.libraryModules.size()]));
   }

   public void setApplicationDescriptor(ApplicationDescriptor var1) {
      this.appDesc = var1;
   }

   public String getRefappUri() {
      return this.srcDir == null ? null : this.srcDir.getPath();
   }

   public Map getContextRootOverrideMap() {
      return this.contextRootOverrideMap;
   }

   public void setContextRootOverrideMap(Map var1) {
      this.contextRootOverrideMap = var1;
   }
}
