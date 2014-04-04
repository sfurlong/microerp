# Microsoft Developer Studio Generated NMAKE File, Based on daiRptViewer.dsp
!IF "$(CFG)" == ""
CFG=daiRptViewer - Win32 Debug
!MESSAGE No configuration specified. Defaulting to daiRptViewer - Win32 Debug.
!ENDIF 

!IF "$(CFG)" != "daiRptViewer - Win32 Release" && "$(CFG)" != "daiRptViewer - Win32 Debug"
!MESSAGE Invalid configuration "$(CFG)" specified.
!MESSAGE You can specify a configuration when running NMAKE
!MESSAGE by defining the macro CFG on the command line. For example:
!MESSAGE 
!MESSAGE NMAKE /f "daiRptViewer.mak" CFG="daiRptViewer - Win32 Debug"
!MESSAGE 
!MESSAGE Possible choices for configuration are:
!MESSAGE 
!MESSAGE "daiRptViewer - Win32 Release" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE "daiRptViewer - Win32 Debug" (based on "Win32 (x86) Dynamic-Link Library")
!MESSAGE 
!ERROR An invalid configuration is specified.
!ENDIF 

!IF "$(OS)" == "Windows_NT"
NULL=
!ELSE 
NULL=nul
!ENDIF 

!IF  "$(CFG)" == "daiRptViewer - Win32 Release"

OUTDIR=.\Release
INTDIR=.\Release
# Begin Custom Macros
OutDir=.\Release
# End Custom Macros

ALL : "$(OUTDIR)\daiRptViewer.dll"


CLEAN :
	-@erase "$(INTDIR)\daiRptViewer.obj"
	-@erase "$(INTDIR)\daiRptViewer.pch"
	-@erase "$(INTDIR)\daiRptViewer.res"
	-@erase "$(INTDIR)\peplus.obj"
	-@erase "$(INTDIR)\StdAfx.obj"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(OUTDIR)\daiRptViewer.dll"
	-@erase "$(OUTDIR)\daiRptViewer.exp"
	-@erase "$(OUTDIR)\daiRptViewer.lib"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_MBCS" /D "_USRDLL" /Fp"$(INTDIR)\daiRptViewer.pch" /Yu"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

MTL=midl.exe
MTL_PROJ=/nologo /D "NDEBUG" /mktyplib203 /win32 
RSC=rc.exe
RSC_PROJ=/l 0x409 /fo"$(INTDIR)\daiRptViewer.res" /d "NDEBUG" 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\daiRptViewer.bsc" 
BSC32_SBRS= \
	
LINK32=link.exe
LINK32_FLAGS=/nologo /subsystem:windows /dll /incremental:no /pdb:"$(OUTDIR)\daiRptViewer.pdb" /machine:I386 /def:".\daiRptViewer.def" /out:"$(OUTDIR)\daiRptViewer.dll" /implib:"$(OUTDIR)\daiRptViewer.lib" 
DEF_FILE= \
	".\daiRptViewer.def"
LINK32_OBJS= \
	"$(INTDIR)\daiRptViewer.obj" \
	"$(INTDIR)\peplus.obj" \
	"$(INTDIR)\StdAfx.obj" \
	"$(INTDIR)\daiRptViewer.res" \
	".\crpe32m.lib"

"$(OUTDIR)\daiRptViewer.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ELSEIF  "$(CFG)" == "daiRptViewer - Win32 Debug"

OUTDIR=.\Debug
INTDIR=.\Debug
# Begin Custom Macros
OutDir=.\Debug
# End Custom Macros

ALL : "$(OUTDIR)\daiRptViewer.dll" "$(OUTDIR)\daiRptViewer.bsc"


CLEAN :
	-@erase "$(INTDIR)\daiRptViewer.obj"
	-@erase "$(INTDIR)\daiRptViewer.pch"
	-@erase "$(INTDIR)\daiRptViewer.res"
	-@erase "$(INTDIR)\daiRptViewer.sbr"
	-@erase "$(INTDIR)\peplus.obj"
	-@erase "$(INTDIR)\peplus.sbr"
	-@erase "$(INTDIR)\StdAfx.obj"
	-@erase "$(INTDIR)\StdAfx.sbr"
	-@erase "$(INTDIR)\vc60.idb"
	-@erase "$(INTDIR)\vc60.pdb"
	-@erase "$(OUTDIR)\daiRptViewer.bsc"
	-@erase "$(OUTDIR)\daiRptViewer.dll"
	-@erase "$(OUTDIR)\daiRptViewer.exp"
	-@erase "$(OUTDIR)\daiRptViewer.ilk"
	-@erase "$(OUTDIR)\daiRptViewer.lib"
	-@erase "$(OUTDIR)\daiRptViewer.pdb"

"$(OUTDIR)" :
    if not exist "$(OUTDIR)/$(NULL)" mkdir "$(OUTDIR)"

CPP=cl.exe
CPP_PROJ=/nologo /MTd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_MBCS" /D "_USRDLL" /FR"$(INTDIR)\\" /Fp"$(INTDIR)\daiRptViewer.pch" /Yu"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

