# Microsoft Developer Studio Generated NMAKE File, Format Version 4.20
# ** DO NOT EDIT **

# TARGTYPE "Win32 (x86) Application" 0x0101

!IF "$(CFG)" == ""
CFG=MYCALL - Win32 Debug
!MESSAGE No configuration specified.  Defaulting to MYCALL - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "MYCALL - Win32 Release" && "$(CFG)" != "MYCALL - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE on this makefile
!MESSAGE by defining the macro CFG on the command line.  For example:
!MESSAGE 
!MESSAGE NMAKE /f "Mycall.mak" CFG="MYCALL - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "MYCALL - Win32 Release" (based on "Win32 (x86) Application")
!MESSAGE "MYCALL - Win32 Debug" (based on "Win32 (x86) Application")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 
################################################################################
# Begin Project
# PROP Target_Last_Scanned "MYCALL - Win32 Debug"
RSC=rc.exe
MTL=mktyplib.exe
CPP=cl.exe

!IF  "$(CFG)" == "MYCALL - Win32 Release"

# PROP BASE Use_MFC 6
# PROP BASE Use_Debug_Libraries 0
# PROP BASE Output_Dir "Release"
# PROP BASE Intermediate_Dir "Release"
# PROP BASE Target_Dir ""
# PROP Use_MFC 5
# PROP Use_Debug_Libraries 0
# PROP Output_Dir "Release"
# PROP Intermediate_Dir "Release"
# PROP Target_Dir ""
OUTDIR=.\Release
INTDIR=.\Release

ALL : "$(OUTDIR)\Mycall.exe"

CLEAN : 
	-@erase "$(INTDIR)\Areacmbo.obj"
	-@erase "$(INTDIR)\Cursorbx.obj"
	-@erase "$(INTDIR)\logonser.obj"
	-@erase "$(INTDIR)\MainFrm.obj"
	-@erase "$(INTDIR)\MainView.obj"
	-@erase "$(INTDIR)\MYCALL.obj"
	-@erase "$(INTDIR)\Mycall.pch"
	-@erase "$(INTDIR)\MYCALL.res"
	-@erase "$(INTDIR)\Mycalldc.obj"
	-@erase "$(INTDIR)\peplus.obj"
	-@erase "$(INTDIR)\Selectev.obj"
	-@erase "$(INTDIR)\StdAfx.obj"
	-@erase "$(INTDIR)\tablelog.obj"
	-@erase "$(INTDIR)\Windowop.obj"
	-@erase "$(OUTDIR)\Mycall.exe"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

# ADD BASE CPP /nologo /MD /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_AFXDLL" /D "_MBCS" /Yu"stdafx.h" /c
# ADD CPP /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS" /Yu"stdafx.h" /c
CPP_PROJ=/nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D\
 "_MBCS" /Fp"$(INTDIR)/Mycall.pch" /Yu"stdafx.h" /Fo"$(INTDIR)/" /c 
CPP_OBJS=.\Release/
CPP_SBRS=.\.
# ADD BASE MTL /nologo /D "NDEBUG" /win32
# ADD MTL /nologo /D "NDEBUG" /win32
MTL_PROJ=/nologo /D "NDEBUG" /win32 
# ADD BASE RSC /l 0x1009 /d "NDEBUG" /d "_AFXDLL"
# ADD RSC /l 0x1009 /d "NDEBUG"
RSC_PROJ=/l 0x1009 /fo"$(INTDIR)/MYCALL.res" /d "NDEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/Mycall.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /machine:I386
# ADD LINK32 /nologo /subsystem:windows /machine:I386
LINK32_FLAGS=/nologo /subsystem:windows /incremental:no\
 /pdb:"$(OUTDIR)/Mycall.pdb" /machine:I386 /out:"$(OUTDIR)/Mycall.exe" 
LINK32_OBJS= \
	"$(INTDIR)\Areacmbo.obj" \
	"$(INTDIR)\Cursorbx.obj" \
	"$(INTDIR)\logonser.obj" \
	"$(INTDIR)\MainFrm.obj" \
	"$(INTDIR)\MainView.obj" \
	"$(INTDIR)\MYCALL.obj" \
	"$(INTDIR)\MYCALL.res" \
	"$(INTDIR)\Mycalldc.obj" \
	"$(INTDIR)\peplus.obj" \
	"$(INTDIR)\Selectev.obj" \
	"$(INTDIR)\StdAfx.obj" \
	"$(INTDIR)\tablelog.obj" \
	"$(INTDIR)\Windowop.obj" \
	"..\..\crpe32m.lib"

"$(OUTDIR)\Mycall.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"

