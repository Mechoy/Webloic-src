package weblogic.servlet.internal;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.ManagementException;
import weblogic.management.runtime.PageFlowActionRuntimeMBean;
import weblogic.management.runtime.PageFlowRuntimeMBean;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.utils.AssertionError;

public class PageFlowRuntimeMBeanImpl extends RuntimeMBeanDelegate implements PageFlowRuntimeMBean {
   private static final long serialVersionUID = 1L;
   private String _className;
   private HashMap _actions;
   private long _lastResetTime;
   private long _createCount;
   private long _destroyCount;
   private int _numExceptionsToKeep;
   private LinkedList _reportedExceptions;
   private DebugLogger _logger;

   public PageFlowRuntimeMBeanImpl(String var1, String var2, String var3, RuntimeMBean var4) throws ManagementException {
      super(var3, var4);
      this._className = var3;
      this._actions = new HashMap();
      this._reportedExceptions = new LinkedList();
      this._numExceptionsToKeep = 5;
      this._logger = PageFlowsRuntimeMBeanImpl.logger;
   }

   public PageFlowRuntimeMBeanImpl() throws ManagementException {
      throw new AssertionError("Public constructor provided only for JMX compliance.");
   }

   public void reset() {
      this._lastResetTime = System.currentTimeMillis();
      this._createCount = 0L;
      this._destroyCount = 0L;
      if (this._actions.size() != 0) {
         Iterator var1 = this._actions.values().iterator();

         while(var1.hasNext()) {
            PageFlowActionRuntimeMBeanImpl var2 = (PageFlowActionRuntimeMBeanImpl)var1.next();
            var2.reset();
         }

      }
   }

   public String getClassName() {
      return this._className;
   }

   public PageFlowActionRuntimeMBean[] getActions() {
      PageFlowActionRuntimeMBean[] var1 = new PageFlowActionRuntimeMBean[this._actions.size()];
      this._actions.values().toArray(var1);
      return var1;
   }

   public PageFlowActionRuntimeMBean getAction(String var1) {
      return (PageFlowActionRuntimeMBean)this._actions.get(var1);
   }

   public void setActions(String[] var1) {
      assert var1 != null;

      for(int var2 = 0; var2 < var1.length; ++var2) {
         String var3 = var1[var2];

         try {
            PageFlowActionRuntimeMBeanImpl var4 = new PageFlowActionRuntimeMBeanImpl(var3, this);
            this._actions.put(var3, var4);
         } catch (ManagementException var6) {
            this._logger.debug("Unable to create MBean for " + var3, var6);
         }
      }

   }

   public void reportCreate() {
      ++this._createCount;
   }

   public void reportDestroy() {
      ++this._destroyCount;
   }

   public void setNumExceptionsToKeep(int var1) {
      this._numExceptionsToKeep = var1;
      Iterator var2 = this._actions.values().iterator();

      while(var2.hasNext()) {
         PageFlowActionRuntimeMBeanImpl var3 = (PageFlowActionRuntimeMBeanImpl)var2.next();
         if (var3 != null) {
            var3.setNumExceptionsToKeep(var1);
         }
      }

   }

   public void reportSuccess(String var1, long var2) {
      PageFlowActionRuntimeMBeanImpl var4 = (PageFlowActionRuntimeMBeanImpl)this.getAction(var1);
      if (var4 != null) {
         var4.reportSuccess(var2);
      }

   }

   public void reportException(String var1, Throwable var2) {
      this.reportException(var1, new PageFlowErrorImpl(var2));
   }

   public void reportException(String var1, PageFlowErrorImpl var2) {
      PageFlowActionRuntimeMBeanImpl var3 = (PageFlowActionRuntimeMBeanImpl)this.getAction(var1);
      if (var3 != null && var2 != null) {
         var3.reportException(var2);
      }

   }

   public void reportExceptionHandled(String var1, Throwable var2, long var3) {
      PageFlowActionRuntimeMBeanImpl var5 = (PageFlowActionRuntimeMBeanImpl)this.getAction(var1);
      if (var5 != null) {
         var5.reportHandledException(var2, var3);
      }

   }

   public long getCreateCount() {
      return this._createCount;
   }

   public long getDestroyCount() {
      return this._destroyCount;
   }

   public long getLastResetTime() {
      return this._lastResetTime;
   }
}
