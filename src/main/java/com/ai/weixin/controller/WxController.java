package com.ai.weixin.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import me.chanjar.weixin.mp.api.WxMpConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.WxMpXmlOutMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.common.utils.lang.string.StringUtils;
import com.ai.weixin.service.MessageRouter;

/** 
 * @author tanzx
 * @date 2015年11月10日 上午11:42:04 
 */
@Controller
public class WxController {
	
	@Resource
	private WxMpConfigStorage wxMpConfigStorage;
	
	@Resource
	private WxMpService wxService;
	
	@Resource 
	private MessageRouter messageRouter;
	
	private Log log = LogFactory.getLog(getClass());
	
	@RequestMapping(value="/request")
	@ResponseBody
	public String weixinRequest(HttpServletRequest request) {
		
		log.debug("微信请求进入");
		
		log.debug(request.getRequestURI());
		log.debug(request.getQueryString());
		
		
		String signature = request.getParameter("signature");
	    String nonce = request.getParameter("nonce");
	    String timestamp = request.getParameter("timestamp");
	    String echostr = request.getParameter("echostr");
	    String encryptType = request.getParameter("encrypt_type");
	    
	    if (!wxService.checkSignature(timestamp, nonce, signature)) {
	      // 消息签名不正确，说明不是公众平台发过来的消息
	      return "非法请求";
	    }
	    
	    if (StringUtils.isNotBlank(echostr)) {
	        // 说明是一个仅仅用来验证的请求，回显echostr
	    	return echostr;
	    }
	    
	    encryptType = StringUtils.isBlank(encryptType) ? "raw" :encryptType;

	    try {
	    	if ("raw".equals(encryptType)) {
	            // 明文传输的消息
	            WxMpXmlMessage inMessage = WxMpXmlMessage.fromXml(request.getInputStream());
	            WxMpXmlOutMessage outMessage = messageRouter.route(inMessage);
	            
	            if (outMessage == null) {
					return "success";
				}
	            return outMessage.toXml();
	          }

	          if ("aes".equals(encryptType)) {
	            // 是aes加密的消息
	            String msgSignature = request.getParameter("msg_signature");
	            WxMpXmlMessage inMessage = WxMpXmlMessage.fromEncryptedXml(request.getInputStream(), wxMpConfigStorage, timestamp, nonce, msgSignature);
	            WxMpXmlOutMessage outMessage = messageRouter.route(inMessage);
	            return outMessage.toEncryptedXml(wxMpConfigStorage);
	          }
	          return "不可识别的加密类型";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
       
	}
}
