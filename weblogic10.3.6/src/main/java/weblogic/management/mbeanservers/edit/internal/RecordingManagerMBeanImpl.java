package weblogic.management.mbeanservers.edit.internal;

import java.util.Map;
import weblogic.management.mbeanservers.Service;
import weblogic.management.mbeanservers.edit.RecordingException;
import weblogic.management.mbeanservers.edit.RecordingManagerMBean;
import weblogic.management.mbeanservers.internal.RecordingManager;
import weblogic.management.mbeanservers.internal.ServiceImpl;

public class RecordingManagerMBeanImpl extends ServiceImpl implements RecordingManagerMBean {
   RecordingManagerMBeanImpl() {
      super("RecordingManager", RecordingManagerMBean.class.getName(), (Service)null);
   }

   public void startRecording(String var1, boolean var2) throws RecordingException {
      RecordingManager.getInstance().startRecording(var1, var2);
   }

   public void startRecording(String var1, Map var2) throws RecordingException {
      RecordingManager.getInstance().startRecording(var1, var2);
   }

   public void stopRecording() throws RecordingException {
      RecordingManager.getInstance().stopRecording();
   }

   public boolean isRecording() {
      return RecordingManager.getInstance().isRecording();
   }

   public String getRecordingFileName() {
      return RecordingManager.getInstance().getRecordingFileName();
   }

   public void record(String var1) throws RecordingException {
      RecordingManager.getInstance().record(var1);
   }
}
