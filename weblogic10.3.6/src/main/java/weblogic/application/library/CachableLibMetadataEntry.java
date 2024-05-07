package weblogic.application.library;

import java.io.File;

public interface CachableLibMetadataEntry {
   CachableLibMetadataType getType();

   File getLocation();

   boolean isStale(long var1);

   Object getCachableObject() throws LibraryProcessingException;
}
