<%@ page contentType="text/html; charset=utf-8"%>

<div id="t35" class="ui-tabs-panel ui-widget-content ui-corner-bottom" style="text-align: center;">
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%;">
<div id="lui_grid_35" class="ui-widget-overlay jqgrid-overlay"></div>
<div id="load_grid_35" class="loading ui-state-default ui-state-active" style="display: none;">数据加载中...</div>
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%;">
  <div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <a href="javascript:void(0)" role="link" class="ui-jqgrid-titlebar-close HeaderButton" style="right: 0px;">
      <span class="ui-icon ui-icon-circle-triangle-n"></span>
    </a>
    <span class="ui-jqgrid-title">书籍列表：</span>
  </div>
  <div style="width: 100%;" class="ui-state-default ui-jqgrid-hdiv">
    <div class="ui-jqgrid-hbox">
      <table cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_grid_35" role="grid" style="width: 100%;" class="ui-jqgrid-htable">
          <thead>
              <tr role="rowheader" class="ui-jqgrid-labels">
                <th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 25px;">
                  <div id="jqgh_rn">
		    <span style="display: none;" class="s-ico">
                       <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
                       <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
		    </span>
		  </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 20px;">
		  <div id="jqgh_subgrid">
		    <span style="display: none;" class="s-ico">
			<span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			<span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
		    </span>
		  </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 150px; display: none;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_contentid" class="ui-jqgrid-sortable">
			contentid
			<span style="" class="s-ico">
			   <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			   <span class="ui-grid-ico-sort ui-icon-desc ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 150px; display: none;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_catid" class="ui-jqgrid-sortable">
			catid
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 502px;">
		    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_title" class="ui-jqgrid-sortable">
			书籍名称
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
		  	</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 71px;">
		    <div id="jqgh_url" class="ui-jqgrid-sortable">
			详细信息
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 71px;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_filesize" class="ui-jqgrid-sortable">
			文件大小
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 71px;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_classtype" class="ui-jqgrid-sortable">
			文件类型
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 150px; display: none;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_downurl" class="ui-jqgrid-sortable">
			downurl
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 71px;">
		  <div id="jqgh_language" class="ui-jqgrid-sortable">
			语言
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		  </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 71px;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_stars" class="ui-jqgrid-sortable">
			推荐指数
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 71px;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_onlineview_times" class="ui-jqgrid-sortable">
			阅读次数
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 71px;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_download_times" class="ui-jqgrid-sortable">
			下载次数
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		    </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr" role="columnheader" style="width: 77px;">
		  <div id="jqgh_onlineviewdocid" class="ui-jqgrid-sortable">
			在线阅读
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
			</span>
		  </div>
		</th>

		<th class="ui-state-default ui-th-column ui-th-ltr " role="columnheader" style="width: 150px; display: none;">
		  <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
		    <div id="jqgh_thumb" class="ui-jqgrid-sortable">
			thumb
			<span style="display: none;" class="s-ico">
			  <span class="ui-grid-ico-sort ui-icon-asc ui-state-disabled ui-icon ui-icon-triangle-1-n ui-sort-ltr" sort="asc"></span>
			  <span class="ui-grid-ico-sort ui-icon-desc ui-state-disabled ui-icon ui-icon-triangle-1-s ui-sort-ltr" sort="desc"></span>
		  	</span>
		   </div>
		</th>
	     </tr>
	</thead>
     </table>
  </div>
</div>

