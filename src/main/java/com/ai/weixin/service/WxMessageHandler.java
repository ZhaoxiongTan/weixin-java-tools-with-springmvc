package com.ai.weixin.service;

import java.util.Map;

import org.springframework.stereotype.Component;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.common.session.WxSessionManager;
import me.chanjar.weixin.mp.api.WxMpMessageHandler;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutTextMessage;

/** 
 * @author tanzx
 * @date 2015年11月10日 上午11:38:19 
 */
@Component
public class WxMessageHandler implements WxMpMessageHandler {

	@Override
	public WxMpXmlOutMessage handle(WxMpXmlMessage wxMessage,
			Map<String, Object> context, WxMpService wxMpService,
			WxSessionManager sessionManager) throws WxErrorException {
		
		WxMpXmlOutTextMessage m= WxMpXmlOutMessage.TEXT()
				.content("测试加密消息")
				.fromUser(wxMessage.getToUserName())
				.toUser(wxMessage.getFromUserName())
				.build();
		return m;
	}

}
