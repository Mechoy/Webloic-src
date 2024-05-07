package weblogic.management.mbeans.custom;

import java.util.StringTokenizer;
import javax.management.InvalidAttributeValueException;
import weblogic.j2ee.descriptor.wl.JDBCPropertyBean;
import weblogic.jdbc.common.internal.JDBCMBeanConverter;
import weblogic.management.provider.custom.ConfigurationMBeanCustomized;

public final class JDBCTxDataSource extends JDBCConfigurationMBeanCustomizer {
   private String jndiName = null;
   private String jndiNameSeparator = ";";
   private String poolName = null;
   private boolean emulate2PC = false;
   private boolean rowPrefetch = false;
   private int rowPrefetchSize = 48;
   private int streamChunkSize = 256;

   public JDBCTxDataSource(ConfigurationMBeanCustomized var1) {
      super(var1);
   }

   public void setJNDIName(String var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         if (var1 == null) {
            return;
         }

         String var2 = this.getJNDINameSeparator();
         StringTokenizer var3 = new StringTokenizer(var1, var2);
         String[] var4 = new String[var3.countTokens()];

         for(int var5 = 0; var3.hasMoreTokens(); var4[var5++] = var3.nextToken()) {
         }

         this.delegate.getJDBCResource().getJDBCDataSourceParams().setJNDINames(var4);
      } else {
         this.jndiName = var1;
      }

   }

   public String getJNDIName() {
      if (this.delegate == null) {
         return this.jndiName;
      } else {
         String[] var1 = this.delegate.getJDBCResource().getJDBCDataSourceParams().getJNDINames();
         if (var1 != null && var1.length != 0) {
            String var2 = this.getJNDINameSeparator();
            StringBuffer var3 = new StringBuffer();

            for(int var4 = 0; var4 < var1.length; ++var4) {
               var3.append(var1[var4]);
               var3.append(var2);
            }

            var3.deleteCharAt(var3.lastIndexOf(var2));
            return var3.toString();
         } else {
            return null;
         }
      }
   }

   public void setJNDINameSeparator(String var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         JDBCMBeanConverter.setInternalProperty(this.delegate.getJDBCResource(), "JNDINameSeparator", var1);
      } else {
         this.jndiNameSeparator = var1;
      }

   }

   public String getJNDINameSeparator() {
      if (this.delegate != null) {
         JDBCPropertyBean var1;
         return (var1 = this.delegate.getJDBCResource().getInternalProperties().lookupProperty("JNDINameSeparator")) != null ? var1.getValue() : ";";
      } else {
         return this.jndiNameSeparator;
      }
   }

   public void setPoolName(String var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         JDBCMBeanConverter.setInternalProperty(this.delegate.getJDBCResource(), "LegacyPoolName", var1);
      } else {
         this.poolName = var1;
      }

   }

   public String getPoolName() {
      if (this.delegate != null) {
         return JDBCMBeanConverter.getLegacyType(this.delegate.getJDBCResource()) == 0 ? this.delegate.getJDBCResource().getName() : JDBCMBeanConverter.getInternalProperty(this.delegate.getJDBCResource(), "LegacyPoolName");
      } else {
         return this.poolName;
      }
   }

   public boolean getEnableTwoPhaseCommit() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDataSourceParams().getGlobalTransactionsProtocol().equals("EmulateTwoPhaseCommit") : this.emulate2PC;
   }

   public void setEnableTwoPhaseCommit(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         if (var1) {
            this.delegate.getJDBCResource().getJDBCDataSourceParams().setGlobalTransactionsProtocol("EmulateTwoPhaseCommit");
         } else {
            this.delegate.getJDBCResource().getJDBCDataSourceParams().setGlobalTransactionsProtocol("OnePhaseCommit");
         }
      } else {
         this.emulate2PC = var1;
      }

   }

   public boolean isRowPrefetchEnabled() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDataSourceParams().isRowPrefetch() : this.rowPrefetch;
   }

   public void setRowPrefetchEnabled(boolean var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDataSourceParams().setRowPrefetch(var1);
      } else {
         this.rowPrefetch = var1;
      }

   }

   public int getRowPrefetchSize() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDataSourceParams().getRowPrefetchSize() : this.rowPrefetchSize;
   }

   public void setRowPrefetchSize(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDataSourceParams().setRowPrefetchSize(var1);
      } else {
         this.rowPrefetchSize = var1;
      }

   }

   public int getStreamChunkSize() {
      return this.delegate != null ? this.delegate.getJDBCResource().getJDBCDataSourceParams().getStreamChunkSize() : this.streamChunkSize;
   }

   public void setStreamChunkSize(int var1) throws InvalidAttributeValueException {
      if (this.delegate != null) {
         this.delegate.getJDBCResource().getJDBCDataSourceParams().setStreamChunkSize(var1);
      } else {
         this.streamChunkSize = var1;
      }

   }
}
