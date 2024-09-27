package com.nou.sgu.dao;

import com.acer.db.DBManager;
import com.acer.db.query.DBResult;
import com.acer.util.Utility;
import com.acer.apps.Page;

import java.sql.Connection;
import java.util.Vector;
import java.util.Hashtable;

/*
 * (SGUT002) Gateway/*
 *-------------------------------------------------------------------------------*
 * Author    : 國長      2007/07/24
 * Modification Log :
 * Vers     Date           By             Notes
 *--------- -------------- -------------- ----------------------------------------
 * V0.0.1   2007/07/24     國長           建立程式
 *                                        新增 getSgut002ForUse(Hashtable ht)
 * V0.0.2   2007/07/26     俊賢           新增 getSgut002Sgut001ForUse(Hashtable ht)
 *--------------------------------------------------------------------------------
 */
public class SGUT002GATEWAY {

    /** 資料排序方式 */
    private String orderBy = "";
    private DBManager dbmanager = null;
    private Connection conn = null;
    /* 頁數 */
    private int pageNo = 0;
    /** 每頁筆數 */
    private int pageSize = 0;

    /** 記錄是否分頁 */
    private boolean pageQuery = false;

    /** 用來存放 SQL 語法的物件 */
    private StringBuffer sql = new StringBuffer();

    /** <pre>
     *  設定資料排序方式.
     *  Ex: "AYEAR, SMS DESC"
     *      先以 AYEAR 排序再以 SMS 倒序序排序
     *  </pre>
     */
    public void setOrderBy(String orderBy) {
        if(orderBy == null) {
            orderBy = "";
        }
        this.orderBy = orderBy;
    }

    /** 取得總筆數 */
    public int getTotalRowCount() {
        return Page.getTotalRowCount();
    }

    /** 不允許建立空的物件 */
    private SGUT002GATEWAY() {}

    /** 建構子，查詢全部資料用 */
    public SGUT002GATEWAY(DBManager dbmanager, Connection conn) {
        this.dbmanager = dbmanager;
        this.conn = conn;
    }

    /** 建構子，查詢分頁資料用 */
    public SGUT002GATEWAY(DBManager dbmanager, Connection conn, int pageNo, int pageSize) {
        this.dbmanager = dbmanager;
        this.conn = conn;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        pageQuery = true;
    }