# PROP BASE Use_MFC 6
# PROP BASE Use_Debug_Libraries 1
# PROP BASE Output_Dir "Debug"
# PROP BASE Intermediate_Dir "Debug"
# PROP BASE Target_Dir ""
# PROP Use_MFC 5
# PROP Use_Debug_Libraries 1
# PROP Output_Dir "Debug"
# PROP Intermediate_Dir "Debug"
# PROP Target_Dir ""
OUTDIR=.\Debug
INTDIR=.\Debug

ALL : "$(OUTDIR)\Mycall.exe" "$(OUTDIR)\Mycall.bsc"

CLEAN : 
	-@erase "$(INTDIR)\Areacmbo.obj"
	-@erase "$(INTDIR)\Areacmbo.sbr"
	-@erase "$(INTDIR)\Cursorbx.obj"
	-@erase "$(INTDIR)\Cursorbx.sbr"
	-@erase "$(INTDIR)\logonser.obj"
	-@erase "$(INTDIR)\logonser.sbr"
	-@erase "$(INTDIR)\MainFrm.obj"
	-@erase "$(INTDIR)\MainFrm.sbr"
	-@erase "$(INTDIR)\MainView.obj"
	-@erase "$(INTDIR)\MainView.sbr"
	-@erase "$(INTDIR)\MYCALL.obj"
	-@erase "$(INTDIR)\Mycall.pch"
	-@erase "$(INTDIR)\MYCALL.res"
	-@erase "$(INTDIR)\MYCALL.sbr"
	-@erase "$(INTDIR)\Mycalldc.obj"
	-@erase "$(INTDIR)\Mycalldc.sbr"
	-@erase "$(INTDIR)\peplus.obj"
	-@erase "$(INTDIR)\peplus.sbr"
	-@erase "$(INTDIR)\Selectev.obj"
	-@erase "$(INTDIR)\Selectev.sbr"
	-@erase "$(INTDIR)\StdAfx.obj"
	-@erase "$(INTDIR)\StdAfx.sbr"
	-@erase "$(INTDIR)\tablelog.obj"
	-@erase "$(INTDIR)\tablelog.sbr"
	-@erase "$(INTDIR)\vc40.idb"
	-@erase "$(INTDIR)\vc40.pdb"
	-@erase "$(INTDIR)\Windowop.obj"
	-@erase "$(INTDIR)\Windowop.sbr"
	-@erase "$(OUTDIR)\Mycall.bsc"
	-@erase "$(OUTDIR)\Mycall.exe"
	-@erase "$(OUTDIR)\Mycall.ilk"
	-@erase "$(OUTDIR)\Mycall.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

# ADD BASE CPP /nologo /MDd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_AFXDLL" /D "_MBCS" /Yu"stdafx.h" /c
# ADD CPP /nologo /MTd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_MBCS" /FR /Yu"stdafx.h" /c
CPP_PROJ=/nologo /MTd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS"\
 /D "_MBCS" /FR"$(INTDIR)/" /Fp"$(INTDIR)/Mycall.pch" /Yu"stdafx.h"\
 /Fo"$(INTDIR)/" /Fd"$(INTDIR)/" /c 
CPP_OBJS=.\Debug/
CPP_SBRS=.\Debug/
# ADD BASE MTL /nologo /D "_DEBUG" /win32
# ADD MTL /nologo /D "_DEBUG" /win32
MTL_PROJ=/nologo /D "_DEBUG" /win32 
# ADD BASE RSC /l 0x1009 /d "_DEBUG" /d "_AFXDLL"
# ADD RSC /l 0x1009 /d "_DEBUG"
RSC_PROJ=/l 0x1009 /fo"$(INTDIR)/MYCALL.res" /d "_DEBUG" 
BSC32=bscmake.exe
# ADD BASE BSC32 /nologo
# ADD BSC32 /nologo
BSC32_FLAGS=/nologo /o"$(OUTDIR)/Mycall.bsc" 
BSC32_SBRS= \
	"$(INTDIR)\Areacmbo.sbr" \
	"$(INTDIR)\Cursorbx.sbr" \
	"$(INTDIR)\logonser.sbr" \
	"$(INTDIR)\MainFrm.sbr" \
	"$(INTDIR)\MainView.sbr" \
	"$(INTDIR)\MYCALL.sbr" \
	"$(INTDIR)\Mycalldc.sbr" \
	"$(INTDIR)\peplus.sbr" \
	"$(INTDIR)\Selectev.sbr" \
	"$(INTDIR)\StdAfx.sbr" \
	"$(INTDIR)\tablelog.sbr" \
	"$(INTDIR)\Windowop.sbr"

"$(OUTDIR)\Mycall.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
# ADD BASE LINK32 /nologo /subsystem:windows /debug /machine:I386
# ADD LINK32 /nologo /subsystem:windows /debug /machine:I386
LINK32_FLAGS=/nologo /subsystem:windows /incremental:yes\
 /pdb:"$(OUTDIR)/Mycall.pdb" /debug /machine:I386 /out:"$(OUTDIR)/Mycall.exe" 
