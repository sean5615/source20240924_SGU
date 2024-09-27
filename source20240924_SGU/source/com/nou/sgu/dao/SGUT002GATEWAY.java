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
 * Author    : ���      2007/07/24
 * Modification Log :
 * Vers     Date           By             Notes
 *--------- -------------- -------------- ----------------------------------------
 * V0.0.1   2007/07/24     ���           �إߵ{��
 *                                        �s�W getSgut002ForUse(Hashtable ht)
 * V0.0.2   2007/07/26     �T��           �s�W getSgut002Sgut001ForUse(Hashtable ht)
 *--------------------------------------------------------------------------------
 */
public class SGUT002GATEWAY {

    /** ��ƱƧǤ覡 */
    private String orderBy = "";
    private DBManager dbmanager = null;
    private Connection conn = null;
    /* ���� */
    private int pageNo = 0;
    /** �C������ */
    private int pageSize = 0;

    /** �O���O�_���� */
    private boolean pageQuery = false;

    /** �ΨӦs�� SQL �y�k������ */
    private StringBuffer sql = new StringBuffer();

    /** <pre>
     *  �]�w��ƱƧǤ覡.
     *  Ex: "AYEAR, SMS DESC"
     *      ���H AYEAR �ƧǦA�H SMS �˧ǧǱƧ�
     *  </pre>
     */
    public void setOrderBy(String orderBy) {
        if(orderBy == null) {
            orderBy = "";
        }
        this.orderBy = orderBy;
    }

    /** ���o�`���� */
    public int getTotalRowCount() {
        return Page.getTotalRowCount();
    }

    /** �����\�إߪŪ����� */
    private SGUT002GATEWAY() {}

    /** �غc�l�A�d�ߥ�����ƥ� */
    public SGUT002GATEWAY(DBManager dbmanager, Connection conn) {
        this.dbmanager = dbmanager;
        this.conn = conn;
    }

    /** �غc�l�A�d�ߤ�����ƥ� */
    public SGUT002GATEWAY(DBManager dbmanager, Connection conn, int pageNo, int pageSize) {
        this.dbmanager = dbmanager;
        this.conn = conn;
        this.pageNo = pageNo;
        this.pageSize = pageSize;
        pageQuery = true;
    }

    /**
     *
     * @param ht �����
     * @return �^�� Vector ����A���e�� Hashtable �����X�A<br>
     *         �C�@�� Hashtable �� KEY �����W�١AKEY ���Ȭ���쪺��<br>
     *         �Y����즳����W�١A�h�� KEY �Х[�W _NAME, EX: SMS �䤤�����г]�� SMS_NAME
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
                // �̤������X���
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // ���X�Ҧ����
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** �N���ۤ@���L�h */
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
     * sgut002m�d�߸��
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

        /** == �d�߱��� ST == */
        if(!Utility.nullToSpace(ht.get("SCHOLAR_TYPE_CODE")).equals("")) {
            sql.append("AND SGUT002.SCHOLAR_TYPE_CODE = '" + Utility.nullToSpace(ht.get("SCHOLAR_TYPE_CODE")) + "' ");
        }

        /** == �d�߱��� ED == */

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
                // �̤������X���
                rs = Page.getPageResultSet(dbmanager, conn, sql.toString(), pageNo, pageSize);
            } else {
                // ���X�Ҧ����
                rs = dbmanager.getSimpleResultSet(conn);
                rs.open();
                rs.executeQuery(sql.toString());
            }
            Hashtable rowHt = null;
            while (rs.next()) {
                rowHt = new Hashtable();
                /** �N���ۤ@���L�h */
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