<div class="ui-jqgrid-bdiv" style="height: auto; width: 100%;">
	<div style="position: relative;">
		<div></div>
			<table cellspacing="0" cellpadding="0" border="0" id="grid_35" class="ui-jqgrid-btable" role="grid" aria-multiselectable="false" aria-labelledby="gbox_grid_35" style="width: 100%;">
				<tbody>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="360">
						<td style="text-align: center; width: 25px;" class="ui-state-default jqgrid-rownum" role="gridcell">1</td>
						<td style="width: 20px;" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="360" style="display: none; width: 150px;" role="gridcell">360</td><td title="59" style="display: none; width: 150px;" role="gridcell">59</td>
						<td title="基于J2EE 的通用Web 信息系统框架设计与实现" style="width: 502px;" role="gridcell">基于J2EE 的通用Web 信息系统框架设计与实现</td>
						<td title="点击查看" style="text-align: center; width: 71px;" role="gridcell"><a target="_blank" href="../2009/1215/360.html">点击查看</a></td>
						<td title="245KB" style="text-align: center; width: 71px;" role="gridcell">245KB</td>
						<td title="" style="text-align: center; width: 71px;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="" style="display: none; width: 150px;" role="gridcell">&nbsp;</td>
						<td title="简体中文" style="text-align: center; width: 71px;" role="gridcell">简体中文</td>
						<td title="★★★☆☆" style="text-align: center; width: 71px;" role="gridcell"><span style="color: green;">★★★☆☆</span></td>
						<td title="47" style="text-align: center; width: 71px;" role="gridcell">47</td>
						<td title="0" style="text-align: center; width: 71px;" role="gridcell">0</td>
						<td title="" style="text-align: center; width: 77px;" role="gridcell"><a title="电子书籍在线阅读" href="../FlexPaper/FlexPaperViewer.php?docid=j2eeweb-091214103915-phpapp02&amp;width=716&amp;height=613" data="360" class="onlineview iframe" id="onlineview_360"><img border="0" alt="" src="images/onlineview.gif"></a></td>
						<td title="" style="display: none; width: 150px;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="355">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">2</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="355" style="display: none;" role="gridcell">355</td><td title="59" style="display: none;" role="gridcell">59</td>
						<td title="飞机订票系统详细规格说明书" style="" role="gridcell">飞机订票系统详细规格说明书</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1213/355.html">点击查看</a></td>
						<td title="未知" style="text-align: center;" role="gridcell">未知</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★☆☆" style="text-align: center;" role="gridcell"><span style="color: green;">★★★☆☆</span></td>
						<td title="15" style="text-align: center;" role="gridcell">15</td>
						<td title="0" style="text-align: center;" role="gridcell">0</td>
						<td title="" style="text-align: center;" role="gridcell"><a title="电子书籍在线阅读" href="../FlexPaper/FlexPaperViewer.php?docid=random-091212014626-phpapp01&amp;width=716&amp;height=613" data="355" class="onlineview iframe" id="onlineview_355"><img border="0" alt="" src="images/onlineview.gif"></a></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="354">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">3</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="354" style="display: none;" role="gridcell">354</td>
						<td title="59" style="display: none;" role="gridcell">59</td>
						<td title="联通省级客户管理（大客户部分）系统需求说明书" style="" role="gridcell">联通省级客户管理（大客户部分）系统需求说明书</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1213/354.html">点击查看</a></td>
						<td title="未知" style="text-align: center;" role="gridcell">未知</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★☆☆" style="text-align: center;" role="gridcell"><span style="color: green;">★★★☆☆</span></td>
						<td title="24" style="text-align: center;" role="gridcell">24</td>
						<td title="0" style="text-align: center;" role="gridcell">0</td>
						<td title="" style="text-align: center;" role="gridcell"><a title="电子书籍在线阅读" href="../FlexPaper/FlexPaperViewer.php?docid=random-091212014416-phpapp01&amp;width=716&amp;height=613" data="354" class="onlineview iframe" id="onlineview_354"><img border="0" alt="" src="images/onlineview.gif"></a></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="344">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">4</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="344" style="display: none;" role="gridcell">344</td>
						<td title="60" style="display: none;" role="gridcell">60</td>
						<td title="中国电信计费模型_数据模型.doc" style="" role="gridcell">中国电信计费模型_数据模型.doc</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1212/344.html">点击查看</a></td>
						<td title="未知" style="text-align: center;" role="gridcell">未知</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="0" style="text-align: center;" role="gridcell">0</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="303">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">5</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="303" style="display: none;" role="gridcell">303</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="设计模式.chm" style="" role="gridcell">设计模式.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/303.html">点击查看</a></td>
						<td title="140KB" style="text-align: center;" role="gridcell">140KB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="CHM" src="images/CHM.gif"></td>
						<td title="api_303.rar" style="display: none;" role="gridcell">api_303.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="51" style="text-align: center;" role="gridcell">51</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="302">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">6</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="302" style="display: none;" role="gridcell">302</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="XmlHTTP对象参考" style="" role="gridcell">XmlHTTP对象参考</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/302.html">点击查看</a></td>
						<td title="25KB" style="text-align: center;" role="gridcell">25KB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="CHM" src="images/CHM.gif"></td>
						<td title="api_302.rar" style="display: none;" role="gridcell">api_302.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★☆" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★☆</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="12" style="text-align: center;" role="gridcell">12</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="301">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">7</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="301" style="display: none;" role="gridcell">301</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="SQL参考手册.chm" style="" role="gridcell">SQL参考手册.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/301.html">点击查看</a></td>
						<td title="216KB" style="text-align: center;" role="gridcell">216KB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="CHM" src="images/CHM.gif"></td>
						<td title="api_301.rar" style="display: none;" role="gridcell">api_301.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="44" style="text-align: center;" role="gridcell">44</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="300">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">8</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="300" style="display: none;" role="gridcell">300</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="SQL Server精华 (CHM).chm" style="" role="gridcell">SQL Server精华 (CHM).chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/300.html">点击查看</a></td>
						<td title="4.65MB" style="text-align: center;" role="gridcell">4.65MB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="CHM" src="images/CHM.gif"></td>
						<td title="api_300.rar" style="display: none;" role="gridcell">api_300.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="81" style="text-align: center;" role="gridcell">81</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="299">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">9</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="299" style="display: none;" role="gridcell">299</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="Script全集.chm" style="" role="gridcell">Script全集.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/299.html">点击查看</a></td>
						<td title="1.41MB" style="text-align: center;" role="gridcell">1.41MB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="api_299.rar" style="display: none;" role="gridcell">api_299.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="32" style="text-align: center;" role="gridcell">32</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="298">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">10</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="298" style="display: none;" role="gridcell">298</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="Oracle性能优化.chm" style="" role="gridcell">Oracle性能优化.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/298.html">点击查看</a></td>
						<td title="107KB" style="text-align: center;" role="gridcell">107KB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="api_298.rar" style="display: none;" role="gridcell">api_298.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★☆" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★☆</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="13" style="text-align: center;" role="gridcell">13</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="297">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">11</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="297" style="display: none;" role="gridcell">297</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="Oracle错误码大全.chm" style="" role="gridcell">Oracle错误码大全.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/297.html">点击查看</a></td>
						<td title="1.8MB" style="text-align: center;" role="gridcell">1.8MB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="api_297.rar" style="display: none;" role="gridcell">api_297.rar</td>
						<td title="英文" style="text-align: center;" role="gridcell">英文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="30" style="text-align: center;" role="gridcell">30</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="296">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">12</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="296" style="display: none;" role="gridcell">296</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="Oracal9iSQL参考手册.chm" style="" role="gridcell">Oracal9iSQL参考手册.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/296.html">点击查看</a></td>
						<td title="7.90MB" style="text-align: center;" role="gridcell">7.90MB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="PDF" src="images/PDF.gif"></td>
						<td title="api_296.rar" style="display: none;" role="gridcell">api_296.rar</td>
						<td title="英文" style="text-align: center;" role="gridcell">英文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="7" style="text-align: center;" role="gridcell">7</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="295">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">13</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="295" style="display: none;" role="gridcell">295</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="MySQL中文参考手册.chm" style="" role="gridcell">MySQL中文参考手册.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/295.html">点击查看</a></td>
						<td title="443KB" style="text-align: center;" role="gridcell">443KB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="CHM" src="images/CHM.gif"></td>
						<td title="api_295.rar" style="display: none;" role="gridcell">api_295.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="49" style="text-align: center;" role="gridcell">49</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="294">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">14</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="294" style="display: none;" role="gridcell">294</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="Js.chm" style="" role="gridcell">Js.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/294.html">点击查看</a></td>
						<td title="880KB" style="text-align: center;" role="gridcell">880KB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="CHM" src="images/CHM.gif"></td>
						<td title="api_294.rar" style="display: none;" role="gridcell">api_294.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="255" style="text-align: center;" role="gridcell">255</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
					<tr class="ui-widget-content jqgrow ui-row-ltr" role="row" id="293">
						<td style="text-align: center;" class="ui-state-default jqgrid-rownum" role="gridcell">15</td>
						<td style="" class="ui-sgcollapsed sgcollapsed" role="grid"><a href="javascript:void(0);"><span class="ui-icon ui-icon-plus"></span></a></td>
						<td title="293" style="display: none;" role="gridcell">293</td>
						<td title="61" style="display: none;" role="gridcell">61</td>
						<td title="jdk142.chm" style="" role="gridcell">jdk142.chm</td>
						<td title="点击查看" style="text-align: center;" role="gridcell"><a target="_blank" href="../2009/1205/293.html">点击查看</a></td>
						<td title="35.9MB" style="text-align: center;" role="gridcell">35.9MB</td>
						<td title="" style="text-align: center;" role="gridcell"><img border="0" title="CHM" src="images/CHM.gif"></td>
						<td title="api_293.rar" style="display: none;" role="gridcell">api_293.rar</td>
						<td title="简体中文" style="text-align: center;" role="gridcell">简体中文</td>
						<td title="★★★★★" style="text-align: center;" role="gridcell"><span style="color: green;">★★★★★</span></td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="1" style="text-align: center;" role="gridcell">1</td>
						<td title="暂未提供" style="text-align: center;" role="gridcell"><span style="color: rgb(204, 204, 204);">暂未提供</span></td>
						<td title="" style="display: none;" role="gridcell">&nbsp;</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
