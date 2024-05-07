package weblogic.management.runtime;

import weblogic.diagnostics.harvester.HarvesterException;

public interface WLDFHarvesterRuntimeMBean extends RuntimeMBean {
   String[][] getHarvestableAttributes(String var1) throws HarvesterException.HarvestableTypesNotFoundException, HarvesterException.AmbiguousTypeName, HarvesterException.TypeNotHarvestable;

   String[][] getHarvestableAttributesForInstance(String var1) throws HarvesterException.HarvestableTypesNotFoundException, HarvesterException.AmbiguousTypeName, HarvesterException.TypeNotHarvestable;

   String[] getCurrentlyHarvestedAttributes(String var1) throws HarvesterException.MissingConfigurationType, HarvesterException.HarvestingNotEnabled, HarvesterException.AmbiguousTypeName;

   String[] getKnownHarvestableTypes();

   String[] getKnownHarvestableTypes(String var1);

   String[] getKnownHarvestableInstances(String var1) throws HarvesterException.HarvestableTypesNotFoundException, HarvesterException.AmbiguousTypeName, HarvesterException.TypeNotHarvestable;

   String[] getKnownHarvestableInstances(String var1, String var2) throws HarvesterException.HarvestableTypesNotFoundException, HarvesterException.AmbiguousTypeName, HarvesterException.TypeNotHarvestable;

   String[] getCurrentlyHarvestedInstances(String var1) throws HarvesterException.MissingConfigurationType, HarvesterException.HarvestingNotEnabled, HarvesterException.AmbiguousTypeName;

   String getHarvestableType(String var1) throws HarvesterException.HarvestableInstancesNotFoundException, HarvesterException.AmbiguousInstanceName;

   String[] getConfiguredNamespaces() throws HarvesterException.HarvestingNotEnabled;

   String getDefaultNamespace() throws HarvesterException.HarvestingNotEnabled;

   long getSamplePeriod() throws HarvesterException.HarvestingNotEnabled;

   long getTotalSamplingCycles();

   long getMinimumSamplingTime();

   long getMaximumSamplingTime();

   long getTotalSamplingTime();

   long getAverageSamplingTime();

   long getTotalDataSampleCount() throws HarvesterException.HarvestingNotEnabled;

   long getTotalImplicitDataSampleCount() throws HarvesterException.HarvestingNotEnabled;

   long getCurrentDataSampleCount() throws HarvesterException.HarvestingNotEnabled;

   long getCurrentImplicitDataSampleCount() throws HarvesterException.HarvestingNotEnabled;

   long getCurrentSnapshotStartTime() throws HarvesterException.HarvestingNotEnabled;

   long getCurrentSnapshotElapsedTime() throws HarvesterException.HarvestingNotEnabled;

   long getTotalSamplingTimeOutlierCount();

   boolean isCurrentSampleTimeAnOutlier();

   float getOutlierDetectionFactor();
}
