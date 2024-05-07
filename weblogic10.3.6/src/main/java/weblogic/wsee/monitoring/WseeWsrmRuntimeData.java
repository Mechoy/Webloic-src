package weblogic.wsee.monitoring;

import com.sun.istack.Nullable;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import weblogic.management.ManagementException;
import weblogic.management.runtime.WsrmRequestInfo;
import weblogic.management.runtime.WsrmSequenceInfo;
import weblogic.wsee.reliability.WsrmConstants;
import weblogic.wsee.reliability2.sequence.DestinationMessageInfo;
import weblogic.wsee.reliability2.sequence.DestinationOfferSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequence;
import weblogic.wsee.reliability2.sequence.DestinationSequenceManager;
import weblogic.wsee.reliability2.sequence.MessageInfo;
import weblogic.wsee.reliability2.sequence.OfferSequence;
import weblogic.wsee.reliability2.sequence.Sequence;
import weblogic.wsee.reliability2.sequence.SourceMessageInfo;
import weblogic.wsee.reliability2.sequence.SourceOfferSequence;
import weblogic.wsee.reliability2.sequence.SourceSequence;
import weblogic.wsee.reliability2.sequence.SourceSequenceManager;
import weblogic.wsee.reliability2.sequence.UnknownDestinationSequenceException;
import weblogic.wsee.reliability2.sequence.UnknownSourceSequenceException;

public final class WseeWsrmRuntimeData extends WseeBaseRuntimeData {
   private boolean _global;
   private List<String> _seqIds;

   public WseeWsrmRuntimeData(String var1, boolean var2, @Nullable WseeBaseRuntimeData var3) throws ManagementException {
      super(var1, var3);
      this._global = var2;
      this._seqIds = new ArrayList();
   }

   public boolean isGlobal() {
      return this._global;
   }

   public void addSequenceId(String var1) {
      if (!this._global) {
         this._seqIds.add(var1);
      }

   }

   public void removeSequenceId(String var1) {
      if (!this._global) {
         this._seqIds.remove(var1);
      }

   }

   public List<String> getSequenceIDList() {
      return this._global ? this.getGlobalSequenceIDList() : this.getLocalSequenceIDList();
   }

   private List<String> getGlobalSequenceIDList() {
      ArrayList var1 = new ArrayList();
      Iterator var2 = SourceSequenceManager.getInstance().listSequences();

      while(var2.hasNext()) {
         SourceSequence var3 = (SourceSequence)var2.next();
         var1.add(var3.getId());
      }

      Iterator var5 = DestinationSequenceManager.getInstance().listSequences();

      while(var5.hasNext()) {
         DestinationSequence var4 = (DestinationSequence)var5.next();
         var1.add(var4.getId());
      }

      return var1;
   }

   private List<String> getLocalSequenceIDList() {
      ArrayList var1 = new ArrayList(this._seqIds);
      Iterator var2 = var1.iterator();

      while(var2.hasNext()) {
         String var3 = (String)var2.next();
         boolean var4 = false;

         try {
            var4 = this.getSequence(var3) != null;
         } catch (Exception var6) {
         }

         if (!var4) {
            var2.remove();
         }
      }

      this._seqIds = new ArrayList(var1);
      return var1;
   }

   public String[] getSequenceIds() {
      List var1 = this.getSequenceIDList();
      return (String[])var1.toArray(new String[var1.size()]);
   }

   public CompositeData getSequenceInfo(String var1) throws ManagementException {
      try {
         Sequence var2 = this.getSequence(var1);
         if (var2 == null) {
            return null;
         } else {
            WsrmSequenceInfoImpl var3 = new WsrmSequenceInfoImpl(var2);
            return var3.createCompositeData();
         }
      } catch (Exception var4) {
         throw new ManagementException(var4.toString(), var4);
      }
   }

   private Sequence getSequence(String var1) throws UnknownSourceSequenceException, UnknownDestinationSequenceException {
      Object var2 = null;
      SourceSequence var3 = SourceSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.RM_11, var1, true);
      if (var3 == null || !var3.getId().equals(var1)) {
         DestinationSequence var4 = DestinationSequenceManager.getInstance().getSequence(WsrmConstants.RMVersion.RM_11, var1, true);
         if (var4 != null) {
            var2 = var4;
         }
      }

      if (var2 == null) {
         var2 = var3;
      }

