package weblogic.management.mbeanservers.edit;

import weblogic.descriptor.DescriptorBean;
import weblogic.management.configuration.DomainMBean;
import weblogic.management.mbeanservers.Service;

public interface ConfigurationManagerMBean extends Service {
   String OBJECT_NAME = "com.bea:Name=ConfigurationManager,Type=" + ConfigurationManagerMBean.class.getName();

   DomainMBean startEdit(int var1, int var2) throws EditTimedOutException;

   DomainMBean startEdit(int var1, int var2, boolean var3) throws EditTimedOutException;

   void stopEdit() throws NotEditorException;

   void cancelEdit();

   String getCurrentEditor();

   boolean isEditor();

   long getCurrentEditorStartTime();

   long getCurrentEditorExpirationTime();

   boolean isCurrentEditorExclusive();

   boolean isCurrentEditorExpired();

   Change[] getChanges() throws NotEditorException;

   void validate() throws NotEditorException, ValidationException;

   void reload() throws NotEditorException, ValidationException;

   void save() throws NotEditorException, ValidationException;

   void undo() throws NotEditorException;

   boolean haveUnactivatedChanges();

   Change[] getUnactivatedChanges() throws NotEditorException;

   void undoUnactivatedChanges() throws NotEditorException;

   ActivationTaskMBean activate(long var1) throws NotEditorException;

   ActivationTaskMBean[] getCompletedActivationTasks();

   long getCompletedActivationTasksCount();

   void setCompletedActivationTasksCount(long var1);

   ActivationTaskMBean[] getActiveActivationTasks();

   void purgeCompletedActivationTasks();

   ActivationTaskMBean[] getActivationTasks();

   Change[] getChangesToDestroyBean(DescriptorBean var1);

   void removeReferencesToBean(DescriptorBean var1) throws NotEditorException;
}
