package weblogic.deployment;

import java.util.Collection;

public interface PersistenceUnitRegistry {
   Collection getPersistenceUnitNames();

   PersistenceUnitInfoImpl getPersistenceUnit(String var1) throws IllegalArgumentException;

   void close();
}
