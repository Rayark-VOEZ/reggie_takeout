/* 自定义trim */
function trim (str) {  //删除左右两端的空格,自定义的trim()方法
  return str == undefined ? "" : str.replace(/(^\s*)|(\s*$)/g, "")
}

//获取url地址上面的参数  argname = id
function requestUrlParam(argname){
  var url = location.href       //eg.  localhost:8080/backend/page/member/add.html?id=1627524327769354242
  var arrStr = url.substring(url.indexOf("?")+1).split("&")  //id=1627524327769354242
  for(var i =0;i<arrStr.length;i++)
  {
      var loc = arrStr[i].indexOf(argname+"=")  //
      if(loc!=-1){
          return arrStr[i].replace(argname+"=","").replace("?","") //1627524327769354242
      }
  }
  return ""
}
