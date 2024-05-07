package weblogic.application.library;

import java.io.File;
import weblogic.application.Type;
import weblogic.application.internal.library.LibraryRuntimeMBeanImpl;
import weblogic.management.runtime.LibraryRuntimeMBean;

public abstract class LibraryDefinition implements Library, CachableLibMetadata, Comparable {
   private final LibraryData libData;
   private LibraryRuntimeMBeanImpl runtime = null;

   public LibraryDefinition(LibraryData var1, Type var2) {
      this.libData = var1;
      var1.setType(var2);
   }

   public void init() throws LibraryProcessingException {
   }

   public void cleanup() throws LibraryProcessingException {
   }

   public void remove() throws LibraryProcessingException {
   }

   public String getName() {
      return this.libData.getName();
   }

   public Type getType() {
      return this.libData.getType();
   }

   public String getSpecificationVersion() {
      return this.libData.getSpecificationVersion() == null ? null : this.libData.getSpecificationVersion().toString();
   }

   public String getImplementationVersion() {
      return this.libData.getImplementationVersion();
   }

   public File getLocation() {
      return this.libData.getLocation();
   }

   public void setLocation(File var1) {
      this.libData.setLocation(var1);
   }

   public void setRuntime(LibraryRuntimeMBeanImpl var1) {
      this.runtime = var1;
   }

   public LibraryRuntimeMBeanImpl getRuntimeImpl() {
      return this.runtime;
   }

   public LibraryRuntimeMBean getRuntime() {
      return this.getRuntimeImpl();
   }

   public String toString() {
      return this.libData.toString();
   }

   public LibraryData getLibData() {
      return this.libData;
   }

   public int compareTo(Object var1) {
      return this.compareTo((LibraryDefinition)var1);
   }

   public int compareTo(LibraryDefinition var1) {
      return this.getName().compareTo(var1.getName());
   }

   public LibraryReference[] getLibraryReferences() {
      return null;
   }

   public CachableLibMetadataEntry[] findAllCachableEntry() {
      return new CachableLibMetadataEntry[0];
   }

   public CachableLibMetadataEntry getCachableEntry(CachableLibMetadataType var1) {
      return null;
   }

   public LibraryConstants.AutoReferrer[] getAutoRef() {
      return this.libData.getAutoRef();
   }
}
