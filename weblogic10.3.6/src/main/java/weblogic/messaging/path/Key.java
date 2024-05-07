package weblogic.messaging.path;

import java.io.Serializable;

public interface Key extends Comparable {
   String CLUSTER = "CLUSTER";
   byte IDX_WLS = 0;
   byte IDX_UOO = 1;
   byte IDX_SAF = 2;
   byte IDX_WLI = 3;
   byte IDX_WS = 4;
   byte IDX_UOW = 5;
   String[] RESERVED_SUBSYSTEMS = new String[]{".wls", ".uoo", ".saf", ".wli", ".ws", ".uow"};

   byte getSubsystem();

   String getAssemblyId();

   Serializable getKeyId();

   int hashCode();

   boolean equals(Object var1);

   int compareTo(Object var1);
}
