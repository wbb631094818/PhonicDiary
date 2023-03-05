package com.js.phonicdiary.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

import com.js.phonicdiary.R;

public class AppMarketUtils {

    //小米应用商店
    public static final String PACKAGE_MI_MARKET = "com.xiaomi.market";
    public static final String MI_MARKET_PAGE = "com.xiaomi.market.ui.AppDetailActivity";
    //魅族应用商店
    public static final String PACKAGE_MEIZU_MARKET = "com.meizu.mstore";
    public static final String MEIZU_MARKET_PAGE = "com.meizu.flyme.appcenter.activitys.AppMainActivity";
    //VIVO应用商店
    public static final String PACKAGE_VIVO_MARKET = "com.bbk.appstore";
    public static final String VIVO_MARKET_PAGE = "com.bbk.appstore.ui.AppStoreTabActivity";
    //OPPO应用商店
    public static final String PACKAGE_OPPO_MARKET = "com.oppo.market";
    public static final String OPPO_MARKET_PAGE = "a.a.a.aoz";
    //华为应用商店
    public static final String PACKAGE_HUAWEI_MARKET = "com.huawei.appmarket";
    public static final String HUAWEI_MARKET_PAGE = "com.huawei.appmarket.service.externalapi.view.ThirdApiActivity";
    //ZTE应用商店
    public static final String PACKAGE_ZTE_MARKET = "zte.com.market";
    public static final String ZTE_MARKET_PAGE = "zte.com.market.view.zte.drain.ZtDrainTrafficActivity";
    //360手机助手
    public static final String PACKAGE_360_MARKET = "com.qihoo.appstore";
    public static final String PACKAGE_360_PAGE = "com.qihoo.appstore.distribute.SearchDistributionActivity";
    //酷市场 -- 酷安网
    public static final String PACKAGE_COOL_MARKET = "com.coolapk.market";
    public static final String COOL_MARKET_PAGE = "com.coolapk.market.activity.AppViewActivity";
    //应用宝
    public static final String PACKAGE_TENCENT_MARKET = "com.tencent.android.qqdownloader";
    public static final String TENCENT_MARKET_PAGE = "com.tencent.pangu.link.LinkProxyActivity";
    //PP助手
    public static final String PACKAGE_ALI_MARKET = "com.pp.assistant";
    public static final String ALI_MARKET_PAGE = "com.pp.assistant.activity.MainActivity";
    //豌豆荚
    public static final String PACKAGE_WANDOUJIA_MARKET = "com.wandoujia.phoenix2";
    // 低版本可能是 com.wandoujia.jupiter.activity.DetailActivity
    public static final String WANDOUJIA_MARKET_PAGE = "com.pp.assistant.activity.PPMainActivity";
    //UCWEB
    public static final String PACKAGE_UCWEB_MARKET = "com.UCMobile";
    public static final String UCWEB_MARKET_PAGE = "com.pp.assistant.activity.PPMainActivity";


    // 进入应用市场详情页
    public static void gotoMarket(Context context) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse("market://details?id=" + AppUtils.getPackageName(context)));
        String[] keys = getKeys(context);
        if (keys != null) {
            intent.setClassName(keys[0], keys[1]);
        }
        //修复某些老手机会因为找不到任何市场而报错
        if (AppUtils.isIntentAvailable(context, intent)) {
            context.startActivity(intent);
        } else {
//            ToastUitls.show(context, context.getString(R.string.no_market));
        }
    }

    private static String[] getKeys(Context context) {
        String[] keys = new String[2];
        if (AppUtils.isPackageExist(context, PACKAGE_MI_MARKET)) {
            keys[0] = PACKAGE_MI_MARKET;
            keys[1] = MI_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_VIVO_MARKET)) {
            keys[0] = PACKAGE_VIVO_MARKET;
            keys[1] = VIVO_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_OPPO_MARKET)) {
            keys[0] = PACKAGE_OPPO_MARKET;
            keys[1] = OPPO_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_HUAWEI_MARKET)) {
            keys[0] = PACKAGE_HUAWEI_MARKET;
            keys[1] = HUAWEI_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_ZTE_MARKET)) {
            keys[0] = PACKAGE_ZTE_MARKET;
            keys[1] = ZTE_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_COOL_MARKET)) {
            keys[0] = PACKAGE_COOL_MARKET;
            keys[1] = COOL_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_360_MARKET)) {
            keys[0] = PACKAGE_360_MARKET;
            keys[1] = PACKAGE_360_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_MEIZU_MARKET)) {
            keys[0] = PACKAGE_MEIZU_MARKET;
            keys[1] = MEIZU_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_TENCENT_MARKET)) {
            keys[0] = PACKAGE_TENCENT_MARKET;
            keys[1] = TENCENT_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_ALI_MARKET)) {
            keys[0] = PACKAGE_ALI_MARKET;
            keys[1] = ALI_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_WANDOUJIA_MARKET)) {
            keys[0] = PACKAGE_WANDOUJIA_MARKET;
            keys[1] = WANDOUJIA_MARKET_PAGE;
        } else if (AppUtils.isPackageExist(context, PACKAGE_UCWEB_MARKET)) {
            keys[0] = PACKAGE_UCWEB_MARKET;
            keys[1] = UCWEB_MARKET_PAGE;
        }
        if (TextUtils.isEmpty(keys[0])) {
            return null;
        } else {
            return keys;
        }
    }
}
