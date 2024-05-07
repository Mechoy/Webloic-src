package weblogic.wsee.mc.mbean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.management.openmbean.CompositeData;
import javax.management.openmbean.CompositeDataSupport;
import javax.management.openmbean.CompositeType;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.SimpleType;
import weblogic.management.ManagementException;
import weblogic.wsee.mc.processor.McPending;
import weblogic.wsee.mc.processor.McPendingManager;
import weblogic.wsee.mc.utils.McProtocolUtils;
import weblogic.wsee.monitoring.WseeBaseRuntimeData;

public final class WseeMcRuntimeData extends WseeBaseRuntimeData {
   private static final String OPEN_TYPE_NAME = "MakeConnectionAnonymousEndpoint";
   private static final String OPEN_DESCRIPTION = "This object represents a MakeConnection Anonymous Id";
   private static final String ITEM_ID = "AnonymousEndpointId";
   private static final String ITEM_PENDING_MESSAGES = "PendingMessageCount";
   private static final String ITEM_RECEIVED_MC_MESSAGES = "ReceivedMcMessageCount";
   private static final String ITEM_EMPTY_RESPONSES = "EmptyResponseCount";
   private static final String ITEM_NON_EMPTY_RESPONSES = "NonEmptyResponseCount";
   private static final String ITEM_OLDEST_PENDING_MESSAGE = "OldestPendingMessageTime";
   private static final String ITEM_NEWEST_PENDING_MESSAGE = "NewestPendingMessageTime";
   private static String[] itemNames = new String[]{"AnonymousEndpointId", "PendingMessageCount", "ReceivedMcMessageCount", "EmptyResponseCount", "NonEmptyResponseCount", "OldestPendingMessageTime", "NewestPendingMessageTime"};
   private static String[] itemDescriptions = new String[]{"Anonymous Endpoint Id", "Pending messages", "Received MakeConnection messages", "Empty responses to MakeConnection messages", "Non-empty responses to MakeConnection messages", "Oldest pending message", "Newest pending message"};
   private static OpenType[] itemTypes;
   private HashSet<String> _mcAnonIds = new HashSet();

   WseeMcRuntimeData(String var1) throws ManagementException {
      super(var1, (WseeBaseRuntimeData)null);
   }

   public void addMcAnonId(String var1) {
      this._mcAnonIds.add(var1);
   }

   public void removeMcAnonId(String var1) {
      this._mcAnonIds.remove(var1);
   }

   public List<String> getMcAnonymousIds() {
      ArrayList var1 = new ArrayList();
      McPendingManager var2 = McPendingManager.getInstance();
      Iterator var3 = this._mcAnonIds.iterator();

      while(var3.hasNext()) {
         String var4 = (String)var3.next();
         McPending var5 = var2.getPending(var4);
         if (var5 == null) {
            var3.remove();
         } else {
            var1.add(McProtocolUtils.decodeId(var4));
         }
      }

      return var1;
   }

   public CompositeData getIdInfo(String var1) throws ManagementException {
      try {
         McPendingManager var2 = McPendingManager.getInstance();
         String var3 = McProtocolUtils.encodeId(var1);
         McPending var4 = var2.getPending(var3);
         if (var4 == null) {
            return null;
         } else {
            int var5 = var4.size();
            int var6 = var4.getRemovalAttempts();
            int var7 = var4.getSuccessfulRemovalAttempts();
            int var8 = var6 - var7;
            long var9 = var4.getOldestTimestamp();
            long var11 = var4.getNewestTimestamp();
            if (var9 == -1L) {
               var9 = 0L;
            }

            if (var11 == -1L) {
               var11 = 0L;
            }

            CompositeType var13 = new CompositeType("MakeConnectionAnonymousEndpoint", "This object represents a MakeConnection Anonymous Id", itemNames, itemDescriptions, itemTypes);
            HashMap var14 = new HashMap();
            var14.put("AnonymousEndpointId", var1);
            var14.put("PendingMessageCount", var5);
            var14.put("ReceivedMcMessageCount", var6);
            var14.put("EmptyResponseCount", var8);
            var14.put("NonEmptyResponseCount", var7);
            var14.put("OldestPendingMessageTime", var9);
            var14.put("NewestPendingMessageTime", var11);
            return new CompositeDataSupport(var13, var14);
         }
      } catch (Exception var15) {
         throw new ManagementException(var15.toString(), var15);
      }
   }

   static {
      itemTypes = new OpenType[]{SimpleType.STRING, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.INTEGER, SimpleType.LONG, SimpleType.LONG};
   }
}