    /**
     *
     * @param ht 條件值
     * @return 回傳 Vector 物件，內容為 Hashtable 的集合，<br>
     *         每一個 Hashtable 其 KEY 為欄位名稱，KEY 的值為欄位的值<br>
     *         若該欄位有中文名稱，則其 KEY 請加上 _NAME, EX: SMS 其中文欄位請設為 SMS_NAME
     * @throws Exception
     */
    public Vector getSgut002ForUse(Hashtable ht) throws Exception {
        if(ht == null) {
            ht = new Hashtable();
        }
        Vector result = new Vector();
        if(sql.length() > 0) {
            sql.delete(0, sql.length());
        }
        sql.append(
            "SELECT S02.SCHOLAR_TYPE_CODE, S02.SCHOLAR_TYPE, S02.AMT, S02.RMK, S02.UPD_USER_ID, S02.UPD_DATE, S02.UPD_TIME, S02.UPD_MK, S02.ROWSTAMP " +
            "FROM SGUT002 S02 " +
            "WHERE 1 = 1 "
        );
        if(!Utility.nullToSpace(ht.get("SCHOLAR_TYPE_CODE")).equals("")) {
            sql.append("AND S02.SCHOLAR_TYPE_CODE = '" + Utility.nullToSpace(ht.get("SCHOLAR_TYPE_CODE")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("SCHOLAR_TYPE")).equals("")) {
            sql.append("AND S02.SCHOLAR_TYPE = '" + Utility.nullToSpace(ht.get("SCHOLAR_TYPE")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("AMT")).equals("")) {
            sql.append("AND S02.AMT = '" + Utility.nullToSpace(ht.get("AMT")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("RMK")).equals("")) {
            sql.append("AND S02.RMK = '" + Utility.nullToSpace(ht.get("RMK")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_USER_ID")).equals("")) {
            sql.append("AND S02.UPD_USER_ID = '" + Utility.nullToSpace(ht.get("UPD_USER_ID")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_DATE")).equals("")) {
            sql.append("AND S02.UPD_DATE = '" + Utility.nullToSpace(ht.get("UPD_DATE")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_TIME")).equals("")) {
            sql.append("AND S02.UPD_TIME = '" + Utility.nullToSpace(ht.get("UPD_TIME")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("UPD_MK")).equals("")) {
            sql.append("AND S02.UPD_MK = '" + Utility.nullToSpace(ht.get("UPD_MK")) + "' ");
        }
        if(!Utility.nullToSpace(ht.get("ROWSTAMP")).equals("")) {
            sql.append("AND S02.ROWSTAMP = '" + Utility.nullToSpace(ht.get("ROWSTAMP")) + "' ");
        }

        if(!orderBy.equals("")) {
            String[] orderByArray = orderBy.split(",");
            for(int i = 0; i < orderByArray.length; i++) {
                orderByArray[i] = "S02." + orderByArray[i].trim();

                if(i == 0) {
                    orderBy += "ORDER BY ";
                } else {
                    orderBy += ", ";
                }
                orderBy += orderByArray[i].trim();
            }
            sql.append(orderBy.toUpperCase());
            orderBy = "";
        }

        DBResult rs = null;
        try {
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++)
                    rowHt.put(rs.getColumnName(i), rs.getString(i));

                result.add(rowHt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }
    /*
     * sgut002m查詢資料
     *
     */
    public Vector getSgut002Sgut001ForUse(Hashtable ht) throws Exception {

        Vector result = new Vector();
        if(sql.length() > 0) {
            sql.delete(0, sql.length());
        }

        sql.append
        (
            "SELECT SGUT002.AYEAR,SGUT002.SMS,SGUT002.SCHOLAR_TYPE_CODE,SGUT001.SCHOLAR_TYPE_CH,SGUT002.SCHOLAR_TYPE,SGUT002.AMT,SGUT002.RMK " +
            "FROM SGUT001 JOIN SGUT002 ON SGUT001.SCHOLAR_TYPE_CODE = SGUT002.SCHOLAR_TYPE_CODE  " +
            "WHERE 1  =  1 "
        );

        /** == 查詢條件 ST == */
        if(!Utility.nullToSpace(ht.get("SCHOLAR_TYPE_CODE")).equals("")) {
            sql.append("AND SGUT002.SCHOLAR_TYPE_CODE = '" + Utility.nullToSpace(ht.get("SCHOLAR_TYPE_CODE")) + "' ");
        }

        /** == 查詢條件 ED == */

        if(!orderBy.equals("")) {
            String[] orderByArray = orderBy.split(",");
            orderBy = "";
            for(int i = 0; i < orderByArray.length; i++) {
                orderByArray[i] = "SGUT002." + orderByArray[i].trim();

                if(i == 0) {
                    orderBy += "ORDER BY ";
                } else {
                    orderBy += ", ";
                }
                orderBy += orderByArray[i].trim();
            }
            sql.append(orderBy.toUpperCase());
            orderBy = "";
        }

        DBResult rs = null;
        try {
            if(pageQuery) {
                // 依分頁取出資料
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // 取出所有資料
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** 將欄位抄一份過去 */
                for (int i = 1; i <= rs.getColumnCount(); i++)
                    rowHt.put(rs.getColumnName(i), rs.getString(i));

                result.add(rowHt);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if(rs != null) {
                rs.close();
            }
        }
        return result;
    }
	
	public void getSgu002mQuery(Vector vt, Hashtable ht) throws Exception {
		DBResult rs = null;
		try
		{
			if(sql.length() >0)
				sql.delete(0, sql.length());

			sql.append
			(
				"SELECT a.ASYS, a.AYEAR, a.HANDICAP_TYPE " +
				", case when a.ayear <= '112' " +
				"       then (SELECT CODE_NAME FROM SYST001 WHERE KIND='HANDICAP_TYPE_112' AND CODE=a.HANDICAP_TYPE) " +
				"  		else (SELECT CODE_NAME FROM SYST001 WHERE KIND='HANDICAP_TYPE' AND CODE=a.HANDICAP_TYPE) end AS HANDICAP_TYPE_NAME " +
				", a.SCHOLAR_AMT, a.GRANT_AMT " +
				", (SELECT CODE_NAME FROM SYST001 WHERE KIND='HANDICAP_GRADE' AND CODE=a.HANDICAP_GRADE) AS HANDICAP_GRADE_NAME " +
				"FROM SGUT002 a " +
				"WHERE '1'='1' " 
			);

			sql.append("AND a.ASYS = '" + Utility.dbStr(ht.get("ASYS")) + "' ");

			sql.append("AND a.AYEAR = '" + Utility.dbStr(ht.get("AYEAR")) + "' ");

			sql.append("ORDER BY a.HANDICAP_TYPE ASC, a.HANDICAP_GRADE ASC" );

			if(pageQuery) {
				rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
			} else {
				rs = dbmanager.getSimpleResultSet(conn);
				rs.open();
				rs.executeQuery(sql.toString());
			}

			Hashtable rowHt = null;
			while (rs.next())
			{
				rowHt = new Hashtable();
				for (int i = 1; i <= rs.getColumnCount(); i++)
					rowHt.put(rs.getColumnName(i), rs.getString(i));
				vt.add(rowHt);
			}

		}
		catch(Exception e)
		{
			throw e;
		}
		finally
		{
			if (rs != null)
				rs.close();
		}
	}
}