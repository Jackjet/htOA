package com.kwchina.oa.bbs.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.file.UploadifyViewUtil;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.bbs.entity.CommentInfor;
import com.kwchina.oa.bbs.entity.ThesisInfor;
import com.kwchina.oa.bbs.service.CommentInforManager;
import com.kwchina.oa.bbs.service.ThesisInforManager;
import com.kwchina.oa.bbs.vo.CommentInforVo;
import com.kwchina.oa.sys.SystemConstant;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/bbs/commentInfor.do")
public class CommentInforController extends BasicController{

	@Resource
	private CommentInforManager commentInforManager;
	
	@Resource
	private ThesisInforManager thesisInforManager;
	
	/**
	 * 查看帖子 
	 * @param commentInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("commentInforVo")
			CommentInforVo commentInforVo, Model model,HttpServletRequest request, HttpServletResponse response) throws Exception{

		CommentInforVo vo = (CommentInforVo)commentInforVo;
        //清空提交的数据，使页面返回所有评论
		vo.setNickName("");
		vo.setReplyContent("");
		vo.setReplyDateAsc(1);

        //分页显示信息
        Pages pages = new Pages(request);
        
        int inPages = vo.getInpages();
        if(inPages == 0){
        	inPages = 1;
        }
        
        pages.setPage(inPages);
        pages.setPerPageNum(10);

        /*String fileName = "commentInfor" + SystemConstant.FILEPREFIX + "?method=edit&thesisId="+bean.getThesisId()+"&page=" + bean.getPage();
        pages.setFileName(fileName);*/
        pages.setStyle(3);

        String queryHql = this.commentInforManager.createHql(vo, pages, "edit", 0);
        String countHql = this.commentInforManager.createHql(vo, pages, null,1);
        PageList pl = this.commentInforManager.getResultByQueryString(queryHql, countHql, true, pages);
        List comments = pl.getObjectList();
        ThesisInfor thesisInfor = (ThesisInfor) thesisInforManager.get(vo.getThesisId());
		//论题的图片附件
		String imgAttachmentFile = thesisInfor.getImgAttachment();
		if (imgAttachmentFile != null && (!imgAttachmentFile.equals(""))) {
			String[][] attachment = UploadifyViewUtil.viewAttachment(imgAttachmentFile);

			request.setAttribute("_ImgAttachment_Names", attachment[1]);
			request.setAttribute("_ImgAttachments", attachment[0]);
			request.setAttribute("_ImgAttachment_Sizes", attachment[2]);
		}
		//附件
		String attachmentFile = thesisInfor.getAttachment();
		if (attachmentFile != null && (!attachmentFile.equals(""))) {
			String[][] attachment = UploadifyViewUtil.viewAttachment(attachmentFile);

			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
			request.setAttribute("_Attachment_Sizes", attachment[2]);
		}
		
        request.setAttribute("_Thesis", thesisInfor);
        request.setAttribute("_Pl", pl);
        request.setAttribute("_Page", pages);
        request.setAttribute("_Comments", comments);
        
        //评论的图片附件
        List commentImgs = displayCommentImg(comments);
//        request.setAttribute("_CommentImgs", commentImgs);
        
        thesisInfor.setViewsCount(thesisInfor.getViewsCount()+1);
        this.thesisInforManager.save(thesisInfor);

