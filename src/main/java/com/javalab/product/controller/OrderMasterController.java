package com.javalab.product.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.javalab.product.dto.OrderItemDTO;
import com.javalab.product.dto.OrderMasterDTO;
import com.javalab.product.dto.PageRequestDTO;
import com.javalab.product.dto.PageResultDTO;
import com.javalab.product.entity.OrderMaster;
import com.javalab.product.service.OrderMasterService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/order")
@Slf4j
public class OrderMasterController {
	private final OrderMasterService orderMasterService;

	public OrderMasterController(OrderMasterService orderMasterService) {
		this.orderMasterService = orderMasterService;
	}

	@GetMapping("/list")
	public void getList(PageRequestDTO pageRequestDTO, Model model) {
		PageResultDTO<OrderMasterDTO, OrderMaster> result = orderMasterService.getList(pageRequestDTO);
		model.addAttribute("result", result);
	}

	@GetMapping("/read")
	public void getOrderMasterById(@RequestParam Integer orderId, Model model) {
		log.info("getOrderMasterById");
		OrderMasterDTO dto = orderMasterService.read(orderId);
		model.addAttribute("orderMaster", dto);
	}

	@GetMapping("/register")
	public void registerForm(@ModelAttribute("orderMasterDTO") OrderMasterDTO orderMasterDTO,
			BindingResult bindingResult, PageRequestDTO pageRequestDTO, Model model) {
		model.addAttribute("orderMaster", new OrderMaster());
	}

	@PostMapping("/save")
	public String saveOrder(@RequestBody @Valid OrderMasterDTO orderMasterDTO, BindingResult bindingResult) {
	    if (bindingResult.hasErrors()) {
	    }
	    
	    orderMasterService.saveOrderMaster(orderMasterDTO); // Save OrderMaster and OrderItems
	    
	    // Return a response or redirect to a success page
	    return "redirect:/order/success";
	}
	
	@GetMapping("/modify")
	public void modify(@RequestParam("orderId") Integer orderId,
			@ModelAttribute("orderMasterDTO") OrderMasterDTO orderMasterDTO, BindingResult bindingResult, Model model) {
		log.info("modify - get");

		OrderMasterDTO dto = orderMasterService.read(orderId);
		model.addAttribute("orderMaster", dto);
	}

	@PostMapping("/modify")
	public String modify(@ModelAttribute @Valid OrderMasterDTO orderMasterDTO, BindingResult bindingResult,
			Model model) {
		log.info("modify - post dto: " + orderMasterDTO.toString());

		if (bindingResult.hasErrors()) {
			List<FieldError> fieldErrors = bindingResult.getFieldErrors();
			for (FieldError error : fieldErrors) {
				log.error(error.getField() + " " + error.getDefaultMessage());
			}

			model.addAttribute("orderMaster", orderMasterDTO);
			return "order/modify";
		}

		orderMasterService.modify(orderMasterDTO);

		return "redirect:/order/list";
	}

}
