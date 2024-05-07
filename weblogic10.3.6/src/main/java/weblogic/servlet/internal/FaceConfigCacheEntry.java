package weblogic.servlet.internal;

import java.util.List;
import weblogic.application.library.CachableLibMetadataType;
import weblogic.application.library.LibraryProcessingException;

public class FaceConfigCacheEntry extends DescriptorCacheEntry {
   public FaceConfigCacheEntry(WarLibraryDefinition var1) {
      super(var1);
   }

   protected List getResourceLocations() throws LibraryProcessingException {
      return this.library.getFacesConfigLocations();
   }

   public CachableLibMetadataType getType() {
      return CachableLibMetadataType.FACE_BEANS;
   }
}
