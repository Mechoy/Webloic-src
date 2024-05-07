package weblogic.servlet.internal;

import java.util.List;
import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.LibraryProcessingException;

public class TldCacheEntry extends DescriptorCacheEntry {
   public TldCacheEntry(WarLibraryDefinition var1) {
      super(var1);
   }

   protected List getResourceLocations() throws LibraryProcessingException {
      return this.library.getTldLocations();
   }

   public CachableLibMetadataType getType() {
      return CachableLibMetadataType.TLD;
   }
}
