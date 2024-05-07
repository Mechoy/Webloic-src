package weblogic.management.runtime;

import weblogic.management.ManagementException;

public interface WLDFImageRuntimeMBean extends RuntimeMBean {
   WLDFImageCreationTaskRuntimeMBean captureImage() throws ManagementException;

   WLDFImageCreationTaskRuntimeMBean captureImage(String var1) throws ManagementException;

   WLDFImageCreationTaskRuntimeMBean captureImage(int var1) throws ManagementException;

   WLDFImageCreationTaskRuntimeMBean captureImage(String var1, int var2) throws ManagementException;

   WLDFImageCreationTaskRuntimeMBean[] listImageCaptureTasks();

   void clearCompletedImageCaptureTasks();

   void resetImageLockout();
}
