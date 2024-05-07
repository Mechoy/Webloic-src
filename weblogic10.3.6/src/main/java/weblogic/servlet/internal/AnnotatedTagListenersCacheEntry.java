package weblogic.servlet.internal;

import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.LibraryProcessingException;

public class AnnotatedTagListenersCacheEntry extends AnnotatedClassesCacheEntry {
   public AnnotatedTagListenersCacheEntry(WarLibraryDefinition var1) {
      super(var1);
   }

   public CachableLibMetadataType getType() {
      return CachableLibMetadataType.TAG_LISTENERS;
   }

   public Object getCachableObject() throws LibraryProcessingException {
      return this.library.getAnnotatedTagListeners();
   }
}
