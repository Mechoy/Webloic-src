package weblogic.cluster.singleton;

import java.io.Serializable;

public interface MigrationData extends Serializable {
   int SUCCEEDED = 0;
   int IN_PROGRESS = 1;
   int FAILED = 2;

   String getServerName();

   int getStatus();

   String getMachineMigratedFrom();

   String getMachineMigratedTo();

   long getMigrationStartTime();

   long getMigrationEndTime();

   String getClusterName();

   String getClusterMasterName();

   String getMigrationType();
}
