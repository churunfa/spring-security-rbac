import{_ as e,a}from"./img.a1e1e0f8.js";import{d as l,p as t,b as s,r as o,o as r,c as p,i as n,w as c,j as u}from"./vendor.47dfe678.js";const d={name:"upload",components:{VueCropper:e},setup(){const e=l(""),t=l(a),s=l(!1),o=l(null);return{cropper:o,imgSrc:e,cropImg:t,dialogVisible:s,setImage:a=>{const l=a.target.files[0];if(!l.type.includes("image/"))return;const t=new FileReader;t.onload=a=>{s.value=!0,e.value=a.target.result,o.value&&o.value.replace(a.target.result)},t.readAsDataURL(l)},cropImage:()=>{t.value=o.value.getCroppedCanvas().toDataURL()},cancelCrop:()=>{s.value=!1,t.value=a}}}},i=c();t("data-v-57383ee2");const m={class:"crumbs"},v=n("i",{class:"el-icon-lx-calendar"},null,-1),g=u(" 表单 "),f=u("图片上传"),_={class:"container"},b=n("div",{class:"content-title"},"支持拖拽",-1),j=n("div",{class:"plugins-tips"},[u(" Element UI自带上传组件。 访问地址： "),n("a",{href:"http://element.eleme.io/#/zh-CN/component/upload",target:"_blank"},"Element UI Upload")],-1),h=n("i",{class:"el-icon-upload"},null,-1),I=n("div",{class:"el-upload__text"},[u(" 将文件拖到此处，或 "),n("em",null,"点击上传")],-1),C=n("div",{class:"el-upload__tip"},"只能上传 jpg/png 文件，且不超过 500kb",-1),U=n("div",{class:"content-title"},"支持裁剪",-1),k=n("div",{class:"plugins-tips"},[u(" vue-cropperjs：一个封装了 cropperjs 的 Vue 组件。 访问地址： "),n("a",{href:"https://github.com/Agontuk/vue-cropperjs",target:"_blank"},"vue-cropperjs")],-1);s();const x=i(((e,a,l,t,s,c)=>{const u=o("el-breadcrumb-item"),d=o("el-breadcrumb"),x=o("el-upload");return r(),p("div",null,[n("div",m,[n(d,{separator:"/"},{default:i((()=>[n(u,null,{default:i((()=>[v,g])),_:1}),n(u,null,{default:i((()=>[f])),_:1})])),_:1})]),n("div",_,[b,j,n(x,{class:"upload-demo",drag:"",action:"http://jsonplaceholder.typicode.com/api/posts/",multiple:""},{tip:i((()=>[C])),default:i((()=>[h,I])),_:1}),U,k])])}));d.render=x,d.__scopeId="data-v-57383ee2";export default d;
