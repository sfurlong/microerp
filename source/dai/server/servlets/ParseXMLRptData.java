package dai.server.servlets;

import java.io.File;
import com.sun.xml.tree.*;
import org.w3c.dom.*;
import org.xml.sax.*;
import com.sun.xml.parser.*;

import dai.shared.cmnSvcs.*;

public class ParseXMLRptData {

	XmlDocument	_doc;

    private String[]    _colNames;
    private String[]    _colHeadings;
    private String[]    _colFormatting;
    private String[]    _tableNames;
    private SessionMetaData _sessionMeta;
    private int[]       _detailColNums;
    private int[]       _groupColNums;
    private String[]    _detailCellTags;
    private int[]       _groupSumColNums;
    private int[]       _rptSumColNums;
    private int         _groupByColNum;
    private String[]    _replacementParms;
    private String      _rptHeader;
    private boolean     _rptUsesGrouping;

    public ParseXMLRptData(String rptName, String[] replacementParms) {

        _sessionMeta = SessionMetaData.getInstance();
        _replacementParms = replacementParms;

        try {
            rptName = _sessionMeta.getDaiRptHome()+rptName;

	        // turn the filename into an input source
	        InputSource input = Resolver.createInputSource (new File (rptName));

	        // turn it into an in-memory object
	        // ... the "false" flag says not to validate
	        _doc = XmlDocument.createXmlDocument (input, false);


            //Parse the Columns
            NodeList listOfCols = _doc.getDocumentElement().getElementsByTagName("Column");
            parseColsTag(listOfCols);

            //Parse the Tables
            NodeList listOfTables = _doc.getDocumentElement().getElementsByTagName("Table");
            parseTablesTag(listOfTables);

            //Parse the detailColData
            NodeList detailColData = _doc.getDocumentElement().getElementsByTagName("Detail");
            parseDetailTag((Element)detailColData.item(0));

            //Parse the groupHeader Tag
            NodeList rptHeader = _doc.getDocumentElement().getElementsByTagName("ReportHeader");
            parseReportHeaderTag((Element)rptHeader.item(0));

            //Parse the groupColNums
            NodeList groupColNums = _doc.getDocumentElement().getElementsByTagName("GroupHeader");
            if (groupColNums.getLength() > 0) {
                parseGroupTag((Element)groupColNums.item(0));
                _rptUsesGrouping = true;
            } else {
                _rptUsesGrouping = false;
            }

            //Parse the groupFooter Tag
            NodeList groupFooterColNums = _doc.getDocumentElement().getElementsByTagName("GroupFooter");
            if (groupFooterColNums.getLength() > 0) {
                parseGroupFooterTag((Element)groupFooterColNums.item(0));
            }

            //Parse the RptFooter
            NodeList rptFooterColNums = _doc.getDocumentElement().getElementsByTagName("ReportFooter");
            if (rptFooterColNums.getLength() > 0) {
                parseRptFooterTag((Element)rptFooterColNums.item(0));
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public String[] getColHeadings() {
        return _colHeadings;
    }

    public String[] getColFormatting() {
        return _colFormatting;
    }

    public String getRptTitle() {
        return useReplacementParms(_rptHeader);
    }

    public String getSQLStmt() {
        String sqlStmt = " select ";
        //Add the column we are selecting
        for (int i=0; i<_colNames.length; i++)
        {
            sqlStmt = sqlStmt + _colNames[i];
            if (i < (_colNames.length-1)) sqlStmt = sqlStmt + ", ";
        }

        sqlStmt = sqlStmt + " from ";

        //Add the tables we are selecting from
        for (int i=0; i<_tableNames.length; i++)
        {
            sqlStmt = sqlStmt + _tableNames[i];
            if (i < (_tableNames.length-1)) sqlStmt = sqlStmt + ", ";
        }

        //Add the where clause.
        sqlStmt = sqlStmt + " " + getNodeValue(_doc.getDocumentElement(), "SQL_Where_Clause");

        return useReplacementParms(sqlStmt);
    }

    public int[] getDetailColNums()
    {
        return _detailColNums;
    }

    public boolean rptUsesGrouping() {
        return _rptUsesGrouping;
    }
    public String getDetailCellTags(int detailColNum)
    {
        String ret = _detailCellTags[detailColNum];
        if (ret == null) {ret = "";}
        return ret;
    }

    public int[] getGroupColNums()
    {
        int[] ret = null;

        if (_groupColNums == null) {
            ret = new int[0];
        } else {
            ret = _groupColNums;
        }
        return ret;
    }

    public int getGroupByColNum()
    {
        return _groupByColNum;
    }

    public int[] getGroupSumColNums()
    {
        int[] ret = null;
        if (_groupSumColNums == null) {
            ret = new int[0];
        } else {
            ret = _groupSumColNums;
        }
        return ret;
    }

    public int[] getRptSumColNums()
    {
        int[] ret = null;
        if (_rptSumColNums == null) {
            ret = new int[0];
        } else {
            ret = _rptSumColNums;
        }
        return ret;
    }

    private void parseDetailTag(Element detailNode)
    {
        Element detailColData;
        NodeList detailColList = detailNode.getElementsByTagName("DetailCol");

        int numDetailCols = detailColList.getLength();
        _detailColNums = new int[numDetailCols];
        _detailCellTags = new String[numDetailCols];

        String s_detailColNum;
        for (int i=0; i<numDetailCols; i++) {
            detailColData = (Element)detailColList.item(i);
            _detailColNums[i] = Integer.parseInt(getNodeValue(detailColData, "DetailColPos"));
            _detailCellTags[i] = getNodeValue(detailColData, "DetailCellTags");
        }
    }

    private void parseReportHeaderTag(Element rptHeaderNode)
    {
        _rptHeader = getNodeValue (rptHeaderNode, "Report_Title");
    }

    private void parseGroupTag(Element groupNode)
    {
        NodeList colNumList = groupNode.getElementsByTagName("ColNum");

        String s_groupByColNum = getNodeValue (groupNode, "GroupByColNum");
        _groupByColNum = Integer.parseInt(s_groupByColNum);

        int numCols = colNumList.getLength();
        _groupColNums = new int[numCols];

        String s_ColNum;
        for (int i=0; i<numCols; i++) {
            s_ColNum = colNumList.item(i).getChildNodes().item(0).getNodeValue().trim();
            _groupColNums[i] = Integer.parseInt(s_ColNum);
        }
    }

    private void parseGroupFooterTag(Element groupNode)
    {
        NodeList colNumList = groupNode.getElementsByTagName("SumColPos");

        int numCols = colNumList.getLength();
        _groupSumColNums = new int[numCols];

        String s_ColNum;
        for (int i=0; i<numCols; i++) {
            s_ColNum = colNumList.item(i).getChildNodes().item(0).getNodeValue().trim();
            _groupSumColNums[i] = Integer.parseInt(s_ColNum);
        }
    }

    private void parseRptFooterTag(Element footerNode)
    {
        NodeList colNumList = footerNode.getElementsByTagName("SumColPos");

        int numCols = colNumList.getLength();
        _rptSumColNums = new int[numCols];

        String s_ColNum;
        for (int i=0; i<numCols; i++) {
            s_ColNum = colNumList.item(i).getChildNodes().item(0).getNodeValue().trim();
            _rptSumColNums[i] = Integer.parseInt(s_ColNum);
        }
    }

    private void parseColsTag(NodeList colList)
    {
        int numCols = colList.getLength();
        _colFormatting = new String[numCols];
        _colHeadings = new String[numCols];
        _colNames = new String[numCols];

        for (int i=0; i<numCols; i++) {
            _colNames[i] = getNodeValue((Element)colList.item(i), "Col_SQL_Name");
            _colHeadings[i] = getNodeValue((Element)colList.item(i), "Col_Heading");
            _colFormatting[i] = getNodeValue((Element)colList.item(i), "Display_Format");
        }
    }

    private void parseTablesTag(NodeList tabList)
    {
        int numTabs = tabList.getLength();
        _tableNames = new String[numTabs];

        for (int i=0; i<numTabs; i++) {
            _tableNames[i] = getNodeValue((Element)tabList.item(i), "Table_SQL_Name");
        }
    }

    private String getNodeValue (Element elm, String tagName)
    {
        try {
        NodeList elements = elm.getElementsByTagName(tagName);
        if (elements == null) return null;
        Node node = elements.item(0);
        if (node == null) return null;
        NodeList nodes = node.getChildNodes();

        //find the value that is non white space
        String s;
        for (int i=0; i<nodes.getLength(); i++)
        {
            s = ((Node)nodes.item(i)).getNodeValue().trim();
            if (s.equals("") || s.equals("\r")) {
                continue;
            } else {
                return s;
            }
        }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private String useReplacementParms(String withRepParms)
    {
        boolean moreRepParms = true;
        int     pos = 0;
        int     cnt = 0;


        while (moreRepParms)
        {
            pos = withRepParms.indexOf("?:", pos);
            if (pos > 0) {
                withRepParms = withRepParms.substring(0, pos) +
                                _replacementParms[cnt] +
                                withRepParms.substring(pos+3, withRepParms.length());
                cnt++;
            } else {
                moreRepParms = false;
            }
        }

        return withRepParms;
    }

    public static void main (String argv [])
    {
        ParseXMLRptData du = new ParseXMLRptData("gl.xml", new String[] {"12/19/1999", "12/20/2000"});
        System.out.println(du.getSQLStmt());

    }
}