.c{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.obj::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.c{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cpp{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

.cxx{$(INTDIR)}.sbr::
   $(CPP) @<<
   $(CPP_PROJ) $< 
<<

MTL=midl.exe
MTL_PROJ=/nologo /D "_DEBUG" /mktyplib203 /win32 
RSC=rc.exe
RSC_PROJ=/l 0x409 /fo"$(INTDIR)\daiRptViewer.res" /d "_DEBUG" 
BSC32=bscmake.exe
BSC32_FLAGS=/nologo /o"$(OUTDIR)\daiRptViewer.bsc" 
BSC32_SBRS= \
	"$(INTDIR)\daiRptViewer.sbr" \
	"$(INTDIR)\peplus.sbr" \
	"$(INTDIR)\StdAfx.sbr"

"$(OUTDIR)\daiRptViewer.bsc" : "$(OUTDIR)" $(BSC32_SBRS)
    $(BSC32) @<<
  $(BSC32_FLAGS) $(BSC32_SBRS)
<<

LINK32=link.exe
LINK32_FLAGS=/nologo /subsystem:windows /dll /incremental:yes /pdb:"$(OUTDIR)\daiRptViewer.pdb" /debug /machine:I386 /def:".\daiRptViewer.def" /out:"$(OUTDIR)\daiRptViewer.dll" /implib:"$(OUTDIR)\daiRptViewer.lib" /pdbtype:sept 
DEF_FILE= \
	".\daiRptViewer.def"
LINK32_OBJS= \
	"$(INTDIR)\daiRptViewer.obj" \
	"$(INTDIR)\peplus.obj" \
	"$(INTDIR)\StdAfx.obj" \
	"$(INTDIR)\daiRptViewer.res" \
	".\crpe32m.lib"

"$(OUTDIR)\daiRptViewer.dll" : "$(OUTDIR)" $(DEF_FILE) $(LINK32_OBJS)
    $(LINK32) @<<
  $(LINK32_FLAGS) $(LINK32_OBJS)
<<

!ENDIF 


!IF "$(NO_EXTERNAL_DEPS)" != "1"
!IF EXISTS("daiRptViewer.dep")
!INCLUDE "daiRptViewer.dep"
!ELSE 
!MESSAGE Warning: cannot find "daiRptViewer.dep"
!ENDIF 
!ENDIF 


!IF "$(CFG)" == "daiRptViewer - Win32 Release" || "$(CFG)" == "daiRptViewer - Win32 Debug"
SOURCE=.\daiRptViewer.cpp

!IF  "$(CFG)" == "daiRptViewer - Win32 Release"


"$(INTDIR)\daiRptViewer.obj" : $(SOURCE) "$(INTDIR)" "$(INTDIR)\daiRptViewer.pch"


!ELSEIF  "$(CFG)" == "daiRptViewer - Win32 Debug"


"$(INTDIR)\daiRptViewer.obj"	"$(INTDIR)\daiRptViewer.sbr" : $(SOURCE) "$(INTDIR)" "$(INTDIR)\daiRptViewer.pch"


!ENDIF 

SOURCE=.\daiRptViewer.rc

"$(INTDIR)\daiRptViewer.res" : $(SOURCE) "$(INTDIR)"
	$(RSC) $(RSC_PROJ) $(SOURCE)


SOURCE=.\peplus.cpp

!IF  "$(CFG)" == "daiRptViewer - Win32 Release"


"$(INTDIR)\peplus.obj" : $(SOURCE) "$(INTDIR)" "$(INTDIR)\daiRptViewer.pch"


!ELSEIF  "$(CFG)" == "daiRptViewer - Win32 Debug"


"$(INTDIR)\peplus.obj"	"$(INTDIR)\peplus.sbr" : $(SOURCE) "$(INTDIR)" "$(INTDIR)\daiRptViewer.pch"


!ENDIF 

SOURCE=.\StdAfx.cpp

!IF  "$(CFG)" == "daiRptViewer - Win32 Release"

CPP_SWITCHES=/nologo /MT /W3 /GX /O2 /D "WIN32" /D "NDEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_MBCS" /D "_USRDLL" /Fp"$(INTDIR)\daiRptViewer.pch" /Yc"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /c 

"$(INTDIR)\StdAfx.obj"	"$(INTDIR)\daiRptViewer.pch" : $(SOURCE) "$(INTDIR)"
	$(CPP) @<<
  $(CPP_SWITCHES) $(SOURCE)
<<


!ELSEIF  "$(CFG)" == "daiRptViewer - Win32 Debug"

CPP_SWITCHES=/nologo /MTd /W3 /Gm /GX /ZI /Od /D "WIN32" /D "_DEBUG" /D "_WINDOWS" /D "_WINDLL" /D "_MBCS" /D "_USRDLL" /FR"$(INTDIR)\\" /Fp"$(INTDIR)\daiRptViewer.pch" /Yc"stdafx.h" /Fo"$(INTDIR)\\" /Fd"$(INTDIR)\\" /FD /GZ /c 

"$(INTDIR)\StdAfx.obj"	"$(INTDIR)\StdAfx.sbr"	"$(INTDIR)\daiRptViewer.pch" : $(SOURCE) "$(INTDIR)"
	$(CPP) @<<
  $(CPP_SWITCHES) $(SOURCE)
<<


!ENDIF 


!ENDIF 

