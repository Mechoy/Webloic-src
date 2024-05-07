package weblogic.wsee.persistence;

import java.io.Serializable;

public interface Storable extends Serializable {
   Serializable getObjectId();

   boolean hasExplicitExpiration();

   boolean isExpired();

   String getPhysicalStoreName();

   void setPhysicalStoreName(String var1);

   Long getCreationTime();

   Long getLastUpdatedTime();

   void touch();
}