LINK32_OBJS= \
	"$(INTDIR)\Areacmbo.obj" \
	"$(INTDIR)\Cursorbx.obj" \
	"$(INTDIR)\logonser.obj" \
	"$(INTDIR)\MainFrm.obj" \
	"$(INTDIR)\MainView.obj" \
	"$(INTDIR)\MYCALL.obj" \
	"$(INTDIR)\MYCALL.res" \
	"$(INTDIR)\Mycalldc.obj" \
	"$(INTDIR)\peplus.obj" \
	"$(INTDIR)\Selectev.obj" \
	"$(INTDIR)\StdAfx.obj" \
	"$(INTDIR)\tablelog.obj" \
	"$(INTDIR)\Windowop.obj" \
	"..\..\crpe32m.lib"

"$(OUTDIR)\Mycall.exe" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 

.c{$(CPP_OBJS)}.obj:
   $(CPP) $(CPP_PROJ) $<  

.cpp{$(CPP_OBJS)}.obj:
   $(CPP) $(CPP_PROJ) $<  

.cxx{$(CPP_OBJS)}.obj:
   $(CPP) $(CPP_PROJ) $<  

.c{$(CPP_SBRS)}.sbr:
   $(CPP) $(CPP_PROJ) $<  

.cpp{$(CPP_SBRS)}.sbr:
   $(CPP) $(CPP_PROJ) $<  

.cxx{$(CPP_SBRS)}.sbr:
   $(CPP) $(CPP_PROJ) $<  

################################################################################
# Begin Target

# Name "MYCALL - Win32 Release"
# Name "MYCALL - Win32 Debug"

!IF  "$(CFG)" == "MYCALL - Win32 Release"

!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"

!ENDIF 

################################################################################
# Begin Source File

SOURCE=.\ReadMe.txt

!IF  "$(CFG)" == "MYCALL - Win32 Release"

!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\MYCALL.cpp
DEP_CPP_MYCAL=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Areacmbo.h"\
	".\Cursorbx.h"\
	".\Mainfrm.h"\
	".\Mainview.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\MYCALL.obj" : $(SOURCE) $(DEP_CPP_MYCAL) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\MYCALL.obj" : $(SOURCE) $(DEP_CPP_MYCAL) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\MYCALL.sbr" : $(SOURCE) $(DEP_CPP_MYCAL) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\StdAfx.cpp
DEP_CPP_STDAF=\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"

# ADD CPP /Yc"stdafx.h"

BuildCmds= \
	$(CPP) /nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_MBCS"\
 /Fp"$(INTDIR)/Mycall.pch" /Yc"stdafx.h" /Fo"$(INTDIR)/" /c $(SOURCE) \
	

"$(INTDIR)\StdAfx.obj" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\Mycall.pch" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(BuildCmds)

!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"

# ADD CPP /Yc"stdafx.h"

BuildCmds= \
	$(CPP) /nologo /MTd /W3 /Gm /GX /Zi /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS"\
 /D "_MBCS" /FR"$(INTDIR)/" /Fp"$(INTDIR)/Mycall.pch" /Yc"stdafx.h"\
 /Fo"$(INTDIR)/" /Fd"$(INTDIR)/" /c $(SOURCE) \
	

"$(INTDIR)\StdAfx.obj" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\StdAfx.sbr" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(BuildCmds)

"$(INTDIR)\Mycall.pch" : $(SOURCE) $(DEP_CPP_STDAF) "$(INTDIR)"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\MainFrm.cpp
DEP_CPP_MAINF=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Mainfrm.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\SELECTEV.H"\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\MainFrm.obj" : $(SOURCE) $(DEP_CPP_MAINF) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\MainFrm.obj" : $(SOURCE) $(DEP_CPP_MAINF) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\MainFrm.sbr" : $(SOURCE) $(DEP_CPP_MAINF) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\MYCALL.rc

!IF  "$(CFG)" == "MYCALL - Win32 Release"

DEP_RSC_MYCALL=\
	".\Res\Crystal.ico"\
	".\Res\Mycall.rc2"\
	".\res\MYCALLDoc.ico"\
	".\Res\Toolbar.bmp"\
	

