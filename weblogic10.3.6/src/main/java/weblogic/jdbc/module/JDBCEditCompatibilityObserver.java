package weblogic.jdbc.module;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import weblogic.j2ee.descriptor.wl.JDBCDataSourceBean;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.management.configuration.DeploymentMBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.configuration.JDBCConnectionPoolMBean;
import weblogic.management.configuration.JDBCDataSourceMBean;
import weblogic.management.configuration.JDBCMultiPoolMBean;
import weblogic.management.configuration.JDBCSystemResourceMBean;
import weblogic.management.configuration.JDBCTxDataSourceMBean;
import weblogic.management.provider.AccessCallback;

public class JDBCEditCompatibilityObserver implements AccessCallback, PropertyChangeListener {
   DomainMBean domainTree;
   JDBCDeploymentHelper helper;

   public void accessed(DomainMBean var1) {
      this.domainTree = var1;
      this.helper = new JDBCDeploymentHelper();
      this.updateConfiguration(var1);
      this.domainTree.addPropertyChangeListener(this);
   }

   public void shutdown() {
      this.domainTree.removePropertyChangeListener(this);
   }

   public void propertyChange(PropertyChangeEvent var1) {
      if (this.JDBCProperty(var1.getPropertyName())) {
         this.applyChanges((Object[])((Object[])var1.getOldValue()), (Object[])((Object[])var1.getNewValue()));
      }

   }

   private boolean JDBCProperty(String var1) {
      return var1.equals("JDBCConnectionPools") || var1.equals("JDBCMultiPools") || var1.equals("JDBCDataSources") || var1.equals("JDBCTxDataSources") || var1.equals("JDBCSystemResources");
   }

   private void applyChanges(Object[] var1, Object[] var2) {
      Object[] var3 = (Object[])((Object[])var2.clone());

      int var4;
      Object var5;
      for(var4 = 0; var4 < var1.length; ++var4) {
         var5 = var1[var4];

         int var6;
         for(var6 = 0; var6 < var3.length; ++var6) {
            Object var7 = var2[var6];
            if (var7 != null && var7.equals(var5)) {
               var3[var6] = null;
               break;
            }
         }

         if (var6 == var3.length) {
            this.removeBean(var5);
         }
      }

      for(var4 = 0; var4 < var3.length; ++var4) {
         var5 = var3[var4];
         if (var5 != null) {
            this.addBean(var5);
         }
      }

   }

