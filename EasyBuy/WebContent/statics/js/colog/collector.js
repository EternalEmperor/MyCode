//埋点 
function collector(){
	//以日期的格式输出
	var start = new Date();  
	var strStart = start.getFullYear()+"-"+(start.getMonth()+1)+"-"+start.getDate()+"\t"+  
	                start.getHours()+":"+start.getMinutes()+":"+start.getSeconds();  
	
  //伪装成一张图片 
   var img = new Image();  
   img.src = "http://192.168.233.134/log.gif?date=" + strStart;
	
	
}

