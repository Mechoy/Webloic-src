package weblogic.ejb.container.interfaces;

public interface PersistenceTypeInfo {
   String getIdentifier();

   String getVersion();

   String getFileName();

   String generateFileName();
}
