"""
Internal script that makes all these Online WLST commands NO-OP
once a user disconnects from a running server.

WARNING: This file is part of the WLST implementation and as such may
change between versions of WLST. You should not try to reuse the logic
in this script or keep copies of this script. Doing so could cause
your WLST scripts to fail when you upgrade to a different version of
WLST.

"""

def unsupported():
  print ''
  print 'You will need to be connected to a running server to execute this command'
  print ''
  return

def invoke(methodName, parameters, signatures):
  unsupported()
  
def runtime():
  unsupported()
  
def config():
  unsupported()
  
def custom():
  unsupported()

def domainCustom():
  unsupported()
  
def listCustomMBeans():
  unsupported()
  
def adminConfig():
  unsupported()
  
def reset():
  unsupported()
  
def shutdown(name="NONAME", entityType='Server', ignoreSessions='true',timeOut=0, force='false'):
  unsupported()
  
def getTarget(path):
  unsupported()
  
def getTargetArray(type, values):
  unsupported()
  
def suspend(serverName):
  unsupported()
  
def resume(serverName):
  unsupported()
  
def migrate(serverName, machineName, sourceDown='false', destinationDown='false'):
  unsupported()
  
def migrateServer(serverName, machineName, sourceDown='false', destinationDown='false'):
  unsupported()
  
def migrateAll(serverName,
                  destinationName,
                  sourceDown='false',
                  destinationDown='false'):
  unsupported()
  
def deploy(appName,path,targets="",stageMode=None,planPath=None,**options):
  unsupported()
  
def redeploy(appName, planPath=None, **options):
  unsupported()
  
def undeploy(appName,targets=None, block="true", subModuleTargets=None):
  unsupported()
  
def state(name, type="Server"):
  unsupported()
  
def showMBI(type=""):
  unsupported()
  
def saveDomain():
  unsupported()
  
def disconnect(force="true"):
  unsupported()
  
def monitorAttribute(attrName, interval, runInBackGround = 'false', path=None, name=None, printTime=None, printCount=None):
  unsupported()
  
def stopAllMonitors():
  unsupported()
  
def serverConfig():
  unsupported()
  
def serverRuntime():
  unsupported()
  
def domainConfig():
  unsupported()
  
def domainRuntime():
  unsupported()
  
def edit():
  unsupported()
  
def stopMonitor(monName):
  unsupported()
  
def showMonitors():
  unsupported()
  
def jndi():
  unsupported()
  
def man(attrName=None):
  unsupported()
  
def manual(attrName=None):
  unsupported()
  
def listApplications():
  unsupported()

def listChildTypes(parent=None):
  unsupported()
  
def startEdit(waitTimeInMillis=0, timeOutInMillis=-1):
  unsupported()
  
def save():
  unsupported()
  
def activate(waitTimeInMillis=30000, timeout=60000, block='true'):
  unsupported()
  
def undo(unactivatedChanges='false', defaultAnswer='from_prompt'):
  unsupported()
  
def stopEdit(defaultAnswer='from_prompt'):
  unsupported()
  
def cancelEdit(defaultAnswer='from_prompt'):
  unsupported()
  
def getActivationTask():
  unsupported()
  
def nmEnroll(domainDir=None, nmHome=None):
  unsupported()
  
def distributeApplication(appPath, planPath=None, targets=None, **options ):
  unsupported()
  
def startApplication(appName, **options):
  unsupported()
  
def stopApplication(appName, block="true"):
  unsupported()
  
def updateApplication(appName, planPath=None, **options):
  unsupported()
  
def getWLDM():
  unsupported()

def showChanges(onlyInMemory="false"):
  unsupported()
  
def validate():
  unsupported()
  
def getMBean(mbeanPath=None):
  unsupported()
  
def getMBI(mbeanType=None):
  unsupported()
  
def threadDump(writeToFile="true", fileName=None, serverName = None):
  unsupported()  

def currentTree():
  unsupported()

def getConfigManager():
  unsupported()

def addListener(mbean, attributeNames=None, logFile=None, listenerName=None):
  unsupported()
  
def removeListener(mbean=None, listenerName=None):
  unsupported()
  
def showListeners():
  unsupported()
  
def viewMBean():
  unsupported()

def getPath(mbean):
  unsupported()
  
def nmGenBootStartupProps(serverName=None):
  unsupported()

def getAvailableCapturedImages() :
  unsupported()

def saveDiagnosticImageCaptureFile(**dict) :
  unsupported()

def saveDiagnosticImageCaptureEntryFile(**dict) :
  unsupported()
