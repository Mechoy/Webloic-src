package weblogic.connector.external.impl;

import weblogic.connector.external.PoolInfo;

public class PoolInfoImpl implements PoolInfo {
   private int initialCapacity;
   private int maxCapacity;
   private int capacityIncrement;
   private boolean shrinkingEnabled;
   private int shrinkFrequencySeconds;
   private int highestNumWaiters;
   private int highestNumUnavailable;
   private int connectionCreationRetryFrequencySeconds;
   private int connectionReserveTimeoutSeconds;
   private int testFrequencySeconds;
   private boolean testConnectionsOnCreate;
   private boolean testConnectionsOnRelease;
   private boolean testConnectionsOnReserve;
   private int profileHarvestFrequencySeconds;
   private boolean ignoreInUseConnectionsEnabled;
   private boolean matchConnectionsSupported;

   public PoolInfoImpl(int var1, int var2, int var3, boolean var4, int var5, int var6, int var7, int var8, int var9, int var10, boolean var11, boolean var12, boolean var13, int var14, boolean var15, boolean var16) {
      this.initialCapacity = var1;
      this.maxCapacity = var2;
      this.capacityIncrement = var3;
      this.shrinkingEnabled = var4;
      this.shrinkFrequencySeconds = var5;
      this.highestNumWaiters = var6;
      this.highestNumUnavailable = var7;
      this.connectionCreationRetryFrequencySeconds = var8;
      this.connectionReserveTimeoutSeconds = var9;
      this.testFrequencySeconds = var10;
      this.testConnectionsOnCreate = var11;
      this.testConnectionsOnRelease = var12;
      this.testConnectionsOnReserve = var13;
      this.profileHarvestFrequencySeconds = var14;
      this.ignoreInUseConnectionsEnabled = var15;
      this.matchConnectionsSupported = var16;
   }

   public int getInitialCapacity() {
      return this.initialCapacity;
   }

   public int getMaxCapacity() {
      return this.maxCapacity;
   }

   public int getCapacityIncrement() {
      return this.capacityIncrement;
   }

   public boolean isShrinkingEnabled() {
      return this.shrinkingEnabled;
   }

   public int getShrinkFrequencySeconds() {
      return this.shrinkFrequencySeconds;
   }

   public int getHighestNumWaiters() {
      return this.highestNumWaiters;
   }

   public int getHighestNumUnavailable() {
      return this.highestNumUnavailable;
   }

   public int getConnectionCreationRetryFrequencySeconds() {
      return this.connectionCreationRetryFrequencySeconds;
   }

   public int getConnectionReserveTimeoutSeconds() {
      return this.connectionReserveTimeoutSeconds;
   }

   public int getTestFrequencySeconds() {
      return this.testFrequencySeconds;
   }

   public boolean isTestConnectionsOnCreate() {
      return this.testConnectionsOnCreate;
   }

   public boolean isTestConnectionsOnRelease() {
      return this.testConnectionsOnRelease;
   }

   public boolean isTestConnectionsOnReserve() {
      return this.testConnectionsOnReserve;
   }

   public int getProfileHarvestFrequencySeconds() {
      return this.profileHarvestFrequencySeconds;
   }

   public boolean isIgnoreInUseConnectionsEnabled() {
      return this.ignoreInUseConnectionsEnabled;
   }

   public boolean isMatchConnectionsSupported() {
      return this.matchConnectionsSupported;
   }
}
