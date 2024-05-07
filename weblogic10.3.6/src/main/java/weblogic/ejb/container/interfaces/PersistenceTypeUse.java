package weblogic.ejb.container.interfaces;

import weblogic.ejb.container.persistence.PersistenceType;

public interface PersistenceTypeUse {
   String getIdentifier();

   String getVersion();

   boolean matchesType(PersistenceType var1);
}
