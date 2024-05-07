package weblogic.management.runtime;

public interface WseePortRuntimeMBean extends WseeBasePortRuntimeMBean {
   WseeOperationRuntimeMBean[] getOperations();

   String getPolicySubjectResourcePattern();

   String getPolicySubjectName();

   String getPolicySubjectType();

   String getPolicyAttachmentSupport();
}
