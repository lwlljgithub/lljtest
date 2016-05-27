package com.hhwy.iepip.scpmm.pm.service;

import java.sql.SQLException;
import java.util.*;

import persistent.popedom.OrgFilter;
import persistent.scpmm.TScpmmPmArchiveMainInfoJgModel;

import com.hhwy.iepip.framework.core.Service;

/**
 * 
 * 创建日期:2011-2-18
 * Title:质检报告Service类
 * Description：对本文件的详细描述，原则上不能少于30字
 * @author Administrator
 * @mender：（文件的修改者，文件创建者之外的人）
 * @version 1.0
 * Remark：认为有必要的其他信息
 */
public interface IZlgdModelService extends Service{


	List hasChildNode(String id);

	TScpmmPmArchiveMainInfoJgModel getFzjstype(OrgFilter filter);

	List<TScpmmPmArchiveMainInfoJgModel> init(OrgFilter filter);

	List<TScpmmPmArchiveMainInfoJgModel> open(String id);

	String initTree(String mainId);

	int deleteFzjsById(String id);


}
