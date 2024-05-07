package weblogic.jms.saf;

import java.util.Date;
import javax.management.openmbean.CompositeData;
import weblogic.management.ManagementException;
import weblogic.management.runtime.RuntimeMBean;
import weblogic.management.runtime.RuntimeMBeanDelegate;
import weblogic.management.runtime.SAFRemoteEndpointRuntimeMBean;
import weblogic.messaging.saf.SAFException;

public class SAFRemoteEndpointCustomizer extends RuntimeMBeanDelegate implements SAFRemoteEndpointRuntimeMBean {
   private final SAFRemoteEndpointRuntimeMBean delegate;

   public SAFRemoteEndpointCustomizer(String var1, RuntimeMBean var2, SAFRemoteEndpointRuntimeMBean var3) throws ManagementException {
      super(var1, var2, true);
      this.delegate = var3;
   }

   public SAFRemoteEndpointRuntimeMBean getDelegate() {
      return this.delegate;
   }

   public String getURL() {
      return this.delegate.getURL();
   }

   public CompositeData getMessage(String var1) throws ManagementException {
      return this.delegate.getMessage(var1);
   }

   public String getEndpointType() {
      return this.delegate.getEndpointType();
   }

   public void pauseIncoming() throws SAFException {
      this.delegate.pauseIncoming();
   }

   public void resumeIncoming() throws SAFException {
      this.delegate.resumeIncoming();
   }

   public boolean isPausedForIncoming() {
      return this.delegate.isPausedForIncoming();
   }

   public void pauseForwarding() throws SAFException {
      this.delegate.pauseForwarding();
   }

   public void resumeForwarding() throws SAFException {
      this.delegate.resumeForwarding();
   }

   public boolean isPausedForForwarding() {
      return this.delegate.isPausedForForwarding();
   }

   public void expireAll() {
      this.delegate.expireAll();
   }

   public void purge() throws SAFException {
      this.delegate.purge();
   }

   public String getMessages(String var1, Integer var2) throws ManagementException {
      return this.delegate.getMessages(var1, var2);
   }

   public long getMessagesCurrentCount() {
      return this.delegate.getMessagesCurrentCount();
   }

   public long getMessagesPendingCount() {
      return this.delegate.getMessagesPendingCount();
   }

   public long getMessagesHighCount() {
      return this.delegate.getMessagesHighCount();
   }

   public long getMessagesReceivedCount() {
      return this.delegate.getMessagesReceivedCount();
   }

   public long getMessagesThresholdTime() {
      return this.delegate.getMessagesThresholdTime();
   }

   public long getBytesPendingCount() {
      return this.delegate.getBytesPendingCount();
   }

   public long getBytesCurrentCount() {
      return this.delegate.getBytesCurrentCount();
   }

   public long getBytesHighCount() {
      return this.delegate.getBytesHighCount();
   }

   public long getBytesReceivedCount() {
      return this.delegate.getBytesReceivedCount();
   }

   public long getBytesThresholdTime() {
      return this.delegate.getBytesThresholdTime();
   }

   public long getFailedMessagesTotal() {
      return this.delegate.getFailedMessagesTotal();
   }

   public Long sort(String var1, Long var2, String[] var3, Boolean[] var4) throws ManagementException {
      return this.delegate.sort(var1, var2, var3, var4);
   }

   public CompositeData getMessage(String var1, String var2) throws ManagementException {
      return this.delegate.getMessage(var1, var2);
   }

   public CompositeData getMessage(String var1, Long var2) throws ManagementException {
      return this.delegate.getMessage(var1, var2);
   }

   public Long getCursorStartPosition(String var1) throws ManagementException {
      return this.delegate.getCursorStartPosition(var1);
   }

   public Long getCursorEndPosition(String var1) throws ManagementException {
      return this.delegate.getCursorEndPosition(var1);
   }

   public CompositeData[] getItems(String var1, Long var2, Integer var3) throws ManagementException {
      return this.delegate.getItems(var1, var2, var3);
   }

   public CompositeData[] getNext(String var1, Integer var2) throws ManagementException {
      return this.delegate.getNext(var1, var2);
   }

   public CompositeData[] getPrevious(String var1, Integer var2) throws ManagementException {
      return this.delegate.getPrevious(var1, var2);
   }

   public Long getCursorSize(String var1) throws ManagementException {
      return this.delegate.getCursorSize(var1);
   }

   public Void closeCursor(String var1) throws ManagementException {
      return this.delegate.closeCursor(var1);
   }

   public long getDowntimeHigh() {
      return this.delegate.getDowntimeHigh();
   }

   public long getDowntimeTotal() {
      return this.delegate.getDowntimeTotal();
   }

   public long getUptimeHigh() {
      return this.delegate.getUptimeHigh();
   }

   public long getUptimeTotal() {
      return this.delegate.getUptimeTotal();
   }

   public Date getLastTimeConnected() {
      return this.delegate.getLastTimeConnected();
   }

   public Date getLastTimeFailedToConnect() {
      return this.delegate.getLastTimeFailedToConnect();
   }

   public Exception getLastException() {
      return this.delegate.getLastException();
   }

   public String getOperationState() {
      return this.delegate.getOperationState().toString();
   }
}
