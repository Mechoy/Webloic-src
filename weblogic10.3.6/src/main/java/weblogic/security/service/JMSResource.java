package weblogic.security.service;

import com.bea.common.security.SecurityLogger;
import weblogic.security.spi.Resource;

public final class JMSResource extends ResourceBase {
   private static final String ACTION = "action";
   private static final String MODULE = "module";
   private static final String[] KEYS = new String[]{"application", "module", "destinationType", "resource", "action"};
   private static final int REPEATING_FIELD_INDEX = 4;
   private static final int REPEATING_FIELD_TERMINATING_INDEX = 0;
   private static final Resource TOP = new JMSResource((String[])null, 0, (String)null, false);
   private String action;
   private boolean showAction;

   public JMSResource(String application, String module, String destType, String resource, String action) throws InvalidParameterException {
      this.action = null;
      this.showAction = false;
      this.initialize(application, module, destType, resource, action);
   }

   /** @deprecated */
   public JMSResource(String application, String destType, String resource, String action) throws InvalidParameterException {
      this(application, (String)null, destType, resource, action);
   }

   /** @deprecated */
   public void initialize(String application, String destType, String resource, String action) {
      this.initialize(application, (String)null, destType, resource, action);
   }

   private void initialize(String application, String module, String destType, String resource, String action) {
      if (application != null && application.length() == 0) {
         throw new InvalidParameterException(SecurityLogger.getEmptyResourceKeyString("JMS", KEYS[0]));
      } else {
         String[] vals = new String[]{application, module, destType, resource};
         this.init(vals, vals.length, action, action != null);
      }
   }

   private JMSResource(String[] values, int length, String action, boolean showAction) {
      this.action = null;
      this.showAction = false;
      this.init(values, length, action, showAction);
   }

   private void init(String[] values, int length, String action, boolean showAction) {
      this.action = action;
      this.showAction = showAction;
      this.init(values, length, getSeed(this.action, this.showAction));
   }

   private static long getSeed(String action, boolean showAction) {
      return (action != null ? (long)("action".hashCode() + action.hashCode()) : 0L) * (long)(showAction ? 1 : -1);
   }

   public String getType() {
      return "<jms>";
   }

   protected Resource makeParent() {
      if (this.length == 0) {
         return this.showAction ? TOP : null;
      } else {
         String act = this.action;
         if (SCOPE_RESOURCE_ACTION) {
            act = null;
         }

         boolean showAct = !this.showAction && this.action != null;
         int len = this.showAction ? this.length : this.length - 1;
         Resource parent = TOP;
         if (len > 0 || showAct) {
            parent = new JMSResource(this.values, len, act, showAct);
         }

         if (this.length == 1 && !this.showAction) {
            parent = new ApplicationResource(this.values[0], (Resource)parent);
         }

         return (Resource)parent;
      }
   }

   public int getRepeatingFieldIndex() {
      return 4;
   }

   public int getRepeatingFieldTerminatingIndex() {
      return 0;
   }

   public boolean isTransitiveField(String fieldName) {
      return "module".equals(fieldName);
   }

   public String[] getKeys() {
      return KEYS;
   }

   public String getDestinationType() {
      return this.length > 2 ? this.values[2] : null;
   }

   public String getResourceName() {
      return this.length > 3 ? this.values[3] : null;
   }

   public String getActionName() {
      return this.showAction ? this.action : null;
   }

   public String getApplicationName() {
      return this.length > 0 ? this.values[0] : null;
   }

   public String getModule() {
      return this.length > 1 ? this.values[1] : null;
   }

   protected void writeResourceString(StringBuffer buf) {
      super.writeResourceString(buf);
      if (this.showAction) {
         buf.append(", ").append("action").append('=');
         appendValue(buf, this.action);
      }

   }

   public boolean equals(Object obj) {
      if (!super.equals(obj)) {
         return false;
      } else {
         JMSResource r = (JMSResource)obj;
         return this.showAction == r.showAction && (this.action == null && r.action == null || this.action != null && this.action.equals(r.action));
      }
   }
}
