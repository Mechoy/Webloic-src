package weblogic.management.descriptors.application.weblogic.jdbc;

import weblogic.management.descriptors.XMLElementMBeanDelegate;
import weblogic.management.tools.ToXML;

public class DriverParamsMBeanImpl extends XMLElementMBeanDelegate implements DriverParamsMBean {
   static final long serialVersionUID = 1L;
   private boolean isSet_streamChunkSize = false;
   private int streamChunkSize;
   private boolean isSet_statement = false;
   private StatementMBean statement;
   private boolean isSet_rowPrefetchSize = false;
   private int rowPrefetchSize;
   private boolean isSet_preparedStatement = false;
   private PreparedStatementMBean preparedStatement;
   private boolean isSet_rowPrefetchEnabled = false;
   private boolean rowPrefetchEnabled;
   private boolean isSet_enableTwoPhaseCommit = false;
   private boolean enableTwoPhaseCommit = false;

   public int getStreamChunkSize() {
      return this.streamChunkSize;
   }

   public void setStreamChunkSize(int var1) {
      int var2 = this.streamChunkSize;
      this.streamChunkSize = var1;
      this.isSet_streamChunkSize = var1 != -1;
      this.checkChange("streamChunkSize", var2, this.streamChunkSize);
   }

   public StatementMBean getStatement() {
      return this.statement;
   }

   public void setStatement(StatementMBean var1) {
      StatementMBean var2 = this.statement;
      this.statement = var1;
      this.isSet_statement = var1 != null;
      this.checkChange("statement", var2, this.statement);
   }

   public int getRowPrefetchSize() {
      return this.rowPrefetchSize;
   }

   public void setRowPrefetchSize(int var1) {
      int var2 = this.rowPrefetchSize;
      this.rowPrefetchSize = var1;
      this.isSet_rowPrefetchSize = var1 != -1;
      this.checkChange("rowPrefetchSize", var2, this.rowPrefetchSize);
   }

   public PreparedStatementMBean getPreparedStatement() {
      return this.preparedStatement;
   }

   public void setPreparedStatement(PreparedStatementMBean var1) {
      PreparedStatementMBean var2 = this.preparedStatement;
      this.preparedStatement = var1;
      this.isSet_preparedStatement = var1 != null;
      this.checkChange("preparedStatement", var2, this.preparedStatement);
   }

   public boolean isRowPrefetchEnabled() {
      return this.rowPrefetchEnabled;
   }

   public void setRowPrefetchEnabled(boolean var1) {
      boolean var2 = this.rowPrefetchEnabled;
      this.rowPrefetchEnabled = var1;
      this.isSet_rowPrefetchEnabled = true;
      this.checkChange("rowPrefetchEnabled", var2, this.rowPrefetchEnabled);
   }

   public boolean getEnableTwoPhaseCommit() {
      return this.enableTwoPhaseCommit;
   }

   public void setEnableTwoPhaseCommit(boolean var1) {
      boolean var2 = this.enableTwoPhaseCommit;
      this.enableTwoPhaseCommit = var1;
      this.isSet_enableTwoPhaseCommit = true;
      this.checkChange("enableTwoPhaseCommit", var2, this.enableTwoPhaseCommit);
   }

   public String toXML(int var1) {
      StringBuffer var2 = new StringBuffer();
      var2.append(ToXML.indent(var1)).append("<driver-params");
      var2.append(">\n");
      var2.append(ToXML.indent(var1)).append("</driver-params>\n");
      return var2.toString();
   }
}
