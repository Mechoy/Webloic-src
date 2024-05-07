package weblogic.management.provider;

import java.beans.BeanInfo;
import java.beans.PropertyDescriptor;
import java.util.Iterator;
import weblogic.descriptor.DescriptorBean;
import weblogic.management.configuration.DomainMBean;

public interface EditAccess {
   DomainMBean startEdit(int var1, int var2) throws EditWaitTimedOutException, EditFailedException;

   DomainMBean startEdit(int var1, int var2, boolean var3) throws EditWaitTimedOutException, EditFailedException;

   DomainMBean getDomainBean() throws EditNotEditorException, EditFailedException;

   DomainMBean getCurrentDomainBean() throws EditNotEditorException, EditFailedException;

   DomainMBean getDomainBeanWithoutLock() throws EditFailedException;

   boolean isDomainBeanTreeLoaded();

   void stopEdit() throws EditNotEditorException, EditFailedException;

   void cancelEdit() throws EditFailedException;

   void undoUnsavedChanges() throws EditNotEditorException, EditFailedException;

   Iterator getUnsavedChanges() throws EditNotEditorException, EditFailedException;

   String getEditor();

   boolean isEditor();

   long getEditorStartTime();

   long getEditorExpirationTime();

   boolean isEditorExclusive();

   void validateChanges() throws EditNotEditorException, EditChangesValidationException;

   void reload() throws EditNotEditorException, EditChangesValidationException;

   void saveChanges() throws EditNotEditorException, EditSaveChangesFailedException, EditChangesValidationException;

   ActivateTask activateChanges(long var1) throws EditNotEditorException, EditFailedException;

   ActivateTask activateChangesAndWaitForCompletion(long var1) throws EditNotEditorException, EditFailedException;

   void undoUnactivatedChanges() throws EditNotEditorException, EditFailedException;

   Iterator getUnactivatedChanges() throws EditNotEditorException, EditFailedException;

   boolean isModified();

   boolean isPendingChange();

   void cancelActivate() throws EditFailedException;

   BeanInfo getBeanInfo(DescriptorBean var1);

   PropertyDescriptor getPropertyDescriptor(BeanInfo var1, String var2);

   boolean getRestartValue(PropertyDescriptor var1);

   void shutdown();
}