   private void addBean(Object var1) {
      if (var1 instanceof JDBCSystemResourceMBean) {
         JDBCSystemResourceMBean var2 = (JDBCSystemResourceMBean)var1;
         JDBCDataSourceBean var3 = var2.getJDBCResource();
         String var4 = null;
         if (var3 != null) {
            var4 = var3.getName();
         }

         if (var4 == null) {
            return;
         }

         int var5 = JDBCMBeanConverter.getLegacyType(var3);
         if (var5 == 1) {
            if (this.domainTree.lookupJDBCConnectionPool(var4) == null) {
               this.domainTree.createJDBCConnectionPool(var4).setJDBCSystemResource(var2);
            }
         } else if (var5 == 2) {
            if (this.domainTree.lookupJDBCMultiPool(var4) == null) {
               this.domainTree.createJDBCMultiPool(var4).setJDBCSystemResource(var2);
            }
         } else if (var5 == 3) {
            if (this.domainTree.lookupJDBCDataSource(var4) == null) {
               this.domainTree.createJDBCDataSource(var4).setJDBCSystemResource(var2);
            }
         } else if (var5 == 4) {
            if (this.domainTree.lookupJDBCTxDataSource(var4) == null) {
               this.domainTree.createJDBCTxDataSource(var4).setJDBCSystemResource(var2);
            }
         } else if (var5 == 0) {
            if (var3.getJDBCDataSourceParams().getDataSourceList() != null) {
               if (this.domainTree.lookupJDBCMultiPool(var4) == null) {
                  this.domainTree.createJDBCMultiPool(var4).setJDBCSystemResource(var2);
               }
            } else if (this.domainTree.lookupJDBCConnectionPool(var4) == null) {
               this.domainTree.createJDBCConnectionPool(var4).setJDBCSystemResource(var2);
            }

            if (var3.getJDBCDataSourceParams().getGlobalTransactionsProtocol().equals("None")) {
               if (this.domainTree.lookupJDBCDataSource(var4) == null) {
                  this.domainTree.createJDBCDataSource(var4).setJDBCSystemResource(var2);
               }
            } else if (this.domainTree.lookupJDBCTxDataSource(var4) == null) {
               this.domainTree.createJDBCTxDataSource(var4).setJDBCSystemResource(var2);
            }
         }
      } else {
         try {
            String var9 = ((DeploymentMBean)var1).getName();
            JDBCSystemResourceMBean[] var11 = this.domainTree.getJDBCSystemResources();
            if (var11 != null) {
               for(int var13 = 0; var13 < var11.length; ++var13) {
                  try {
                     if (var9.equals(var11[var13].getJDBCResource().getName())) {
                        return;
                     }
                  } catch (Throwable var6) {
                  }
               }
            }
         } catch (Throwable var8) {
         }

         byte var10 = 0;
         if (var1 instanceof JDBCConnectionPoolMBean) {
            var10 = 1;
         } else if (var1 instanceof JDBCMultiPoolMBean) {
            var10 = 2;
         } else if (var1 instanceof JDBCDataSourceMBean) {
            var10 = 3;
         } else if (var1 instanceof JDBCTxDataSourceMBean) {
            var10 = 4;
         }

         JDBCSystemResourceMBean var12;
         try {
            if (this.domainTree.lookupJDBCSystemResource(((DeploymentMBean)var1).getName()) != null) {
               return;
            }

            var12 = this.helper.createJDBCSystemResource((DeploymentMBean)var1, var10, this.domainTree);
            if (var12 == null) {
               return;
            }
         } catch (Exception var7) {
            return;
         }

         if (var10 == 1) {
            ((JDBCConnectionPoolMBean)var1).setJDBCSystemResource(var12);
         } else if (var10 == 2) {
            ((JDBCMultiPoolMBean)var1).setJDBCSystemResource(var12);
         } else if (var10 == 3) {
            ((JDBCDataSourceMBean)var1).setJDBCSystemResource(var12);
         } else if (var10 == 4) {
            ((JDBCTxDataSourceMBean)var1).setJDBCSystemResource(var12);
         }
      }

   }