<div id="rs_mgrid_35" class="ui-jqgrid-resize-mark">&nbsp;</div>
<div id="pager_35" style="width: 100%;" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
	<div role="group" class="ui-pager-control" id="pg_pager_35">
		<table cellspacing="0" cellpadding="0" border="0" role="row" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
			<tbody>
				<tr>
					<td align="left" id="pager_35_left">
						<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
							<tbody>
								<tr>
									<td class="ui-pg-button ui-corner-all" title="刷新表格" id="refresh_grid_35">
										<div class="ui-pg-div">
											<span class="ui-icon ui-icon-refresh"></span>
										</div>
									</td>
									<td class="ui-pg-button ui-corner-all" title="在线阅读所选书籍" style="cursor: pointer;">
										<div class="ui-pg-div">
											<span class="ui-icon ui-icon-bookmark"></span>
											<span style="color: red;">阅读</span>
										</div>
									</td>
									<td class="ui-pg-button ui-corner-all" title="下载所选书籍" style="cursor: pointer;">
										<div class="ui-pg-div">
											<span class="ui-icon ui-icon-disk"></span>下载
										</div>
									</td>
									<td class="ui-pg-button ui-corner-all" title="收藏所选书籍" style="cursor: pointer;">
										<div class="ui-pg-div">
											<span class="ui-icon ui-icon-cart"></span>收藏
										</div>
									</td>
									<td class="ui-pg-button ui-corner-all" title="提交您需要的书籍，让他人帮助您" style="cursor: pointer;">
										<div class="ui-pg-div">
											<span class="ui-icon ui-icon-plusthick"></span>提交需求
										</div>
									</td>
								</tr>
							</tbody>
						</table>
					</td>
					<td align="center" style="white-space: pre; width: 272px;" id="pager_35_center"><table cellspacing="0" cellpadding="0" border="0" class="ui-pg-table" style="table-layout: auto;">
						<tbody>
							<tr>
								<td class="ui-pg-button ui-corner-all ui-state-disabled" id="first"><span class="ui-icon ui-icon-seek-first"></span></td>
								<td class="ui-pg-button ui-corner-all ui-state-disabled" id="prev"><span class="ui-icon ui-icon-seek-prev"></span></td>
								<td style="width: 4px;" class="ui-pg-button ui-state-disabled"><span class="ui-separator"></span></td>
								<td dir="ltr"> 第 <input type="text" role="textbox" value="0" maxlength="7" size="2" class="ui-pg-input"/> 页 共 <span id="sp_1">3</span> 页</td>
								<td style="width: 4px;" class="ui-pg-button ui-state-disabled"><span class="ui-separator"></span></td>
								<td class="ui-pg-button ui-corner-all" id="next"><span class="ui-icon ui-icon-seek-next"></span></td>
								<td class="ui-pg-button ui-corner-all" id="last"><span class="ui-icon ui-icon-seek-end"></span></td>
								<td dir="ltr"><select role="listbox" class="ui-pg-selbox"><option selected="" value="15" role="option">15</option><option value="25" role="option">25</option><option value="35" role="option">35</option></select></td>
								</tr>
							</tbody>
						</table></td>
					<td align="right" id="pager_35_right">
						<div class="ui-paging-info" style="text-align: right;" dir="ltr">第 1 至 15 条　共 35 条</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
</div>
</div>
