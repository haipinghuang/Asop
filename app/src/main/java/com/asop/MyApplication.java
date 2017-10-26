package com.asop;

import com.tencent.tinker.loader.app.TinkerApplication;
import com.tencent.tinker.loader.shareutil.ShareConstants;

/**
 * Created by 黄海 on 2017/10/26.
 */

public class MyApplication extends TinkerApplication {
    protected MyApplication() {
        super(ShareConstants.TINKER_ENABLE_ALL, "com.asop.MyApplicationLike",
                "com.tencent.tinker.loader.TinkerLoader", false);
    }
}
