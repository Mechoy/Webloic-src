package weblogic.deploy.internal.targetserver.datamanagement;

import java.util.List;

public interface DataUpdateRequestInfo {
   List getDeltaFiles();

   long getRequestId();

   boolean isStatic();

   boolean isDelete();

   boolean isPlanUpdate();
}