	     return "thesisDetail";
	}
	
	//显示评论的图片附件
    public List displayCommentImg(List comments) {
    	
    	List commentImgs = new ArrayList();
        for (Iterator it=comments.iterator();it.hasNext();) {
        	CommentInfor comment = (CommentInfor)it.next();
        	String imgFile = comment.getImgAttachment();
        	if (imgFile != null && (!imgFile.equals(""))) {
    			String[] arrayFiles = imgFile.split("\\|");
    			Arrays.sort(arrayFiles);
    			commentImgs.add(arrayFiles);
    		}
        }
        return commentImgs;
    }
    
    
    /**
     * 保存评论内容
     * @param commentInforVo
     * @param model
     * @param request
     * @param response
     * @param multipartRequest
     * @return
     * @throws Exception
     */
    @RequestMapping(params = "method=save")
    public String save(@ModelAttribute("commentInforVo")
			CommentInforVo commentInforVo, Model model,HttpServletRequest request, 
			HttpServletResponse response,DefaultMultipartHttpServletRequest multipartRequest) throws Exception{

    	CommentInforVo vo = (CommentInforVo) commentInforVo;
        CommentInfor comment = new CommentInfor();
        
        int commentId = vo.getCommentId();
        long nowTime = System.currentTimeMillis();
        
        //修改
        if (commentId != 0) {
            comment = (CommentInfor) this.commentInforManager.get(commentId);
        }

        comment.setReplyContent(vo.getReplyContent());
        ThesisInfor thesisInfor = (ThesisInfor) this.thesisInforManager.get(vo.getThesisId());

        comment.setThesisInfor(thesisInfor);
        if (commentId == 0) {
            SystemUserInfor sysUser = SysCommonMethod.getSystemUser(request);
            comment.setReplyMan(sysUser);
            long time = System.currentTimeMillis();
            Timestamp updateDate = new Timestamp(time);
            comment.setReplyDate(updateDate);
            thesisInfor.setUpdateDate(updateDate);
            //昵称(保存用户名)
            comment.setNickName(sysUser.getUserName());
        }

        /*if (!bean.getNickName().equals("")) {
            comment.setNickName(bean.getNickName());
        } else {
            comment.setNickName(comment.getSystemUserInforByReplyMan().getUserName());
        }*/
        
		//获取图片附件
		/*String folder = SystemConstant.Img_folder;
		String attachment = this.uploadAttachment(multipartRequest, folder);*/
        String uploadImgAttachment = request.getParameter("uploadImgAttachment");
		comment.setImgAttachment(uploadImgAttachment);
        
        this.thesisInforManager.save(thesisInfor);
        this.commentInforManager.save(comment);

        return "redirect:/bbs/commentInfor.do?method=edit&thesisId="+vo.getThesisId();
//        response.sendRedirect("/bbs/commentInfor.do?method=edit&thesisId="+vo.getThesisId());
//        request.getRequestDispatcher("/bbs/commentInfor.do?method=edit&thesisId="+vo.getThesisId()).forward(request, response);
	}
	
	

	@RequestMapping(params = "method=list")
	public void list(@ModelAttribute("commentInforVo")
			CommentInforVo commentInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		//from CommentInfor commentInfor where 1=1 order by 
		//commentInfor.topComment desc,commentInfor.essence desc,commentInfor.updateDate  desc,
		//commentInfor.commentinforsByCommentId.size desc
		queryString[0] = "from CommentInfor commentInfor where 1=1 ";
		
		queryString[1] = "select count(commentId) from CommentInfor  where 1=1 ";
		
//		condition += "order by commentInfor.topComment desc,commentInfor.essence desc," +
//				"commentInfor.updateDate  desc,commentInfor.commentinfor.size desc ";
		
		queryString = this.commentInforManager.generateQueryString(queryString, getSearchParams(request));
		queryString[0] += condition+" order by commentInfor.topComment desc,commentInfor.essence desc," +
		"commentInfor.updateDate  desc,commentInfor.commentinfor.size desc ";;
		queryString[1] += condition;
		
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.commentInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();


		// 把查询到的结果转化为VO
		List commentVos = new ArrayList();
		if (list.size() > 0) {

			for (Iterator it = list.iterator(); it.hasNext();) {
				CommentInfor comment = (CommentInfor) it.next();

				// 把查询到的结果转化为VO
				commentInforVo = this.commentInforManager.transPOToVO(comment);
				commentVos.add(commentInforVo);
			}
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		JSONConvert convert = new JSONConvert();
		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		// awareObject.add("sender.person");
		rows = convert.modelCollect2JSONArray(commentVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	@RequestMapping(params = "method=myList")
	public String myList(@ModelAttribute("commentInforVo")
			CommentInforVo commentInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception{
		CommentInforVo vo = (CommentInforVo)commentInforVo;
		//分页显示信息
		Pages pages = new Pages(request);
		
		int inPages = vo.getInpages();
        if(inPages == 0){
        	inPages = 1;
        }
		
		pages.setPage(inPages);
		pages.setPerPageNum(10);
        SystemUserInfor sysUser = SysCommonMethod.getSystemUser(request);
        vo.setAuthor(sysUser.getPersonId());

		/*String fileName = "commentInfor" + SystemConstant.FILEPREFIX + "?method=mylist&page=" + bean.getPage();
        pages.setFileName(fileName);*/
		pages.setStyle(3);

		String queryHql = this.commentInforManager.createHql(vo, pages, "mylist", 0);
		String countHql = this.commentInforManager.createHql(vo, pages, null, 1);
		PageList pl = this.commentInforManager.getResultByQueryString(queryHql, countHql, true, pages);
		ArrayList<CommentInfor> comments = (ArrayList<CommentInfor>) pl.getObjectList();
        /*List commentList=new ArrayList<HashMap>();
          for (CommentInfor c : comments) {
              HashMap a = new HashMap();
              a.put("nickName", c.getNickName());
              a.put("replyDate", c.getReplyDate());
              a.put("replyContent", c.getReplyContent());
              a.put("title", c.getThesisinforByThesisId().getTitle());
              a.put("thesisId", c.getThesisinforByThesisId().getId());
              commentList.add(a);
          }*/
		request.setAttribute("_Pl", pl);
        request.setAttribute("_Comments", comments);

        //评论的图片附件
        List commentImgs = displayCommentImg(comments);
        request.setAttribute("_CommentImgs", commentImgs);

		return "listMyComment";
	}

}
