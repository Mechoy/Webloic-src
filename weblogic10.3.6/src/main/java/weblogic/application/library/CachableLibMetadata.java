package weblogic.application.library;

public interface CachableLibMetadata {
   CachableLibMetadataEntry[] findAllCachableEntry();

   CachableLibMetadataEntry getCachableEntry(CachableLibMetadataType var1);
}
