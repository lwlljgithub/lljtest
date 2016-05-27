package com.hhwy.iepip.scpmm.pm.service;

import java.sql.SQLException;
import java.util.*;

import persistent.popedom.OrgFilter;
import persistent.scpmm.TScpmmPmArchiveMainInfoJgModel;

import com.hhwy.iepip.framework.core.Service;

/**
 * 
 * ��������:2011-2-18
 * Title:�ʼ챨��Service��
 * Description���Ա��ļ�����ϸ������ԭ���ϲ�������30��
 * @author Administrator
 * @mender�����ļ����޸��ߣ��ļ�������֮����ˣ�
 * @version 1.0
 * Remark����Ϊ�б�Ҫ��������Ϣ
 */
public interface IZlgdModelService extends Service{


	List hasChildNode(String id);

	TScpmmPmArchiveMainInfoJgModel getFzjstype(OrgFilter filter);

	List<TScpmmPmArchiveMainInfoJgModel> init(OrgFilter filter);

	List<TScpmmPmArchiveMainInfoJgModel> open(String id);

	String initTree(String mainId);

	int deleteFzjsById(String id);


}
