package weblogic.management.mbeanservers.edit.internal;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.management.ObjectName;
import javax.management.openmbean.OpenType;
import weblogic.descriptor.BeanUpdateEvent;
import weblogic.descriptor.DescriptorBean;
import weblogic.descriptor.internal.AbstractDescriptorBean;
import weblogic.descriptor.internal.ResolvedReference;
import weblogic.diagnostics.debug.DebugLogger;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.jmx.modelmbean.WLSModelMBeanContext;
import weblogic.management.mbeanservers.Service;
import weblogic.management.mbeanservers.edit.ActivationTaskMBean;
import weblogic.management.mbeanservers.edit.Change;
import weblogic.management.mbeanservers.edit.ConfigurationManagerMBean;
import weblogic.management.mbeanservers.edit.EditTimedOutException;
import weblogic.management.mbeanservers.edit.NotEditorException;
import weblogic.management.mbeanservers.edit.ValidationException;
import weblogic.management.mbeanservers.internal.ServiceImpl;
import weblogic.management.provider.ActivateTask;
import weblogic.management.provider.EditAccess;
import weblogic.management.provider.EditChangesValidationException;
import weblogic.management.provider.EditFailedException;
import weblogic.management.provider.EditNotEditorException;
import weblogic.management.provider.EditSaveChangesFailedException;
import weblogic.management.provider.EditWaitTimedOutException;
import weblogic.timers.Timer;
import weblogic.timers.TimerListener;
import weblogic.timers.TimerManager;
import weblogic.timers.TimerManagerFactory;

public class ConfigurationManagerMBeanImpl extends ServiceImpl implements ConfigurationManagerMBean, TimerListener {
   private static DebugLogger debugLogger = DebugLogger.getDebugLogger("DebugJMXEdit");
   private EditAccess edit;
   private WLSModelMBeanContext context;
   private List completedTasks;
   private long completedActivationTasksCount = 10L;
   private List uncompletedTasks;
   private TimerManager timerManager;
   private Timer timer;
   public static final long STATUS_CHECK_INTERVAL = 60000L;

   ConfigurationManagerMBeanImpl(EditAccess var1, WLSModelMBeanContext var2) {
      super("ConfigurationManager", ConfigurationManagerMBean.class.getName(), (Service)null);
      if (var1 == null) {
         throw new AssertionError("EditAccess should not be null");
      } else {
         this.edit = var1;
         this.context = var2;
         this.completedTasks = Collections.synchronizedList(new ArrayList());
         this.uncompletedTasks = Collections.synchronizedList(new ArrayList());
         this.timerManager = TimerManagerFactory.getTimerManagerFactory().getDefaultTimerManager();
      }
   }

   public DomainMBean startEdit(int var1, int var2) throws EditTimedOutException {
      return this.startEdit(var1, var2, false);
   }

   public DomainMBean startEdit(int var1, int var2, boolean var3) throws EditTimedOutException {
      try {
         return this.edit.startEdit(var1, var2, var3);
      } catch (EditWaitTimedOutException var5) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Timed out starting edit ", var5);
         }