      return (Sequence)var2;
   }

   public static class WsrmRequestInfoImpl implements WsrmRequestInfo, WseeCompositeDataFactory.WseeCompositable {
      private static final long serialVersionUID = 1L;
      private static WseeCompositeDataFactory _factory;
      private String _messageId;
      private long _seqNum;
      private String _soapAction;
      private long _timestamp;
      private boolean _ackFlag;
      private String _responseMessageId;
      private long _responseTimestamp;

      public WsrmRequestInfoImpl() {
      }

      public WsrmRequestInfoImpl(Sequence var1, MessageInfo var2) {
         this.initTransients();
         this._messageId = var2.getMessageId();
         this._seqNum = var2.getMessageNum();
         this._soapAction = var2.getSOAPAction();
         this._timestamp = var2.getTimestamp();
         this._ackFlag = var2.isAck();
         if (var1 instanceof SourceOfferSequence) {
            this._responseMessageId = null;
            this._responseTimestamp = 0L;
         } else {
            long var4;
            if (var1 instanceof SourceSequence) {
               DestinationOfferSequence var3 = ((SourceSequence)var1).getOffer();
               var4 = ((SourceMessageInfo)var2).getResponseMessageNum();
               if (var4 > 0L && var3 != null) {
                  this._responseMessageId = ((DestinationMessageInfo)var3.getRequest(var4)).getMessageId();
                  this._responseTimestamp = ((DestinationMessageInfo)var3.getRequest(var4)).getTimestamp();
               } else {
                  this._responseMessageId = null;
                  this._responseTimestamp = 0L;
               }
            } else if (var1 instanceof DestinationOfferSequence) {
               this._responseMessageId = null;
            } else if (var1 instanceof DestinationSequence) {
               SourceOfferSequence var6 = ((DestinationSequence)var1).getOffer();
               var4 = ((DestinationMessageInfo)var2).getResponseMsgNum();
               if (var4 > 0L && var6 != null) {
                  this._responseMessageId = ((SourceMessageInfo)var6.getRequest(var4)).getMessageId();
                  this._responseTimestamp = ((SourceMessageInfo)var6.getRequest(var4)).getTimestamp();
               } else {
                  this._responseMessageId = null;
                  this._responseTimestamp = 0L;
               }
            }
         }

      }

      public WsrmRequestInfoImpl(WsrmRequestInfoImpl var1) {
         this.initTransients();
         this._messageId = var1._messageId;
         this._seqNum = var1._seqNum;
         this._soapAction = var1._soapAction;
         this._timestamp = var1._timestamp;
         this._ackFlag = var1._ackFlag;
         this._responseMessageId = var1._responseMessageId;
         this._responseTimestamp = var1._responseTimestamp;
      }

      private void initTransients() {
      }

      public CompositeData createCompositeData() throws OpenDataException {
         return _factory.createCompositeData(this);
      }

      public String getMessageId() {
         return this._messageId;
      }

      public long getSeqNum() {
         return this._seqNum;
      }

      public void setSeqNum(int var1) {
         this._seqNum = (long)var1;
      }

      public String getSoapAction() {
         return this._soapAction;
      }

      public void setSoapAction(String var1) {
         this._soapAction = var1;
      }

      public long getTimestamp() {
         return this._timestamp;
      }

      public void setTimestamp(long var1) {
         this._timestamp = var1;
      }

      public boolean isAckFlag() {
         return this._ackFlag;
      }

      public void setAckFlag(boolean var1) {
         this._ackFlag = var1;
      }

      public String getResponseMessageId() {
         return this._responseMessageId;
      }

      public void setResponseMessageId(String var1) {
         this._responseMessageId = var1;
      }

      public long getResponseTimestamp() {
         return this._responseTimestamp;
      }

      public void setResponseTimestamp(long var1) {
         this._responseTimestamp = var1;
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         this.initTransients();
         var1.defaultReadObject();
      }

      static {
         try {
            _factory = new WseeCompositeDataFactory(WsrmRequestInfo.class);
         } catch (Exception var1) {
            throw new RuntimeException(var1.toString(), var1);
         }
      }
   }

   public static class WsrmSequenceInfoImpl implements WsrmSequenceInfo, WseeCompositeDataFactory.WseeCompositable {
      private static final long serialVersionUID = 1L;
      private static WseeCompositeDataFactory _factory;
      private String _id;
      private String _logicalStoreName;
      private String _physicalStoreName;
      private boolean _isSource;
      private String _destinationId;
      private boolean _isOffer;
      private String _mainSequenceId;
      private String _state;
      private long _creationTime;
      private long _lastActivityTime;
      private long _maxAge;
      private long _lastAckdMsgNum;
      private long _unackdCount;
      private Map<String, WsrmRequestInfoImpl> _requestInfoList;

      public WsrmSequenceInfoImpl() {
      }

      public WsrmSequenceInfoImpl(Sequence var1) {
         this.initTransients();
         this._id = var1.getId();
         this._logicalStoreName = var1.getLogicalStoreName();
         this._physicalStoreName = var1.getPhysicalStoreName();
         this._isSource = var1 instanceof SourceSequence;
         if (var1 instanceof OfferSequence) {
            this._isOffer = true;
            Sequence var2 = ((OfferSequence)var1).getMainSequence();
            this._mainSequenceId = var2 != null ? var2.getId() : null;
         }

         if (this._isSource && !this._isOffer && var1 instanceof SourceSequence) {
            this._destinationId = ((SourceSequence)var1).getDestinationId();
         }

         this._state = var1.getState().name();
         this._creationTime = var1.getTimestamp();
         this._lastActivityTime = var1.getLastActivityTime();
         this._maxAge = var1.getExpires() != null ? var1.getTimestamp() + var1.getExpires().getTimeInMillis(new Date()) : 0L;
         Map var6 = var1.getRequests();
         this._lastAckdMsgNum = 0L;
         Iterator var3 = var6.keySet().iterator();

         MessageInfo var5;
         while(var3.hasNext()) {
            Long var4 = (Long)var3.next();
            var5 = (MessageInfo)var6.get(var4);
            if (var5.isAck() && var5.getMessageNum() > this._lastAckdMsgNum) {
               this._lastAckdMsgNum = var5.getMessageNum();
            }
         }

         this._unackdCount = var1.getUnackdCount();
         this._requestInfoList = new HashMap();
         Map var7 = var1.getRequests();
         Iterator var8 = var7.values().iterator();

         while(var8.hasNext()) {
            var5 = (MessageInfo)var8.next();
            this._requestInfoList.put(var5.getMessageId(), new WsrmRequestInfoImpl(var1, var5));
         }

      }

      public WsrmSequenceInfoImpl(WsrmSequenceInfoImpl var1) {
         this.initTransients();
         this._id = var1._id;
         this._logicalStoreName = var1._logicalStoreName;
         this._physicalStoreName = var1._physicalStoreName;
         this._isSource = var1._isSource;
         this._destinationId = var1._destinationId;
         this._isOffer = var1._isOffer;
         this._mainSequenceId = var1._mainSequenceId;
         this._state = var1._state;
         this._creationTime = var1._creationTime;
         this._lastActivityTime = var1._lastActivityTime;
         this._maxAge = var1._maxAge;
         this._lastAckdMsgNum = var1._lastAckdMsgNum;
         this._unackdCount = var1._unackdCount;
         this._requestInfoList = new HashMap(var1._requestInfoList.size());
         Iterator var2 = var1._requestInfoList.keySet().iterator();

         while(var2.hasNext()) {
            String var3 = (String)var2.next();
            WsrmRequestInfoImpl var4 = (WsrmRequestInfoImpl)var1._requestInfoList.get(var3);
            WsrmRequestInfoImpl var5 = new WsrmRequestInfoImpl(var4);
            this._requestInfoList.put(var3, var5);
         }

      }

      private void initTransients() {
      }

      public CompositeData createCompositeData() throws OpenDataException {
         return _factory.createCompositeData(this);
      }

      public String getId() {
         return this._id;
      }

      public String getLogicalStoreName() {
         return this._logicalStoreName;
      }

      public String getPhysicalStoreName() {
         return this._physicalStoreName;
      }

      public boolean isSource() {
         return this._isSource;
      }

      public String getDestinationId() {
         return this._destinationId;
      }

      public boolean isOffer() {
         return this._isOffer;
      }

      public String getMainSequenceId() {
         return this._mainSequenceId;
      }

      public String getState() {
         return this._state;
      }

      public long getCreationTime() {
         return this._creationTime;
      }

      public long getLastActivityTime() {
         return this._lastActivityTime;
      }

      public long getMaxAge() {
         return this._maxAge;
      }

      public long getLastAckdMessageNum() {
         return this._lastAckdMsgNum;
      }

      public long getUnackdCount() {
         return this._unackdCount;
      }

      public WsrmRequestInfo[] getRequests() throws ManagementException {
         return (WsrmRequestInfo[])this._requestInfoList.values().toArray(new WsrmRequestInfo[this._requestInfoList.size()]);
      }

      private void writeObject(ObjectOutputStream var1) throws IOException {
         var1.defaultWriteObject();
      }

      private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
         this.initTransients();
         var1.defaultReadObject();
      }

      static {
         try {
            _factory = new WseeCompositeDataFactory(WsrmSequenceInfo.class);
         } catch (Exception var1) {
            throw new RuntimeException(var1.toString(), var1);
         }
      }
   }
}
