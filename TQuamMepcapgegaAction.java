package com.hhwy.iepip.scpmm.pm.action;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.ServletActionContext;
import persistent.controls.TCtlFiles;
import com.hhwy.iepip.controls.helper.FileDown;
import com.hhwy.iepip.controls.helper.FileHelper;
import com.hhwy.iepip.controls.helper.FileManager;
import com.hhwy.iepip.controls.util.FileFunction;
import com.hhwy.iepip.framework.container.BeanProvider;
import com.hhwy.iepip.framework.web.action.Action;
import com.hhwy.iepip.scpmm.pm.message.ArchiveDelService;

public class TQuamMepcapgegaAction extends Action{
	
	private String filePath; //文件路径
	private String fileName; //文件名称
	private String fileOnlyName; //文件唯一名称
	
	/** 附件管理系列类 */
	private FileHelper fileHelp = new FileHelper();

	/** 附件管理类 */
	private FileManager filManager = new FileManager();

	private FileFunction function = new FileFunction();
	
	
	private ArchiveDelService archiveDelService = (ArchiveDelService) BeanProvider
			.getService("pm.comPactInfoService");
	
      /**
       * 除了最子节点以外上级目录打包下载接口
       * @author llj
       * @return
       */
      public String downLoad(){
    	 String ids=this.getHttpRequest().getParameter("ids");//获取选中节点下面所有最叶节点(无根节点的子节点)
    	 String treeProjectId=(String)this.getHttpRequest().getSession().getAttribute("treeProjectId");
    	 String [] idsArray= ids.split(",");
    	 List listCtlFileId=new ArrayList<String>();
    	 List<Map<String ,Object>> PmArchiveDellist=null;
    	 List<Map<String ,Object>> ctlFiles=null;
    	 List<String> groupIdList=new ArrayList<String>();
    	n:for(int i=0;i<idsArray.length;i++){
    		  try {
				PmArchiveDellist= archiveDelService.getTScpmmPmArchiveDelById(idsArray[i],treeProjectId);
				if(PmArchiveDellist.size()>0){
				 groupIdList.add((String)PmArchiveDellist.get(0).get("GROUPID"));
				 ctlFiles=archiveDelService.getTCtlFilesByGroupId((String)PmArchiveDellist.get(0).get("GROUPID"));
				}else{
					continue n;
				}
				if(ctlFiles!=null&&ctlFiles.size()>0){
					listCtlFileId.add(ctlFiles.get(0).get("ID"));
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
  		String ret = null;
  		String path="";
  		File[] files=new File[100];
  		String fileId = getHttpRequest().getParameter("fileId"); // 附件主键id
  		String checkId = getHttpRequest().getParameter("checkId"); // 下载或删除时候的多余验证随机数来防止非法url
  		boolean fileIsNull = null != fileId && !"".equals(fileId);
  		path = function.getFileSavePathRoot();
  		//path=CommonTools.readValue("downLoadFilePath");
  		try {
  			execute1(path,files,groupIdList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  		return ret;
  	}
      
      /**
       * 最后一级节点打包下载接口
       * @author llj
       * @return
       */
      public String downLoadLastlevel(){
    	 String ids=this.getHttpRequest().getParameter("ids");//获取选中节点下面所有最叶节点(无根节点的子节点)
    	 String treeProjectId=(String)this.getHttpRequest().getSession().getAttribute("treeProjectId");
    	 String [] idsArray= ids.split(",");
    	 List listCtlFileId=new ArrayList<String>();
    	 List<Map<String ,Object>> PmArchiveDellist=null;
    	 List<Map<String ,Object>> ctlFiles=null;
    	 List<String> groupIdList=new ArrayList<String>();
    	 for(int i=0;i<idsArray.length;i++){
    		  try {
				PmArchiveDellist= archiveDelService.getTScpmmPmArchiveDelById(idsArray[i],treeProjectId);
				groupIdList.add((String)PmArchiveDellist.get(0).get("GROUPID"));
				ctlFiles=archiveDelService.getTCtlFilesByGroupId((String)PmArchiveDellist.get(0).get("GROUPID"));
				if(ctlFiles!=null&&ctlFiles.size()>0){
					listCtlFileId.add(ctlFiles.get(0).get("ID"));
				}
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }
  		String ret = null;
  		String path="";
  		File[] files=new File[100];
  		String fileId = getHttpRequest().getParameter("fileId"); // 附件主键id
  		String checkId = getHttpRequest().getParameter("checkId"); // 下载或删除时候的多余验证随机数来防止非法url
  		boolean fileIsNull = null != fileId && !"".equals(fileId);
  		for(int a=0;a<listCtlFileId.size();a++){
  			fileId=(String) listCtlFileId.get(a);
  		if (checkId != null && !"".equals(checkId)) {
  		} else {
  			FileDown fileDown = null;
  			TCtlFiles tFile = fileHelp.getFileByFileId(fileId);
  			String fileName = FileFunction.getCanonicalName(tFile.getFileName());
  			//** 从多个存储路径中下载附件 *//*
  			String[] paths = function.getFileSavePathConfig().split(";");  //获取配置的附件根路径
  			String fileStorePath = "";
  			for (int i = 0; i < paths.length; i++) {
  				if(!paths[i].endsWith(File.separator)){  //是否已文件分隔符结尾
  					paths[i] = paths[i] + File.separator ; 
  				}
  				fileStorePath = paths[i] + new FileFunction().getRealFilePath(tFile.getFilePath());   //附件存放的绝对路径
  				path=CommonTools.readValue("downLoadFilePath");
  				files[a]=new File(path+tFile.getFileName());
  			}
  		 }
  		}
  		try {
  			execute(path,files,groupIdList);
		} catch (IOException e) {
			e.printStackTrace();
		}
  		return ret;
  	}
     
      public String execute1(String FilePath,File[] file1,List<String> listGroupId) throws IOException {   
	       //生成的ZIP文件名为Demo.zip   
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		    String date=sdf.format(new Date());
		    int fileNum=0;
		    int c=0;
		    //File[] files=new File[500];
		    boolean isFirst=true;
	        String tmpFileName = "归档文件包-"+date+".zip";   
	        byte[] buffer = new byte[1024];   
	        String strZipPath = FilePath + tmpFileName; 
	        ZipOutputStream out =null;
	        List<Map<String,Object>> map=null;
	        String downLoadpath="";
	        Set<String> pathSet=new HashSet<String>();
	        List<Map<String,Object>> tScpmmPmArchiveMainJgMap=null;
	       // File[] files=new File[300];
	        try {
	        	for(int a=0;a<listGroupId.size();a++){//循环每一条子节点获取他的文件路径
		            String path="";
		            String pid="";
					map=archiveDelService.getTScpmmPmArchiveDelPidByGid(listGroupId.get(a));
					if(map==null){
						continue;
						 //pid=(String)map.get(0).get("PARENT_ID");
					}
					pid=(String)map.get(0).get("PARENT_ID");
					tScpmmPmArchiveMainJgMap=archiveDelService.getTScpmmPmArchiveMainJgById(pid);
					for(int i=tScpmmPmArchiveMainJgMap.size()-1;i>=0;i--){
						path+=tScpmmPmArchiveMainJgMap.get(i).get("NODE_NAME")+File.separator;
					}
					pathSet.add(path+"&"+pid);//将获取到的所以路径装入pathSet
	         }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        List<String> pathList=new ArrayList<String>();//不同的目录结构
	        Iterator<String> it = pathSet.iterator();
	        while(it.hasNext()){
	        	pathList.add(it.next());
	        }
	       try {   
	            out = new ZipOutputStream(new FileOutputStream(   
	                   strZipPath));
	            downLoadpath = function.getFileSavePathRoot();
	           // downLoadpath=CommonTools.readValue("downLoadFilePath");
	          f:for(int m=0;m<pathList.size();m++){
	        	  File[] files=new File[500];
	        	  String path=pathList.get(m);
	        	  String pathArray[]= path.split("&");
	        	  String projectId=(String)this.getHttpRequest().getSession().getAttribute("treeProjectId");
	        	  List<Map<String,Object>> pmArchiveDelMapList= archiveDelService.getTScpmmPmArchiveDelByPid(pathArray[1],projectId); 
	        	  int h=0;
	        	  for(int k=0;k<pmArchiveDelMapList.size();k++){
		        		 String gid=(String) pmArchiveDelMapList.get(k).get("GROUPID");
		        		 List<Map<String,Object>>ctlFiles=archiveDelService.getTCtlFilesByGroupId(gid);	 
		        		 TCtlFiles tFile=null;
		        		if(ctlFiles!=null){
		        		 for(int q=0;q<ctlFiles.size();q++){
		        			
		        			 tFile= fileHelp.getFileByFileId((String)ctlFiles.get(q).get("ID"));
		        			 files[h]=new File(downLoadpath+tFile.getFileName());
		        			// files[fileNum]=new File(downLoadpath+tFile.getFileName());
		        			 h++;
		        			 fileNum++;
		        		 }
		        			
		        		}
		        	  }	
	        	 n:for (int n = 0; n < files.length+1; n++) {
	        		      if(isFirst){ 
			            	File filea=null;
			            	if(files[n]==null){
			            		isFirst=false;
			            	}
			            	if(files[n]!=null){
			            		 filea =new File(FilePath+files[n].getName());
			            	}else{
			            		continue f;
			            	}
			                if(filea.exists()){       
				                FileInputStream fis = new FileInputStream(files[n]);  
				                out.putNextEntry(new ZipEntry(pathArray[0]+files[n].getName()));
				                //设置压缩文件内的字符编码，不然会变成乱码   
				               //out.setEncoding("GBK");  
				               int len;   
				               // 读入需要下载的文件的内容，打包到zip文件   
				               while ((len = fis.read(buffer)) > 0) {   
				                    out.write(buffer, 0, len);   
				                }   
				               out.closeEntry();   
				               fis.close();   
			            	}
	        		      } else{//区分第一次
	        		    	  h:for (int j = 0; j < files.length+1; j++) {
	        		    	  File filea=null;
				            	if(files[j]==null){
				            		continue f;
				            		//continue f;
				            	}else{
				            		filea =new File(FilePath+files[j].getName());
				            	}
				                if(filea.exists()){       
					                FileInputStream fis = new FileInputStream(files[j]);  
					                out.putNextEntry(new ZipEntry(pathArray[0]+files[j].getName()));
					                //设置压缩文件内的字符编码，不然会变成乱码   
					               //out.setEncoding("GBK");  
					               int len;   
					               // 读入需要下载的文件的内容，打包到zip文件   
					               while ((len = fis.read(buffer)) > 0) {   
					                    out.write(buffer, 0, len);   
					                }   
					               out.closeEntry();   
					               fis.close();   
				            	} 
	        		        }
	        		      }
			            } 
	        	  files=null;
	            }    
	          //  this.downFile(getResponse(), tmpFileName, FilePath);   
	       } catch (Exception e) {   
	          //Log.error("文件下载出错", e);   
	    	   System.out.println(e.getMessage());
	       } 
	      out.close();   
	      this.downFile(getResponse(), tmpFileName, FilePath); 
	      return null;   
	    }  
      
      public String execute22(String FilePath,File[] file1,List<String> listGroupId) throws IOException {   
	       //生成的ZIP文件名为Demo.zip   
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		    String date=sdf.format(new Date());
		    int fileNum=0;
		    int c=0;
		    File[] files=new File[500];
		    boolean isFirst=true;
	        String tmpFileName = "归档文件包-"+date+".zip";   
	        byte[] buffer = new byte[1024];   
	        String strZipPath = FilePath + tmpFileName; 
	        ZipOutputStream out =null;
	        List<Map<String,Object>> map=null;
	        String downLoadpath="";
	        Set<String> pathSet=new HashSet<String>();
	        List<Map<String,Object>> tScpmmPmArchiveMainJgMap=null;
	       // File[] files=new File[300];
	        try {
	        	for(int a=0;a<listGroupId.size();a++){//循环每一条子节点获取他的文件路径
		            String path="";
					map=archiveDelService.getTScpmmPmArchiveDelPidByGid(listGroupId.get(a));
					String pid=(String)map.get(0).get("PARENT_ID");
					tScpmmPmArchiveMainJgMap=archiveDelService.getTScpmmPmArchiveMainJgById(pid);
					for(int i=tScpmmPmArchiveMainJgMap.size()-1;i>=0;i--){
						path+=tScpmmPmArchiveMainJgMap.get(i).get("NODE_NAME")+File.separator;
					}
					pathSet.add(path+"&"+pid);//将获取到的所以路径装入pathSet
	         }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        List<String> pathList=new ArrayList<String>();//不同的目录结构
	        Iterator<String> it = pathSet.iterator();
	        while(it.hasNext()){
	        	pathList.add(it.next());
	        }
	       try {   
	            out = new ZipOutputStream(new FileOutputStream(   
	                   strZipPath));
	            downLoadpath = function.getFileSavePathRoot();
	           // downLoadpath=CommonTools.readValue("downLoadFilePath");
	          f:for(int m=0;m<pathList.size();m++){
	        	  //File[] files=new File[500];
	        	  String path=pathList.get(m);
	        	  String pathArray[]= path.split("&");
	        	  String projectId=(String)this.getHttpRequest().getSession().getAttribute("treeProjectId");
	        	  List<Map<String,Object>> pmArchiveDelMapList= archiveDelService.getTScpmmPmArchiveDelByPid(pathArray[1],projectId); 
	        	  for(int k=0;k<pmArchiveDelMapList.size();k++){
		        		 String gid=(String) pmArchiveDelMapList.get(k).get("GROUPID");
		        		 List<Map<String,Object>>ctlFiles=archiveDelService.getTCtlFilesByGroupId(gid);	 
		        		 TCtlFiles tFile=null;
		        		 for(int q=0;q<ctlFiles.size();q++){
		        			 tFile= fileHelp.getFileByFileId((String)ctlFiles.get(q).get("ID"));
		        			 files[fileNum]=new File(downLoadpath+tFile.getFileName());
		        			 fileNum++;
		        		 } 		   
		        	  }	
	        	 n:for (int n = 0; n < files.length+1; n++) {
	        		      if(isFirst){ 
			            	File filea=null;
			            	if(files[n]==null){
			            		isFirst=false;
			            	}
			            	if(files[n]!=null){
			            		 filea =new File(FilePath+files[n].getName());
			            	}else{
			            		continue f;
			            	}
			                if(filea.exists()){       
				                FileInputStream fis = new FileInputStream(files[n]);  
				                out.putNextEntry(new ZipEntry(pathArray[0]+files[n].getName()));
				                //设置压缩文件内的字符编码，不然会变成乱码   
				               //out.setEncoding("GBK");  
				               int len;   
				               // 读入需要下载的文件的内容，打包到zip文件   
				               while ((len = fis.read(buffer)) > 0) {   
				                    out.write(buffer, 0, len);   
				                }   
				               out.closeEntry();   
				               fis.close();   
			            	}
	        		      } else{//区分第一次
	        		    	  File filea=null;
				            	if(files[n]==null){
				            		//continue n;
				            		continue f;
				            	}else{
				            		filea =new File(FilePath+files[n].getName());
				            	}
				                if(filea.exists()){       
					                FileInputStream fis = new FileInputStream(files[n]);  
					                out.putNextEntry(new ZipEntry(pathArray[0]+files[n].getName()));
					                //设置压缩文件内的字符编码，不然会变成乱码   
					               //out.setEncoding("GBK");  
					               int len;   
					               // 读入需要下载的文件的内容，打包到zip文件   
					               while ((len = fis.read(buffer)) > 0) {   
					                    out.write(buffer, 0, len);   
					                }   
					               out.closeEntry();   
					               fis.close();   
				            	} 
	        		        }
			            } 
	        	  files=null;
	            }    
	          //  this.downFile(getResponse(), tmpFileName, FilePath);   
	       } catch (Exception e) {   
	          //Log.error("文件下载出错", e);   
	    	   System.out.println(e.getMessage());
	       } 
	      out.close();   
	      this.downFile(getResponse(), tmpFileName, FilePath); 
	      return null;   
	    }  
      
      
      public String execute(String FilePath,File[] file1,List<String> listGroupId) throws IOException {   
	       //生成的ZIP文件名为Demo.zip   
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		    String date=sdf.format(new Date());
	        String tmpFileName = "归档文件包-"+date+".zip";   
	        byte[] buffer = new byte[1024];   
	        String strZipPath = FilePath + tmpFileName; 
	        ZipOutputStream out =null;
	        List<Map<String,Object>> map=null;
	        String downLoadpath="";
	        Set<String> pathSet=new HashSet<String>();
	        List<Map<String,Object>> tScpmmPmArchiveMainJgMap=null;
	       // File[] files=new File[300];
	        try {
	        	for(int a=0;a<listGroupId.size();a++){//循环每一条子节点获取他的文件路径
		            String path="";
					map=archiveDelService.getTScpmmPmArchiveDelPidByGid(listGroupId.get(a));
					String pid=(String)map.get(0).get("PARENT_ID");
					tScpmmPmArchiveMainJgMap=archiveDelService.getTScpmmPmArchiveMainJgById(pid);
					for(int i=tScpmmPmArchiveMainJgMap.size()-1;i>=0;i--){
						path+=tScpmmPmArchiveMainJgMap.get(i).get("NODE_NAME")+File.separator;
					}
					pathSet.add(path+"&"+pid);//将获取到的所以路径装入pathSet
	         }
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	        List<String> pathList=new ArrayList<String>();//不同的目录结构
	        Iterator<String> it = pathSet.iterator();
	        while(it.hasNext()){
	        	pathList.add(it.next());
	        }
	       try {       	   
	            out = new ZipOutputStream(new FileOutputStream(   
	                   strZipPath));
	            downLoadpath=CommonTools.readValue("downLoadFilePath");
	          f:for(int m=0;m<pathList.size();m++){
	        	  String path=pathList.get(m);
	        	  String pathArray[]= path.split("&");
	        	  String projectId=(String)this.getHttpRequest().getSession().getAttribute("treeProjectId");
	        	  List<Map<String,Object>> pmArchiveDelMapList= archiveDelService.getTScpmmPmArchiveDelByPid(pathArray[1],projectId);
	        	  for(int k=0;k<pmArchiveDelMapList.size();k++){
	        		 String gid=(String) pmArchiveDelMapList.get(k).get("GROUPID");
	        		 List<Map<String,Object>>ctlFiles=archiveDelService.getTCtlFilesByGroupId(gid);	        		 
	        		 TCtlFiles tFile = fileHelp.getFileByFileId((String)ctlFiles.get(0).get("ID"));
	        	  }	        	  
	        	  for (int n = 0; n < file1.length-1; n++) {
			            	File filea=null;
			            	if(file1[n]!=null){
			            		 filea =new File(FilePath+file1[n].getName());
			            	}else{
			            		continue f;
			            	}
			                if(filea.exists()){       
				                FileInputStream fis = new FileInputStream(file1[n]);  // 在这里如果对应的盘符里面找不到文件会终止追加文件需要解决
				                out.putNextEntry(new ZipEntry(pathArray[0]+file1[n].getName()));
				                //设置压缩文件内的字符编码，不然会变成乱码   
				               //out.setEncoding("GBK");  
				               int len;   
				               // 读入需要下载的文件的内容，打包到zip文件   
				               while ((len = fis.read(buffer)) > 0) {   
				                    out.write(buffer, 0, len);   
				                }   
				               out.closeEntry();   
				               fis.close();   
			            	}
			            } 
	            }     
	       } catch (Exception e) {     
	       } 
	      out.close();   
	      this.downFile(getResponse(), tmpFileName, FilePath);   
	      return null;   
	    }  
	
	
	public String execute2(String FilePath,File[] file1,List<String> listGroupId) throws IOException {   
	       //生成的ZIP文件名为Demo.zip   
		    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		    String date=sdf.format(new Date());
	        String tmpFileName = "归档文件包-"+date+".zip";   
	        byte[] buffer = new byte[1024];   
	        String strZipPath = FilePath + tmpFileName; 
	        ZipOutputStream out =null;
	        List<Map<String,Object>> map=null;
	        String path="";
	        List<Map<String,Object>> tScpmmPmArchiveMainJgMap=null;
	        try {
				map=archiveDelService.getTScpmmPmArchiveDelPidByGid(listGroupId.get(0));
				String pid=(String)map.get(0).get("PARENT_ID");
				tScpmmPmArchiveMainJgMap=archiveDelService.getTScpmmPmArchiveMainJgById(pid);
				for(int i=tScpmmPmArchiveMainJgMap.size()-1;i>=0;i--){
					path+=tScpmmPmArchiveMainJgMap.get(i).get("NODE_NAME")+File.separator;
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	       try {   
	            out = new ZipOutputStream(new FileOutputStream(   
	                   strZipPath));   
	            for (int i = 0; i < file1.length; i++) { 
	            	File filea =new File(FilePath+file1[i].getName());
	                if(filea.exists()){       
	                FileInputStream fis = new FileInputStream(file1[i]);  // 在这里如果对应的盘符里面找不到文件会终止追加文件需要解决
	                out.putNextEntry(new ZipEntry(path+file1[i].getName()));   
	                //设置压缩文件内的字符编码，不然会变成乱码   
	               //out.setEncoding("GBK");  
	               int len;   
	               // 读入需要下载的文件的内容，打包到zip文件   
	               while ((len = fis.read(buffer)) > 0) {   
	                    out.write(buffer, 0, len);   
	                }   
	                out.closeEntry();   
	               fis.close();   
	            	}
	            }    
	            out.close();   
	            this.downFile(getResponse(), tmpFileName, FilePath);   
	       } catch (Exception e) {   
	         //  Log.error("文件下载出错", e);   
	        } 
	      out.close();   
	      this.downFile(getResponse(), tmpFileName, FilePath);   
	      return null;   
	    }   
	 

	    private HttpServletResponse getResponse() {   
	       return ServletActionContext.getResponse();   
	    }   
	  
	   /**  
	    * 文件下载  
	    * @param response  
	     * @param str  
	     */  
	   private void downFile(HttpServletResponse response, String str, String FilePath) {   
	       try {   
	          String path = FilePath + str;   
	           File file = new File(path);   
	           if (file.exists()) {   
	                InputStream ins = new FileInputStream(path);   
	               BufferedInputStream bins = new BufferedInputStream(ins);// 放到缓冲流里面   
	               OutputStream outs = response.getOutputStream();// 获取文件输出IO流   
	                BufferedOutputStream bouts = new BufferedOutputStream(outs);   
	                response.setContentType("application/x-download");// 设置response内容的类型   
	               response.setHeader(   
	                       "Content-disposition",   
	                        "attachment;filename="  
	                               + URLEncoder.encode(str, "UTF-8"));// 设置头部信息   
	                int bytesRead = 0;   
	               byte[] buffer = new byte[8192];   
	               // 开始向网络传输文件流   
	               while ((bytesRead = bins.read(buffer, 0, 8192)) != -1) {   
	                   bouts.write(buffer, 0, bytesRead);   
	               }   
	                bouts.flush();// 这里一定要调用flush()方法   
	                ins.close();   
	                bins.close();   
	               outs.close();   
	                bouts.close();   
	           } else {   
	               response.sendRedirect("../error.jsp");   
	            }   
	       } catch (IOException e) {   
	           // Log.error("文件下载出错", e);   
	       }   
	    }   
	   
	   /**
		 * 读取生成的质检报告
		 * @param filename
		 * @return
		 * @throws IOException
		 */
		public static byte[] readFile(String filename) throws IOException {

			File file =new File(filename);
			if(filename==null || filename.equals("")){
				throw new NullPointerException("无效的文件路径");
			}
			int n = 0;
			long len = file.length();
			byte[] bytes = new byte[(int)len];
			byte[] temp = new byte[(int)len]; 
			ByteArrayOutputStream bos = new ByteArrayOutputStream((int)len);
			BufferedInputStream bufferedInputStream=new BufferedInputStream(new FileInputStream(file));
			 
			while((n = bufferedInputStream.read(temp)) != -1){
				bos.write(temp,0,n);
			}
			bytes = temp;
			/*int r = bufferedInputStream.read( bytes );
			if (r != len)
				throw new IOException("读取文件不正确");*/
			bufferedInputStream.close();

			return bytes;

		}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileOnlyName() {
		return fileOnlyName;
	}

	public void setFileOnlyName(String fileOnlyName) {
		this.fileOnlyName = fileOnlyName;
	}
	

}
