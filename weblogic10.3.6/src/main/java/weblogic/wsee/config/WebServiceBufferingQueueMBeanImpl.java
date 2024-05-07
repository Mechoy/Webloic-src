package weblogic.wsee.config;

import weblogic.management.configuration.WebServiceBufferingQueueMBean;

public class WebServiceBufferingQueueMBeanImpl extends DummyConfigurationMBeanImpl implements WebServiceBufferingQueueMBean {
   private boolean _enabled;
   private String _connectionFactoryJndiName;
   private boolean _transactionEnabled;

   public Boolean isEnabled() {
      return this._enabled;
   }

   public void setEnabled(Boolean var1) {
      this._enabled = var1;
   }

   public String getConnectionFactoryJndiName() {
      return this._connectionFactoryJndiName;
   }

   public void setConnectionFactoryJndiName(String var1) {
      this._connectionFactoryJndiName = var1;
   }

   public Boolean isTransactionEnabled() {
      return this._transactionEnabled;
   }

   public void setTransactionEnabled(Boolean var1) {
      this._transactionEnabled = var1;
   }
}
