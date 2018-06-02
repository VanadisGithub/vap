package com.vanadis.vap.controller.life;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.RegexUtils;
import com.vanadis.vap.utils.ResultUtils;
import com.vanadis.vap.weixin.*;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("weixin")
public class WeixinController extends BaseController {

    public static final ArrayList<String> API_SPECIAL_USER = new ArrayList<String>(Arrays.asList("filehelper", "weibo",
            "qqmail", "fmessage", "tmessage", "qmessage", "qqsync", "floatbottle", "lbsapp", "shakeapp", "medianote",
            "qqfriend", "readerapp", "blogapp", "facebookapp", "masssendapp", "meishiapp", "feedsapp", "voip",
            "blogappweixin", "brandsessionholder", "weixin", "weixinreminder", "officialaccounts", "wxitil",
            "notification_messages", "wxid_novlwrv3lqwv11", "gh_22b87fa7cb3c", "userexperience_alarm"));

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/weixin");
        return modelAndView;
    }

    @RequestMapping("getQrCode")
    public Result getQrCode() {
        String qrPath = "qrCode/weixinQRCode.png"; // 保存登陆二维码图片的路径
        System.setProperty("jsse.enableSNIExtension", "false"); // 重要：防止SSL错误

        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(UUIDParaEnum.APP_ID.para(), UUIDParaEnum.APP_ID.value()));
        params.add(new BasicNameValuePair(UUIDParaEnum.FUN.para(), UUIDParaEnum.FUN.value()));
        params.add(new BasicNameValuePair(UUIDParaEnum.LANG.para(), UUIDParaEnum.LANG.value()));
        params.add(new BasicNameValuePair(UUIDParaEnum.TIMESTAMP_.para(), String.valueOf(System.currentTimeMillis())));

        String paramStr = null;
        try {
            paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String uuidStr = HttpUtils.doGet(URLEnum.UUID_URL.getUrl() + "?" + paramStr, null, null);
        String regEx = "window.QRLogin.code = (\\d+); window.QRLogin.uuid = \"(\\S+?)\";";
        Matcher matcher = Pattern.compile(regEx).matcher(uuidStr);
        if (matcher.find()) {
            if ("200".equals(matcher.group(1))) {
                String uuid = matcher.group(2);
                String qrUrl = URLEnum.QRCODE_URL.getUrl() + uuid;
                HttpUtils.saveFile(qrUrl, qrPath);
                return ResultUtils.success(uuid);
            }
        }
        return ResultUtils.error(1, "获取登录二维码失败！");
    }

    @RequestMapping("loginWeiXin")
    public Result loginWeiXin(String uuId) {
        long millis = System.currentTimeMillis();
        List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        params.add(new BasicNameValuePair(LoginParaEnum.LOGIN_ICON.para(), LoginParaEnum.LOGIN_ICON.value()));
        params.add(new BasicNameValuePair(LoginParaEnum.UUID.para(), uuId));
        params.add(new BasicNameValuePair(LoginParaEnum.TIP.para(), LoginParaEnum.TIP.value()));
        params.add(new BasicNameValuePair(LoginParaEnum.R.para(), String.valueOf(millis / 1579L)));
        params.add(new BasicNameValuePair(LoginParaEnum._.para(), String.valueOf(millis)));
        String paramStr = null;
        try {
            paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
        String loginResult = HttpUtils.doGet(URLEnum.LOGIN_URL.getUrl() + "?" + paramStr, null, null);
        String status = RegexUtils.getSubUtilSimple(loginResult, "window.code=(\\d+)");
        if ("200".equals(status)) {
            Core core = Core.getInstance();
            core = processLoginInfo(core, loginResult);
            core = webWxInit(core);
            core = webWxGetContact(core);
            return ResultUtils.success(core);
        } else {
            return ResultUtils.error(1, "未扫码二维码！");
        }
    }

    /**
     * 处理登陆信息
     */
    private Core processLoginInfo(Core core, String loginContent) {
        String regEx = "window.redirect_uri=\"(\\S+)\";";
        Matcher matcher = Pattern.compile(regEx).matcher(loginContent);
        if (matcher.find()) {
            String originalUrl = matcher.group(1);
            String url = originalUrl.substring(0, originalUrl.lastIndexOf('/')); // https://wx2.qq.com/cgi-bin/mmwebwx-bin
            core.getLoginInfo().put("url", url);
            Map<String, List<String>> possibleUrlMap = this.getPossibleUrlMap();
            Iterator<Map.Entry<String, List<String>>> iterator = possibleUrlMap.entrySet().iterator();
            Map.Entry<String, List<String>> entry;
            String fileUrl;
            String syncUrl;
            while (iterator.hasNext()) {
                entry = iterator.next();
                String indexUrl = entry.getKey();
                fileUrl = "https://" + entry.getValue().get(0) + "/cgi-bin/mmwebwx-bin";
                syncUrl = "https://" + entry.getValue().get(1) + "/cgi-bin/mmwebwx-bin";
                if (core.getLoginInfo().get("url").toString().contains(indexUrl)) {
                    core.setIndexUrl(indexUrl);
                    core.getLoginInfo().put("fileUrl", fileUrl);
                    core.getLoginInfo().put("syncUrl", syncUrl);
                    break;
                }
            }
            if (core.getLoginInfo().get("fileUrl") == null && core.getLoginInfo().get("syncUrl") == null) {
                core.getLoginInfo().put("fileUrl", url);
                core.getLoginInfo().put("syncUrl", url);
            }
            core.getLoginInfo().put("deviceid", "e" + String.valueOf(new Random().nextLong()).substring(1, 16)); // 生成15位随机数
            core.getLoginInfo().put("BaseRequest", new ArrayList<String>());
            String text = HttpUtils.doGet(originalUrl, null, null);
            //add by 默非默 2017-08-01 22:28:09
            //如果登录被禁止时，则登录返回的message内容不为空，下面代码则判断登录内容是否为空，不为空则退出程序
//            String msg = getLoginMessage(text);
            Document doc = xmlParser(text);
            if (doc != null) {
                core.getLoginInfo().put(StorageLoginInfoEnum.skey.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.skey.getKey()).item(0).getFirstChild()
                                .getNodeValue());
                core.getLoginInfo().put(StorageLoginInfoEnum.wxsid.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.wxsid.getKey()).item(0).getFirstChild()
                                .getNodeValue());
                core.getLoginInfo().put(StorageLoginInfoEnum.wxuin.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.wxuin.getKey()).item(0).getFirstChild()
                                .getNodeValue());
                core.getLoginInfo().put(StorageLoginInfoEnum.pass_ticket.getKey(),
                        doc.getElementsByTagName(StorageLoginInfoEnum.pass_ticket.getKey()).item(0).getFirstChild()
                                .getNodeValue());
            }

        }
        return core;
    }

    public Core webWxInit(Core core) {
        core.setAlive(true);
        core.setLastNormalRetcodeTime(System.currentTimeMillis());
        // 组装请求URL和参数
        String url = String.format(URLEnum.INIT_URL.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()),
                String.valueOf(System.currentTimeMillis() / 3158L),
                core.getLoginInfo().get(StorageLoginInfoEnum.pass_ticket.getKey()));

        Map<String, Object> paramMap = core.getParamMap(core.getLoginInfo());

        // 请求初始化接口
        try {
            String result = HttpUtils.doPost(url, JSON.toJSONString(paramMap), null, null);
            JSONObject obj = JSON.parseObject(result);

            JSONObject user = obj.getJSONObject(StorageLoginInfoEnum.User.getKey());
            JSONObject syncKey = obj.getJSONObject(StorageLoginInfoEnum.SyncKey.getKey());

            core.getLoginInfo().put(StorageLoginInfoEnum.InviteStartCount.getKey(),
                    obj.getInteger(StorageLoginInfoEnum.InviteStartCount.getKey()));
            core.getLoginInfo().put(StorageLoginInfoEnum.SyncKey.getKey(), syncKey);

            JSONArray syncArray = syncKey.getJSONArray("List");
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < syncArray.size(); i++) {
                sb.append(syncArray.getJSONObject(i).getString("Key") + "_"
                        + syncArray.getJSONObject(i).getString("Val") + "|");
            }
            // 1_661706053|2_661706420|3_661706415|1000_1494151022|
            String synckey = sb.toString();

            // 1_661706053|2_661706420|3_661706415|1000_1494151022
            core.getLoginInfo().put(StorageLoginInfoEnum.synckey.getKey(), synckey.substring(0, synckey.length() - 1));// 1_656161336|2_656161626|3_656161313|11_656159955|13_656120033|201_1492273724|1000_1492265953|1001_1492250432|1004_1491805192
            core.setUserName(user.getString("UserName"));
            core.setNickName(user.getString("NickName"));
            core.setUserSelf(obj.getJSONObject("User"));

            String chatSet = obj.getString("ChatSet");
            String[] chatSetArray = chatSet.split(",");
            for (int i = 0; i < chatSetArray.length; i++) {
                if (chatSetArray[i].indexOf("@@") != -1) {
                    // 更新GroupIdList
                    core.getGroupIdList().add(chatSetArray[i]); //
                }
            }
            // JSONArray contactListArray = obj.getJSONArray("ContactList");
            // for (int i = 0; i < contactListArray.size(); i++) {
            // JSONObject o = contactListArray.getJSONObject(i);
            // if (o.getString("UserName").indexOf("@@") != -1) {
            // core.getGroupIdList().add(o.getString("UserName")); //
            // // 更新GroupIdList
            // core.getGroupList().add(o); // 更新GroupList
            // core.getGroupNickNameList().add(o.getString("NickName"));
            // }
            // }
        } catch (Exception e) {
            e.printStackTrace();
            return core;
        }
        return core;
    }

    public Core webWxGetContact(Core core) {
        String url = String.format(URLEnum.WEB_WX_GET_CONTACT.getUrl(),
                core.getLoginInfo().get(StorageLoginInfoEnum.url.getKey()));
        Map<String, Object> paramMap = core.getParamMap(core.getLoginInfo());
        try {
            String result = HttpUtils.doPost(url, JSON.toJSONString(paramMap), null, null);
            JSONObject fullFriendsJsonList = JSON.parseObject(result);
            // 查看seq是否为0，0表示好友列表已全部获取完毕，若大于0，则表示好友列表未获取完毕，当前的字节数（断点续传）
            long seq = 0;
            long currentTime = 0L;
            List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
            if (fullFriendsJsonList.get("Seq") != null) {
                seq = fullFriendsJsonList.getLong("Seq");
                currentTime = new Date().getTime();
            }
            core.setMemberCount(fullFriendsJsonList.getInteger(StorageLoginInfoEnum.MemberCount.getKey()));
            JSONArray member = fullFriendsJsonList.getJSONArray(StorageLoginInfoEnum.MemberList.getKey());
            // 循环获取seq直到为0，即获取全部好友列表 ==0：好友获取完毕 >0：好友未获取完毕，此时seq为已获取的字节数
            while (seq > 0) {
                // 设置seq传参
                params.add(new BasicNameValuePair("r", String.valueOf(currentTime)));
                params.add(new BasicNameValuePair("seq", String.valueOf(seq)));

                String paramStr = null;
                try {
                    paramStr = EntityUtils.toString(new UrlEncodedFormEntity(params, Consts.UTF_8));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                result = HttpUtils.doGet(url + "?" + paramStr, null, null);

                params.remove(new BasicNameValuePair("r", String.valueOf(currentTime)));
                params.remove(new BasicNameValuePair("seq", String.valueOf(seq)));


                fullFriendsJsonList = JSON.parseObject(result);

                if (fullFriendsJsonList.get("Seq") != null) {
                    seq = fullFriendsJsonList.getLong("Seq");
                    currentTime = new Date().getTime();
                }

                // 累加好友列表
                member.addAll(fullFriendsJsonList.getJSONArray(StorageLoginInfoEnum.MemberList.getKey()));
            }
            core.setMemberCount(member.size());
            for (Iterator<?> iterator = member.iterator(); iterator.hasNext(); ) {
                JSONObject o = (JSONObject) iterator.next();
                if ((o.getInteger("VerifyFlag") & 8) != 0) { // 公众号/服务号
                    core.getPublicUsersList().add(o);
                } else if (API_SPECIAL_USER.contains(o.getString("UserName"))) { // 特殊账号
                    core.getSpecialUsersList().add(o);
                } else if (o.getString("UserName").indexOf("@@") != -1) { // 群聊
                    if (!core.getGroupIdList().contains(o.getString("UserName"))) {
                        core.getGroupNickNameList().add(o.getString("NickName"));
                        core.getGroupIdList().add(o.getString("UserName"));
                        core.getGroupList().add(o);
                    }
                } else if (o.getString("UserName").equals(core.getUserSelf().getString("UserName"))) { // 自己
                    core.getContactList().remove(o);
                } else { // 普通联系人
                    core.getContactList().add(o);
                }
            }
            return core;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return core;
    }

    private Map<String, List<String>> getPossibleUrlMap() {
        Map<String, List<String>> possibleUrlMap = new HashMap<String, List<String>>();
        possibleUrlMap.put("wx.qq.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.wx.qq.com");
                add("webpush.wx.qq.com");
            }
        });

        possibleUrlMap.put("wx2.qq.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.wx2.qq.com");
                add("webpush.wx2.qq.com");
            }
        });
        possibleUrlMap.put("wx8.qq.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.wx8.qq.com");
                add("webpush.wx8.qq.com");
            }
        });

        possibleUrlMap.put("web2.wechat.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.web2.wechat.com");
                add("webpush.web2.wechat.com");
            }
        });
        possibleUrlMap.put("wechat.com", new ArrayList<String>() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            {
                add("file.web.wechat.com");
                add("webpush.web.wechat.com");
            }
        });
        return possibleUrlMap;
    }

    /**
     * 解析登录返回的消息，如果成功登录，则message为空
     *
     * @param result
     * @return
     */
    public String getLoginMessage(String result) {
        String[] strArr = result.split("<message>");
        String[] rs = strArr[1].split("</message>");
        if (rs != null && rs.length > 1) {
            return rs[0];
        }
        return "";
    }

    /**
     * xml解析器
     *
     * @param text
     * @return
     * @author https://github.com/yaphone
     * @date 2017年4月9日 下午6:24:25
     */
    public static Document xmlParser(String text) {
        Document doc = null;
        StringReader sr = new StringReader(text);
        InputSource is = new InputSource(sr);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doc;
    }
}
