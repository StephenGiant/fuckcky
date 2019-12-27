package com.nsk.app.wxapi;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Xml;
import android.widget.Toast;

import com.blankj.utilcode.util.ToastUtils;
import com.nsk.cky.ckylibrary.UserConstants;
import com.nsk.cky.ckylibrary.utils.MD5;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class WXProducts
{
    Context _Context = null;

    PayReq req;
    IWXAPI msgApi = null;
    Map<String, String> resultunifiedorder;

    private int payMoney = 0;
    private String describe = "";

    public WXProducts(Context context)
    {
        if (context == null)
            return;
        _Context = context;
        msgApi = WXAPIFactory.createWXAPI(_Context, null);

    }

    /**
     * 从服务器获取的订单号
     */
    private String outTradNo = "";

    //应用id appid；商户号 mch_id，商户订单号	out_trade_no 总金额	total_fee
    public void startPay(float money,  String outTradNo)
    {

        if (!isWXAppInstalledAndSupported(_Context, msgApi))
        {
            return;
        }

        describe = "财康育支付支付金额";

        this.outTradNo = outTradNo;

        if (this.outTradNo == null || this.outTradNo.trim().length() == 0)
        {
            Toast.makeText(_Context, "订单号不能为空,请重新下单", Toast.LENGTH_SHORT).show();
            return;
        }

        if (money <= 0)
        {
            Toast.makeText(_Context, "付款金额不允许为0元", Toast.LENGTH_SHORT).show();
            return;
        }

        BigDecimal bd = new BigDecimal(String.valueOf(money));
        if (bd.scale() > 2)
        {
            Toast.makeText(_Context, "付款金额不允许为小于分", Toast.LENGTH_SHORT).show();
            return;
        }
        req = new PayReq();
        BigDecimal b1 = new BigDecimal((money + ""));
        BigDecimal b2 = new BigDecimal("100");
        b1 = b1.multiply(b2);

        payMoney = b1.intValue();

        // /Toast.makeText(_Context, "微信即将支付 " + money + "元", Toast.LENGTH_SHORT).show();
        // payMoney = 1;
        if (msgApi == null) {
            return;
        }

        msgApi.registerApp(UserConstants.appid);

        // 第一步生成预支付订单
        GetPrepayIdTask getPrepayId = new GetPrepayIdTask();
        getPrepayId.execute();
    }

    /**
     *  获取sign
     */
    @SuppressLint("DefaultLocale")
    private String genPackageSign(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++)
        {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append("E6CF121F5F5F8AEF8DB5A198F98AF6EQ");

        String packageSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
//        Random random = new Random();
//        String messageDigest = MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
        return  packageSign;
    }

    @SuppressLint("DefaultLocale")
    private String genAppSign(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < params.size(); i++)
        {
            sb.append(params.get(i).getName());
            sb.append('=');
            sb.append(params.get(i).getValue());
            sb.append('&');
        }
        sb.append("key=");
        sb.append("E6CF121F5F5F8AEF8DB5A198F98AF6EQ");
        String appSign = MD5.getMessageDigest(sb.toString().getBytes()).toUpperCase();
        return appSign;
    }

    private String toXml(List<NameValuePair> params)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("<xml>");
        for (int i = 0; i < params.size(); i++)
        {
            sb.append("<" + params.get(i).getName() + ">");

            sb.append(params.get(i).getValue());
            sb.append("</" + params.get(i).getName() + ">");
        }
        sb.append("</xml>");

        return sb.toString();
    }
    //异步加载
    private class GetPrepayIdTask extends AsyncTask<Void, Void, Map<String, String>>
    {


        @Override
        protected void onPreExecute()
        {
        }

        @Override
        protected void onPostExecute(Map<String, String> result)
        {

            resultunifiedorder = result;
            String prepay_id = resultunifiedorder.get("prepay_id");

            // Toast.makeText(_Context, result.toString(), Toast.LENGTH_SHORT).show();
            // String value = "第一步生成参数" + "prepay_id----> " + result.get("prepay_id");
            // Log.e("-----> orion", value);

            // Toast.makeText(_Context, value, Toast.LENGTH_SHORT).show();

            genPayReq();
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
        }

        @Override
        protected Map<String, String> doInBackground(Void... params)
        {

            String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
            String entity = genProductArgs(payMoney, describe);



            String content = getXml(url, entity);
            Map<String, String> xml = decodeXml(content);

            return xml;
        }
    }
    String str;
    //请求网络
    public String getXml(final String url, final String entity){
        try
        {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[] { new MyTrustManager() },
                    new SecureRandom());
            HttpsURLConnection
                    .setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection
                    .setDefaultHostnameVerifier(new MyHostnameVerifier());

            // 创建连接
            byte[] xmlbyte = entity.getBytes("UTF-8");
            URL path = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) path.openConnection();

            conn.setConnectTimeout(2*1000);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(xmlbyte.length));
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");
            conn.getOutputStream().write(xmlbyte);
            conn.getOutputStream().flush();
            conn.getOutputStream().close();
            if (conn.getResponseCode() != 200)
            {
                throw new RuntimeException("error");
            }
            else {
                InputStream is = conn.getInputStream();// get is
                // output data
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                byte[] buf = new byte[1024];
                int len;
                while ((len = is.read(buf)) != -1)
                {
                    out.write(buf, 0, len);
                }
                str = out.toString("UTF-8");
                out.close();

            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return str;
    }
    private Map<String, String> decodeXml(String content)
    {
        try
        {
            Map<String, String> xml = new HashMap<String, String>();
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(content));
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT)
            {

                String nodeName = parser.getName();
                switch (event)
                {
                    case XmlPullParser.START_DOCUMENT:

                        break;
                    case XmlPullParser.START_TAG:

                        if ("xml".equals(nodeName) == false)
                        {
                            xml.put(nodeName, parser.nextText());
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                event = parser.next();
            }

            return xml;
        }
        catch (Exception e)
        {
        }
        return null;
    }
    //获取NonceStr
    private String genNonceStr()
    {
        Random random = new Random();
        String messageDigest = MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
        return  messageDigest;
    }
    //获取timestamp
    private long genTimeStamp()
    {
        return System.currentTimeMillis() / 1000;
    }

    // private String genOutTradNo()
    // {
    // Random random = new Random();
    // return MD5.getMessageDigest(String.valueOf(random.nextInt(10000)).getBytes());
    // }

    /**
     *
     * @param money
     * @param describe
     *            该字段长度不允许超过32
     * @return
     */
    private String genProductArgs(int money, String describe)
    {
        StringBuffer xml = new StringBuffer();

        try
        {
            String nonceStr = genNonceStr();

            if (describe.length() > 32)
                describe = describe.substring(0, 27) + "...";

            xml.append("</xml>");
            List<NameValuePair> packageParams = new LinkedList<NameValuePair>();
            packageParams.add(new BasicNameValuePair("appid",UserConstants.appid));
            packageParams.add(new BasicNameValuePair("body", describe));
            packageParams.add(new BasicNameValuePair("mch_id", "1513891351"));
            packageParams.add(new BasicNameValuePair("nonce_str", nonceStr));

            packageParams.add(new BasicNameValuePair("notify_url","http://mtapi.aicky.cn/Pay/WeiXin/notify_url.aspx"));// 更新服务器回调地址

            packageParams.add(new BasicNameValuePair("out_trade_no", outTradNo));
            packageParams.add(new BasicNameValuePair("spbill_create_ip", "127.0.0.1"));
            packageParams.add(new BasicNameValuePair("total_fee", money + ""));// 更新总商品价格 x100
            packageParams.add(new BasicNameValuePair("trade_type", "APP"));

            String sign = genPackageSign(packageParams);
            packageParams.add(new BasicNameValuePair("sign", sign));

            String xmlstring = toXml(packageParams);
            //	return new String(xmlstring.toString().getBytes(), "ISO8859-1");
            return xmlstring;
        }
        catch (Exception e)
        {
            //	Log.e("e.getMessage()", "genProductArgs fail, ex = " + e.getMessage());
            return null;
        }

    }

    private void genPayReq()
    {

        req.appId = UserConstants.appid;
        req.partnerId ="1513891351";
        req.prepayId = resultunifiedorder.get("prepay_id");
        req.packageValue = "Sign=WXPay";
        req.nonceStr = genNonceStr();
        req.timeStamp = String.valueOf(genTimeStamp());

        List<NameValuePair> signParams = new LinkedList<NameValuePair>();
        signParams.add(new BasicNameValuePair("appid", req.appId));
        signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
        signParams.add(new BasicNameValuePair("package", req.packageValue));
        signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
        signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
        signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));

        req.sign = genAppSign(signParams);
        // String value="第二步生成参数"+ "sign----> " + req.sign + " 正在调用支付功能";
        //
        // Log.e("-----> orion",value);

        // Toast.makeText(_Context, value, Toast.LENGTH_SHORT).show();
        // //调起微信支付
        sendPayReq();
        // //Log.e("orion", signParams.toString());

    }

    private void sendPayReq()
    {

        msgApi.registerApp(UserConstants.appid);
        msgApi.sendReq(req);
    }

    /**
     * 判断微信是否安装
     *
     * @param context
     * @param api
     * @return
     */
    private boolean isWXAppInstalledAndSupported(Context context, IWXAPI api)
    {
        boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() ;
        if (sIsWXAppInstalledAndSupported != true)
            ToastUtils.showLong("未安装微信客户端,不能进行微信支付");
            //Toast.makeText(_Context, "微信客户端未安装", Toast.LENGTH_SHORT).show();

        return sIsWXAppInstalledAndSupported;
    }



    private class MyHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // TODO Auto-generated method stub
            return true;
        }

    }

    private class MyTrustManager implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType)

                throws CertificateException {
            // TODO Auto-generated method stub
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            // TODO Auto-generated method stub
            return null;
        }

    }
}
