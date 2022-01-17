package com.iot.controller;

import java.security.Principal;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.iot.service.IRoleService;

@Controller
public class WebController {
	//@Autowired
	///private MqttPahoMessageHandler mqtt;
	
	@Autowired
	private IRoleService roleService;
	
	
	@RequestMapping(value = "/trang-chu", method = RequestMethod.GET)
	public ModelAndView getHomePage() throws MqttPersistenceException, MqttException {
		//String data = "Hế lo các bạn nhé !!!";
		//Message<String> message = MessageBuilder.withPayload(data).setHeader(MqttHeaders.TOPIC, "demotopic").build();
		//mqtt.handleMessage(message);
		return new ModelAndView("web/home");
	}

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView loginPage() {
		ModelAndView mav = new ModelAndView("web/login");
		return mav;
	}

	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView profilePage(Model model, Principal principal) {
		model.addAttribute("message", "Hi " + principal.getName() + "<br> Information page");
		ModelAndView mav = new ModelAndView("web/profile");
		return mav;
	}

	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ModelAndView registerPage(Model model) {
		ModelAndView mav = new ModelAndView("web/register");
		return mav;
	}
	
	@RequestMapping(value = "/accessDenied", method = RequestMethod.GET)
	public String accessDenied(Model model, Principal principal) {

		if (principal != null) {
			model.addAttribute("message",
					"Hi " + principal.getName() + "<br> You do not have permission to access this page!");
		} else {
			model.addAttribute("msg", "You do not have permission to access this page!");
		}
		return "web/403Page";
	}
}
