package com.ai.weixin.service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.springframework.stereotype.Component;

/** 
 * @author tanzx
 * @date 2015年11月10日 上午11:26:32 
 */

@Component
public class MessageRouter {
	
	@Resource
	private WxMpMessageRouter wxMpMessageRouter;
	
	@Resource
	private WxMessageHandler wxMessageHandler;
	
	@Resource
	private SubscribeHandler subscribeHandler;
	
	@PostConstruct
	public void init() {
		wxMpMessageRouter
		
		//关注事件处理器
		.rule()
		.async(false)
	    .msgType(WxConsts.XML_MSG_EVENT)
	    .event(WxConsts.EVT_SUBSCRIBE)
	    .handler(subscribeHandler)
	    .end()
		
	    // 拦截内容包含“哈哈”的消息
		.rule()
        .async(false)
        .rContent(".*哈哈.*") 
        .handler(wxMessageHandler)
        .end();
	}
	
	public WxMpXmlOutMessage route(final WxMpXmlMessage wxMessage) {
		return wxMpMessageRouter.route(wxMessage);
	}
}