   private void removeBean(Object var1) {
      if (var1 instanceof JDBCSystemResourceMBean) {
         JDBCSystemResourceMBean var2 = (JDBCSystemResourceMBean)var1;
         JDBCDataSourceBean var3 = var2.getJDBCResource();
         if (var3 == null) {
            return;
         }

         String var4 = var3.getName();
         if (var4 == null) {
            return;
         }

         int var5 = JDBCMBeanConverter.getLegacyType(var3);
         if (var5 == 1) {
            if (this.domainTree.lookupJDBCConnectionPool(var4) != null) {
               this.domainTree.destroyJDBCConnectionPool(this.domainTree.lookupJDBCConnectionPool(var4));
            }
         } else if (var5 == 2) {
            if (this.domainTree.lookupJDBCMultiPool(var4) != null) {
               this.domainTree.destroyJDBCMultiPool(this.domainTree.lookupJDBCMultiPool(var4));
            }
         } else if (var5 == 3) {
            if (this.domainTree.lookupJDBCDataSource(var4) != null) {
               this.domainTree.destroyJDBCDataSource(this.domainTree.lookupJDBCDataSource(var4));
            }
         } else if (var5 == 4) {
            if (this.domainTree.lookupJDBCTxDataSource(var4) != null) {
               this.domainTree.destroyJDBCTxDataSource(this.domainTree.lookupJDBCTxDataSource(var4));
            }
         } else if (var5 == 0) {
            if (var3.getJDBCDataSourceParams().getDataSourceList() != null) {
               if (this.domainTree.lookupJDBCMultiPool(var4) != null) {
                  this.domainTree.destroyJDBCMultiPool(this.domainTree.lookupJDBCMultiPool(var4));
               }
            } else if (this.domainTree.lookupJDBCConnectionPool(var4) != null) {
               this.domainTree.destroyJDBCConnectionPool(this.domainTree.lookupJDBCConnectionPool(var4));
            }

            if (var3.getJDBCDataSourceParams().getGlobalTransactionsProtocol().equals("None")) {
               if (this.domainTree.lookupJDBCDataSource(var4) != null) {
                  this.domainTree.destroyJDBCDataSource(this.domainTree.lookupJDBCDataSource(var4));
               }
            } else if (this.domainTree.lookupJDBCTxDataSource(var4) != null) {
               this.domainTree.destroyJDBCTxDataSource(this.domainTree.lookupJDBCTxDataSource(var4));
            }
         }
      } else if (var1 instanceof JDBCConnectionPoolMBean) {
         this.domainTree.destroyJDBCSystemResource(((JDBCConnectionPoolMBean)var1).getJDBCSystemResource());
      } else if (var1 instanceof JDBCMultiPoolMBean) {
         this.domainTree.destroyJDBCSystemResource(((JDBCMultiPoolMBean)var1).getJDBCSystemResource());
      } else if (var1 instanceof JDBCDataSourceMBean) {
         this.domainTree.destroyJDBCSystemResource(((JDBCDataSourceMBean)var1).getJDBCSystemResource());
      } else if (var1 instanceof JDBCTxDataSourceMBean) {
         this.domainTree.destroyJDBCSystemResource(((JDBCTxDataSourceMBean)var1).getJDBCSystemResource());
      }

   }

   private void updateConfiguration(DomainMBean var1) {
      JDBCSystemResourceMBean[] var2 = var1.getJDBCSystemResources();
      if (var2 != null) {
         for(int var3 = 0; var3 < var2.length; ++var3) {
            JDBCSystemResourceMBean var4 = var2[var3];
            JDBCDataSourceBean var5 = var4.getJDBCResource();
            if (var5 != null) {
               String var6 = var5.getName();
               if (var6 != null) {
                  int var7 = JDBCMBeanConverter.getLegacyType(var5);
                  if (var7 == 1) {
                     if (var1.lookupJDBCConnectionPool(var6) == null) {
                        var1.createJDBCConnectionPool(var6).setJDBCSystemResource(var4);
                     }
                  } else if (var7 == 2) {
                     if (var1.lookupJDBCMultiPool(var6) == null) {
                        var1.createJDBCMultiPool(var6).setJDBCSystemResource(var4);
                     }
                  } else if (var7 == 3) {
                     if (var1.lookupJDBCDataSource(var6) == null) {
                        var1.createJDBCDataSource(var6).setJDBCSystemResource(var4);
                     }
                  } else if (var7 == 4) {
                     if (var1.lookupJDBCTxDataSource(var6) == null) {
                        var1.createJDBCTxDataSource(var6).setJDBCSystemResource(var4);
                     }
                  } else if (var7 == 0) {
                     if (var5.getJDBCDataSourceParams().getDataSourceList() != null) {
                        if (var1.lookupJDBCMultiPool(var6) == null) {
                           var1.createJDBCMultiPool(var6).setJDBCSystemResource(var4);
                        }
                     } else if (var1.lookupJDBCConnectionPool(var6) == null) {
                        var1.createJDBCConnectionPool(var6).setJDBCSystemResource(var4);
                     }

                     if (var5.getJDBCDataSourceParams().getGlobalTransactionsProtocol().equals("None")) {
                        if (var1.lookupJDBCDataSource(var6) == null) {
                           var1.createJDBCDataSource(var6).setJDBCSystemResource(var4);
                        }
                     } else if (var1.lookupJDBCTxDataSource(var6) == null) {
                        var1.createJDBCTxDataSource(var6).setJDBCSystemResource(var4);
                     }
                  }
               }
            }
         }
      }

   }
}
