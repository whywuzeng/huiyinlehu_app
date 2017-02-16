package com.huiyin.wxapi;

import android.content.Context;
import android.content.Intent;
import com.huiyin.ui.user.LoginActivity;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;

public class WXEntryActivity extends cn.bidaround.ytcore.wxapi.WXEntryActivity
		implements IWXAPIEventHandler {
	private Context mContext;

	@Override
	public void onReq(BaseReq req) {
		mContext = WXEntryActivity.this;
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			break;
		case ConstantsAPI.COMMAND_SENDAUTH:
			break;
		default:
			break;
		}
	}

	@Override
	public void onResp(BaseResp response) {
		mContext = WXEntryActivity.this;
		switch (response.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			if (response.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
				SendAuth.Resp tempResp = new SendAuth.Resp();
				tempResp = (SendAuth.Resp) response;
				Intent intent = new Intent(mContext, LoginActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.putExtra("code", tempResp.code);
				startActivity(intent);
			}
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;
		default:
			break;
		}
	}
}
