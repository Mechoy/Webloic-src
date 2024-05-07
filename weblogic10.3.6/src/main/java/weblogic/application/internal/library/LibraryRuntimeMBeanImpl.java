package weblogic.application.internal.library;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import weblogic.application.library.LibraryData;
import weblogic.application.library.LibraryReferencer;
import weblogic.management.ManagementException;
import weblogic.management.configuration.ComponentMBean;
import weblogic.management.runtime.LibraryRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;

public final class LibraryRuntimeMBeanImpl extends RuntimeMBeanDelegate implements LibraryRuntimeMBean {
   private final ComponentMBean[] components;
   private final LibraryData libData;
   private final String libId;
   private final Collection references = new HashSet();

   public LibraryRuntimeMBeanImpl(LibraryData var1, String var2, ComponentMBean[] var3) throws ManagementException {
      super(var2);
      this.components = var3;
      this.libData = var1;
      this.libId = var2;
   }

   public ComponentMBean[] getComponents() {
      return this.components;
   }

   public String getLibraryName() {
      return this.libData.getName();
   }

   public File getLocation() {
      return this.libData.getLocation();
   }

   public String getLibraryIdentifier() {
      return this.libId;
   }

   public String getSpecificationVersion() {
      return this.libData.getSpecificationVersion() == null ? null : this.libData.getSpecificationVersion().toString();
   }

   public String getImplementationVersion() {
      return this.libData.getImplementationVersion();
   }

   public RuntimeMBean[] getReferencingRuntimes() {
      synchronized(this.references) {
         RuntimeMBean[] var2 = new RuntimeMBean[this.references.size()];
         int var3 = 0;

         LibraryReferencer var5;
         for(Iterator var4 = this.references.iterator(); var4.hasNext(); var2[var3++] = var5.getReferencerRuntime()) {
            var5 = (LibraryReferencer)var4.next();
         }

         return var2;
      }
   }

   public String[] getReferencingNames() {
      synchronized(this.references) {
         String[] var2 = new String[this.references.size()];
         int var3 = 0;

         for(Iterator var4 = this.references.iterator(); var4.hasNext(); var2[var3++] = ((LibraryReferencer)var4.next()).getReferencerName()) {
         }

         return var2;
      }
   }

   public void addReference(LibraryReferencer var1) {
      synchronized(this.references) {
         this.references.add(var1);
      }
   }

   public void removeReference(LibraryReferencer var1) {
      synchronized(this.references) {
         this.references.remove(var1);
      }
   }

   public boolean isReferenced() {
      synchronized(this.references) {
         return !this.references.isEmpty();
      }
   }
}