         throw new EditTimedOutException(var5);
      } catch (EditFailedException var6) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception starting edit ", var6);
         }

         throw new RuntimeException(var6);
      }
   }

   public void stopEdit() throws NotEditorException {
      try {
         this.edit.stopEdit();
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Not editor for stop ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditFailedException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception stopping edit ", var3);
         }

         throw new RuntimeException(var3);
      }
   }

   public void cancelEdit() {
      try {
         this.edit.cancelEdit();
      } catch (EditFailedException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception canceling edit ", var2);
         }

         throw new RuntimeException(var2);
      }
   }

   public String getCurrentEditor() {
      return this.edit.getEditor();
   }

   public boolean isEditor() {
      return this.edit.isEditor();
   }

   public long getCurrentEditorStartTime() {
      return this.edit.getEditorStartTime();
   }

   public long getCurrentEditorExpirationTime() {
      return this.edit.getEditorExpirationTime();
   }

   public boolean isCurrentEditorExclusive() {
      return this.edit.isEditorExclusive();
   }

   public boolean isCurrentEditorExpired() {
      long var1 = this.edit.getEditorExpirationTime();
      if (var1 <= 0L) {
         return false;
      } else {
         return var1 <= System.currentTimeMillis();
      }
   }

   public Change[] getChanges() throws NotEditorException {
      try {
         Iterator var1 = this.edit.getUnsavedChanges();
         return this.convertBeanUpdatesToChanges(var1);
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Getting changes not editor ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditFailedException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception getting changes ", var3);
         }

         throw new RuntimeException(var3);
      }
   }

   public void validate() throws NotEditorException, ValidationException {
      try {
         this.edit.validateChanges();
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Validating changes not editor ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditChangesValidationException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception validating changes ", var3);
         }

         throw new ValidationException(var3);
      }
   }

   public void reload() throws NotEditorException, ValidationException {
      try {
         this.edit.reload();
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Reloading changes not editor ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditChangesValidationException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception reloading changes ", var3);
         }

         throw new ValidationException(var3);
      }
   }

   public void save() throws NotEditorException, ValidationException {
      try {
         this.edit.saveChanges();
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Saving changes not editor ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditSaveChangesFailedException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Saving changes failed ", var3);
         }

         throw new RuntimeException(var3);
      } catch (EditChangesValidationException var4) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception validating changes ", var4);
         }

         throw new ValidationException(var4);
      }
   }

   public void undo() throws NotEditorException {
      try {
         this.removeReferences(this.edit.getUnsavedChanges());
         this.edit.undoUnsavedChanges();
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Undo changes not editor ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditFailedException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception undoing changes ", var3);
         }

         throw new RuntimeException(var3);
      }
   }

   public boolean haveUnactivatedChanges() {
      if (this.edit.isPendingChange()) {
         return true;
      } else if (!this.edit.isModified()) {
         return false;
      } else {
         try {
            Iterator var1 = this.edit.getUnactivatedChanges();
            return var1.hasNext();
         } catch (Exception var2) {
            return false;
         }
      }
   }

   public Change[] getUnactivatedChanges() throws NotEditorException {
      try {
         Iterator var1 = this.edit.getUnactivatedChanges();
         return this.convertBeanUpdatesToChanges(var1);
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Getting unactivated changes not editor ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditFailedException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception getting unactivated changes ", var3);
         }

         throw new RuntimeException(var3);
      }
   }

   public void undoUnactivatedChanges() throws NotEditorException {
      try {
         this.removeReferences(this.edit.getUnactivatedChanges());
         this.edit.undoUnactivatedChanges();
      } catch (EditNotEditorException var2) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Undo unactivated changes not editor ", var2);
         }

         throw new NotEditorException(var2);
      } catch (EditFailedException var3) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception undoing unactivated changes ", var3);
         }

         throw new RuntimeException(var3);
      }
   }

   public ActivationTaskMBean activate(long var1) throws NotEditorException {
      try {
         ActivateTask var3 = null;
         if (var1 == 0L) {
            var3 = this.edit.activateChanges(Long.MAX_VALUE);
         } else {
            if (var1 == -1L) {
               var1 = Long.MAX_VALUE;
            }

            var3 = this.edit.activateChangesAndWaitForCompletion(var1);
         }

         ActivationTaskMBeanImpl var4 = new ActivationTaskMBeanImpl(this, var3);
         this.uncompletedTasks.add(var4);
         synchronized(this) {
            if (this.timer == null) {
               this.timer = this.timerManager.schedule(this, 60000L, 60000L);
            }
         }

         return var4;
      } catch (EditNotEditorException var8) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Activate changes not editor ", var8);
         }

         throw new NotEditorException(var8);
      } catch (EditFailedException var9) {
         debugLogger.debug("Exception activating changes ", var9);
         throw new RuntimeException(var9);
      }
   }

   public ActivationTaskMBean[] getCompletedActivationTasks() {
      this.moveCompletedTasksToCompletedList();
      ActivationTaskMBean[] var1 = new ActivationTaskMBean[this.completedTasks.size()];
      if (this.completedTasks.size() == 0) {
         return var1;
      } else {
         this.completedTasks.toArray(var1);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Get all completed tasks" + var1);
         }

         return var1;
      }
   }

   public long getCompletedActivationTasksCount() {
      return this.completedActivationTasksCount;
   }

   public void setCompletedActivationTasksCount(long var1) {
      this.completedActivationTasksCount = var1;
   }

   public ActivationTaskMBean[] getActivationTasks() {
      ArrayList var1 = new ArrayList();
      var1.addAll(this.completedTasks);
      var1.addAll(this.uncompletedTasks);
      ActivationTaskMBean[] var2 = new ActivationTaskMBean[var1.size()];
      return (ActivationTaskMBean[])((ActivationTaskMBean[])var1.toArray(var2));
   }

   public ActivationTaskMBean[] getActiveActivationTasks() {
      this.moveCompletedTasksToCompletedList();
      ActivationTaskMBean[] var1 = new ActivationTaskMBean[this.uncompletedTasks.size()];
      if (this.uncompletedTasks.size() == 0) {
         return var1;
      } else {
         this.uncompletedTasks.toArray(var1);
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Get all active tasks" + var1);
         }

         return var1;
      }
   }

   public void purgeCompletedActivationTasks() {
      this.moveCompletedTasksToCompletedList();
      if (debugLogger.isDebugEnabled()) {
         debugLogger.debug("Purging all completed tasks");
      }

      this.completedTasks.clear();
   }

   public Change[] getChangesToDestroyBean(DescriptorBean var1) {
      if (var1 == null) {
         return new Change[0];
      } else {
         try {
            List var2 = var1.getDescriptor().getResolvedReferences(var1);
            if (var2 == null) {
               return new Change[0];
            } else {
               Object[] var3 = var2.toArray();
               Change[] var4 = new Change[var3.length];

               for(int var5 = 0; var5 < var3.length; ++var5) {
                  ResolvedReference var6 = (ResolvedReference)var3[var5];
                  DescriptorBean var7 = var6.getBean();
                  String var8 = var6.getPropertyName();
                  if (var8.indexOf(47) != -1) {
                     var8 = var8.substring(var8.lastIndexOf(47) + 1);
                  }

                  Object var9 = this.context.mapToJMX(this.getType(var7), var7, (OpenType)null);
                  BeanInfo var10 = this.edit.getBeanInfo(var7);
                  PropertyDescriptor var11 = this.edit.getPropertyDescriptor(var10, var8);
                  Method var12 = var11.getReadMethod();
                  Object var13 = var12.invoke(var7, (Object[])null);
                  var13 = this.mapValueToJMX(var13);
                  Object var14 = null;
                  boolean var15 = this.edit.getRestartValue(var11);
                  if (var13 != null && var13 instanceof Object[] && ((Object[])((Object[])var13)).length > 0) {
                     var13 = this.mapValueToJMX(var1);
                     var4[var5] = new ChangeImpl(var9, var8, "remove", var13, var14, var15);
                  } else {
                     var4[var5] = new ChangeImpl(var9, var8, "modify", var13, var14, var15);
                  }
               }

               return var4;
            }
         } catch (Exception var16) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception getting reference changes ", var16);
            }

            throw new RuntimeException(var16);
         }
      }
   }

   public void removeReferencesToBean(DescriptorBean var1) throws NotEditorException {
      if (!this.isEditor()) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("removeReferencesToBean - not editor ");
         }

         throw new NotEditorException("Not edit lock owner");
      } else if (var1 != null) {
         try {
            List var2 = var1.getDescriptor().getResolvedReferences(var1);
            if (var2 != null) {
               Object[] var3 = var2.toArray();

               for(int var4 = 0; var4 < var3.length; ++var4) {
                  ResolvedReference var5 = (ResolvedReference)var3[var4];
                  DescriptorBean var6 = var5.getBean();
                  String var7 = var5.getPropertyName();
                  if (var7.indexOf(47) != -1) {
                     var7 = var7.substring(var7.lastIndexOf(47) + 1);
                  }

                  BeanInfo var8 = this.edit.getBeanInfo(var6);
                  PropertyDescriptor var9 = this.edit.getPropertyDescriptor(var8, var7);
                  Method var10 = var9.getReadMethod();
                  Method var11 = var9.getWriteMethod();
                  Object var12 = var10.invoke(var6, (Object[])null);
                  if (var12 != null && var12 instanceof Object[] && ((Object[])((Object[])var12)).length > 0) {
                     Object[] var13 = this.removeBeanFromArray((Object[])((Object[])var12), var1);
                     var11.invoke(var6, (Object)var13);
                  } else {
                     var11.invoke(var6, null);
                     var6.unSet(var7);
                  }
               }

            }
         } catch (Exception var14) {
            if (debugLogger.isDebugEnabled()) {
               debugLogger.debug("Exception removing references ", var14);
            }

            throw new RuntimeException(var14);
         }
      }
   }

   private Object[] removeBeanFromArray(Object[] var1, DescriptorBean var2) {
      int var3 = 0;

      for(int var4 = 0; var4 < var1.length; ++var4) {
         if (!var2.equals(var1[var4])) {
            ++var3;
         }
      }

      Object[] var7 = (Object[])((Object[])Array.newInstance(var1.getClass().getComponentType(), var3));
      int var5 = 0;

      for(int var6 = 0; var6 < var1.length; ++var6) {
         if (!var2.equals(var1[var6])) {
            var7[var5++] = var1[var6];
         }
      }

      return var7;
   }

   private synchronized void moveCompletedTasksToCompletedList() {
      if (this.uncompletedTasks.size() == 0) {
         if (this.timer != null) {
            this.timer.cancel();
            this.timer = null;
         }

      } else {
         synchronized(this.uncompletedTasks) {
            Iterator var2 = this.uncompletedTasks.iterator();

            while(var2 != null && var2.hasNext()) {
               ActivationTaskMBeanImpl var3 = (ActivationTaskMBeanImpl)var2.next();
               if (var3.getState() == 4 || var3.getState() == 5) {
                  var2.remove();
                  if (this.getCompletedActivationTasksCount() > 0L) {
                     var3.movingToCompleted();
                     this.completedTasks.add(var3);

                     while((long)this.completedTasks.size() > this.getCompletedActivationTasksCount()) {
                        ActivationTaskMBean var4 = (ActivationTaskMBean)this.completedTasks.remove(0);
                        this.context.unregister(var4);
                     }
                  } else {
                     this.context.unregister(var3);
                  }
               }
            }

         }
      }
   }

   public Change[] convertBeanUpdatesToChanges(Iterator var1) {
      try {
         Vector var2 = new Vector();

         while(var1.hasNext()) {
            BeanUpdateEvent var3 = (BeanUpdateEvent)var1.next();
            DescriptorBean var4 = var3.getSourceBean();
            DescriptorBean var5 = var3.getProposedBean();
            BeanInfo var6 = this.edit.getBeanInfo(var5);
            Object var7 = this.context.mapToJMX(this.getType(var5), var5, (OpenType)null);
            BeanUpdateEvent.PropertyUpdate[] var8 = var3.getUpdateList();

            for(int var9 = 0; var9 < var8.length; ++var9) {
               BeanUpdateEvent.PropertyUpdate var10 = var8[var9];
               String var11 = var10.getPropertyName();
               AbstractDescriptorBean var13 = null;
               Object var14 = null;
               Object var15 = null;
               PropertyDescriptor var16 = this.edit.getPropertyDescriptor(var6, var11);
               boolean var17 = !var10.isDynamic();
               boolean var18 = var10.isUnsetUpdate();
               if (var16 == null) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Skipping update for internal property name " + var11 + " update:" + var10);
                  }
               } else {
                  Method var19 = var16.getReadMethod();
                  String var20 = (String)var16.getValue("relationship");
                  boolean var21 = true;
                  if (var20 != null && var20.equals("containment")) {
                     var21 = false;
                  }

                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Processing update for property name " + var11 + " update:" + var10);
                  }

                  String var12;
                  switch (var10.getUpdateType()) {
                     case 1:
                        if (var18) {
                           var12 = "unset";
                        } else {
                           var12 = "modify";
                        }

                        var14 = var19.invoke(var4, (Object[])null);
                        var14 = this.mapValueToJMX(var14);
                        var15 = var19.invoke(var5, (Object[])null);
                        var15 = this.mapValueToJMX(var15);
                        break;
                     case 2:
                        if (var21) {
                           var12 = "add";
                        } else {
                           var12 = "create";
                        }

                        var15 = var10.getAddedObject();
                        Object var22 = var19.invoke(var5, (Object[])null);
                        if (var15 instanceof AbstractDescriptorBean && var22 instanceof AbstractDescriptorBean) {
                           var13 = (AbstractDescriptorBean)var22;
                        }

                        if (var15 instanceof AbstractDescriptorBean && var22 instanceof DescriptorBean[]) {
                           DescriptorBean[] var23 = (DescriptorBean[])((DescriptorBean[])var22);
                           AbstractDescriptorBean var24 = (AbstractDescriptorBean)var15;

                           for(int var25 = 0; var25 < var23.length; ++var25) {
                              AbstractDescriptorBean var26 = (AbstractDescriptorBean)var23[var25];
                              if (var24._getKey().equals(var26._getKey())) {
                                 var13 = var26;
                              }
                           }
                        }

                        var15 = this.mapValueToJMX(var15);
                        break;
                     case 3:
                        if (var21) {
                           var12 = "remove";
                        } else {
                           var12 = "destroy";
                        }

                        var14 = var10.getRemovedObject();
                        var14 = this.mapValueToJMX(var14);
                        break;
                     default:
                        throw new AssertionError("Unexpected updateType:" + var10.getUpdateType());
                  }

                  ChangeImpl var29 = new ChangeImpl(var7, var11, var12, var14, var15, var17);
                  var2.add(var29);
                  if (var13 != null) {
                     this.addModifiesForNewBean(var13, var2, var5, var4, var17);
                  }
               }
            }
         }

         Change[] var28 = new Change[var2.size()];
         var2.toArray(var28);
         return var28;
      } catch (Exception var27) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception converting bean events ", var27);
         }

         throw new RuntimeException(var27);
      }
   }

   public void addModifiesForNewBean(AbstractDescriptorBean var1, Vector var2, DescriptorBean var3, DescriptorBean var4, boolean var5) {
      try {
         BeanInfo var6 = this.edit.getBeanInfo(var1);
         Object var7 = this.context.mapToJMX(ObjectName.class, var1, (OpenType)null);
         PropertyDescriptor[] var8 = var6.getPropertyDescriptors();

         for(int var9 = 0; var8 != null && var9 < var8.length; ++var9) {
            PropertyDescriptor var10 = var8[var9];
            Boolean var11 = (Boolean)var10.getValue("key");
            if ((var11 == null || !var11) && var10.getWriteMethod() != null && var1.isSet(var10.getName())) {
               Method var12 = var10.getReadMethod();
               if (debugLogger.isDebugEnabled()) {
                  debugLogger.debug("Getter method is " + var12);
                  debugLogger.debug("Source bean is " + var4);
               }

               Object var13 = var12.invoke(var1, (Object[])null);
               Object var14 = var10.getValue("default");
               if (var14 == null && var13 == null || var14 != null && var14.equals(var13)) {
                  if (debugLogger.isDebugEnabled()) {
                     debugLogger.debug("Skipping default value " + var10.getName());
                  }
               } else if (var13 instanceof DescriptorBean[]) {
                  DescriptorBean[] var15 = (DescriptorBean[])((DescriptorBean[])var13);

                  for(int var16 = 0; var16 < var15.length; ++var16) {
                     var13 = this.mapValueToJMX(var15[var16]);
                     ChangeImpl var17 = new ChangeImpl(var7, var10.getName(), "add", (Object)null, var13, var5);
                     var2.add(var17);
                  }
               } else {
                  var13 = this.mapValueToJMX(var13);
                  ChangeImpl var19 = new ChangeImpl(var7, var10.getName(), "modify", (Object)null, var13, var5);
                  var2.add(var19);
               }
            }
         }

      } catch (Exception var18) {
         if (debugLogger.isDebugEnabled()) {
            debugLogger.debug("Exception add modifies for new bean ", var18);
         }

         throw new RuntimeException(var18);
      }
   }

   private Object mapValueToJMX(Object var1) {
      if (var1 instanceof AbstractDescriptorBean) {
         AbstractDescriptorBean var5 = (AbstractDescriptorBean)var1;
         return var5._getKey();
      } else if (var1 instanceof DescriptorBean[]) {
         DescriptorBean[] var2 = (DescriptorBean[])((DescriptorBean[])var1);
         String[] var3 = new String[var2.length];

         for(int var4 = 0; var4 < var2.length; ++var4) {
            if (var2[var4] != null) {
               var3[var4] = ((AbstractDescriptorBean)var2[var4])._getKey().toString();
            }
         }

         return var3;
      } else {
         return var1;
      }
   }

   private Class getType(Object var1) {
      Class var2 = var1.getClass();
      Class[] var3 = var2.getInterfaces();

      for(int var4 = 0; var4 < var3.length; ++var4) {
         Class var5 = var3[var4];
         if (var5 != DescriptorBean.class) {
            String var6 = var5.getName();
            if (var6.endsWith("Bean")) {
               return var5;
            }
         }
      }

      return var1.getClass();
   }

   public void timerExpired(Timer var1) {
      this.moveCompletedTasksToCompletedList();
   }

   private void removeReferences(Iterator var1) throws NotEditorException {
      if (var1 != null) {
         while(var1.hasNext()) {
            BeanUpdateEvent var2 = (BeanUpdateEvent)var1.next();
            BeanUpdateEvent.PropertyUpdate[] var3 = var2.getUpdateList();

            for(int var4 = 0; var4 < var3.length; ++var4) {
               BeanUpdateEvent.PropertyUpdate var5 = var3[var4];
               if (var5.getUpdateType() == 2) {
                  Object var6 = var5.getAddedObject();
                  if (var6 instanceof DescriptorBean) {
                     this.removeReferencesToBean((DescriptorBean)var6);
                  }
               }
            }
         }

      }
   }
}
