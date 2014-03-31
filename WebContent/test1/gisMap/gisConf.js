//初始化部队类别图标，根据部队类别内码对应部队类别图标
var bdlbImgs={
  "defaultimg":"其它.png",
  "imgMap":[
    {"bdlbnm":"0201","bdlbimg":"综合库.png"},
    {"bdlbnm":"0202","bdlbimg":"军械库.png"},
    {"bdlbnm":"0203","bdlbimg":"弹药库.png"},
    {"bdlbnm":"0204","bdlbimg":"油料库.png"},
    {"bdlbnm":"0205","bdlbimg":"军需库.png"},
    {"bdlbnm":"0206","bdlbimg":"药材库.png"},
    {"bdlbnm":"0207","bdlbimg":"器材库.png"},
    {"bdlbnm":"0208","bdlbimg":"器材库.png"},
    {"bdlbnm":"0209","bdlbimg":"器材库.png"},
    {"bdlbnm":"0210","bdlbimg":"汽车库.png"},
    {"bdlbnm":"0301","bdlbimg":"医院.png"},
    {"bdlbnm":"0302","bdlbimg":"医院.png"},
    {"bdlbnm":"0303","bdlbimg":"防疫.png"},
    {"bdlbnm":"06","bdlbimg":"汽车部队.png"}
  ],
  "imgLbMap": {
    "0201":"综合库.png",
    "0202":"军械库.png",
    "0203":"弹药库.png",
    "0204":"油料库.png",
    "0205":"军需库.png",
    "0206":"药材库.png",
    "0207":"器材库.png",
    "0208":"器材库.png",
    "0209":"器材库.png",
    "0210":"汽车库.png",
    "0301":"医院.png",
    "0302":"医院.png",
    "0303":"防疫.png",
    "06":"汽车部队.png"
  }
};

//var t="0201";
//alert("ABC::"+bdlbImgs.imgLbMap[t]);

var layerArrays={
    yy:{
        mc: "社采图层数据",
        value:{
		    0:{
		        ename:"TEST_SUBWAY13_PT",
		        cname:"地铁13号线站点",
		        zdyw:"name",
		        zdzw:"站点名称",	
		        imgUrl:"../images/layerImg/purple-dot.gif",			        
		        showLevel: "11"        
		    },
		    1:{
		        ename:"TEST_YL_PT",
		        cname:"社会医疗机构",
		        zdyw:"name",
		        zdzw:"部队名称",		
		        imgUrl:"../images/layerImg/hospitals.gif",		        
		        showLevel: "15"         
		    }
		}   
	},      
    ck:{
        mc: "专业工程数据",
        value:{
        	0:{
		        ename:"ZCHJ_ZBGC_PT",
		        cname:"战备工程",
		        zdyw:"zbgcmc",
		        zdzw:"战备工程名称",	
		        imgUrl:"../images/layerImg/hq.gif",		        
		        showLevel: "9"        
		    }, 
        	1:{
		        ename:"SUB_YLJGJBQK_PT",
		        cname:"医疗机构基本情况",
		        zdyw:"ssmc",
		        zdzw:"部队名称",		
		        imgUrl:"../images/layerImg/green.gif",	        
		        showLevel: "7"        
		    },
		    2:{
		        ename:"SUB_CKJBQK_PT",
		        cname:"仓库",
		        zdyw:"bzssmc",
		        zdzw:"部队名称",	
		        imgUrl:"../images/layerImg/office-building.gif",			        	        
		        showLevel: "7"      
		    }
		}       
    }     
}