"$(INTDIR)\MYCALL.res" : $(SOURCE) $(DEP_RSC_MYCALL) "$(INTDIR)"
   $(RSC) $(RSC_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"

DEP_RSC_MYCALL=\
	".\Res\Crystal.ico"\
	".\Res\Mycall.rc2"\
	".\Res\Toolbar.bmp"\
	
NODEP_RSC_MYCALL=\
	".\Debug\res\MYCALLDoc.ico"\
	

"$(INTDIR)\MYCALL.res" : $(SOURCE) $(DEP_RSC_MYCALL) "$(INTDIR)"
   $(RSC) $(RSC_PROJ) $(SOURCE)


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\MainView.cpp
DEP_CPP_MAINV=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Areacmbo.h"\
	".\Cursorbx.h"\
	".\Mainfrm.h"\
	".\Mainview.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\MainView.obj" : $(SOURCE) $(DEP_CPP_MAINV) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\MainView.obj" : $(SOURCE) $(DEP_CPP_MAINV) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\MainView.sbr" : $(SOURCE) $(DEP_CPP_MAINV) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\tablelog.cpp
DEP_CPP_TABLE=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\Stdafx.h"\
	".\Tablelog.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\tablelog.obj" : $(SOURCE) $(DEP_CPP_TABLE) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\tablelog.obj" : $(SOURCE) $(DEP_CPP_TABLE) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\tablelog.sbr" : $(SOURCE) $(DEP_CPP_TABLE) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\logonser.cpp
DEP_CPP_LOGON=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Logonser.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\logonser.obj" : $(SOURCE) $(DEP_CPP_LOGON) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\logonser.obj" : $(SOURCE) $(DEP_CPP_LOGON) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\logonser.sbr" : $(SOURCE) $(DEP_CPP_LOGON) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\Mycalldc.cpp
DEP_CPP_MYCALLD=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Areacmbo.h"\
	".\Cursorbx.h"\
	".\Logonser.h"\
	".\Mainview.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\SELECTEV.H"\
	".\Stdafx.h"\
	".\Tablelog.h"\
	".\Windowop.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\Mycalldc.obj" : $(SOURCE) $(DEP_CPP_MYCALLD) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\Mycalldc.obj" : $(SOURCE) $(DEP_CPP_MYCALLD) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\Mycalldc.sbr" : $(SOURCE) $(DEP_CPP_MYCALLD) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\Selectev.cpp
DEP_CPP_SELEC=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\Routing.h"\
	".\SELECTEV.H"\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\Selectev.obj" : $(SOURCE) $(DEP_CPP_SELEC) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\Selectev.obj" : $(SOURCE) $(DEP_CPP_SELEC) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\Selectev.sbr" : $(SOURCE) $(DEP_CPP_SELEC) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\Windowop.cpp
DEP_CPP_WINDO=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Mycall.h"\
	".\Mycalldc.h"\
	".\Stdafx.h"\
	".\Windowop.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\Windowop.obj" : $(SOURCE) $(DEP_CPP_WINDO) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\Windowop.obj" : $(SOURCE) $(DEP_CPP_WINDO) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\Windowop.sbr" : $(SOURCE) $(DEP_CPP_WINDO) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\Cursorbx.cpp
DEP_CPP_CURSO=\
	"..\..\crpe.h"\
	".\Cursorbx.h"\
	".\Mycall.h"\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\Cursorbx.obj" : $(SOURCE) $(DEP_CPP_CURSO) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\Cursorbx.obj" : $(SOURCE) $(DEP_CPP_CURSO) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\Cursorbx.sbr" : $(SOURCE) $(DEP_CPP_CURSO) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE=.\Areacmbo.cpp
DEP_CPP_AREAC=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	".\Areacmbo.h"\
	".\Cursorbx.h"\
	".\Mycall.h"\
	".\Stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\Areacmbo.obj" : $(SOURCE) $(DEP_CPP_AREAC) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


"$(INTDIR)\Areacmbo.obj" : $(SOURCE) $(DEP_CPP_AREAC) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"

"$(INTDIR)\Areacmbo.sbr" : $(SOURCE) $(DEP_CPP_AREAC) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"


!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE="..\..\peplus.cpp"
DEP_CPP_PEPLU=\
	"..\..\crpe.h"\
	"..\..\peplus.h"\
	
NODEP_CPP_PEPLU=\
	"..\..\stdafx.h"\
	

!IF  "$(CFG)" == "MYCALL - Win32 Release"


"$(INTDIR)\peplus.obj" : $(SOURCE) $(DEP_CPP_PEPLU) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"
   $(CPP) $(CPP_PROJ) $(SOURCE)


!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"


BuildCmds= \
	$(CPP) $(CPP_PROJ) $(SOURCE) \
	

"$(INTDIR)\peplus.obj" : $(SOURCE) $(DEP_CPP_PEPLU) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"
   $(BuildCmds)

"$(INTDIR)\peplus.sbr" : $(SOURCE) $(DEP_CPP_PEPLU) "$(INTDIR)"\
 "$(INTDIR)\Mycall.pch"
   $(BuildCmds)

!ENDIF 

# End Source File
################################################################################
# Begin Source File

SOURCE="..\..\crpe32m.lib"

!IF  "$(CFG)" == "MYCALL - Win32 Release"

!ELSEIF  "$(CFG)" == "MYCALL - Win32 Debug"

!ENDIF 

# End Source File
# End Target
# End Project
################################################################################
