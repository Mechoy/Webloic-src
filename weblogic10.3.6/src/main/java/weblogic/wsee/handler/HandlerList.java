package weblogic.wsee.handler;

import java.util.List;
import javax.xml.rpc.handler.HandlerInfo;

public interface HandlerList {
   int size();

   HandlerInfo getInfo(int var1);

   String getName(int var1);

   void add(String var1, HandlerInfo var2) throws HandlerException;

   void insert(String var1, int var2, HandlerInfo var3) throws HandlerException;

   int lenientInsert(String var1, HandlerInfo var2, List<String> var3, List<String> var4) throws HandlerException;

   int insert(String var1, HandlerInfo var2, List<String> var3, List<String> var4) throws HandlerException;

   void remove(int var1);

   boolean remove(String var1);

   boolean contains(String var1);

   String[] getHandlerNames();
}
