/*
** File:    UXFRTF.h
**
** Author:  Douglas Lankshear
** Date:    93-07-08
**
** Purpose: An export format DLL for Doc files.
**
** Copyright (c) 1993  Seagate Software Information Management Group, Inc.
*/

#if !defined (UXFRTF_H)
#define UXFRTF_H

#if defined (__cplusplus)
extern "C"
{
#endif

// Set 1-byte structure alignment
#if defined (__BORLANDC__)               // Borland C/C++
  #pragma option -a-
#elif defined (_MSC_VER)                 // Microsoft Visual C++
  #if _MSC_VER >= 900                    // MSVC 2.x and later
    #pragma pack (push)
  #endif
  #pragma pack (1)
#endif 

/********************************************/

#define UXFRichTextFormatType  0

// No options.

#if defined (__cplusplus)
}
#endif

/********************************************/
// Reset structure alignment
#if defined (__BORLANDC__)
  #pragma option -a.
#elif defined (_MSC_VER)
  #if _MSC_VER >= 900
    #pragma pack (pop)
  #else
    #pragma pack ()
  #endif
#endif    

#endif // UXFRTF_H
