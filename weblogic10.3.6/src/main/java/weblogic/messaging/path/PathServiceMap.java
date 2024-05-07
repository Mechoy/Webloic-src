package weblogic.messaging.path;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import javax.naming.NamingException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import weblogic.cache.lld.BaseCacheEntry;
import weblogic.cache.lld.ChangeListener;
import weblogic.common.CompletionListener;
import weblogic.common.CompletionRequest;
import weblogic.messaging.path.helper.KeyString;
import weblogic.messaging.path.helper.PathHelper;
import weblogic.messaging.path.internal.PathObjectHandler;
import weblogic.messaging.runtime.DiagnosticImageTimeoutException;
import weblogic.store.PersistentMapAsyncTX;
import weblogic.store.PersistentStoreException;
import weblogic.store.PersistentStoreTransaction;
import weblogic.store.xa.PersistentStoreXA;

class PathServiceMap extends AsyncMapImpl implements AsyncMap {
   static final String MAP_CONNECTION_PREFIX = "weblogic.messaging.PathService.";
   HashMap createdMaps = new HashMap();
   private final PersistentStoreXA store;
   private ChangeListener invalidator;
   private final ExceptionAdapter exceptionAdapter;

   PathServiceMap(String var1, PersistentStoreXA var2, ExceptionAdapter var3) {
      super(var1, (AsyncMap)null, var3);
      this.store = var2;

      assert var2 != null;

      this.exceptionAdapter = var3;
      this.setInvalidator();
      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("PathService store: " + this.store + ", jndiName " + var1);
      }

   }

   private void setInvalidator() {
      PathHelper.manager().register(true, this.jndiName, this);
      this.invalidator = PathHelper.manager().getDirtyCacheUpdaterMap(this.jndiName);
   }

   PersistentMapAsyncTX mapByKey(Key var1) throws PersistentStoreException {
      String var2 = mapNameFromKey(var1);
      synchronized(this.createdMaps) {
         PersistentMapAsyncTX var3 = (PersistentMapAsyncTX)this.createdMaps.get(var2);
         if (var3 != null) {
            return var3;
         } else {
            var3 = this.store.createPersistentMap(var2, PathObjectHandler.getObjectHandler(var1.getSubsystem()));
            Object var5 = this.createdMaps.put(var2, var3);

            assert var5 == null;

            return var3;
         }
      }
   }

   private static String mapNameFromKey(Key var0) {
      return "weblogic.messaging.PathService." + var0.getAssemblyId() + Key.RESERVED_SUBSYSTEMS[var0.getSubsystem()];
   }

   static KeyString sampleKeyFromMapName(String var0) {
      if (var0.startsWith("weblogic.messaging.PathService.")) {
         for(int var1 = 0; var1 < Key.RESERVED_SUBSYSTEMS.length; ++var1) {
            if (var0.endsWith(Key.RESERVED_SUBSYSTEMS[var1])) {
               String var2 = var0.substring("weblogic.messaging.PathService.".length(), var0.length() - Key.RESERVED_SUBSYSTEMS[var1].length());
               return new KeyString((byte)var1, var2, var2);
            }
         }
      }

      return null;
   }

   public void putIfAbsent(Object var1, final Object var2, final CompletionRequest var3) {
      if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
         PathHelper.PathSvcVerbose.debug("PathService putIfAbsent key: " + var1 + ", value " + var2);
      }

      final Key var4 = (Key)var1;

      final PersistentMapAsyncTX var5;
      try {
         var5 = this.mapByKey(var4);
      } catch (PersistentStoreException var7) {
         var3.setResult(this.exceptionAdapter.wrapException(var7));
         return;
      }

      var5.putIfAbsent(var4.getKeyId(), var2, new CompReqListener() {
         public final void onException(CompletionRequest var1, Throwable var2x) {
            if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
               PathHelper.PathSvcVerbose.debug("PathService putIfAbsent key: " + var4 + ", value " + var2, var2x);
            }

            PathServiceMap.wrappedSetResult(var3, var2x, PathServiceMap.this.exceptionAdapter);
         }

         public final void onCompletion(CompletionRequest var1, final Object var2x) {
            if (var2x != null && !var2x.equals(var2)) {
               if (!(var2x instanceof LegalMember)) {
                  PathServiceMap.wrappedSetResult(var3, var2x, PathServiceMap.this.exceptionAdapter);
               } else {
                  ((LegalMember)var2x).isLegal(var4, (LegalMember)var2x, new CompReqListener() {
                     public final void onException(CompletionRequest var1, Throwable var2xx) {
                        PathServiceMap.wrappedSetResult(var3, var2xx, PathServiceMap.this.exceptionAdapter);
                     }

                     public final void onCompletion(CompletionRequest var1, Object var2xx) {
                        if (var2xx == Boolean.TRUE) {
                           PathServiceMap.wrappedSetResult(var3, var2x, PathServiceMap.this.exceptionAdapter);
                        } else {
                           var5.remove(var4.getKeyId(), var2x, new CompReqListener() {
                              public final void onException(CompletionRequest var1, Throwable var2xx) {
                                 PathServiceMap.wrappedSetResult(var3, var2xx, PathServiceMap.this.exceptionAdapter);
                              }

                              public final void onCompletion(CompletionRequest var1, Object var2xx) {
                                 boolean var4x;
                                 try {
                                    var5.putIfAbsent(var4.getKeyId(), var2, new CompReqListener() {
                                       public final void onException(CompletionRequest var1, Throwable var2xx) {
                                          try {
                                             PathServiceMap.this.broadcastRemove(var4);
                                          } finally {
                                             PathServiceMap.wrappedSetResult(var3, var2xx, PathServiceMap.this.exceptionAdapter);
                                          }

                                       }

                                       public final void onCompletion(CompletionRequest var1, Object var2xx) {
                                          try {
                                             if (var2xx != null && !var2x.equals(var2xx)) {
                                                PathServiceMap.this.broadcastRemove(var4);
                                             }
                                          } finally {
                                             if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
                                                PathHelper.PathSvcVerbose.debug("PathService putIfAbsent key: " + var4 + ", value " + var2 + ", result " + var2xx);
                                             }

                                             PathServiceMap.wrappedSetResult(var3, var2xx, PathServiceMap.this.exceptionAdapter);
                                          }

                                       }
                                    });
                                 } catch (RuntimeException var10) {
                                    synchronized(var3) {
                                       var4x = !var3.hasResult();
                                    }

                                    if (var4x) {
                                       var3.setResult(PathServiceMap.this.exceptionAdapter.wrapException(var10));
                                    }

                                    throw var10;
                                 } catch (Error var11) {
                                    synchronized(var3) {
                                       var4x = !var3.hasResult();
                                    }

                                    if (var4x) {
                                       var3.setResult(PathServiceMap.this.exceptionAdapter.wrapException(var11));
                                    }

                                    throw var11;
                                 }
                              }
                           });
                        }
                     }
                  });
               }
            } else {
               PathServiceMap.wrappedSetResult(var3, var2x, PathServiceMap.this.exceptionAdapter);
            }
         }
      });
   }

   public void put(Object var1, Object var2, final CompletionRequest var3) {
      boolean var5;
      try {
         final Key var4 = (Key)var1;
         final UpdatableMember var15 = (UpdatableMember)var2;

         final PersistentMapAsyncTX var6;
         try {
            var6 = this.mapByKey(var4);
         } catch (PersistentStoreException var12) {
            var3.setResult(this.exceptionAdapter.wrapException(var12));
            return;
         }

         final PersistentStoreTransaction var7 = var6.begin();
         var6.putIfAbsent(var4.getKeyId(), var15, var7, new CompReqListener() {
            public final void onException(CompletionRequest var1, Throwable var2) {
               try {
                  var7.rollback();
               } catch (PersistentStoreException var4x) {
                  PathServiceMap.wrappedSetResult(var3, var4x, PathServiceMap.this.exceptionAdapter);
                  return;
               }

               PathServiceMap.wrappedSetResult(var3, var2, PathServiceMap.this.exceptionAdapter);
            }

            public final void onCompletion(CompletionRequest var1, Object var2) {
               final UpdatableMember var3x;
               boolean var4x;
               try {
                  var3x = (UpdatableMember)var2;
                  var4x = var3x == null || var3x.equals(var15);
               } catch (RuntimeException var6x) {
                  PathServiceMap.rollbackSameThreadSetResult(var7, var3, var6x, PathServiceMap.this.exceptionAdapter);
                  throw var6x;
               } catch (Error var7x) {
                  PathServiceMap.rollbackSameThreadSetResult(var7, var3, var7x, PathServiceMap.this.exceptionAdapter);
                  throw var7x;
               }

               if (var4x) {
                  var7.commit(var3);
               } else {
                  var6.put(var4.getKeyId(), var15, var7, new CompReqListener() {
                     public final void onException(CompletionRequest var1, Throwable var2) {
                        try {
                           var7.rollback();
                        } catch (PersistentStoreException var4x) {
                           PathServiceMap.wrappedSetResult(var3, var4x, PathServiceMap.this.exceptionAdapter);
                           return;
                        }

                        PathServiceMap.wrappedSetResult(var3, var2, PathServiceMap.this.exceptionAdapter);
                     }

                     public final void onCompletion(CompletionRequest var1, Object var2) {
                        var3x.update(var4, var15, new CompReqListener() {
                           public final void onException(CompletionRequest var1, Throwable var2) {
                              try {
                                 var15.updateException(var2, var4, var3x, var7, var3);
                              } catch (RuntimeException var4x) {
                                 PathServiceMap.rollbackSameThreadSetResult(var7, var3, var4x, PathServiceMap.this.exceptionAdapter);
                                 throw var4x;
                              } catch (Error var5) {
                                 PathServiceMap.rollbackSameThreadSetResult(var7, var3, var5, PathServiceMap.this.exceptionAdapter);
                                 throw var5;
                              }
                           }

                           public final void onCompletion(CompletionRequest var1, Object var2) {
                              var7.commit(new CompReqListener() {
                                 public void onException(CompletionRequest var1, Throwable var2) {
                                    PathServiceMap.wrappedSetResult(var3, var2, PathServiceMap.this.exceptionAdapter);
                                 }

                                 public void onCompletion(CompletionRequest var1, Object var2) {
                                    if (var2 == null) {
                                       var2 = Boolean.TRUE;
                                    }

                                    try {
                                       PathServiceMap.this.broadcastRemove(var4);
                                    } finally {
                                       PathServiceMap.wrappedSetResult(var3, var2, PathServiceMap.this.exceptionAdapter);
                                    }

                                 }
                              });
                           }
                        });
                     }
                  });
               }
            }
         });
      } catch (RuntimeException var13) {
         synchronized(var3) {
            var5 = !var3.hasResult();
         }

         if (var5) {
            var3.setResult(this.exceptionAdapter.wrapException(var13));
         }

         throw var13;
      } catch (Error var14) {
         synchronized(var3) {
            var5 = !var3.hasResult();
         }

         if (var5) {
            var3.setResult(this.exceptionAdapter.wrapException(var14));
         }

         throw var14;
      }
   }

   private static void wrappedSetResult(CompletionRequest var0, Object var1, ExceptionAdapter var2) {
      synchronized(var0) {
         if (var0.hasResult()) {
            return;
         }
      }

      if (var1 instanceof Throwable) {
         var1 = var2.wrapException((Throwable)var1);
      }

      var0.setResult(var1);
   }

   private static void rollbackSameThreadSetResult(PersistentStoreTransaction var0, CompletionRequest var1, Object var2, ExceptionAdapter var3) {
      try {
         var0.rollback();
      } catch (PersistentStoreException var9) {
         var2 = var9;
      } finally {
         wrappedSetResult(var1, var2, var3);
      }

   }

   public void get(Object var1, CompletionRequest var2) {
      boolean var4;
      try {
         Key var3 = (Key)var1;
         this.mapByKey(var3).get(var3.getKeyId(), var2);
      } catch (PersistentStoreException var11) {
         synchronized(var2) {
            if (var2.hasResult()) {
               return;
            }
         }

         var2.setResult(this.exceptionAdapter.wrapException(var11));
      } catch (RuntimeException var12) {
         synchronized(var2) {
            var4 = !var2.hasResult();
         }

         if (var4) {
            var2.setResult(this.exceptionAdapter.wrapException(var12));
         }

         throw var12;
      } catch (Error var13) {
         synchronized(var2) {
            var4 = !var2.hasResult();
         }

         if (var4) {
            var2.setResult(this.exceptionAdapter.wrapException(var13));
         }

         throw var13;
      }

   }

   public void remove(Object var1, final Object var2, final CompletionRequest var3) {
      try {
         if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
            PathHelper.PathSvcVerbose.debug("PathService remove key: " + var1 + ", value " + var2);
         }

         final Key var4 = (Key)var1;
         this.mapByKey(var4).remove(var4.getKeyId(), var2, new CompReqListener() {
            public final void onException(CompletionRequest var1, Throwable var2x) {
               try {
                  PathServiceMap.this.broadcastRemove(var4);
               } finally {
                  PathServiceMap.wrappedSetResult(var3, var2x, PathServiceMap.this.exceptionAdapter);
               }

            }

            public final void onCompletion(CompletionRequest var1, Object var2x) {
               try {
                  if (var2x == Boolean.TRUE) {
                     PathServiceMap.this.broadcastRemove(var4);
                  }
               } finally {
                  if (PathHelper.PathSvcVerbose.isDebugEnabled()) {
                     PathHelper.PathSvcVerbose.debug("PathService remove key: " + var4 + ", value " + var2 + ", result " + var2x);
                  }

                  PathServiceMap.wrappedSetResult(var3, var2x, PathServiceMap.this.exceptionAdapter);
               }

            }
         });
      } catch (PersistentStoreException var8) {
         synchronized(var3) {
            if (var3.hasResult()) {
               return;
            }
         }

         var3.setResult(this.exceptionAdapter.wrapException(var8));
      }

   }

   private void broadcastRemove(Key var1) {
      try {
         PathHelper.manager().cachedRemove(this.jndiName, var1, (Member)null, 520);
      } catch (NamingException var8) {
         PathHelper.PathSvcVerbose.debug(var8.getMessage(), var8);
      } catch (PathHelper.PathServiceException var9) {
         PathHelper.PathSvcVerbose.debug(var9.getMessage(), var9);
      } finally {
         this.invalidator.onDelete(new BaseCacheEntry(var1, (Object)null));
      }

   }

   public PersistentStoreXA getStore() {
      return this.store;
   }

   public void dump(PathServiceDiagnosticImageSource var1, XMLStreamWriter var2) throws DiagnosticImageTimeoutException, XMLStreamException, PersistentStoreException {
      var1.checkTimeout();
      var2.writeStartElement("Map");
      var2.writeAttribute("jndiName", this.getJndiName());
      var2.writeAttribute("storeName", this.store.getName());
      var2.writeStartElement("Assemblies");
      Iterator var3 = this.store.getMapConnectionNames();
      ArrayList var4 = new ArrayList();

      KeyString var5;
      while(var3.hasNext()) {
         var5 = sampleKeyFromMapName((String)var3.next());
         if (var5 != null) {
            var4.add(var5);
         }
      }

      var2.writeAttribute("count", String.valueOf(var4.size()));
      var3 = var4.iterator();

      while(var3.hasNext()) {
         var1.checkTimeout();
         var5 = (KeyString)var3.next();
         var2.writeStartElement("Assembly");
         var2.writeAttribute("name", var5.getAssemblyId() + var5.getSubsystem());
         PersistentMapAsyncTX var6 = this.mapByKey(var5);
         Set var7 = var6.keySet();
         var2.writeStartElement("Keys");
         if (var7 != null && var7.size() != 0) {
            var2.writeAttribute("count", String.valueOf(var7.size()));
            Iterator var8 = var7.iterator();

            while(var8.hasNext()) {
               var2.writeStartElement("Key");
               Object var9 = var8.next();
               var2.writeCharacters(var9.toString());
               var2.writeEndElement();
            }
         } else {
            var2.writeAttribute("count", "0");
         }

         var2.writeEndElement();
         var2.writeEndElement();
      }

      var2.writeEndElement();
      var2.writeEndElement();
   }

   class SubsystemInfo {
      String name;
      int index;

      SubsystemInfo(String var2, int var3) {
         this.name = var2;
         this.index = var3;
      }
   }

   private abstract static class CompReqListener extends CompletionRequest implements CompletionListener {
      CompReqListener() {
         this.addListener(this);
      }
   }
}
