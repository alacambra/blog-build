<#include "header.ftl">
	
	<#include "menu.ftl">

	<div class="page-header">
		<h1>Blog</h1>
	</div>
	<#list posts as post>
  		<#if (post.status == "published")>
		  <p th:fragment="firstPara(of)"
                           th:with="
                           pstart=${#strings.indexOf(of, '<p')}  ,
                           pend=${#strings.indexOf(of,'</p')} ,
                           summary=${ pend lt 0 ? 'No summary available' : #strings.substring(of,pstart,pend) + '</p>'}"
                           th:utext='${summary}' th:remove="tag">The first &lt;p&gt; element of <code>of</code> will appear here
                        </p>
  			<a href="${post.uri}"><h1><#escape x as x?xml>${post.title}</#escape></h1></a>
  			<p>${post.date?string("dd MMMM yyyy")}</p>
  			<p>${post.body}</p>
  		</#if>
  	</#list>
	
	<hr />
	
	<p>Older posts are available in the <a href="${content.rootpath}${config.archive_file}">archive</a>.</p>

<#include "footer.ftl">