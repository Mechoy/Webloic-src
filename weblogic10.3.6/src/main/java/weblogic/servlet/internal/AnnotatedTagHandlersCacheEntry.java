package weblogic.servlet.internal;

import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.LibraryProcessingException;

public class AnnotatedTagHandlersCacheEntry extends AnnotatedClassesCacheEntry {
   public AnnotatedTagHandlersCacheEntry(WarLibraryDefinition var1) {
      super(var1);
   }

   public CachableLibMetadataType getType() {
      return CachableLibMetadataType.TAG_HANDLERS;
   }

   public Object getCachableObject() throws LibraryProcessingException {
      return this.library.getAnnotatedTagHandlers();
   }
}
